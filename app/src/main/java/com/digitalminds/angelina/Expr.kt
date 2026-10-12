package com.digitalminds.angelina

import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode

sealed interface Node
data class Num(val t: String) : Node
data class Sym(val n: String) : Node
data class Op(val c: String) : Node
data class Str(val s: String) : Node
data class Frac(val n: Row, val d: Row) : Node
data class Root(val idx: Row?, val r: Row) : Node
data class Pow(val e: Row) : Node
data class Paren(val r: Row) : Node
data class Lst(val items: List<Row>) : Node
data class Func(val name: String, val args: List<Row>) : Node
data class Mat(val rows: Int, val cols: Int, val cells: List<Row>) : Node
data class Mixed(val w: Row, val n: Row, val d: Row) : Node
data class Row(val nodes: List<Node> = emptyList())

data class Step(val node: Int, val slot: Int)
data class Cur(val path: List<Step> = emptyList(), val idx: Int = 0)
data class Ed(val row: Row = Row(), val cur: Cur = Cur())

fun slots(n: Node): List<Row> = when (n) {
    is Frac -> listOf(n.n, n.d)
    is Root -> if (n.idx != null) listOf(n.idx, n.r) else listOf(n.r)
    is Pow -> listOf(n.e)
    is Paren -> listOf(n.r)
    is Lst -> n.items
    is Func -> n.args
    is Mat -> n.cells
    is Mixed -> listOf(n.w, n.n, n.d)
    else -> emptyList()
}

fun withSlots(n: Node, s: List<Row>): Node = when (n) {
    is Frac -> Frac(s[0], s[1])
    is Root -> if (n.idx != null) Root(s[0], s[1]) else Root(null, s[0])
    is Pow -> Pow(s[0])
    is Paren -> Paren(s[0])
    is Lst -> Lst(s)
    is Func -> Func(n.name, s)
    is Mat -> Mat(n.rows, n.cols, s)
    is Mixed -> Mixed(s[0], s[1], s[2])
    else -> n
}

fun Row.sub(path: List<Step>, d: Int = 0): Row {
    if (d == path.size) return this
    val s = path[d]
    return slots(nodes[s.node])[s.slot].sub(path, d + 1)
}

fun Row.upd(path: List<Step>, d: Int = 0, f: (Row) -> Row): Row {
    if (d == path.size) return f(this)
    val s = path[d]
    val n = nodes[s.node]
    val sl = slots(n).toMutableList()
    sl[s.slot] = sl[s.slot].upd(path, d + 1, f)
    val ns = nodes.toMutableList()
    ns[s.node] = withSlots(n, sl)
    return Row(ns)
}

object Editor {
    private fun isContainer(n: Node) = slots(n).isNotEmpty()

    fun insert(ed: Ed, node: Node, enter: Int = -1): Ed {
        val i = ed.cur.idx
        val row = ed.row.upd(ed.cur.path) { r -> Row(r.nodes.subList(0, i) + node + r.nodes.subList(i, r.nodes.size)) }
        val cur = if (enter >= 0) Cur(ed.cur.path + Step(i, enter), 0) else Cur(ed.cur.path, i + 1)
        return Ed(row, cur)
    }

    private fun prev(ed: Ed): Node? = ed.row.sub(ed.cur.path).nodes.getOrNull(ed.cur.idx - 1)

    private fun replacePrev(ed: Ed, node: Node): Ed {
        val i = ed.cur.idx
        return Ed(ed.row.upd(ed.cur.path) { r -> Row(r.nodes.toMutableList().also { it[i - 1] = node }) }, ed.cur)
    }

    private fun isHex(t: String) = t.startsWith("0x") || t.startsWith("0X")

