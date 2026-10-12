package com.digitalminds.angelina

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

@Composable
private fun Chips(opts: List<String>, sel: Int, onSel: (Int) -> Unit) {
    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        opts.forEachIndexed { i, o -> Pill(o, selected = i == sel) { onSel(i) } }
    }
}

@Composable
private fun PillRows(items: List<Pair<String, () -> Unit>>, per: Int = 4) {
    for (r in items.chunked(per)) Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        for ((t, a) in r) Pill(t, Modifier.weight(1f), onClick = a)
        repeat(per - r.size) { Spacer(Modifier.weight(1f)) }
    }
}

@Composable
private fun ResultRow(text: String, fs: Float = 20f) {
    val row = remember(text) { Parse.row(text) ?: Row(listOf(Str(text))) }
    Box(Modifier.horizontalScroll(rememberScrollState())) { MathView(row, fs) }
}

private fun mxNames() = listOf('A', 'B', 'C', 'U', 'V')

// ---------------- Matrix ----------------
@Composable
fun MatrixScreen(vm: MainViewModel) {
    val s by vm.mx.collectAsState()
    val isVec = s.name in "UV"
    val rows = if (isVec) s.vdim else s.dims[s.name]!!.first
    val cols = if (isVec) 1 else s.dims[s.name]!!.second
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Chips(listOf("A", "B", "C", "u", "v"), mxNames().indexOf(s.name)) { vm.mxSelect(mxNames()[it]) }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            if (isVec) { Pill("2D", selected = s.vdim == 2) { if (s.vdim == 3) vm.mxResize(-1, 0) }; Pill("3D", selected = s.vdim == 3) { if (s.vdim == 2) vm.mxResize(1, 0) } }
            else {
                Pill("- row") { vm.mxResize(-1, 0) }; Pill("+ row") { vm.mxResize(1, 0) }
                Pill("- col") { vm.mxResize(0, -1) }; Pill("+ col") { vm.mxResize(0, 1) }
            }
            Spacer(Modifier.weight(1f))
            Caption("$rows x $cols", Modifier.align(Alignment.CenterVertically))
        }
        Box(Modifier.horizontalScroll(rememberScrollState())) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                for (i in 0 until rows) Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (j in 0 until cols) Fld(vm, "M:${s.name}:$i:$j", Modifier.width(68.dp), fs = 16f, h = 38.dp)
                }
            }
        }
        if (isVec) PillRows(listOf("dot" to { vm.mxOp("dot") }, "cross" to { vm.mxOp("cross") }, "norm" to { vm.mxOp("norm") }, "Save" to { vm.mxSave() }))
        else {
            PillRows(listOf("det" to { vm.mxOp("det") }, "inv" to { vm.mxOp("inv") }, "Aᵀ" to { vm.mxOp("T") }, "rref" to { vm.mxOp("rref") },
                "eig" to { vm.mxOp("eig") }, "eigvec" to { vm.mxOp("eigvec") }, "LU" to { vm.mxOp("lu") }, "QR" to { vm.mxOp("qr") },
                "A+B" to { vm.mxOp("A+B") }, "A×B" to { vm.mxOp("A*B") }, "Ax=u" to { vm.mxOp("Ax=b") }, "Save" to { vm.mxSave() }))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Pill("Aⁿ, n =") { vm.mxOp("A^n") }
                Fld(vm, "M:pow:0:0", Modifier.width(80.dp), fs = 16f, h = 38.dp)
            }
        }
        s.err?.let { BasicText(it, style = label(14, Brand.error)) }
        s.out?.let { o ->
            Caption("Result")
            ResultRow(o)
            s.dec?.let { ResultRow(it, 16f) }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) { Pill("To Home") { vm.sendExpr(o, false) } }
        }
        Spacer(Modifier.height(8.dp))
    }
}

