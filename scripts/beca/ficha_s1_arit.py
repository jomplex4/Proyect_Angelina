"""Ficha de Aritmética, semana 1, de Junior Burga: números racionales e irracionales. 30 preguntas, 5 alternativas."""
from fractions import Fraction
from model import ficha
F = ficha(1, "arit")
LAB = dict(letters="abcde")

def fr(m):
    f = Fraction(m.replace(",", "."))
    return "%d/%d" % (f.numerator, f.denominator)

def sci(m, e, N):
    """Condición: m×10^e es el número N y m está en notación científica, 1 <= m < 10."""
    a = fr(m)
    return "(%s)*10^(%d)==%s and %s>=1 and %s<10" % (a, e, N, a, a)

def sciR(m, e, X, tol):
    """m×10^e redondea a X con la tolerancia dada, en las unidades de 10^e."""
    return "abs((%s)*10^(%d)-(%s))<%s*10^(%d)" % (fr(m), e, X, tol, e)

FRAC = "Una fracción se calcula con denominador común. Un decimal exacto es una fracción con denominador 10, 100, 1000, …"
RAIZ = "Una raíz exacta se obtiene factorizando: √(a/b) = √a/√b y la raíz cúbica de a/b es la raíz cúbica de a entre la de b."
NC = "Notación científica: a × 10ⁿ con 1 ≤ a < 10. Cada lugar que se mueve la coma hacia la izquierda suma 1 al exponente y cada lugar hacia la derecha resta 1."

F(1, "frac", 1, "Efectúa:\n§(1/3)^2+7/9", ["8/9", "7/9", "9/8", "1", "9/7"], ["8/9", "7/9", "9/8", "1", "9/7"], "(1/3)^2+7/9",
  FRAC, "Se eleva la fracción al cuadrado y se suma con el mismo denominador.",
  ["(1/3)² = 1/9.", "1/9 + 7/9 = 8/9."], "8/9 ≈ 0,889 y 1/9 + 7/9 = 0,111 + 0,778.", ok="a", **LAB,
  mistake="Elevar solo el numerador: 1/3² = 1/9 sale igual aquí, pero con otras fracciones falla. Hay que elevar numerador y denominador.")

F(2, "frac", 1, "Calcula el valor de \"x\":\n§x+sqrt(1/25)=sqrt(16/25)", ["1", "2", "1/2", "3/5", "5/3"], ["1", "2", "1/2", "3/5", "5/3"], "solve(x+sqrt(1/25)=sqrt(16/25),x)[0]",
  RAIZ, "Se calculan las dos raíces y se despeja x.",
  ["√(1/25) = 1/5 y √(16/25) = 4/5.", "x + 1/5 = 4/5.", "x = 4/5 − 1/5 = 3/5."], "3/5 + 1/5 = 4/5.", ok="d", **LAB,
  mistake="Restar mal: 4/5 − 1/5 = 3/5, no 3.")

F(3, "frac", 1, "Efectúa:\n(0,5)² + √0,64", ["1,05", "1,10", "1,15", "1", "1,04"], ["21/20", "11/10", "23/20", "1", "26/25"], "(1/2)^2+sqrt(16/25)",
  FRAC + " " + RAIZ, "0,5 = 1/2 y 0,64 = 64/100.",
  ["(0,5)² = 0,25.", "√0,64 = 0,8, porque 0,8·0,8 = 0,64.", "0,25 + 0,8 = 1,05."], "0,25 + 0,80 = 1,05.", ok="a", **LAB,
  note="Las alternativas se comprueban como fracciones: 1,05 es 21/20.",
  mistake="Tomar √0,64 = 0,08. La raíz de 0,64 es 0,8.")

F(4, "frac", 2, "Efectúa:\n§(surd(125/64,3))^2+sqrt(100/256)", ["33/16", "35/16", "37/16", "39/16", "41/16"], ["33/16", "35/16", "37/16", "39/16", "41/16"],
  "(exact(evalf((125/64)^(1/3))))^2+sqrt(100/256)", RAIZ, "Las dos raíces son exactas: 125 = 5³, 64 = 4³, 100 = 10² y 256 = 16².",
  ["La raíz cúbica de 125/64 es 5/4. Su cuadrado es 25/16.", "√(100/256) = 10/16.", "25/16 + 10/16 = 35/16."], "35/16 = 2,1875 y 1,5625 + 0,625.", ok="b", **LAB,
  mistake="Calcular la raíz cúbica de 125/64 como 25/16, sin simplificar. Ese número es el cuadrado.")

