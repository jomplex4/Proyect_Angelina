package com.digitalminds.angelina

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import kotlin.math.max

sealed class It
class TI(val x: Float, val y: Float, val r: TextLayoutResult, val c: Color) : It()
class LI(val x1: Float, val y1: Float, val x2: Float, val y2: Float, val c: Color) : It()
class SI(val kind: Int, val x: Float, val y: Float, val w: Float, val h: Float, val c: Color) : It()
class Caret(val path: List<Step>, val idx: Int, val x: Float, val top: Float, val bot: Float)
class Lay(val w: Float, val asc: Float, val desc: Float, val items: List<It>, val carets: List<Caret>)

private const val K_RAD = 1
private const val K_LPAR = 2
private const val K_RPAR = 3
private const val K_LBR = 4
private const val K_RBR = 5
private const val K_BAR = 6
private const val K_BOX = 9

private fun List<It>.shift(dx: Float, dy: Float): List<It> = map {
    when (it) {
        is TI -> TI(it.x + dx, it.y + dy, it.r, it.c)
        is LI -> LI(it.x1 + dx, it.y1 + dy, it.x2 + dx, it.y2 + dy, it.c)
        is SI -> SI(it.kind, it.x + dx, it.y + dy, it.w, it.h, it.c)
    }
}

private fun List<Caret>.shiftC(dx: Float, dy: Float): List<Caret> = map { Caret(it.path, it.idx, it.x + dx, it.top + dy, it.bot + dy) }

class Lo(private val tm: TextMeasurer, private val d: Density, private val col: Color = Brand.text) {
    private val cache = HashMap<String, TextLayoutResult>()

    private fun measure(s: String, fs: Float, bold: Boolean): TextLayoutResult {
        val key = "$s|$fs|$bold"
        return cache.getOrPut(key) {
            tm.measure(s, TextStyle(fontFamily = Brand.sans, fontSize = with(d) { fs.toSp() }, color = col, fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal))
        }
    }

    fun text(s: String, fs: Float, c: Color = col, bold: Boolean = false): Lay {
        val r = measure(s, fs, bold)
        val b = r.firstBaseline
        return Lay(r.size.width.toFloat(), b, r.size.height - b, listOf(TI(0f, -b, r, c)), emptyList())
    }

    fun row(r: Row, fs: Float, path: List<Step> = emptyList(), ph: Boolean = false): Lay {
        val items = ArrayList<It>()
        val carets = ArrayList<Caret>()
        val xs = ArrayList<Float>()
        var x = 0f
        var asc = fs * 0.78f
        var desc = fs * 0.26f
        var lastAsc = asc
        if (r.nodes.isEmpty()) {
            val w = if (ph) fs * 0.6f else 2f
            if (ph) items.add(SI(K_BOX, 0f, -asc, w, asc + desc, Brand.accent))
            return Lay(w, asc, desc, items, listOf(Caret(path, 0, if (ph) w / 2 else 0f, -asc, desc)))
        }
        for ((i, n) in r.nodes.withIndex()) {
            xs.add(x)
            val l: Lay
            var dy = 0f
            if (n is Pow) {
                l = row(n.e, fs * 0.68f, path + Step(i, 0), true)
                dy = -max(0.4f * fs, lastAsc * 0.5f)
            } else l = node(n, fs, path, i)
            items.addAll(l.items.shift(x, dy))
            carets.addAll(l.carets.shiftC(x, dy))
            asc = max(asc, l.asc - dy)
            desc = max(desc, l.desc + dy)
            x += l.w + (if (n is Pow) 1f else 0f)
            lastAsc = l.asc
        }
        xs.add(x)
        for (i in xs.indices) carets.add(Caret(path, i, xs[i], -asc, desc))
        return Lay(x, asc, desc, items, carets)
    }