// ---------------- Stats ----------------
@Composable
fun StatsScreen(vm: MainViewModel) {
    val s by vm.st.collectAsState()
    var tab by remember { mutableIntStateOf(0) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Chips(listOf("1 variable", "2 variables", "Distributions"), tab) { tab = it; vm.stSet { st -> st.copy(two = it == 1, out = emptyList(), err = null) } }
        if (tab < 2) {
            Box(Modifier.fillMaxWidth().height(230.dp).background(Brand.surface, RoundedCornerShape(12.dp)).padding(6.dp)) {
                LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(s.rows, key = { it }) { i ->
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                            BasicText("${i + 1}", Modifier.width(30.dp), style = label(12, Brand.textDim))
                            Fld(vm, "S:x:$i", Modifier.weight(1f), fs = 16f, h = 38.dp)
                            if (tab == 1) Fld(vm, "S:y:$i", Modifier.weight(1f), fs = 16f, h = 38.dp)
                        }
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Pill("+10 rows") { vm.stSet { it.copy(rows = (it.rows + 10).coerceAtMost(200)) } }
                Pill("Clear") { vm.stClear(); vm.stSet { it.copy(out = emptyList(), err = null) } }
                Pill("Calculate", bg = Brand.accent) { vm.stCompute() }
            }
            if (tab == 1) Chips(listOf("Linear", "Quadratic", "Cubic", "Log", "Exp", "Power"), s.reg) { vm.stSet { st -> st.copy(reg = it) } }
            s.err?.let { BasicText(it, style = label(14, Brand.error)) }
            for ((k, v) in s.out) Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                BasicText(k, Modifier.width(110.dp), style = label(14, Brand.textDim))
                ResultRow(v, 18f)
            }
        } else {
            val d = Dists.all[s.dist]
            Chips(Dists.all.map { it.name }, s.dist) { vm.stSet { st -> st.copy(dist = it, dout = null) } }
            Chips(listOf("pdf", "cdf", "inverse"), s.mode) { vm.stSet { st -> st.copy(mode = it, dout = null) } }
            for ((i, p) in d.params.withIndex()) Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                BasicText(p, Modifier.width(70.dp), style = label(14, Brand.textDim)); Fld(vm, "D:p$i", Modifier.weight(1f), fs = 16f, h = 40.dp)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                BasicText(if (s.mode == 2) "p" else "x", Modifier.width(70.dp), style = label(14, Brand.textDim)); Fld(vm, "D:x", Modifier.weight(1f), fs = 16f, h = 40.dp)
            }
            Pill("Calculate", bg = Brand.accent) { vm.distCompute() }
            s.dout?.let { Caption("Result"); ResultRow(it, 22f) }
        }
        Spacer(Modifier.height(8.dp))
    }
}

