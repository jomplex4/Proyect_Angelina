package com.digitalminds.angelina

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BecaTest {
    @Test fun bankStructure() {
        assertTrue(Beca.all.size >= 75)
        assertEquals(Beca.all.size, Beca.all.map { it.id }.toSet().size)
        for (t in Beca.topics) assertTrue("tema ${t.id}", Beca.all.count { it.topic == t.id } >= 5)
        for (p in Beca.all) {
            assertEquals("5 opciones ${p.id}", 5, p.opts.size); assertEquals(5, p.vals.size)
            assertEquals("opciones distintas ${p.id}", 5, p.opts.toSet().size)
            assertTrue(p.correct in 0..4); assertTrue(p.level in 1..3); assertTrue(p.steps.isNotEmpty())
            if (p.fig.isNotEmpty()) assertNotNull("figura ${p.id}", Figs.get(p.fig))
            if (p.solver.isNotEmpty()) assertNotNull("resolutor ${p.id}", Solvers.byId(p.solver))
            if (p.calc.isNotEmpty()) assertNotNull("calculadora ${p.id}", Parse.row(p.calc))
            for (t in listOf(p.text) + p.opts + p.steps) assertTrue("guion largo ${p.id}", !t.contains('\u2014'))
        }
    }

    @Test fun answersAreSpreadAcrossLetters() {
        val c = Beca.all.groupingBy { it.correct }.eachCount()
        for (i in 0..4) assertTrue("letra $i: ${c[i]}", (c[i] ?: 0) >= Beca.all.size / 5 - 2)
    }

    @Test fun simulacroIsBalancedAndRepeatable() {
        val s = Beca.simulacro(42)
        assertEquals(Beca.SIM_SIZE, s.size); assertEquals(s.size, s.toSet().size)
        val topics = s.mapNotNull { Beca.byId(it)?.topic }.toSet()
        assertEquals(Beca.topics.size, topics.size)
        assertEquals(s, Beca.simulacro(42)); assertNotEquals(s, Beca.simulacro(43))
    }

    /** La alternativa marcada coincide con lo que calcula Giac y ninguna otra coincide. */
    @Test fun onlyTheMarkedAnswerIsCorrect() {
        val bin = System.getenv("GIAC_BIN") ?: return
        fun g(e: String): String { val p = ProcessBuilder(bin, "simplify($e)").start(); val o = p.inputStream.bufferedReader().readText().trim().replace("\n", " "); p.waitFor(); return o }
        for (p in Beca.all) {
            val c = g(p.check)
            assertTrue("sin resultado ${p.id}", c.isNotEmpty() && !c.contains("Error") && c != "undef")
            p.vals.forEachIndexed { i, v ->
                if (i == p.correct) assertEquals("${p.id}: correcta ${p.opts[i]}", c, g(v))
                else assertNotEquals("${p.id}: también coincide ${p.opts[i]}", c, g(v))
            }
        }
    }
}