    private fun join(parts: List<Lay>, fs: Float): Lay {
        val items = ArrayList<It>(); val carets = ArrayList<Caret>()
        var x = 0f; var asc = 0f; var desc = 0f
        for ((k, p) in parts.withIndex()) {
            if (k > 0) { val c = text(",", fs, Brand.textDim); items.addAll(c.items.shift(x, 0f)); x += c.w + fs * 0.25f }
            items.addAll(p.items.shift(x, 0f)); carets.addAll(p.carets.shiftC(x, 0f))
            x += p.w; asc = max(asc, p.asc); desc = max(desc, p.desc)
        }
        return Lay(x, max(asc, fs * 0.78f), max(desc, fs * 0.26f), items, carets)
    }

    private fun wrap(inner: Lay, kl: Int, kr: Int, fs: Float): Lay {
        val pw = fs * 0.3f
        val h = inner.asc + inner.desc
        val items = ArrayList<It>()
        items.add(SI(kl, 0f, -inner.asc, pw, h, Brand.textDim))
        items.addAll(inner.items.shift(pw, 0f))
        items.add(SI(kr, pw + inner.w + 1f, -inner.asc, pw, h, Brand.textDim))
        return Lay(inner.w + 2 * pw + 1f, inner.asc, inner.desc, items, inner.carets.shiftC(pw, 0f))
    }

    private fun opText(c: String): Pair<String, Boolean> = when (c) {
        "*" -> "·" to true; "-" -> "−" to true; "<=" -> "≤" to true; ">=" -> "≥" to true; "!=" -> "≠" to true; "->" -> "→" to true
        "+", "×", "÷", "−", "=", "==", "<", ">", "≤", "≥", "≠", ":=", "/", "%", "and", "or" -> c to true
        "," -> "," to false
        else -> c to false
    }

    private fun node(n: Node, fs: Float, path: List<Step>, i: Int): Lay = when (n) {
        is Num -> text(n.t, fs)
        is Sym -> text(
            when {
                n.n.startsWith("_") -> " " + n.n.trimStart('_')
                else -> n.n
            }, fs, if (n.n.startsWith("_")) Brand.textDim else col
        )
        is Str -> text(n.s, fs, Brand.textDim)
        is Op -> {
            val (s, pad) = opText(n.c)
            val t = text(if (n.c == "and" || n.c == "or") " $s " else s, fs, Brand.textDim)
            val p = if (pad) fs * 0.22f else if (n.c == ",") 0f else 0f
            val trail = if (n.c == ",") fs * 0.28f else 0f
            Lay(t.w + 2 * p + trail, t.asc, t.desc, t.items.shift(p, 0f), emptyList())
        }
        is Frac -> {
            val f2 = fs * 0.88f
            val a = row(n.n, f2, path + Step(i, 0), true)
            val b = row(n.d, f2, path + Step(i, 1), true)
            val w = max(a.w, b.w) + fs * 0.3f
            val axis = fs * 0.3f
            val gap = fs * 0.12f
            val ay = -(axis + gap + a.desc)
            val by = -axis + gap + b.asc
            val it = ArrayList<It>()
            it.addAll(a.items.shift((w - a.w) / 2, ay)); it.addAll(b.items.shift((w - b.w) / 2, by))
            it.add(LI(fs * 0.08f, -axis, w - fs * 0.08f, -axis, Brand.text))
            Lay(w, -ay + a.asc, by + b.desc, it, a.carets.shiftC((w - a.w) / 2, ay) + b.carets.shiftC((w - b.w) / 2, by))
        }
        is Root -> {
            val rs = if (n.idx != null) 1 else 0
            val r = row(n.r, fs, path + Step(i, rs), true)
            val ix = n.idx?.let { row(it, fs * 0.55f, path + Step(i, 0), true) }
            val rw = fs * 0.55f
            val top = r.asc + fs * 0.18f
            val h = top + r.desc
            val x0 = max(0f, (ix?.w ?: 0f) - rw * 0.35f)
            val it = ArrayList<It>()
            it.add(SI(K_RAD, x0, -top, rw, h, Brand.text))
            it.add(LI(x0 + rw, -top, x0 + rw + r.w + fs * 0.12f, -top, Brand.text))
            it.addAll(r.items.shift(x0 + rw + fs * 0.05f, 0f))
            var car = r.carets.shiftC(x0 + rw + fs * 0.05f, 0f)
            if (ix != null) {
                val ixx = max(0f, x0 + rw * 0.45f - ix.w)
                val iy = -(h * 0.42f)
                it.addAll(ix.items.shift(ixx, iy)); car = car + ix.carets.shiftC(ixx, iy)
            }
            Lay(x0 + rw + r.w + fs * 0.17f, top + fs * 0.05f, r.desc, it, car)
        }
        is Paren -> wrap(row(n.r, fs, path + Step(i, 0), true), K_LPAR, K_RPAR, fs)
        is Lst -> wrap(join(n.items.mapIndexed { k, r -> row(r, fs, path + Step(i, k), true) }, fs), K_LBR, K_RBR, fs)
        is Mixed -> {
            val w = row(n.w, fs, path + Step(i, 0), true)
            val f = node(Frac(n.n, n.d), fs, path, i)
            val it = ArrayList<It>(); it.addAll(w.items); it.addAll(f.items.shift(w.w + fs * 0.1f, 0f))
            Lay(w.w + f.w + fs * 0.1f, max(w.asc, f.asc), max(w.desc, f.desc), it, w.carets)
        }
        is Mat -> {
            val f2 = fs * 0.9f
            val cells = n.cells.mapIndexed { k, r -> row(r, f2, path + Step(i, k), true) }
            val cw = FloatArray(n.cols); val ra = FloatArray(n.rows); val rd = FloatArray(n.rows)
            for (r in 0 until n.rows) for (c in 0 until n.cols) {
                val l = cells[r * n.cols + c]
                cw[c] = max(cw[c], l.w); ra[r] = max(ra[r], l.asc); rd[r] = max(rd[r], l.desc)
            }
            val gx = fs * 0.6f; val gy = fs * 0.35f
            var h = gy * (n.rows - 1); for (r in 0 until n.rows) h += ra[r] + rd[r]
            val wTot = cw.sum() + gx * (n.cols - 1)
            val it = ArrayList<It>(); val car = ArrayList<Caret>()
            var y = -fs * 0.3f - h / 2
            for (r in 0 until n.rows) {
                y += ra[r]
                var x = 0f
                for (c in 0 until n.cols) {
                    val l = cells[r * n.cols + c]
                    val dx = x + (cw[c] - l.w) / 2
                    it.addAll(l.items.shift(dx, y)); car.addAll(l.carets.shiftC(dx, y))
                    x += cw[c] + gx
                }
                y += rd[r] + gy
            }
            val inner = Lay(wTot, h / 2 + fs * 0.3f, h / 2 - fs * 0.3f, it, car)
            wrap(inner, K_LBR, K_RBR, fs)
        }
        is Func -> func(n, fs, path, i)
        is Pow -> text("^", fs)
    }