    fun typeText(ed: Ed, s: String): Ed {
        val p = prev(ed)
        val c = s[0]
        if (c == '.' && s.length == 1 && p is Num && p.t.contains('.')) return ed
        if (p is Num && s.length == 1) {
            val ok = c.isDigit() || (c == '.' && !p.t.contains('.') && !isHex(p.t)) ||
                (isHex(p.t) && c.uppercaseChar() in 'A'..'F') ||
                (p.t == "0" && (c == 'x' || c == 'b' || c == 'o'))
            if (ok) return replacePrev(ed, Num(p.t + c))
        }
        return when {
            c.isDigit() || (c == '.' && s.length == 1) -> insert(ed, Num(if (s == ".") "0." else s))
            s == "0x" || s == "0b" || s == "0o" -> insert(ed, Num(s))
            else -> insert(ed, Sym(s))
        }
    }

    fun op(ed: Ed, c: String): Ed = insert(ed, Op(c))

    fun back(ed: Ed): Ed {
        val path = ed.cur.path
        val i = ed.cur.idx
        val r = ed.row.sub(path)
        if (i > 0) {
            val p = r.nodes[i - 1]
            if (p is Num && p.t.length > 1) return replacePrev(ed, Num(p.t.dropLast(1)))
            if (isContainer(p)) {
                val sl = slots(p)
                if (sl.all { it.nodes.isEmpty() }) return removeAt(ed, i - 1)
                return Ed(ed.row, Cur(path + Step(i - 1, sl.lastIndex), sl.last().nodes.size))
            }
            return removeAt(ed, i - 1)
        }
        if (path.isEmpty()) return ed
        val st = path.last()
        val up = path.dropLast(1)
        val parent = ed.row.sub(up).nodes[st.node]
        val sl = slots(parent)
        if (parent is Lst && sl.size > 1 && sl[st.slot].nodes.isEmpty()) {
            val ns = sl.toMutableList().also { it.removeAt(st.slot) }
            val nr = ed.row.upd(up) { rr -> Row(rr.nodes.toMutableList().also { it[st.node] = Lst(ns) }) }
            val ps = maxOf(0, st.slot - 1)
            return Ed(nr, Cur(up + Step(st.node, ps), ns[ps].nodes.size))
        }
        if (sl.all { it.nodes.isEmpty() }) return Ed(removeNode(ed.row, up, st.node), Cur(up, st.node))
        if (st.slot > 0) return Ed(ed.row, Cur(up + Step(st.node, st.slot - 1), sl[st.slot - 1].nodes.size))
        return Ed(ed.row, Cur(up, st.node))
    }

    private fun removeNode(row: Row, path: List<Step>, i: Int): Row =
        row.upd(path) { r -> Row(r.nodes.toMutableList().also { it.removeAt(i) }) }

    private fun removeAt(ed: Ed, i: Int) = Ed(removeNode(ed.row, ed.cur.path, i), Cur(ed.cur.path, i))

    fun left(ed: Ed): Ed {
        val path = ed.cur.path
        val i = ed.cur.idx
        if (i > 0) {
            val n = ed.row.sub(path).nodes[i - 1]
            val sl = slots(n)
            return if (sl.isNotEmpty()) Ed(ed.row, Cur(path + Step(i - 1, sl.lastIndex), sl.last().nodes.size)) else Ed(ed.row, Cur(path, i - 1))
        }
        if (path.isEmpty()) return ed
        val st = path.last()
        val up = path.dropLast(1)
        if (st.slot > 0) {
            val sl = slots(ed.row.sub(up).nodes[st.node])
            return Ed(ed.row, Cur(up + Step(st.node, st.slot - 1), sl[st.slot - 1].nodes.size))
        }
        return Ed(ed.row, Cur(up, st.node))
    }

    fun right(ed: Ed): Ed {
        val path = ed.cur.path
        val i = ed.cur.idx
        val r = ed.row.sub(path)
        if (i < r.nodes.size) {
            val n = r.nodes[i]
            return if (isContainer(n)) Ed(ed.row, Cur(path + Step(i, 0), 0)) else Ed(ed.row, Cur(path, i + 1))
        }
        if (path.isEmpty()) return ed
        val st = path.last()
        val up = path.dropLast(1)
        val sl = slots(ed.row.sub(up).nodes[st.node])
        if (st.slot < sl.lastIndex) return Ed(ed.row, Cur(up + Step(st.node, st.slot + 1), 0))
        return Ed(ed.row, Cur(up, st.node + 1))
    }

