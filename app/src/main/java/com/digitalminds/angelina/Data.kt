package com.digitalminds.angelina

class GuideEntry(val title: String, val goal: String, val steps: List<String>, val example: String, val tags: String = "")

/** Manual en español. Cada ejemplo fue comprobado con Giac 1.9 (resultado anotado en el comentario). */
object Guide {
    private fun g(t: String, goal: String, ex: String, tags: String, vararg steps: String) = GuideEntry(t, goal, steps.toList(), ex, tags)

    val entries: List<GuideEntry> = listOf(
        // Tareas frecuentes (buscador por tarea)
        g("Ecuación de grado 2", "Hallar las dos raíces de ax² + bx + c = 0.", "solve(2*x^2-3*x-2=0,x)", "ecuacion cuadratica segundo grado raices formula general bhaskara",
            "Ve a la página 4 y pulsa resolver.", "Escribe la ecuación. El signo = está en la tecla = < > de la página 5 o manteniendo pulsada la coma.",
            "Deja x como incógnita y pulsa =.", "Resultado del ejemplo: x = -1/2 y x = 2."),
        g("Raíz dentro de raíz", "Simplificar radicales dobles como √(3 + 2√2).", "simplify(sqrt(3+2*sqrt(2)))", "raiz sobre raiz radical doble anidada denestar simplificar",
            "Pulsa √ y, dentro, escribe 3 + 2 y otra √ con el 2.", "Encierra todo en simplif. (página 5) y pulsa =.", "Resultado del ejemplo: √2 + 1."),
        g("Graficar una parábola", "Ver la forma de y = x² − 4x + 3 y sus raíces.", "x^2-4*x+3", "graficar parabola funcion cuadratica grafica curva vertice",
            "Abre Modos (ícono de cuadrícula) y elige Gráfica.", "Escribe la función en f1.", "Toca la curva cerca del cruce con el eje x y pulsa Raíz; cerca del fondo pulsa Mín.",
            "En el ejemplo las raíces son 1 y 3 y el vértice está en x = 2."),
        g("Porcentajes sucesivos", "Aplicar aumentos y descuentos uno tras otro.", "50*(1+10/100)*(1-30/100)*(1-5/100)", "porcentaje aumento descuento sucesivo tanto por ciento",
            "En la página Aritmética usa +% para aumentar y −% para descontar; % único da el porcentaje equivalente de una lista como [10, −30, −5].",
            "A mano: cada aumento de a% multiplica por (1 + a/100) y cada descuento de d% por (1 − d/100).",
            "Ejemplo: 50 con +10%, −30% y −5% da 1463/40 = 36,575 (factor 0,7315, descuento único 26,85%)."),
        g("Descuento único equivalente", "Reemplazar dos descuentos por uno solo.", "1-(1-20/100)*(1-40/100)", "descuento unico equivalente sucesivos",
            "Resta de 1 el producto de los factores.", "Ejemplo: 20% y 40% dan 13/25 = 0,52, es decir 52%."),
        g("Interés simple", "Calcular el interés I = C · r · t.", "3000*30/100*3", "interes simple capital tasa tiempo monto",
            "Multiplica capital, tasa (en fracción) y tiempo en la misma unidad que la tasa.", "Ejemplo: S/ 3000 al 30% anual por 3 años da S/ 2700 de interés.",
            "El monto es M = C + I."),
        g("Media y mediana", "Promedio y valor central de una lista.", "median([18,25,12,15,20,22,15,30,18,15])", "media mediana moda promedio estadistica",
            "Abre Modos, Estadística, y escribe los datos en la columna X.", "Pulsa Calcular.", "También puedes escribir mean([...]) o median([...]) en Inicio. Ejemplo: mediana 18."),
        g("Combinaciones y permutaciones", "Contar grupos sin orden (C) y con orden (P).", "comb(10,3)", "combinacion permutacion analisis combinatorio nCr nPr factorial",
            "Página 3: nCr para combinaciones y nPr para permutaciones.", "Escribe n, coma, r y pulsa =.", "Ejemplo: C(10,3) = 120 y P(5,3) = 60."),
        g("¿Es cierto?: comprobar una igualdad", "Saber si dos expresiones son iguales.", "3^2+4^2=5^2", "comprobar verificar igualdad verdadero falso identidad",
            "Escribe una igualdad, por ejemplo 3² + 4² = 5².", "Mantén pulsada la tecla = y elige ¿Es cierto?.",
            "Si la diferencia entre ambos lados se simplifica a 0 verás Verdadero ✓; si no, Falso ✗."),
        g("Seno de ángulos en grados", "Calcular sen 30° y otros ángulos notables.", "sin(30)", "seno coseno tangente grados angulo notable trigonometria",
            "Toca el indicador de ángulo arriba a la derecha hasta que diga GRADOS.", "Pulsa sen, escribe 30 y pulsa =.", "Resultado: 1/2."),
        g("Practicar para Beca 18", "Problemas tipo examen por tema y simulacros de 30 preguntas con cronómetro.", "comb(10,3)", "beca 18 examen simulacro practica razonamiento matematico preguntas alternativas",
            "Abre Modos y elige Beca 18.", "Practicar por tema: eliges una alternativa y ves al instante si acertaste, con la solución paso a paso.",
            "Simulacro: 30 preguntas mezcladas de todos los temas, con o sin límite de tiempo; se corrige al terminar y ves tus resultados por tema.",
            "Repasar mis errores junta las preguntas que fallaste la última vez. Desde cada solución puedes probarla en la calculadora o abrir el paso a paso."),
        g("Resolver paso a paso", "Ver la fórmula, la sustitución de tus datos y cada paso hasta el resultado.", "solve(2*x^2-3*x-2=0,x)", "paso a paso resolver explicar pasos sustitucion tabla frecuencias",
            "Abre Modos y elige Paso a paso.", "Elige un tema (por ejemplo Ecuación de segundo grado) y escribe tus datos o pulsa Ejemplo.",
            "Pulsa Resolver: verás Δ, √Δ, x₁, x₂, la suma, el producto y el vértice.", "En Estadística puedes armar la tabla de frecuencias con intervalos y obtener media, mediana y moda."),
        g("Personalizar el teclado", "Ordenar páginas, ocultarlas, fijar una y armar Mis teclas.", "2+3", "teclado paginas ordenar ocultar fijar mis teclas favoritas personalizar",
            "Desliza el teclado a los lados; abajo ves el nombre de la página.", "En Ajustes, sección Teclado, usa ↑ ↓ para ordenar, 👁 para ocultar y 📌 para fijar la página con la que abre.",
            "Ordenar por mi uso pone primero las páginas que más usas.", "Para Mis teclas mantén pulsada cualquier tecla y toca ☆ Agregar a Mis teclas (hasta 10)."),
        g("Plantillas de gráficas", "Graficar recta, parábola, exponencial y otras con una tecla.", "x^2-4*x+3", "plantilla grafica recta parabola exponencial logaritmica hiperbola valor absoluto",
            "Abre Gráfica y toca f1.", "Desliza el teclado hasta la página Gráficas y toca la plantilla.", "Cambia los números para mover la curva y usa Raíz, Mín o Máx."),
        // Funciones de la calculadora
        g("Matrices: agregar filas y columnas", "Agrandar o achicar una matriz mientras la escribes.", "det([[1,2,3],[4,5,6],[7,8,10]])", "matriz tamano filas columnas",
            "Pulsa [M] en la página 6 para insertar una matriz 2 × 2. Aparecen los botones + fila y + columna.", "Toca una celda, escribe el valor y pulsa + fila o + columna.",
            "Con − fila o − columna se quita una fila o columna; los valores se conservan. Las celdas vacías valen 0.", "Pulsa = para calcular det, inv o autoval. El tamaño máximo es 8 × 8. Ejemplo: det = −3."),
        g("Cotangente, secante y cosecante", "Usar cot, sec y csc y sus inversas.", "cot(pi/4)", "cot sec csc reciproca cotangente secante cosecante",
            "Abre la página 2 y pulsa cot, sec o csc.", "Para las inversas mantén pulsada la tecla.", "Ejemplo: cot(π/4) = 1."),
        g("Raíz cúbica", "Raíz cúbica real de un número.", "simplify(surd(27,3))", "raiz cubica radical",
            "Mantén pulsada la tecla √ de la página 1 y elige ∛.", "Escribe el número y pulsa =. Ejemplo: 3."),
        g("Raíz enésima", "Raíz de cualquier índice, como la raíz quinta de 32.", "simplify(surd(32,5))", "raiz indice radical enesima",
            "Mantén pulsada √ y elige ⁿ√.", "Escribe el índice en la casilla pequeña, avanza y escribe el radicando.", "Ejemplo: 2."),
        g("Logaritmo en cualquier base", "Calcular logₐ de un número.", "logb(8,2)", "logaritmo base",
            "Mantén pulsada ln o log y elige logₐ.", "Escribe el número, coma y la base.", "Ejemplo: log₂ 8 = 3."),
        g("ln y eˣ", "Logaritmo natural y exponencial.", "ln(exp(2))", "exponencial natural neperiano",
            "ln está en la página 1; eˣ manteniendo pulsada ln.", "Se deshacen entre sí: ln(e²) = 2."),
        g("Potencias", "Elevar a un exponente.", "2^10", "potencia exponente cuadrado",
            "Escribe la base, pulsa ^ y escribe el exponente.", "Desliza a la derecha sobre la línea de entrada para salir del exponente. Ejemplo: 1024."),
        g("Fracciones", "Sumar y simplificar fracciones exactas.", "1/2+1/3", "fraccion racional",
            "Escribe 1, pulsa a/b, escribe 2 y avanza.", "Suma la siguiente fracción y pulsa =. Ejemplo: 5/6."),
        g("Fracciones mixtas", "Ver fracciones impropias como mixtas.", "7/3", "mixta fraccion impropia",
            "En Ajustes activa Fracciones mixtas.", "Escribe 7/3 y pulsa =; verás 2 1/3."),
        g("Exacto y decimal (S⇔D)", "Cambiar un resultado entre forma exacta y decimal.", "sqrt(2)", "exacto decimal aproximado",
            "Pulsa = para el resultado exacto.", "Toca el resultado para verlo en decimal y otra vez para volver."),
        g("Integral definida", "Área bajo una curva entre dos límites.", "int(x^2,x,0,3)", "integral area definida",
            "Página 4, tecla ∫ₐᵇ.", "Completa función, variable y límites, luego =. Ejemplo: 9."),
        g("Integral indefinida", "Hallar una primitiva.", "int(x*exp(x),x)", "integral primitiva antiderivada",
            "Página 4, tecla ∫.", "Escribe la función; x ya está puesta. Ejemplo: (x − 1)eˣ."),
        g("Derivada", "Derivar una función.", "diff(sin(x)^2,x)", "derivada derivar pendiente",
            "Página 4, tecla d/dx.", "Escribe la función. Ejemplo: 2 cos x sen x."),
        g("Derivada de orden n", "Derivar varias veces.", "diff(x^3,x,2)", "derivada segunda orden",
            "Mantén pulsada d/dx y elige dⁿ/dxⁿ.", "Escribe el orden en la última casilla. Ejemplo: 6x."),
        g("Derivada parcial", "Derivar respecto de una de varias variables.", "diff(x^2*y,y)", "derivada parcial varias variables",
            "Mantén pulsada d/dx y elige ∂/∂y. Ejemplo: x²."),
        g("Límites", "Valor al que se acerca una función.", "limit(sin(x)/x,x,0)", "limite tiende",
            "Página 4, tecla lím.", "Completa función, variable y punto.", "Para límites laterales mantén pulsada lím. Ejemplo: 1."),
        g("Sumatorias", "Sumar los términos de una sucesión.", "sum(k^2,k,1,10)", "sigma sumatoria serie",
            "Página 4, tecla Σ.", "Completa el término, el índice y los límites. Ejemplo: 385."),
        g("Productorias", "Multiplicar los términos de una sucesión.", "product(k,k,1,5)", "producto productoria factorial",
            "Página 4, tecla Π.", "Ejemplo: 1·2·3·4·5 = 120."),
        g("Resolver una ecuación", "Hallar las soluciones de una ecuación.", "solve(x^2-5*x+6=0,x)", "ecuacion raices polinomio resolver",
            "Página 4, tecla resolver.", "Escribe la ecuación con el signo =.", "Ejemplo: x = 2 y x = 3."),
        g("Sistemas de ecuaciones", "Resolver varias incógnitas a la vez.", "solve([x+y+z=6,x-y=0,z=3],[x,y,z])", "sistema ecuaciones simultaneas",
            "Pulsa resolver y abre una lista con [ ] (mantén pulsada la coma).", "Separa las ecuaciones con comas y luego lista las incógnitas.", "Ejemplo: x = 3/2, y = 3/2, z = 3."),
        g("Inecuaciones", "Resolver una desigualdad.", "solve(x^2-4>=0,x)", "inecuacion desigualdad mayor menor intervalo",
            "Usa la tecla = < > de la página 5 y elige ≥.", "Escríbela dentro de resolver. Ejemplo: x ≤ −2 o x ≥ 2."),
        g("Serie de Taylor", "Aproximar con un polinomio alrededor de un punto.", "taylor(sin(x),x=0,5)", "serie taylor maclaurin",
            "Página 4, tecla Taylor.", "Completa la función, el punto como x=0 y el orden."),
        g("Determinante", "Determinante de una matriz cuadrada.", "det([[1,2],[3,4]])", "matriz determinante",
            "Inserta una matriz con [M] y pulsa det. Ejemplo: −2."),
        g("Matriz inversa", "Inversa de una matriz.", "inv([[1,2],[3,4]])", "matriz inversa",
            "Inserta una matriz y pulsa inv en la página 6."),
        g("Autovalores", "Autovalores de una matriz.", "eigenvals([[2,1],[1,2]])", "matriz autovalores valores propios eigen",
            "Inserta una matriz y pulsa autoval. Mantén pulsada para autovectores. Ejemplo: 3 y 1."),
        g("Sistema lineal", "Resolver ecuaciones lineales.", "linsolve([x+y=3,x-y=1],[x,y])", "sistema lineal matriz",
            "Página 5, tecla sist.lin.", "Completa la lista de ecuaciones y la de incógnitas. Ejemplo: [2, 1]."),
        g("Producto vectorial", "Producto cruz de vectores 3D.", "cross([1,2,3],[4,5,6])", "vector cruz vectorial",
            "Página 6, tecla cruz. Ejemplo: [−3, 6, −3]."),
        g("Producto escalar", "Producto punto de vectores.", "dot([1,2,3],[4,5,6])", "vector punto escalar",
            "Página 6, tecla punto. Ejemplo: 32."),
        g("Norma de un vector", "Longitud de un vector.", "norm([3,4])", "vector norma modulo longitud",
            "Mantén pulsada punto y elige norma. Ejemplo: 5."),
        g("Números complejos", "Usar i, módulo, argumento y conjugado.", "abs(3+4*i)", "complejo imaginario modulo",
            "Pulsa i en la página 6; mantén pulsada para Re, Im, arg y conjugado. Ejemplo: |3 + 4i| = 5."),
        g("Forma polar", "Pasar un complejo a coordenadas polares.", "polar_coordinates(1+i)", "complejo polar angulo",
            "Escribe el número y pulsa ▶polar. Ejemplo: [√2, π/4]."),
        g("Transformada de Laplace", "Pasar una función del tiempo al dominio s.", "laplace(sin(2*t),t,s)", "laplace transformada",
            "Página 4, tecla Laplace. Ejemplo: 2/(s² + 4)."),
        g("Laplace inversa", "Volver de s a t.", "ilaplace(1/(s^2+4),s,t)", "laplace inversa transferencia",
            "Mantén pulsada Laplace y elige Laplace⁻¹. Ejemplo: sen(2t)/2."),
        g("Transformada Z", "Pasar una sucesión al dominio z.", "ztrans(1/2^n,n,z)", "transformada z discreta",
            "Mantén pulsada Laplace y elige Z. Ejemplo: 2z/(2z − 1)."),
        g("Ecuación diferencial", "Resolver una EDO.", "desolve(y'=x*y,x,y)", "edo ecuacion diferencial",
            "Página 4, tecla EDO.", "Mantén pulsada x para obtener y'. Ejemplo: c₀·e^(x²/2)."),
        g("Conversión de unidades", "Pasar km/h a m/s con factores exactos.", "72*1000/3600", "unidades convertir velocidad longitud",
            "Abre Modos, Unidades.", "Elige categoría, unidad de origen y de destino.", "La operación se envía a Inicio. Ejemplo: 72 km/h = 20 m/s."),
        g("Cambio de base", "Ver un entero en binario, octal o hexadecimal.", "255", "binario hexadecimal octal base",
            "Pulsa Bin-Hex en la página 6 para cambiar la base de visualización.", "Mantén pulsada para and, or, xor, not y desplazamientos."),
        g("Regresión", "Ajustar una recta a datos.", "linear_regression([1,2,3,4],[2,4,6,8.5])", "regresion ajuste lineal estadistica",
            "Abre Estadística, pestaña 2 variables, y escribe X e Y.", "Elige el tipo de regresión y lee los coeficientes y R²."),
        g("Ans y variables", "Reusar el último resultado y guardar valores.", "(2+3)*2", "ans anterior variable guardar",
            "Ans inserta el resultado anterior.", "Guarda un valor con :=, por ejemplo A:=5.", "Las variables se conservan entre sesiones."),
        g("Guardar una variable", "Guardar un número en una letra.", "A:=5", "variable asignar guardar",
            "Mantén pulsada x para ver las letras.", "Elige A, luego := y el valor."),
        g("Escribir un programa", "Definir tu propia función.", "f(x):=x^2+1", "programa funcion definir",
            "Abre Modos, Programas, y elige una plantilla.", "Pulsa Ejecutar; la función queda disponible en Inicio."),
        g("Gráfica: raíz", "Dónde una función corta al eje x.", "fsolve(cos(x)=x,x=0.7)", "grafica raiz cero corte",
            "En Gráfica, toca cerca del corte y pulsa Raíz. Ejemplo: 0,739085..."),
        g("Gráfica: mínimo", "Ubicar un mínimo.", "fsolve(diff(x^2-4*x,x)=0,x=1)", "grafica minimo maximo extremo",
            "Toca cerca del mínimo y pulsa Mín. Ejemplo: x = 2."),
        g("Gráfica: intersección", "Dónde dos funciones son iguales.", "fsolve(x^2=x+2,x=1)", "grafica interseccion cruce",
            "Escribe dos funciones, toca cerca del cruce y pulsa Intersección. Ejemplo: x = 2."),
        g("Regla de tres y proporciones", "Resolver una proporción con una incógnita.", "solve(3/4=x/20,x)", "regla de tres proporcion razon",
            "Escribe ambas razones como fracciones con = entre ellas.", "Resuelve para x. Ejemplo: 15."),
        g("Residuo, MCD y MCM", "Divisibilidad básica.", "irem(17,5)", "residuo modulo mcd mcm divisor multiplo",
            "Página 3: mod, MCD y MCM.", "Ejemplo: 17 mod 5 = 2; MCD(12,18) = 6; MCM(12,18) = 36.")
    )

