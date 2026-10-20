package com.digitalminds.angelina

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Figuras del formulario como datos puros en una caja de 100 × 60 (y hacia abajo).
 * Colores: 0 texto, 1 rojo, 2 azul, 3 violeta, 4 naranja, 5 gris.
 */
sealed interface Pr
data class Ln(val x1: Float, val y1: Float, val x2: Float, val y2: Float, val c: Int = 0, val dash: Boolean = false, val w: Float = 1f) : Pr
data class Pg(val p: List<Float>, val c: Int = 0, val fill: Boolean = false, val dash: Boolean = false) : Pr
data class Ci(val x: Float, val y: Float, val r: Float, val c: Int = 0, val dash: Boolean = false) : Pr
/** Elipse; back = la mitad de atrás punteada (bases de cilindros y conos). */
data class El(val x: Float, val y: Float, val rx: Float, val ry: Float, val c: Int = 0, val back: Boolean = false) : Pr
/** Arco: ángulos en grados de pantalla (0 = derecha, positivo = horario). wedge = sector relleno. */
data class Ar(val x: Float, val y: Float, val r: Float, val a0: Float, val sw: Float, val c: Int = 2, val wedge: Boolean = false) : Pr
data class Tx(val x: Float, val y: Float, val s: String, val c: Int = 0, val size: Float = 1f) : Pr
data class Dt(val x: Float, val y: Float, val c: Int = 1) : Pr

private class B {
    val l = ArrayList<Pr>()
    fun ln(x1: Float, y1: Float, x2: Float, y2: Float, c: Int = 0, dash: Boolean = false, w: Float = 1f) { l.add(Ln(x1, y1, x2, y2, c, dash, w)) }
    fun poly(vararg p: Float, c: Int = 0, fill: Boolean = false, dash: Boolean = false) { l.add(Pg(p.toList(), c, fill, dash)) }
    fun ci(x: Float, y: Float, r: Float, c: Int = 0, dash: Boolean = false) { l.add(Ci(x, y, r, c, dash)) }
    fun el(x: Float, y: Float, rx: Float, ry: Float, c: Int = 0, back: Boolean = false) { l.add(El(x, y, rx, ry, c, back)) }
    fun t(x: Float, y: Float, s: String, c: Int = 0, size: Float = 1f) { l.add(Tx(x, y, s, c, size)) }
    fun dot(x: Float, y: Float, c: Int = 1) { l.add(Dt(x, y, c)) }
    fun ang(vx: Float, vy: Float, px: Float, py: Float) = (atan2((py - vy).toDouble(), (px - vx).toDouble()) * 180 / PI).toFloat()

    /** Arco del ángulo en V entre las direcciones a P y a Q. */
    fun arc(vx: Float, vy: Float, px: Float, py: Float, qx: Float, qy: Float, r: Float = 7f, c: Int = 2) {
        val a0 = ang(vx, vy, px, py)
        var sw = ang(vx, vy, qx, qy) - a0
        while (sw > 180) sw -= 360
        while (sw < -180) sw += 360
        l.add(Ar(vx, vy, r, a0, sw, c))
    }

    /** Marca de ángulo recto en V. */
    fun ra(vx: Float, vy: Float, px: Float, py: Float, qx: Float, qy: Float, s: Float = 4f) {
        val d1 = hypot(px - vx, py - vy); val d2 = hypot(qx - vx, qy - vy)
        val ux = (px - vx) / d1 * s; val uy = (py - vy) / d1 * s
        val wx = (qx - vx) / d2 * s; val wy = (qy - vy) / d2 * s
        ln(vx + ux, vy + uy, vx + ux + wx, vy + uy + wy, 5); ln(vx + ux + wx, vy + uy + wy, vx + wx, vy + wy, 5)
    }

    fun tri(ax: Float, ay: Float, bx: Float, by: Float, cx: Float, cy: Float, c: Int = 0) = poly(ax, ay, bx, by, cx, cy, c = c)

    /** Ejes cartesianos con origen (ox, oy). */
    fun axes(ox: Float, oy: Float, x0: Float = 6f, x1: Float = 94f, y0: Float = 4f, y1: Float = 57f) {
        for (k in -8..8) { val gx = ox + k * 6; if (gx in x0..x1) ln(gx, y0, gx, y1, 5, dash = true, w = 0.4f) }
        for (k in -8..8) { val gy = oy + k * 6; if (gy in y0..y1) ln(x0, gy, x1, gy, 5, dash = true, w = 0.4f) }
        ln(x0, oy, x1, oy, 0, w = 1.2f); ln(ox, y0, ox, y1, 0, w = 1.2f)
        t(x1 - 1, oy + 4, "x", 5); t(ox + 3, y0 + 1, "y", 5)
    }

    /** Triángulo notable recto en B: A abajo izquierda, C arriba de B. */
    fun notable(ax: Float, bx: Float, cy: Float, adj: String, opp: String, hyp: String, angA: String, angC: String) {
        val y = 52f
        tri(ax, y, bx, y, bx, cy)
        ra(bx, y, ax, y, bx, cy)
        arc(ax, y, bx, y, bx, cy, 9f, 1); arc(bx, cy, ax, y, bx, y, 6f, 4)
        t((ax + bx) / 2, y + 5, adj, 2); t(bx + 7, (y + cy) / 2, opp, 2); t((ax + bx) / 2 - 4, (y + cy) / 2 - 4, hyp, 3)
        t(ax + 15, y - 3, angA, 1, 0.9f); t(bx - 5, cy + 11, angC, 4, 0.9f)
    }
}

private fun fig(f: B.() -> Unit): List<Pr> = B().apply(f).l

private fun pt(cx: Float, cy: Float, r: Float, a: Double) = Pair(cx + r * cos(a * PI / 180).toFloat(), cy + r * sin(a * PI / 180).toFloat())

