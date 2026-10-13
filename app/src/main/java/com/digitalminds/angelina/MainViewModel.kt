package com.digitalminds.angelina

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.MathContext

sealed interface Screen {
    data object Home : Screen
    data object Matrix : Screen
    data object Stats : Screen
    data object Table : Screen
    data object Graph : Screen
    data object Programs : Screen
    data object Units : Screen
    data object Guide : Screen
    data object Settings : Screen
}

data class MxState(val name: Char = 'A', val dims: Map<Char, Pair<Int, Int>> = mapOf('A' to (3 to 3), 'B' to (3 to 3), 'C' to (3 to 3)), val vdim: Int = 3, val out: String? = null, val dec: String? = null, val err: String? = null)
data class StState(val two: Boolean = false, val rows: Int = 10, val reg: Int = 0, val dist: Int = 0, val mode: Int = 0, val out: List<Pair<String, String>> = emptyList(), val err: String? = null, val dout: String? = null)
data class PgState(val list: List<Prog> = emptyList(), val cur: Int = 0, val name: String = "prog1", val src: String = "", val out: String = "", val ok: Boolean = true)

/** Graph viewport lives here so it survives mode switches; read only in draw phases. */
class GView {
    var cx by mutableFloatStateOf(0f)
    var cy by mutableFloatStateOf(0f)
    var sx by mutableFloatStateOf(40f)
    var sy by mutableFloatStateOf(40f)
    var trace by mutableFloatStateOf(Float.NaN)
    var anchor by mutableFloatStateOf(Float.NaN)
    var sel by mutableStateOf(0)
    var info by mutableStateOf("")
}

class MainViewModel : ViewModel() {
    private val store = Services.store
    private val cas = Services.cas

    val screen = MutableStateFlow<Screen>(Screen.Home)
    val overlay = MutableStateFlow(0)
    val settings = MutableStateFlow(store.loadSettings())
    val history = MutableStateFlow(store.loadHistory())
    val busy = MutableStateFlow(false)
    val error = MutableStateFlow<String?>(null)
    val focus = MutableStateFlow("home")
    val mx = MutableStateFlow(MxState())
    val st = MutableStateFlow(StState())
    val pg = MutableStateFlow(PgState())
    val gv = GView()

    private val fields = HashMap<String, MutableStateFlow<Ed>>()
    private val defs = store.loadDefs()
    private var ans: String? = history.value.lastOrNull { it.exact != null }?.exact
    private var nextId = (history.value.maxOfOrNull { it.id } ?: 0L) + 1

    val funcs: List<FDef> by lazy {
        Services.app.assets.open("functions.txt").bufferedReader().readLines().mapNotNull { l ->
            val p = l.split("|", limit = 3)
            if (p.size == 3) FDef(p[0], p[1].toIntOrNull() ?: 1, p[2]) else null
        }.distinctBy { it.name }
    }

    init {
        cas.timeoutMs = settings.value.timeout * 1000L
        cas.setDefinitions(defs.values.toList())
        val progs = store.loadProgs()
        pg.value = PgState(progs, 0, progs.firstOrNull()?.name ?: "prog1", progs.firstOrNull()?.src ?: "")
    }

    fun field(id: String): MutableStateFlow<Ed> = fields.getOrPut(id) { MutableStateFlow(Ed()) }
    private var preAns: String? = null
    private fun ctx() = Ctx(settings.value.angle, ans, preAns)

    fun cancel() = cas.cancel()

    // ---------- navigation and settings ----------
    fun go(s: Screen) {
        screen.value = s
        overlay.value = 0
        error.value = null
        focus.value = when (s) {
            Screen.Home -> "home"
            Screen.Table -> "T:f"
            Screen.Graph -> "G:0"
            Screen.Matrix -> "M:${mx.value.name}:0:0"
            Screen.Stats -> "S:x:0"
            else -> focus.value
        }
    }