    fun norm(t: String) = t.lowercase().replace('á', 'a').replace('é', 'e').replace('í', 'i').replace('ó', 'o').replace('ú', 'u').replace('ñ', 'n')

    fun search(q: String): List<GuideEntry> {
        val words = norm(q).split(' ').filter { it.isNotBlank() }
        if (words.isEmpty()) return entries
        return entries.filter { e -> val h = norm(e.title + " " + e.goal + " " + e.tags); words.all { h.contains(it) } }
    }
}

class UnitDef(val name: String, val factor: String)
class UnitCat(val name: String, val units: List<UnitDef>)

object Units {
    private fun u(n: String, f: String) = UnitDef(n, f)
    val cats = listOf(
        UnitCat("Longitud", listOf(u("m", "1"), u("km", "1000"), u("cm", "1/100"), u("mm", "1/1000"), u("µm", "1/1000000"), u("in", "254/10000"), u("ft", "3048/10000"), u("yd", "9144/10000"), u("mi", "1609344/1000"), u("nmi", "1852"))),
        UnitCat("Masa", listOf(u("kg", "1"), u("g", "1/1000"), u("mg", "1/1000000"), u("t", "1000"), u("lb", "45359237/100000000"), u("oz", "28349523125/1000000000000"))),
        UnitCat("Tiempo", listOf(u("s", "1"), u("ms", "1/1000"), u("µs", "1/1000000"), u("min", "60"), u("h", "3600"), u("día", "86400"))),
        UnitCat("Velocidad", listOf(u("m/s", "1"), u("km/h", "1000/3600"), u("mph", "1609344/3600000"), u("kn", "1852/3600"))),
        UnitCat("Fuerza", listOf(u("N", "1"), u("kN", "1000"), u("lbf", "44482216152605/10000000000000"), u("kgf", "980665/100000"))),
        UnitCat("Presión", listOf(u("Pa", "1"), u("kPa", "1000"), u("MPa", "1000000"), u("bar", "100000"), u("atm", "101325"), u("psi", "6894757293168/1000000000"), u("mmHg", "133322387415/1000000000"))),
        UnitCat("Energía", listOf(u("J", "1"), u("kJ", "1000"), u("Wh", "3600"), u("kWh", "3600000"), u("cal", "4184/1000"), u("kcal", "4184"), u("eV", "1602176634/10000000000000000000000000000"))),
        UnitCat("Potencia", listOf(u("W", "1"), u("kW", "1000"), u("hp", "7456998715822702/10000000000000"))),
        UnitCat("Ángulo", listOf(u("rad", "1"), u("°", "pi/180"), u("g (cent.)", "pi/200"), u("vuelta", "2*pi"), u("′ (minuto)", "pi/10800"))),
        UnitCat("Frecuencia", listOf(u("Hz", "1"), u("kHz", "1000"), u("MHz", "1000000"), u("GHz", "1000000000"), u("rpm", "1/60"))),
        UnitCat("Temperatura", listOf(u("°C", "C"), u("°F", "F"), u("K", "K")))
    )

