package com.digitalminds.angelina

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class NotablesTest {
    @Test fun tableIsComplete() {
        assertEquals(17, Notables.rows.size)
        for (r in Notables.rows) {
            assertEquals(6, r.r.size)
            assertNotNull("figura ${r.angle}", Figs.get(r.fig))
            for (x in r.r) assertNotNull("${r.angle}: $x", Parse.row(x))
        }
    }

    private fun g(bin: String, e: String): String { val p = ProcessBuilder(bin, e).start(); val o = p.inputStream.bufferedReader().readText().trim(); p.waitFor(); return o }

    /** Exactos: igual al valor real. Aproximados: menos de 2% de diferencia. Lados: cumplen Pitágoras. */
    @Test fun valuesMatchRealTrigonometry() {
        val bin = System.getenv("GIAC_BIN") ?: return
        for (t in Notables.tris) assertEquals("Pitágoras ${t.small}", "0", g(bin, "simplify((${t.opp})^2+(${t.adj})^2-(${t.hyp})^2)"))
        val fn = listOf("sin(@)", "cos(@)", "tan(@)", "1/tan(@)", "1/cos(@)", "1/sin(@)")
        for (r in Notables.rows) for (i in 0..5) {
            val real = fn[i].replace("@", "(${r.angle})*pi/180")
            if (r.exact) assertEquals("${Notables.names[i]} ${r.angle}", "0", g(bin, "simplify((${r.r[i]})-($real))"))
            else {
                val rel = g(bin, "evalf(abs(($real)-(${r.r[i]}))/($real))").toDouble()
                assertTrue("${Notables.names[i]} ${r.angle}: error $rel", rel < 0.02)
            }
        }
    }
}
