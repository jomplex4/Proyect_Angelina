package com.digitalminds.angelina

enum class S { AC, DEL, EQ, LEFT, RIGHT, LP, RP, COMMA, FRAC, MIXED, SQRT, NROOT, CBRT, POW, SQUARE, CUBE, INV, EXP10, EXPE, X10, ANS, MAT2, MAT3, MAT4, LST, BASE, VERIFY, MYHINT }

sealed interface KA {
    data class T(val s: String) : KA
    data class F(val name: String, val pre: List<String>) : KA
    data class Sp(val s: S) : KA
    /** Plantilla: inserta una expresión completa (por ejemplo una función para graficar). */
    data class P(val src: String) : KA
}

/** Una página del teclado: identificador fijo, nombre visible y 10 teclas. */
class KeyPage(val id: String, val name: String, val keys: List<Key>)

class Key(val label: String, val act: KA, val role: Int = 0, val more: List<Pair<String, KA>> = emptyList(), val tapMenu: Boolean = false)

object Keys {
    private fun f(label: String, name: String, vararg pre: String, role: Int = 0, more: List<Pair<String, KA>> = emptyList()) =
        Key(label, KA.F(name, if (pre.isEmpty()) listOf("") else pre.toList()), role, more)

    private fun fv(label: String, name: String, vararg pre: String) = label to (KA.F(name, if (pre.isEmpty()) listOf("") else pre.toList()) as KA)
    private fun sp(label: String, s: S, role: Int = 0, more: List<Pair<String, KA>> = emptyList()) = Key(label, KA.Sp(s), role, more)
    private fun t(label: String, s: String = label, role: Int = 0, more: List<Pair<String, KA>> = emptyList()) = Key(label, KA.T(s), role, more)
    private fun tv(label: String, s: String = label) = label to (KA.T(s) as KA)
    private fun sv(label: String, s: S) = label to (KA.Sp(s) as KA)

    private fun p(label: String, src: String) = Key(label, KA.P(src))