F(5, "frac", 2, "Si: P = (0,02)² − 5 × 0,3 + (0,3)²\nCalcula el valor de: P + 0,4096", ["−1", "−2", "−3", "−4", "−5"], ["-1", "-2", "-3", "-4", "-5"],
  "(2/100)^2-5*3/10+(3/10)^2+4096/10000", FRAC, "Se calcula P y luego se suma 0,4096.",
  ["(0,02)² = 0,0004 y (0,3)² = 0,09.", "5 × 0,3 = 1,5.", "P = 0,0004 − 1,5 + 0,09 = −1,4096.", "P + 0,4096 = −1,4096 + 0,4096 = −1."], "−1,4096 + 0,4096 = −1,0000.", ok="a", **LAB,
  mistake="Olvidar que 5 × 0,3 resta: sumarlo daría un número positivo.")

F(6, "frac", 2, "Calcula: (0, 3)² − 2 + (0, 4)²", ["389/225", "383/225", "387/225", "381/225", "385/225"], ["389/225", "383/225", "387/225", "381/225", "385/225"],
  "(3/10)^2-2+(4/10)^2", FRAC, "Se calcula cada cuadrado y se suma.",
  ["(0,3)² = 0,09 y (0,4)² = 0,16.", "0,09 − 2 + 0,16 = −1,75 = −7/4.", "Ninguna alternativa vale −7/4."], "0,09 + 0,16 = 0,25 y 0,25 − 2 = −1,75.",
  answer="−7/4", answerVal="-7/4",
  warn="Con el enunciado tal cual, el resultado es −7/4 y ninguna alternativa lo recoge. La ficha tiene una errata.",
  note="Si el 3 fuera periódico, es decir 0,333…, que vale 1/3, el resultado sería (1/3)² − 2 + (0,4)² = −389/225. Eso coincide con la alternativa a salvo el signo. Es solo una hipótesis y la academia debe confirmar el enunciado.",
  review=True, **LAB, mistake="Marcar la alternativa a por el parecido del número. El valor es negativo y no coincide.")

F(7, "frac", 1, "Calcula el valor de \"x\" en:\n§x+(1/2)^2+3/4", ["0,5", "0,6", "0,7", "0,8", "0,9"], ["1/2", "3/5", "7/10", "4/5", "9/10"],
  "(1/2)^2+3/4", "Una ecuación necesita dos miembros: sin el segundo no se puede despejar x.", "Los términos conocidos suman 1, pero falta el valor al que debe ser igual toda la expresión.",
  ["La expresión es x + (1/2)² + 3/4 = x + 1/4 + 3/4 = x + 1.", "No hay un signo igual ni un valor a la derecha: x queda sin determinar."], "Si la expresión fuera igual a 1,5, x = 0,5; si fuera igual a 1,6, x = 0,6. Cada alternativa corresponde a un valor distinto.",
  answer="No se puede determinar: falta el segundo miembro de la ecuación. Los términos conocidos suman 1.", answerVal="1",
  warn="A la ficha le falta el segundo miembro de la ecuación. Con los datos dados, x no queda determinado.", review=True, **LAB,
  mistake="Suponer un valor para el segundo miembro y elegir una alternativa sin que el enunciado la respalde.")

F(8, "frac", 1, "Efectúa:\n§sqrt(4/9)+sqrt(25/9)", ["7/3", "7/4", "7/5", "7/6", "1"], ["7/3", "7/4", "7/5", "7/6", "1"], "sqrt(4/9)+sqrt(25/9)",
  RAIZ, "Las dos raíces son exactas.", ["√(4/9) = 2/3 y √(25/9) = 5/3.", "2/3 + 5/3 = 7/3."], "7/3 ≈ 2,33 y 0,67 + 1,67.", ok="a", **LAB,
  mistake="Sumar los radicandos antes de sacar la raíz: √(29/9) no es 7/3.")

