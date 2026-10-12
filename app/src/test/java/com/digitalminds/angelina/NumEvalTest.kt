package com.digitalminds.angelina

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs

class NumEvalTest {
    private fun fn(s: String, angle: Int = 1) = NumEval.compile(NumEval.parse(Parse.row(s)!!)!!, "x", angle)!!

    @Test fun matchesGiacOnGoldenNumericCases() {
        val lines = GoldenTest.lines().filter { it[0] == "NUM" }
        assertTrue(lines.size >= 10)
        for (l in lines) {
            val (expr, xv) = l[1].split("|")
            val want = l[2].toDouble()
            val got = fn(expr).f(xv.toDouble())
            assertTrue("$expr at $xv: $got vs $want", abs(got - want) <= 1e-9 * maxOf(1.0, abs(want)))
        }
    }
    @Test fun degreeMode() {
        assertEquals(0.5, fn("sin(x)", 0).f(30.0), 1e-12)
        assertEquals(30.0, fn("asin(x)", 0).f(0.5), 1e-9)
        assertEquals(0.5, fn("cos(x)", 2).f(66.66666666666667), 1e-9)
    }
    @Test fun implicitMultiplicationAndPowers() {
        val row = Row(listOf(Num("2"), Sym("x"), Pow(Row(listOf(Num("2")))), Op("+"), Num("1")))
        val f = NumEval.compile(NumEval.parse(row)!!)!!
        assertEquals(19.0, f.f(3.0), 1e-12)
    }
    @Test fun unknownVariableRejected() = assertTrue(NumEval.parse(Row(listOf(Sym("q")))) == null)
    @Test fun constantsFromVariables() {
        val ex = NumEval.parse(Row(listOf(Sym("A"), Op("×"), Sym("x"))), mapOf("A" to 4.0))!!
        assertEquals(12.0, NumEval.compile(ex)!!.f(3.0), 1e-12)
    }
    @Test fun complexResults() {
        val ex = NumEval.parse(Parse.row("sqrt(x)")!!)!!
        val z = NumEval.evalC(ex, Complex(-4.0))
        assertEquals(0.0, z.re, 1e-12); assertEquals(2.0, z.im, 1e-12)
        val e = NumEval.evalC(NumEval.parse(Parse.row("exp(i*pi)")!!)!!, Complex(0.0))
        assertEquals(-1.0, e.re, 1e-12); assertTrue(abs(e.im) < 1e-12)
        assertNotNull(Complex(1.0, 1.0).sqrt())
    }
}