// ---------------- Table ----------------
@Composable
fun TableScreen(vm: MainViewModel) {
    LaunchedEffect(Unit) {
        if (vm.field("T:s").value.row.nodes.isEmpty()) { vm.setField("T:s", Row(listOf(Num("0")))); vm.setField("T:e", Row(listOf(Num("10")))); vm.setField("T:d", Row(listOf(Num("1")))) }
    }
    val f by vm.field("T:f").collectAsState()
    val g by vm.field("T:g").collectAsState()
    val rs by vm.field("T:s").collectAsState()
    val re by vm.field("T:e").collectAsState()
    val rd by vm.field("T:d").collectAsState()
    val ang by vm.settings.collectAsState()
    val exF = remember(f.row) { vm.tableEx("T:f") }
    val exG = remember(g.row) { vm.tableEx("T:g") }
    val fnF = remember(exF, ang.angle) { exF?.let { NumEval.compile(it, "x", ang.angle) } }
    val fnG = remember(exG, ang.angle) { exG?.let { NumEval.compile(it, "x", ang.angle) } }
    fun const(r: Row): Double? = NumEval.parse(r)?.let { NumEval.compile(it, "x")?.f(0.0) }
    val s0 = remember(rs.row) { const(rs.row) }
    val e0 = remember(re.row) { const(re.row) }
    val d0 = remember(rd.row) { const(rd.row) }
    val count = if (s0 == null || e0 == null || d0 == null || d0 == 0.0 || (e0 - s0) / d0 < 0) 0 else minOf(5000, floor((e0 - s0) / d0 + 1e-9).toInt() + 1)
    fun cell(ex: Ex?, fn: DFn?, x: Double): String {
        if (ex == null || fn == null) return "-"
        val y = fn.f(x)
        if (!y.isNaN() && !y.isInfinite()) return vm.fmt(y)
        val z = NumEval.evalC(ex, Complex(x))
        if (z.re.isNaN() || z.im.isNaN()) return "undefined"
        return vm.fmt(z.re) + (if (z.im >= 0) "+" else "-") + vm.fmt(abs(z.im)) + "i"
    }
    Column(Modifier.fillMaxSize().padding(horizontal = 10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BasicText("f(x)", Modifier.width(44.dp), style = label(14, Brand.series[0], true)); Fld(vm, "T:f", Modifier.weight(1f), fs = 17f, h = 40.dp)
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BasicText("g(x)", Modifier.width(44.dp), style = label(14, Brand.series[1], true)); Fld(vm, "T:g", Modifier.weight(1f), fs = 17f, h = 40.dp)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
            Caption("start"); Fld(vm, "T:s", Modifier.weight(1f), fs = 16f, h = 38.dp)
            Caption("end"); Fld(vm, "T:e", Modifier.weight(1f), fs = 16f, h = 38.dp)
            Caption("step"); Fld(vm, "T:d", Modifier.weight(1f), fs = 16f, h = 38.dp)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) { Pill("To Graph") { vm.tableToGraph() } }
        Row(Modifier.fillMaxWidth().background(Brand.surface, RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)).padding(8.dp)) {
            BasicText("x", Modifier.weight(1f), style = label(13, Brand.textDim, true)); BasicText("f(x)", Modifier.weight(1f), style = label(13, Brand.series[0], true)); BasicText("g(x)", Modifier.weight(1f), style = label(13, Brand.series[1], true))
        }
        if (count == 0) BasicText("Enter a valid start, end and step.", style = label(13, Brand.error))
        LazyColumn(Modifier.weight(1f).fillMaxWidth()) {
            items(count) { i ->
                val x = s0!! + i * d0!!
                Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 5.dp)) {
                    BasicText(vm.fmt(x), Modifier.weight(1f), style = label(14, Brand.text))
                    BasicText(cell(exF, fnF, x), Modifier.weight(1f), style = label(14, Brand.text))
                    BasicText(cell(exG, fnG, x), Modifier.weight(1f), style = label(14, Brand.text))
                }
            }
        }
    }
}

// ---------------- Graph ----------------
private fun nice(raw: Float): Float {
    val e = floor(log10(raw)).toInt()
    val b = 10f.pow(e)
    val m = raw / b
    return b * (if (m <= 1f) 1f else if (m <= 2f) 2f else if (m <= 5f) 5f else 10f)
}