F(9, "frac", 1, "Efectúa:\n§surd(0.027,3)-(0.2)^3", ["0,290", "0,291", "0,292", "0,293", "0,294"], ["290/1000", "291/1000", "292/1000", "293/1000", "294/1000"],
  "exact(evalf((27/1000)^(1/3)))-(1/5)^3", RAIZ, "0,027 = 27/1000 y 27 = 3³, 1000 = 10³.",
  ["La raíz cúbica de 0,027 es 0,3.", "(0,2)³ = 0,008.", "0,3 − 0,008 = 0,292."], "0,292 + 0,008 = 0,300.", ok="c", **LAB,
  note="En la fórmula los decimales se escriben con punto; en la ficha llevan coma.",
  mistake="Calcular (0,2)³ = 0,06, multiplicando 0,2 por 3.")

F(10, "frac", 2, "Efectúa:\n§(2/3)^2+(sqrt(16/81))^(-1)", ["1", "2", "3", "4", "5"], ["1", "2", "3", "4", "5"], "(2/3)^2+(sqrt(16/81))^(-1)",
  "Un exponente −1 invierte la fracción. " + RAIZ, "Se calcula cada sumando y se suman.",
  ["(2/3)² = 4/9.", "√(16/81) = 4/9 y su inverso es 9/4.", "4/9 + 9/4 = 16/36 + 81/36 = 97/36 ≈ 2,69."], "97/36 = 2,694, que no es entero.",
  answer="97/36, aproximadamente 2,69", answerVal="97/36",
  warn="Con el enunciado tal cual, el resultado es 97/36 y ninguna alternativa lo recoge. La ficha tiene una errata.", review=True, **LAB,
  mistake="Redondear 2,69 a 3 y marcar la alternativa c. El resultado exacto no es 3.")

F(11, "frac", 2, "Calcula el valor de la siguiente expresión:\nA = (0,5)² ÷ (0,1) + √0,64 ÷ 2 − 0,02", ["2,88", "2,90", "2,50", "2,95", "2,80"], ["288/100", "290/100", "250/100", "295/100", "280/100"],
  "(1/2)^2/(1/10)+sqrt(64/100)/2-2/100", FRAC + " Dividir entre 0,1 es multiplicar por 10.", "Se hace cada operación y se suman los resultados con signo.",
  ["(0,5)² ÷ 0,1 = 0,25 ÷ 0,1 = 2,5.", "√0,64 ÷ 2 = 0,8 ÷ 2 = 0,4.", "A = 2,5 + 0,4 − 0,02 = 2,88."], "2,5 + 0,4 = 2,9 y 2,9 − 0,02 = 2,88.", ok="a", **LAB,
  mistake="Olvidar restar 0,02 y marcar 2,90, alternativa b.", trap="290/100", trap_opt="b")

F(12, "frac", 3, "Calcula el resultado de la siguiente expresión:\n§M=((((5/6-1/3)^2+1)/(1/24)))/sqrt(5/4-5/9)\ny luego da como resultado la raíz cuadrada de M.",
  ["6", "7", "8", "9", "10"], ["6", "7", "8", "9", "10"], "sqrt((((5/6-1/3)^2+1)/(1/24))/sqrt(5/4-5/9))",
  FRAC + " Dividir entre una fracción es multiplicar por su inversa.", "Se resuelve primero el numerador, luego el denominador y al final se divide.",
  ["5/6 − 1/3 = 1/2. Su cuadrado es 1/4 y 1/4 + 1 = 5/4.", "5/4 ÷ 1/24 = 5/4 · 24 = 30.", "Denominador: 5/4 − 5/9 = 45/36 − 20/36 = 25/36, y su raíz es 5/6.", "M = 30 ÷ 5/6 = 30 · 6/5 = 36.", "La raíz cuadrada de M es 6."],
  "6² = 36 y 36 · 5/6 = 30.", ok="a", **LAB, mistake="Responder M = 36, que no está entre las alternativas. La pregunta pide la raíz cuadrada de M.")

