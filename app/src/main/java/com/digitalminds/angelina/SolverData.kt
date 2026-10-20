package com.digitalminds.angelina

/** Dato que pide un resolutor: identificador, etiqueta y valor de ejemplo. list = lista de números escrita como texto. */
class SIn(val id: String, val label: String, val def: String, val list: Boolean = false)

/** Paso: texto, expresión que se muestra (con {datos}), expresión que se calcula, nombre donde se guarda el resultado y si se calcula sin simplificar. */
class SStep(val label: String, val show: String, val eval: String, val save: String = "", val raw: Boolean = false)

/** special: 0 = pasos con Giac; 1 = datos sueltos; 2 = tabla de frecuencias. */
class Solver(val sec: String, val id: String, val title: String, val desc: String, val inputs: List<SIn>, val formula: String, val steps: List<SStep>, val note: String = "", val links: List<String> = emptyList(), val special: Int = 0)

/** Resolutores "paso a paso" offline. Los resultados de los ejemplos los comprueba SolverTest con Giac real. */
object Solvers {
    val secs = listOf(FSec("arit", "Aritmética"), FSec("alg", "Álgebra"), FSec("plana", "Geometría"), FSec("solidos", "Sólidos"), FSec("trigo", "Trigonometría"), FSec("transf", "Transformaciones"), FSec("estad", "Estadística"), FSec("prob", "Probabilidad"))

    private val bare = Regex("\\(([0-9]+(\\.[0-9]+)?)\\)")
    private val simple = Regex("^([0-9]+(\\.[0-9]+)?|[A-Za-z_][A-Za-z0-9_]*)$")

    /** Reemplaza {dato} por su valor; los valores compuestos o negativos van entre paréntesis. */
    fun subst(t: String, v: Map<String, String>): String {
        var s = t
        for ((k, x0) in v) {
            // (2)/(5) se escribe 2/5: quitar paréntesis alrededor de un número no negativo nunca cambia el valor.
            val x = x0.replace(bare, "$1")
            s = s.replace("{$k}", if (simple.matches(x) || wrapped(x)) x else "($x)")
        }
        return s
    }

    /** true si todo el valor ya está dentro de un único par de paréntesis, como (-3). */
    private fun wrapped(x: String): Boolean {
        if (!x.startsWith("(") || !x.endsWith(")")) return false
        var depth = 0
        for ((i, c) in x.withIndex()) {
            if (c == '(') depth++ else if (c == ')') depth--
            if (depth == 0 && i < x.lastIndex) return false
        }
        return true
    }

    /** La plantilla con los nombres de los datos (para mostrar la fórmula). */
    fun symbolic(t: String): String = t.replace("{", "").replace("}", "")

    fun byId(id: String): Solver? = all.firstOrNull { it.id == id }
    fun forFormula(title: String): Solver? = all.firstOrNull { title in it.links }

