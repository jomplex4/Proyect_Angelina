package com.digitalminds.angelina

import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode

/** Una línea del resultado paso a paso. shown y result son expresiones (se dibujan como matemática) salvo text = true. */
class SLine(val label: String, val shown: String?, val result: String?, val approx: String? = null, val text: Boolean = false)

/** Resultado completo: líneas, tabla opcional (primera fila = encabezados) y error. */
class SOut(val lines: List<SLine>, val table: List<List<String>>? = null, val error: String? = null)

/** Fracción exacta: así las tablas de frecuencias no acumulan errores de redondeo. */
class Rat private constructor(val n: BigInteger, val d: BigInteger) : Comparable<Rat> {
    companion object {
        fun of(n: BigInteger, d: BigInteger = BigInteger.ONE): Rat {
            require(d.signum() != 0)
            val g = n.gcd(d).let { if (it.signum() == 0) BigInteger.ONE else it }
            val s = if (d.signum() < 0) BigInteger.ONE.negate() else BigInteger.ONE
            return Rat(n.divide(g).multiply(s), d.divide(g).multiply(s))
        }
        fun of(i: Long) = of(BigInteger.valueOf(i))

        /** Acepta enteros, decimales con punto y fracciones a/b. */
        fun parse(t0: String): Rat? {
            val t = t0.trim()
            if (t.isEmpty()) return null
            return try {
                if ("/" in t) { val (a, b) = t.split("/", limit = 2); val fa = parse(a) ?: return null; val fb = parse(b) ?: return null; if (fb.n.signum() == 0) null else fa / fb }
                else { val bd = BigDecimal(t); of(bd.unscaledValue(), BigInteger.TEN.pow(maxOf(0, bd.scale()))).let { if (bd.scale() < 0) of(bd.toBigIntegerExact()) else it } }
            } catch (_: Exception) { null }
        }
    }
    operator fun plus(o: Rat) = of(n * o.d + o.n * d, d * o.d)
    operator fun minus(o: Rat) = of(n * o.d - o.n * d, d * o.d)
    operator fun times(o: Rat) = of(n * o.n, d * o.d)
    operator fun div(o: Rat) = of(n * o.d, d * o.n)
    override fun compareTo(other: Rat) = (n * other.d).compareTo(other.n * d)
    override fun equals(other: Any?) = other is Rat && n == other.n && d == other.d
    override fun hashCode() = n.hashCode() * 31 + d.hashCode()
    val isInt get() = d == BigInteger.ONE
    fun toDouble() = BigDecimal(n).divide(BigDecimal(d), MathContext.DECIMAL64).toDouble()
    /** Forma para Giac y para dibujar: "a", "-a", "a/b". */
    override fun toString() = if (isInt) n.toString() else "$n/$d"
    /** Entre paréntesis si es negativa o fracción (para escribirla dentro de una operación). */
    fun p() = if (isInt && n.signum() >= 0) toString() else "($this)"
    /** Decimal con hasta k decimales, sin ceros sobrantes. */
    fun dec(k: Int = 4): String = BigDecimal(n).divide(BigDecimal(d), k, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()
}

object StatSolve {
    fun parseList(s: String): List<Rat>? {
        val parts = s.split(Regex("[,;\\s]+")).filter { it.isNotBlank() }
        if (parts.isEmpty()) return null
        return parts.map { Rat.parse(it) ?: return null }
    }

    private fun approx(f: Rat) = if (f.isInt) null else f.dec(6)
    private fun sqrtLine(label: String, f: Rat): SLine {
        val v = kotlin.math.sqrt(f.toDouble())
        return SLine(label, null, "sqrt($f)", BigDecimal(v).round(MathContext(7)).stripTrailingZeros().toPlainString())
    }

    /** Datos sueltos: ordena y calcula n, suma, media, mediana, moda, rango, varianzas y desviaciones. */
    fun loose(input: String): SOut {
        val xs = parseList(input) ?: return SOut(emptyList(), error = "Escribe los datos separados por comas, por ejemplo 12, 10, 14. Usa punto para decimales.")
        val s = xs.sorted(); val n = s.size
        val out = ArrayList<SLine>()
        out.add(SLine("Ordenamos los datos de menor a mayor", null, "[" + s.joinToString(",") + "]"))
        out.add(SLine("Cantidad de datos n", null, n.toString()))
        val sum = s.fold(Rat.of(0)) { a, b -> a + b }
        out.add(SLine("Suma de los datos S", if (n <= 12) s.joinToString("+") { it.p() } else null, sum.toString(), approx(sum)))
        val mean = sum / Rat.of(n.toLong())
        out.add(SLine("Media: x̄ = S / n", "$sum/$n", mean.toString(), approx(mean)))
        if (n % 2 == 1) {
            out.add(SLine("Mediana: n es impar, es el dato de la posición (n + 1)/2 = ${(n + 1) / 2}", null, s[n / 2].toString(), approx(s[n / 2])))
        } else {
            val a = s[n / 2 - 1]; val b = s[n / 2]; val me = (a + b) / Rat.of(2)
            out.add(SLine("Mediana: n es par, promedio de los datos ${n / 2} y ${n / 2 + 1}", "(${a.p()}+${b.p()})/2", me.toString(), approx(me)))
        }
        val counts = s.groupingBy { it }.eachCount()
        val top = counts.values.max()
        val modes = counts.filter { it.value == top }.keys.sorted()
        out.add(
            if (top == 1) SLine("Moda", null, "No hay moda: ningún dato se repite.", text = true)
            else SLine("Moda: el dato que más se repite ($top veces)", null, modes.joinToString(" y ") { it.toString() }, text = modes.size > 1)
        )
        val range = s.last() - s.first()
        out.add(SLine("Rango: máximo − mínimo", "${s.last().p()}-${s.first().p()}", range.toString(), approx(range)))
        val ss = s.fold(Rat.of(0)) { a, x -> a + (x - mean) * (x - mean) }
        val vp = ss / Rat.of(n.toLong())
        out.add(SLine("Varianza poblacional: σ² = Σ(x − x̄)² / n", "$ss/$n", vp.toString(), approx(vp)))
        out.add(sqrtLine("Desviación poblacional: σ = √σ²", vp))
        if (n > 1) {
            val vs = ss / Rat.of((n - 1).toLong())
            out.add(SLine("Varianza muestral: s² = Σ(x − x̄)² / (n − 1)", "$ss/${n - 1}", vs.toString(), approx(vs)))
            out.add(sqrtLine("Desviación muestral: s = √s²", vs))
        }
        return SOut(out)
    }