    fun closeParen(ed: Ed): Ed {
        val path = ed.cur.path
        for (d in path.indices.reversed()) {
            val st = path[d]
            val n = ed.row.sub(path.take(d)).nodes[st.node]
            if (n is Paren || n is Func) return Ed(ed.row, Cur(path.take(d), st.node + 1))
        }
        return ed
    }

    fun comma(ed: Ed): Ed {
        val path = ed.cur.path
        if (path.isNotEmpty()) {
            val st = path.last()
            val up = path.dropLast(1)
            val n = ed.row.sub(up).nodes[st.node]
            if (n is Func && st.slot < n.args.lastIndex) return Ed(ed.row, Cur(up + Step(st.node, st.slot + 1), 0))
            if (n is Lst) {
                val cur = n.items[st.slot]
                val here = ed.cur.idx
                val head = Row(cur.nodes.subList(0, here))
                val tail = Row(cur.nodes.subList(here, cur.nodes.size))
                val items = n.items.toMutableList()
                items[st.slot] = head
                items.add(st.slot + 1, tail)
                val nr = ed.row.upd(up) { rr -> Row(rr.nodes.toMutableList().also { it[st.node] = Lst(items) }) }
                return Ed(nr, Cur(up + Step(st.node, st.slot + 1), 0))
            }
        }
        return op(ed, ",")
    }

    fun place(ed: Ed, c: Cur): Ed = Ed(ed.row, c)

    fun fracKey(ed: Ed): Ed {
        val p = prev(ed)
        if (p is Num || p is Sym) {
            val e = Ed(removeNode(ed.row, ed.cur.path, ed.cur.idx - 1), Cur(ed.cur.path, ed.cur.idx - 1))
            val e2 = insert(e, Frac(Row(listOf(p)), Row()))
            val i = e.cur.idx
            return Ed(e2.row, Cur(ed.cur.path + Step(i, 1), 0))
        }
        return insert(ed, Frac(Row(), Row()), 0)
    }

    fun firstEmpty(row: Row, path: List<Step> = emptyList()): Cur? {
        for ((i, n) in row.nodes.withIndex()) {
            val sl = slots(n)
            for ((j, s) in sl.withIndex()) {
                if (s.nodes.isEmpty()) return Cur(path + Step(i, j), 0)
                firstEmpty(s, path + Step(i, j))?.let { return it }
            }
        }
        return null
    }

    fun isBlank(row: Row) = row.nodes.isEmpty()
}

object Names {
    val display = mapOf(
        "limit" to "lim", "diff" to "d/dx", "log10" to "log", "logb" to "log", "comb" to "nCr", "perm" to "nPr",
        "irem" to "mod", "iquo" to "quo", "ifactor" to "factor#", "laplace" to "Laplace", "ilaplace" to "iLaplace",
        "ztrans" to "Ztrans", "fourier" to "Fourier", "eigenvals" to "eig", "eigenvects" to "eigvec",
        "polar_coordinates" to "polar", "shl" to "shl", "shr" to "shr", "not" to "not", "dms" to "▶DMS", "fromdms" to "DMS▶"
    )
    val trig = setOf("sin", "cos", "tan")
    val itrig = setOf("asin", "acos", "atan")
    fun show(n: String) = display[n] ?: n
}

class Ctx(val angle: Int = 1, val ans: String? = null, val pre: String? = null)

object Ser {
    private class Tok(var s: String, val kind: Int, val compound: Boolean = false)

    fun giac(row: Row, c: Ctx = Ctx()): String = tokens(row, c).joinToString("") { it.s }