object Figs {
    private val defs: Map<String, () -> List<Pr>> = mapOf(
        "pct" to { fig {
            poly(10f, 22f, 90f, 22f, 90f, 38f, 10f, 38f)
            poly(10f, 22f, 26f, 22f, 26f, 38f, 10f, 38f, c = 1, fill = true)
            t(18f, 15f, "a%", 1); t(50f, 46f, "100% = N", 5); t(58f, 30f, "resto", 5)
        } },
        "tri" to { fig {
            tri(15f, 52f, 85f, 52f, 40f, 10f)
            ln(40f, 10f, 40f, 52f, 1, dash = true); ra(40f, 52f, 85f, 52f, 40f, 10f)
            t(50f, 57f, "b", 2); t(44f, 32f, "h", 1)
        } },
        "tri3" to { fig {
            tri(12f, 52f, 88f, 52f, 55f, 8f)
            t(8f, 55f, "A"); t(92f, 55f, "B"); t(55f, 4f, "C")
            t(76f, 28f, "a", 2); t(29f, 28f, "b", 2); t(50f, 57f, "c", 2)
            arc(12f, 52f, 88f, 52f, 55f, 8f, 8f, 1); arc(88f, 52f, 12f, 52f, 55f, 8f, 7f, 4); arc(55f, 8f, 12f, 52f, 88f, 52f, 6f, 3)
        } },
        "rect" to { fig {
            tri(15f, 52f, 75f, 52f, 75f, 10f); ra(75f, 52f, 15f, 52f, 75f, 10f)
            t(81f, 31f, "a", 2); t(45f, 57f, "b", 2); t(40f, 27f, "c", 1)
        } },
        "recth" to { fig {
            val c = pt(50f, 52f, 40f, -120.0)
            tri(10f, 52f, 90f, 52f, c.first, c.second)
            ln(c.first, c.second, c.first, 52f, 1, dash = true); ra(c.first, 52f, 90f, 52f, c.first, c.second)
            ra(c.first, c.second, 10f, 52f, 90f, 52f)
            t(20f, 57f, "m", 4); t(60f, 57f, "n", 4); t(c.first + 3f, 36f, "h", 1)
            t(14f, 33f, "a", 2); t(63f, 28f, "b", 2); t(50f, 57f, "", 0)
        } },
        "cuadrado" to { fig {
            poly(28f, 8f, 72f, 8f, 72f, 52f, 28f, 52f); ln(28f, 52f, 72f, 8f, 1, dash = true)
            t(50f, 57f, "l", 2); t(77f, 30f, "l", 2); t(46f, 27f, "d", 1)
        } },
        "rectangulo" to { fig {
            poly(15f, 12f, 85f, 12f, 85f, 50f, 15f, 50f); t(50f, 56f, "b", 2); t(90f, 31f, "h", 2)
            ln(15f, 50f, 85f, 12f, 5, dash = true)
        } },
        "rombo" to { fig {
            poly(15f, 30f, 50f, 8f, 85f, 30f, 50f, 52f)
            ln(15f, 30f, 85f, 30f, 1, dash = true); ln(50f, 8f, 50f, 52f, 2, dash = true)
            t(68f, 27f, "DM", 1); t(54f, 44f, "dm", 2)
        } },
        "paralelo" to { fig {
            poly(15f, 50f, 70f, 50f, 85f, 12f, 30f, 12f)
            ln(30f, 12f, 30f, 50f, 1, dash = true); ra(30f, 50f, 70f, 50f, 30f, 12f)
            t(45f, 56f, "b", 2); t(34f, 31f, "h", 1)
        } },
        "trapecio" to { fig {
            poly(10f, 50f, 90f, 50f, 70f, 12f, 30f, 12f)
            ln(30f, 12f, 30f, 50f, 1, dash = true); ln(20f, 31f, 80f, 31f, 3, dash = true)
            t(50f, 56f, "B", 2); t(50f, 7f, "b", 2); t(26f, 40f, "h", 1); t(86f, 31f, "m", 3)
        } },
        "circulo" to { fig {
            ci(50f, 30f, 24f); ln(50f, 30f, 74f, 30f, 1); dot(50f, 30f, 0)
            t(62f, 26f, "r", 1); t(46f, 34f, "O", 5)
        } },
        "sector" to { fig {
            ci(50f, 32f, 24f, 5, dash = true)
            l.add(Ar(50f, 32f, 24f, -60f, 60f, 1, wedge = true))
            ln(50f, 32f, 74f, 32f); val p = pt(50f, 32f, 24f, -60.0); ln(50f, 32f, p.first, p.second)
            arc(50f, 32f, 74f, 32f, p.first, p.second, 7f, 0); t(62f, 28f, "θ", 0, 0.9f); t(64f, 36f, "r", 2); t(80f, 18f, "L", 1)
        } },
        "hexagono" to { fig {
            val p = (0 until 6).map { pt(50f, 30f, 26f, it * 60.0) }
            poly(*p.flatMap { listOf(it.first, it.second) }.toFloatArray())
            ln(p[0].first, p[0].second, p[2].first, p[2].second, 5, dash = true); ln(p[0].first, p[0].second, p[3].first, p[3].second, 5, dash = true)
            ln(p[0].first, p[0].second, p[4].first, p[4].second, 5, dash = true)
            t((p[1].first + p[2].first) / 2, 57f, "l", 2)
        } },
        "equilatero" to { fig {
            tri(22f, 52f, 78f, 52f, 50f, 3.5f); ln(50f, 3.5f, 50f, 52f, 1, dash = true); ra(50f, 52f, 78f, 52f, 50f, 3.5f)
            t(50f, 57f, "l", 2); t(31f, 26f, "l", 2); t(69f, 26f, "l", 2); t(54f, 32f, "h", 1)
        } },
        "inscrito" to { fig {
            ci(50f, 30f, 25f)
            val v = pt(50f, 30f, 25f, 90.0); val p = pt(50f, 30f, 25f, 200.0); val q = pt(50f, 30f, 25f, 340.0)
            ln(v.first, v.second, p.first, p.second); ln(v.first, v.second, q.first, q.second)
            l.add(Ar(50f, 30f, 25f, 200f, 140f, 1)); arc(v.first, v.second, p.first, p.second, q.first, q.second, 7f, 2)
            t(50f, 46f, "x", 2); t(50f, 2.5f, "arco", 1)
        } },
        "cuerdas" to { fig {
            ci(50f, 30f, 25f)
            val a = pt(50f, 30f, 25f, 200.0); val b = pt(50f, 30f, 25f, 20.0); val c = pt(50f, 30f, 25f, 140.0); val d = pt(50f, 30f, 25f, 290.0)
            ln(a.first, a.second, b.first, b.second, 2); ln(c.first, c.second, d.first, d.second, 1)
            t((a.first + 48f) / 2 - 2, (a.second + 30f) / 2 - 3, "a", 2); t((b.first + 52f) / 2 + 2, (b.second + 30f) / 2 + 4, "b", 2)
            t((c.first + 50f) / 2 - 4, (c.second + 30f) / 2, "c", 1); t((d.first + 50f) / 2 + 4, (d.second + 30f) / 2, "d", 1)
        } },
        "tangente" to { fig {
            val cx = 66f; val cy = 30f; val r = 20f; val px = 8f; val py = 30f
            ci(cx, cy, r)
            val dd = hypot(cx - px, cy - py); val tl = sqrt(dd * dd - r * r)
            val base = atan2((cy - py).toDouble(), (cx - px).toDouble()); val off = kotlin.math.asin((r / dd).toDouble())
            val tx = px + tl * cos(base - off).toFloat(); val ty = py + tl * sin(base - off).toFloat()
            ln(px, py, tx, ty, 1); ln(px, py, cx + r, cy, 2); dot(px, py, 0)
            t((px + tx) / 2 - 2, (py + ty) / 2 - 4, "t", 1); t((px + cx - r) / 2, cy + 5, "a", 2); t(cx, cy + 5, "b", 2); t(px - 3, py, "P", 5)
        } },
        "thales" to { fig {
            for (y in listOf(12f, 30f, 52f)) ln(8f, y, 92f, y, 5)
            ln(20f, 6f, 35f, 57f, 2); ln(55f, 6f, 88f, 57f, 1)
            t(22f, 21f, "a", 2); t(27f, 41f, "b", 2); t(65f, 19f, "c", 1); t(76f, 40f, "d", 1)
        } },
        "linea" to { fig {
            axes(15f, 52f)
            val x1 = 15f + 2 * 6; val y1 = 52f - 3 * 6; val x2 = 15f + 4 * 6; val y2 = 52f - 7 * 6
            ln(15f + 0.5f * 6, 52f - 0f, 15f + 5 * 6, 52f - 9 * 6, 1, w = 1.4f)
            ln(x1, y1, x2, y1, 3, dash = true); ln(x2, y1, x2, y2, 3, dash = true)
            dot(x1, y1, 2); dot(x2, y2, 2); t(x1 - 9, y1 + 1, "(x₁,y₁)", 2, 0.85f); t(x2 + 11, y2 + 1, "(x₂,y₂)", 2, 0.85f)
        } },
        "parabola" to { fig {
            axes(30f, 34f)
            val pts = ArrayList<Float>()
            var x = -0.6f
            while (x <= 4.6f) { pts.add(30f + x * 8); pts.add(34f - (x * x - 4 * x + 3) * 8); x += 0.2f }
            for (i in 0 until pts.size / 2 - 1) ln(pts[2 * i], pts[2 * i + 1], pts[2 * i + 2], pts[2 * i + 3], 1, w = 1.4f)
            dot(30f + 16, 34f + 8, 3); t(46f, 48f, "V(h, k)", 3, 0.85f)
        } },
        "mediana" to { fig {
            tri(10f, 52f, 55f, 6f, 90f, 52f); val gx = 55f + (50f - 55f) * 2 / 3; val gy = 6f + (52f - 6f) * 2 / 3
            ln(55f, 6f, 50f, 52f, 1); dot(gx, gy, 3); dot(50f, 52f, 2)
            t(6f, 55f, "A"); t(55f, 2f, "B"); t(94f, 55f, "C"); t(50f, 57f, "M", 2); t(gx + 4, gy, "G", 3); t(56f, 20f, "2k", 1, 0.85f); t(55f, 46f, "k", 1, 0.85f)
        } },
        "bisectriz" to { fig {
            val a = Pair(10f, 52f); val b = Pair(40f, 8f); val c = Pair(90f, 52f)
            val la = hypot(b.first - c.first, b.second - c.second); val lb = hypot(a.first - c.first, a.second - c.second); val lc = hypot(a.first - b.first, a.second - b.second)
            val ix = (la * a.first + lb * b.first + lc * c.first) / (la + lb + lc); val iy = (la * a.second + lb * b.second + lc * c.second) / (la + lb + lc)
            tri(a.first, a.second, b.first, b.second, c.first, c.second)
            ln(b.first, b.second, ix, iy, 3, dash = true); ln(a.first, a.second, ix, iy, 2); ln(c.first, c.second, ix, iy, 2); dot(ix, iy, 3)
            arc(ix, iy, a.first, a.second, c.first, c.second, 6f, 1); arc(b.first, b.second, a.first, a.second, c.first, c.second, 6f, 4)
            t(6f, 55f, "A"); t(40f, 3.5f, "B"); t(94f, 55f, "C"); t(ix + 1, iy - 7, "I", 3); t(ix, iy + 10, "x", 1)
        } },
        "mediatriz" to { fig {
            val pa = pt(50f, 31f, 26f, 160.0); val pb = pt(50f, 31f, 26f, 255.0); val pc = pt(50f, 31f, 26f, 20.0)
            val ax = pa.first; val ay = pa.second; val bx = pb.first; val by = pb.second; val cx = pc.first; val cy = pc.second
            val d = 2 * (ax * (by - cy) + bx * (cy - ay) + cx * (ay - by))
            val ox = ((ax * ax + ay * ay) * (by - cy) + (bx * bx + by * by) * (cy - ay) + (cx * cx + cy * cy) * (ay - by)) / d
            val oy = ((ax * ax + ay * ay) * (cx - bx) + (bx * bx + by * by) * (ax - cx) + (cx * cx + cy * cy) * (bx - ax)) / d
            val r = hypot(ax - ox, ay - oy)
            ci(ox, oy, r, 5, dash = true); tri(ax, ay, bx, by, cx, cy)
            ln(ox, oy, ax, ay, 2); ln(ox, oy, cx, cy, 2); dot(ox, oy, 3); arc(ox, oy, ax, ay, cx, cy, 5f, 1); arc(bx, by, ax, ay, cx, cy, 5f, 4)
            ln((ax + cx) / 2, (ay + cy) / 2, ox, oy, 3, dash = true)
            t(ax - 4, ay + 3, "A"); t(bx, by - 4, "B"); t(cx + 4, cy + 3, "C"); t(ox + 4, oy - 2, "O", 3); t(ox, oy + 9, "x", 1)
        } },
        "altura" to { fig {
            val ax = 10f; val ay = 52f; val bx = 40f; val by = 4f; val cx = 90f; val cy = 52f
            tri(ax, ay, bx, by, cx, cy)
            ln(bx, by, bx, ay, 1, dash = true)
            fun foot(px: Float, py: Float, qx: Float, qy: Float, rx: Float, ry: Float): Pair<Float, Float> {
                val dx = rx - qx; val dy = ry - qy; val u = ((px - qx) * dx + (py - qy) * dy) / (dx * dx + dy * dy)
                return Pair(qx + u * dx, qy + u * dy)
            }
            val fa = foot(ax, ay, bx, by, cx, cy); val fc = foot(cx, cy, ax, ay, bx, by)
            ln(ax, ay, fa.first, fa.second, 1, dash = true); ln(cx, cy, fc.first, fc.second, 1, dash = true)
            val hy = ay - (bx - ax) * (cx - bx) / (ay - by) // ortocentro sobre la altura desde B
            dot(bx, hy, 3); ln(ax, ay, bx, hy, 2); ln(cx, cy, bx, hy, 2); arc(bx, hy, ax, ay, cx, cy, 5f, 1)
            t(6f, 55f, "A"); t(36f, 3f, "B"); t(94f, 55f, "C"); t(bx + 5, hy - 4, "H", 3); t(bx, hy + 9, "x", 1)
        } },
        "excentro" to { fig {
            val a = Pair(38f, 16f); val b = Pair(50f, 2f); val c = Pair(62f, 16f)
            val la = hypot(b.first - c.first, b.second - c.second); val lb = hypot(a.first - c.first, a.second - c.second); val lc = hypot(a.first - b.first, a.second - b.second)
            val ex = (la * a.first - lb * b.first + lc * c.first) / (la - lb + lc); val ey = (la * a.second - lb * b.second + lc * c.second) / (la - lb + lc)
            tri(a.first, a.second, b.first, b.second, c.first, c.second)
            // Prolongaciones de BA y BC: el excentro está fuera, frente a B.
            ln(a.first, a.second, a.first - (b.first - a.first) * 1.6f, a.second - (b.second - a.second) * 1.6f, 5, dash = true)
            ln(c.first, c.second, c.first - (b.first - c.first) * 1.6f, c.second - (b.second - c.second) * 1.6f, 5, dash = true)
            ln(a.first, a.second, ex, ey, 2); ln(c.first, c.second, ex, ey, 2); ln(b.first, b.second, ex, ey, 3, dash = true); dot(ex, ey, 3)
            arc(ex, ey, a.first, a.second, c.first, c.second, 6f, 1)
            t(a.first - 4, a.second, "A"); t(b.first + 5, b.second + 2, "B"); t(c.first + 4, c.second, "C"); t(ex + 5, ey, "E", 3); t(ex, ey - 9, "x", 1)
        } },
        "incirculo" to { fig {
            tri(20f, 52f, 80f, 52f, 20f, 7f); ra(20f, 52f, 80f, 52f, 20f, 7f); ci(35f, 37f, 15f, 3); dot(35f, 37f, 3)
            ln(35f, 37f, 35f, 52f, 1); t(38f, 46f, "r", 1); t(50f, 57f, "a", 2); t(14f, 30f, "b", 2); t(55f, 27f, "c", 2)
        } },
        "circuncirculo" to { fig {
            tri(30f, 48f, 70f, 48f, 30f, 18f); ra(30f, 48f, 70f, 48f, 30f, 18f); ci(50f, 33f, 25f, 3); dot(50f, 33f, 3)
            ln(50f, 33f, 70f, 48f, 1); t(64f, 38f, "R", 1); t(46f, 30f, "O", 3)
        } },
        "pitot" to { fig {
            poly(14f, 48f, 41f, 12f, 59f, 12f, 86f, 48f); ci(50f, 30f, 18f, 3)
            t(10f, 52f, "A"); t(39f, 8f, "B"); t(61f, 8f, "C"); t(90f, 52f, "D")
        } },
        "cuadinscrito" to { fig {
            ci(50f, 30f, 26f); val d = 26f / sqrt(2f)
            poly(50f - d, 30f - d, 50f + d, 30f - d, 50f + d, 30f + d, 50f - d, 30f + d, c = 2)
            ln(50f - d, 30f + d, 50f + d, 30f - d, 5, dash = true); ln(50f, 30f, 76f, 30f, 1); dot(50f, 30f, 0); t(64f, 34f, "R", 1); t(50f, 30f + d + 5, "l", 2)
        } },
        "circinscrita" to { fig {
            poly(24f, 4f, 76f, 4f, 76f, 56f, 24f, 56f, c = 2); ci(50f, 30f, 26f); ln(50f, 30f, 50f, 56f, 1); dot(50f, 30f, 0); t(53f, 44f, "r", 1); t(82f, 30f, "l", 2)
        } },
        "hexinscrito" to { fig {
            ci(50f, 30f, 26f); val p = (0 until 6).map { pt(50f, 30f, 26f, it * 60.0) }
            poly(*p.flatMap { listOf(it.first, it.second) }.toFloatArray(), c = 2)
            ln(50f, 30f, p[0].first, p[0].second, 1); ln(50f, 30f, p[1].first, p[1].second, 1); t(63f, 27f, "R", 1); t(73f, 45f, "l", 2)
        } },
        "triinscrito" to { fig {
            ci(50f, 32f, 26f); val p = listOf(-90.0, 30.0, 150.0).map { pt(50f, 32f, 26f, it) }
            poly(*p.flatMap { listOf(it.first, it.second) }.toFloatArray(), c = 2)
            ln(50f, 32f, p[1].first, p[1].second, 1); dot(50f, 32f, 0); t(62f, 34f, "R", 1); t(50f, 49f, "l", 2)
        } },
        "cubo" to { fig {
            val x0 = 22f; val y0 = 22f; val s = 34f; val dx = 16f; val dy = -14f
            poly(x0, y0, x0 + s, y0, x0 + s, y0 + s, x0, y0 + s)
            ln(x0, y0, x0 + dx, y0 + dy); ln(x0 + s, y0, x0 + s + dx, y0 + dy); ln(x0 + s, y0 + s, x0 + s + dx, y0 + s + dy)
            ln(x0 + dx, y0 + dy, x0 + s + dx, y0 + dy); ln(x0 + s + dx, y0 + dy, x0 + s + dx, y0 + s + dy)
            ln(x0 + dx, y0 + dy, x0 + dx, y0 + s + dy, 5, dash = true); ln(x0 + dx, y0 + s + dy, x0 + s + dx, y0 + s + dy, 5, dash = true); ln(x0, y0 + s, x0 + dx, y0 + s + dy, 5, dash = true)
            ln(x0, y0 + s, x0 + s + dx, y0 + dy, 1, dash = true)
            t(x0 + s / 2, y0 + s + 4, "a", 2); t(x0 + s / 2 + 8, y0 + s / 2 - 6, "D", 1)
        } },
        "ortoedro" to { fig {
            val x0 = 12f; val y0 = 24f; val w = 50f; val h = 30f; val dx = 20f; val dy = -16f
            poly(x0, y0, x0 + w, y0, x0 + w, y0 + h, x0, y0 + h)
            ln(x0, y0, x0 + dx, y0 + dy); ln(x0 + w, y0, x0 + w + dx, y0 + dy); ln(x0 + w, y0 + h, x0 + w + dx, y0 + h + dy)
            ln(x0 + dx, y0 + dy, x0 + w + dx, y0 + dy); ln(x0 + w + dx, y0 + dy, x0 + w + dx, y0 + h + dy)
            ln(x0 + dx, y0 + dy, x0 + dx, y0 + h + dy, 5, dash = true); ln(x0 + dx, y0 + h + dy, x0 + w + dx, y0 + h + dy, 5, dash = true); ln(x0, y0 + h, x0 + dx, y0 + h + dy, 5, dash = true)
            ln(x0, y0 + h, x0 + w + dx, y0 + dy, 1, dash = true)
            t(x0 + w / 2, y0 + h + 4, "a", 2); t(x0 + w + dx / 2 + 4, y0 + h + dy / 2 + 2, "b", 2); t(x0 - 4, y0 + h / 2, "c", 2); t(x0 + w / 2 + 6, y0 + 10, "D", 1)
        } },
        "prisma" to { fig {
            val b = listOf(15f, 54f, 58f, 54f, 36f, 44f); val up = 36f
            ln(b[0], b[1], b[2], b[3]); ln(b[2], b[3], b[4], b[5], 5, dash = true); ln(b[4], b[5], b[0], b[1], 5, dash = true)
            poly(b[0], b[1] - up, b[2], b[3] - up, b[4], b[5] - up)
            ln(b[0], b[1], b[0], b[1] - up); ln(b[2], b[3], b[2], b[3] - up); ln(b[4], b[5], b[4], b[5] - up, 5, dash = true)
            t(64f, 36f, "h", 1); t(36f, 58f, "Ab", 2)
        } },
        "cilindro" to { fig {
            el(50f, 12f, 22f, 6f); el(50f, 50f, 22f, 6f, back = true); ln(28f, 12f, 28f, 50f); ln(72f, 12f, 72f, 50f)
            ln(50f, 50f, 72f, 50f, 1); dot(50f, 50f, 0); t(61f, 47f, "r", 1); t(77f, 31f, "h", 2)
        } },
        "piramide" to { fig {
            val ax = 50f; val ay = 6f
            ln(15f, 50f, 65f, 50f); ln(65f, 50f, 85f, 40f); ln(85f, 40f, 35f, 40f, 5, dash = true); ln(35f, 40f, 15f, 50f, 5, dash = true)
            ln(ax, ay, 15f, 50f); ln(ax, ay, 65f, 50f); ln(ax, ay, 85f, 40f); ln(ax, ay, 35f, 40f, 5, dash = true)
            ln(ax, ay, 50f, 45f, 1, dash = true); dot(50f, 45f, 0); t(53f, 27f, "h", 1); t(40f, 55f, "Ab", 2)
        } },
        "cono" to { fig {
            el(50f, 48f, 26f, 7f, back = true); ln(50f, 6f, 24f, 48f); ln(50f, 6f, 76f, 48f)
            ln(50f, 6f, 50f, 48f, 1, dash = true); ln(50f, 48f, 76f, 48f, 2); dot(50f, 48f, 0)
            t(47f, 28f, "h", 1); t(63f, 52f, "r", 2); t(67f, 25f, "g", 3)
        } },
        "esfera" to { fig {
            ci(50f, 30f, 25f); el(50f, 30f, 25f, 7f, 5, back = true); ln(50f, 30f, 75f, 30f, 1); dot(50f, 30f, 0); t(63f, 26f, "r", 1)
        } },
        "tronco" to { fig {
            el(50f, 14f, 15f, 4f); el(50f, 50f, 28f, 6f, back = true); ln(35f, 14f, 22f, 50f); ln(65f, 14f, 78f, 50f)
            ln(50f, 14f, 50f, 50f, 1, dash = true); ln(50f, 50f, 78f, 50f, 2); ln(50f, 14f, 65f, 14f, 2)
            t(64f, 54f, "R", 2); t(57f, 10f, "r", 2); t(46f, 32f, "h", 1)
        } },
        "razones" to { fig {
            tri(12f, 52f, 80f, 52f, 80f, 10f); ra(80f, 52f, 12f, 52f, 80f, 10f); arc(12f, 52f, 80f, 52f, 80f, 10f, 10f, 1)
            t(27f, 49f, "α", 1); t(88f, 31f, "CO", 2); t(46f, 57f, "CA", 2); t(40f, 27f, "H", 3)
        } },
        "n45" to { fig { notable(20f, 64f, 8f, "k", "k", "k√2", "45°", "45°") } },
        "n30" to { fig { notable(14f, 80f, 13.9f, "k√3", "k", "2k", "30°", "60°") } },
        "n37" to { fig { notable(18f, 78f, 7f, "4k", "3k", "5k", "37°", "53°") } },
        "n15" to { fig { notable(8f, 74f, 34.3f, "(√6+√2)k", "(√6−√2)k", "4k", "15°", "75°") } },
        "n16" to { fig { notable(12f, 84f, 31f, "24k", "7k", "25k", "16°", "74°") } },
        "n8" to { fig { notable(12f, 82f, 42f, "7k", "k", "5√2 k", "8°", "82°") } },
        "n14" to { fig { notable(14f, 82f, 35f, "4k", "k", "√17 k", "14°", "76°") } },
        "n18" to { fig { notable(14f, 80f, 30f, "3k", "k", "√10 k", "18,5°", "71,5°") } },
        "n26" to { fig { notable(18f, 78f, 22f, "2k", "k", "√5 k", "26,5°", "63,5°") } },
        "traslacion" to { fig {
            axes(26f, 30f)
            tri(26f + 6, 30f - 6, 26f + 18, 30f - 6, 26f + 12, 30f - 18, 2)
            tri(26f + 24, 30f + 18, 26f + 36, 30f + 18, 26f + 30, 30f + 6, 1)
            ln(26f + 12, 30f - 18, 26f + 30, 30f + 6, 4, dash = true); t(26f + 26, 30f - 9, "(a, b)", 4, 0.85f)
            t(26f + 6, 30f - 22, "P", 2); t(26f + 34, 30f + 3, "P'", 1)
        } },
        "reflexion" to { fig {
            axes(20f, 30f)
            ln(6f, 30f, 94f, 30f, 3, w = 1.6f)
            tri(20f + 12, 30f - 6, 20f + 30, 30f - 6, 20f + 18, 30f - 20, 2)
            tri(20f + 12, 30f + 6, 20f + 30, 30f + 6, 20f + 18, 30f + 20, 1)
            ln(20f + 18, 30f - 20, 20f + 18, 30f + 20, 5, dash = true); t(20f + 23, 30f - 23, "P", 2); t(20f + 23, 30f + 25, "P'", 1)
        } },
        "rotacion" to { fig {
            axes(40f, 36f)
            val px = 40f + 18; val py = 36f - 6; val qx = 40f + 6; val qy = 36f - 18
            ln(40f, 36f, px, py, 2); ln(40f, 36f, qx, qy, 1); dot(px, py, 2); dot(qx, qy, 1)
            arc(40f, 36f, px, py, qx, qy, 10f, 4); t(px + 4, py, "P", 2); t(qx - 4, qy - 2, "P'", 1); t(53f, 24f, "θ", 4, 0.9f)
        } },
        "homotecia" to { fig {
            axes(14f, 50f)
            tri(14f + 12, 50f - 6, 14f + 24, 50f - 6, 14f + 18, 50f - 18, 2)
            tri(14f + 24, 50f - 12, 14f + 48, 50f - 12, 14f + 36, 50f - 36, 1)
            ln(14f, 50f, 14f + 48, 50f - 12, 5, dash = true); ln(14f, 50f, 14f + 36, 50f - 36, 5, dash = true)
            t(14f + 58, 50f - 30, "k = 2", 4)
        } },
        "tabla" to { fig {
            val xs = listOf(4f, 28f, 44f, 60f, 76f, 96f); val ys = listOf(4f, 15f, 26f, 37f, 48f)
            for (x in xs) ln(x, ys.first(), x, ys.last(), 5); for (y in ys) ln(xs.first(), y, xs.last(), y, 5)
            val rows = listOf(listOf("Iᵢ", "xᵢ", "fᵢ", "Fᵢ", "hᵢ"), listOf("[10; 20⟩", "15", "5", "5", "0,25"), listOf("[20; 30⟩", "25", "8", "13", "0,40"), listOf("[30; 40⟩", "35", "7", "20", "0,35"))
            for ((r, row) in rows.withIndex()) for ((k, s) in row.withIndex()) t((xs[k] + xs[k + 1]) / 2, (ys[r] + ys[r + 1]) / 2, s, if (r == 0) 3 else 0, 0.85f)
            t(50f, 55f, "n = 20", 5, 0.85f)
        } },
        "circular" to { fig {
            l.add(Ar(50f, 30f, 24f, -90f, 90f, 1, wedge = true)); l.add(Ar(50f, 30f, 24f, 0f, 144f, 2, wedge = true)); l.add(Ar(50f, 30f, 24f, 144f, 126f, 3, wedge = true))
            ci(50f, 30f, 24f); t(60f, 21f, "25%", 0, 0.85f); t(55f, 42f, "40%", 0, 0.85f); t(38f, 26f, "35%", 0, 0.85f); t(84f, 12f, "θ = 90°", 1)
        } },
        "dados" to { fig {
            poly(18f, 14f, 46f, 14f, 46f, 42f, 18f, 42f); poly(54f, 14f, 82f, 14f, 82f, 42f, 54f, 42f)
            for ((x, y) in listOf(Pair(24f, 20f), Pair(32f, 28f), Pair(40f, 36f))) dot(x, y, 0)
            for ((x, y) in listOf(Pair(60f, 20f), Pair(76f, 20f), Pair(60f, 36f), Pair(76f, 36f))) dot(x, y, 0)
            t(50f, 52f, "3 + 4 = 7", 1)
        } }
    )