    /** Páginas en orden de prioridad para Beca 18. "mis" (Mis teclas) se arma con las teclas que elige el usuario. */
    val allPages: List<KeyPage> = listOf(
        KeyPage("basico", "Básico", listOf(
            f("sen", "sin", more = listOf(fv("sen⁻¹", "asin"), fv("senh", "sinh"), fv("senh⁻¹", "asinh"))),
            f("cos", "cos", more = listOf(fv("cos⁻¹", "acos"), fv("cosh", "cosh"), fv("cosh⁻¹", "acosh"))),
            f("tan", "tan", more = listOf(fv("tan⁻¹", "atan"), fv("tanh", "tanh"), fv("tanh⁻¹", "atanh"))),
            f("ln", "ln", more = listOf(sv("eˣ", S.EXPE), fv("logₐ", "logb", "", ""))),
            f("log", "log10", more = listOf(sv("10ˣ", S.EXP10), fv("logₐ", "logb", "", ""))),
            sp("√", S.SQRT, more = listOf(sv("ⁿ√", S.NROOT), sv("∛", S.CBRT))),
            sp("x²", S.SQUARE, more = listOf(sv("x³", S.CUBE), sv("x⁻¹", S.INV))),
            sp("^", S.POW),
            t("π", more = listOf(tv("i"), tv("∞"))),
            t("e", more = listOf(sv("eˣ", S.EXPE)))
        )),
        KeyPage("algebra", "Álgebra", listOf(
            f("resolver", "solve", "", "x", more = listOf(fv("en ℂ", "csolve", "", "x"), fv("aprox.", "fsolve", "", "x=0"))),
            Key("= < >", KA.T("<"), 0, listOf(tv("="), tv("<"), tv(">"), tv("≤"), tv("≥"), tv("≠"), tv(" and "), tv(" or ")), true),
            f("factoriz.", "factor"), f("expandir", "expand"), f("simplif.", "simplify"),
            f("sist.lin", "linsolve", "[]", "[]"), f("raíces", "roots"), f("sustit.", "subst", "", "x="),
            f("▶a/b", "exact"), f("fr.parc.", "partfrac")
        )),
        KeyPage("aritmetica", "Aritmética", listOf(
            f("% de", "pctde", "", ""), f("+%", "pctmas", "", ""), f("−%", "pctmenos", "", ""), f("% único", "pctequiv", "[]"),
            t("n!", "!"), f("nCr", "comb", "", ""), f("nPr", "perm", "", ""),
            f("MCD", "gcd", "", ""), f("MCM", "lcm", "", ""),
            f("mod", "irem", "", "", more = listOf(fv("cociente", "iquo", "", ""), fv("factores", "ifactor"), fv("¿primo?", "isprime"), fv("sig.primo", "nextprime")))
        )),
        KeyPage("trigo", "Trigonometría", listOf(
            f("sen⁻¹", "asin", more = listOf(fv("senh⁻¹", "asinh"))),
            f("cos⁻¹", "acos", more = listOf(fv("cosh⁻¹", "acosh"))),
            f("tan⁻¹", "atan", more = listOf(fv("tanh⁻¹", "atanh"))),
            f("cot", "cot", more = listOf(fv("cot⁻¹", "acot"))),
            f("sec", "sec", more = listOf(fv("sec⁻¹", "asec"))),
            f("csc", "csc", more = listOf(fv("csc⁻¹", "acsc"))),
            f("senh", "sinh", more = listOf(fv("senh⁻¹", "asinh"))),
            f("cosh", "cosh", more = listOf(fv("cosh⁻¹", "acosh"))),
            f("tanh", "tanh", more = listOf(fv("tanh⁻¹", "atanh"))),
            f("|x|", "abs", more = listOf(fv("signo", "sign"), fv("conjug.", "conj")))
        )),
        KeyPage("graficas", "Gráficas", listOf(
            p("recta", "2*x+1"), p("parábola", "x^2-4*x+3"), p("cúbica", "x^3-3*x"), p("expon.", "2^x"), p("logar.", "ln(x)"),
            p("onda sen", "sin(x)"), p("onda cos", "cos(x)"), p("en V", "abs(x-2)"), p("raíz x", "sqrt(x)"), p("hipérbola", "1/x")
        )),
        KeyPage("calculo", "Cálculo", listOf(
            f("d/dx", "diff", "", "x", more = listOf(fv("dⁿ/dxⁿ", "diff", "", "x", "2"), fv("∂/∂y", "diff", "", "y"))),
            f("∫", "int", "", "x"), f("∫ₐᵇ", "int", "", "x", "", ""),
            f("lím", "limit", "", "x", "", more = listOf(fv("lím⁺", "limit", "", "x", "", "1"), fv("lím⁻", "limit", "", "x", "", "-1"))),
            f("Σ", "sum", "", "k", "1", ""), f("Π", "product", "", "k", "1", ""),
            f("Taylor", "taylor", "", "x=0", "5", more = listOf(fv("serie", "series", "", "x=0", "5"))),
            f("aprox.", "fsolve", "", "x=0"),
            f("EDO", "desolve", "", "x", "y"),
            f("Laplace", "laplace", "", "t", "s", more = listOf(fv("Laplace⁻¹", "ilaplace", "", "s", "t"), fv("Z", "ztrans", "", "n", "z"), fv("Fourier", "fourier", "", "t", "w")))
        )),
        KeyPage("matrices", "Matrices", listOf(
            sp("[M]", S.MAT2, more = listOf(sv("3×3", S.MAT3), sv("4×4", S.MAT4), sv("[ ]", S.LST))),
            f("det", "det", more = listOf(fv("Aᵀ", "transpose"), fv("rref", "rref"), fv("LU", "lu"), fv("QR", "qr"), fv("traza", "trace"))),
            f("inv", "inv"), f("autoval", "eigenvals", more = listOf(fv("autovec", "eigenvects"))),
            f("punto", "dot", "", "", more = listOf(fv("norma", "norm"), fv("cruz", "cross", "", ""))),
            f("cruz", "cross", "", ""), t("i", more = listOf(fv("Re", "re"), fv("Im", "im"), fv("arg", "arg"), fv("conjug.", "conj"))), f("▶polar", "polar_coordinates"), f("▶rect", "normal"),
            Key("Bin-Hex", KA.Sp(S.BASE), 0, listOf(fv("and", "bitand", "", ""), fv("or", "bitor", "", ""), fv("xor", "bitxor", "", ""), fv("not", "not"), fv("shl", "shl", "", ""), fv("shr", "shr", "", ""), tv("0x"), tv("0b"), tv("A"), tv("B"), tv("C"), tv("D"), tv("E"), tv("F")))
        )),
        KeyPage("mas", "Más funciones", listOf(
            f("piso", "floor", more = listOf(fv("p.dec", "fPart"), fv("p.ent", "iPart"))),
            f("techo", "ceil"), f("redond.", "round"),
            f("aleat", "rand", "0", "1", more = listOf(fv("aleat.ent", "randint", "1", "6"))), sp("10ˣ", S.EXP10),
            f("factores", "ifactor"), f("¿primo?", "isprime"), f("cociente", "iquo", "", ""),
            f("▶GMS", "dms"), f("GMS▶", "fromdms", "", "", "")
        ))
    )

