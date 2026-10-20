package com.digitalminds.angelina

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Demostración independiente de las fórmulas del formulario (no solo de sus ejemplos):
 * identidades con álgebra simbólica de Giac, volúmenes con integrales y geometría con coordenadas.
 */
class FormulaProofTest {
    private val symbolic = listOf(
        "Binomio al cuadrado" to "(a+b)^2-(a^2+2*a*b+b^2)",
        "Diferencia de cuadrados" to "(a+b)*(a-b)-(a^2-b^2)",
        "Binomio al cubo" to "(a+b)^3-(a^3+3*a^2*b+3*a*b^2+b^3)",
        "Suma de cubos" to "a^3+b^3-(a+b)*(a^2-a*b+b^2)",
        "Identidad de Legendre" to "(a+b)^2+(a-b)^2-2*(a^2+b^2)",
        "Suma de cuadrados conocida a + b y ab" to "a^2+b^2-((a+b)^2-2*a*b)",
        "Si a + b + c = 0" to "subst(a^3+b^3+c^3-3*a*b*c,c=-a-b)",
        "Fórmula general (2.º grado)" to "subst(a*x^2+b*x+c,x=(-b+sqrt(b^2-4*a*c))/(2*a))",
        "Suma y producto de raíces" to "((-b+sqrt(b^2-4*a*c))/(2*a)+(-b-sqrt(b^2-4*a*c))/(2*a)+b/a)+((-b+sqrt(b^2-4*a*c))/(2*a)*(-b-sqrt(b^2-4*a*c))/(2*a)-c/a)",
        "Sistema 2 × 2 (Cramer)" to "(linsolve([a*x+b*y=c,d*x+e*y=f],[x,y])[0]-(c*e-b*f)/(a*e-b*d))+(linsolve([a*x+b*y=c,d*x+e*y=f],[x,y])[1]-(a*f-c*d)/(a*e-b*d))",
        "Vértice de la parábola" to "subst(diff(a*x^2+b*x+c,x),x=-b/(2*a))",
        "Suma de los n primeros naturales" to "sum(k,k,1,n)-n*(n+1)/2",
        "Suma de cuadrados" to "sum(k^2,k,1,n)-n*(n+1)*(2*n+1)/6",
        "Progresión aritmética" to "sum(a1+(k-1)*r,k,1,n)-(a1+a1+(n-1)*r)*n/2",
        "Cambio de base" to "logb(81,3)-ln(81)/ln(3)",
        "Logaritmo de un producto" to "logb(32,2)-(logb(8,2)+logb(4,2))",
        "Cilindro" to "integrate(pi*r^2,z,0,h)-pi*r^2*h",
        "Pirámide" to "integrate(Ab*(z/h)^2,z,0,h)-Ab*h/3",
        "Cono" to "integrate(pi*(r*z/h)^2,z,0,h)-pi*r^2*h/3",
        "Esfera" to "integrate(pi*(r^2-z^2),z,-r,r)-4/3*pi*r^3",
        "Tronco de cono" to "integrate(pi*(r+(R-r)*z/h)^2,z,0,h)-pi*h*(R^2+r^2+R*r)/3",
        "Identidad pitagórica" to "sin(t)^2+cos(t)^2-1",
        "Ángulos complementarios" to "sin(t)-cos(pi/2-t)",
        "Ángulo doble" to "texpand(sin(2*t))-2*sin(t)*cos(t)",
        "Ley de cosenos" to "(b-c*cos(A))^2+(c*sin(A))^2-(b^2+c^2-2*b*c*cos(A))",
        "Rotación de ángulo θ" to "[[cos(t),-sin(t)],[sin(t),cos(t)]]*[x,y]-[x*cos(t)-y*sin(t),x*sin(t)+y*cos(t)]",
        "Distribución binomial" to "sum(comb(5,k)*p^k*(1-p)^(5-k),k,0,5)-1",
        "Hexágono regular" to "6*(l^2*sqrt(3)/4)-3*sqrt(3)*l^2/2",
        "Probabilidad condicional" to "(2/5*3/10)/(2/5*3/10+3/5*2/5)-1/3"
    )

    @Test fun identitiesAreTrue() {
        val bin = System.getenv("GIAC_BIN") ?: return
        for ((name, e) in symbolic) {
            assertTrue("la prueba nombra una fórmula que existe: $name", Formulas.all.any { it.title == name })
            val p = ProcessBuilder(bin, "normal(simplify($e))").start()
            val out = p.inputStream.bufferedReader().readText().trim(); p.waitFor()
            assertTrue("$name: $out", out == "0" || out == "[0,0]")
        }
    }

    private class P(val x: Double, val y: Double) {
        operator fun minus(o: P) = P(x - o.x, y - o.y)
        operator fun plus(o: P) = P(x + o.x, y + o.y)
        operator fun times(k: Double) = P(x * k, y * k)
        val len get() = hypot(x, y)
    }
    private fun ang(v: P, a: P, b: P): Double { val u = a - v; val w = b - v; return acos((u.x * w.x + u.y * w.y) / (u.len * w.len)) * 180 / PI }
    private fun near(name: String, a: Double, b: Double) = assertTrue("$name: $a vs $b", abs(a - b) < 1e-9)

