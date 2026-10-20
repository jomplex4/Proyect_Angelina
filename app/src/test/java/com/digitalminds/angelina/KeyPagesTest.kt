package com.digitalminds.angelina

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class KeyPagesTest {
    @Test fun atLeastSevenPagesWithTenKeysAndUniqueIds() {
        assertTrue(Keys.allPages.size >= 7)
        for (p in Keys.allPages) assertEquals("teclas en ${p.id}", 10, p.keys.size)
        assertEquals(Keys.defaultOrder.size, Keys.defaultOrder.toSet().size)
        assertEquals(Keys.MY, Keys.defaultOrder.last())
    }

    @Test fun defaultArrangementShowsEverythingInPriorityOrder() {
        val ps = Keys.arrange(emptyList(), emptySet(), emptyList())
        assertEquals(Keys.defaultOrder, ps.map { it.id })
        assertEquals("basico", ps.first().id)
        assertTrue(ps.last().keys.all { it === Keys.emptySlot })
    }

    @Test fun savedOrderHiddenAndNewPagesAreRespected() {
        val ps = Keys.arrange(listOf("graficas", "basico", "viejo"), setOf("matrices"), emptyList())
        assertEquals("graficas", ps[0].id); assertEquals("basico", ps[1].id)
        assertTrue(ps.none { it.id == "matrices" || it.id == "viejo" })
        assertEquals(Keys.defaultOrder.size - 1, ps.size)
    }

    @Test fun neverEmptyKeyboard() {
        val ps = Keys.arrange(emptyList(), Keys.defaultOrder.toSet(), emptyList())
        assertEquals(1, ps.size)
    }

    @Test fun myKeysPageUsesChosenKeysThenEmptySlots() {
        val ps = Keys.arrange(emptyList(), emptySet(), listOf("resolver", "% único", "no-existe"))
        val my = ps.first { it.id == Keys.MY }
        assertEquals(10, my.keys.size)
        assertEquals("resolver", my.keys[0].label); assertEquals("% único", my.keys[1].label)
        assertTrue(my.keys.drop(2).all { it === Keys.emptySlot })
    }

    @Test fun usageSortIsStable() {
        val o = Keys.byUsage(Keys.defaultOrder, mapOf("graficas" to 30, "aritmetica" to 30, "calculo" to 5))
        assertEquals(listOf("aritmetica", "graficas", "calculo"), o.take(3))
        assertEquals(Keys.defaultOrder.size, o.size)
    }

    @Test fun graphTemplatesParse() {
        for (k in Keys.allPages.first { it.id == "graficas" }.keys) {
            val a = k.act as KA.P
            val row = Parse.row(a.src)
            assertTrue("plantilla ${k.label}", row != null)
            // La gráfica debe poder dibujarla: se evalúa en x = 4.
            val f = NumEval.parse(row!!)?.let { NumEval.compile(it, "x", 1) }
            assertTrue("grafica ${k.label}", f != null && !f.f(4.0).isNaN())
        }
    }
}