    private fun func(n: Func, fs: Float, path: List<Step>, i: Int): Lay {
        val args = n.args.mapIndexed { k, r -> row(r, fs, path + Step(i, k), true) }
        if (n.name == "abs" && args.size == 1) return wrap(args[0], K_BAR, K_BAR, fs)
        if ((n.name == "int" && (args.size == 2 || args.size == 4)) || ((n.name == "sum" || n.name == "product") && args.size == 4)) {
            val sym = when (n.name) { "int" -> "∫"; "sum" -> "Σ"; else -> "Π" }
            val s = text(sym, fs * 1.7f)
            val sdy = -fs * 0.3f - (s.desc - s.asc) / 2
            val lo = if (n.name == "int" && args.size == 4) args[2] else if (n.name != "int") args[2].let { l -> val v = args[1]; joinEq(v, l, fs) } else null
            val hi = if (n.name == "int" && args.size == 4) args[3] else if (n.name != "int") args[3] else null
            val sw = max(s.w, max(lo?.w ?: 0f, hi?.w ?: 0f))
            val it = ArrayList<It>(); var car: List<Caret> = emptyList()
            val sx = if (n.name == "int") 0f else (sw - s.w) / 2
            it.addAll(s.items.shift(sx, sdy))
            var asc = s.asc - sdy; var desc = s.desc + sdy
            if (hi != null) {
                val hy = sdy - s.asc - hi.desc - fs * 0.02f
                val hx = if (n.name == "int") s.w * 0.75f else (sw - hi.w) / 2
                it.addAll(hi.items.shift(hx, hy)); car = car + hi.carets.shiftC(hx, hy); asc = max(asc, -hy + hi.asc)
            }
            if (lo != null) {
                val ly = sdy + s.desc + lo.asc + fs * 0.02f
                val lx = if (n.name == "int") s.w * 0.1f else (sw - lo.w) / 2
                it.addAll(lo.items.shift(lx, ly)); car = car + lo.carets.shiftC(lx, ly); desc = max(desc, ly + lo.desc)
            }
            var x = (if (n.name == "int") s.w + (hi?.w ?: 0f) * 0.4f else sw) + fs * 0.2f
            val body = args[0]
            it.addAll(body.items.shift(x, 0f)); car = car + body.carets.shiftC(x, 0f)
            x += body.w
            asc = max(asc, body.asc); desc = max(desc, body.desc)
            if (n.name == "int") {
                val d = text(" d", fs, Brand.textDim)
                it.addAll(d.items.shift(x, 0f)); x += d.w
                val v = args[1]
                it.addAll(v.items.shift(x, 0f)); car = car + v.carets.shiftC(x, 0f); x += v.w
            }
            return Lay(x, asc, desc, it, car)
        }
        val name = text(Names.show(n.name), fs, Brand.textDim, true)
        val inner = if (args.isEmpty()) Lay(0f, fs * 0.78f, fs * 0.26f, emptyList(), emptyList()) else join(args, fs)
        val w = wrap(inner, K_LPAR, K_RPAR, fs)
        val it = ArrayList<It>(); it.addAll(name.items); it.addAll(w.items.shift(name.w, 0f))
        return Lay(name.w + w.w, max(name.asc, w.asc), max(name.desc, w.desc), it, w.carets.shiftC(name.w, 0f))
    }

