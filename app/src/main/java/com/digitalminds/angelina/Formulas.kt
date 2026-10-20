package com.digitalminds.angelina

class FSec(val id: String, val name: String)

/** Una fórmula del formulario. Si formula empieza con "T:" se muestra como texto; si no, dibujada como matemática. */
class Formula(val sec: String, val title: String, val formula: String, val vars: String, val fig: String, val exText: String, val ex: HEx?, val note: String) {
    val isText get() = formula.startsWith("T:")
    val shown get() = formula.removePrefix("T:")
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
        l.add(Formula("arit", "Tanto por ciento", "P=a/100*N", "a: porcentaje, N: cantidad.", "pct", "El 20% de 300.", HEx("20/100*300", "60"), "El a% del b% de N es a/100 · b/100 · N."))
        l.add(Formula("arit", "Aumentos sucesivos", "F=(1+a/100)*(1+b/100)", "a y b: aumentos en %. F: factor total.", "", "Aumentos de 5% y 10%.", HEx("(1+5/100)*(1+10/100)", "231/200"), "231/200 = 1,155: equivale a un aumento único de 15,5%."))
        l.add(Formula("arit", "Descuentos sucesivos", "Du=100-(100-a)*(100-b)/100", "a y b: descuentos en %. Du: descuento único.", "", "Descuentos de 20% y 40%.", HEx("100-(100-20)*(100-40)/100", "52"), "No se suman: 20% + 40% no es 60%."))
        l.add(Formula("arit", "Interés simple", "I=C*r*t/100", "C: capital, r: tasa en %, t: tiempo en la misma unidad que la tasa.", "", "S/ 3000 al 30% anual por 3 años.", HEx("3000*30*3/100", "2700"), "Si la tasa es mensual, el tiempo va en meses."))
        l.add(Formula("arit", "Monto", "M=C+I", "M: monto, C: capital, I: interés.", "", "S/ 7200 al 4% anual por 3 años y 4 meses (10/3 años).", HEx("7200+7200*4*(10/3)/100", "8160"), ""))
        l.add(Formula("arit", "Regla de tres simple directa", "x=b*c/a", "a es a b como c es a x (si una aumenta, la otra también).", "", "3 cuadernos cuestan S/ 12; ¿cuánto cuestan 5?", HEx("12*5/3", "20"), ""))
        l.add(Formula("arit", "Regla de tres simple inversa", "x=a*b/c", "Si una magnitud aumenta, la otra disminuye.", "", "6 obreros tardan 10 días; ¿cuánto tardan 4?", HEx("6*10/4", "15"), ""))
        l.add(Formula("arit", "Promedio aritmético", "MA=S/n", "S: suma de los datos, n: cantidad de datos.", "", "Promedio de 12, 15 y 18.", HEx("(12+15+18)/3", "15"), ""))
        l.add(Formula("arit", "Promedio geométrico", "MG=sqrt(a*b)", "Para dos números a y b.", "", "MG de 4 y 9.", HEx("sqrt(4*9)", "6"), "Para n números: raíz enésima del producto."))
        l.add(Formula("arit", "Promedio armónico", "MH=2*a*b/(a+b)", "Para dos números a y b.", "", "MH de 4 y 12.", HEx("2*4*12/(4+12)", "6"), "Siempre MA ≥ MG ≥ MH."))
        l.add(Formula("arit", "MCD y MCM", "a*b=MCD*MCM", "Solo para dos números.", "", "Con 12 y 18: MCD = 6 y MCM = 36.", HEx("gcd(12,18)*lcm(12,18)", "216"), "Da 216 = 12 · 18."))
        l.add(Formula("arit", "Cantidad de divisores", "CD=(m+1)*(n+1)*(p+1)", "Si N = a^m · b^n · c^p (primos).", "", "360 = 2³ · 3² · 5.", HEx("(3+1)*(2+1)*(1+1)", "24"), ""))
        l.add(Formula("arit", "Fracción generatriz", "T:0,ababab... = ab/99", "Decimal periódico puro de 2 cifras: tantos 9 como cifras.", "", "0,363636... = 36/99.", HEx("36/99", "4/11"), "Periódico mixto: (número completo − parte no periódica) / (9 y 0)."))
        l.add(Formula("arit", "Producto de potencias", "a^m*a^n=a^(m+n)", "Misma base: se suman exponentes.", "", "2³ · 2⁴.", HEx("2^3*2^4", "128"), ""))
        l.add(Formula("arit", "Cociente de potencias", "a^m/a^n=a^(m-n)", "Misma base: se restan exponentes.", "", "3⁵ / 3².", HEx("3^5/3^2", "27"), ""))
        l.add(Formula("arit", "Potencia de potencia", "(a^m)^n=a^(m*n)", "Se multiplican los exponentes.", "", "(2³)².", HEx("(2^3)^2", "64"), ""))
        l.add(Formula("arit", "Exponente negativo", "a^(-n)=1/a^n", "Pasa al denominador.", "", "2⁻³.", HEx("2^(-3)", "1/8"), ""))
        l.add(Formula("arit", "Exponente fraccionario", "T:a^(m/n) = ⁿ√(aᵐ)", "La raíz enésima de a elevado a m.", "", "8^(2/3).", HEx("simplify(8^(2/3))", "4"), ""))
        l.add(Formula("arit", "Raíz de un producto", "sqrt(a*b)=sqrt(a)*sqrt(b)", "Sirve para simplificar radicales.", "", "√50 = √(25 · 2).", HEx("sqrt(50)", "5*sqrt(2)"), ""))
        l.add(Formula("arit", "Racionalizar", "1/sqrt(a)=sqrt(a)/a", "Se multiplica arriba y abajo por √a.", "", "1/√2.", HEx("simplify(1/sqrt(2))", "sqrt(2)/2"), ""))
        l.add(Formula("arit", "Propiedad distributiva", "a*(b+c)=a*b+a*c", "También: conmutativa a + b = b + a y asociativa (a + b) + c = a + (b + c).", "", "3 · (4 + 5) = 3 · 4 + 3 · 5.", HEx("3*4+3*5", "27"), "Ley de signos: (+)(+) = +, (−)(−) = +, (+)(−) = −."))
        l.add(Formula("arit", "Logaritmo de un producto", "logb(a*c,b)=logb(a,b)+logb(c,b)", "El logaritmo convierte productos en sumas.", "", "log₂(8 · 4) = 3 + 2.", HEx("logb(8*4,2)", "5"), "De un cociente: resta. De una potencia: logb(aⁿ) = n · logb(a)."))
        l.add(Formula("arit", "Cambio de base", "logb(a,b)=ln(a)/ln(b)", "Pasa cualquier logaritmo a ln o log.", "", "log₃ 81.", HEx("logb(81,3)", "4"), ""))
        l.add(Formula("arit", "Proporción", "a/b=c/d", "Producto de extremos = producto de medios: a · d = b · c.", "", "3/4 = x/20.", HEx("solve(3/4=x/20,x)", "list[15]"), ""))
        l.add(Formula("alg", "Binomio al cuadrado", "(a+b)^2=a^2+2*a*b+b^2", "Con resta: (a − b)² = a² − 2ab + b².", "", "(x + 3)².", HEx("expand((x+3)^2)", "x^2+6*x+9"), ""))
        l.add(Formula("alg", "Diferencia de cuadrados", "(a+b)*(a-b)=a^2-b^2", "Suma por diferencia.", "", "103 · 97 = (100 + 3)(100 − 3).", HEx("100^2-3^2", "9991"), ""))
        l.add(Formula("alg", "Binomio al cubo", "(a+b)^3=a^3+3*a^2*b+3*a*b^2+b^3", "Con resta cambian los signos de los términos impares.", "", "(x + 1)³.", HEx("expand((x+1)^3)", "x^3+3*x^2+3*x+1"), ""))
        l.add(Formula("alg", "Suma de cubos", "a^3+b^3=(a+b)*(a^2-a*b+b^2)", "Diferencia: a³ − b³ = (a − b)(a² + ab + b²).", "", "x³ + 8.", HEx("factor(x^3+8)", "(x+2)*(x^2-2*x+4)"), ""))
        l.add(Formula("alg", "Identidad de Legendre", "(a+b)^2+(a-b)^2=2*(a^2+b^2)", "Resta: (a + b)² − (a − b)² = 4ab.", "", "Con a = 5 y b = 3.", HEx("(5+3)^2+(5-3)^2", "68"), ""))
        l.add(Formula("alg", "Suma de cuadrados conocida a + b y ab", "a^2+b^2=(a+b)^2-2*a*b", "Truco muy usado en exámenes.", "", "a + b = 5 y ab = 3.", HEx("5^2-2*3", "19"), ""))
    }
    private fun part1(l: MutableList<Formula>) {
        l.add(Formula("alg", "Si a + b + c = 0", "a^3+b^3+c^3=3*a*b*c", "Identidad condicional.", "", "a = 1, b = 2, c = −3.", HEx("1^3+2^3+(-3)^3", "-18"), "Da −18 = 3 · 1 · 2 · (−3)."))
        l.add(Formula("alg", "Ecuación de primer grado", "a*x+b=0", "Solución: x = −b/a (si a ≠ 0).", "", "3x + 6 = 0.", HEx("solve(3*x+6=0,x)", "list[-2]"), ""))
        l.add(Formula("alg", "Fórmula general (2.º grado)", "x=(-b+sqrt(b^2-4*a*c))/(2*a)", "La otra raíz lleva − delante de la raíz.", "", "2x² − 3x − 2 = 0.", HEx("solve(2*x^2-3*x-2=0,x)", "list[-1/2,2]"), ""))
        l.add(Formula("alg", "Discriminante", "D=b^2-4*a*c", "D > 0: dos raíces reales; D = 0: raíz doble; D < 0: raíces complejas.", "", "x² + 5x + 6.", HEx("5^2-4*1*6", "1"), ""))
        l.add(Formula("alg", "Suma y producto de raíces", "x1+x2=-b/a", "Producto: x₁ · x₂ = c/a.", "", "En x² − 7x + 5 = 0, la suma es 7 y el producto 5.", HEx("solve(x^2-7*x+5=0,x)", "list[1/2*(-sqrt(29)+7),1/2*(sqrt(29)+7)]"), "(r + 1)(s + 1) = producto + suma + 1 = 5 + 7 + 1 = 13."))
        l.add(Formula("alg", "Sistema 2 × 2 (Cramer)", "x=(c*e-b*f)/(a*e-b*d)", "Para ax + by = c y dx + ey = f.", "", "4x + 3y = 8 y x − 2y = 2.", HEx("linsolve([4*x+3*y=8,x-2*y=2],[x,y])", "[2,0]"), ""))
        l.add(Formula("alg", "Inecuación cuadrática", "x^2-k>=0", "Se hallan las raíces y se ven los signos por tramos.", "", "x² − 4 ≥ 0.", HEx("solve(x^2-4>=0,x)", "list[x<=-2,x>=2]"), ""))
        l.add(Formula("alg", "Ecuación con valor absoluto", "abs(x)=a", "Si a ≥ 0: x = a o x = −a.", "", "|x − 2| = 3.", HEx("solve(abs(x-2)=3,x)", "list[-1,5]"), ""))
        l.add(Formula("alg", "Pendiente de una recta", "m=(y2-y1)/(x2-x1)", "Entre los puntos (x₁, y₁) y (x₂, y₂).", "linea", "Entre (2, 3) y (4, 7).", HEx("(7-3)/(4-2)", "2"), ""))
        l.add(Formula("alg", "Vértice de la parábola", "h=-b/(2*a)", "La altura del vértice es k = f(h).", "parabola", "y = x² − 4x + 3.", HEx("-(-4)/(2*1)", "2"), ""))
        l.add(Formula("alg", "Distancia entre dos puntos", "d=sqrt((x2-x1)^2+(y2-y1)^2)", "Es Pitágoras en el plano cartesiano.", "linea", "Entre (1, 2) y (4, 6).", HEx("sqrt((4-1)^2+(6-2)^2)", "5"), ""))
        l.add(Formula("alg", "Punto medio", "M=[(x1+x2)/2,(y1+y2)/2]", "Promedio de las coordenadas.", "linea", "Entre (1, 2) y (5, 8).", HEx("[(1+5)/2,(2+8)/2]", "[3,5]"), ""))
        l.add(Formula("alg", "Suma de los n primeros naturales", "S=n*(n+1)/2", "1 + 2 + ... + n.", "", "De 1 a 100.", HEx("100*(100+1)/2", "5050"), ""))
        l.add(Formula("alg", "Suma de cuadrados", "S=n*(n+1)*(2*n+1)/6", "1² + 2² + ... + n².", "", "Hasta 10.", HEx("10*11*21/6", "385"), ""))
        l.add(Formula("alg", "Progresión aritmética", "an=a1+(n-1)*r", "r: razón (lo que se suma cada vez).", "", "a₁ = 3, r = 4, término 10.", HEx("3+(10-1)*4", "39"), "Suma: S = (a₁ + aₙ) · n / 2."))
        l.add(Formula("alg", "Progresión geométrica", "an=a1*q^(n-1)", "q: razón (lo que se multiplica cada vez).", "", "a₁ = 2, q = 3, término 5.", HEx("2*3^(5-1)", "162"), ""))
        l.add(Formula("plana", "Área del triángulo", "A=b*h/2", "b: base, h: altura relativa a esa base.", "tri", "Base 10 y altura 6.", HEx("10*6/2", "30"), ""))
        l.add(Formula("plana", "Fórmula de Herón", "A=sqrt(p*(p-a)*(p-b)*(p-c))", "p = (a + b + c)/2 es el semiperímetro.", "tri3", "Lados 13, 14 y 15 (p = 21).", HEx("sqrt(21*(21-13)*(21-14)*(21-15))", "84"), ""))
        l.add(Formula("plana", "Ángulos del triángulo", "A+B+C=180", "Ángulo exterior = suma de los dos interiores no adyacentes.", "tri3", "Si A = 50° y B = 70°.", HEx("180-(50+70)", "60"), ""))
        l.add(Formula("plana", "Teorema de Pitágoras", "c^2=a^2+b^2", "c: hipotenusa; a y b: catetos.", "rect", "Catetos 3 y 4.", HEx("sqrt(3^2+4^2)", "5"), "Ternas: 3-4-5, 5-12-13, 8-15-17, 7-24-25."))
        l.add(Formula("plana", "Altura sobre la hipotenusa", "h^2=m*n", "m y n: proyecciones de los catetos sobre la hipotenusa.", "recth", "m = 4 y n = 9.", HEx("sqrt(4*9)", "6"), ""))
        l.add(Formula("plana", "Cateto y su proyección", "a^2=c*m", "El cateto al cuadrado es la hipotenusa por su proyección.", "recth", "Hipotenusa 13 y proyección 4.", HEx("sqrt(13*4)", "2*sqrt(13)"), ""))
        l.add(Formula("plana", "Catetos por altura", "a*b=c*h", "Producto de catetos = hipotenusa por altura.", "recth", "Catetos 3 y 4, hipotenusa 5.", HEx("3*4/5", "12/5"), ""))
        l.add(Formula("plana", "Cuadrado", "A=l^2", "Diagonal: d = l√2. Perímetro: 4l.", "cuadrado", "Lado 6: diagonal.", HEx("6*sqrt(2)", "6*sqrt(2)"), ""))
        l.add(Formula("plana", "Rectángulo", "A=b*h", "Perímetro: 2(b + h). Diagonal: √(b² + h²).", "rectangulo", "Base 8 y altura 5.", HEx("8*5", "40"), ""))
        l.add(Formula("plana", "Rombo", "A=DM*dm/2", "DM: diagonal mayor, dm: diagonal menor.", "rombo", "Diagonales 10 y 6.", HEx("10*6/2", "30"), ""))
        l.add(Formula("plana", "Paralelogramo", "A=b*h", "h: altura (no el lado inclinado).", "paralelo", "Base 9 y altura 4.", HEx("9*4", "36"), ""))
        l.add(Formula("plana", "Trapecio", "A=(B+b)/2*h", "Base media: m = (B + b)/2.", "trapecio", "B = 12, b = 6, h = 5.", HEx("(12+6)/2*5", "45"), ""))
        l.add(Formula("plana", "Círculo", "A=π*r^2", "Longitud de la circunferencia: L = 2πr.", "circulo", "Radio 5.", HEx("simplify(pi*5^2)", "25*pi"), ""))
        l.add(Formula("plana", "Sector circular", "A=π*r^2*θ/360", "θ: ángulo central en grados.", "sector", "Radio 6 y θ = 60°.", HEx("simplify(pi*6^2*60/360)", "6*pi"), "Longitud del arco: L = πrθ/180."))
    }
    private fun part2(l: MutableList<Formula>) {
        l.add(Formula("plana", "Ángulos de un polígono", "S=180*(n-2)", "Suma de ángulos interiores de n lados. Exterior regular: 360/n.", "hexagono", "Hexágono.", HEx("180*(6-2)", "720"), ""))
        l.add(Formula("plana", "Diagonales de un polígono", "D=n*(n-3)/2", "n: número de lados.", "hexagono", "Hexágono.", HEx("6*(6-3)/2", "9"), ""))
        l.add(Formula("plana", "Hexágono regular", "A=3*sqrt(3)*l^2/2", "Son 6 triángulos equiláteros.", "hexagono", "Lado 2.", HEx("simplify(3*sqrt(3)*2^2/2)", "6*sqrt(3)"), ""))
        l.add(Formula("plana", "Triángulo equilátero", "A=l^2*sqrt(3)/4", "Altura: h = l√3/2.", "equilatero", "Lado 4.", HEx("simplify(4^2*sqrt(3)/4)", "4*sqrt(3)"), ""))
        l.add(Formula("plana", "Ángulo inscrito", "x=arco/2", "Vale la mitad del arco que abarca. El central vale igual que su arco.", "inscrito", "Arco de 100°.", HEx("100/2", "50"), ""))
        l.add(Formula("plana", "Ángulo interior entre cuerdas", "x=(a+b)/2", "a y b: arcos opuestos.", "cuerdas", "Arcos de 80° y 40°.", HEx("(80+40)/2", "60"), "Ángulo exterior: x = (a − b)/2."))
        l.add(Formula("plana", "Teorema de las cuerdas", "a*b=c*d", "Productos de los segmentos de cada cuerda.", "cuerdas", "3 · 8 = 4 · x.", HEx("solve(3*8=4*x,x)", "list[6]"), ""))
        l.add(Formula("plana", "Tangente y secante", "t^2=a*(a+b)", "t: tangente; a: parte externa; a + b: secante completa.", "tangente", "a = 4 y a + b = 9.", HEx("sqrt(4*9)", "6"), ""))
        l.add(Formula("plana", "Teorema de Thales", "a/b=c/d", "Paralelas cortan segmentos proporcionales.", "thales", "a = 3, b = 6, c = 4.", HEx("solve(3/6=4/x,x)", "list[8]"), ""))
        l.add(Formula("lineas", "Baricentro (medianas)", "AG=2*GM", "El baricentro divide cada mediana en razón 2 a 1.", "mediana", "Mediana de 9.", HEx("2*9/3", "6"), ""))
        l.add(Formula("lineas", "Incentro (bisectrices)", "x=90+B/2", "x: ángulo AIC; el incentro es el centro de la circunferencia inscrita.", "bisectriz", "B = 80°.", HEx("90+80/2", "130"), ""))
        l.add(Formula("lineas", "Circuncentro (mediatrices)", "x=2*B", "x: ángulo AOC (central); centro de la circunferencia circunscrita.", "mediatriz", "B = 50°.", HEx("2*50", "100"), "En un triángulo rectángulo está en el punto medio de la hipotenusa."))
        l.add(Formula("lineas", "Ortocentro (alturas)", "x=180-B", "x: ángulo AHC; las alturas se cortan en H.", "altura", "B = 70°.", HEx("180-70", "110"), ""))
        l.add(Formula("lineas", "Excentro", "x=90-B/2", "x: ángulo AEC; centro de una circunferencia exinscrita.", "excentro", "B = 80°.", HEx("90-80/2", "50"), ""))
        l.add(Formula("lineas", "Baricentro y circuncentro en un rectángulo", "OG=c/6", "c: hipotenusa; la mediana a la hipotenusa mide c/2.", "mediana", "Hipotenusa 30.", HEx("30/6", "5"), ""))
        l.add(Formula("lineas", "Teorema de la bisectriz", "T:AB/BC = AD/DC", "La bisectriz de B divide AC en la razón de los lados.", "bisectriz", "AB = 6, BC = 9, AC = 10: AD = 6·10/15.", HEx("6*10/(6+9)", "4"), ""))
        l.add(Formula("inscritas", "Radio inscrito en un triángulo", "r=A/p", "A: área, p: semiperímetro.", "incirculo", "Triángulo 3-4-5 (A = 6, p = 6).", HEx("6/6", "1"), ""))
        l.add(Formula("inscritas", "Radio circunscrito", "R=a*b*c/(4*A)", "a, b, c: lados; A: área.", "circuncirculo", "Triángulo 3-4-5.", HEx("3*4*5/(4*6)", "5/2"), "En el rectángulo R = hipotenusa/2."))
        l.add(Formula("inscritas", "Teorema de Poncelet", "a+b=c+2*r", "En un triángulo rectángulo con circunferencia inscrita de radio r.", "incirculo", "Catetos 3 y 4, hipotenusa 5.", HEx("solve(3+4=5+2*r,r)", "list[1]"), ""))
        l.add(Formula("inscritas", "Teorema de Pitot", "T:AB + CD = BC + AD", "Cuadrilátero circunscrito: lados opuestos suman igual.", "pitot", "AB = 5, CD = 7, BC = 4.", HEx("solve(5+7=4+x,x)", "list[8]"), ""))
        l.add(Formula("inscritas", "Cuadrado inscrito en circunferencia", "l=R*sqrt(2)", "La diagonal del cuadrado es el diámetro.", "cuadinscrito", "R = 5.", HEx("5*sqrt(2)", "5*sqrt(2)"), ""))
        l.add(Formula("inscritas", "Circunferencia inscrita en cuadrado", "r=l/2", "El diámetro es igual al lado.", "circinscrita", "Lado 8.", HEx("8/2", "4"), ""))
        l.add(Formula("inscritas", "Hexágono inscrito", "l=R", "El lado del hexágono regular es igual al radio.", "hexinscrito", "R = 7.", HEx("7", "7"), ""))
        l.add(Formula("inscritas", "Triángulo equilátero inscrito", "l=R*sqrt(3)", "Radio inscrito del equilátero: r = l√3/6.", "triinscrito", "R = 2.", HEx("2*sqrt(3)", "2*sqrt(3)"), ""))
        l.add(Formula("solidos", "Cubo", "V=a^3", "Área total: 6a². Diagonal: a√3.", "cubo", "Arista 3: volumen y diagonal.", HEx("[3^3,3*sqrt(3)]", "[27,3*sqrt(3)]"), ""))
        l.add(Formula("solidos", "Rectoedro (ortoedro)", "D=sqrt(a^2+b^2+c^2)", "Volumen: V = abc. Área total: 2(ab + bc + ac).", "ortoedro", "Aristas 4, 5 y 6.", HEx("sqrt(4^2+5^2+6^2)", "sqrt(77)"), ""))
        l.add(Formula("solidos", "Prisma recto", "V=Ab*h", "Ab: área de la base. Área lateral: perímetro de la base · h.", "prisma", "Base triángulo rectángulo 3-4 y altura 6.", HEx("3*4/2*6", "36"), ""))
        l.add(Formula("solidos", "Cilindro", "V=π*r^2*h", "Área lateral: 2πrh. Área total: 2πr(h + r).", "cilindro", "r = 3 y h = 4.", HEx("simplify(pi*3^2*4)", "36*pi"), ""))
        l.add(Formula("solidos", "Pirámide", "V=Ab*h/3", "Área lateral: perímetro de la base · apotema / 2.", "piramide", "Base cuadrada de lado 6 y altura 4.", HEx("6^2*4/3", "48"), ""))
        l.add(Formula("solidos", "Cono", "V=π*r^2*h/3", "Generatriz: g² = r² + h². Área lateral: πrg.", "cono", "h = 6 y g = 7 (r² = 49 − 36 = 13).", HEx("simplify(pi*13*6/3)", "26*pi"), ""))
    }
    private fun part3(l: MutableList<Formula>) {
        l.add(Formula("solidos", "Esfera", "V=4/3*π*r^3", "Área de la superficie: 4πr².", "esfera", "Radio 3.", HEx("simplify(4/3*pi*3^3)", "36*pi"), ""))
        l.add(Formula("solidos", "Tronco de cono", "V=π*h*(R^2+r^2+R*r)/3", "R y r: radios de las bases.", "tronco", "R = 4, r = 2, h = 3.", HEx("simplify(pi*3*(4^2+2^2+4*2)/3)", "28*pi"), ""))
        l.add(Formula("solidos", "Teorema de Euler (poliedros)", "C+V=A+2", "C: caras, V: vértices, A: aristas.", "cubo", "Cubo: 6 caras, 8 vértices, 12 aristas.", HEx("6+8-12", "2"), "Poliedros regulares: tetraedro, hexaedro (cubo), octaedro, dodecaedro e icosaedro."))
        l.add(Formula("trigo", "Razones trigonométricas", "sin(α)=CO/H", "Truco CO CA CO CA HIP HIP: sen = CO/H, cos = CA/H, tan = CO/CA, cot = CA/CO, sec = H/CA, csc = H/CO.", "razones", "sen 30° (en GRADOS).", HEx("sin(30)", "1/2", true), "Se leen de arriba hacia abajo: los numeradores son CO CA CO CA HIP HIP y los denominadores HIP HIP CA CO CA CO."))
        l.add(Formula("trigo", "Recíprocas", "sin(α)*csc(α)=1", "También cos · sec = 1 y tan · cot = 1.", "razones", "sen 30° · csc 30°.", HEx("sin(30)*csc(30)", "1", true), ""))
        l.add(Formula("trigo", "Identidad pitagórica", "sin(α)^2+cos(α)^2=1", "También 1 + tan² = sec² y 1 + cot² = csc².", "razones", "Con α = 37°.", HEx("simplify(sin(37)^2+cos(37)^2)", "1", true), ""))
        l.add(Formula("trigo", "Ángulos complementarios", "sin(α)=cos(90-α)", "Si α + β = 90°, sen α = cos β y tan α = cot β.", "razones", "sen 20° − cos 70°.", HEx("simplify(sin(20)-cos(70))", "0", true), ""))
        l.add(Formula("trigo", "Ley de senos", "a/sin(A)=b/sin(B)", "Lado entre el seno del ángulo opuesto.", "tri3", "a = 10, A = 30°, B = 45°: b.", HEx("simplify(10*sin(45)/sin(30))", "10*sqrt(2)", true), ""))
        l.add(Formula("trigo", "Ley de cosenos", "a^2=b^2+c^2-2*b*c*cos(A)", "Generaliza Pitágoras.", "tri3", "b = 5, c = 8, A = 60°.", HEx("sqrt(5^2+8^2-2*5*8*cos(60))", "7", true), ""))
        l.add(Formula("trigo", "Área con dos lados y el ángulo", "A=a*b*sin(C)/2", "C: ángulo entre a y b.", "tri3", "a = 6, b = 8, C = 30°.", HEx("6*8*sin(30)/2", "12", true), ""))
        l.add(Formula("trigo", "Sistemas de medida angular", "T:S/180 = C/200 = R/π", "S: sexagesimal, C: centesimal, R: radianes.", "", "60° en radianes.", HEx("simplify(60*pi/180)", "pi/3"), "90° = 100ᵍ = π/2 rad."))
        l.add(Formula("trigo", "Ángulo doble", "sin(2*α)=2*sin(α)*cos(α)", "cos 2α = cos²α − sen²α.", "", "Comprobación con α = 30°.", HEx("simplify(sin(60)-2*sin(30)*cos(30))", "0", true), ""))
        l.add(Formula("notables", "45° y 45°", "k*sqrt(2)", "Catetos k y k; hipotenusa k√2. Exacto.", "n45", "tan 45°.", HEx("tan(45)", "1", true), ""))
        l.add(Formula("notables", "30° y 60°", "2*k", "Catetos k (frente a 30°) y k√3; hipotenusa 2k. Exacto.", "n30", "sen 30° y cos 30°.", HEx("[sin(30),cos(30)]", "[1/2,sqrt(3)/2]", true), ""))
        l.add(Formula("notables", "37° y 53° (aproximado)", "5*k", "Catetos 3k (frente a 37°) y 4k; hipotenusa 5k. Es aproximado.", "n37", "sen 37° real.", HEx("evalf(sin(37))", "0.601815023152", true), "En exámenes se usa sen 37° ≈ 3/5 = 0,6; el valor real es 0,6018..."))
        l.add(Formula("notables", "15° y 75°", "4*k", "Catetos (√6 − √2)k y (√6 + √2)k; hipotenusa 4k. Exacto.", "n15", "sen 15° = (√6 − √2)/4: la diferencia da 0.", HEx("simplify(sin(15)-(sqrt(6)-sqrt(2))/4)", "0", true), ""))
        l.add(Formula("notables", "16° y 74° (aproximado)", "25*k", "Catetos 7k (frente a 16°) y 24k; hipotenusa 25k.", "n16", "sen 16° real.", HEx("evalf(sin(16))", "0.275637355817", true), "7/25 = 0,28 es aproximado."))
        l.add(Formula("notables", "8° y 82° (aproximado)", "5*sqrt(2)*k", "Catetos k (frente a 8°) y 7k; hipotenusa 5√2 k.", "n8", "tan 8° real.", HEx("evalf(tan(8))", "0.140540834702", true), "1/7 ≈ 0,1429 es aproximado."))
        l.add(Formula("notables", "14° y 76° (aproximado)", "sqrt(17)*k", "Catetos k (frente a 14°) y 4k; hipotenusa √17 k.", "n14", "tan 14° real.", HEx("evalf(tan(14))", "0.249328002843", true), "1/4 = 0,25 es aproximado."))
        l.add(Formula("notables", "18,5° y 71,5° (aproximado)", "sqrt(10)*k", "Catetos k y 3k; hipotenusa √10 k. Es la mitad de 37°.", "n18", "tan 18,5° real.", HEx("evalf(tan(18.5))", "0.334595319502", true), "1/3 ≈ 0,333 es aproximado."))
        l.add(Formula("notables", "26,5° y 63,5° (aproximado)", "sqrt(5)*k", "Catetos k y 2k; hipotenusa √5 k. Es la mitad de 53°.", "n26", "tan 26,5° real.", HEx("evalf(tan(26.5))", "0.498581608053", true), "1/2 = 0,5 es aproximado."))
        l.add(Formula("transf", "Traslación", "P'=[x+a,y+b]", "Mueve el punto a unidades en x y b en y.", "traslacion", "P(2, 1) con vector (3, −4).", HEx("[2+3,1-4]", "[5,-3]"), "La figura no cambia de forma ni de tamaño."))
        l.add(Formula("transf", "Reflexión en el eje x", "P'=[x,-y]", "Como un espejo sobre el eje x.", "reflexion", "P(3, 2).", HEx("[3,-2]", "[3,-2]"), "En el eje y: (−x, y). En el origen: (−x, −y). En la recta y = x: (y, x)."))
        l.add(Formula("transf", "Rotación de 90°", "P'=[-y,x]", "Giro antihorario alrededor del origen. 180°: (−x, −y). 270°: (y, −x).", "rotacion", "P(3, 1) girado 90°.", HEx("[-1,3]", "[-1,3]"), ""))
        l.add(Formula("transf", "Rotación de ángulo θ", "P'=[x*cos(θ)-y*sin(θ),x*sin(θ)+y*cos(θ)]", "Giro antihorario de θ alrededor del origen.", "rotacion", "P(3, 1) girado 90° (en GRADOS).", HEx("[3*cos(90)-1*sin(90),3*sin(90)+1*cos(90)]", "[-1,3]", true), ""))
        l.add(Formula("transf", "Homotecia", "P'=[k*x,k*y]", "Agranda (k > 1) o reduce (0 < k < 1) desde el origen.", "homotecia", "P(3, −1) con k = 2.", HEx("[2*3,2*(-1)]", "[6,-2]"), "Las áreas se multiplican por k²."))
        l.add(Formula("estad", "Media", "T:x̄ = S / n", "S: suma de datos, n: cantidad.", "", "Notas 11, 13, 15, 17, 19.", HEx("mean([11,13,15,17,19])", "15"), ""))
        l.add(Formula("estad", "Mediana", "T:Me = dato central (datos ordenados)", "Ordena los datos; si n es par, promedia los dos centrales.", "", "18, 25, 12, 15, 20, 22, 15, 30, 18, 15.", HEx("median([18,25,12,15,20,22,15,30,18,15])", "18.0"), ""))
        l.add(Formula("estad", "Moda", "T:Mo = dato que más se repite", "Puede haber más de una moda o ninguna.", "", "12, 10, 12, 14, 12, 18.", HEx("12", "12"), "El 12 se repite 3 veces."))
        l.add(Formula("estad", "Marca de clase", "xi=(Li+Ls)/2", "Li y Ls: límites del intervalo.", "tabla", "Intervalo [20; 30⟩.", HEx("(20+30)/2", "25"), ""))
    }
    private fun part4(l: MutableList<Formula>) {
        l.add(Formula("estad", "Rango y ancho de clase", "w=R/k", "R = máximo − mínimo; k: número de intervalos (Sturges: k = 1 + 3,3 log n).", "tabla", "R = 30 y k = 3.", HEx("30/3", "10"), ""))
        l.add(Formula("estad", "Frecuencias", "hi=fi/n", "Fi: suma de fi hasta la fila; Hi: suma de hi. La última Fi es n y la última Hi es 1.", "tabla", "fi = 8 de n = 20.", HEx("8/20", "2/5"), "En porcentaje: hi · 100."))
        l.add(Formula("estad", "Media de datos agrupados", "T:x̄ = Σ(xᵢ · fᵢ) / n", "Se usa la marca de clase de cada intervalo.", "tabla", "Tabla del dibujo: x = 15, 25, 35 con f = 5, 8, 7.", HEx("(15*5+25*8+35*7)/20", "26"), ""))
        l.add(Formula("estad", "Mediana de datos agrupados", "Me=Li+w*(n/2-F)/fi", "F: frecuencia acumulada anterior; fi: la del intervalo de la mediana.", "tabla", "n/2 = 10 cae en [20; 30⟩: Li = 20, F = 5, fi = 8.", HEx("20+10*(10-5)/8", "105/4"), ""))
        l.add(Formula("estad", "Moda de datos agrupados", "Mo=Li+w*d1/(d1+d2)", "d1 = fi − f anterior; d2 = fi − f siguiente.", "tabla", "Intervalo modal [20; 30⟩: d1 = 3, d2 = 1.", HEx("20+10*3/(3+1)", "55/2"), ""))
        l.add(Formula("estad", "Gráfico circular", "θ=hi*360", "Ángulo del sector de cada categoría.", "circular", "Una categoría con 25%.", HEx("25/100*360", "90"), ""))
        l.add(Formula("estad", "Varianza y desviación", "T:σ² = Σ(xᵢ − x̄)² / n", "σ: raíz de la varianza (poblacional). La muestral divide entre n − 1.", "", "Datos 2, 4, 6.", HEx("((2-4)^2+(4-4)^2+(6-4)^2)/3", "8/3"), ""))
        l.add(Formula("prob", "Regla de Laplace", "P=f/t", "f: casos favorables; t: casos totales igualmente posibles.", "dados", "Suma 7 con dos dados: 6 de 36.", HEx("6/36", "1/6"), "0 ≤ P ≤ 1."))
        l.add(Formula("prob", "Complemento", "T:P(no A) = 1 − P(A)", "Probabilidad de que no ocurra.", "", "Anotar al menos un penal de 2 si cada uno tiene 0,9.", HEx("1-(1/10)^2", "99/100"), ""))
        l.add(Formula("prob", "Unión", "T:P(A ∪ B) = P(A) + P(B) − P(A ∩ B)", "Si son excluyentes, P(A y B) = 0.", "", "50 alumnos: 35 San Marcos, 25 UNI, 10 ambas; solo una.", HEx("(35+25-2*10)/50", "4/5"), ""))
        l.add(Formula("prob", "Sucesos independientes", "T:P(A ∩ B) = P(A) · P(B)", "Uno no afecta al otro.", "", "Cara en una moneda y suma 7 en dos dados.", HEx("1/2*6/36", "1/12"), ""))
        l.add(Formula("prob", "Probabilidad condicional", "T:P(A | B) = P(A ∩ B) / P(B)", "Probabilidad de A sabiendo que pasó B.", "", "60% varones (40% becados) y 40% mujeres (30% becadas): mujer si es becado.", HEx("(4/10*3/10)/(4/10*3/10+6/10*4/10)", "1/3"), "Da 1/3 (equivale a 9/27)."))
        l.add(Formula("prob", "Distribución binomial", "P=C(n,k)*p^k*(1-p)^(n-k)", "n intentos, k éxitos, p: probabilidad de éxito.", "", "4 cachorros, exactamente 3 machos.", HEx("comb(4,3)*(1/2)^3*(1/2)^1", "1/4"), ""))
        l.add(Formula("prob", "Principio de multiplicación", "N=m*n*p", "Si hay m formas, luego n y luego p.", "", "4 camisas, 3 sacos, 3 pantalones y 2 pares de zapatos.", HEx("4*3*3*2", "72"), ""))
        l.add(Formula("prob", "Permutaciones", "P=n!", "Ordenar n objetos distintos.", "", "Palabras con LUZ.", HEx("3!", "6"), "Con repetición: n!/(a! b!). Circular: (n − 1)!."))
        l.add(Formula("prob", "Variaciones", "V=n!/(n-k)!", "Ordenar k de n (importa el orden).", "", "Códigos de 3 cifras distintas con {2, 4, 6, 8}.", HEx("perm(4,3)", "24"), ""))
        l.add(Formula("prob", "Combinaciones", "C=n!/(k!*(n-k)!)", "Elegir k de n (no importa el orden).", "", "Elegir 3 de 10.", HEx("comb(10,3)", "120"), ""))
    }
}