    fun setSettings(s: Settings) {
        settings.value = s
        cas.timeoutMs = s.timeout * 1000L
        store.saveSettings(s)
    }

    fun cycleAngle() = setSettings(settings.value.copy(angle = if (settings.value.angle == 0) 1 else 0))
    fun setPage(p: Int) { if (p != settings.value.page) setSettings(settings.value.copy(page = p)) }

    fun clearHistory() { history.value = emptyList(); store.saveHistory(emptyList()) }
    fun clearDefs() { defs.clear(); store.saveDefs(defs); cas.setDefinitions(emptyList()); cas.cancel() }

    // ---------- editing ----------
    fun focusOn(id: String) { focus.value = id }

    fun swipe(dir: Int) = edit { if (dir > 0) Editor.right(it) else Editor.left(it) }

    fun place(id: String, c: Cur) {
        focus.value = id
        val f = field(id)
        f.value = Editor.place(f.value, c)
    }

    private inline fun edit(f: (Ed) -> Ed) {
        val fl = field(focus.value)
        fl.value = f(fl.value)
    }

    fun insertNode(n: Node) = edit { Editor.insert(it, n) }

    fun setField(id: String, row: Row) { field(id).value = Ed(row, Cur(emptyList(), row.nodes.size)) }

    fun stClear() { for (i in 0 until 200) { field("S:x:$i").value = Ed(); field("S:y:$i").value = Ed() } }

    fun graphParam(): Pair<DFn, DFn>? {
        val a = NumEval.parse(field("G:px").value.row)?.let { NumEval.compile(it, "t", settings.value.angle) } ?: return null
        val b = NumEval.parse(field("G:py").value.row)?.let { NumEval.compile(it, "t", settings.value.angle) } ?: return null
        return a to b
    }

    fun tableEx(id: String): Ex? = NumEval.parse(field(id).value.row)

    /** Adds or removes rows/columns of the matrix the cursor is in (Home only). */
    fun matResize(dr: Int, dc: Int) {
        val f = field("home")
        Editor.resizeMat(f.value, dr, dc)?.let { f.value = it }
    }

    fun setHomeRow(row: Row) { field("home").value = Ed(row, Cur(emptyList(), row.nodes.size)) }

    fun onKey(k: KA) {
        error.value = null
        when (k) {
            is KA.T -> edit { text(it, k.s) }
            is KA.F -> edit { func(it, k.name, k.pre) }
            is KA.Sp -> special(k.s)
        }
    }

    private val ops = setOf("+", "−", "×", "÷", "=", "<", ">", "≤", "≥", "≠", "!", ":=", " and ", " or ")

    private fun text(e: Ed, s: String): Ed = if (s in ops) Editor.op(e, s.trim()) else Editor.typeText(e, s)

    private fun preRow(s: String): Row = when {
        s.isEmpty() -> Row()
        s == "[]" -> Row(listOf(Lst(listOf(Row()))))
        s.endsWith("=") -> Row((Parse.row(s.dropLast(1))?.nodes ?: emptyList()) + Op("="))
        else -> Parse.row(s) ?: Row()
    }

    fun func(e: Ed, name: String, pre: List<String>): Ed {
        val node = Func(name, pre.map { preRow(it) })
        val idx = e.cur.idx
        val ins = Editor.insert(e, node)
        val fe = Editor.firstEmpty(Row(listOf(node))) ?: return ins
        val path = e.cur.path + fe.path.mapIndexed { k, s -> if (k == 0) Step(idx, s.slot) else s }
        return Ed(ins.row, Cur(path, 0))
    }