    private fun tokens(row: Row, c: Ctx): List<Tok> {
        val out = ArrayList<Tok>()
        for (n in row.nodes) {
            when (n) {
                is Op -> out.add(Tok(opText(n.c), if (n.c == "!") 2 else 1))
                is Pow -> {
                    val b = out.lastOrNull()
                    val e = "^(" + giac(n.e, c) + ")"
                    if (b != null && b.kind == 0) {
                        b.s = (if (b.compound) "(" + b.s + ")" else b.s) + e
                        out[out.size - 1] = Tok(b.s, 0)
                    } else out.add(Tok("1" + e, 0))
                }
                else -> {
                    val t = operand(n, c)
                    val l = out.lastOrNull()
                    if (l != null && (l.kind == 0 || l.kind == 2)) out.add(Tok("*", 1))
                    out.add(t)
                }
            }
        }
        return out
    }

    private fun opText(c: String) = when (c) {
        "×" -> "*"; "÷" -> "/"; "−" -> "-"; "≤" -> "<="; "≥" -> ">="; "≠" -> "!="; "→" -> "->"; else -> c
    }

    private fun operand(n: Node, c: Ctx): Tok = when (n) {
        is Num -> Tok(if (n.t.endsWith(".")) n.t.dropLast(1) else n.t, 0)
        is Sym -> Tok(sym(n.n, c), 0, n.n == "Ans" || n.n == "PreAns")
        is Str -> Tok("\"" + n.s + "\"", 0)
        is Frac -> Tok("(" + giac(n.n, c) + ")/(" + giac(n.d, c) + ")", 0, true)
        is Root -> if (n.idx == null) Tok("sqrt(" + giac(n.r, c) + ")", 0)
        else Tok("simplify(surd(" + giac(n.r, c) + "," + giac(n.idx, c) + "))", 0)
        is Paren -> Tok("(" + giac(n.r, c) + ")", 0)
        is Lst -> Tok("[" + n.items.joinToString(",") { giac(it, c) } + "]", 0)
        is Mat -> Tok("[" + (0 until n.rows).joinToString(",") { r ->
            "[" + (0 until n.cols).joinToString(",") { k -> giac(n.cells[r * n.cols + k], c) } + "]"
        } + "]", 0)
        is Mixed -> Tok("((" + giac(n.w, c) + ")+(" + giac(n.n, c) + ")/(" + giac(n.d, c) + "))", 0)
        is Func -> Tok(func(n, c), 0)
        else -> Tok("", 0)
    }

    private fun sym(n: String, c: Ctx) = when (n) {
        "π" -> "pi"; "e" -> "exp(1)"; "∞" -> "inf"; "Ans" -> "(" + (c.ans ?: "0") + ")"; "PreAns" -> "(" + (c.pre ?: "0") + ")"; else -> n
    }

    private fun func(f: Func, c: Ctx): String {
        val a = f.args.map { giac(it, c) }
        val k = when (c.angle) { 0 -> "pi/180"; 2 -> "pi/200"; else -> null }
        return when {
            f.name == "shl" -> "((${a[0]})*2^(${a[1]}))"
            f.name == "shr" -> "iquo(${a[0]},2^(${a[1]}))"
            f.name == "not" -> "(-1-(${a[0]}))"
            f.name == "dms" -> "[iPart(${a[0]}),iPart(fPart(${a[0]})*60),fPart(fPart(${a[0]})*60)*60]"
            f.name == "fromdms" -> "((${a[0]})+(${a[1]})/60+(${a[2]})/3600)"
            k != null && f.name in Names.trig -> f.name + "((" + a.joinToString(",") + ")*" + k + ")"
            k != null && f.name in Names.itrig -> "simplify((" + f.name + "(" + a.joinToString(",") + "))/(" + k + "))"
            else -> f.name + "(" + a.joinToString(",") + ")"
        }
    }
}