F(13, "frac", 2, "Si:\n§M=(surd((36/100)/(6/10),3))^3\n§N=(surd(1/8,3)*sqrt(25/9))^2\nCalcula el valor de \"MxN\":",
  ["5/12", "7/12", "9/11", "11/12", "1/12"], ["5/12", "7/12", "9/11", "11/12", "1/12"], "((36/100)/(6/10))*(exact(evalf((1/8)^(1/3)))*sqrt(25/9))^2",
  RAIZ + " Elevar al cubo la raíz cúbica de un número da el mismo número.", "En M la raíz cúbica y el cubo se anulan. En N se calculan las raíces.",
  ["M = (36/100) ÷ (6/10) = 36/100 · 10/6 = 3/5.", "En N: la raíz cúbica de 1/8 es 1/2 y √(25/9) = 5/3. Su producto es 5/6 y al cuadrado 25/36.", "M × N = 3/5 · 25/36 = 75/180 = 5/12."],
  "0,6 · 0,6944 = 0,4167 = 5/12.", ok="a", **LAB, note="En la ficha, la división de M está escrita con el signo ÷ entre dos fracciones.",
  mistake="Elevar al cubo 3/5 una segunda vez, en lugar de ver que raíz cúbica y cubo se anulan.")

F(14, "arit", 1, "Determina en notación lo siguiente: la velocidad de la luz en el agua es de 225 000 km/s",
  ["2,25x10⁵ km/s", "2,25x10⁴ km/s", "22,5x10⁵ km/s", "22,5x10⁴ km/s", "2,25x10⁶ km/s"],
  [sci("2,25", 5, 225000), sci("2,25", 4, 225000), sci("22,5", 5, 225000), sci("22,5", 4, 225000), sci("2,25", 6, 225000)], "true",
  NC, "El número se escribe como a × 10ⁿ con 1 ≤ a < 10.", ["225 000 = 2,25 × 100 000.", "100 000 = 10⁵.", "La velocidad es 2,25 × 10⁵ km/s."],
  "2,25 × 100 000 = 225 000.", ok="a", **LAB, note="Las alternativas se comprueban por dos condiciones: que el valor sea 225 000 y que el primer número esté entre 1 y 10.",
  mistake="Elegir 22,5×10⁴, alternativa d. Vale lo mismo, pero 22,5 no está entre 1 y 10, así que no es notación científica.")

N15 = 73490000000000000000000
F(15, "arit", 1, "La masa, en kilogramos, de la Luna es 73 490 000 000 000 000 000 000.",
  ["7,349x10²² kg", "73,49x10²² kg", "734,9x10²² kg", "7,349x10²¹ kg", "734,9x10²⁰ kg"],
  [sci("7,349", 22, N15), sci("73,49", 22, N15), sci("734,9", 22, N15), sci("7,349", 21, N15), sci("734,9", 20, N15)], "true",
  NC, "Se cuentan los lugares que se mueve la coma hasta dejar un solo dígito antes de ella.", ["73 490 000 000 000 000 000 000 tiene 23 cifras, así que se mueve la coma 22 lugares.", "Queda 7,349 × 10²² kg."],
  "7,349 × 10²² = 73 490 000 000 000 000 000 000.", ok="a", **LAB,
  note="Las alternativas se comprueban por dos condiciones: que el valor sea el de la Luna y que el primer número esté entre 1 y 10.",
  mistake="Elegir 734,9×10²⁰, alternativa e. Vale lo mismo, pero 734,9 no está entre 1 y 10.")

N16 = "91/100000000000000000000000000000000"
F(16, "arit", 2, "Determina en notación lo siguiente: la masa en kilogramos de un electrón es de 0,000 000 000 000 000 000 000 000 000 000 91.",
  ["9,1x10⁻³¹ kg", "9,1x10⁻³² kg", "9,1x10⁻³³ kg", "9,1x10⁻³⁰ kg", "9,1x10⁻³⁴ kg"],
  [sci("9,1", -31, N16), sci("9,1", -32, N16), sci("9,1", -33, N16), sci("9,1", -30, N16), sci("9,1", -34, N16)], "true",
  NC, "Se cuentan los lugares que se mueve la coma hacia la derecha hasta dejar un dígito antes de ella.",
  ["Después de la coma hay 30 ceros antes del 91.", "Para pasar a 9,1 la coma se mueve 31 lugares hacia la derecha.", "Queda 9,1 × 10⁻³¹ kg."], "91 × 10⁻³² = 9,1 × 10⁻³¹.", ok="a", **LAB,
  note="Las alternativas se comprueban por valor y por la regla de que el primer número esté entre 1 y 10.",
  mistake="Contar 30 lugares en lugar de 31 y marcar 10⁻³⁰, alternativa d.")