@Composable
fun GraphScreen(vm: MainViewModel) {
    val gv = vm.gv
    val st by vm.settings.collectAsState()
    val rows = (0..3).map { vm.field("G:$it").collectAsState().value }
    val px by vm.field("G:px").collectAsState()
    val py by vm.field("G:py").collectAsState()
    val t0 by vm.field("G:t0").collectAsState()
    val t1 by vm.field("G:t1").collectAsState()
    LaunchedEffect(Unit) {
        if (vm.field("G:t0").value.row.nodes.isEmpty()) { vm.setField("G:t0", Row(listOf(Num("0")))); vm.setField("G:t1", Row(listOf(Num("6.2832")))) }
    }
    val fns = rows.mapIndexed { i, r -> remember(r.row, st.angle) { vm.graphFn(i) } }
    val param = remember(px.row, py.row, st.angle) { vm.graphParam() }
    val tr0 = remember(t0.row) { NumEval.parse(t0.row)?.let { NumEval.compile(it, "x")?.f(0.0) } ?: 0.0 }
    val tr1 = remember(t1.row) { NumEval.parse(t1.row)?.let { NumEval.compile(it, "x")?.f(0.0) } ?: 6.2832 }
    val paths = remember { List(5) { Path() } }
    val tm = rememberTextMeasurer(cacheSize = 64)
    val sel = gv.sel
    Column(Modifier.fillMaxSize().padding(horizontal = 10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Chips(listOf("f1", "f2", "f3", "f4", "param"), sel) { gv.sel = it; vm.focusOn(if (it < 4) "G:$it" else "G:px") }
        if (sel < 4) Fld(vm, "G:$sel", Modifier.fillMaxWidth(), fs = 17f, h = 40.dp)
        else {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                Caption("x(t)"); Fld(vm, "G:px", Modifier.weight(1f), fs = 16f, h = 38.dp); Caption("y(t)"); Fld(vm, "G:py", Modifier.weight(1f), fs = 16f, h = 38.dp)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                Caption("t from"); Fld(vm, "G:t0", Modifier.weight(1f), fs = 16f, h = 38.dp); Caption("to"); Fld(vm, "G:t1", Modifier.weight(1f), fs = 16f, h = 38.dp)
            }
        }
        Canvas(
            Modifier.fillMaxWidth().weight(1f).background(Brand.surface, RoundedCornerShape(12.dp))
                .pointerInput(Unit) {
                    detectTransformGestures { c, pan, zoom, _ ->
                        val w = size.width.toFloat(); val h = size.height.toFloat()
                        val wx = gv.cx + (c.x - w / 2) / gv.sx
                        val wy = gv.cy - (c.y - h / 2) / gv.sy
                        val nsx = (gv.sx * zoom).coerceIn(1f, 20000f); val nsy = (gv.sy * zoom).coerceIn(1f, 20000f)
                        gv.sx = nsx; gv.sy = nsy
                        gv.cx = wx - (c.x + pan.x - w / 2) / nsx
                        gv.cy = wy + (c.y + pan.y - h / 2) / nsy
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures { o -> gv.trace = gv.cx + (o.x - size.width / 2f) / gv.sx }
                }
        ) {
            val w = size.width; val h = size.height
            val cx = gv.cx; val cy = gv.cy; val sx = gv.sx; val sy = gv.sy
            fun X(v: Float) = w / 2 + (v - cx) * sx
            fun Y(v: Float) = h / 2 - (v - cy) * sy
            val gx = nice(70f / sx); val gy = nice(70f / sy)
            var k = ceil((cx - w / 2 / sx) / gx) * gx
            while (k <= cx + w / 2 / sx) {
                val xp = X(k); drawLine(Brand.line, Offset(xp, 0f), Offset(xp, h), 1f)
                if (abs(k) > gx / 2) drawText(tm.measure(fmtTick(k), TextStyle(color = Brand.textDim, fontSize = 9.sp, fontFamily = Brand.sans)), topLeft = Offset(xp + 2f, (Y(0f)).coerceIn(2f, h - 14f)))
                k += gx
            }
            k = ceil((cy - h / 2 / sy) / gy) * gy
            while (k <= cy + h / 2 / sy) {
                val yp = Y(k); drawLine(Brand.line, Offset(0f, yp), Offset(w, yp), 1f)
                if (abs(k) > gy / 2) drawText(tm.measure(fmtTick(k), TextStyle(color = Brand.textDim, fontSize = 9.sp, fontFamily = Brand.sans)), topLeft = Offset((X(0f) + 3f).coerceIn(2f, w - 30f), yp - 12f))
                k += gy
            }
            drawLine(Brand.textDim, Offset(X(0f), 0f), Offset(X(0f), h), 1.5f)
            drawLine(Brand.textDim, Offset(0f, Y(0f)), Offset(w, Y(0f)), 1.5f)
            for (i in 0..3) {
                val f = fns[i] ?: continue
                val p = paths[i]; p.rewind()
                var pen = false; var prev = 0f
                var xp = 0f
                while (xp <= w) {
                    val y = f.f((cx + (xp - w / 2) / sx).toDouble())
                    if (y.isNaN() || y.isInfinite()) pen = false
                    else {
                        val yp = Y(y.toFloat())
                        if (!pen || abs(yp - prev) > h * 3) p.moveTo(xp, yp) else p.lineTo(xp, yp)
                        pen = true; prev = yp
                    }
                    xp += 2f
                }
                drawPath(p, Brand.series[i], style = Stroke(if (i == sel) 4f else 2.5f))
            }
            param?.let { (fx, fy) ->
                val p = paths[4]; p.rewind()
                val n = 500
                var pen = false
                for (j in 0..n) {
                    val t = tr0 + (tr1 - tr0) * j / n
                    val a = fx.f(t); val b = fy.f(t)
                    if (a.isNaN() || b.isNaN() || a.isInfinite() || b.isInfinite()) { pen = false; continue }
                    if (!pen) p.moveTo(X(a.toFloat()), Y(b.toFloat())) else p.lineTo(X(a.toFloat()), Y(b.toFloat()))
                    pen = true
                }
                drawPath(p, Brand.series[4], style = Stroke(3f))
            }
            if (!gv.anchor.isNaN()) drawLine(Brand.violet, Offset(X(gv.anchor), 0f), Offset(X(gv.anchor), h), 1.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f)))
            if (!gv.trace.isNaN()) {
                drawLine(Brand.textDim, Offset(X(gv.trace), 0f), Offset(X(gv.trace), h), 1.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f)))
                val f = if (sel < 4) fns[sel] else null
                val y = f?.f(gv.trace.toDouble())
                if (y != null && !y.isNaN() && !y.isInfinite()) drawCircle(Brand.text, 7f, Offset(X(gv.trace), Y(y.toFloat())))
            }
        }
        val tv = gv.trace
        val readout = if (tv.isNaN()) "" else {
            val y = (if (sel < 4) fns[sel] else null)?.f(tv.toDouble())
            "x = ${vm.fmt(tv.toDouble())}" + (if (y != null) ",  y = ${vm.fmt(y)}" else "")
        }
        BasicText(if (gv.info.isNotEmpty()) gv.info else readout.ifEmpty { "Tap the graph to trace. Pinch to zoom, drag to pan." }, style = label(13, if (gv.info.isNotEmpty()) Brand.text else Brand.textDim), maxLines = 2)
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Pill("Zero") { vm.graphAnalyze(0) }; Pill("Min") { vm.graphAnalyze(1) }; Pill("Max") { vm.graphAnalyze(2) }
            Pill("Intersect") { vm.graphAnalyze(3) }; Pill("Mark A") { gv.anchor = gv.trace }; Pill("Integral") { vm.graphAnalyze(4) }
            Pill("Table") { vm.graphToTable() }
            Pill("Reset") { gv.cx = 0f; gv.cy = 0f; gv.sx = 40f; gv.sy = 40f; gv.trace = Float.NaN; gv.anchor = Float.NaN; gv.info = "" }
        }
        Spacer(Modifier.height(2.dp))
    }
}