private sealed class A
private class AN(val t: String) : A()
private class AS(val n: String) : A()
private class AStr(val s: String) : A()
private class AB(val op: String, val l: A, val r: A) : A()
private class AU(val op: String, val a: A) : A()
private class AF(val name: String, val args: List<A>) : A()
private class AL(val items: List<A>) : A()
private class AP(val a: A) : A()
private class APost(val op: String, val a: A) : A()
private class ASeq(val items: List<A>) : A()

class ParseError(m: String) : Exception(m)

object Parse {
    private val order = Regex("""\+?\s*([A-Za-z])\^(\d+)\*order_size\(\1\)""")

    fun row(src: String): Row? = try { rowOrThrow(src) } catch (e: Exception) { null }

    fun rowOrThrow(src0: String): Row {
        val src = order.replace(src0.trim()) { "+O(" + it.groupValues[1] + "^" + it.groupValues[2] + ")" }
        val p = P(tokenize(src))
        val a = p.top()
        return Row(conv(a))
    }

    private fun tokenize(s: String): List<String> {
        val t = ArrayList<String>()
        var i = 0
        while (i < s.length) {
            val c = s[i]
            when {
                c.isWhitespace() -> i++
                c.isDigit() || (c == '.' && i + 1 < s.length && s[i + 1].isDigit()) -> {
                    var j = i
                    if (c == '0' && i + 1 < s.length && s[i + 1] in "xXbBoO") {
                        j = i + 2
                        while (j < s.length && s[j].isLetterOrDigit()) j++
                    } else {
                        while (j < s.length && s[j].isDigit()) j++
                        if (j < s.length && s[j] == '.' && !(j + 1 < s.length && s[j + 1] == '.')) { j++; while (j < s.length && s[j].isDigit()) j++ }
                        if (j < s.length && (s[j] == 'e' || s[j] == 'E')) {
                            var k = j + 1
                            if (k < s.length && (s[k] == '+' || s[k] == '-')) k++
                            if (k < s.length && s[k].isDigit()) { while (k < s.length && s[k].isDigit()) k++; j = k }
                        }
                    }
                    t.add(s.substring(i, j)); i = j
                }
                c.isLetter() || c == '_' -> {
                    var j = i
                    while (j < s.length && (s[j].isLetterOrDigit() || s[j] == '_')) j++
                    while (j < s.length && s[j] == '\'') j++
                    t.add(s.substring(i, j)); i = j
                }
                c == '"' -> {
                    val j = s.indexOf('"', i + 1)
                    if (j < 0) throw ParseError("string")
                    t.add(s.substring(i, j + 1)); i = j + 1
                }
                else -> {
                    val two = if (i + 1 < s.length) s.substring(i, i + 2) else ""
                    if (two in setOf(":=", "<=", ">=", "!=", "==", "->", "..", ".^")) { t.add(two); i += 2 }
                    else if (c in "+-*/^()[],=<>%!|") { t.add(c.toString()); i++ }
                    else throw ParseError("char $c")
                }
            }
        }
        return t
    }