    private fun joinEq(v: Lay, a: Lay, fs: Float): Lay {
        val eq = text("=", fs * 0.8f, Brand.textDim)
        val it = ArrayList<It>(); var x = 0f
        it.addAll(v.items); x += v.w
        it.addAll(eq.items.shift(x, 0f)); x += eq.w
        it.addAll(a.items.shift(x, 0f))
        return Lay(x + a.w, max(v.asc, a.asc), max(v.desc, a.desc), it, v.carets + a.carets.shiftC(v.w + eq.w, 0f))
    }
}

fun DrawScope.drawLay(l: Lay, ox: Float, oy: Float, stroke: Float) {
    for (it in l.items) when (it) {
        is TI -> drawText(it.r, color = it.c, topLeft = Offset(ox + it.x, oy + it.y))
        is LI -> drawLine(it.c, Offset(ox + it.x1, oy + it.y1), Offset(ox + it.x2, oy + it.y2), strokeWidth = stroke)
        is SI -> shape(it, ox, oy, stroke)
    }
}

private fun DrawScope.shape(s: SI, ox: Float, oy: Float, sw: Float) {
    val x = ox + s.x; val y = oy + s.y
    val p = Path()
    when (s.kind) {
        K_RAD -> {
            p.moveTo(x, y + s.h * 0.58f); p.lineTo(x + s.w * 0.22f, y + s.h * 0.5f); p.lineTo(x + s.w * 0.5f, y + s.h); p.lineTo(x + s.w, y)
            drawPath(p, s.c, style = Stroke(sw))
        }
        K_LPAR -> { p.moveTo(x + s.w * 0.85f, y); p.quadraticTo(x - s.w * 0.1f, y + s.h / 2, x + s.w * 0.85f, y + s.h); drawPath(p, s.c, style = Stroke(sw)) }
        K_RPAR -> { p.moveTo(x + s.w * 0.15f, y); p.quadraticTo(x + s.w * 1.1f, y + s.h / 2, x + s.w * 0.15f, y + s.h); drawPath(p, s.c, style = Stroke(sw)) }
        K_LBR -> { p.moveTo(x + s.w * 0.8f, y); p.lineTo(x + s.w * 0.2f, y); p.lineTo(x + s.w * 0.2f, y + s.h); p.lineTo(x + s.w * 0.8f, y + s.h); drawPath(p, s.c, style = Stroke(sw)) }
        K_RBR -> { p.moveTo(x + s.w * 0.2f, y); p.lineTo(x + s.w * 0.8f, y); p.lineTo(x + s.w * 0.8f, y + s.h); p.lineTo(x + s.w * 0.2f, y + s.h); drawPath(p, s.c, style = Stroke(sw)) }
        K_BAR -> drawLine(s.c, Offset(x + s.w / 2, y), Offset(x + s.w / 2, y + s.h), strokeWidth = sw)
        K_BOX -> drawRect(s.c, Offset(x, y), Size(s.w, s.h), style = Stroke(sw * 0.8f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f))))
    }
}

