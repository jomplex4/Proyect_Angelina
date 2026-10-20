package com.digitalminds.angelina

class FSec(val id: String, val name: String)

/**
 * Una fórmula del formulario. Si formula empieza con "T:" se muestra como texto (con subíndices reales); si no, dibujada.
 * vars es la leyenda: cada "letra = significado" va separado por ";".
 */
class Formula(val sec: String, val title: String, val formula: String, val vars: String, val fig: String, val exText: String, val ex: HEx?, val note: String) {
    val isText get() = formula.startsWith("T:")
    val shown get() = formula.removePrefix("T:")
    val legend get() = vars.split(";").map { it.trim() }.filter { it.isNotEmpty() }
    val src get() = Formulas.sources[sec] ?: ""
}

/** Formulario offline. Cada ejemplo fue calculado con Giac 1.9 y FormulaTest lo vuelve a comprobar. */
object Formulas {
    val secs = listOf(FSec("arit", "Aritmética"), FSec("alg", "Álgebra"), FSec("plana", "Geometría plana"), FSec("lineas", "Líneas y puntos notables"), FSec("inscritas", "Inscritas y circunscritas"), FSec("solidos", "Sólidos y poliedros"), FSec("trigo", "Trigonometría"), FSec("notables", "Triángulos notables"), FSec("transf", "Transformaciones"), FSec("estad", "Estadística"), FSec("prob", "Probabilidad"))

    val sources = mapOf(
        "arit" to "Aritmética escolar estándar (ver Baldor, Aritmética).",
        "alg" to "Álgebra estándar (ver Baldor, Álgebra).",
        "plana" to "Geometría euclidiana (ver Baldor, Geometría plana y del espacio).",
        "lineas" to "Puntos notables del triángulo (ver Baldor, Geometría plana y del espacio).",
        "inscritas" to "Polígonos y circunferencia (ver Baldor, Geometría plana y del espacio).",
        "solidos" to "Geometría del espacio (ver Baldor, Geometría plana y del espacio).",
        "trigo" to "Trigonometría plana (ver Baldor, Geometría y Trigonometría).",
        "notables" to "Triángulos notables de preuniversitaria; los valores exactos los calcula Giac.",
        "transf" to "Transformaciones en el plano cartesiano (Currículo Nacional, MINEDU, secundaria).",
        "estad" to "Estadística descriptiva (ver Córdova Zamora, Estadística descriptiva e inferencial).",
        "prob" to "Probabilidad clásica (ver Walpole y Myers, Probabilidad y estadística para ingeniería y ciencias)."
    )

    val all: List<Formula> = ArrayList<Formula>().also { l -> part0(l); part1(l); part2(l); part3(l); part4(l) }

    fun search(q: String, sec: String?): List<Formula> {
        val words = Guide.norm(q).split(' ').filter { it.isNotBlank() }
        return all.filter { f -> (sec == null || words.isNotEmpty() || f.sec == sec) &&
            words.all { w -> Guide.norm(f.title + " " + f.vars + " " + f.note + " " + f.exText + " " + (secs.firstOrNull { it.id == f.sec }?.name ?: "")).contains(w) } }
    }