F(17, "arit", 2, "Realiza la siguiente operación y expresa el resultado en notación científica con tres cifras significativas.\n(1,44 × 10⁴)(4,9 × 10⁻⁶)(2,15 × 10⁻¹²)",
  ["1,52x10⁻¹¹", "1,52x10⁻¹²", "1,52x10⁻¹³", "1,52x10⁻¹⁴", "1,52x10⁻¹⁵"],
  [sciR("1,52", e, "(144/100*10^4)*(49/10*10^(-6))*(215/100*10^(-12))", "5/1000") for e in (-11, -12, -13, -14, -15)], "true",
  NC + " Al multiplicar potencias de 10 se suman los exponentes.", "Se multiplican los números y las potencias por separado.",
  ["1,44 · 4,9 · 2,15 = 15,1704.", "Potencias: 10⁴ · 10⁻⁶ · 10⁻¹² = 10⁻¹⁴.", "15,1704 × 10⁻¹⁴ = 1,51704 × 10⁻¹³.", "Con tres cifras significativas: 1,52 × 10⁻¹³."],
  "1,44 · 4,9 = 7,056 y 7,056 · 2,15 = 15,1704.", ok="c", **LAB,
  note="Las alternativas se comprueban por cercanía: la correcta es la única que se acerca al valor real con un error menor que media unidad de la última cifra.",
  mistake="Dejar 15,17 × 10⁻¹⁴ sin pasar a notación científica y marcar 10⁻¹⁴, alternativa d.")

F(18, "arit", 1, "Se sabe que una molécula de grasa mide 0,0023  m. Al analizar una muestra, se encontró 3 millones de estas moléculas. Supón que colocamos las moléculas, una a continuación de otra, formando una cadena, ¿cuántos metros mediría dicha cadena?",
  ["6,9x10⁵ m", "6,9x10⁴ m", "6,9x10³ m", "6,9x10² m", "6,9x10 m"],
  [sci("6,9", e, 6900) for e in (5, 4, 3, 2, 1)], "true",
  NC, "La longitud de la cadena es la longitud de una molécula por la cantidad de moléculas.",
  ["3 millones = 3 000 000.", "Longitud: 0,0023 · 3 000 000 = 6900 m.", "6900 = 6,9 × 10³ m."], "0,0023 · 3 = 0,0069 y 0,0069 · 10⁶ = 6900.", ok="c", **LAB,
  note="En la ficha el exponente de las alternativas aparece sin formato, por ejemplo 6,9x103. Aquí se muestra como potencia de 10.",
  mistake="Calcular 0,0023 · 300 000 y obtener 690, es decir 6,9×10², alternativa d.")

X19 = "(30000^4*(2/100000)^5)/(500^2*25000000*(4/10000)^3)"
F(19, "arit", 3, "Expresa en notación científica. Muestra el resultado con dos cifras significativas.\n§((30000)^4*(0.00002)^5)/((500)^2*(25000000)*(0.0004)^3)",
  ["6,5x10⁻⁷", "6,5x10⁻⁸", "6,5x10⁻⁹", "6,5x10⁻¹⁰", "6,5x10⁻¹¹"], [sciR("6,5", e, X19, "5/100") for e in (-7, -8, -9, -10, -11)], "true",
  NC, "Cada número se escribe en notación científica y se agrupan las potencias de 10.",
  ["Arriba: (3×10⁴)⁴ = 81×10¹⁶ y (2×10⁻⁵)⁵ = 32×10⁻²⁵. Producto: 2592×10⁻⁹.", "Abajo: 500² = 25×10⁴, 25 000 000 = 25×10⁶ y (4×10⁻⁴)³ = 64×10⁻¹². Producto: 100 000 000 × 10⁻⁸... = 40 000 × 10⁶ × 10⁻¹²... cuyo valor es 400.", "Cociente: 2592×10⁻⁹ ÷ 400 = 6,48×10⁻⁹.", "Con dos cifras significativas: 6,5×10⁻⁹."],
  "6,48 × 10⁻⁹ redondeado a dos cifras da 6,5 × 10⁻⁹.", ok="c", **LAB,
  note="En la fórmula los decimales se escriben con punto; en la ficha llevan coma. Las alternativas se comprueban por cercanía.",
  mistake="No sumar bien los exponentes de potencias de 10 al cancelar. Un error de uno cambia la alternativa.")