    private fun special(s: S) {
        when (s) {
            S.AC -> edit { Ed() }
            S.DEL -> edit { Editor.back(it) }
            S.LEFT -> edit { Editor.left(it) }
            S.RIGHT -> edit { Editor.right(it) }
            S.EQ -> if (focus.value == "home") evaluate()
            S.LP -> edit { Editor.insert(it, Paren(Row()), 0) }
            S.RP -> edit { Editor.closeParen(it) }
            S.COMMA -> edit { Editor.comma(it) }
            S.FRAC -> edit { Editor.fracKey(it) }
            S.MIXED -> edit { Editor.insert(it, Mixed(Row(), Row(), Row()), 0) }
            S.SQRT -> edit { Editor.insert(it, Root(null, Row()), 0) }
            S.NROOT -> edit { Editor.insert(it, Root(Row(), Row()), 0) }
            S.CBRT -> edit { Editor.insert(it, Root(Row(listOf(Num("3"))), Row()), 1) }
            S.POW -> edit { Editor.insert(it, Pow(Row()), 0) }
            S.SQUARE -> edit { Editor.insert(it, Pow(Row(listOf(Num("2"))))) }
            S.CUBE -> edit { Editor.insert(it, Pow(Row(listOf(Num("3"))))) }
            S.INV -> edit { Editor.insert(it, Pow(Row(listOf(Op("-"), Num("1"))))) }
            S.EXP10 -> edit { Editor.insert(Editor.insert(it, Num("10")), Pow(Row()), 0) }
            S.EXPE -> edit { Editor.insert(Editor.insert(it, Sym("e")), Pow(Row()), 0) }
            S.X10 -> edit { Editor.insert(Editor.insert(Editor.insert(it, Op("×")), Num("10")), Pow(Row()), 0) }
            S.ANS -> edit { Editor.insert(it, Sym("Ans")) }
            S.MAT2 -> edit { Editor.insert(it, Mat(2, 2, List(4) { Row() }), 0) }
            S.MAT3 -> edit { Editor.insert(it, Mat(3, 3, List(9) { Row() }), 0) }
            S.MAT4 -> edit { Editor.insert(it, Mat(4, 4, List(16) { Row() }), 0) }
            S.LST -> edit { Editor.insert(it, Lst(listOf(Row())), 0) }
            S.BASE -> setSettings(settings.value.copy(base = when (settings.value.base) { 10 -> 2; 2 -> 8; 8 -> 16; else -> 10 }))
        }
    }

    // ---------- Home evaluation ----------
    private val defRe = Regex("""^\s*([A-Za-z][A-Za-z0-9_]*)\s*(\([^)]*\))?\s*:=""")

    fun friendly(m: String?): String {
        val t = m?.trim() ?: return "Something went wrong."
        return when {
            t.contains("Bad Argument", true) -> "Check the arguments of this function."
            t.contains("Syntax", true) || t.contains("parse", true) -> "Syntax error. Check brackets and operators."
            t.contains("Dimension", true) -> "The sizes do not match."
            t.contains("division by 0", true) || t.contains("Division", true) -> "Division by zero."
            t.contains("Incompatible", true) -> "Incompatible units."
            t.length > 90 -> t.take(90) + "..."
            else -> t
        }
    }

    private suspend fun approxOf(exact: String): String? {
        if (exact.startsWith("\"") || exact.contains("->") || Regex("-?\\d+(\\.\\d+)?").matches(exact)) return null
        val sig = settings.value.sig
        val r = cas.eval(if (sig > 0) "evalf($exact,$sig)" else "evalf($exact)")
        return if (r.ok && r.out != null && r.out != exact) r.out else null
    }

    fun evaluate() {
        val ed = field("home").value
        if (Editor.isBlank(ed.row) || busy.value) return
        Editor.firstEmpty(ed.row)?.let {
            field("home").value = Ed(ed.row, it)
            error.value = "Fill in the empty box."
            return
        }
        run(Ser.giac(ed.row, ctx()), ed)
    }

    fun runSource(src: String) = run(src, null)

