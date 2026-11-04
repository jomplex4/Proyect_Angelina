package com.digitalminds.angelina

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private fun figColor(i: Int): Color = when (i) {
    1 -> Brand.accentHi; 2 -> Brand.blue; 3 -> Brand.violet; 4 -> Brand.orange; 5 -> Brand.textDim
    6 -> Brand.surface; 7 -> Color(0xFF3A3C44); else -> Brand.text
}

/** Dibuja una figura del formulario (caja de 100 × 60 escalada al ancho disponible). */
@Composable
fun FigureView(id: String, modifier: Modifier = Modifier) {
    val prims = remember(id) { Figs.get(id) } ?: return
    val tm = rememberTextMeasurer(cacheSize = 64)
    BoxWithConstraints(modifier.fillMaxWidth()) {
        val h = maxWidth * ((BecaFigs.height[id] ?: 60f) / 100f)
        Canvas(Modifier.fillMaxWidth().height(h).clipToBounds()) {
            val s = size.width / 100f
            val sw = maxOf(2f, s * 0.38f)
            val dash = PathEffect.dashPathEffect(floatArrayOf(s * 1.6f, s * 1.2f))
            for (p in prims) when (p) {
                is Ln -> drawLine(figColor(p.c), Offset(p.x1 * s, p.y1 * s), Offset(p.x2 * s, p.y2 * s), strokeWidth = sw * p.w, pathEffect = if (p.dash) dash else null)
                is Pg -> {
                    val path = Path()
                    for (k in p.p.indices step 2) if (k == 0) path.moveTo(p.p[0] * s, p.p[1] * s) else path.lineTo(p.p[k] * s, p.p[k + 1] * s)
                    path.close()
                    if (p.fill) drawPath(path, figColor(p.c).copy(alpha = if (p.c >= 6) 1f else 0.85f))
                    else drawPath(path, figColor(p.c), style = Stroke(sw, pathEffect = if (p.dash) dash else null))
                }
                is Ci -> drawCircle(figColor(p.c), p.r * s, Offset(p.x * s, p.y * s), style = Stroke(sw, pathEffect = if (p.dash) dash else null))
                is El -> {
                    val tl = Offset((p.x - p.rx) * s, (p.y - p.ry) * s); val sz = Size(2 * p.rx * s, 2 * p.ry * s)
                    if (p.back) {
                        drawArc(figColor(5), 180f, 180f, false, tl, sz, style = Stroke(sw, pathEffect = dash))
                        drawArc(figColor(p.c), 0f, 180f, false, tl, sz, style = Stroke(sw))
                    } else drawOval(figColor(p.c), tl, sz, style = Stroke(sw))
                }
                is Ar -> {
                    val tl = Offset((p.x - p.r) * s, (p.y - p.r) * s); val sz = Size(2 * p.r * s, 2 * p.r * s)
                    if (p.wedge) drawArc(figColor(p.c).copy(alpha = if (p.c >= 6) 1f else 0.85f), p.a0, p.sw, true, tl, sz, style = Fill)
                    else drawArc(figColor(p.c), p.a0, p.sw, false, tl, sz, style = Stroke(sw))
                }
                is Tx -> {
                    val r = tm.measure(p.s, TextStyle(color = figColor(p.c), fontSize = (12f * p.size).sp, fontFamily = Brand.math))
                    drawText(r, topLeft = Offset(p.x * s - r.size.width / 2f, p.y * s - r.size.height / 2f))
                }
                is Dt -> drawCircle(figColor(p.c), maxOf(3f, s * 0.9f), Offset(p.x * s, p.y * s))
            }
        }
    }
}

@Composable
private fun FormulaLine(f: Formula, fs: Float) {
    if (f.isText) BasicText(f.shown, style = TextStyle(color = Brand.text, fontFamily = Brand.math, fontSize = fs.sp))
    else {
        val row = remember(f.formula) { Parse.row(f.formula) ?: Row(listOf(Str(f.formula))) }
        Box(Modifier.horizontalScroll(rememberScrollState())) { MathView(row, fs) }
    }
}

/** Cuadro de las 6 razones de todos los notables; tocar una fila muestra su triángulo y los valores reales. */
@Composable
private fun NotablesPanel() {
    var sel by remember { mutableStateOf("37") }
    Column(Modifier.fillMaxWidth().background(Brand.surface, RoundedCornerShape(12.dp)).padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        BasicText("Cuadro de razones trigonométricas", style = label(16, Brand.text, true))
        BasicText("★ exacto   ≈ aproximado (se usa en exámenes)   ·   Toca un ángulo para ver su triángulo", style = label(11, Brand.textDim))
        Box(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
            Column {
                Row(Modifier.background(Brand.keyOp)) {
                    Box(Modifier.width(64.dp).padding(6.dp)) { BasicText("Ángulo", style = label(12, Brand.violet, true)) }
                    for (n in Notables.names) Box(Modifier.width(84.dp).padding(6.dp), contentAlignment = Alignment.Center) { BasicText(n, style = label(13, Brand.violet, true)) }
                }
                for (r in Notables.rows) {
                    val on = r.angle == sel
                    Row(
                        Modifier.background(if (on) Brand.keyOp else Brand.surface).pointerInput(r.angle) { detectTapGestures { sel = r.angle } },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(Modifier.width(64.dp).padding(6.dp)) {
                            BasicText(r.angle.replace('.', ',') + "° " + if (r.exact) "★" else "≈", style = label(13, if (r.exact) Brand.blue else Brand.orange, true))
                        }
                        for (x in r.r) Box(Modifier.width(84.dp).height(44.dp).padding(2.dp), contentAlignment = Alignment.Center) {
                            val row = remember(x) { Parse.row(x) ?: Row(listOf(Str(x))) }
                            MathView(row, 14f)
                        }
                    }
                }
            }
        }
        val r = Notables.rows.first { it.angle == sel }
        val t = Notables.tris.first { it.small == r.angle || it.big == r.angle }
        Box(Modifier.fillMaxWidth().background(Brand.bg, RoundedCornerShape(10.dp)).padding(6.dp)) { FigureView(r.fig) }
        BasicText("Triángulo de ${t.small.replace('.', ',')}° y ${t.big.replace('.', ',')}°: lados ${t.sides}. " +
            (if (t.exact) "Es exacto." else "Es aproximado: sirve para exámenes, pero no es el valor real."), style = label(13, Brand.text))
        BasicText("El ángulo menor mira al cateto menor. CO CA CO CA HIP HIP / HIP HIP CA CO CA CO.", style = label(12, Brand.textDim))
        if (!r.exact) {
            Caption("Valor real de ${r.angle.replace('.', ',')}° (con calculadora)")
            for (i in 0..5) BasicText("${Notables.names[i]} ${r.angle.replace('.', ',')}° = " + "%.4f".format(r.real(i)).replace('.', ','), style = label(13, Brand.textDim))
        }
    }
}