X20 = "((15/1000000-8/1000000)/(3/1000000))*((20000000-500000000)/4000)"
F(20, "arit", 3, "Realiza la siguiente operación. Expresa el resultado en notación científica con tres cifras significativas.\n§(((1.5*10^(-5)-8*10^(-6))/(3*10^(-6)))*((2*10^7-5*10^8)/(4*10^3)))",
  ["−2,80x10⁵", "−2,80x10⁴", "−2,80x10³", "−2,80x10²", "−2,80x10⁶"], ["-280/100*10^(%d)==%s" % (e, X20) for e in (5, 4, 3, 2, 6)], "true",
  NC, "Cada corchete se resuelve por separado.",
  ["Primer corchete: 1,5×10⁻⁵ − 8×10⁻⁶ = 15×10⁻⁶ − 8×10⁻⁶ = 7×10⁻⁶. Dividido entre 3×10⁻⁶ da 7/3.", "Segundo corchete: 2×10⁷ − 5×10⁸ = 2×10⁷ − 50×10⁷ = −48×10⁷. Dividido entre 4×10³ da −12×10⁴.", "Producto: 7/3 · (−12×10⁴) = −28×10⁴ = −2,80×10⁵."],
  "−2,80 × 10⁵ = −280 000 y 7/3 · (−120 000) = −280 000.", ok="a", **LAB,
  note="En la fórmula el decimal 1,5 se escribe con punto; en la ficha lleva coma.",
  mistake="Restar mal 2×10⁷ − 5×10⁸ y obtener −3×10⁷. Se deben igualar las potencias antes de restar.")

F(21, "frac", 1, "En una canasta de frutas hay 5 manzanas y 7 naranjas, 12 frutas en total. Si Pedro coge 3 frutas, ¿qué parte del total representan las frutas que quedaron?",
  ["3/4", "3/5", "3/7", "3/8", "2/5"], ["3/4", "3/5", "3/7", "3/8", "2/5"], "(12-3)/12", "Una parte del total es lo que se toma entre el total.", "Pedro coge 3 de 12, pero se pregunta por las que quedan.",
  ["Quedan 12 − 3 = 9 frutas.", "Parte del total: 9/12 = 3/4."], "3/4 de 12 es 9.", ok="a", **LAB,
  mistake="Responder 3/12 = 1/4, que es lo que cogió Pedro. Esa fracción no está entre las alternativas.")

F(22, "frac", 1, "Si cortamos una cuerda de 27 metros en pedazos de 3/4 metros, ¿cuántos pedazos habría?",
  ["36", "38", "30", "42", "40"], ["36", "38", "30", "42", "40"], "27/(3/4)", "Dividir entre una fracción es multiplicar por su inversa.", "Cuántas veces cabe 3/4 en 27.",
  ["27 ÷ 3/4 = 27 · 4/3.", "27 · 4/3 = 36 pedazos."], "36 · 3/4 = 27.", ok="a", **LAB, mistake="Multiplicar 27 · 3/4 = 20,25. Cortar en pedazos pequeños da más pedazos, no menos.")

F(23, "frac", 1, "Un ciclista ha recorrido tres etapas de una carrera. En la primera etapa recorrió 283,2 km; en la segunda, 222,6 km; y en la tercera, 197,5 km. ¿Qué distancia total ha recorrido?",
  ["703,3 km", "704,4 km", "705,5 km", "702,2 km", "701,1 km"], ["7033/10", "7044/10", "7055/10", "7022/10", "7011/10"], "2832/10+2226/10+1975/10",
  "Para sumar decimales se alinean las comas.", "Se suman las tres distancias.", ["283,2 + 222,6 = 505,8.", "505,8 + 197,5 = 703,3 km."], "703,3 − 197,5 = 505,8.", ok="a", **LAB,
  mistake="Desalinear la coma de 197,5. Se obtiene un valor lejano.")

