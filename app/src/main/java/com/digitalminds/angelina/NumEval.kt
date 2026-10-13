package com.digitalminds.angelina

import kotlin.math.*

class Complex(val re: Double, val im: Double = 0.0) {
    operator fun plus(o: Complex) = Complex(re + o.re, im + o.im)
    operator fun minus(o: Complex) = Complex(re - o.re, im - o.im)
    operator fun times(o: Complex) = Complex(re * o.re - im * o.im, re * o.im + im * o.re)
    operator fun div(o: Complex): Complex {
        val d = o.re * o.re + o.im * o.im
        return Complex((re * o.re + im * o.im) / d, (im * o.re - re * o.im) / d)
    }
    fun abs() = hypot(re, im)
    fun arg() = atan2(im, re)
    fun exp() = Complex(exp(re) * cos(im), exp(re) * sin(im))
    fun ln() = Complex(ln(abs()), arg())
    fun pow(o: Complex): Complex = if (re == 0.0 && im == 0.0) Complex(0.0) else (ln() * o).exp()
    fun sqrt(): Complex = pow(Complex(0.5))
    fun isReal(eps: Double = 1e-12) = abs(im) <= eps * max(1.0, abs(re))
}

sealed class Ex
class EC(val v: Double) : Ex()
class EI : Ex()
class EV(val name: String) : Ex()
class EB(val op: Char, val l: Ex, val r: Ex) : Ex()
class EN(val a: Ex) : Ex()
class EF(val name: String, val args: List<Ex>) : Ex()

fun interface DFn { fun f(x: Double): Double }

object NumEval {
    private class P(val nodes: List<Node>, val consts: Map<String, Double>) {
        var i = 0
        fun peek() = nodes.getOrNull(i)
        fun expr(): Ex {
            var l = term()
            while (true) {
                val o = peek()
                if (o is Op && (o.c == "+" || o.c == "-" || o.c == "−")) { i++; val r = term(); l = EB(if (o.c == "+") '+' else '-', l, r) } else break
            }
            return l
        }
        fun isOperand(n: Node?) = n != null && n !is Op && n !is Pow && n !is Str
        fun term(): Ex {
            var l = factor()
            while (true) {
                val o = peek()
                if (o is Op && (o.c == "*" || o.c == "×" || o.c == "·")) { i++; l = EB('*', l, factor()) }
                else if (o is Op && (o.c == "/" || o.c == "÷")) { i++; l = EB('/', l, factor()) }
                else if (isOperand(o)) l = EB('*', l, factor())
                else break
            }
            return l
        }
        fun factor(): Ex {
            val o = peek()
            if (o is Op && (o.c == "-" || o.c == "−")) { i++; return EN(factor()) }
            if (o is Op && o.c == "+") { i++; return factor() }
            var a = atom()
            while (true) {
                val n = peek()
                if (n is Pow) { i++; a = EB('^', a, sub(n.e)) }
                else if (n is Op && n.c == "!") { i++; a = EF("factorial", listOf(a)) }
                else break
            }
            return a
        }
        fun sub(r: Row): Ex = P(r.nodes, consts).also { if (r.nodes.isEmpty()) throw ParseError("empty") }.let { p -> p.expr().also { if (p.i != r.nodes.size) throw ParseError("junk") } }
        fun atom(): Ex {
            val n = nodes.getOrNull(i) ?: throw ParseError("eof")
            i++
            return when (n) {
                is Num -> EC(n.t.toDoubleOrNull() ?: throw ParseError("num"))
                is Sym -> when (n.n) {
                    "π" -> EC(PI); "e" -> EC(E); "i" -> EI(); "x", "t" -> EV(n.n)
                    else -> consts[n.n]?.let { EC(it) } ?: throw ParseError("var ${n.n}")
                }
                is Paren -> sub(n.r)
                is Frac -> EB('/', sub(n.n), sub(n.d))
                is Mixed -> EB('+', sub(n.w), EB('/', sub(n.n), sub(n.d)))
                is Root -> if (n.idx == null) EF("sqrt", listOf(sub(n.r))) else EF("root", listOf(sub(n.r), sub(n.idx)))
                is Func -> EF(n.name, n.args.map { sub(it) })
                else -> throw ParseError("node")
            }
        }
    }

    fun parse(row: Row, consts: Map<String, Double> = emptyMap()): Ex? = try {
        val p = P(row.nodes, consts)
        if (row.nodes.isEmpty()) null else p.expr().also { if (p.i != row.nodes.size) return null }
    } catch (e: ParseError) { null }

    private fun fact(x: Double): Double {
        if (x < 0 || x != floor(x) || x > 170) return Double.NaN
        var r = 1.0
        for (k in 2..x.toInt()) r *= k
        return r
    }

