package com.digitalminds.angelina

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class KeyHelpTest {
    /** Teclas que solo escriben un símbolo y no necesitan ventana de ayuda. */
    private val plain = setOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "+", "−", "×", "÷",
        "=", "<", ">", "≤", "≥", "≠", " and ", " or ", "y", "z", "t", "y'", "y''", "!", "0x", "0b", "and", "or", "xor", "not", "shl", "shr") + ('A'..'Z').map { it.toString() }

    @Test fun everyKeyAndVariantHasHelp() {
        val all = (Keys.pages.flatten() + Keys.fixed + Keys.numeric.flatten())
        val labels = all.map { it.label } + all.filter { !it.tapMenu }.flatMap { k -> k.more.map { it.first } }
        for (l in labels.toSet()) if (l !in plain || l == "=") assertNotNull("sin ayuda: $l", KeyHelp.of(l))
    }

    @Test fun everyHelpHasTextAndMostHaveExamples() {
        for ((l, h) in KeyHelp.map) {
            assertTrue("titulo $l", h.title.isNotBlank()); assertTrue("para que $l", h.what.isNotBlank()); assertTrue("como $l", h.how.isNotBlank())
        }
        assertTrue(KeyHelp.map.values.count { it.ex.isNotEmpty() } >= 90)
    }

    @Test fun noLongDashAnywhere() {
        for ((l, h) in KeyHelp.map) for (t in listOf(l, h.title, h.what, h.how, h.note)) assertTrue("guion largo en $l", !t.contains('\u2014'))
        for (g in Guide.entries) for (t in listOf(g.title, g.goal) + g.steps) assertTrue("guion largo en ${g.title}", !t.contains('\u2014'))
    }

    @Test fun examplesParseAndResultsRender() {
        for ((l, h) in KeyHelp.map) for (e in h.ex) {
            assertNotNull("entrada $l: ${e.input}", Parse.row(e.input))
            assertNotNull("resultado $l: ${e.out}", Parse.row(e.out))
        }
    }

    /** Cada ejemplo se serializa como lo hace la app (grados incluidos) y se compara con Giac real. */
    @Test fun examplesMatchRealGiac() {
        val bin = System.getenv("GIAC_BIN") ?: return
        for ((l, h) in KeyHelp.map) for (e in h.ex) {
            val src = Ser.giac(Parse.row(e.input)!!, Ctx(if (e.deg) 0 else 1))
            val p = ProcessBuilder(bin, src).redirectErrorStream(false).start()
            val out = p.inputStream.bufferedReader().readText().trim().replace("\n", " ")
            p.waitFor()
            assertEquals("giac $l: $src", e.out, out)
        }
    }
}