private fun fmtTick(v: Float): String {
    val r = if (abs(v) >= 1e5f || (abs(v) < 1e-3f && v != 0f)) String.format("%.1e", v) else String.format("%.4f", v).trimEnd('0').trimEnd('.')
    return r
}

// ---------------- Programs ----------------
@Composable
fun ProgramsScreen(vm: MainViewModel) {
    val s by vm.pg.collectAsState()
    Column(Modifier.fillMaxSize().padding(horizontal = 10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Chips(s.list.map { it.name } + "+ New", if (s.cur < s.list.size) s.cur else s.list.size) { if (it >= s.list.size) vm.pgNew() else vm.pgSelect(it) }
        Box(Modifier.fillMaxWidth().height(40.dp).background(Brand.surface, RoundedCornerShape(10.dp)).border(1.dp, Brand.line, RoundedCornerShape(10.dp)).padding(horizontal = 12.dp), contentAlignment = Alignment.CenterStart) {
            BasicTextField(s.name, { vm.pgEdit(it, s.src) }, textStyle = label(15, Brand.text, true), singleLine = true, cursorBrush = SolidColor(Brand.accentHi), keyboardOptions = KeyboardOptions(autoCorrectEnabled = false))
        }
        Box(Modifier.fillMaxWidth().weight(1f).background(Brand.surface, RoundedCornerShape(10.dp)).border(1.dp, Brand.line, RoundedCornerShape(10.dp)).padding(12.dp)) {
            BasicTextField(
                s.src, { vm.pgEdit(s.name, it) }, Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                textStyle = TextStyle(color = Brand.text, fontFamily = Brand.mono, fontSize = 14.sp), cursorBrush = SolidColor(Brand.accentHi),
                keyboardOptions = KeyboardOptions(autoCorrectEnabled = false)
            )
            if (s.src.isEmpty()) BasicText("Write Giac code here, or pick a template.", style = label(14, Brand.textDim))
        }
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Pill("Run", bg = Brand.accent) { vm.pgRun() }; Pill("Save") { vm.pgSave() }; Pill("Delete") { vm.pgDelete() }
            for ((i, t) in Templates.programs.withIndex()) Pill(t.first) { vm.pgTemplate(i) }
        }
        if (s.out.isNotEmpty()) Box(Modifier.fillMaxWidth().background(Brand.surface, RoundedCornerShape(10.dp)).padding(10.dp)) {
            BasicText(s.out, style = TextStyle(color = if (s.ok) Brand.text else Brand.error, fontFamily = Brand.mono, fontSize = 14.sp))
        }
        Spacer(Modifier.height(4.dp))
    }
}

