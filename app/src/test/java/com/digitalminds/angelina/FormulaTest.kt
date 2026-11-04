package com.digitalminds.angelina

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FormulaTest {
    @Test fun enoughFormulasInEverySection() {
        assertTrue(Formulas.all.size >= 120)
        for (s in Formulas.secs) assertTrue("seccion ${s.id}", Formulas.all.count { it.sec == s.id } >= 5)
        assertEquals(Formulas.all.size, Formulas.all.map { it.sec + it.title }.toSet().size)
    }

    @Test fun everyFormulaHasSourceExampleAndText() {
        for (f in Formulas.all) {
            assertTrue("fuente ${f.title}", f.src.isNotBlank())
            assertTrue("variables ${f.title}", f.vars.isNotBlank())
            assertNotNull("ejemplo ${f.title}", f.ex)
        }
    }

    @Test fun formulasAndExamplesRender() {
        for (f in Formulas.all) {
            if (!f.isText) assertNotNull("formula ${f.title}: ${f.formula}", Parse.row(f.formula))
            assertNotNull("entrada ${f.title}", Parse.row(f.ex!!.input))
            assertNotNull("resultado ${f.title}", Parse.row(f.ex.out))
        }
    }

    @Test fun figuresExistAndFitTheBox() {
        val used = Formulas.all.map { it.fig }.filter { it.isNotEmpty() }.toSet()
        for (id in used) assertNotNull("figura $id", Figs.get(id))
        for (id in Figs.ids) {
            val p = Figs.get(id)!!
            assertTrue("figura vacia $id", p.isNotEmpty())
            for (x in p) {
                val pts: List<Pair<Float, Float>> = when (x) {
                    is Ln -> listOf(x.x1 to x.y1, x.x2 to x.y2)
                    is Pg -> x.p.chunked(2).map { it[0] to it[1] }
                    is Ci -> listOf((x.x - x.r) to (x.y - x.r), (x.x + x.r) to (x.y + x.r))
                    is El -> listOf((x.x - x.rx) to (x.y - x.ry), (x.x + x.rx) to (x.y + x.ry))
                    is Ar -> listOf(x.x to x.y)
                    is Tx -> listOf(x.x to x.y)
                    is Dt -> listOf(x.x to x.y)
                }
                // Las rectas largas de ejes y gráficas se recortan al dibujar; el resto debe caber en la caja.
                if (x is Ln) continue
                val hb = (BecaFigs.height[id] ?: 60f) + 1f
                for ((a, b) in pts) assertTrue("fuera de la caja $id: $x", a in -1f..101f && b in -1f..hb)
            }
        }
    }

    @Test fun noLongDash() {
        for (f in Formulas.all) for (t in listOf(f.title, f.formula, f.vars, f.exText, f.note, f.src)) assertTrue("guion largo: ${f.title}", !t.contains('\u2014'))
        for (id in Figs.ids) for (x in Figs.get(id)!!) if (x is Tx) assertTrue("guion largo en figura $id", !x.s.contains('\u2014'))
    }

    @Test fun searchFindsByTaskWithoutAccents() {
        assertTrue(Formulas.search("pitagoras", null).any { it.title.contains("Pitágoras") })
        assertTrue(Formulas.search("cono", null).any { it.title == "Cono" })
        assertTrue(Formulas.search("rotacion", null).isNotEmpty())
        assertTrue(Formulas.search("", "transf").size == Formulas.all.count { it.sec == "transf" })
    }

    /** Cada ejemplo se serializa como en la app (grados incluidos) y se compara con Giac real. */
    @Test fun examplesMatchRealGiac() {
        val bin = System.getenv("GIAC_BIN") ?: return
        for (f in Formulas.all) {
            val e = f.ex!!
            val src = Ser.giac(Parse.row(e.input)!!, Ctx(if (e.deg) 0 else 1))
            val p = ProcessBuilder(bin, src).start()
            val out = p.inputStream.bufferedReader().readText().trim().replace("\n", " ")
            p.waitFor()
            assertEquals("giac ${f.title}: $src", e.out, out)
        }
    }
}