F(24, "frac", 1, "Nina va al mercado y realiza cinco compras de S/ 23,80; S/11,00; S/ 46,50; S/ 29,60 y S/ 27,30. ¿Cuánto dinero ha gastado en total?",
  ["138,20", "130,60", "136,20", "138,40", "138,50"], ["13820/100", "13060/100", "13620/100", "13840/100", "13850/100"], "2380/100+1100/100+4650/100+2960/100+2730/100",
  "Para sumar decimales se alinean las comas.", "Se suman las cinco compras.", ["23,80 + 11,00 = 34,80.", "34,80 + 46,50 = 81,30.", "81,30 + 29,60 = 110,90.", "110,90 + 27,30 = 138,20."], "Sumando de otra forma: 23,80 + 46,50 + 29,60 = 99,90 y 99,90 + 11 + 27,30 = 138,20.", ok="a", **LAB,
  mistake="Equivocarse con un arrastre. Las alternativas se diferencian en décimas.")

F(25, "frac", 2, "Pedro emplea 2/5 del día para trabajar; 1/74 del día, para comer; y 3/7 del día los emplea para dormir. ¿Qué fracción del día lo dedica a otras actividades?",
  ["109/840", "107/840", "109/810", "109/800", "109/830"], ["109/840", "107/840", "109/810", "109/800", "109/830"], "1-(2/5+1/74+3/7)",
  FRAC, "Lo que queda del día es 1 menos la suma de las tres partes.",
  ["Con los datos tal cual: 2/5 + 1/74 + 3/7 = 1036/2590 + 35/2590 + 1110/2590 = 2181/2590.", "Otras actividades: 1 − 2181/2590 = 409/2590.", "Ninguna alternativa vale 409/2590."], "2181/2590 + 409/2590 = 1.",
  answer="409/2590", answerVal="409/2590",
  warn="Con el enunciado tal cual, el resultado es 409/2590 y ninguna alternativa lo recoge. Parece que 1/74 es una errata.",
  note="Si la fracción fuera 1/24, el cálculo sería 2/5 + 1/24 + 3/7 = 731/840 y quedarían 109/840, que es la alternativa a. Es muy probable que ese sea el dato correcto, pero la academia debe confirmarlo.",
  review=True, **LAB, mistake="Calcular con 1/24 sin avisar del cambio. El enunciado impreso dice 1/74.")

F(26, "frac", 1, "La edad de un hijo es las 3/5 partes de la edad de su padre. Las edades de los 2 se diferencian en 16 años. ¿Cuál es la edad del padre?",
  ["20", "30", "40", "50", "60"], ["20", "30", "40", "50", "60"], "solve(p-3/5*p=16,p)[0]", "Una fracción de una cantidad se multiplica. Una diferencia se plantea con una ecuación.", "La diferencia entre padre e hijo es 2/5 de la edad del padre.",
  ["Sea p la edad del padre. El hijo tiene 3p/5.", "Diferencia: p − 3p/5 = 2p/5 = 16.", "p = 16 · 5/2 = 40."], "El hijo tiene 24 y 40 − 24 = 16.", ok="c", **LAB, mistake="Responder 24, la edad del hijo, que no está entre las alternativas.")

F(27, "frac", 2, "Tenía S/ 900, presté los 3/10 de esa cantidad, luego gasté una cantidad igual a los 4/15 de lo que presté, finalmente, invertí una cantidad igual a los 5/9 de lo que gasté. ¿Cuánto me queda?",
  ["518", "520", "525", "530", "535"], ["518", "520", "525", "530", "535"], "900-3/10*900-4/15*(3/10*900)-5/9*(4/15*(3/10*900))",
  "Cada fracción se toma de la cantidad que indica el enunciado, no siempre del total.", "Hay tres fracciones, cada una de una cantidad distinta.",
  ["Presté 3/10 de 900 = 270.", "Gasté 4/15 de 270 = 72.", "Invertí 5/9 de 72 = 40.", "Me queda 900 − 270 − 72 − 40 = 518."], "270 + 72 + 40 = 382 y 900 − 382 = 518.", ok="a", **LAB,
  mistake="Tomar las fracciones 4/15 y 5/9 de 900. Cada una es de la cantidad anterior.")

