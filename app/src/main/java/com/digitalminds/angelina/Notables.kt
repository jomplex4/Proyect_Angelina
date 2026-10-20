package com.digitalminds.angelina

/** Triángulo notable: ángulos, lados (opuesto y adyacente al ángulo menor, hipotenusa) en k, si es exacto y su figura. */
class NTri(val small: String, val big: String, val opp: String, val adj: String, val hyp: String, val exact: Boolean, val fig: String, val sides: String)

/** Fila del cuadro: ángulo, si es exacto y sus 6 razones (sen, cos, tan, cot, sec, csc) como expresiones exactas. */
class NRow(val angle: String, val exact: Boolean, val r: List<String>, val fig: String) {
    val deg get() = angle.toDouble()
    /** Valor real de cada razón (para mostrar cuánto se aleja el valor aproximado). */
    fun real(i: Int): Double {
        val x = Math.toRadians(deg)
        return when (i) { 0 -> Math.sin(x); 1 -> Math.cos(x); 2 -> Math.tan(x); 3 -> 1 / Math.tan(x); 4 -> 1 / Math.cos(x); else -> 1 / Math.sin(x) }
    }
}

/**
 * Cuadro de razones trigonométricas de los triángulos notables.
 * Cada razón sale de los lados del triángulo (CO/H, CA/H, CO/CA, CA/CO, H/CA, H/CO); NotablesTest lo comprueba con Giac:
 * los exactos coinciden con el valor real y los aproximados se alejan menos de 2%.
 */
object Notables {
    val names = listOf("sen", "cos", "tan", "cot", "sec", "csc")
    val tris = listOf(
        NTri("15", "75", "sqrt(6)-sqrt(2)", "sqrt(6)+sqrt(2)", "4", true, "n15", "(√6 − √2)k, (√6 + √2)k y 4k"),
        NTri("16", "74", "7", "24", "25", false, "n16", "7k, 24k y 25k"),
        NTri("30", "60", "1", "sqrt(3)", "2", true, "n30", "k, k√3 y 2k"),
        NTri("37", "53", "3", "4", "5", false, "n37", "3k, 4k y 5k"),
        NTri("45", "45", "1", "1", "sqrt(2)", true, "n45", "k, k y k√2"),
        NTri("8", "82", "1", "7", "5*sqrt(2)", false, "n8", "k, 7k y 5√2 k"),
        NTri("14", "76", "1", "4", "sqrt(17)", false, "n14", "k, 4k y √17 k"),
        NTri("18.5", "71.5", "1", "3", "sqrt(10)", false, "n18", "k, 3k y √10 k"),
        NTri("26.5", "63.5", "1", "2", "sqrt(5)", false, "n26", "k, 2k y √5 k")
    )
    val rows = listOf(
        NRow("8", false, listOf("sqrt(2)/10", "7*sqrt(2)/10", "1/7", "7", "5*sqrt(2)/7", "5*sqrt(2)"), "n8"),
        NRow("14", false, listOf("sqrt(17)/17", "4*sqrt(17)/17", "1/4", "4", "sqrt(17)/4", "sqrt(17)"), "n14"),
        NRow("15", true, listOf("(sqrt(6)-sqrt(2))/4", "(sqrt(6)+sqrt(2))/4", "2-sqrt(3)", "2+sqrt(3)", "sqrt(6)-sqrt(2)", "sqrt(6)+sqrt(2)"), "n15"),
        NRow("16", false, listOf("7/25", "24/25", "7/24", "24/7", "25/24", "25/7"), "n16"),
        NRow("18.5", false, listOf("sqrt(10)/10", "3*sqrt(10)/10", "1/3", "3", "sqrt(10)/3", "sqrt(10)"), "n18"),
        NRow("26.5", false, listOf("sqrt(5)/5", "2*sqrt(5)/5", "1/2", "2", "sqrt(5)/2", "sqrt(5)"), "n26"),
        NRow("30", true, listOf("1/2", "sqrt(3)/2", "sqrt(3)/3", "sqrt(3)", "2*sqrt(3)/3", "2"), "n30"),
        NRow("37", false, listOf("3/5", "4/5", "3/4", "4/3", "5/4", "5/3"), "n37"),
        NRow("45", true, listOf("sqrt(2)/2", "sqrt(2)/2", "1", "1", "sqrt(2)", "sqrt(2)"), "n45"),
        NRow("53", false, listOf("4/5", "3/5", "4/3", "3/4", "5/3", "5/4"), "n37"),
        NRow("60", true, listOf("sqrt(3)/2", "1/2", "sqrt(3)", "sqrt(3)/3", "2", "2*sqrt(3)/3"), "n30"),
        NRow("63.5", false, listOf("2*sqrt(5)/5", "sqrt(5)/5", "2", "1/2", "sqrt(5)", "sqrt(5)/2"), "n26"),
        NRow("71.5", false, listOf("3*sqrt(10)/10", "sqrt(10)/10", "3", "1/3", "sqrt(10)", "sqrt(10)/3"), "n18"),
        NRow("74", false, listOf("24/25", "7/25", "24/7", "7/24", "25/7", "25/24"), "n16"),
        NRow("75", true, listOf("(sqrt(6)+sqrt(2))/4", "(sqrt(6)-sqrt(2))/4", "2+sqrt(3)", "2-sqrt(3)", "sqrt(6)+sqrt(2)", "sqrt(6)-sqrt(2)"), "n15"),
        NRow("76", false, listOf("4*sqrt(17)/17", "sqrt(17)/17", "4", "1/4", "sqrt(17)", "sqrt(17)/4"), "n14"),
        NRow("82", false, listOf("7*sqrt(2)/10", "sqrt(2)/10", "7", "1/7", "5*sqrt(2)", "5*sqrt(2)/7"), "n8")
    )
}