    fun convert(v: String, cat: UnitCat, from: UnitDef, to: UnitDef): String {
        if (cat.name == "Temperatura") {
            val k = when (from.factor) { "C" -> "($v)+273.15"; "F" -> "(($v)-32)*5/9+273.15"; else -> "($v)" }
            return when (to.factor) { "C" -> "($k)-273.15"; "F" -> "(($k)-273.15)*9/5+32"; else -> k }
        }
        return "($v)*(${from.factor})/(${to.factor})"
    }
}

class ConstDef(val name: String, val sym: String, val value: String, val unit: String)

object Consts {
    val all = listOf(
        ConstDef("Velocidad de la luz", "c", "299792458", "m/s"),
        ConstDef("Constante de Planck", "h", "6.62607015e-34", "J s"),
        ConstDef("Planck reducida", "hbar", "1.054571817e-34", "J s"),
        ConstDef("Carga elemental", "e", "1.602176634e-19", "C"),
        ConstDef("Constante de Boltzmann", "kB", "1.380649e-23", "J/K"),
        ConstDef("Número de Avogadro", "NA", "6.02214076e23", "1/mol"),
        ConstDef("Constante de los gases", "R", "8.314462618", "J/(mol K)"),
        ConstDef("Gravedad estándar", "g", "9.80665", "m/s2"),
        ConstDef("Gravitación universal", "G", "6.6743e-11", "m3/(kg s2)"),
        ConstDef("Permitividad del vacío", "eps0", "8.8541878128e-12", "F/m"),
        ConstDef("Permeabilidad del vacío", "mu0", "1.25663706212e-6", "N/A2"),
        ConstDef("Stefan y Boltzmann", "sigma", "5.670374419e-8", "W/(m2 K4)"),
        ConstDef("Masa del electrón", "me", "9.1093837015e-31", "kg"),
        ConstDef("Masa del protón", "mp", "1.67262192369e-27", "kg"),
        ConstDef("Constante de Faraday", "F", "96485.33212", "C/mol"),
        ConstDef("Atmósfera estándar", "atm", "101325", "Pa"),
        ConstDef("Estructura fina", "alpha", "7.2973525693e-3", "")
    )
}