    /** Verify: a = b is proven by simplify(a-b) == 0 in the CAS. */
    fun verify() {
        val ed = field("home").value
        val i = ed.row.nodes.indexOfFirst { it is Op && it.c == "=" }
        if (i <= 0 || i == ed.row.nodes.lastIndex) { error.value = "Write an equality such as a = b to verify it."; return }
        Editor.firstEmpty(ed.row)?.let { field("home").value = Ed(ed.row, it); error.value = "Fill in the empty box."; return }
        val l = Ser.giac(Row(ed.row.nodes.take(i)), ctx())
        val r = Ser.giac(Row(ed.row.nodes.drop(i + 1)), ctx())
        viewModelScope.launch {
            busy.value = true
            error.value = null
            val res = cas.eval("simplify(($l)-($r))")
            if (!res.ok) error.value = friendly(res.error)
            else {
                val ok = res.out!!.trim() == "0"
                history.update { (it + Hist(nextId++, Ser.giac(ed.row, ctx()), if (ok) "true" else "false", null, null)).takeLast(200) }
                store.saveHistory(history.value)
                field("home").value = Ed()
            }
            busy.value = false
        }
    }

    private fun run(src: String, ed: Ed?) {
        viewModelScope.launch {
            busy.value = true
            error.value = null
            val r = cas.eval(src)
            if (!r.ok) {
                error.value = friendly(r.error)
                if (ed != null) field("home").value = Ed(ed.row, Cur(emptyList(), ed.row.nodes.size))
            } else {
                val exact = r.out!!.trim()
                if (exact == "undef" || exact.isEmpty()) error.value = "The result is undefined."
                else {
                    val ap = approxOf(exact)
                    val h = Hist(nextId++, src, exact, ap, null)
                    history.update { (it + h).takeLast(200) }
                    store.saveHistory(history.value)
                    preAns = ans
                    ans = exact
                    defRe.find(src)?.let { m ->
                        defs[m.groupValues[1]] = src
                        store.saveDefs(defs)
                        cas.setDefinitions(defs.values.toList())
                    }
                    if (ed != null) field("home").value = Ed()
                }
            }
            busy.value = false
        }
    }

    fun toggle(id: Long) {
        history.update { l -> l.map { if (it.id == id && it.approx != null) it.copy(dec = !it.dec) else it } }
    }

    fun recall(h: Hist) { setHomeRow(Parse.row(h.input) ?: Row()); go(Screen.Home) }
    fun useAns(h: Hist) { ans = h.exact }

    // ---------- Search, Guide, Units ----------
    fun insertFunc(f: FDef) {
        val rows = List(f.argc) { "" }
        if (f.argc == 0) insertNode(Func(f.name, emptyList())) else edit { func(it, f.name, rows) }
        overlay.value = 0
        if (screen.value != Screen.Home && focus.value == "home") go(Screen.Home)
    }

    fun sendExpr(expr: String, run: Boolean) {
        setHomeRow(Parse.row(expr) ?: Row(listOf(Str(expr))))
        go(Screen.Home)
        if (run) evaluate()
    }

    fun insertConst(c: ConstDef) {
        insertNode(Num(c.value))
        go(Screen.Home)
    }

    // ---------- Matrix / vector ----------
    fun mxSelect(c: Char) { mx.update { MxState(c, it.dims, it.vdim, it.out, it.dec, it.err) }; focus.value = "M:$c:0:0" }

    fun mxResize(dr: Int, dc: Int) = mx.update { s ->
        if (s.name in "UV") MxState(s.name, s.dims, (s.vdim + dr + dc).coerceIn(2, 3), s.out, s.dec, s.err)
        else {
            val (r, c) = s.dims[s.name]!!
            MxState(s.name, s.dims + (s.name to ((r + dr).coerceIn(1, 8) to (c + dc).coerceIn(1, 8))), s.vdim, s.out, s.dec, s.err)
        }
    }

    private fun cellText(id: String): String = field(id).value.row.let { if (it.nodes.isEmpty()) "0" else Ser.giac(it, ctx()) }

    fun mLit(n: Char): String {
        val (r, c) = mx.value.dims[n]!!
        return "[" + (0 until r).joinToString(",") { i -> "[" + (0 until c).joinToString(",") { j -> cellText("M:$n:$i:$j") } + "]" } + "]"
    }

