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
    val haptics: Boolean = true, val timeout: Int = 10, val page: Int = 0, val base: Int = 10
)

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
            o.optInt("timeout", 10), o.optInt("page"), o.optInt("base", 10))
    } catch (_: Exception) { Settings() }

    fun saveSettings(s: Settings) = save("settings", JSONObject().put("angle", s.angle).put("eng", s.eng).put("sig", s.sig)
        .put("mixed", s.mixed).put("haptics", s.haptics).put("timeout", s.timeout).put("page", s.page).put("base", s.base).toString())

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

    fun saveProgs(p: List<Prog>) = save("programs", JSONArray().also { a -> p.forEach { a.put(JSONObject().put("n", it.name).put("s", it.src)) } }.toString())
}