object Templates {
    val programs = listOf(
        "Factorial" to "fact(n):={\n  local k,r;\n  r:=1;\n  for k from 1 to n do r:=r*k; od;\n  return r;\n};\nfact(5)",
        "Suma de cifras" to "digsum(n):={\n  local s;\n  s:=0;\n  while n>0 do s:=s+irem(n,10); n:=iquo(n,10); od;\n  return s;\n};\ndigsum(12345)",
        "Bisección" to "bisect(f,a,b):={\n  local m,k;\n  for k from 1 to 40 do\n    m:=(a+b)/2;\n    if f(a)*f(m)<=0 then b:=m; else a:=m; fi;\n  od;\n  return evalf(m);\n};\ng(x):=x^2-2;\nbisect(g,1,2)"
    )
}

class Dist(val name: String, val params: List<String>, val pdf: String, val cdf: String, val inv: String)

object Dists {
    val all = listOf(
        Dist("Normal", listOf("μ (media)", "σ (desviación)"), "normald", "normald_cdf", "normald_icdf"),
        Dist("t de Student", listOf("n"), "student", "student_cdf", "student_icdf"),
        Dist("Ji cuadrado", listOf("n"), "chisquare", "chisquare_cdf", "chisquare_icdf"),
        Dist("F de Fisher", listOf("n", "m"), "fisher", "fisher_cdf", "fisher_icdf"),
        Dist("Binomial", listOf("n", "p"), "binomial", "binomial_cdf", "binomial_icdf"),
        Dist("Poisson", listOf("λ"), "poisson", "poisson_cdf", "poisson_icdf")
    )
}