    fun vLit(n: Char) = "[" + (0 until mx.value.vdim).joinToString(",") { cellText("M:$n:$it:0") } + "]"

    fun mxOp(op: String) {
        val a = mLit('A'); val b = mLit('B')
        val (ra, ca) = mx.value.dims['A']!!
        val e = when (op) {
            "det", "inv", "rref", "lu", "qr" -> "$op($a)"
            "T" -> "transpose($a)"
            "eig" -> "eigenvals($a)"
            "eigvec" -> "eigenvects($a)"
            "A+B" -> "$a+$b"
            "A*B" -> "$a*$b"
            "A^n" -> "$a^(${cellText("M:pow:0:0")})"
            "Ax=b" -> {
                if (ra != ca) { mx.update { MxState(it.name, it.dims, it.vdim, null, null, "A must be square.") }; return }
                val bv = (0 until ra).map { cellText("M:U:$it:0") }
                val eq = (0 until ra).joinToString(",") { i -> (0 until ca).joinToString("+") { j -> "(${cellText("M:A:$i:$j")})*x${j + 1}" } + "=" + bv[i] }
                "linsolve([$eq],[" + (1..ca).joinToString(",") { "x$it" } + "])"
            }
            "dot" -> "dot(${vLit('U')},${vLit('V')})"
            "cross" -> "cross(${vLit('U')},${vLit('V')})"
            "norm" -> "norm(${vLit('U')})"
            else -> return
        }
        viewModelScope.launch {
            busy.value = true
            val r = cas.eval(e)
            if (r.ok) {
                val o = r.out!!.trim()
                val ap = approxOf(o)
                mx.update { MxState(it.name, it.dims, it.vdim, o, ap, null) }
            } else mx.update { MxState(it.name, it.dims, it.vdim, null, null, friendly(r.error)) }
            busy.value = false
        }
    }

    fun mxSave() {
        val n = mx.value.name
        val src = if (n in "UV") "$n:=${vLit(n)}" else "$n:=${mLit(n)}"
        run(src, null)
    }

    // ---------- Stats ----------
    private fun col(c: String, n: Int) = (0 until n).mapNotNull { i -> field("S:$c:$i").value.row.takeIf { it.nodes.isNotEmpty() }?.let { Ser.giac(it, ctx()) } }

    fun stSet(f: (StState) -> StState) = st.update(f)

    fun stCompute() {
        val s = st.value
        viewModelScope.launch {
            busy.value = true
            val xs = col("x", 200)
            val out = ArrayList<Pair<String, String>>()
            var err: String? = null
            if (xs.isEmpty()) err = "Enter data in the X column first."
            else if (!s.two) {
                val l = "[" + xs.joinToString(",") + "]"
                for ((lab, e) in listOf("n" to "size($l)", "mean" to "evalf(mean($l))", "median" to "evalf(median($l))", "σ (population)" to "evalf(stddev($l))",
                    "s (sample)" to "evalf(stddevp($l))", "Q1" to "evalf(quartile1($l))", "Q3" to "evalf(quartile3($l))", "min" to "min($l)", "max" to "max($l)", "Σx" to "sum($l)")) {
                    val r = cas.eval(e)
                    out.add(lab to (r.out ?: friendly(r.error)))
                }
            } else {
                val ys = col("y", 200)
                if (ys.size != xs.size) err = "X and Y need the same number of values."
                else regression(s.reg, "[" + xs.joinToString(",") + "]", "[" + ys.joinToString(",") + "]", out).let { err = it }
            }
            st.update { StState(it.two, it.rows, it.reg, it.dist, it.mode, out, err, it.dout) }
            busy.value = false
        }
    }

