package com.digitalminds.angelina

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SolverTest {
    private val ph = Regex("\\{([^}]+)\\}")

    @Test fun structureIsValid() {
        assertTrue(Solvers.all.size >= 30)
        assertEquals(Solvers.all.size, Solvers.all.map { it.id }.toSet().size)
        for (s in Solvers.all) {
            assertTrue("seccion ${s.id}", Solvers.secs.any { it.id == s.sec })
            if (!s.formula.startsWith("T:")) assertNotNull("formula ${s.id}", Parse.row(s.formula))
            for (i in s.inputs) if (!i.list) assertNotNull("dato ${s.id}.${i.id}", Parse.row(i.def))
            val known = s.inputs.map { it.id }.toMutableSet()
            for (st in s.steps) {
                for (m in ph.findAll(st.show + st.eval)) assertTrue("dato sin definir ${s.id}: ${m.value}", m.groupValues[1] in known)
                if (st.save.isNotEmpty()) known.add(st.save)
            }
            for (l in s.links) assertTrue("enlace ${s.id}: $l", Formulas.all.any { it.title == l })
        }
    }

    @Test fun substitutionWrapsNegativesAndFractions() {
        assertEquals("-(-3)", Solvers.subst("-{b}", mapOf("b" to "-3")))
        assertEquals("(2/5)*3", Solvers.subst("{p}*{a}", mapOf("p" to "(2)/(5)", "a" to "3")))
        assertEquals("(-3)^2", Solvers.subst("{x}^2", mapOf("x" to "(-3)")))
    }

    @Test fun shownStepsRender() {
        for (s in Solvers.all.filter { it.special == 0 }) {
            val v = s.inputs.associate { it.id to Ser.giac(Parse.row(it.def)!!, Ctx(1)) }.toMutableMap()
            for (st in s.steps) { assertNotNull("mostrar ${s.id}: ${st.show}", Parse.row(Solvers.subst(st.show, v))); if (st.save.isNotEmpty()) v[st.save] = "1" }
        }
    }

    /** Cadena completa con los datos de ejemplo, igual que en la app, comparada con lo esperado y con Giac real. */
    @Test fun examplesMatchRealGiac() {
        val bin = System.getenv("GIAC_BIN") ?: return
        val gold = SolverTest::class.java.getResourceAsStream("/solver_golden.txt")!!.bufferedReader().readLines().filter { it.isNotBlank() }
            .associate { l -> l.split("\t").let { (it[0] + "#" + it[1]) to it[2] } }
        for (s in Solvers.all.filter { it.special == 0 }) {
            val v = LinkedHashMap<String, String>()
            for (i in s.inputs) v[i.id] = Ser.giac(Parse.row(i.def)!!, Ctx(1))
            s.steps.forEachIndexed { k, st ->
                val e = Solvers.subst(st.eval, v)
                val p = ProcessBuilder(bin, if (st.raw) e else "simplify($e)").start()
                val out = p.inputStream.bufferedReader().readText().trim().replace("\n", " "); p.waitFor()
                assertEquals("${s.id} paso ${k + 1}: $e", gold[s.id + "#" + k], out)
                if (st.save.isNotEmpty()) v[st.save] = out
            }
        }
    }

    @Test fun looseDataIsExact() {
        val o = StatSolve.loose("12, 10, 12, 14, 12, 18")
        assertNull(o.error)
        val r = o.lines.associate { it.label.substringBefore(":") to it.result }
        assertEquals("13", r["Media"]); assertEquals("12", r["Mediana"]); assertEquals("8", r["Rango"])
        assertEquals("19/3", r["Varianza poblacional"]); assertEquals("38/5", r["Varianza muestral"])
        assertEquals("12", o.lines.first { it.label.startsWith("Moda") }.result)
        assertEquals("15", StatSolve.loose("11 13 15 17 19").lines.first { it.label.startsWith("Mediana") }.result)
        assertTrue(StatSolve.loose("1, 2, 3").lines.first { it.label.startsWith("Moda") }.text)
        assertNotNull(StatSolve.loose("1, a").error)
        assertEquals("5/2", StatSolve.loose("2.5").lines.first { it.label.startsWith("Media") }.result)
    }

    @Test fun groupedTableIsExact() {
        val o = StatSolve.grouped("10", "10", "5, 8, 7")
        assertNull(o.error)
        val t = o.table!!
        assertEquals(listOf("[20; 30⟩", "25", "8", "13", "0.4", "0.65", "200"), t[2])
        assertEquals("520", t.last().last())
        val r = o.lines.associate { it.label.substringBefore(":") to it.result }
        assertEquals("26", r["Media"]); assertEquals("105/4", r["Mediana"]); assertEquals("55/2", r["Moda"])
        assertNotNull(StatSolve.grouped("10", "0", "5").error)
        assertNotNull(StatSolve.grouped("10", "5", "2.5, 3").error)
    }

    /** La tabla de la ficha Yomi de pesos (40 postulantes): la media exacta es 65,85 kg. */
    @Test fun yomiWeightsTable() {
        val o = StatSolve.grouped("48", "6", "2, 6, 14, 10, 5, 3")
        assertEquals("1317/20", o.lines.first { it.label.startsWith("Media") }.result)
    }
}