// ---------------- Units and constants ----------------
@Composable
fun UnitsScreen(vm: MainViewModel) {
    var tab by remember { mutableIntStateOf(0) }
    var cat by remember { mutableIntStateOf(0) }
    var from by remember { mutableIntStateOf(0) }
    var to by remember { mutableIntStateOf(1) }
    var value by remember { mutableStateOf("1") }
    Column(Modifier.fillMaxSize().padding(horizontal = 10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Chips(listOf("Units", "Constants"), tab) { tab = it }
        if (tab == 0) {
            val c = Units.cats[cat]
            Chips(Units.cats.map { it.name }, cat) { cat = it; from = 0; to = 1 }
            Box(Modifier.fillMaxWidth().height(44.dp).background(Brand.surface, RoundedCornerShape(10.dp)).border(1.5.dp, Brand.accent, RoundedCornerShape(10.dp)).padding(horizontal = 12.dp), contentAlignment = Alignment.CenterStart) {
                BasicTextField(value, { value = it.filter { ch -> ch.isDigit() || ch in ".-" } }, textStyle = label(18, Brand.text), singleLine = true, cursorBrush = SolidColor(Brand.accentHi),
                    keyboardOptions = KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal))
            }
            Caption("From")
            Chips(c.units.map { it.name }, from) { from = it }
            Caption("To")
            Chips(c.units.map { it.name }, to) { to = it }
            Pill("Convert in Home", bg = Brand.accent) {
                val v = value.ifBlank { "0" }
                vm.sendExpr(Units.convert(v, c, c.units[from], c.units[to]), true)
            }
        } else {
            LazyColumn(Modifier.fillMaxSize()) {
                items(Consts.all, key = { it.sym }) { k ->
                    Row(Modifier.fillMaxWidth().pointerInput(k.sym) { detectTapGestures { vm.insertConst(k) } }.padding(vertical = 9.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        BasicText(k.sym, Modifier.width(56.dp), style = label(16, Brand.accentHi, true))
                        Column(Modifier.weight(1f)) {
                            BasicText(k.name, style = label(15, Brand.text))
                            BasicText(k.value + " " + k.unit, style = label(12, Brand.textDim))
                        }
                    }
                }
            }
        }
    }
}

// ---------------- Guide ----------------
@Composable
fun GuideScreen(vm: MainViewModel) {
    var q by remember { mutableStateOf("") }
    var open by remember { mutableIntStateOf(-1) }
    val list = remember(q) { Guide.search(q) }
    Column(Modifier.fillMaxSize().padding(horizontal = 10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(Modifier.fillMaxWidth().height(46.dp).background(Brand.surface, RoundedCornerShape(12.dp)).border(1.dp, Brand.line, RoundedCornerShape(12.dp)).padding(horizontal = 14.dp), contentAlignment = Alignment.CenterStart) {
            if (q.isEmpty()) BasicText("Search the guide", style = label(16, Brand.textDim))
            BasicTextField(q, { q = it; open = -1 }, Modifier.fillMaxWidth(), textStyle = label(16, Brand.text), singleLine = true, cursorBrush = SolidColor(Brand.accentHi))
        }
        LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            itemsIndexed(list, key = { _, e -> e.title }) { i, e ->
                val isOpen = open == i
                Column(Modifier.fillMaxWidth().background(Brand.surface, RoundedCornerShape(12.dp)).pointerInput(i) { detectTapGestures { open = if (isOpen) -1 else i } }.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    BasicText(e.title, style = label(16, Brand.text, true))
                    BasicText(e.goal, style = label(13, Brand.textDim))
                    if (isOpen) {
                        e.steps.forEachIndexed { k, st -> BasicText("${k + 1}. $st", style = label(14, Brand.text)) }
                        Caption("Example")
                        ResultRow(e.example, 18f)
                        Pill("Insert example", bg = Brand.accent) { vm.sendExpr(e.example, false) }
                    }
                }
            }
        }
    }
}

// ---------------- Settings ----------------
@Composable
private fun Toggle(text: String, on: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().height(44.dp).pointerInput(on) { detectTapGestures { onChange(!on) } }, verticalAlignment = Alignment.CenterVertically) {
        BasicText(text, Modifier.weight(1f), style = label(16, Brand.text))
        Box(Modifier.width(46.dp).height(26.dp).background(if (on) Brand.accent else Brand.keyOp, RoundedCornerShape(13.dp)).padding(3.dp), contentAlignment = if (on) Alignment.CenterEnd else Alignment.CenterStart) {
            Box(Modifier.size(20.dp).background(Brand.text, RoundedCornerShape(10.dp)))
        }
    }
}