    /** Tabla de frecuencias con intervalos [Li; Ls⟩ de ancho w y sus medidas de tendencia central. */
    fun grouped(l0s: String, ws: String, fs: String): SOut {
        val l0 = Rat.parse(l0s) ?: return SOut(emptyList(), error = "Escribe el límite inferior del primer intervalo.")
        val w = Rat.parse(ws)?.takeIf { it > Rat.of(0) } ?: return SOut(emptyList(), error = "El ancho de clase debe ser un número mayor que 0.")
        val f = parseList(fs)?.takeIf { l -> l.all { it.isInt && it >= Rat.of(0) } } ?: return SOut(emptyList(), error = "Las frecuencias deben ser enteros separados por comas, por ejemplo 5, 8, 7.")
        val k = f.size
        val n = f.fold(Rat.of(0)) { a, b -> a + b }
        if (n <= Rat.of(0)) return SOut(emptyList(), error = "La suma de frecuencias debe ser mayor que 0.")
        val li = (0 until k).map { l0 + w * Rat.of(it.toLong()) }
        val x = li.map { it + w / Rat.of(2) }
        val cum = f.runningFold(Rat.of(0)) { a, b -> a + b }.drop(1)
        val table = ArrayList<List<String>>()
        table.add(listOf("Iᵢ", "xᵢ", "fᵢ", "Fᵢ", "hᵢ", "Hᵢ", "xᵢ·fᵢ"))
        var sxf = Rat.of(0)
        for (i in 0 until k) {
            val xf = x[i] * f[i]; sxf += xf
            table.add(listOf("[${li[i].dec()}; ${(li[i] + w).dec()}⟩", x[i].dec(), f[i].toString(), cum[i].toString(), (f[i] / n).dec(), (cum[i] / n).dec(), xf.dec()))
        }
        table.add(listOf("Total", "", n.toString(), "", "1", "", sxf.dec()))
        val out = ArrayList<SLine>()
        out.add(SLine("Marca de clase: xᵢ = (Li + Ls)/2; hᵢ = fᵢ/n; Fᵢ y Hᵢ son las sumas acumuladas", null, null))
        out.add(SLine("Total de datos: n = Σfᵢ", null, n.toString()))
        val mean = sxf / n
        out.add(SLine("Media: x̄ = Σ(xᵢ·fᵢ) / n", "$sxf/$n", mean.toString(), approx(mean)))
        val half = n / Rat.of(2)
        val mi = cum.indexOfFirst { it >= half }
        val fPrev = if (mi == 0) Rat.of(0) else cum[mi - 1]
        val me = li[mi] + w * (half - fPrev) / f[mi]
        out.add(SLine("Mediana: n/2 = ${half.dec()} cae en [${li[mi].dec()}; ${(li[mi] + w).dec()}⟩ (Li = ${li[mi].dec()}, F anterior = $fPrev, fᵢ = ${f[mi]})",
            "${li[mi].p()}+${w.p()}*(${half.p()}-${fPrev.p()})/${f[mi].p()}", me.toString(), approx(me)))
        val top = f.max(); val mo = f.indexOf(top)
        val d1 = top - (if (mo > 0) f[mo - 1] else Rat.of(0))
        val d2 = top - (if (mo < k - 1) f[mo + 1] else Rat.of(0))
        val many = f.count { it == top } > 1
        if (d1 + d2 == Rat.of(0)) {
            out.add(SLine("Moda: d₁ = d₂ = 0, se toma la marca de clase del intervalo modal", null, x[mo].toString(), approx(x[mo])))
        } else {
            val moda = li[mo] + w * d1 / (d1 + d2)
            out.add(SLine("Moda: intervalo [${li[mo].dec()}; ${(li[mo] + w).dec()}⟩ con fᵢ = $top; d₁ = $d1, d₂ = $d2" + if (many) " (hay varios intervalos modales; se usa el primero)" else "",
                "${li[mo].p()}+${w.p()}*${d1.p()}/(${d1.p()}+${d2.p()})", moda.toString(), approx(moda)))
        }
        return SOut(out, table)
    }
}