    private fun part0(l: MutableList<Formula>) {
        l.add(Formula("arit", "Tanto por ciento", "P=a/100*N", "P = parte que se busca; a = porcentaje (el número antes de %); N = cantidad total", "pct", "El 20% de 300.", HEx("20/100*300", "60"), "El a% del b% de N es a/100 · b/100 · N."))
        l.add(Formula("arit", "Aumentos sucesivos", "F=(1+a/100)*(1+b/100)", "F = factor total (multiplica al precio); a = primer aumento en %; b = segundo aumento en %", "", "Aumentos de 5% y 10%.", HEx("(1+5/100)*(1+10/100)", "231/200"), "231/200 = 1,155: equivale a un aumento único de 15,5%."))
        l.add(Formula("arit", "Descuentos sucesivos", "T:Descuento único = 100 − (100 − a)(100 − b)/100", "a = primer descuento en %; b = segundo descuento en %", "", "Descuentos de 20% y 40%.", HEx("100-(100-20)*(100-40)/100", "52"), "No se suman: 20% + 40% no es 60%."))
        l.add(Formula("arit", "Interés simple", "I=C*r*t/100", "I = interés ganado; C = capital (dinero inicial); r = tasa en %; t = tiempo (en la misma unidad que la tasa: años con tasa anual, meses con tasa mensual)", "", "S/ 3000 al 30% anual por 3 años.", HEx("3000*30*3/100", "2700"), "Si la tasa es mensual, el tiempo va en meses."))
        l.add(Formula("arit", "Monto", "M=C+I", "M = monto final (lo que se devuelve); C = capital; I = interés", "", "S/ 7200 al 4% anual por 3 años y 4 meses (10/3 años).", HEx("7200+7200*4*(10/3)/100", "8160"), ""))
        l.add(Formula("arit", "Regla de tres simple directa", "T:a → b   y   c → x   ⇒   x = b · c / a", "a y b = primer par de valores que se corresponden; c = nuevo valor; x = lo que corresponde a c", "", "3 cuadernos cuestan S/ 12; ¿cuánto cuestan 5?", HEx("12*5/3", "20"), ""))
        l.add(Formula("arit", "Regla de tres simple inversa", "T:a → b   y   c → x   ⇒   x = a · b / c", "a y b = primer par de valores; c = nuevo valor; x = lo que corresponde a c (si una crece, la otra baja)", "", "6 obreros tardan 10 días; ¿cuánto tardan 4?", HEx("6*10/4", "15"), ""))
        l.add(Formula("arit", "Promedio aritmético", "T:MA = (suma de los datos) / n", "MA = promedio aritmético; n = cantidad de datos", "", "Promedio de 12, 15 y 18.", HEx("(12+15+18)/3", "15"), ""))
        l.add(Formula("arit", "Promedio geométrico", "MG=sqrt(a*b)", "MG = promedio geométrico; a y b = los dos números", "", "MG de 4 y 9.", HEx("sqrt(4*9)", "6"), "Para n números: raíz enésima del producto."))
        l.add(Formula("arit", "Promedio armónico", "MH=2*a*b/(a+b)", "MH = promedio armónico; a y b = los dos números", "", "MH de 4 y 12.", HEx("2*4*12/(4+12)", "6"), "Siempre MA ≥ MG ≥ MH."))
        l.add(Formula("arit", "MCD y MCM", "a*b=MCD*MCM", "a y b = los dos números; MCD = máximo común divisor; MCM = mínimo común múltiplo", "", "Con 12 y 18: MCD = 6 y MCM = 36.", HEx("gcd(12,18)*lcm(12,18)", "216"), "Da 216 = 12 · 18."))
        l.add(Formula("arit", "Cantidad de divisores", "T:Si N = aᵐ · bⁿ · cᵖ, entonces CD = (m + 1)(n + 1)(p + 1)", "N = el número; a, b, c = sus factores primos; m, n, p = sus exponentes; CD = cantidad de divisores", "", "360 = 2³ · 3² · 5.", HEx("(3+1)*(2+1)*(1+1)", "24"), ""))
        l.add(Formula("arit", "Fracción generatriz", "T:0,ababab... = ab/99", "ab = las dos cifras que se repiten; el denominador lleva un 9 por cada cifra del periodo", "", "0,363636... = 36/99.", HEx("36/99", "4/11"), "Periódico mixto: (número completo − parte no periódica) / (9 y 0)."))
        l.add(Formula("arit", "Producto de potencias", "a^m*a^n=a^(m+n)", "a = base; m y n = exponentes", "", "2³ · 2⁴.", HEx("2^3*2^4", "128"), ""))
        l.add(Formula("arit", "Cociente de potencias", "a^m/a^n=a^(m-n)", "a = base (distinta de 0); m y n = exponentes", "", "3⁵ / 3².", HEx("3^5/3^2", "27"), ""))
        l.add(Formula("arit", "Potencia de potencia", "(a^m)^n=a^(m*n)", "a = base; m y n = exponentes", "", "(2³)².", HEx("(2^3)^2", "64"), ""))
        l.add(Formula("arit", "Exponente negativo", "a^(-n)=1/a^n", "a = base (distinta de 0); n = exponente", "", "2⁻³.", HEx("2^(-3)", "1/8"), ""))
        l.add(Formula("arit", "Exponente fraccionario", "T:a^(m/n) = ⁿ√(aᵐ)", "a = base; m = exponente de la base; n = índice de la raíz", "", "8^(2/3).", HEx("simplify(8^(2/3))", "4"), ""))
        l.add(Formula("arit", "Raíz de un producto", "sqrt(a*b)=sqrt(a)*sqrt(b)", "a y b = números no negativos", "", "√50 = √(25 · 2).", HEx("sqrt(50)", "5*sqrt(2)"), ""))
        l.add(Formula("arit", "Racionalizar", "1/sqrt(a)=sqrt(a)/a", "a = número positivo (se multiplica arriba y abajo por √a)", "", "1/√2.", HEx("simplify(1/sqrt(2))", "sqrt(2)/2"), ""))
        l.add(Formula("arit", "Propiedad distributiva", "a*(b+c)=a*b+a*c", "a, b, c = números cualesquiera", "", "3 · (4 + 5) = 3 · 4 + 3 · 5.", HEx("3*4+3*5", "27"), "Ley de signos: (+)(+) = +, (−)(−) = +, (+)(−) = −."))
        l.add(Formula("arit", "Logaritmo de un producto", "T:log_b(a · c) = log_b(a) + log_b(c)", "b = base del logaritmo; a y c = números positivos", "", "log₂(8 · 4) = 3 + 2.", HEx("logb(8*4,2)", "5"), "De un cociente: resta. De una potencia: logb(aⁿ) = n · logb(a)."))
        l.add(Formula("arit", "Cambio de base", "T:log_b(a) = ln(a) / ln(b)", "b = base que quieres; a = número positivo; ln = logaritmo natural (también sirve log)", "", "log₃ 81.", HEx("logb(81,3)", "4"), ""))
        l.add(Formula("arit", "Proporción", "a/b=c/d", "a, b, c, d = términos de la proporción (b y d distintos de 0)", "", "3/4 = x/20.", HEx("solve(3/4=x/20,x)", "list[15]"), ""))
        l.add(Formula("alg", "Binomio al cuadrado", "(a+b)^2=a^2+2*a*b+b^2", "a y b = los dos términos del binomio", "", "(x + 3)².", HEx("expand((x+3)^2)", "x^2+6*x+9"), ""))
        l.add(Formula("alg", "Diferencia de cuadrados", "(a+b)*(a-b)=a^2-b^2", "a y b = los dos términos", "", "103 · 97 = (100 + 3)(100 − 3).", HEx("100^2-3^2", "9991"), ""))
        l.add(Formula("alg", "Binomio al cubo", "(a+b)^3=a^3+3*a^2*b+3*a*b^2+b^3", "a y b = los dos términos del binomio", "", "(x + 1)³.", HEx("expand((x+1)^3)", "x^3+3*x^2+3*x+1"), ""))
        l.add(Formula("alg", "Suma de cubos", "a^3+b^3=(a+b)*(a^2-a*b+b^2)", "a y b = los dos términos", "", "x³ + 8.", HEx("factor(x^3+8)", "(x+2)*(x^2-2*x+4)"), ""))
        l.add(Formula("alg", "Identidad de Legendre", "(a+b)^2+(a-b)^2=2*(a^2+b^2)", "a y b = números cualesquiera", "", "Con a = 5 y b = 3.", HEx("(5+3)^2+(5-3)^2", "68"), ""))
        l.add(Formula("alg", "Suma de cuadrados conocida a + b y ab", "a^2+b^2=(a+b)^2-2*a*b", "a + b = la suma conocida; a·b = el producto conocido", "", "a + b = 5 y ab = 3.", HEx("5^2-2*3", "19"), ""))
    }
    private fun part1(l: MutableList<Formula>) {
        l.add(Formula("alg", "Si a + b + c = 0", "a^3+b^3+c^3=3*a*b*c", "a, b, c = tres números cuya suma es cero", "", "a = 1, b = 2, c = −3.", HEx("1^3+2^3+(-3)^3", "-18"), "Da −18 = 3 · 1 · 2 · (−3)."))
        l.add(Formula("alg", "Ecuación de primer grado", "T:a·x + b = 0   ⇒   x = −b / a", "a = coeficiente de x (distinto de 0); b = término independiente; x = incógnita", "", "3x + 6 = 0.", HEx("solve(3*x+6=0,x)", "list[-2]"), ""))
        l.add(Formula("alg", "Fórmula general (2.º grado)", "T:Para a·x² + b·x + c = 0:   x = (−b ± √(b² − 4ac)) / (2a)", "a = número que acompaña a x²; b = número que acompaña a x; c = término independiente; ± = una raíz con + y otra con −", "", "2x² − 3x − 2 = 0.", HEx("solve(2*x^2-3*x-2=0,x)", "list[-1/2,2]"), ""))
        l.add(Formula("alg", "Discriminante", "T:Δ = b² − 4ac", "Δ = discriminante; a, b, c = coeficientes de a·x² + b·x + c = 0", "", "x² + 5x + 6.", HEx("5^2-4*1*6", "1"), ""))
        l.add(Formula("alg", "Suma y producto de raíces", "T:x₁ + x₂ = −b/a      x₁ · x₂ = c/a", "x₁ y x₂ = las dos raíces; a, b, c = coeficientes de a·x² + b·x + c = 0", "", "En x² − 7x + 5 = 0, la suma es 7 y el producto 5.", HEx("solve(x^2-7*x+5=0,x)", "list[1/2*(-sqrt(29)+7),1/2*(sqrt(29)+7)]"), "(r + 1)(s + 1) = producto + suma + 1 = 5 + 7 + 1 = 13."))
        l.add(Formula("alg", "Sistema 2 × 2 (Cramer)", "T:Para a·x + b·y = c  y  d·x + e·y = f:   x = (c·e − b·f)/(a·e − b·d),   y = (a·f − c·d)/(a·e − b·d)", "a, b, d, e = coeficientes de x e y; c y f = términos independientes", "", "4x + 3y = 8 y x − 2y = 2.", HEx("linsolve([4*x+3*y=8,x-2*y=2],[x,y])", "[2,0]"), ""))
        l.add(Formula("alg", "Inecuación cuadrática", "T:(x − r₁)(x − r₂) ≥ 0  ⇒  x ≤ r₁  o  x ≥ r₂      (x − r₁)(x − r₂) ≤ 0  ⇒  r₁ ≤ x ≤ r₂", "r₁ y r₂ = raíces ordenadas (r₁ < r₂); con ≥ la solución va hacia afuera de las raíces, con ≤ entre ellas", "", "x² − 4 ≥ 0.", HEx("solve(x^2-4>=0,x)", "list[x<=-2,x>=2]"), ""))
        l.add(Formula("alg", "Ecuación con valor absoluto", "T:|x| = a   ⇒   x = a  o  x = −a   (a ≥ 0)", "x = incógnita; a = número no negativo", "", "|x − 2| = 3.", HEx("solve(abs(x-2)=3,x)", "list[-1,5]"), ""))
        l.add(Formula("alg", "Pendiente de una recta", "T:m = (y₂ − y₁) / (x₂ − x₁)", "m = pendiente; (x₁, y₁) y (x₂, y₂) = dos puntos de la recta", "linea", "Entre (2, 3) y (4, 7).", HEx("(7-3)/(4-2)", "2"), ""))
        l.add(Formula("alg", "Vértice de la parábola", "T:Para y = a·x² + b·x + c:   h = −b/(2a)   y   k = valor de y en x = h", "(h, k) = vértice; a, b, c = coeficientes", "parabola", "y = x² − 4x + 3.", HEx("-(-4)/(2*1)", "2"), ""))
        l.add(Formula("alg", "Distancia entre dos puntos", "T:d = √((x₂ − x₁)² + (y₂ − y₁)²)", "d = distancia; (x₁, y₁) y (x₂, y₂) = los dos puntos", "linea", "Entre (1, 2) y (4, 6).", HEx("sqrt((4-1)^2+(6-2)^2)", "5"), ""))
        l.add(Formula("alg", "Punto medio", "T:M = ((x₁ + x₂)/2 ; (y₁ + y₂)/2)", "M = punto medio; (x₁, y₁) y (x₂, y₂) = extremos del segmento", "linea", "Entre (1, 2) y (5, 8).", HEx("[(1+5)/2,(2+8)/2]", "[3,5]"), ""))
        l.add(Formula("alg", "Suma de los n primeros naturales", "S=n*(n+1)/2", "S = suma 1 + 2 + ... + n; n = último número", "", "De 1 a 100.", HEx("100*(100+1)/2", "5050"), ""))
        l.add(Formula("alg", "Suma de cuadrados", "S=n*(n+1)*(2*n+1)/6", "S = suma 1² + 2² + ... + n²; n = último número", "", "Hasta 10.", HEx("10*11*21/6", "385"), ""))
        l.add(Formula("alg", "Progresión aritmética", "T:aₙ = a₁ + (n − 1)·r", "aₙ = término de lugar n; a₁ = primer término; n = lugar; r = razón (lo que se suma cada vez)", "", "a₁ = 3, r = 4, término 10.", HEx("3+(10-1)*4", "39"), "Suma: S = (a₁ + aₙ) · n / 2."))
        l.add(Formula("alg", "Progresión geométrica", "T:aₙ = a₁ · qⁿ⁻¹", "aₙ = término de lugar n; a₁ = primer término; n = lugar; q = razón (lo que se multiplica)", "", "a₁ = 2, q = 3, término 5.", HEx("2*3^(5-1)", "162"), ""))
        l.add(Formula("plana", "Área del triángulo", "A=b*h/2", "A = área; b = base; h = altura (perpendicular a esa base)", "tri", "Base 10 y altura 6.", HEx("10*6/2", "30"), ""))
        l.add(Formula("plana", "Fórmula de Herón", "A=sqrt(p*(p-a)*(p-b)*(p-c))", "A = área; a, b, c = los tres lados; p = semiperímetro = (a + b + c)/2", "tri3", "Lados 13, 14 y 15 (p = 21).", HEx("sqrt(21*(21-13)*(21-14)*(21-15))", "84"), ""))
        l.add(Formula("plana", "Ángulos del triángulo", "A+B+C=180", "A, B, C = los tres ángulos interiores (en grados)", "tri3", "Si A = 50° y B = 70°.", HEx("180-(50+70)", "60"), ""))
        l.add(Formula("plana", "Teorema de Pitágoras", "c^2=a^2+b^2", "c = hipotenusa (lado opuesto al ángulo recto); a y b = catetos", "rect", "Catetos 3 y 4.", HEx("sqrt(3^2+4^2)", "5"), "Ternas: 3-4-5, 5-12-13, 8-15-17, 7-24-25."))
        l.add(Formula("plana", "Altura sobre la hipotenusa", "h^2=m*n", "h = altura trazada desde el ángulo recto; m y n = las dos partes en que la altura divide a la hipotenusa", "recth", "m = 4 y n = 9.", HEx("sqrt(4*9)", "6"), ""))
        l.add(Formula("plana", "Cateto y su proyección", "a^2=c*m", "a = un cateto; c = hipotenusa; m = proyección de ese cateto sobre la hipotenusa", "recth", "Hipotenusa 13 y proyección 4.", HEx("sqrt(13*4)", "2*sqrt(13)"), ""))
        l.add(Formula("plana", "Catetos por altura", "a*b=c*h", "a y b = catetos; c = hipotenusa; h = altura relativa a la hipotenusa", "recth", "Catetos 3 y 4, hipotenusa 5.", HEx("3*4/5", "12/5"), ""))
        l.add(Formula("plana", "Cuadrado", "A=l^2", "A = área; l = lado; d = diagonal = l√2", "cuadrado", "Lado 6: diagonal.", HEx("6*sqrt(2)", "6*sqrt(2)"), ""))
        l.add(Formula("plana", "Rectángulo", "A=b*h", "A = área; b = base; h = altura", "rectangulo", "Base 8 y altura 5.", HEx("8*5", "40"), ""))
        l.add(Formula("plana", "Rombo", "T:A = (D · d) / 2", "A = área; D = diagonal mayor; d = diagonal menor", "rombo", "Diagonales 10 y 6.", HEx("10*6/2", "30"), ""))
        l.add(Formula("plana", "Paralelogramo", "A=b*h", "A = área; b = base; h = altura (no el lado inclinado)", "paralelo", "Base 9 y altura 4.", HEx("9*4", "36"), ""))
        l.add(Formula("plana", "Trapecio", "A=(B+b)/2*h", "A = área; B = base mayor; b = base menor; h = altura", "trapecio", "B = 12, b = 6, h = 5.", HEx("(12+6)/2*5", "45"), ""))
        l.add(Formula("plana", "Círculo", "A=π*r^2", "A = área; r = radio; π ≈ 3,1416", "circulo", "Radio 5.", HEx("simplify(pi*5^2)", "25*pi"), ""))
        l.add(Formula("plana", "Sector circular", "T:A = π · r² · θ / 360", "A = área del sector; r = radio; θ = ángulo central en grados", "sector", "Radio 6 y θ = 60°.", HEx("simplify(pi*6^2*60/360)", "6*pi"), "Longitud del arco: L = πrθ/180."))
    }
    private fun part2(l: MutableList<Formula>) {
        l.add(Formula("plana", "Ángulos de un polígono", "S=180*(n-2)", "S = suma de ángulos interiores; n = número de lados", "hexagono", "Hexágono.", HEx("180*(6-2)", "720"), ""))
        l.add(Formula("plana", "Diagonales de un polígono", "D=n*(n-3)/2", "D = número de diagonales; n = número de lados", "hexagono", "Hexágono.", HEx("6*(6-3)/2", "9"), ""))
        l.add(Formula("plana", "Hexágono regular", "A=3*sqrt(3)*l^2/2", "A = área; l = lado", "hexagono", "Lado 2.", HEx("simplify(3*sqrt(3)*2^2/2)", "6*sqrt(3)"), ""))
        l.add(Formula("plana", "Triángulo equilátero", "A=l^2*sqrt(3)/4", "A = área; l = lado", "equilatero", "Lado 4.", HEx("simplify(4^2*sqrt(3)/4)", "4*sqrt(3)"), ""))
        l.add(Formula("plana", "Ángulo inscrito", "T:Ángulo inscrito = arco / 2", "El ángulo inscrito tiene su vértice en la circunferencia; arco = medida del arco que abarca", "inscrito", "Arco de 100°.", HEx("100/2", "50"), ""))
        l.add(Formula("plana", "Ángulo interior entre cuerdas", "T:x = (a + b) / 2", "x = ángulo donde se cortan dos cuerdas; a y b = los dos arcos opuestos que abarcan", "cuerdas", "Arcos de 80° y 40°.", HEx("(80+40)/2", "60"), "Ángulo exterior: x = (a − b)/2."))
        l.add(Formula("plana", "Teorema de las cuerdas", "a*b=c*d", "a y b = partes de una cuerda; c y d = partes de la otra cuerda (cortadas en el mismo punto)", "cuerdas", "3 · 8 = 4 · x.", HEx("solve(3*8=4*x,x)", "list[6]"), ""))
        l.add(Formula("plana", "Tangente y secante", "t^2=a*(a+b)", "t = segmento tangente; a = parte externa de la secante; a + b = secante completa", "tangente", "a = 4 y a + b = 9.", HEx("sqrt(4*9)", "6"), ""))
        l.add(Formula("plana", "Teorema de Thales", "a/b=c/d", "a y b = segmentos en una transversal; c y d = segmentos correspondientes en la otra (entre las mismas paralelas)", "thales", "a = 3, b = 6, c = 4.", HEx("solve(3/6=4/x,x)", "list[8]"), ""))
        l.add(Formula("lineas", "Baricentro (medianas)", "AG=2*GM", "G = baricentro; A = vértice; M = punto medio del lado opuesto; AG es el doble de GM", "mediana", "Mediana de 9.", HEx("2*9/3", "6"), ""))
        l.add(Formula("lineas", "Incentro (bisectrices)", "T:Ángulo AIC = 90° + B/2", "I = incentro (cruce de bisectrices); B = ángulo del vértice opuesto", "bisectriz", "B = 80°.", HEx("90+80/2", "130"), ""))
        l.add(Formula("lineas", "Circuncentro (mediatrices)", "T:Ángulo AOC = 2 · B", "O = circuncentro (cruce de mediatrices); B = ángulo opuesto al lado AC (triángulo acutángulo)", "mediatriz", "B = 50°.", HEx("2*50", "100"), "En un triángulo rectángulo está en el punto medio de la hipotenusa."))
        l.add(Formula("lineas", "Ortocentro (alturas)", "T:Ángulo AHC = 180° − B", "H = ortocentro (cruce de alturas); B = ángulo del vértice opuesto (triángulo acutángulo)", "altura", "B = 70°.", HEx("180-70", "110"), ""))
        l.add(Formula("lineas", "Excentro", "T:Ángulo AEC = 90° − B/2", "E = excentro relativo a AC; B = ángulo del vértice opuesto", "excentro", "B = 80°.", HEx("90-80/2", "50"), ""))
        l.add(Formula("lineas", "Baricentro y circuncentro en un rectángulo", "T:OG = c / 6", "O = circuncentro (punto medio de la hipotenusa); G = baricentro; c = hipotenusa", "mediana", "Hipotenusa 30.", HEx("30/6", "5"), ""))
        l.add(Formula("lineas", "Teorema de la bisectriz", "T:AB/BC = AD/DC", "BD = bisectriz del ángulo B; AD y DC = partes del lado AC; AB y BC = los otros lados", "bisectriz", "AB = 6, BC = 9, AC = 10: AD = 6·10/15.", HEx("6*10/(6+9)", "4"), ""))
        l.add(Formula("inscritas", "Radio inscrito en un triángulo", "r=A/p", "r = radio de la circunferencia inscrita; A = área; p = semiperímetro", "incirculo", "Triángulo 3-4-5 (A = 6, p = 6).", HEx("6/6", "1"), ""))
        l.add(Formula("inscritas", "Radio circunscrito", "R=a*b*c/(4*A)", "R = radio de la circunferencia circunscrita; a, b, c = lados; A = área", "circuncirculo", "Triángulo 3-4-5.", HEx("3*4*5/(4*6)", "5/2"), "En el rectángulo R = hipotenusa/2."))
        l.add(Formula("inscritas", "Teorema de Poncelet", "a+b=c+2*r", "a y b = catetos; c = hipotenusa; r = radio inscrito", "incirculo", "Catetos 3 y 4, hipotenusa 5.", HEx("solve(3+4=5+2*r,r)", "list[1]"), ""))
        l.add(Formula("inscritas", "Teorema de Pitot", "T:AB + CD = BC + AD", "AB, BC, CD, AD = lados de un cuadrilátero con una circunferencia inscrita", "pitot", "AB = 5, CD = 7, BC = 4.", HEx("solve(5+7=4+x,x)", "list[8]"), ""))
        l.add(Formula("inscritas", "Cuadrado inscrito en circunferencia", "l=R*sqrt(2)", "l = lado del cuadrado; R = radio de la circunferencia", "cuadinscrito", "R = 5.", HEx("5*sqrt(2)", "5*sqrt(2)"), ""))
        l.add(Formula("inscritas", "Circunferencia inscrita en cuadrado", "r=l/2", "r = radio de la circunferencia; l = lado del cuadrado", "circinscrita", "Lado 8.", HEx("8/2", "4"), ""))
        l.add(Formula("inscritas", "Hexágono inscrito", "l=R", "l = lado del hexágono regular; R = radio de la circunferencia", "hexinscrito", "R = 7.", HEx("7", "7"), ""))
        l.add(Formula("inscritas", "Triángulo equilátero inscrito", "l=R*sqrt(3)", "l = lado del triángulo; R = radio de la circunferencia", "triinscrito", "R = 2.", HEx("2*sqrt(3)", "2*sqrt(3)"), ""))
        l.add(Formula("solidos", "Cubo", "V=a^3", "V = volumen; a = arista; diagonal = a√3; área total = 6a²", "cubo", "Arista 3: volumen y diagonal.", HEx("[3^3,3*sqrt(3)]", "[27,3*sqrt(3)]"), ""))
        l.add(Formula("solidos", "Rectoedro (ortoedro)", "D=sqrt(a^2+b^2+c^2)", "D = diagonal; a, b, c = largo, ancho y alto", "ortoedro", "Aristas 4, 5 y 6.", HEx("sqrt(4^2+5^2+6^2)", "sqrt(77)"), ""))
        l.add(Formula("solidos", "Prisma recto", "T:V = Ab · h", "V = volumen; Ab = área de la base; h = altura", "prisma", "Base triángulo rectángulo 3-4 y altura 6.", HEx("3*4/2*6", "36"), ""))
        l.add(Formula("solidos", "Cilindro", "V=π*r^2*h", "V = volumen; r = radio de la base; h = altura", "cilindro", "r = 3 y h = 4.", HEx("simplify(pi*3^2*4)", "36*pi"), ""))
        l.add(Formula("solidos", "Pirámide", "T:V = Ab · h / 3", "V = volumen; Ab = área de la base; h = altura", "piramide", "Base cuadrada de lado 6 y altura 4.", HEx("6^2*4/3", "48"), ""))
        l.add(Formula("solidos", "Cono", "V=π*r^2*h/3", "V = volumen; r = radio de la base; h = altura; g = generatriz", "cono", "h = 6 y g = 7 (r² = 49 − 36 = 13).", HEx("simplify(pi*13*6/3)", "26*pi"), ""))
    }
    private fun part3(l: MutableList<Formula>) {
        l.add(Formula("solidos", "Esfera", "V=4/3*π*r^3", "V = volumen; r = radio", "esfera", "Radio 3.", HEx("simplify(4/3*pi*3^3)", "36*pi"), ""))
        l.add(Formula("solidos", "Tronco de cono", "V=π*h*(R^2+r^2+R*r)/3", "V = volumen; R = radio mayor; r = radio menor; h = altura", "tronco", "R = 4, r = 2, h = 3.", HEx("simplify(pi*3*(4^2+2^2+4*2)/3)", "28*pi"), ""))
        l.add(Formula("solidos", "Teorema de Euler (poliedros)", "C+V=A+2", "C = número de caras; V = número de vértices; A = número de aristas", "cubo", "Cubo: 6 caras, 8 vértices, 12 aristas.", HEx("6+8-12", "2"), "Poliedros regulares: tetraedro, hexaedro (cubo), octaedro, dodecaedro e icosaedro."))
        l.add(Formula("trigo", "Razones trigonométricas", "T:sen α = CO/H   cos α = CA/H   tan α = CO/CA   cot α = CA/CO   sec α = H/CA   csc α = H/CO", "α = ángulo agudo; CO = cateto opuesto a α; CA = cateto adyacente a α; H = hipotenusa", "razones", "sen 30° (en GRADOS).", HEx("sin(30)", "1/2", true), "Se leen de arriba hacia abajo: los numeradores son CO CA CO CA HIP HIP y los denominadores HIP HIP CA CO CA CO."))
        l.add(Formula("trigo", "Recíprocas", "sin(α)*csc(α)=1", "α = cualquier ángulo agudo", "razones", "sen 30° · csc 30°.", HEx("sin(30)*csc(30)", "1", true), ""))
        l.add(Formula("trigo", "Identidad pitagórica", "sin(α)^2+cos(α)^2=1", "α = cualquier ángulo", "razones", "Con α = 37°.", HEx("simplify(sin(37)^2+cos(37)^2)", "1", true), ""))
        l.add(Formula("trigo", "Ángulos complementarios", "sin(α)=cos(90-α)", "α = ángulo agudo; 90° − α = su complemento", "razones", "sen 20° − cos 70°.", HEx("simplify(sin(20)-cos(70))", "0", true), ""))
        l.add(Formula("trigo", "Ley de senos", "a/sin(A)=b/sin(B)", "a, b = lados; A, B = ángulos opuestos a esos lados", "tri3", "a = 10, A = 30°, B = 45°: b.", HEx("simplify(10*sin(45)/sin(30))", "10*sqrt(2)", true), ""))
        l.add(Formula("trigo", "Ley de cosenos", "a^2=b^2+c^2-2*b*c*cos(A)", "a = lado que se busca; b y c = los otros lados; A = ángulo entre b y c (opuesto a a)", "tri3", "b = 5, c = 8, A = 60°.", HEx("sqrt(5^2+8^2-2*5*8*cos(60))", "7", true), ""))
        l.add(Formula("trigo", "Área con dos lados y el ángulo", "A=a*b*sin(C)/2", "A = área; a y b = dos lados; C = ángulo entre ellos", "tri3", "a = 6, b = 8, C = 30°.", HEx("6*8*sin(30)/2", "12", true), ""))
        l.add(Formula("trigo", "Sistemas de medida angular", "T:S/180 = C/200 = R/π", "S = medida en grados sexagesimales; C = en grados centesimales; R = en radianes", "", "60° en radianes.", HEx("simplify(60*pi/180)", "pi/3"), "90° = 100ᵍ = π/2 rad."))
        l.add(Formula("trigo", "Ángulo doble", "sin(2*α)=2*sin(α)*cos(α)", "α = cualquier ángulo", "", "Comprobación con α = 30°.", HEx("simplify(sin(60)-2*sin(30)*cos(30))", "0", true), ""))
        l.add(Formula("notables", "45° y 45°", "T:Catetos k y k;   hipotenusa k√2", "k = constante de proporcionalidad (cualquier número positivo)", "n45", "Si la hipotenusa mide 10√2: k = 10, cada cateto mide 10.", HEx("simplify(10*sqrt(2)/sqrt(2))", "10"), ""))
        l.add(Formula("notables", "30° y 60°", "T:Frente a 30°: k;   frente a 60°: k√3;   hipotenusa 2k", "k = constante de proporcionalidad", "n30", "Si la hipotenusa mide 20: k = 10; catetos 10 (frente a 30°) y 10√3.", HEx("[20/2,20/2*sqrt(3)]", "[10,10*sqrt(3)]"), ""))
        l.add(Formula("notables", "37° y 53° (aproximado)", "T:Frente a 37°: 3k;   frente a 53°: 4k;   hipotenusa 5k", "k = constante de proporcionalidad (valores aproximados)", "n37", "Si la hipotenusa mide 20: k = 4; catetos 12 (frente a 37°) y 16.", HEx("[3*20/5,4*20/5]", "[12,16]"), "En exámenes se usa sen 37° ≈ 3/5 = 0,6; el valor real es 0,6018..."))
        l.add(Formula("notables", "15° y 75°", "T:Frente a 15°: (√6 − √2)k;   frente a 75°: (√6 + √2)k;   hipotenusa 4k", "k = constante de proporcionalidad", "n15", "Si la hipotenusa mide 8: k = 2; el cateto frente a 15° mide 2(√6 − √2).", HEx("2*(sqrt(6)-sqrt(2))", "2*(sqrt(6)-sqrt(2))"), ""))
        l.add(Formula("notables", "16° y 74° (aproximado)", "T:Frente a 16°: 7k;   frente a 74°: 24k;   hipotenusa 25k", "k = constante de proporcionalidad (valores aproximados)", "n16", "Si la hipotenusa mide 50: k = 2; catetos 14 y 48.", HEx("[7*2,24*2]", "[14,48]"), "7/25 = 0,28 es aproximado."))
        l.add(Formula("notables", "8° y 82° (aproximado)", "T:Frente a 8°: k;   frente a 82°: 7k;   hipotenusa 5√2 k", "k = constante de proporcionalidad (valores aproximados)", "n8", "Si el cateto menor mide 3: k = 3; la hipotenusa mide 15√2.", HEx("simplify(5*sqrt(2)*3)", "15*sqrt(2)"), "1/7 ≈ 0,1429 es aproximado."))
        l.add(Formula("notables", "14° y 76° (aproximado)", "T:Frente a 14°: k;   frente a 76°: 4k;   hipotenusa √17 k", "k = constante de proporcionalidad (valores aproximados)", "n14", "Si el cateto mayor mide 12: k = 3; la hipotenusa mide 3√17.", HEx("sqrt(17)*3", "3*sqrt(17)"), "1/4 = 0,25 es aproximado."))
        l.add(Formula("notables", "18,5° y 71,5° (aproximado)", "T:Frente a 18,5°: k;   frente a 71,5°: 3k;   hipotenusa √10 k", "k = constante de proporcionalidad (valores aproximados)", "n18", "Si el cateto menor mide 5: k = 5; la hipotenusa mide 5√10.", HEx("5*sqrt(10)", "5*sqrt(10)"), "1/3 ≈ 0,333 es aproximado."))
        l.add(Formula("notables", "26,5° y 63,5° (aproximado)", "T:Frente a 26,5°: k;   frente a 63,5°: 2k;   hipotenusa √5 k", "k = constante de proporcionalidad (valores aproximados)", "n26", "Si el cateto mayor mide 8: k = 4; la hipotenusa mide 4√5.", HEx("4*sqrt(5)", "4*sqrt(5)"), "1/2 = 0,5 es aproximado."))
        l.add(Formula("transf", "Traslación", "T:P(x, y) → P'(x + a, y + b)", "P = punto original; P' = punto trasladado; a = movimiento horizontal; b = movimiento vertical", "traslacion", "P(2, 1) con vector (3, −4).", HEx("[2+3,1-4]", "[5,-3]"), "La figura no cambia de forma ni de tamaño."))
        l.add(Formula("transf", "Reflexión en el eje x", "T:P(x, y) → P'(x, −y)", "P = punto original; P' = punto reflejado", "reflexion", "P(3, 2).", HEx("[3,-2]", "[3,-2]"), "En el eje y: (−x, y). En el origen: (−x, −y). En la recta y = x: (y, x)."))
        l.add(Formula("transf", "Rotación de 90°", "T:P(x, y) → P'(−y, x)", "Giro de 90° antihorario alrededor del origen", "rotacion", "P(3, 1) girado 90°.", HEx("[-1,3]", "[-1,3]"), ""))
        l.add(Formula("transf", "Rotación de ángulo θ", "T:P'(x·cos θ − y·sen θ ; x·sen θ + y·cos θ)", "θ = ángulo de giro (antihorario) alrededor del origen; (x, y) = punto original", "rotacion", "P(3, 1) girado 90° (en GRADOS).", HEx("[3*cos(90)-1*sin(90),3*sin(90)+1*cos(90)]", "[-1,3]", true), ""))
        l.add(Formula("transf", "Homotecia", "T:P(x, y) → P'(k·x, k·y)", "k = razón de homotecia (k > 1 agranda, 0 < k < 1 reduce), con centro en el origen", "homotecia", "P(3, −1) con k = 2.", HEx("[2*3,2*(-1)]", "[6,-2]"), "Las áreas se multiplican por k²."))
        l.add(Formula("estad", "Media", "T:x̄ = S / n", "x̄ = media; S = suma de los datos; n = cantidad de datos", "", "Notas 11, 13, 15, 17, 19.", HEx("mean([11,13,15,17,19])", "15"), ""))
        l.add(Formula("estad", "Mediana", "T:Me = dato central (datos ordenados)", "Me = mediana; se ordenan los datos de menor a mayor", "", "18, 25, 12, 15, 20, 22, 15, 30, 18, 15.", HEx("median([18,25,12,15,20,22,15,30,18,15])", "18.0"), ""))
        l.add(Formula("estad", "Moda", "T:Mo = dato que más se repite", "Mo = moda", "", "12, 10, 12, 14, 12, 18.", HEx("12", "12"), "El 12 se repite 3 veces."))
        l.add(Formula("estad", "Marca de clase", "T:xᵢ = (Lᵢ + Lₛ) / 2", "xᵢ = marca de clase; Lᵢ = límite inferior; Lₛ = límite superior del intervalo", "tabla", "Intervalo [20; 30⟩.", HEx("(20+30)/2", "25"), ""))
    }
    private fun part4(l: MutableList<Formula>) {
        l.add(Formula("estad", "Rango y ancho de clase", "T:R = máximo − mínimo      w = R / k", "R = rango; w = ancho de clase; k = número de intervalos", "tabla", "R = 30 y k = 3.", HEx("30/3", "10"), ""))
        l.add(Formula("estad", "Frecuencias", "T:hᵢ = fᵢ / n", "fᵢ = frecuencia absoluta; Fᵢ = acumulada (suma hasta esa fila); hᵢ = relativa; Hᵢ = relativa acumulada; n = total de datos", "tabla", "fi = 8 de n = 20.", HEx("8/20", "2/5"), "En porcentaje: hi · 100."))
        l.add(Formula("estad", "Media de datos agrupados", "T:x̄ = Σ(xᵢ · fᵢ) / n", "x̄ = media; xᵢ = marca de clase; fᵢ = frecuencia; n = total", "tabla", "Tabla del dibujo: x = 15, 25, 35 con f = 5, 8, 7.", HEx("(15*5+25*8+35*7)/20", "26"), ""))
        l.add(Formula("estad", "Mediana de datos agrupados", "T:Me = Lᵢ + w · (n/2 − Fᵢ₋₁) / fᵢ", "Lᵢ = límite inferior del intervalo donde cae n/2; w = ancho; Fᵢ₋₁ = frecuencia acumulada del intervalo anterior; fᵢ = frecuencia del intervalo", "tabla", "n/2 = 10 cae en [20; 30⟩: Li = 20, F = 5, fi = 8.", HEx("20+10*(10-5)/8", "105/4"), ""))
        l.add(Formula("estad", "Moda de datos agrupados", "T:Mo = Lᵢ + w · d₁ / (d₁ + d₂)", "Lᵢ = límite inferior del intervalo de mayor fᵢ; w = ancho; d₁ = fᵢ − f anterior; d₂ = fᵢ − f siguiente", "tabla", "Intervalo modal [20; 30⟩: d1 = 3, d2 = 1.", HEx("20+10*3/(3+1)", "55/2"), ""))
        l.add(Formula("estad", "Gráfico circular", "T:Ángulo del sector = hᵢ · 360°", "hᵢ = fracción del total (por ejemplo 25% = 0,25)", "circular", "Una categoría con 25%.", HEx("25/100*360", "90"), ""))
        l.add(Formula("estad", "Varianza y desviación", "T:σ² = Σ(xᵢ − x̄)² / n", "σ² = varianza; σ = desviación estándar; xᵢ = cada dato; x̄ = media; n = cantidad de datos", "", "Datos 2, 4, 6.", HEx("((2-4)^2+(4-4)^2+(6-4)^2)/3", "8/3"), ""))
        l.add(Formula("prob", "Regla de Laplace", "T:P = casos favorables / casos totales", "Todos los casos deben ser igual de posibles", "dados", "Suma 7 con dos dados: 6 de 36.", HEx("6/36", "1/6"), "0 ≤ P ≤ 1."))
        l.add(Formula("prob", "Complemento", "T:P(no A) = 1 − P(A)", "P(A) = probabilidad de que ocurra A", "", "Anotar al menos un penal de 2 si cada uno tiene 0,9.", HEx("1-(1/10)^2", "99/100"), ""))
        l.add(Formula("prob", "Unión", "T:P(A ∪ B) = P(A) + P(B) − P(A ∩ B)", "A ∪ B = ocurre A o B (o ambos); A ∩ B = ocurren los dos", "", "50 alumnos: 35 San Marcos, 25 UNI, 10 ambas; solo una.", HEx("(35+25-2*10)/50", "4/5"), ""))
        l.add(Formula("prob", "Sucesos independientes", "T:P(A ∩ B) = P(A) · P(B)", "A y B no se afectan entre sí", "", "Cara en una moneda y suma 7 en dos dados.", HEx("1/2*6/36", "1/12"), ""))
        l.add(Formula("prob", "Probabilidad condicional", "T:P(A | B) = P(A ∩ B) / P(B)", "P(A | B) = probabilidad de A sabiendo que ocurrió B", "", "60% varones (40% becados) y 40% mujeres (30% becadas): mujer si es becado.", HEx("(4/10*3/10)/(4/10*3/10+6/10*4/10)", "1/3"), "Da 1/3 (equivale a 9/27)."))
        l.add(Formula("prob", "Distribución binomial", "T:P = C(n, k) · pᵏ · (1 − p)ⁿ⁻ᵏ", "n = número de intentos; k = éxitos que se piden; p = probabilidad de éxito en cada intento; C(n, k) = combinaciones", "", "4 cachorros, exactamente 3 machos.", HEx("comb(4,3)*(1/2)^3*(1/2)^1", "1/4"), ""))
        l.add(Formula("prob", "Principio de multiplicación", "N=m*n*p", "m, n, p = número de opciones en cada etapa", "", "4 camisas, 3 sacos, 3 pantalones y 2 pares de zapatos.", HEx("4*3*3*2", "72"), ""))
        l.add(Formula("prob", "Permutaciones", "P=n!", "n = número de objetos distintos; n! = 1 · 2 · ... · n", "", "Palabras con LUZ.", HEx("3!", "6"), "Con repetición: n!/(a! b!). Circular: (n − 1)!."))
        l.add(Formula("prob", "Variaciones", "V=n!/(n-k)!", "n = objetos disponibles; k = cuántos se ordenan", "", "Códigos de 3 cifras distintas con {2, 4, 6, 8}.", HEx("perm(4,3)", "24"), ""))
        l.add(Formula("prob", "Combinaciones", "C=n!/(k!*(n-k)!)", "n = objetos disponibles; k = cuántos se eligen (sin importar el orden)", "", "Elegir 3 de 10.", HEx("comb(10,3)", "120"), ""))
    }
}