class FDef(val name: String, val argc: Int, val desc: String)

object Aliases {
    /** Palabras en español (sin tildes) que llevan a la función de Giac. */
    val map = mapOf(
        "integral" to "int", "integrar" to "int", "derivada" to "diff", "derivar" to "diff", "raiz cubica" to "surd", "raiz enesima" to "surd", "raiz" to "sqrt",
        "logaritmo" to "logb", "determinante" to "det", "inversa" to "inv", "autovalor" to "eigenvals", "autovector" to "eigenvects",
        "regresion" to "linear_regression", "desviacion" to "stddev", "promedio" to "mean", "media" to "mean", "mediana" to "median", "combinacion" to "comb", "permutacion" to "perm",
        "residuo" to "irem", "resto" to "irem", "cociente" to "iquo", "primo" to "isprime", "factores primos" to "ifactor", "valor absoluto" to "abs", "aleatorio" to "rand", "sumatoria" to "sum",
        "productoria" to "product", "serie" to "taylor", "diferencial" to "desolve", "edo" to "desolve", "ecuacion" to "solve", "resolver" to "solve", "sistema" to "linsolve",
        "producto vectorial" to "cross", "producto escalar" to "dot", "norma" to "norm", "modulo" to "abs", "transformada z" to "ztrans", "mcd" to "gcd", "maximo comun" to "gcd",
        "mcm" to "lcm", "minimo comun" to "lcm", "simplificar" to "simplify", "factorizar" to "factor", "expandir" to "expand", "sustituir" to "subst", "limite" to "limit",
        "seno" to "sin", "coseno" to "cos", "tangente" to "tan", "redondear" to "round", "piso" to "floor", "techo" to "ceil", "factorial" to "factorial", "fraccion" to "exact",
        "laplace" to "laplace", "fourier" to "fourier", "raices" to "roots", "aproximar" to "evalf", "decimal" to "evalf", "transpuesta" to "transpose", "traza" to "trace"
    )
}