    private fun f1(name: String, k: Double): ((Double) -> Double)? = when (name) {
        "sin" -> { x -> sin(x * k) }
        "cos" -> { x -> cos(x * k) }
        "tan" -> { x -> tan(x * k) }
        "cot" -> { x -> 1.0 / tan(x * k) }
        "sec" -> { x -> 1.0 / cos(x * k) }
        "csc" -> { x -> 1.0 / sin(x * k) }
        "acot" -> { x -> atan(1.0 / x) / k }
        "asec" -> { x -> acos(1.0 / x) / k }
        "acsc" -> { x -> asin(1.0 / x) / k }
        "asin" -> { x -> asin(x) / k }
        "acos" -> { x -> acos(x) / k }
        "atan" -> { x -> atan(x) / k }
        "sinh" -> { x -> sinh(x) }; "cosh" -> { x -> cosh(x) }; "tanh" -> { x -> tanh(x) }
        "asinh" -> { x -> asinh(x) }; "acosh" -> { x -> acosh(x) }; "atanh" -> { x -> atanh(x) }
        "exp" -> { x -> exp(x) }
        "ln", "log" -> { x -> ln(x) }; "log10" -> { x -> log10(x) }
        "sqrt" -> { x -> sqrt(x) }; "abs" -> { x -> abs(x) }; "floor" -> { x -> floor(x) }; "ceil" -> { x -> ceil(x) }
        "round" -> { x -> if (x >= 0) floor(x + 0.5) else -floor(-x + 0.5) }
        "sign" -> { x -> sign(x) }; "factorial" -> ::fact
        else -> null
    }

    fun angleK(angle: Int) = when (angle) { 0 -> PI / 180; 2 -> PI / 200; else -> 1.0 }

    fun compile(ex: Ex, v: String = "x", angle: Int = 1): DFn? {
        val k = angleK(angle)
        fun c(e: Ex): DFn? = when (e) {
            is EC -> DFn { e.v }
            is EI -> null
            is EV -> if (e.name == v) DFn { x -> x } else null
            is EN -> c(e.a)?.let { a -> DFn { x -> -a.f(x) } }
            is EB -> {
                val l = c(e.l)
                val r = c(e.r)
                if (l == null || r == null) null else when (e.op) {
                    '+' -> DFn { x -> l.f(x) + r.f(x) }
                    '-' -> DFn { x -> l.f(x) - r.f(x) }
                    '*' -> DFn { x -> l.f(x) * r.f(x) }
                    '/' -> DFn { x -> l.f(x) / r.f(x) }
                    else -> DFn { x -> val b = l.f(x); val p = r.f(x); if (b < 0 && p != floor(p)) rootOdd(b, p) else b.pow(p) }
                }
            }
            is EF -> when {
                e.name == "root" && e.args.size == 2 -> {
                    val a = c(e.args[0]); val n = c(e.args[1])
                    if (a == null || n == null) null else DFn { x -> val nn = n.f(x); val b = a.f(x); if (b < 0 && nn % 2.0 == 1.0) -(-b).pow(1 / nn) else b.pow(1 / nn) }
                }
                e.name == "logb" && e.args.size == 2 -> {
                    val a = c(e.args[0]); val b = c(e.args[1])
                    if (a == null || b == null) null else DFn { x -> ln(a.f(x)) / ln(b.f(x)) }
                }
                (e.name == "min" || e.name == "max") && e.args.size == 2 -> {
                    val a = c(e.args[0]); val b = c(e.args[1])
                    if (a == null || b == null) null else if (e.name == "min") DFn { x -> min(a.f(x), b.f(x)) } else DFn { x -> max(a.f(x), b.f(x)) }
                }
                e.name == "irem" && e.args.size == 2 -> {
                    val a = c(e.args[0]); val b = c(e.args[1])
                    if (a == null || b == null) null else DFn { x -> val m = b.f(x); ((a.f(x) % m) + m) % m }
                }
                e.args.size == 1 -> f1(e.name, k)?.let { fn -> c(e.args[0])?.let { a -> DFn { x -> fn(a.f(x)) } } }
                else -> null
            }
        }
        return c(ex)
    }

    private fun rootOdd(b: Double, p: Double): Double = Double.NaN

    fun evalC(e: Ex, x: Complex, v: String = "x"): Complex = when (e) {
        is EC -> Complex(e.v)
        is EI -> Complex(0.0, 1.0)
        is EV -> if (e.name == v) x else Complex(Double.NaN)
        is EN -> Complex(0.0) - evalC(e.a, x, v)
        is EB -> {
            val l = evalC(e.l, x, v); val r = evalC(e.r, x, v)
            when (e.op) { '+' -> l + r; '-' -> l - r; '*' -> l * r; '/' -> l / r; else -> l.pow(r) }
        }
        is EF -> {
            val a = e.args.map { evalC(it, x, v) }
            val z = a.getOrNull(0) ?: Complex(Double.NaN)
            when (e.name) {
                "sqrt" -> z.sqrt(); "exp" -> z.exp(); "ln", "log" -> z.ln(); "abs" -> Complex(z.abs())
                "re" -> Complex(z.re); "im" -> Complex(z.im); "arg" -> Complex(z.arg()); "conj" -> Complex(z.re, -z.im)
                "sin" -> Complex(sin(z.re) * cosh(z.im), cos(z.re) * sinh(z.im))
                "cos" -> Complex(cos(z.re) * cosh(z.im), -sin(z.re) * sinh(z.im))
                else -> if (z.isReal()) f1(e.name, 1.0)?.let { Complex(it(z.re)) } ?: Complex(Double.NaN) else Complex(Double.NaN)
            }
        }
    }
}