    const val MY = "mis"
    const val MY_NAME = "Mis teclas"
    const val MY_MAX = 10

    /** Casilla libre de "Mis teclas": al tocarla explica cómo agregar teclas. */
    val emptySlot = Key("+", KA.Sp(S.MYHINT))

    val pages: List<List<Key>> get() = allPages.map { it.keys }

    /** Orden por defecto: por prioridad de uso para Beca 18, con Mis teclas al final. */
    val defaultOrder: List<String> get() = allPages.map { it.id } + MY

    fun pageName(id: String) = if (id == MY) MY_NAME else allPages.firstOrNull { it.id == id }?.name ?: id

    /**
     * Páginas visibles: respeta el orden guardado, agrega al final las páginas nuevas de una actualización,
     * quita las ocultas y nunca deja el teclado sin páginas.
     */
    fun arrange(order: List<String>, hidden: Set<String>, myKeys: List<String>): List<KeyPage> {
        val known = defaultOrder
        val ids = order.filter { it in known }.distinct() + known.filter { it !in order }
        val vis = ids.filter { it !in hidden }.ifEmpty { listOf(known.first()) }
        return vis.map { id ->
            if (id == MY) KeyPage(MY, MY_NAME, (myKeys.mapNotNull { byLabel[it] }.take(MY_MAX) + List(MY_MAX) { emptySlot }).take(MY_MAX))
            else allPages.first { it.id == id }
        }
    }

    /** Ordena las páginas de más a menos usada (empate: se mantiene el orden actual). */
    fun byUsage(order: List<String>, usage: Map<String, Int>): List<String> {
        val ids = order.filter { it in defaultOrder }.distinct() + defaultOrder.filter { it !in order }
        return ids.sortedByDescending { usage[it] ?: 0 }
    }

    private val letters = ('A'..'Z').map { tv(it.toString()) }

    val fixed: List<Key> = listOf(
        sp("(", S.LP), sp(")", S.RP),
        Key("a/b", KA.Sp(S.FRAC), 0, listOf(sv("a b/c", S.MIXED))),
        Key("x", KA.T("x"), 0, listOf(tv("y"), tv("z"), tv("t"), tv("y'"), tv("y''"), tv(":=")) + letters),
        Key(",", KA.Sp(S.COMMA), 0, listOf(sv("[ ]", S.LST), tv(":="), tv("="), tv("!")))
    )

    /** Todas las teclas y variantes por etiqueta (para ayuda, manual y "Mis teclas"). */
    val byLabel: Map<String, Key> by lazy {
        val m = LinkedHashMap<String, Key>()
        fun add(k: Key) { m.putIfAbsent(k.label, k); for ((l, a) in k.more) m.putIfAbsent(l, Key(l, a)) }
        (pages.flatten() + fixed + numeric.flatten()).forEach { add(it) }
        m
    }

    /** Tecla principal que contiene una variante (para mostrar sus hermanas en la ayuda). */
    fun parentOf(label: String): Key? = (pages.flatten() + fixed + numeric.flatten()).firstOrNull { k -> k.label == label || k.more.any { it.first == label } }

    val numeric: List<List<Key>> = listOf(
        listOf(t("7", role = 1), t("8", role = 1), t("9", role = 1), sp("⌫", S.DEL, 2), sp("AC", S.AC, 2)),
        listOf(t("4", role = 1), t("5", role = 1), t("6", role = 1), t("×", role = 2), t("÷", role = 2)),
        listOf(t("1", role = 1), t("2", role = 1), t("3", role = 1), t("+", role = 2), t("−", role = 2)),
        listOf(t("0", role = 1), t(".", role = 1), sp("×10ˣ", S.X10, 2), sp("Ans", S.ANS, 2, more = listOf(tv("PreAns"))), sp("=", S.EQ, 3, more = listOf(sv("¿Es cierto?", S.VERIFY))))
    )
}