    private class P(val t: List<String>) {
        var i = 0
        fun peek() = t.getOrNull(i)
        fun next() = t[i++]
        fun eat(s: String): Boolean { if (peek() == s) { i++; return true }; return false }
        fun top(): A {
            val items = arrayListOf(expr())
            while (eat(",")) items.add(expr())
            if (i != t.size) throw ParseError("trailing ${peek()}")
            return if (items.size == 1) items[0] else ASeq(items)
        }
        fun expr(): A = arrow()
        fun arrow(): A { val l = logic(); return if (eat("->")) AB("->", l, arrow()) else l }
        fun logic(): A {
            var l = assign()
            while (peek() == "and" || peek() == "or") { val o = next(); l = AB(o, l, assign()) }
            return l
        }
        fun assign(): A { val l = cmp(); return if (eat(":=")) AB(":=", l, assign()) else l }
        fun cmp(): A {
            val l = add()
            val o = peek()
            if (o in setOf("<", ">", "<=", ">=", "=", "==", "!=")) { next(); return AB(o!!, l, add()) }
            return l
        }
        fun add(): A {
            var l = mul()
            while (peek() == "+" || peek() == "-") { val o = next(); l = AB(o, l, mul()) }
            return l
        }
        fun mul(): A {
            var l = unary()
            while (true) {
                val o = peek()
                if (o == "*" || o == "/" || o == "%") { next(); l = AB(o, l, unary()) }
                else if (o == "_" && t.getOrNull(i + 1) == "(") l = AB("*", l, unary())
                else if (o != null && o.startsWith("_") && o.length > 1) { next(); l = AB("*", l, AS(o)) }
                else break
            }
            return l
        }
        fun unary(): A {
            if (peek() == "-") { next(); return AU("-", unary()) }
            if (peek() == "+") { next(); return unary() }
            return pow()
        }
        fun pow(): A {
            val b = post()
            return if (eat("^")) AB("^", b, unary()) else if (eat(".^")) AB(".^", b, unary()) else b
        }
        fun post(): A {
            var a = atom()
            while (peek() == "!" && t.getOrNull(i + 1) != "=") { next(); a = APost("!", a) }
            return a
        }
        fun args(close: String): List<A> {
            val r = ArrayList<A>()
            if (eat(close)) return r
            do { r.add(expr()) } while (eat(","))
            if (!eat(close)) throw ParseError("expected $close")
            return r
        }
        fun atom(): A {
            val s = peek() ?: throw ParseError("eof")
            next()
            return when {
                s[0].isDigit() || s[0] == '.' -> AN(s)
                s[0] == '"' -> AStr(s.substring(1, s.length - 1))
                s == "(" -> { val e = expr(); if (!eat(")")) throw ParseError("expected )"); AP(e) }
                s == "[" -> AL(args("]"))
                s[0].isLetter() || s[0] == '_' -> when {
                    peek() == "(" -> { next(); AF(s, args(")")) }
                    peek() == "[" && (s == "list" || s == "matrix") -> { next(); AL(args("]")) }
                    else -> AS(s)
                }
                else -> throw ParseError("unexpected $s")
            }
        }
    }

    private fun noParen(a: A): Row = Row(conv(if (a is AP) a.a else a))

    private fun conv(a: A): List<Node> = when (a) {
        is AN -> listOf(Num(a.t))
        is AS -> listOf(when (a.n) { "pi" -> Sym("π"); "inf", "infinity" -> Sym("∞"); else -> Sym(a.n) })
        is AStr -> listOf(Str(a.s))
        is AP -> listOf(Paren(Row(conv(a.a))))
        is APost -> conv(a.a) + Op(a.op)
        is AU -> listOf<Node>(Op("-")) + (if (a.a is AB && (a.a.op == "+" || a.a.op == "-")) listOf(Paren(Row(conv(a.a)))) else conv(a.a))
        is AB -> when (a.op) {
            "/" -> listOf(Frac(noParen(a.l), noParen(a.r)))
            "^" -> conv(a.l) + Pow(noParen(a.r))
            else -> conv(a.l) + Op(a.op) + conv(a.r)
        }
        is AL -> {
            val it = a.items
            if (it.isNotEmpty() && it.all { x -> x is AL } && it.map { x -> (x as AL).items.size }.distinct().size == 1 && (it[0] as AL).items.isNotEmpty())
                listOf(Mat(it.size, (it[0] as AL).items.size, it.flatMap { x -> (x as AL).items.map { y -> noParen(y) } }))
            else listOf(Lst(it.map { x -> noParen(x) }))
        }
        is ASeq -> a.items.flatMapIndexed { k, x -> if (k > 0) listOf<Node>(Op(",")) + conv(x) else conv(x) }
        is AF -> when {
            a.name == "sqrt" && a.args.size == 1 -> listOf(Root(null, noParen(a.args[0])))
            a.name == "surd" && a.args.size == 2 -> listOf(Root(noParen(a.args[1]), noParen(a.args[0])))
            a.name == "simplify" && a.args.size == 1 && a.args[0] is AF && (a.args[0] as AF).name == "surd" && (a.args[0] as AF).args.size == 2 ->
                (a.args[0] as AF).let { listOf(Root(noParen(it.args[1]), noParen(it.args[0]))) }
            a.name == "exp" && a.args.size == 1 && a.args[0].let { it is AN && it.t == "1" } -> listOf(Sym("e"))
            a.name == "exp" && a.args.size == 1 -> listOf(Sym("e"), Pow(noParen(a.args[0])))
            a.name == "_" && a.args.size == 1 -> listOf(Paren(noParen(a.args[0])))
            else -> listOf(Func(a.name, a.args.map { noParen(it) }))
        }
    }
}

