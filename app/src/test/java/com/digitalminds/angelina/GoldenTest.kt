package com.digitalminds.angelina

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class GoldenTest {
    companion object {
        fun lines(): List<List<String>> =
            GoldenTest::class.java.getResourceAsStream("/golden.txt")!!.bufferedReader().readLines().filter { it.isNotBlank() }.map { it.split("\t") }
    }

    private val math get() = lines().filter { it[0] != "NUM" }

    @Test fun hasAtLeastSixtyCases() = assertTrue(math.size >= 60)

    @Test fun coversEveryFeatureGroup() {
        val groups = math.map { it[0] }.toSet()
        listOf("basic", "roots", "logs", "trig", "discrete", "calculus", "solve", "algebra", "complex", "base", "units", "matrix", "mecha", "stats", "dist")
            .forEach { assertTrue("missing group $it", it in groups) }
    }

    @Test fun inputsParseAndSerializeIdempotently() {
        for (l in math) {
            val row = Parse.row(l[1])
            assertNotNull("parse ${l[1]}", row)
            val once = Ser.giac(row!!)
            val twice = Ser.giac(Parse.row(once)!!)
            assertEquals("idempotent ${l[1]}", once, twice)
        }
    }

    @Test fun expectedResultsAreRenderable() {
        for (l in math) assertNotNull("render ${l[2]}", Parse.row(l[2]))
    }

    @Test fun everyGuideExampleIsInGolden() {
        val inputs = math.map { it[1] }.toSet()
        for (g in Guide.entries) assertTrue("guide example not in golden: ${g.example}", g.example in inputs)
    }

    @Test fun requiredGuideTopicsExist() {
        val t = Guide.entries.joinToString(" ") { it.title.lowercase() + " " + it.tags }
        listOf("cube root", "nth root", "log base", "ln", "power", "fraction", "mixed", "exact", "definite", "indefinite", "derivative", "nth derivative",
            "partial", "limit", "sum", "product", "solve", "system", "inequalit", "taylor", "determinant", "inverse", "eigen", "linear system", "vector",
            "complex", "polar", "laplace", "inverse laplace", "differential", "unit", "base", "regression", "ans", "variable", "program", "zero", "minimum",
            "intersection", "ratio").forEach { assertTrue("guide topic $it", t.contains(it)) }
    }

    @Test fun realGiacMatchesGoldenWhenAvailable() {
        val bin = System.getenv("GIAC_BIN") ?: return
        for (l in math) {
            val p = ProcessBuilder(bin, l[1]).redirectErrorStream(false).start()
            val out = p.inputStream.bufferedReader().readText().trim().replace("\n", " ")
            p.waitFor()
            assertEquals("giac ${l[1]}", l[2], out)
        }
    }

    @Test fun unitsAndConstantsTablesAreWellFormed() {
        for (c in Units.cats) for (u in c.units) assertTrue(u.factor.isNotBlank())
        val sp = Units.cats[3]
        assertEquals("(72)*(1000/3600)/(1)", Units.convert("72", sp, sp.units[1], sp.units[0]))
        assertTrue(Consts.all.size >= 15)
    }
}