F(28, "frac", 2, "Ordena en forma creciente los siguientes números irracionales e indica el mayor:\ne ; −√2 ; √7 ; −ϕ",
  ["2,70", "−1,414", "2,645", "−1,618", "2,718"],
  ["abs(270/100-evalf(max(exp(1),sqrt(7),-sqrt(2),-(1+sqrt(5))/2)))<1/2000", "abs(-1414/1000-evalf(max(exp(1),sqrt(7),-sqrt(2),-(1+sqrt(5))/2)))<1/2000",
   "abs(2645/1000-evalf(max(exp(1),sqrt(7),-sqrt(2),-(1+sqrt(5))/2)))<1/2000", "abs(-1618/1000-evalf(max(exp(1),sqrt(7),-sqrt(2),-(1+sqrt(5))/2)))<1/2000",
   "abs(2718/1000-evalf(max(exp(1),sqrt(7),-sqrt(2),-(1+sqrt(5))/2)))<1/2000"], "true",
  "Para ordenar números irracionales se usan sus valores decimales aproximados. Los negativos son menores que los positivos, y entre negativos es menor el que está más lejos de cero.",
  "Se calcula cada valor con tres decimales y se comparan.",
  ["e ≈ 2,718; √7 ≈ 2,646; −√2 ≈ −1,414; −ϕ ≈ −1,618.", "Orden creciente: −ϕ < −√2 < √7 < e.", "El mayor es e ≈ 2,718."], "2,718 > 2,646 > −1,414 > −1,618.", ok="e", **LAB,
  note="La ficha no define ϕ. Se toma como el número áureo, (1 + √5)/2 ≈ 1,618, que coincide con la alternativa d. Las alternativas se comprueban contra el valor del mayor.",
  mistake="Elegir 2,645, la raíz de 7, por ser la alternativa con una raíz. El mayor es e.")

F(29, "frac", 1, "Efectúa con aproximación a los centésimos.\nE = √3 + 1,82456…",
  ["3,55", "3,56", "3,57", "3,58", "3,59"], ["355", "356", "357", "358", "359"], "round(100*(sqrt(3)+1.82456))",
  "Aproximar a los centésimos es dejar dos decimales y mirar el tercero: si es 5 o más, se sube la segunda cifra.", "√3 ≈ 1,73205.",
  ["√3 ≈ 1,73205.", "E ≈ 1,73205 + 1,82456 = 3,55661.", "El tercer decimal es 6, así que se sube: 3,56."], "3,56 − 3,55661 = 0,00339, menos de media centésima.", ok="b", **LAB,
  note="Las alternativas se comprueban multiplicadas por 100.", mistake="Cortar sin redondear y marcar 3,55, alternativa a.", trap="355", trap_opt="a")

F(30, "frac", 3, "El resultado con aproximación al centésimo del siguiente ejercicio es:\nM = π/2 + √5 × √7 − ϕ × √6",
  ["3,51", "3,52", "3,53", "3,54", "3,55"], ["351", "352", "353", "354", "355"], "round(100*(pi/2+sqrt(5)*sqrt(7)-(1+sqrt(5))/2*sqrt(6)))",
  "Se calculan los cuatro valores con varios decimales para que el redondeo final sea correcto.", "√5·√7 = √35 y ϕ es el número áureo.",
  ["π/2 ≈ 1,57080 y √5·√7 = √35 ≈ 5,91608.", "ϕ ≈ 1,61803 y √6 ≈ 2,44949, así que ϕ·√6 ≈ 3,96343.", "M ≈ 1,57080 + 5,91608 − 3,96343 = 3,52345.", "Con dos decimales: 3,52."], "3,52345 queda entre 3,515 y 3,525.", ok="b", **LAB,
  note="La ficha no define ϕ. Se toma como el número áureo, (1 + √5)/2. Las alternativas se comprueban multiplicadas por 100.",
  mistake="Redondear cada término antes de sumar. El error acumulado puede cambiar el último decimal.")