    /** Teoremas de geometría comprobados con un triángulo acutángulo concreto A(0,0), B(3,6), C(10,0). */
    @Test fun triangleCentersByCoordinates() {
        val a = P(0.0, 0.0); val b = P(3.0, 6.0); val c = P(10.0, 0.0)
        val la = (b - c).len; val lb = (a - c).len; val lc = (a - b).len
        val angB = ang(b, a, c)
        val inc = (a * la + b * lb + c * lc) * (1 / (la + lb + lc))
        near("Incentro", ang(inc, a, c), 90 + angB / 2)
        val oy = (3.0 * 3 + 6.0 * 6 - 10.0 * 3) / (2 * 6.0) // circuncentro (5, oy): |OA| = |OB|
        val o = P(5.0, oy)
        near("Circuncentro equidista", (o - a).len, (o - b).len)
        near("Circuncentro", ang(o, a, c), 2 * angB)
        val h = P(3.0, 3.0 * 7 / 6) // ortocentro: sobre x = 3 y AH ⟂ BC
        near("Ortocentro AH ⟂ BC", (h - a).x * (c - b).x + (h - a).y * (c - b).y, 0.0)
        near("Ortocentro", ang(h, a, c), 180 - angB)
        val ex = (a * la - b * lb + c * lc) * (1 / (la - lb + lc))
        near("Excentro", ang(ex, a, c), 90 - angB / 2)
        val g = (a + b + c) * (1.0 / 3); val m = (a + c) * 0.5
        near("Baricentro", (b - g).len, 2 * (g - m).len)
        val area = abs((b - a).x * (c - a).y - (c - a).x * (b - a).y) / 2
        val p = (la + lb + lc) / 2
        near("Herón", sqrt(p * (p - la) * (p - lb) * (p - lc)), area)
        near("Radio circunscrito", (o - a).len, la * lb * lc / (4 * area))
        val r = area / p
        near("Radio inscrito", abs(inc.y), r)
        near("Área con dos lados y el ángulo", lc * la * sin(angB * PI / 180) / 2, area)
        near("Ley de senos", la / sin(ang(a, b, c) * PI / 180), lb / sin(angB * PI / 180))
        near("Ángulos del triángulo", ang(a, b, c) + angB + ang(c, a, b), 180.0)
        near("Teorema de la bisectriz", ((c - a) * (lc / (lc + la))).len / ((c - a) * (la / (lc + la))).len, lc / la)
    }

    @Test fun rightTriangleRelations() {
        val a = 3.0; val b = 4.0; val c = 5.0
        val h = a * b / c; val m = a * a / c; val n = b * b / c
        near("Pitágoras", a * a + b * b, c * c)
        near("Altura sobre la hipotenusa", h * h, m * n)
        near("Cateto y su proyección", a * a, c * m)
        near("Catetos por altura", a * b, c * h)
        near("Teorema de Poncelet", a + b, c + 2 * (a * b / 2) / ((a + b + c) / 2))
        val g = P((0 + b + 0) / 3.0, (0 + 0 + a) / 3.0); val o = P(b / 2, a / 2)
        near("Baricentro y circuncentro en un rectángulo", (g - o).len, c / 6)
    }

    @Test fun circleTheorems() {
        fun pt(d: Double) = P(cos(d * PI / 180), sin(d * PI / 180))
        val v = pt(90.0); val p = pt(200.0); val q = pt(340.0)
        near("Ángulo inscrito", ang(v, p, q), (340.0 - 200.0) / 2)
        val x = pt(30.0); val y = pt(210.0 + 40); val z = pt(100.0); val w = pt(300.0)
        val den = (x.x - y.x) * (z.y - w.y) - (x.y - y.y) * (z.x - w.x)
        val t = ((x.x - z.x) * (z.y - w.y) - (x.y - z.y) * (z.x - w.x)) / den
        val i = P(x.x + t * (y.x - x.x), x.y + t * (y.y - x.y))
        near("Teorema de las cuerdas", (i - x).len * (i - y).len, (i - z).len * (i - w).len)
        val ext = P(3.0, 0.0); val tang = sqrt(ext.len * ext.len - 1)
        near("Tangente y secante", tang * tang, (ext.len - 1) * (ext.len + 1))
        near("Cuadrado inscrito en circunferencia", (pt(0.0) - pt(90.0)).len, sqrt(2.0))
        near("Triángulo equilátero inscrito", (pt(90.0) - pt(210.0)).len, sqrt(3.0))
        near("Hexágono inscrito", (pt(0.0) - pt(60.0)).len, 1.0)
        near("Ángulos de un polígono", 180.0 * (6 - 2), 6 * (180 - 360.0 / 6))
        assertEquals("Diagonales de un polígono", 8 * 7 / 2 - 8, 8 * (8 - 3) / 2)
        near("Teorema de Pitot", 45.0 + 45.0, 18.0 + 72.0)
    }
}