    private suspend fun regression(type: Int, x: String, y: String, out: MutableList<Pair<String, String>>): String? {
        val call = when (type) {
            0 -> "linear_regression($x,$y)"; 1 -> "polynomial_regression($x,$y,2)"; 2 -> "polynomial_regression($x,$y,3)"
            3 -> "logarithmic_regression($x,$y)"; 4 -> "exponential_regression($x,$y)"; else -> "power_regression($x,$y)"
        }
        val r = cas.eval("evalf($call)")
        if (!r.ok) return friendly(r.error)
        val c = r.out!!.trim().trim('[', ']').split(",").map { it.trim() }
        fun p(v: String) = "(" + v + ")"
        val model = when (type) {
            0 -> "${p(c[0])}*t+${p(c[1])}"
            1 -> "${p(c[0])}*t^2+${p(c[1])}*t+${p(c[2])}"
            2 -> "${p(c[0])}*t^3+${p(c[1])}*t^2+${p(c[2])}*t+${p(c[3])}"
            3 -> "${p(c[0])}*ln(t)+${p(c[1])}"
            4 -> "${p(c[1])}*${p(c[0])}^t"
            else -> "${p(c[1])}*t^${p(c[0])}"
        }
        val names = when (type) { 0 -> listOf("a", "b"); 1 -> listOf("a", "b", "c"); 2 -> listOf("a", "b", "c", "d"); 3 -> listOf("a", "b"); 4 -> listOf("a (base)", "b"); else -> listOf("m", "a") }
        out.add("model" to when (type) {
            0 -> "y=a*x+b"; 1 -> "y=a*x^2+b*x+c"; 2 -> "y=a*x^3+b*x^2+c*x+d"; 3 -> "y=a*ln(x)+b"; 4 -> "y=b*a^x"; else -> "y=a*x^m"
        })
        c.forEachIndexed { i, v -> out.add(names.getOrElse(i) { "c$i" } to v) }
        val r2 = cas.eval("evalf(1-sum(($y-map($x,t->$model)).^2)/sum(($y-mean($y)).^2))")
        out.add("R²" to (r2.out ?: friendly(r2.error)))
        return null
    }

    fun distCompute() {
        val s = st.value
        val d = Dists.all[s.dist]
        viewModelScope.launch {
            busy.value = true
            val ps = d.params.indices.map { cellText("D:p${it}") }
            val x = cellText("D:x")
            val fn = when (s.mode) { 0 -> d.pdf; 1 -> d.cdf; else -> d.inv }
            val r = cas.eval("evalf($fn(${(ps + x).joinToString(",")}))")
            st.update { StState(it.two, it.rows, it.reg, it.dist, it.mode, it.out, it.err, r.out ?: friendly(r.error)) }
            busy.value = false
        }
    }

    // ---------- Programs ----------
    fun pgNew() = pg.update { PgState(it.list, it.list.size, "prog${it.list.size + 1}", "", "", true) }
    fun pgSelect(i: Int) = pg.update { s -> s.list.getOrNull(i)?.let { PgState(s.list, i, it.name, it.src, "", true) } ?: s }
    fun pgEdit(name: String, src: String) = pg.update { PgState(it.list, it.cur, name, src, it.out, it.ok) }
    fun pgTemplate(i: Int) = pg.update { PgState(it.list, it.cur, Templates.programs[i].first.lowercase().replace(' ', '_'), Templates.programs[i].second, "", true) }

    fun pgSave() {
        pg.update { s ->
            val p = Prog(s.name.ifBlank { "prog" }, s.src)
            val l = if (s.cur < s.list.size) s.list.toMutableList().also { it[s.cur] = p } else s.list + p
            store.saveProgs(l)
            PgState(l, l.indexOf(p).coerceAtLeast(0), p.name, p.src, s.out, s.ok)
        }
    }

    fun pgDelete() = pg.update { s ->
        if (s.cur >= s.list.size) PgState(s.list) else {
            val l = s.list.toMutableList().also { it.removeAt(s.cur) }
            store.saveProgs(l)
            PgState(l, 0, l.firstOrNull()?.name ?: "prog1", l.firstOrNull()?.src ?: "")
        }
    }

