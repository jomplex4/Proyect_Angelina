package com.digitalminds.angelina

import android.util.AtomicFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

data class Settings(
    val angle: Int = 1, val eng: Boolean = false, val sig: Int = 0, val mixed: Boolean = false,
    val haptics: Boolean = true, val timeout: Int = 10, val page: Int = 0, val base: Int = 10,
    /** Teclado: orden de páginas, páginas ocultas, página fijada (vacío = la última usada), Mis teclas y uso por página. */
    val order: List<String> = emptyList(), val hidden: Set<String> = emptySet(), val pinned: String = "",
    val myKeys: List<String> = emptyList(), val usage: Map<String, Int> = emptyMap(), val lastPage: String = ""
)

/** Progreso Beca 18: intentos, aciertos y si el último intento fue correcto, por problema; y los simulacros hechos. */
data class BRec(val tries: Int = 0, val ok: Int = 0, val last: Boolean = false)
data class SimRec(val time: Long, val score: Int, val total: Int, val secs: Int)
data class BecaStats(val rec: Map<String, BRec> = emptyMap(), val sims: List<SimRec> = emptyList())

data class Hist(val id: Long, val input: String, val exact: String?, val approx: String?, val error: String?, val dec: Boolean = false)
data class Prog(val name: String, val src: String)

/** JSON files in filesDir, debounced atomic writes. */
class Store(private val dir: File, private val scope: CoroutineScope) {
    private val jobs = HashMap<String, Job>()

    private fun file(n: String) = AtomicFile(File(dir, "$n.json"))

    private fun read(n: String): String? = try {
        val f = File(dir, "$n.json")
        if (f.exists()) file(n).readFully().toString(Charsets.UTF_8) else null
    } catch (_: Exception) { null }

    private fun save(n: String, text: String) {
        jobs[n]?.cancel()
        jobs[n] = scope.launch(Dispatchers.IO) {
            delay(400)
            val af = file(n)
            val os = af.startWrite()
            try { os.write(text.toByteArray(Charsets.UTF_8)); af.finishWrite(os) } catch (e: Exception) { af.failWrite(os) }
        }
    }

    fun loadSettings(): Settings = try {
        val o = JSONObject(read("settings") ?: "{}")
        Settings(o.optInt("angle", 1), o.optBoolean("eng"), o.optInt("sig"), o.optBoolean("mixed"), o.optBoolean("haptics", true),
            o.optInt("timeout", 10), o.optInt("page"), o.optInt("base", 10),
            strs(o.optJSONArray("order")), strs(o.optJSONArray("hidden")).toSet(), o.optString("pinned"),
            strs(o.optJSONArray("myKeys")), o.optJSONObject("usage")?.let { u -> u.keys().asSequence().associateWith { u.optInt(it) } } ?: emptyMap(),
            o.optString("lastPage"))
    } catch (_: Exception) { Settings() }

    fun saveSettings(s: Settings) = save("settings", JSONObject().put("angle", s.angle).put("eng", s.eng).put("sig", s.sig)
        .put("mixed", s.mixed).put("haptics", s.haptics).put("timeout", s.timeout).put("page", s.page).put("base", s.base)
        .put("order", JSONArray(s.order)).put("hidden", JSONArray(s.hidden.toList())).put("pinned", s.pinned)
        .put("myKeys", JSONArray(s.myKeys)).put("usage", JSONObject(s.usage as Map<*, *>)).put("lastPage", s.lastPage).toString())

    private fun strs(a: JSONArray?): List<String> = if (a == null) emptyList() else (0 until a.length()).map { a.optString(it) }.filter { it.isNotEmpty() }

    fun loadHistory(): List<Hist> = try {
        val a = JSONArray(read("history") ?: "[]")
        (0 until a.length()).map {
            val o = a.getJSONObject(it)
            Hist(o.getLong("id"), o.getString("i"), o.optString("x").ifEmpty { null }, o.optString("a").ifEmpty { null }, o.optString("e").ifEmpty { null })
        }
    } catch (_: Exception) { emptyList() }

    fun saveHistory(h: List<Hist>) = save("history", JSONArray().also { a ->
        h.takeLast(200).forEach { a.put(JSONObject().put("id", it.id).put("i", it.input).put("x", it.exact ?: "").put("a", it.approx ?: "").put("e", it.error ?: "")) }
    }.toString())

    fun loadDefs(): LinkedHashMap<String, String> = try {
        val a = JSONArray(read("vars") ?: "[]")
        LinkedHashMap<String, String>().also { m -> for (i in 0 until a.length()) a.getJSONArray(i).let { m[it.getString(0)] = it.getString(1) } }
    } catch (_: Exception) { LinkedHashMap() }

    fun saveDefs(d: Map<String, String>) = save("vars", JSONArray().also { a -> d.forEach { (k, v) -> a.put(JSONArray().put(k).put(v)) } }.toString())

    fun loadProgs(): List<Prog> = try {
        val a = JSONArray(read("programs") ?: "[]")
        (0 until a.length()).map { a.getJSONObject(it).let { o -> Prog(o.getString("n"), o.getString("s")) } }
    } catch (_: Exception) { emptyList() }

    fun loadBeca(): BecaStats = try {
        val o = JSONObject(read("beca") ?: "{}")
        val r = o.optJSONObject("p")
        val rec = HashMap<String, BRec>()
        if (r != null) for (k in r.keys()) r.getJSONArray(k).let { rec[k] = BRec(it.getInt(0), it.getInt(1), it.getBoolean(2)) }
        val sa = o.optJSONArray("s") ?: JSONArray()
        val sims = (0 until sa.length()).map { sa.getJSONArray(it).let { a -> SimRec(a.getLong(0), a.getInt(1), a.getInt(2), a.getInt(3)) } }
        BecaStats(rec, sims)
    } catch (_: Exception) { BecaStats() }

    fun saveBeca(b: BecaStats) = save("beca", JSONObject()
        .put("p", JSONObject().also { o -> b.rec.forEach { (k, v) -> o.put(k, JSONArray().put(v.tries).put(v.ok).put(v.last)) } })
        .put("s", JSONArray().also { a -> b.sims.takeLast(30).forEach { a.put(JSONArray().put(it.time).put(it.score).put(it.total).put(it.secs)) } })
        .toString())

    fun saveProgs(p: List<Prog>) = save("programs", JSONArray().also { a -> p.forEach { a.put(JSONObject().put("n", it.name).put("s", it.src)) } }.toString())
}