    /** Figuras redibujadas de los exámenes oficiales (no son copias de imágenes). */
    private val exDefs: Map<String, () -> List<Pr>> = mapOf(
        "ex23_balanza" to { fig {
            for ((ox, left, right) in listOf(Triple(25f, "2 cilindros", "2 cubos + 1 esfera"), Triple(75f, "3 cilindros + 1 cubo", "2 esferas"))) {
                ln(ox, 54f, ox, 22f); ln(ox - 8, 54f, ox + 8, 54f); ln(ox - 20, 22f, ox + 20, 22f, w = 1.4f)
                ln(ox - 20, 22f, ox - 20, 34f, 5); ln(ox + 20, 22f, ox + 20, 34f, 5)
                ln(ox - 24, 34f, ox - 16, 34f, 4, w = 1.6f); ln(ox + 16, 34f, ox + 24, 34f, 4, w = 1.6f)
                t(ox, 59f, left + " = " + right, 0, 0.55f)
            }
            t(50f, 8f, "Las dos balanzas están en equilibrio · cilindro = 1 kg", 0, 0.7f)
        } },
        "ex23_cuadtri" to { fig {
            tri(20f, 54f, 84f, 54f, 20f, 6f); ra(20f, 54f, 84f, 54f, 20f, 6f)
            val l = 64f * 48f / (64f + 48f)
            poly(20f, 54f, 20f + l, 54f, 20f + l, 54f - l, 20f, 54f - l, c = 2)
            t(10f, 30f, "12", 2); t(52f, 58f, "16", 2); t(58f, 26f, "20", 2); t(20f + l / 2, 54f - l / 2, "L", 1)
        } },
        "ex23_altura" to { fig {
            tri(10f, 52f, 90f, 52f, 30f, 12f); ln(30f, 12f, 30f, 52f, 1, dash = true); ra(30f, 52f, 90f, 52f, 30f, 12f)
            t(6f, 55f, "B"); t(94f, 55f, "C"); t(30f, 8f, "A"); t(30f, 57f, "D"); t(20f, 57f, "2", 2); t(60f, 57f, "6", 2)
        } },
        "ex23_cochera" to { fig {
            poly(20f, 4f, 80f, 4f, 80f, 56f, 20f, 56f); poly(20f, 4f, 80f, 4f, 80f, 56f, 72f, 56f, 72f, 12f, 28f, 12f, 28f, 56f, 20f, 56f, c = 5, fill = true)
            poly(28f, 12f, 72f, 12f, 72f, 56f, 28f, 56f, c = 2); t(50f, 32f, "Cochera", 2); t(50f, 52f, "Entrada / Salida", 5, 0.8f)
            t(24f, 59f, "2", 4, 0.8f); t(76f, 59f, "2", 4, 0.8f); t(50f, 8f, "2", 4, 0.8f); t(16f, 6f, "B"); t(84f, 6f, "C"); t(16f, 56f, "A"); t(84f, 56f, "D")
        } },
        "ex23_corona" to { fig {
            ci(50f, 30f, 26f); ci(50f, 30f, 14f, 2); dot(50f, 30f, 0)
            val y = 30f - 14f; val hx = sqrt(26f * 26f - 14f * 14f)
            ln(50f - hx, y, 50f + hx, y, 1); ln(50f, 30f, 50f, y, 5, dash = true); t(50f - hx - 3, y - 2, "A"); t(50f + hx + 3, y - 2, "B"); t(46f, 33f, "O", 5)
        } },
        "ex23_anpd" to { fig {
            val s = 12f
            fun X(x: Float) = 26f + x * s
            fun Y(y: Float) = 54f - y * s
            poly(X(0f), Y(0f), X(4f), Y(0f), X(4f), Y(4f), X(0f), Y(4f))
            ln(X(0f), Y(0f), X(4f), Y(2f), 5); ln(X(2f), Y(1f), X(4f), Y(4f), 5)
            poly(X(0f), Y(0f), X(2f), Y(1f), X(3f), Y(2.5f), X(0f), Y(4f), c = 1)
            t(X(0f) - 3, Y(0f) + 2, "A"); t(X(4f) + 3, Y(0f) + 2, "B"); t(X(4f) + 3, Y(4f) - 2, "C"); t(X(0f) - 3, Y(4f) - 2, "D")
            t(X(4f) + 4, Y(2f), "M", 2); t(X(2f), Y(1f) + 4, "N", 2); t(X(3f) + 3, Y(2.5f) - 3, "P", 2)
        } },
        "ex23_zigzag" to { fig {
            ln(6f, 8f, 94f, 8f); ln(6f, 52f, 94f, 52f); t(96f, 5f, "L₁", 5, 0.8f); t(96f, 55f, "L₂", 5, 0.8f)
            ln(38f, 8f, 22f, 52f, 2); ln(32f, 24.5f, 80f, 8f, 1); ln(32f, 24.5f, 82f, 52f, 1); ln(80f, 8f, 66f, 30f); ln(66f, 30f, 82f, 52f)
            dot(32f, 24.5f, 0); ra(66f, 30f, 80f, 8f, 82f, 52f)
            t(38f, 4f, "N"); t(80f, 4f, "C"); t(22f, 57f, "M"); t(82f, 57f, "A"); t(27f, 24f, "B"); t(61f, 30f, "Q"); t(40f, 20f, "40°", 1, 0.8f)
        } },
        "ex23_mediana" to { fig {
            tri(8f, 52f, 92f, 52f, 34f, 14f); ln(34f, 14f, 50f, 52f, 2); ln(34f, 14f, 22f - 4f, -2f + 6f, 5, dash = true)
            t(5f, 55f, "A"); t(95f, 55f, "C"); t(34f, 9f, "B"); t(50f, 57f, "M", 2)
            t(15f, 49f, "α", 1); t(46f, 47f, "45°", 4, 0.8f); t(40f, 22f, "α", 1); t(39f, 12f, "45°", 4, 0.8f); t(82f, 49f, "x", 3)
            t(28f, 40f, "Niños", 5, 0.75f); t(60f, 40f, "Adultos", 5, 0.75f)
        } },
        "ex24_temp" to { fig {
            ln(8f, 54f, 94f, 54f); ln(8f, 54f, 8f, 4f)
            for (k in 0..7) { val y = 54f - k * 7f; ln(8f, y, 94f, y, 5, dash = true, w = 0.4f); t(4f, y, "${k * 5}", 5, 0.6f) }
            for (d in listOf(5, 10, 15, 20, 25, 30)) t(8f + d * 2.8f, 58f, "$d", 5, 0.6f)
            val pts = listOf(1 to 10f, 2 to 17f, 3 to 20.5f, 5 to 21.7f, 7 to 21f, 10 to 18f, 11 to 18f, 13 to 19.5f, 15 to 22f, 18 to 25.5f, 20 to 29f, 22 to 31.3f, 23 to 31f, 25 to 26f, 27 to 14f, 28 to 6f)
            for (i in 0 until pts.size - 1) ln(8f + pts[i].first * 2.8f, 54f - pts[i].second * 1.4f, 8f + pts[i + 1].first * 2.8f, 54f - pts[i + 1].second * 1.4f, 1, w = 1.3f)
            ln(8f, 54f - 20 * 1.4f, 94f, 54f - 20 * 1.4f, 2, dash = true); t(80f, 54f - 20 * 1.4f - 3, "20 °C", 2, 0.75f); t(50f, 3f, "°C por día de febrero", 0, 0.8f)
        } },
        "ex24_montana" to { fig {
            poly(55f, 54f, 76f, 8f, 98f, 54f, c = 5); ln(76f, 8f, 76f, 54f, 1, dash = true)
            ln(10f, 50f, 76f, 8f, 2); ln(32f, 50f, 76f, 8f, 2); ln(10f, 50f, 76f, 50f, 5, dash = true)
            ln(10f, 50f, 10f, 56f); ln(32f, 50f, 32f, 56f); t(10f, 59f, "A"); t(32f, 59f, "B"); t(21f, 47f, "140", 0, 0.75f)
            t(18f, 46f, "64°", 4, 0.7f); t(39f, 46f, "76°", 4, 0.7f); t(82f, 30f, "h", 1); t(88f, 57f, "1,8 m", 5, 0.7f)
        } },
        "ex24_llanta" to { fig {
            ci(50f, 30f, 26f, 5); ci(50f, 30f, 24f); dot(50f, 30f, 0)
            val p = listOf(-90.0, -45.0, 0.0, 45.0).map { pt(50f, 30f, 24f, it) }
            for (i in 0 until 3) ln(p[i].first, p[i].second, p[i + 1].first, p[i + 1].second, 1, w = 1.6f)
            ln(50f, 30f, p[0].first, p[0].second, 5); ln(50f, 30f, p[1].first, p[1].second, 5)
            arc(50f, 30f, p[0].first, p[0].second, p[1].first, p[1].second, 7f, 4); arc(p[1].first, p[1].second, p[0].first, p[0].second, p[2].first, p[2].second, 5f, 2)
            t(56f, 20f, "θ", 4); t(p[1].first - 6, p[1].second + 2, "α", 2); t(46f, 34f, "O", 5)
        } },
        "ex24_gas" to { fig {
            val tot = listOf(83, 145, 172, 259, 328, 336, 699, 1033, 1144, 1180); val cam = listOf(19, 73, 103, 189, 254, 273, 640, 972, 1083, 1139)
            ln(6f, 52f, 96f, 52f)
            for (i in 0 until 10) {
                val x = 8f + i * 8.8f
                val hc = cam[i] / 1200f * 42f; val ht = tot[i] / 1200f * 42f
                poly(x, 52f, x + 3.5f, 52f, x + 3.5f, 52f - hc, x, 52f - hc, c = 2, fill = true)
                poly(x + 3.7f, 52f, x + 7.2f, 52f, x + 7.2f, 52f - ht, x + 3.7f, 52f - ht, c = 5, fill = true)
                t(x + 3.6f, 56f, "${(2004 + i) % 100}", 5, 0.55f)
            }
            t(30f, 5f, "Camisea", 2, 0.75f); t(60f, 5f, "Total país", 5, 0.75f)
        } },
        "ex24_graficas" to { fig {
            fun panel(ox: Float, oy: Float, lab: String, f: (Float) -> Float, t0: Float, t1: Float, c: Int) {
                ln(ox, oy, ox + 40, oy, 5); ln(ox + 4, oy + 3, ox + 4, oy - 24, 5); t(ox - 3, oy - 22, lab, 0, 0.8f)
                var t = t0
                while (t < t1 - 0.01f) { val u = minOf(t + 0.1f, t1); ln(ox + 4 + t * 7, oy - f(t) * 3.5f, ox + 4 + u * 7, oy - f(u) * 3.5f, c, w = 1.2f); t = u }
            }
            panel(4f, 28f, "A", { 1 + 10 * it - 5 * it * it }, 0f, 2.095f, 1)
            panel(54f, 28f, "B", { -5 * (it - 1) * (it - 3) / 1f }, 1f, 3.3f, 2)
            panel(4f, 58f, "C", { 5 * it * (2 - it) }, 0f, 2f, 2)
            panel(54f, 58f, "D", { 1 + 10 * it - 5 * it * it }, 0f, 2.3f, 2)
        } },
        "ex24_piscinas" to { fig {
            ci(36f, 32f, 24f, 5); ci(72f, 32f, 12f, 5); dot(36f, 32f, 0); dot(72f, 32f, 0)
            ln(12f, 32f, 72f, 32f, 1, w = 1.6f); ln(72f, 32f, 80.5f, 23.5f, 2); t(80f, 20f, "R", 2); t(24f, 28f, "2R", 4, 0.8f)
        } },
        "ex25_pelota" to { fig {
            axes(10f, 54f, x0 = 6f, x1 = 92f, y0 = 3f, y1 = 57f)
            var t = 0f
            while (t < 6f) { val u = t + 0.1f; ln(10f + t * 12, 54f - (6 * t - t * t) * 5.5f, 10f + u * 12, 54f - (6 * u - u * u) * 5.5f, 1, w = 1.4f); t = u }
            ln(10f, 54f - 8 * 5.5f, 92f, 54f - 8 * 5.5f, 2, dash = true); t(88f, 54f - 8 * 5.5f - 3, "8 m", 2, 0.75f); t(46f, 5f, "9 m", 4, 0.75f); t(82f, 58f, "6 s", 4, 0.75f)
        } },
        "ex25_cremas" to { fig {
            ln(8f, 54f, 94f, 54f); ln(8f, 54f, 8f, 4f)
            for (x in 0..12) { val y = x * x + 5 * x; dot(8f + x * 6.6f, 54f - y * 0.23f, if (x == 7) 1 else 2) }
            t(8f + 7 * 6.6f, 54f - 84 * 0.23f - 5, "(7, 84)", 1, 0.75f); t(8f + 12 * 6.6f - 4, 54f - 204 * 0.23f - 4, "(12, 204)", 2, 0.75f); t(50f, 58f, "semanas", 5, 0.7f)
        } },
        "ex25_vivero" to { fig {
            val x0 = 30f; val y0 = 26f; val s = 28f; val dx = 12f; val dy = -9f
            poly(x0, y0, x0 + s, y0, x0 + s, y0 + s, x0, y0 + s)
            ln(x0, y0, x0 + dx, y0 + dy); ln(x0 + s, y0, x0 + s + dx, y0 + dy); ln(x0 + s, y0 + s, x0 + s + dx, y0 + s + dy); ln(x0 + dx, y0 + dy, x0 + s + dx, y0 + dy); ln(x0 + s + dx, y0 + dy, x0 + s + dx, y0 + s + dy)
            ln(x0 + dx, y0 + dy, x0 + dx, y0 + s + dy, 5, dash = true); ln(x0 + dx, y0 + s + dy, x0 + s + dx, y0 + s + dy, 5, dash = true); ln(x0, y0 + s, x0 + dx, y0 + s + dy, 5, dash = true)
            ln(x0, y0, x0 + dx, y0 + s + dy, 1); ln(x0, y0 + s, x0 + dx, y0 + dy, 1); ln(x0 + s, y0, x0 + s + dx, y0 + s + dy, 1); ln(x0 + s, y0 + s, x0 + s + dx, y0 + dy, 1)
            val ax = x0 + s / 2 + dx / 2; val ay = 2f
            ln(ax, ay, x0, y0, 2); ln(ax, ay, x0 + s, y0, 2); ln(ax, ay, x0 + s + dx, y0 + dy, 2); ln(ax, ay, x0 + dx, y0 + dy, 2, dash = true)
            t(x0 + s / 2, y0 + s + 4, "2 m", 0, 0.75f); t(ax + 14, 10f, "√5", 2, 0.8f)
        } },
        "ex25_cohete" to { fig {
            val k = 8f; val ox = 24f; val oy = 54f; val bx = ox + 2 * sqrt(3f) * k
            val y1 = oy - 2 * k; val y2 = oy - 6 * k
            ln(ox, oy, bx + 6, oy); ln(bx, oy, bx, y2 - 2, 5); ra(bx, oy, ox, oy, bx, y2)
            ln(ox, oy, bx, y1, 2); ln(ox, oy, bx, y2, 1); dot(bx, y1, 2); dot(bx, y2, 1)
            t((ox + bx) / 2, oy + 4, "2√3 km", 0, 0.8f); t(bx + 9, (oy + y1) / 2, "2 km", 2, 0.8f); t(bx + 5, (y1 + y2) / 2, "h", 1)
            t(ox + 13, oy - 3, "30°", 2, 0.7f); t(ox + 8, oy - 9, "60°", 1, 0.7f)
        } },
        "ex25_barras" to { fig {
            val a = listOf(6, 10, 12, 2); val b = listOf(5, 11, 10, 4); val lab = listOf("C", "B", "A", "AD")
            ln(8f, 52f, 94f, 52f)
            for (i in 0 until 4) {
                val x = 12f + i * 21f
                poly(x, 52f, x + 7f, 52f, x + 7f, 52f - a[i] * 3.4f, x, 52f - a[i] * 3.4f, c = 2, fill = true)
                poly(x + 8f, 52f, x + 15f, 52f, x + 15f, 52f - b[i] * 3.4f, x + 8f, 52f - b[i] * 3.4f, c = 4, fill = true)
                t(x + 3.5f, 52f - a[i] * 3.4f - 3, "${a[i]}", 2, 0.7f); t(x + 11.5f, 52f - b[i] * 3.4f - 3, "${b[i]}", 4, 0.7f); t(x + 7.5f, 56f, lab[i], 0, 0.8f)
            }
            t(30f, 4f, "Sección A", 2, 0.75f); t(70f, 4f, "Sección B", 4, 0.75f)
        } }
    )

    fun get(id: String): List<Pr>? = (defs[id] ?: exDefs[id])?.invoke()
    val ids: Set<String> get() = defs.keys + exDefs.keys
}