@Composable
fun SettingsScreen(vm: MainViewModel) {
    val s by vm.settings.collectAsState()
    val sigs = listOf(0, 3, 4, 5, 6, 8, 10, 12)
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Caption("Angle unit")
        Chips(listOf("Degrees", "Radians", "Grads"), s.angle) { vm.setSettings(s.copy(angle = it)) }
        Caption("Number base")
        Chips(listOf("Dec", "Bin", "Oct", "Hex"), listOf(10, 2, 8, 16).indexOf(s.base)) { vm.setSettings(s.copy(base = listOf(10, 2, 8, 16)[it])) }
        Caption("Notation")
        Chips(listOf("Normal", "Engineering (ENG)"), if (s.eng) 1 else 0) { vm.setSettings(s.copy(eng = it == 1)) }
        Caption("Significant figures")
        Chips(sigs.map { if (it == 0) "Auto" else "$it" }, sigs.indexOf(s.sig).coerceAtLeast(0)) { vm.setSettings(s.copy(sig = sigs[it])) }
        Caption("Calculation timeout")
        Chips(listOf("10 s", "30 s", "60 s"), listOf(10, 30, 60).indexOf(s.timeout).coerceAtLeast(0)) { vm.setSettings(s.copy(timeout = listOf(10, 30, 60)[it])) }
        Toggle("Mixed fractions", s.mixed) { vm.setSettings(s.copy(mixed = it)) }
        Toggle("Haptic feedback", s.haptics) { vm.setSettings(s.copy(haptics = it)) }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Pill("Clear history") { vm.clearHistory() }
            Pill("Clear stored variables") { vm.clearDefs() }
        }
        Spacer(Modifier.height(10.dp))
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Image(painterResource(R.drawable.ic_mark), null, Modifier.size(width = 96.dp, height = 57.dp))
            BasicText("ANGELINA 1.0", style = label(15, Brand.text, true))
            Image(painterResource(R.drawable.splash_brand), null, Modifier.size(width = 180.dp, height = 42.dp))
        }
        Spacer(Modifier.height(16.dp))
    }
}