/** Formulario offline: secciones, buscador, figuras dibujadas y un ejemplo comprobado por fórmula. */
@Composable
fun FormulasScreen(vm: MainViewModel) {
    var q by remember { mutableStateOf(vm.formulaQuery.value) }
    LaunchedEffect(Unit) { vm.formulaQuery.value = "" }
    var sec by remember { mutableIntStateOf(0) }
    var open by remember { mutableStateOf<String?>(null) }
    val st by vm.settings.collectAsState()
    val list = remember(q, sec) { Formulas.search(q, Formulas.secs[sec].id) }
    Column(Modifier.fillMaxSize().padding(horizontal = 10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(Modifier.fillMaxWidth().height(46.dp).background(Brand.surface, RoundedCornerShape(12.dp)).border(1.dp, Brand.line, RoundedCornerShape(12.dp)).padding(horizontal = 14.dp), contentAlignment = Alignment.CenterStart) {
            if (q.isEmpty()) BasicText("Busca: Pitágoras, cono, mediana, binomial...", style = label(16, Brand.textDim))
            BasicTextField(q, { q = it; open = null }, Modifier.fillMaxWidth(), textStyle = label(16, Brand.text), singleLine = true, cursorBrush = SolidColor(Brand.accentHi))
        }
        if (q.isBlank()) Chips(Formulas.secs.map { it.name }, sec) { sec = it; open = null }
        else BasicText("${list.size} resultados en todas las secciones", style = label(12, Brand.textDim))
        LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            if (q.isBlank() && Formulas.secs[sec].id == "notables") item(key = "cuadro") { NotablesPanel() }
            items(list, key = { it.sec + "/" + it.title }) { f ->
                val isOpen = open == f.title
                Column(
                    Modifier.fillMaxWidth().background(Brand.surface, RoundedCornerShape(12.dp))
                        .pointerInput(f.title, isOpen) { detectTapGestures { open = if (isOpen) null else f.title } }.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BasicText(f.title, style = label(15, Brand.textDim, true))
                    FormulaLine(f, if (f.isText) 17f else 20f)
                    // Leyenda: qué significa cada letra, siempre visible.
                    Column(Modifier.fillMaxWidth().background(Brand.bg, RoundedCornerShape(8.dp)).padding(8.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        for (l in f.legend) BasicText("• " + l, style = label(13, Brand.text))
                    }
                    if (isOpen) {
                        if (f.fig.isNotEmpty()) Box(Modifier.fillMaxWidth().background(Brand.bg, RoundedCornerShape(10.dp)).padding(6.dp)) { FigureView(f.fig) }
                        f.ex?.let { e ->
                            Caption("Ejemplo")
                            BasicText(f.exText, style = label(14, Brand.text))
                            val inRow = remember(e.input) { Parse.row(e.input) ?: Row(listOf(Str(e.input))) }
                            val outRow = remember(e.out) { Parse.row(e.out) ?: Row(listOf(Str(e.out))) }
                            Row(Modifier.fillMaxWidth().background(Brand.keyNum, RoundedCornerShape(10.dp)).padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Column(Modifier.weight(1f)) {
                                    Box(Modifier.horizontalScroll(rememberScrollState())) { MathView(inRow, 16f, color = Brand.textDim) }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        BasicText("= ", style = label(17, Brand.textDim))
                                        Box(Modifier.horizontalScroll(rememberScrollState())) { MathView(outRow, 18f) }
                                    }
                                    if (e.deg) BasicText(if (st.angle == 0) "En modo GRADOS" else "Necesita modo GRADOS (Probar lo activa)", style = label(11, Brand.violet))
                                }
                                Pill("Probar", bg = Brand.accent) { vm.tryExample(e) }
                            }
                        }
                        if (f.note.isNotEmpty()) BasicText("💡 " + f.note, style = label(13, Brand.textDim))
                        BasicText("Fuente: " + f.src, style = label(11, Brand.textDim.copy(alpha = 0.8f)))
                        Solvers.forFormula(f.title)?.let { sv -> Pill("Resolver paso a paso: " + sv.title, bg = Brand.keyOp, fg = Brand.violet) { vm.openSolver(sv.id) } }
                    }
                }
            }
        }
    }
}