    val all: List<Solver> = listOf(
        Solver("arit", "pct", "Tanto por ciento", "Calcula el a% de una cantidad N.",
            listOf(SIn("a", "Porcentaje a (%)", "20"), SIn("N", "Cantidad N", "300")),
            "P=a/100*N",
            listOf(
            SStep("Sustituimos en P = a/100 · N", "{a}/100*{N}", "{a}/100*{N}", "P")
            ), "", listOf("Tanto por ciento")),
        Solver("arit", "sucesivos", "Aumentos y descuentos sucesivos", "Hasta tres variaciones seguidas: aumentos con +, descuentos con −. Pon 0 en las que no uses.",
            listOf(SIn("P", "Precio inicial", "50"), SIn("p1", "1.ª variación (%)", "10"), SIn("p2", "2.ª variación (%)", "-30"), SIn("p3", "3.ª variación (%)", "-5")),
            "F=(1+p1/100)*(1+p2/100)*(1+p3/100)",
            listOf(
            SStep("Factor total: se multiplican los factores (1 + p/100)", "(1+{p1}/100)*(1+{p2}/100)*(1+{p3}/100)", "(1+{p1}/100)*(1+{p2}/100)*(1+{p3}/100)", "F"),
            SStep("Precio final = precio inicial × factor", "{P}*{F}", "{P}*{F}"),
            SStep("Variación única en %: (F − 1) × 100", "({F}-1)*100", "({F}-1)*100")
            ), "Si la variación única sale negativa, es un descuento único.", listOf("Aumentos sucesivos", "Descuentos sucesivos")),
        Solver("arit", "interes", "Interés simple", "Interés y monto con tasa y tiempo en la misma unidad.",
            listOf(SIn("C", "Capital C", "3000"), SIn("r", "Tasa r (%)", "30"), SIn("t", "Tiempo t", "3")),
            "I=C*r*t/100",
            listOf(
            SStep("Interés: I = C · r · t / 100", "{C}*{r}*{t}/100", "{C}*{r}*{t}/100", "I"),
            SStep("Monto: M = C + I", "{C}+{I}", "{C}+{I}")
            ), "Si la tasa es anual y el tiempo está en meses, escribe t como meses/12.", listOf("Interés simple", "Monto")),
        Solver("arit", "r3d", "Regla de tres directa", "Si a corresponde a b, ¿cuánto corresponde a c? (más de uno, más del otro).",
            listOf(SIn("a", "a", "3"), SIn("b", "b (corresponde a a)", "12"), SIn("c", "c", "5")),
            "x=b*c/a",
            listOf(
            SStep("x = b · c / a", "{b}*{c}/{a}", "{b}*{c}/{a}")
            ), "", listOf("Regla de tres simple directa")),
        Solver("arit", "r3i", "Regla de tres inversa", "Si una magnitud aumenta, la otra disminuye.",
            listOf(SIn("a", "a", "6"), SIn("b", "b (corresponde a a)", "10"), SIn("c", "c", "4")),
            "x=a*b/c",
            listOf(
            SStep("x = a · b / c", "{a}*{b}/{c}", "{a}*{b}/{c}")
            ), "", listOf("Regla de tres simple inversa")),
        Solver("arit", "promedios", "Promedios de dos números", "Aritmético, geométrico y armónico.",
            listOf(SIn("a", "a", "4"), SIn("b", "b", "9")),
            "MA=(a+b)/2",
            listOf(
            SStep("Aritmético: (a + b)/2", "({a}+{b})/2", "({a}+{b})/2"),
            SStep("Geométrico: √(a · b)", "sqrt({a}*{b})", "sqrt({a}*{b})"),
            SStep("Armónico: 2ab/(a + b)", "2*{a}*{b}/({a}+{b})", "2*{a}*{b}/({a}+{b})")
            ), "Siempre MA ≥ MG ≥ MH.", listOf("Promedio aritmético", "Promedio geométrico", "Promedio armónico")),
        Solver("arit", "mcdmcm", "MCD y MCM", "Por descomposición en factores primos.",
            listOf(SIn("a", "a", "12"), SIn("b", "b", "18")),
            "a*b=MCD*MCM",
            listOf(
            SStep("Descomponemos a en primos", "ifactor({a})", "ifactor({a})", raw = true),
            SStep("Descomponemos b en primos", "ifactor({b})", "ifactor({b})", raw = true),
            SStep("MCD: factores comunes con su menor exponente", "gcd({a},{b})", "gcd({a},{b})", "M"),
            SStep("MCM: todos los factores con su mayor exponente", "lcm({a},{b})", "lcm({a},{b})", "m"),
            SStep("Comprobación: MCD · MCM = a · b", "{M}*{m}", "{M}*{m}")
            ), "", listOf("MCD y MCM")),
        Solver("alg", "lineal", "Ecuación de primer grado", "Resuelve a·x + b = c.",
            listOf(SIn("a", "a", "3"), SIn("b", "b", "6"), SIn("c", "c", "21")),
            "a*x+b=c",
            listOf(
            SStep("Pasamos b al otro lado: a·x = c − b", "{c}-{b}", "{c}-{b}", "D"),
            SStep("Dividimos entre a: x = (c − b)/a", "{D}/{a}", "{D}/{a}")
            ), "", listOf("Ecuación de primer grado")),
        Solver("alg", "cuadratica", "Ecuación de segundo grado", "Resuelve a·x² + b·x + c = 0 con la fórmula general.",
            listOf(SIn("a", "a", "2"), SIn("b", "b", "-3"), SIn("c", "c", "-2")),
            "x=(-b+sqrt(b^2-4*a*c))/(2*a)",
            listOf(
            SStep("Discriminante: Δ = b² − 4ac", "{b}^2-4*{a}*{c}", "{b}^2-4*{a}*{c}", "D"),
            SStep("Raíz del discriminante: √Δ", "sqrt({D})", "sqrt({D})", "R"),
            SStep("x₁ = (−b + √Δ)/(2a)", "(-{b}+{R})/(2*{a})", "(-{b}+{R})/(2*{a})"),
            SStep("x₂ = (−b − √Δ)/(2a)", "(-{b}-{R})/(2*{a})", "(-{b}-{R})/(2*{a})"),
            SStep("Suma de raíces: −b/a", "-{b}/{a}", "-{b}/{a}"),
            SStep("Producto de raíces: c/a", "{c}/{a}", "{c}/{a}"),
            SStep("Vértice: h = −b/(2a)", "-{b}/(2*{a})", "-{b}/(2*{a})")
            ), "Δ > 0: dos raíces reales; Δ = 0: raíz doble; Δ < 0: raíces complejas (con i).", listOf("Fórmula general (2.º grado)", "Discriminante", "Suma y producto de raíces", "Vértice de la parábola")),
        Solver("alg", "sistema", "Sistema 2 × 2", "Resuelve a·x + b·y = c y d·x + e·y = f por determinantes (Cramer).",
            listOf(SIn("a", "a", "4"), SIn("b", "b", "3"), SIn("c", "c", "8"), SIn("d", "d", "1"), SIn("e", "e", "-2"), SIn("f", "f", "2")),
            "x=(c*e-b*f)/(a*e-b*d)",
            listOf(
            SStep("Determinante: D = a·e − b·d", "{a}*{e}-{b}*{d}", "{a}*{e}-{b}*{d}", "D"),
            SStep("Dx = c·e − b·f", "{c}*{e}-{b}*{f}", "{c}*{e}-{b}*{f}", "X"),
            SStep("Dy = a·f − c·d", "{a}*{f}-{c}*{d}", "{a}*{f}-{c}*{d}", "Y"),
            SStep("x = Dx / D", "{X}/{D}", "{X}/{D}"),
            SStep("y = Dy / D", "{Y}/{D}", "{Y}/{D}")
            ), "Si D = 0 el sistema no tiene solución única.", listOf("Sistema 2 × 2 (Cramer)")),
        Solver("alg", "recta", "Recta por dos puntos", "Pendiente, intercepto, distancia y punto medio.",
            listOf(SIn("x1", "x₁", "2"), SIn("y1", "y₁", "3"), SIn("x2", "x₂", "4"), SIn("y2", "y₂", "7")),
            "m=(y2-y1)/(x2-x1)",
            listOf(
            SStep("Pendiente: m = (y₂ − y₁)/(x₂ − x₁)", "({y2}-{y1})/({x2}-{x1})", "({y2}-{y1})/({x2}-{x1})", "m"),
            SStep("Intercepto: b = y₁ − m·x₁", "{y1}-{m}*{x1}", "{y1}-{m}*{x1}"),
            SStep("Distancia: √((x₂ − x₁)² + (y₂ − y₁)²)", "sqrt(({x2}-{x1})^2+({y2}-{y1})^2)", "sqrt(({x2}-{x1})^2+({y2}-{y1})^2)"),
            SStep("Punto medio", "[({x1}+{x2})/2,({y1}+{y2})/2]", "[({x1}+{x2})/2,({y1}+{y2})/2]")
            ), "La recta es y = m·x + b.", listOf("Pendiente de una recta", "Distancia entre dos puntos", "Punto medio")),
        Solver("plana", "pitagoras", "Pitágoras: hipotenusa", "Con los dos catetos.",
            listOf(SIn("a", "Cateto a", "3"), SIn("b", "Cateto b", "4")),
            "c=sqrt(a^2+b^2)",
            listOf(
            SStep("c² = a² + b²", "{a}^2+{b}^2", "{a}^2+{b}^2", "q"),
            SStep("c = √(c²)", "sqrt({q})", "sqrt({q})")
            ), "", listOf("Teorema de Pitágoras")),
        Solver("plana", "pitagoras2", "Pitágoras: cateto", "Con la hipotenusa y un cateto.",
            listOf(SIn("c", "Hipotenusa c", "13"), SIn("a", "Cateto a", "5")),
            "b=sqrt(c^2-a^2)",
            listOf(
            SStep("b² = c² − a²", "{c}^2-{a}^2", "{c}^2-{a}^2", "q"),
            SStep("b = √(b²)", "sqrt({q})", "sqrt({q})")
            ), "", listOf()),
        Solver("plana", "heron", "Triángulo por sus tres lados", "Área con Herón y radios inscrito y circunscrito.",
            listOf(SIn("a", "Lado a", "13"), SIn("b", "Lado b", "14"), SIn("c", "Lado c", "15")),
            "A=sqrt(p*(p-a)*(p-b)*(p-c))",
            listOf(
            SStep("Semiperímetro: p = (a + b + c)/2", "({a}+{b}+{c})/2", "({a}+{b}+{c})/2", "p"),
            SStep("Área: A = √(p(p − a)(p − b)(p − c))", "sqrt({p}*({p}-{a})*({p}-{b})*({p}-{c}))", "sqrt({p}*({p}-{a})*({p}-{b})*({p}-{c}))", "A"),
            SStep("Radio inscrito: r = A/p", "{A}/{p}", "{A}/{p}"),
            SStep("Radio circunscrito: R = abc/(4A)", "{a}*{b}*{c}/(4*{A})", "{a}*{b}*{c}/(4*{A})")
            ), "Cada lado debe ser menor que la suma de los otros dos.", listOf("Fórmula de Herón", "Radio inscrito en un triángulo", "Radio circunscrito")),
        Solver("plana", "poligono", "Polígono regular", "Ángulos, diagonales y perímetro.",
            listOf(SIn("n", "Número de lados n", "6"), SIn("l", "Lado l", "2")),
            "S=180*(n-2)",
            listOf(
            SStep("Suma de ángulos interiores: 180(n − 2)", "180*({n}-2)", "180*({n}-2)", "S"),
            SStep("Ángulo interior: S/n", "{S}/{n}", "{S}/{n}"),
            SStep("Ángulo exterior: 360/n", "360/{n}", "360/{n}"),
            SStep("Diagonales: n(n − 3)/2", "{n}*({n}-3)/2", "{n}*({n}-3)/2"),
            SStep("Perímetro: n · l", "{n}*{l}", "{n}*{l}")
            ), "", listOf("Ángulos de un polígono", "Diagonales de un polígono")),
        Solver("plana", "circulo", "Círculo y sector", "Área, longitud, sector y arco.",
            listOf(SIn("r", "Radio r", "6"), SIn("θ", "Ángulo central θ (grados)", "60")),
            "A=π*r^2",
            listOf(
            SStep("Área del círculo: πr²", "pi*{r}^2", "pi*{r}^2"),
            SStep("Longitud: 2πr", "2*pi*{r}", "2*pi*{r}"),
            SStep("Área del sector: πr²θ/360", "pi*{r}^2*{θ}/360", "pi*{r}^2*{θ}/360"),
            SStep("Longitud del arco: πrθ/180", "pi*{r}*{θ}/180", "pi*{r}*{θ}/180")
            ), "", listOf("Círculo", "Sector circular")),
        Solver("solidos", "cilindro", "Cilindro", "Áreas y volumen.",
            listOf(SIn("r", "Radio r", "3"), SIn("h", "Altura h", "4")),
            "V=π*r^2*h",
            listOf(
            SStep("Área lateral: 2πrh", "2*pi*{r}*{h}", "2*pi*{r}*{h}"),
            SStep("Área total: 2πr(h + r)", "2*pi*{r}*({h}+{r})", "2*pi*{r}*({h}+{r})"),
            SStep("Volumen: πr²h", "pi*{r}^2*{h}", "pi*{r}^2*{h}")
            ), "", listOf("Cilindro")),
        Solver("solidos", "cono", "Cono", "Generatriz, áreas y volumen.",
            listOf(SIn("r", "Radio r", "3"), SIn("h", "Altura h", "4")),
            "V=π*r^2*h/3",
            listOf(
            SStep("Generatriz: g = √(r² + h²)", "sqrt({r}^2+{h}^2)", "sqrt({r}^2+{h}^2)", "g"),
            SStep("Área lateral: πrg", "pi*{r}*{g}", "pi*{r}*{g}"),
            SStep("Área total: πr(g + r)", "pi*{r}*({g}+{r})", "pi*{r}*({g}+{r})"),
            SStep("Volumen: πr²h/3", "pi*{r}^2*{h}/3", "pi*{r}^2*{h}/3")
            ), "", listOf("Cono")),
        Solver("solidos", "esfera", "Esfera", "Área y volumen.",
            listOf(SIn("r", "Radio r", "3")),
            "V=4/3*π*r^3",
            listOf(
            SStep("Área: 4πr²", "4*pi*{r}^2", "4*pi*{r}^2"),
            SStep("Volumen: 4πr³/3", "4*pi*{r}^3/3", "4*pi*{r}^3/3")
            ), "", listOf("Esfera")),
        Solver("solidos", "piramide", "Pirámide cuadrangular regular", "Apotema, áreas y volumen.",
            listOf(SIn("l", "Arista básica l", "6"), SIn("h", "Altura h", "4")),
            "V=l^2*h/3",
            listOf(
            SStep("Apotema: ap = √(h² + (l/2)²)", "sqrt({h}^2+({l}/2)^2)", "sqrt({h}^2+({l}/2)^2)", "ap"),
            SStep("Área lateral: 4 · l · ap / 2", "4*{l}*{ap}/2", "4*{l}*{ap}/2", "AL"),
            SStep("Área total: AL + l²", "{AL}+{l}^2", "{AL}+{l}^2"),
            SStep("Volumen: l² · h / 3", "{l}^2*{h}/3", "{l}^2*{h}/3")
            ), "", listOf("Pirámide")),
        Solver("solidos", "ortoedro", "Rectoedro (ortoedro)", "Área, volumen y diagonal.",
            listOf(SIn("a", "Arista a", "4"), SIn("b", "Arista b", "5"), SIn("c", "Arista c", "6")),
            "D=sqrt(a^2+b^2+c^2)",
            listOf(
            SStep("Área total: 2(ab + bc + ac)", "2*({a}*{b}+{b}*{c}+{a}*{c})", "2*({a}*{b}+{b}*{c}+{a}*{c})"),
            SStep("Volumen: abc", "{a}*{b}*{c}", "{a}*{b}*{c}"),
            SStep("Diagonal: √(a² + b² + c²)", "sqrt({a}^2+{b}^2+{c}^2)", "sqrt({a}^2+{b}^2+{c}^2)")
            ), "", listOf("Rectoedro (ortoedro)", "Cubo")),
        Solver("trigo", "rect1", "Triángulo rectángulo: hipotenusa y ángulo", "Calcula los catetos (ángulo en grados).",
            listOf(SIn("H", "Hipotenusa H", "10"), SIn("α", "Ángulo α (grados)", "30")),
            "CO=H*sin(α)",
            listOf(
            SStep("Cateto opuesto: CO = H · sen α", "{H}*sin({α})", "{H}*sin({α}*pi/180)"),
            SStep("Cateto adyacente: CA = H · cos α", "{H}*cos({α})", "{H}*cos({α}*pi/180)"),
            SStep("El otro ángulo agudo: 90 − α", "90-{α}", "90-{α}")
            ), "Los ángulos se toman en grados aunque la calculadora esté en RAD.", listOf("Razones trigonométricas")),
        Solver("trigo", "rect2", "Triángulo rectángulo: dos catetos", "Hipotenusa, razones y el ángulo.",
            listOf(SIn("a", "Cateto opuesto a α", "3"), SIn("b", "Cateto adyacente a α", "4")),
            "H=sqrt(a^2+b^2)",
            listOf(
            SStep("Hipotenusa: H = √(a² + b²)", "sqrt({a}^2+{b}^2)", "sqrt({a}^2+{b}^2)", "H"),
            SStep("sen α = CO/H", "{a}/{H}", "{a}/{H}"),
            SStep("cos α = CA/H", "{b}/{H}", "{b}/{H}"),
            SStep("tan α = CO/CA", "{a}/{b}", "{a}/{b}"),
            SStep("α en grados: tan⁻¹(CO/CA)", "atan({a}/{b})*180/pi", "atan({a}/{b})*180/pi")
            ), "Con catetos 3 y 4, α ≈ 36,87°: por eso se usa 37° como aproximado.", listOf("Razones trigonométricas", "37° y 53° (aproximado)")),
        Solver("trigo", "cosenos", "Ley de cosenos", "Tercer lado con dos lados y el ángulo entre ellos (grados).",
            listOf(SIn("b", "Lado b", "5"), SIn("c", "Lado c", "8"), SIn("A", "Ángulo A (grados)", "60")),
            "a^2=b^2+c^2-2*b*c*cos(A)",
            listOf(
            SStep("a² = b² + c² − 2bc · cos A", "{b}^2+{c}^2-2*{b}*{c}*cos({A})", "{b}^2+{c}^2-2*{b}*{c}*cos({A}*pi/180)", "q"),
            SStep("a = √(a²)", "sqrt({q})", "sqrt({q})")
            ), "", listOf("Ley de cosenos")),
        Solver("trigo", "senos", "Ley de senos", "Con un lado y dos ángulos (grados).",
            listOf(SIn("a", "Lado a", "10"), SIn("A", "Ángulo A (grados)", "30"), SIn("B", "Ángulo B (grados)", "45")),
            "a/sin(A)=b/sin(B)",
            listOf(
            SStep("Tercer ángulo: C = 180 − A − B", "180-{A}-{B}", "180-{A}-{B}", "C"),
            SStep("b = a · sen B / sen A", "{a}*sin({B})/sin({A})", "{a}*sin({B}*pi/180)/sin({A}*pi/180)"),
            SStep("c = a · sen C / sen A", "{a}*sin({C})/sin({A})", "{a}*sin({C}*pi/180)/sin({A}*pi/180)")
            ), "", listOf("Ley de senos")),
        Solver("trigo", "sistemas", "Sistemas de medida angular", "De sexagesimal a radianes y centesimal.",
            listOf(SIn("S", "Ángulo S (grados)", "60")),
            "T:S/180 = C/200 = R/π",
            listOf(
            SStep("Radianes: R = S · π / 180", "{S}*pi/180", "{S}*pi/180"),
            SStep("Centesimal: C = S · 200 / 180", "{S}*200/180", "{S}*200/180")
            ), "", listOf("Sistemas de medida angular")),
        Solver("transf", "punto", "Transformar un punto", "Traslación, reflexiones y rotación de P(x, y).",
            listOf(SIn("x", "x", "3"), SIn("y", "y", "1"), SIn("a", "Traslación en x (a)", "2"), SIn("b", "Traslación en y (b)", "-4"), SIn("θ", "Rotación θ (grados)", "90")),
            "P'=[x+a,y+b]",
            listOf(
            SStep("Traslación: (x + a, y + b)", "[{x}+{a},{y}+{b}]", "[{x}+{a},{y}+{b}]"),
            SStep("Reflexión en el eje x: (x, −y)", "[{x},-{y}]", "[{x},-{y}]"),
            SStep("Reflexión en el eje y: (−x, y)", "[-{x},{y}]", "[-{x},{y}]"),
            SStep("Reflexión en el origen: (−x, −y)", "[-{x},-{y}]", "[-{x},-{y}]"),
            SStep("Reflexión en y = x: (y, x)", "[{y},{x}]", "[{y},{x}]"),
            SStep("Rotación θ: (x cos θ − y sen θ, x sen θ + y cos θ)", "[{x}*cos({θ})-{y}*sin({θ}),{x}*sin({θ})+{y}*cos({θ})]", "[{x}*cos({θ}*pi/180)-{y}*sin({θ}*pi/180),{x}*sin({θ}*pi/180)+{y}*cos({θ}*pi/180)]")
            ), "La rotación es antihoraria alrededor del origen.", listOf("Traslación", "Reflexión en el eje x", "Rotación de 90°", "Rotación de ángulo θ")),
        Solver("prob", "laplace", "Regla de Laplace", "Casos favorables entre casos totales.",
            listOf(SIn("f", "Casos favorables", "6"), SIn("t", "Casos totales", "36")),
            "P=f/t",
            listOf(
            SStep("P = f / t", "{f}/{t}", "{f}/{t}", "P"),
            SStep("En porcentaje: P × 100", "{P}*100", "{P}*100"),
            SStep("Que no ocurra: 1 − P", "1-{P}", "1-{P}")
            ), "", listOf("Regla de Laplace", "Complemento")),
        Solver("prob", "binomial", "Distribución binomial", "Probabilidad de exactamente k éxitos en n intentos.",
            listOf(SIn("n", "Intentos n", "4"), SIn("k", "Éxitos k", "3"), SIn("p", "Probabilidad de éxito p", "1/2")),
            "P=C(n,k)*p^k*(1-p)^(n-k)",
            listOf(
            SStep("Combinaciones C(n, k)", "comb({n},{k})", "comb({n},{k})", "C"),
            SStep("pᵏ", "{p}^{k}", "{p}^{k}", "u"),
            SStep("(1 − p)ⁿ⁻ᵏ", "(1-{p})^({n}-{k})", "(1-{p})^({n}-{k})", "v"),
            SStep("P = C · pᵏ · (1 − p)ⁿ⁻ᵏ", "{C}*{u}*{v}", "{C}*{u}*{v}")
            ), "", listOf("Distribución binomial")),
        Solver("prob", "conteo", "Factorial, permutaciones y combinaciones", "Contar ordenando (P) o eligiendo sin orden (C).",
            listOf(SIn("n", "n", "10"), SIn("r", "r", "3")),
            "C=n!/(r!*(n-r)!)",
            listOf(
            SStep("n!", "{n}!", "{n}!"),
            SStep("Variaciones: P(n, r) = n!/(n − r)!", "perm({n},{r})", "perm({n},{r})"),
            SStep("Combinaciones: C(n, r) = n!/(r!(n − r)!)", "comb({n},{r})", "comb({n},{r})")
            ), "", listOf("Permutaciones", "Variaciones", "Combinaciones")),
        Solver("prob", "bayes", "Probabilidad total y Bayes", "Con P(A), P(B | A) y P(B | no A).",
            listOf(SIn("pa", "P(A)", "2/5"), SIn("pb", "P(B | A)", "3/10"), SIn("pn", "P(B | no A)", "2/5")),
            "T:P(A | B) = P(A) · P(B | A) / P(B)",
            listOf(
            SStep("P(no A) = 1 − P(A)", "1-{pa}", "1-{pa}", "q"),
            SStep("Probabilidad total: P(B) = P(A)P(B|A) + P(no A)P(B|no A)", "{pa}*{pb}+{q}*{pn}", "{pa}*{pb}+{q}*{pn}", "B"),
            SStep("Bayes: P(A|B) = P(A)P(B|A) / P(B)", "{pa}*{pb}/{B}", "{pa}*{pb}/{B}")
            ), "Ejemplo: A = ser mujer (40%), B = ser becado.", listOf("Probabilidad condicional")),
        Solver("estad", "sueltos", "Datos sueltos", "Media, mediana, moda, rango, varianza y desviación de una lista.",
            listOf(SIn("datos", "Datos (separados por comas; decimales con punto)", "12, 10, 12, 14, 12, 18", list = true)),
            "T:x̄ = S / n", emptyList(), "σ es la desviación poblacional (divide entre n); s es la muestral (divide entre n − 1).", listOf("Media", "Mediana", "Moda", "Varianza y desviación"), special = 1),
        Solver("estad", "tabla", "Tabla de frecuencias (datos agrupados)", "Arma la tabla con intervalos de igual ancho y calcula media, mediana y moda.",
            listOf(SIn("L", "Límite inferior del primer intervalo", "10", list = true), SIn("w", "Ancho de clase", "10", list = true), SIn("f", "Frecuencias fᵢ (separadas por comas)", "5, 8, 7", list = true)),
            "T:Me = Li + w · (n/2 − F) / fᵢ", emptyList(), "Intervalos cerrados a la izquierda y abiertos a la derecha: [Li; Ls⟩.", listOf("Marca de clase", "Frecuencias", "Media de datos agrupados", "Mediana de datos agrupados", "Moda de datos agrupados", "Rango y ancho de clase"), special = 2)
    )
}