    fun pgRun() {
        val s = pg.value
        viewModelScope.launch {
            busy.value = true
            val r = cas.eval(s.src.replace("\n", " "))
            pg.update { PgState(it.list, it.cur, it.name, it.src, if (r.ok) r.out!!.trim() else friendly(r.error), r.ok) }
            if (r.ok) {
                defs["prog:" + s.name] = s.src.replace("\n", " ")
                store.saveDefs(defs)
                cas.setDefinitions(defs.values.toList())
            }
            busy.value = false
        }
    }

    // ---------- Graph ----------
    private fun gnum(s: String?): Double? = s?.let { Regex("-?\\d+(\\.\\d+)?([eE][-+]?\\d+)?").find(it)?.value?.toDoubleOrNull() }

    fun graphFn(i: Int): DFn? {
        val row = field("G:$i").value.row
        val consts = HashMap<String, Double>()
        return NumEval.parse(row, consts)?.let { NumEval.compile(it, "x", settings.value.angle) }
    }

    fun graphAnalyze(kind: Int) {
        val i = gv.sel
        val f = field("G:$i").value.row
        if (Editor.isBlank(f)) { gv.info = "Enter a function first."; return }
        val fs = Ser.giac(f, ctx())
        val x0 = if (gv.trace.isNaN()) gv.cx.toDouble() else gv.trace.toDouble()
        viewModelScope.launch {
            busy.value = true
            val expr = when (kind) {
                0 -> "fsolve(($fs)=0,x=$x0)"
                1, 2 -> "fsolve(diff($fs,x)=0,x=$x0)"
                3 -> {
                    val j = (1..3).map { (i + it) % 4 }.firstOrNull { !Editor.isBlank(field("G:$it").value.row) }
                    if (j == null) { gv.info = "Enter a second function."; busy.value = false; return@launch }
                    "fsolve(($fs)=(${Ser.giac(field("G:$j").value.row, ctx())}),x=$x0)"
                }
                else -> {
                    if (gv.anchor.isNaN() || gv.trace.isNaN()) { gv.info = "Tap to set a start (A), then move to the end."; busy.value = false; return@launch }
                    "evalf(int($fs,x,${gv.anchor},${gv.trace}))"
                }
            }
            val r = cas.eval(expr)
            val v = gnum(r.out)
            if (!r.ok || v == null) gv.info = friendly(r.error ?: "No result near the trace point.")
            else if (kind == 4) gv.info = "Integral = " + fmt(v)
            else {
                gv.trace = v.toFloat()
                val y = graphFn(i)?.f(v)
                val tag = when (kind) {
                    0 -> "Zero"
                    3 -> "Intersection"
                    else -> {
                        val fn = graphFn(i)
                        val h = 1e-3 * maxOf(1.0, kotlin.math.abs(v))
                        val c = fn?.f(v) ?: Double.NaN
                        val l = fn?.f(v - h) ?: Double.NaN
                        val rr = fn?.f(v + h) ?: Double.NaN
                        if (c <= l && c <= rr) "Minimum" else if (c >= l && c >= rr) "Maximum" else "Critical point"
                    }
                }
                gv.info = "$tag: x = ${fmt(v)}" + (if (y != null) ", y = ${fmt(y)}" else "")
            }
            busy.value = false
        }
    }

    fun fmt(v: Double): String {
        if (v.isNaN() || v.isInfinite()) return "undefined"
        val sig = settings.value.sig.let { if (it > 0) it else 8 }
        val nodes = Fmt.number(BigDecimal(v, MathContext(15)).toPlainString(), settings.value.eng, sig)
        return Ser.giac(Row(nodes)).replace("*10^(", "e").replace(")", "")
    }

    fun tableToGraph() {
        field("G:0").value = field("T:f").value
        field("G:1").value = field("T:g").value
        go(Screen.Graph)
    }

    fun graphToTable() {
        field("T:f").value = field("G:0").value
        field("T:g").value = field("G:1").value
        go(Screen.Table)
    }
}
