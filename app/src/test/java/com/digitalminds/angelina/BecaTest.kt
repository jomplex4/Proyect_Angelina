package com.digitalminds.angelina

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BecaTest {
    @Test fun bankStructure() {
        assertTrue(Beca.all.size >= 75)
        val every = Beca.all + Beca.exams + Beca.fichas
        assertEquals(every.size, every.map { it.id }.toSet().size)
        for (t in Beca.topics) assertTrue("tema ${t.id}", Beca.ofTopic(t.id).size >= 5)
        for (p in every) {
            if (p.src == "oficial") assertEquals("4 opciones ${p.id}", 4, p.opts.size)
            else assertTrue("opciones ${p.id}", p.opts.size in setOf(0, 4, 5))
            assertTrue("valores ${p.id}", p.vals.isEmpty() || p.vals.size == p.opts.size)
            assertTrue("opciones distintas ${p.id}", p.opts.size == p.opts.toSet().size || p.review)
            assertTrue("rótulos ${p.id}", p.letters.isEmpty() || p.letters.length == p.opts.size)
            assertTrue("correcta ${p.id}", p.correct in p.opts.indices || (p.correct == -1 && (p.answer.isNotEmpty() || p.opts.isEmpty())))
            assertTrue(p.level in 1..3); assertTrue(p.steps.isNotEmpty())
            assertTrue("tema existente ${p.id}", Beca.topics.any { it.id == p.topic })
            if (p.fig.isNotEmpty()) assertNotNull("figura ${p.id}", Figs.get(p.fig))
            if (p.solver.isNotEmpty()) assertNotNull("resolutor ${p.id}", Solvers.byId(p.solver))
            if (p.calc.isNotEmpty()) assertNotNull("calculadora ${p.id}", Parse.row(p.calc))
            if (p.trapOpt >= 0) assertTrue("trampa ${p.id}", p.trapOpt in p.opts.indices && p.trapOpt != p.correct && p.trap.isNotEmpty() && p.mistake.isNotEmpty())
            if (p.src == "oficial") assertTrue("clave oficial ${p.id}", p.official in p.opts.indices && p.official == p.correct)
            if (p.src == "ficha") assertTrue("grupo ${p.id}", p.group.startsWith("Semana ") && p.num > 0)
            if (p.src == "ficha" && p.correct < 0 && p.opts.isNotEmpty()) assertTrue("aviso ${p.id}", p.warn.isNotEmpty())
            if (p.opts.isEmpty()) assertTrue("respuesta abierta ${p.id}", p.answer.isNotEmpty())
            if (p.text.contains("[tabla]")) assertTrue("tabla ${p.id}", p.table.isNotEmpty())
            val texts = listOf(p.text, p.note, p.theory, p.idea, p.mistake, p.verify, p.warn, p.answer) + p.opts + p.steps + p.table.flatten()
            for (t in texts) assertTrue("guion largo ${p.id}", !t.contains('\u2014'))
            for (line in p.text.lines() + p.opts) if (line.startsWith("§")) assertNotNull("fórmula ${p.id}: $line", Parse.row(line.drop(1)))
        }
    }

    /** Los problemas rehechos traen toda la explicación pedida. */
    @Test fun redoneHaveFullExplanation() {
        for (p in (Beca.exams + Beca.fichas).filter { it.theory.isNotEmpty() || it.src == "ficha" }) {
            assertTrue("idea ${p.id}", p.idea.isNotEmpty()); assertTrue("comprobación ${p.id}", p.verify.isNotEmpty())
            assertTrue("error común ${p.id}", p.mistake.isNotEmpty()); assertTrue("pasos ${p.id}", p.steps.size >= 2)
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

    /** La alternativa marcada coincide con lo que calcula Giac y ninguna otra coincide; el error común da su alternativa. */
    @Test fun onlyTheMarkedAnswerIsCorrect() {
        val bin = System.getenv("GIAC_BIN") ?: return
        fun g(e: String): String { val p = ProcessBuilder(bin, "simplify($e)").start(); val o = p.inputStream.bufferedReader().readText().trim().replace("\n", " "); p.waitFor(); return o }
        for (p in Beca.all + (Beca.exams + Beca.fichas).filter { it.check.isNotEmpty() && it.vals.size == it.opts.size && (it.opts.isNotEmpty() || it.answer.isNotEmpty()) }) {
            val c = g(p.check)
            assertTrue("sin resultado ${p.id}", c.isNotEmpty() && !c.contains("Error") && c != "undef")
            val vs = p.vals.map { g(it) }
            vs.forEachIndexed { i, v ->
                if (i == p.correct) assertEquals("${p.id}: correcta ${p.opts[i]}", c, v)
                else if (p.correct >= 0 && i !in p.alsoRight && p.opts[i] != p.opts[p.correct]) assertNotEquals("${p.id}: también coincide ${p.opts[i]}", c, v)
            }
            if (p.correct == -1) assertTrue("${p.id}: la respuesta calculada no debía estar entre las opciones", c !in vs)
            if (p.trapOpt >= 0) assertEquals("${p.id}: error común", vs[p.trapOpt], g(p.trap))
            if (p.answerVal.isNotEmpty()) assertEquals("${p.id}: respuesta abierta", c, g(p.answerVal))
        }
    }
}
