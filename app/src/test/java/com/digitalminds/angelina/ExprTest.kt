package com.digitalminds.angelina

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExprTest {
    private fun typed(vararg steps: (Ed) -> Ed): Ed = steps.fold(Ed()) { e, f -> f(e) }
    private fun t(s: String): (Ed) -> Ed = { Editor.typeText(it, s) }
    private val right: (Ed) -> Ed = { Editor.right(it) }
    private val frac: (Ed) -> Ed = { Editor.fracKey(it) }
    private fun op(c: String): (Ed) -> Ed = { Editor.op(it, c) }
    private fun ser(e: Ed, angle: Int = 1, ans: String? = null) = Ser.giac(e.row, Ctx(angle, ans))

    @Test fun digitsMerge() = assertEquals("123.5", ser(typed(t("1"), t("2"), t("3"), t("."), t("5"))))
    @Test fun doubleDotIgnored() = assertEquals("1.25", ser(typed(t("1"), t("."), t("2"), t("."), t("5"))))
    @Test fun fractionFromNumber() = assertEquals("(1)/(2)", ser(typed(t("1"), frac, t("2"))))
    @Test fun emptyFraction() = assertEquals("(3)/(4)", ser(typed(frac, t("3"), right, t("4"))))
    @Test fun powerAndImplicitMultiplication() =
        assertEquals("2*x^(2)+1", ser(typed(t("2"), t("x"), { Editor.insert(it, Pow(Row()), 0) }, t("2"), right, op("+"), t("1"))))
    @Test fun implicitBeforeParen() = assertEquals("2*(3+4)", ser(typed(t("2"), { Editor.insert(it, Paren(Row()), 0) }, t("3"), op("+"), t("4"))))
    @Test fun fractionPower() =
        assertEquals("((1)/(2))^(2)", ser(typed(t("1"), frac, t("2"), right, { Editor.insert(it, Pow(Row()), 0) }, t("2"))))
    @Test fun rootKinds() {
        assertEquals("sqrt(9)", ser(Ed(Row(listOf(Root(null, Row(listOf(Num("9")))))))))
        assertEquals("simplify(surd(27,3))", ser(Ed(Row(listOf(Root(Row(listOf(Num("3"))), Row(listOf(Num("27")))))))))
    }
    @Test fun constants() = assertEquals("pi*exp(1)*i", ser(Ed(Row(listOf(Sym("π"), Sym("e"), Sym("i"))))))
    @Test fun degreeScaling() {
        val sin = Ed(Row(listOf(Func("sin", listOf(Row(listOf(Num("30"))))))))
        assertEquals("sin((30)*pi/180)", ser(sin, 0))
        assertEquals("sin((30)*pi/200)", ser(sin, 2))
        assertEquals("sin(30)", ser(sin, 1))
        val asin = Ed(Row(listOf(Func("asin", listOf(Row(listOf(Num("1"), Op("/"), Num("2"))))))))
        assertEquals("simplify((asin(1/2))/(pi/180))", ser(asin, 0))
    }
    @Test fun ansSubstitution() = assertEquals("(5/6)*2", ser(Ed(Row(listOf(Sym("Ans"), Op("×"), Num("2")))), 1, "5/6"))
    @Test fun matrix() {
        val m = Mat(2, 2, listOf("1", "2", "3", "4").map { Row(listOf(Num(it))) })
        assertEquals("det([[1,2],[3,4]])", ser(Ed(Row(listOf(Func("det", listOf(Row(listOf(m)))))))))
    }
    private fun r(vararg n: Node) = Row(n.toList())
    @Test fun bitHelpers() {
        assertEquals("((5)*2^(2))", ser(Ed(r(Func("shl", listOf(r(Num("5")), r(Num("2"))))))))
        assertEquals("(-1-(5))", ser(Ed(r(Func("not", listOf(r(Num("5"))))))))
    }
    @Test fun dmsAndPreAns() {
        assertEquals("[iPart(12.5),iPart(fPart(12.5)*60),fPart(fPart(12.5)*60)*60]", ser(Ed(r(Func("dms", listOf(r(Num("12.5"))))))))
        assertEquals("((12)+(30)/60+(0)/3600)", ser(Ed(r(Func("fromdms", listOf(r(Num("12")), r(Num("30")), r(Num("0"))))))))
        assertEquals("(7)+(5)", Ser.giac(r(Sym("PreAns"), Op("+"), Sym("Ans")), Ctx(1, "5", "7")))
    }
    @Test fun factorialPostfix() = assertEquals("5!*2", ser(Ed(Row(listOf(Num("5"), Op("!"), Num("2"))))))

    @Test fun backspaceDeletesDigitsThenNode() {
        var e = typed(t("12"))
        e = Editor.back(e); assertEquals("1", ser(e))
        e = Editor.back(e); assertEquals("", ser(e))
    }
    @Test fun backspaceEntersContainerThenDeletesEmpty() {
        var e = typed({ Editor.insert(it, Func("sin", listOf(Row())), 0) }, t("3"))
        e = Editor.back(e); assertEquals(listOf<Step>(Step(0, 0)), e.cur.path)
        e = Editor.back(e); assertTrue(e.row.nodes.isEmpty())
    }
    @Test fun leftRightTraversal() {
        var e = typed(t("1"), frac, t("2"))
        assertEquals(1, e.cur.path.size)
        e = Editor.right(e); assertTrue(e.cur.path.isEmpty()); assertEquals(1, e.cur.idx)
        e = Editor.left(e); assertEquals(Step(0, 1), e.cur.path.last())
        e = Editor.left(e); e = Editor.left(e); assertEquals(Step(0, 0), e.cur.path.last())
    }
    @Test fun commaMovesBetweenArguments() {
        var e = typed({ Editor.insert(it, Func("logb", listOf(Row(), Row())), 0) }, t("8"), { Editor.comma(it) }, t("2"))
        assertEquals("logb(8,2)", ser(e))
    }
    @Test fun commaInsideListAddsItem() {
        val e = typed({ Editor.insert(it, Lst(listOf(Row())), 0) }, t("1"), { Editor.comma(it) }, t("2"))
        assertEquals("[1,2]", ser(e))
    }
    @Test fun closeParenExits() {
        val e = typed({ Editor.insert(it, Paren(Row()), 0) }, t("1"), { Editor.closeParen(it) }, op("+"), t("2"))
        assertEquals("(1)+2", ser(e))
    }
    @Test fun firstEmptyFindsBox() {
        val row = Row(listOf(Num("1"), Op("+"), Frac(Row(listOf(Num("2"))), Row())))
        assertEquals(Cur(listOf(Step(2, 1)), 0), Editor.firstEmpty(row))
        assertNull(Editor.firstEmpty(Row(listOf(Num("1")))))
    }
    @Test fun hexLiteral() = assertEquals("0x1F", ser(typed(t("0x"), t("1"), t("F"))))

    @Test fun parseFractionStructure() {
        val r = Parse.row("1/2*x^2")!!
        assertTrue(r.nodes[0] is Frac)
        assertTrue(r.nodes[1] is Op)
        assertTrue(r.nodes[3] is Pow)
    }
    @Test fun parseSpecials() {
        assertTrue(Parse.row("sqrt(2)")!!.nodes[0] is Root)
        assertTrue(Parse.row("exp(1)")!!.nodes[0] == Sym("e"))
        assertTrue(Parse.row("exp(x)")!!.nodes[1] is Pow)
        assertTrue(Parse.row("[[1,2],[3,4]]")!!.nodes[0] is Mat)
        assertTrue(Parse.row("list[2,3]")!!.nodes[0] is Lst)
        assertTrue(Parse.row("matrix[[1,3],[2,4]]")!!.nodes[0] is Mat)
        assertTrue(Parse.row("pi/4")!!.nodes[0] is Frac)
        assertEquals("x-1/6*x^3+O(x^6)".replace("1/6", "(1)/(6)").replace("x^3", "x^(3)").replace("x^6", "x^(6)"),
            Ser.giac(Parse.row("x-1/6*x^3+x^6*order_size(x)")!!))
        assertNotNull(Parse.row("5000.0_m"))
        assertNotNull(Parse.row("1.0_(c_)"))
        assertNull(Parse.row("a ## b"))
    }
    @Test fun numberFormatting() {
        fun f(t: String, eng: Boolean = false, sig: Int = 0) = Fmt.rows(Row(listOf(Num(t))), eng, sig, false).nodes.let { Ser.giac(Row(it)) }
        assertEquals("0.833333", f("0.833333333333", sig = 6))
        assertEquals("1.5*10^(-5)", f("1.5e-05"))
        assertEquals("15*10^(-6)", f("1.5e-05", eng = true))
        assertEquals("2.5", f("2.5"))
        assertEquals("0", f("0.0"))
    }
    @Test fun mixedFractions() {
        val r = Fmt.rows(Row(listOf(Frac(Row(listOf(Num("7"))), Row(listOf(Num("3")))))), false, 0, true)
        assertTrue(r.nodes[0] is Mixed)
        assertEquals("((2)+(1)/(3))", Ser.giac(r))
        assertEquals(Row(listOf(Num("3"))), Fmt.rows(Row(listOf(Frac(Row(listOf(Num("6"))), Row(listOf(Num("2")))))), false, 0, true))
    }
    @Test fun baseFormatting() {
        assertEquals("0xFF", Fmt.base("255", 16))
        assertEquals("0b101", Fmt.base("5", 2))
        assertEquals("-0o10", Fmt.base("-8", 8))
        assertNull(Fmt.base("1/2", 16))
        assertNull(Fmt.base("255", 10))
    }
}
