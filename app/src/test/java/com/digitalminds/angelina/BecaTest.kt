package com.digitalminds.angelina

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BecaTest {
    @Test fun bankStructure() {
        assertTrue(Beca.all.size >= 75)
        val every = Beca.all + Beca.exams
        assertEquals(every.size, every.map { it.id }.toSet().size)
        for (t in Beca.topics) assertTrue("tema ${t.id}", Beca.ofTopic(t.id).size >= 5)
        for (p in every) {
            assertEquals("4 opciones ${p.id}", 4, p.opts.size)
            assertTrue("valores ${p.id}", p.vals.isEmpty() || p.vals.size == 4)
            assertEquals("opciones distintas ${p.id}", 4, p.opts.toSet().size)
            assertTrue(p.correct in 0..3); assertTrue(p.level in 1..3); assertTrue(p.steps.isNotEmpty())
            assertTrue("tema existente ${p.id}", Beca.topics.any { it.id == p.topic })
            if (p.fig.isNotEmpty()) assertNotNull("figura ${p.id}", Figs.get(p.fig))
            if (p.solver.isNotEmpty()) assertNotNull("resolutor ${p.id}", Solvers.byId(p.solver))
            if (p.calc.isNotEmpty()) assertNotNull("calculadora ${p.id}", Parse.row(p.calc))
            for (t in listOf(p.text, p.note) + p.opts + p.steps + p.table.flatten()) assertTrue("guion largo ${p.id}", !t.contains('\u2014'))
        }
    }

    @Test fun officialExamsAreComplete() {
        for (y in Beca.years) {
            val e = Beca.exam(y)
            assertEquals("examen $y", 30, e.size)
            assertEquals((1..30).toList(), e.map { it.id.substringAfter("_").toInt() })
        }
    }

    @Test fun answersAreSpreadAcrossLetters() {
        val c = Beca.all.groupingBy { it.correct }.eachCount()
        for (i in 0..3) assertTrue("letra $i: ${c[i]}", (c[i] ?: 0) >= Beca.all.size / 4 - 2)
    }

    @Test fun levelSimulacrosHaveThirtyDistinct() {
        for (lv in 0..3) { val s = Beca.simulacro(7, lv); assertEquals(Beca.SIM_SIZE, s.size); assertEquals(s.size, s.toSet().size) }
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
        for (p in Beca.all + Beca.exams.filter { it.check.isNotEmpty() && it.vals.size == 4 }) {
            val c = g(p.check)
            assertTrue("sin resultado ${p.id}", c.isNotEmpty() && !c.contains("Error") && c != "undef")
            p.vals.forEachIndexed { i, v ->
                if (i == p.correct) assertEquals("${p.id}: correcta ${p.opts[i]}", c, g(v))
                else assertNotEquals("${p.id}: también coincide ${p.opts[i]}", c, g(v))
            }
        }
    }
}