/** Descripciones en español de las funciones más usadas. Las demás se muestran como función avanzada. */
object FuncEs {
    val desc = mapOf(
        "abs" to "Valor absoluto o módulo", "sqrt" to "Raíz cuadrada", "surd" to "Raíz real de índice n: surd(a, n)", "exp" to "Exponencial eˣ",
        "ln" to "Logaritmo natural", "log10" to "Logaritmo decimal", "logb" to "Logaritmo en base b: logb(número, base)", "sin" to "Seno", "cos" to "Coseno",
        "tan" to "Tangente", "cot" to "Cotangente", "sec" to "Secante", "csc" to "Cosecante", "asin" to "Arco seno", "acos" to "Arco coseno", "atan" to "Arco tangente",
        "sinh" to "Seno hiperbólico", "cosh" to "Coseno hiperbólico", "tanh" to "Tangente hiperbólica", "int" to "Integral: int(f, x) o int(f, x, a, b)",
        "diff" to "Derivada: diff(f, x) o diff(f, x, n)", "limit" to "Límite: limit(f, x, a)", "sum" to "Sumatoria: sum(término, k, inicio, fin)",
        "product" to "Productoria: product(término, k, inicio, fin)", "solve" to "Resuelve ecuaciones e inecuaciones", "csolve" to "Resuelve incluyendo soluciones complejas",
        "fsolve" to "Resuelve en forma aproximada cerca de un valor", "desolve" to "Resuelve ecuaciones diferenciales", "linsolve" to "Resuelve un sistema lineal",
        "simplify" to "Simplifica una expresión", "factor" to "Factoriza un polinomio", "expand" to "Desarrolla productos y potencias", "partfrac" to "Fracciones parciales",
        "subst" to "Sustituye una variable por un valor", "roots" to "Raíces de un polinomio con su multiplicidad", "taylor" to "Serie de Taylor", "series" to "Desarrollo en serie",
        "det" to "Determinante de una matriz", "inv" to "Inversa", "transpose" to "Transpuesta", "rref" to "Forma escalonada reducida", "trace" to "Traza de una matriz",
        "eigenvals" to "Autovalores", "eigenvects" to "Autovectores", "dot" to "Producto escalar", "cross" to "Producto vectorial", "norm" to "Norma de un vector",
        "comb" to "Combinaciones C(n, r)", "perm" to "Permutaciones P(n, r)", "factorial" to "Factorial n!", "irem" to "Residuo de la división entera", "iquo" to "Cociente entero",
        "gcd" to "Máximo común divisor", "lcm" to "Mínimo común múltiplo", "ifactor" to "Descomposición en factores primos", "isprime" to "¿Es primo?", "nextprime" to "Siguiente primo",
        "floor" to "Mayor entero menor o igual", "ceil" to "Menor entero mayor o igual", "round" to "Redondeo", "fPart" to "Parte decimal", "iPart" to "Parte entera",
        "rand" to "Número aleatorio", "randint" to "Entero aleatorio entre dos valores", "mean" to "Media aritmética", "median" to "Mediana", "stddev" to "Desviación estándar poblacional",
        "stddevp" to "Desviación estándar muestral", "variance" to "Varianza", "quartile1" to "Primer cuartil", "quartile3" to "Tercer cuartil", "max" to "Máximo", "min" to "Mínimo",
        "linear_regression" to "Regresión lineal", "exponential_regression" to "Regresión exponencial", "logarithmic_regression" to "Regresión logarítmica",
        "power_regression" to "Regresión potencial", "polynomial_regression" to "Regresión polinómica", "laplace" to "Transformada de Laplace", "ilaplace" to "Laplace inversa",
        "ztrans" to "Transformada Z", "fourier" to "Transformada de Fourier", "re" to "Parte real", "im" to "Parte imaginaria", "arg" to "Argumento de un complejo",
        "conj" to "Conjugado", "polar_coordinates" to "Coordenadas polares", "evalf" to "Valor decimal aproximado", "exact" to "Convierte a fracción exacta",
        "normald" to "Densidad normal", "normald_cdf" to "Probabilidad acumulada normal", "binomial" to "Probabilidad binomial", "poisson" to "Probabilidad de Poisson",
        "sign" to "Signo de un número", "numer" to "Numerador", "denom" to "Denominador", "convert" to "Convierte a otra forma o base", "size" to "Cantidad de elementos",
        "sort" to "Ordena una lista", "seq" to "Genera una lista con una fórmula", "dms" to "Grados, minutos y segundos", "fromdms" to "De grados, minutos y segundos a decimal"
    )
}
