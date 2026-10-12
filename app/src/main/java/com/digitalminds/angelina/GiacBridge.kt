package com.digitalminds.angelina

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.os.Process
import android.os.RemoteException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.withTimeoutOrNull
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.LinkedBlockingQueue

data class CasResult(val out: String? = null, val error: String? = null) {
    val ok get() = error == null
}

interface CasEngine {
    suspend fun eval(expr: String): CasResult
}

private const val MSG_EVAL = 1
private const val MSG_RESULT = 2
private const val MSG_KILL = 3
private const val MSG_REPLAY = 4

/**
 * UI-side client. Giac runs in the isolated ":cas" process (the JNI wrapper exposes no interrupt),
 * so a hung or crashed evaluation is stopped by killing that process; the system rebinds it and
 * stored definitions are replayed.
 */
class GiacClient(private val ctx: Context) : CasEngine {
    var timeoutMs: Long = 10_000
    private var svc: Messenger? = null
    private val pending = ConcurrentHashMap<Int, CompletableDeferred<CasResult>>()
    private var nextId = 1
    private var ready = CompletableDeferred<Unit>()
    private var defs: List<String> = emptyList()

    private val reply = Messenger(Handler(Looper.getMainLooper()) { m ->
        if (m.what == MSG_RESULT) {
            val d = m.data
            pending.remove(m.arg1)?.complete(CasResult(d.getString("o"), d.getString("e")))
        }
        true
    })

    private val conn = object : ServiceConnection {
        override fun onServiceConnected(n: ComponentName, b: IBinder) {
            svc = Messenger(b)
            sendReplay()
            ready.complete(Unit)
        }

        override fun onServiceDisconnected(n: ComponentName) {
            svc = null
            ready = CompletableDeferred()
            failAll("The math engine restarted. Try again.")
        }
    }

    fun bind() {
        ctx.bindService(Intent(ctx, CasService::class.java), conn, Context.BIND_AUTO_CREATE)
    }

    fun setDefinitions(list: List<String>) {
        defs = list
        sendReplay()
    }

    private fun sendReplay() {
        val s = svc ?: return
        if (defs.isEmpty()) return
        val m = Message.obtain(null, MSG_REPLAY)
        m.data = Bundle().apply { putStringArray("d", defs.toTypedArray()) }
        try { s.send(m) } catch (_: RemoteException) {}
    }

    suspend fun warmUp() {
        eval("1+1")
    }

    override suspend fun eval(expr: String): CasResult = evalFor(expr, timeoutMs)

    suspend fun evalFor(expr: String, ms: Long): CasResult {
        if (withTimeoutOrNull(5_000) { ready.await() } == null) return CasResult(error = "The math engine is not available.")
        val s = svc ?: return CasResult(error = "The math engine is not available.")
        val id = synchronized(this) { nextId++ }
        val d = CompletableDeferred<CasResult>()
        pending[id] = d
        val m = Message.obtain(null, MSG_EVAL, id, 0)
        m.data = Bundle().apply { putString("e", expr) }
        m.replyTo = reply
        try { s.send(m) } catch (e: RemoteException) { pending.remove(id); return CasResult(error = "The math engine is not available.") }
        val r = withTimeoutOrNull(ms) { d.await() }
        if (r == null) {
            pending.remove(id)
            kill()
            return CasResult(error = "Stopped after ${ms / 1000} s. The calculation was too long.")
        }
        return r
    }

    fun cancel() {
        kill()
        failAll("Calculation cancelled.")
    }

    private fun kill() {
        try { svc?.send(Message.obtain(null, MSG_KILL)) } catch (_: RemoteException) {}
    }

    private fun failAll(msg: String) {
        val it = pending.entries.iterator()
        while (it.hasNext()) { it.next().value.complete(CasResult(error = msg)); it.remove() }
    }
}

/** Runs in the ":cas" process. One dedicated 16 MB stack thread owns the Giac context. */
class CasService : Service() {
    private class Job(val id: Int, val expr: String?, val defs: Array<String>?, val to: Messenger?)

    private val queue = LinkedBlockingQueue<Job>()
    private var giacCtx = 0L
    private var initError: String? = null

    private val messenger = Messenger(Handler(Looper.getMainLooper()) { m ->
        when (m.what) {
            MSG_EVAL -> queue.put(Job(m.arg1, m.data.getString("e"), null, m.replyTo))
            MSG_REPLAY -> queue.put(Job(0, null, m.data.getStringArray("d"), null))
            MSG_KILL -> Process.killProcess(Process.myPid())
        }
        true
    })

    override fun onBind(i: Intent?): IBinder = messenger.binder

    override fun onCreate() {
        super.onCreate()
        Thread(null, { loop() }, "giac", 16L * 1024 * 1024).apply { isDaemon = true; start() }
    }

    private fun init() {
        if (giacCtx != 0L || initError != null) return
        try {
            giacCtx = javagiac.giacJNI.new_context__SWIG_0()
        } catch (t: Throwable) {
            initError = "The math engine is not installed. Build libs/giac-android.aar (see README)."
        }
    }

    private fun run(expr: String): CasResult {
        init()
        initError?.let { return CasResult(error = it) }
        return try {
            val g = javagiac.giacJNI.new_gen__SWIG_25(expr, giacCtx, null)
            val r = javagiac.giacJNI.gen_eval(g, null, 25, giacCtx, null)
            val s = javagiac.giacJNI.gen_print__SWIG_0(r, null, giacCtx, null)
            javagiac.giacJNI.delete_gen(r)
            javagiac.giacJNI.delete_gen(g)
            val i = s.indexOf("Error:")
            if (i >= 0) CasResult(error = s.substring(i + 6).trim()) else CasResult(out = s)
        } catch (t: Throwable) {
            CasResult(error = "Syntax error.")
        }
    }

    private fun loop() {
        while (true) {
            val j = queue.take()
            if (j.defs != null) { for (d in j.defs) run(d); continue }
            val r = run(j.expr ?: "")
            val m = Message.obtain(null, MSG_RESULT, j.id, 0)
            m.data = Bundle().apply { putString("o", r.out); putString("e", r.error) }
            try { j.to?.send(m) } catch (_: RemoteException) {}
        }
    }
}