@Composable
fun MathView(row: Row, fs: Float, modifier: Modifier = Modifier, color: Color = Brand.text) {
    val tm = rememberTextMeasurer()
    val d = LocalDensity.current
    val px = with(d) { fs.sp.toPx() }
    val lay = remember(row, px, color) { Lo(tm, d, color).row(row, px) }
    val w = with(d) { (lay.w + 2f).toDp() }
    val h = with(d) { (lay.asc + lay.desc + 2f).toDp() }
    val st = px / 14f
    Canvas(modifier.size(w, h)) { drawLay(lay, 1f, lay.asc + 1f, st) }
}

@Composable
fun EditorView(
    ed: Ed, fs: Float, active: Boolean, modifier: Modifier = Modifier,
    onPlace: (Cur) -> Unit, onSwipe: (Int) -> Unit
) {
    val tm = rememberTextMeasurer()
    val d = LocalDensity.current
    val px = with(d) { fs.sp.toPx() }
    val lay = remember(ed.row, px) { Lo(tm, d).row(ed.row, px) }
    val caret = remember(lay, ed.cur) { lay.carets.firstOrNull { it.path == ed.cur.path && it.idx == ed.cur.idx } }
    var vw by remember { mutableFloatStateOf(0f) }
    var vh by remember { mutableFloatStateOf(0f) }
    val layS = rememberUpdatedState(lay)
    val scroll = run {
        val cx = caret?.x ?: 0f
        val maxS = max(0f, lay.w + px - vw)
        if (vw <= 0f) 0f else (cx - vw * 0.75f).coerceIn(0f, maxS)
    }
    val scrollS = rememberUpdatedState(scroll)
    val vhS = rememberUpdatedState(vh)
    val blink = rememberInfiniteTransition(label = "caret").animateFloat(
        0f, 1f, infiniteRepeatable(tween(1000, easing = LinearEasing), RepeatMode.Restart), label = "a"
    )
    Canvas(
        modifier
            .onSizeChanged { vw = it.width.toFloat(); vh = it.height.toFloat() }
            .pointerInput(Unit) {
                detectTapGestures { o ->
                    val l = layS.value
                    val oy = (vhS.value - (l.asc + l.desc)) / 2 + l.asc
                    val tx = o.x + scrollS.value
                    val ty = o.y - oy
                    val best = l.carets.minByOrNull { c ->
                        val dx = c.x - tx
                        val dy = if (ty < c.top) c.top - ty else if (ty > c.bot) ty - c.bot else 0f
                        dx * dx + dy * dy * 4f
                    }
                    if (best != null) onPlace(Cur(best.path, best.idx))
                }
            }
            .pointerInput(Unit) {
                var acc = 0f
                detectHorizontalDragGestures(onDragStart = { acc = 0f }) { _, dx ->
                    acc += dx
                    val step = px * 0.9f
                    while (abs(acc) >= step) { onSwipe(if (acc > 0) 1 else -1); acc -= if (acc > 0) step else -step }
                }
            }
    ) {
        val oy = (size.height - (lay.asc + lay.desc)) / 2 + lay.asc
        drawLay(lay, -scroll, oy, px / 14f)
        if (active && caret != null && blink.value < 0.6f) {
            drawLine(Brand.accentHi, Offset(caret.x - scroll, oy + caret.top), Offset(caret.x - scroll, oy + caret.bot), strokeWidth = px / 9f)
        }
    }
}