object Fmt {
    fun rows(r: Row, eng: Boolean, sig: Int, mixed: Boolean): Row = Row(r.nodes.flatMap { node(it, eng, sig, mixed) })

    private fun node(n: Node, eng: Boolean, sig: Int, mixed: Boolean): List<Node> {
        fun m(r: Row) = rows(r, eng, sig, mixed)
        return when (n) {
            is Num -> if (n.t.contains('.') || n.t.contains('e') && !n.t.startsWith("0x")) number(n.t, eng, sig) else listOf(n)
            is Frac -> {
                val a = n.n.nodes.singleOrNull()
                val b = n.d.nodes.singleOrNull()
                if (mixed && a is Num && b is Num && a.t.all { it.isDigit() } && b.t.all { it.isDigit() } && b.t != "0" &&
                    BigInteger(a.t) > BigInteger(b.t)
                ) {
                    val q = BigInteger(a.t).divideAndRemainder(BigInteger(b.t))
                    if (q[1].signum() == 0) listOf(Num(q[0].toString()))
                    else listOf(Mixed(Row(listOf(Num(q[0].toString()))), Row(listOf(Num(q[1].toString()))), Row(listOf(b))))
                } else listOf(Frac(m(n.n), m(n.d)))
            }
            is Root -> listOf(Root(n.idx?.let { m(it) }, m(n.r)))
            is Pow -> listOf(Pow(m(n.e)))
            is Paren -> listOf(Paren(m(n.r)))
            is Lst -> listOf(Lst(n.items.map { m(it) }))
            is Func -> listOf(Func(n.name, n.args.map { m(it) }))
            is Mat -> listOf(Mat(n.rows, n.cols, n.cells.map { m(it) }))
            else -> listOf(n)
        }
    }

    fun number(t: String, eng: Boolean, sig: Int): List<Node> {
        val bd = try { BigDecimal(t) } catch (e: Exception) { return listOf(Num(t)) }
        if (bd.signum() == 0) return listOf(Num("0"))
        var v = bd
        if (sig > 0) v = v.round(MathContext(sig, RoundingMode.HALF_UP))
        val exp10 = v.precision() - v.scale() - 1
        val needExp = eng || exp10 >= 12 || exp10 <= -5
        if (!needExp) return listOf(Num(plain(v)))
        val e = if (eng) Math.floorDiv(exp10, 3) * 3 else exp10
        val mant = v.movePointLeft(e).stripTrailingZeros()
        return listOf(Num(plain(mant)), Op("×"), Num("10"), Pow(Row(listOf(Num(e.toString())))))
    }

    private fun plain(v: BigDecimal): String {
        val s = v.stripTrailingZeros().toPlainString()
        return if (s.contains('.')) s.trimEnd('0').trimEnd('.') else s
    }

    private val intRe = Regex("-?\\d+")

    fun base(exact: String, base: Int): String? {
        if (base == 10 || !intRe.matches(exact)) return null
        val v = BigInteger(exact)
        val pre = when (base) { 2 -> "0b"; 8 -> "0o"; else -> "0x" }
        return (if (v.signum() < 0) "-" else "") + pre + v.abs().toString(base).uppercase()
    }
}
