"""Ficha de Álgebra, semana 2, de Jean Sanchez: productos notables y factorización. 24 problemas abiertos y 5 de Ahora hazlo tú con alternativas."""
from model import ficha
F = ficha(2, "alg")
NOT = "Productos notables: (a + b)² = a² + 2ab + b², (a − b)² = a² − 2ab + b² y (a + b)(a − b) = a² − b²."
FAC = "Factorizar es escribir un polinomio como producto de factores primos, que son los factores que ya no se pueden descomponer. Las constantes no se cuentan como factores primos."
LAB = dict(letters="abcde")

F(1, "alg", 1, "Calcula: a² + b²\nSi a + b = 5 ∧ ab = 3", [], [], "simplify(solve(t^2-5*t+3=0,t)[0]^2+solve(t^2-5*t+3=0,t)[1]^2)",
  NOT, "El cuadrado de una suma contiene a² + b² más el doble producto.",
  ["(a + b)² = a² + 2ab + b².", "Despejamos: a² + b² = (a + b)² − 2ab.", "a² + b² = 5² − 2·3 = 25 − 6 = 19."],
  "Con a y b raíces de t² − 5t + 3 = 0, la suma de sus cuadrados también da 19.",
  answer="19", answerVal="19",
  mistake="Calcular a² + b² como 5² = 25, olvidando el doble producto.")

F(2, "alg", 1, "Calcula \"ab\"\nSi:\na − b = 5 ∧ a² + b² = 17", [], [], "solve([a-b=5,a^2+b^2=17],[a,b])[0][0]*solve([a-b=5,a^2+b^2=17],[a,b])[0][1]",
  NOT, "El cuadrado de una diferencia contiene a² + b² menos el doble producto.",
  ["(a − b)² = a² − 2ab + b².", "5² = 17 − 2ab, es decir 25 = 17 − 2ab.", "2ab = 17 − 25 = −8, así que ab = −4."],
  "Con a = 4 y b = −1: a − b = 5, a² + b² = 17 y ab = −4.",
  answer="−4", answerVal="-4",
  mistake="Escribir 25 = 17 + 2ab y obtener ab = 4. En el cuadrado de una diferencia el doble producto resta.")

F(3, "alg", 2, "Calcula a − b\nSi: a + b = 4 ∧ a² + b² = 9\na < b", [], [], "-sqrt(4^2-4*((16-9)/2))",
  NOT, "Con la suma y la suma de cuadrados se halla el producto ab, y con él el cuadrado de a − b.",
  ["(a + b)² = a² + 2ab + b²: 16 = 9 + 2ab, así que ab = 7/2.",
   "(a − b)² = a² + b² − 2ab = 9 − 7 = 2.",
   "a − b = ±√2. Como a < b, la diferencia es negativa: a − b = −√2."],
  "Con a = 2 − √2/2 y b = 2 + √2/2: la suma es 4, a − b = −√2 y a² + b² = 9.",
  answer="−√2", answerVal="-sqrt(2)",
  mistake="Responder √2 sin usar la condición a < b.")

F(4, "alg", 2, "Calcula a − b\nSi a + b = 6 ∧ ab = 6; a < b", [], [], "-sqrt(6^2-4*6)",
  NOT, "(a − b)² = (a + b)² − 4ab.",
  ["(a − b)² = (a + b)² − 4ab = 36 − 24 = 12.", "a − b = ±√12 = ±2√3.", "Como a < b, a − b = −2√3."],
  "a y b son raíces de t² − 6t + 6 = 0: 3 − √3 y 3 + √3. Su diferencia es −2√3.",
  answer="−2√3", answerVal="-2*sqrt(3)",
  mistake="Olvidar el signo negativo que impone a < b.")

F(5, "alg", 2, "Calcula a − b\nSi a + b = 4 ∧ ab = 2", [], [], "sqrt(4^2-4*2)",
  NOT, "(a − b)² = (a + b)² − 4ab.",
  ["(a − b)² = 16 − 8 = 8.", "a − b = ±√8 = ±2√2.", "Sin una condición como a < b, no se sabe el signo."],
  "a y b son 2 − √2 y 2 + √2, en uno u otro orden.",
  answer="2√2 o −2√2, según cuál de los dos sea mayor", answerVal="2*sqrt(2)",
  note="A diferencia de los problemas 3 y 4, este no trae la condición a < b, así que el signo no queda determinado. La comprobación con Giac compara el valor absoluto, 2√2.",
  review=True,
  mistake="Elegir un solo signo sin tener datos para decidirlo.")

F(6, "alg", 3, "Simplifica:\n§((x+1+2*sqrt(x))*(x+1-2*sqrt(x))+(x+1)^2)/(2*(x+1+sqrt(2*x))*(x+1-sqrt(2*x)))", [], [],
  "simplify(((x+1+2*sqrt(x))*(x+1-2*sqrt(x))+(x+1)^2)/(2*(x+1+sqrt(2*x))*(x+1-sqrt(2*x))))",
  NOT + " Diferencia de cuadrados con raíces: (A + B)(A − B) = A² − B².",
  "Arriba y abajo hay productos de la forma suma por diferencia.",
  ["Arriba: (x + 1 + 2√x)(x + 1 − 2√x) = (x + 1)² − 4x. Sumado con (x + 1)² queda 2(x + 1)² − 4x = 2x² + 2.",
   "Abajo: (x + 1 + √(2x))(x + 1 − √(2x)) = (x + 1)² − 2x = x² + 1. Con el 2 de adelante: 2x² + 2.",
   "El cociente es 1."],
  "Con x = 1: arriba (4)(0)... (1 + 1 + 2)(1 + 1 − 2) + 4 = 4 y abajo 2·(2 + √2)(2 − √2) = 2·2 = 4. El cociente es 1.",
  answer="1", answerVal="1",
  mistake="Desarrollar (x + 1 + 2√x)² en vez de aprovechar la diferencia de cuadrados.")

F(7, "alg", 3, "Dado\n§a/b+b/a=1\n(a; b ≠ 0)\nCalcula:\n§(a^4+b^4)/(a^2*b^2)", [], [],
  "simplify(subs((a^4+b^4)/(a^2*b^2),[a,b],[1,exp(i*pi/3)]))",
  NOT + " (a² + b²)² = a⁴ + 2a²b² + b⁴.",
  "De la condición se obtiene a² + b² = ab. Luego se eleva al cuadrado.",
  ["a/b + b/a = (a² + b²)/(ab) = 1, así que a² + b² = ab.",
   "Elevamos al cuadrado: (a² + b²)² = a²b², es decir a⁴ + 2a²b² + b⁴ = a²b².",
   "Entonces a⁴ + b⁴ = −a²b².",
   "El cociente (a⁴ + b⁴)/(a²b²) vale −1."],
  "Con a = 1 y b = cos 60° + i·sen 60°, que cumplen a/b + b/a = 1, el cociente vale −1.",
  answer="−1", answerVal="-1",
  note="La igualdad a² + b² = ab no tiene soluciones reales con a y b distintos de 0, porque a² − ab + b² es siempre positivo. El resultado −1 vale con números complejos.",
  mistake="Concluir que no hay respuesta porque no existen reales. El procedimiento algebraico sigue siendo válido.")

F(8, "alg", 3, "Determina la expresión simplificada de:\n§(a^b+a^(-b))*(a^b-a^(-b))*(a^(4*b)+1+a^(-4*b))", [], [],
  "subs((a^b+a^(-b))*(a^b-a^(-b))*(a^(4*b)+1+a^(-4*b))-(a^(6*b)-a^(-6*b)),[a,b],[2,3])",
  NOT + " Además (u − 1/u)(u² + 1 + 1/u²) = u³ − 1/u³.",
  "Con u = a^(2b) las dos primeras llaves forman una diferencia de cuadrados y el resultado se acopla a la tercera.",
  ["(a^b + a^(−b))(a^b − a^(−b)) = a^(2b) − a^(−2b).",
   "Sea u = a^(2b). Entonces a^(4b) + 1 + a^(−4b) = u² + 1 + 1/u².",
   "(u − 1/u)(u² + 1 + 1/u²) = u³ + u + 1/u − u − 1/u − 1/u³ = u³ − 1/u³.",
   "Resultado: a^(6b) − a^(−6b)."],
  "Con a = 2 y b = 1: (2 + 1/2)(2 − 1/2)(16 + 1 + 1/16) = (15/4)(273/16) = 4095/64 y 2⁶ − 2⁻⁶ = 4095/64.",
  answer="a^(6b) − a^(−6b)", answerVal="0",
  note="La comprobación con Giac resta el resultado de la expresión original, con a = 2 y b = 3, y debe dar 0.",
  mistake="Multiplicar solo las dos primeras llaves y olvidar la tercera.")

F(9, "alg", 2, "Reduce la siguiente expresión:\n§A=(x+1)*(x-1)*(x^2-x+1)*(x^2+x+1)-x^6", [], [],
  "expand((x+1)*(x-1)*(x^2-x+1)*(x^2+x+1)-x^6)",
  NOT + " Además (x² + x + 1)(x² − x + 1) = x⁴ + x² + 1 y (x² − 1)(x⁴ + x² + 1) = x⁶ − 1.",
  "Los cuatro factores se agrupan de dos en dos.",
  ["(x + 1)(x − 1) = x² − 1.", "(x² − x + 1)(x² + x + 1) = (x² + 1)² − x² = x⁴ + x² + 1.",
   "(x² − 1)(x⁴ + x² + 1) = x⁶ − 1.", "A = x⁶ − 1 − x⁶ = −1."],
  "Con x = 2: 3·1·3·7 = 63 y 63 − 64 = −1.",
  answer="−1", answerVal="-1",
  mistake="Perder el signo de −x⁶ al restar y responder 1.")

F(10, "alg", 2, "Determina el valor de \"abc\"\nSi:\n§a+1/b=1\ny\n§b+1/c=1\nsean b ≠ 0 y c ≠ 0", [], [],
  "subs(a*b*c,[a,b,c],[2/3,3,-1/2])",
  "Se despeja a y c en función de b, y se multiplica.",
  "a y c se expresan con b, y al multiplicar los denominadores se cancelan.",
  ["De a + 1/b = 1: a = 1 − 1/b = (b − 1)/b.", "De b + 1/c = 1: 1/c = 1 − b, así que c = 1/(1 − b).",
   "abc = (b − 1)/b · b · 1/(1 − b) = (b − 1)/(1 − b) = −1."],
  "Con b = 3: a = 2/3 y c = −1/2. El producto es (2/3)·3·(−1/2) = −1.",
  answer="−1", answerVal="-1",
  mistake="Escribir (b − 1)/(1 − b) = 1 por no ver que los signos son contrarios.")

F(11, "alg", 3, "Determina el valor de \"x\" que verifica:\n§sqrt(3+sqrt(x))+sqrt(3-sqrt(x))=2*sqrt(2)", [], [],
  "simplify(solve(sqrt(3+sqrt(x))+sqrt(3-sqrt(x))=2*sqrt(2),x)[0])",
  "Para quitar raíces se eleva al cuadrado. Se usa (A + B)² = A² + 2AB + B².",
  "Al elevar al cuadrado, las raíces √x se cancelan entre sí y queda una sola raíz.",
  ["Elevamos al cuadrado: (3 + √x) + (3 − √x) + 2√((3 + √x)(3 − √x)) = 8.",
   "6 + 2√(9 − x) = 8, así que √(9 − x) = 1.",
   "9 − x = 1 y x = 8."],
  "Con x = 8: √8 = 2√2, y √(3 + 2√2) = 1 + √2 y √(3 − 2√2) = √2 − 1. Su suma es 2√2.",
  answer="8", answerVal="8",
  mistake="Elevar al cuadrado cada raíz por separado en vez de toda la suma.")

F(12, "alg", 1, "Si a + b + c = 8\nab + bc + ac = 15\nCalcula a² + b² + c²", [], [], "8^2-2*15",
  "(a + b + c)² = a² + b² + c² + 2(ab + bc + ac).",
  "El cuadrado de una suma de tres términos contiene los tres cuadrados más el doble de los productos.",
  ["64 = a² + b² + c² + 2·15.", "a² + b² + c² = 64 − 30 = 34."],
  "Con a = 1, b = (7 + √17)/2 y c = (7 − √17)/2: suman 8, los productos suman 15 y los cuadrados suman 34.",
  answer="34", answerVal="34",
  mistake="Olvidar el factor 2 y restar solo 15: 49.")

F(13, "alg", 3, "Si x + y + z = 0, calcula:\n§M=(x^3+y^3+z^3)/(x*y*z)+(x^2+y^2+z^2)/(x*y+x*z+y*z)", [], [],
  "simplify(subs((x^3+y^3+z^3)/(x*y*z)+(x^2+y^2+z^2)/(x*y+x*z+y*z),z=-x-y))",
  "Si x + y + z = 0, entonces x³ + y³ + z³ = 3xyz y x² + y² + z² = −2(xy + xz + yz).",
  "Con la condición, cada fracción se vuelve un número.",
  ["Como x + y + z = 0: x³ + y³ + z³ = 3xyz, así que la primera fracción vale 3.",
   "(x + y + z)² = 0 da x² + y² + z² = −2(xy + xz + yz), así que la segunda fracción vale −2.",
   "M = 3 − 2 = 1."],
  "Con x = 1, y = 2, z = −3: 1 + 8 − 27 = −18 = 3·(−6) y 1 + 4 + 9 = 14 = −2·(2 − 3 − 6) = 14.",
  answer="1", answerVal="1",
  note="Se supone xyz ≠ 0 y xy + xz + yz ≠ 0, para que las fracciones existan.",
  mistake="Intentar sustituir z = −x − y y desarrollar todo. El camino con las identidades es más corto.")

F(14, "alg", 3, "Si a + b + c = 11, calcula el valor de:\n§A=((a-3)^3+(b-6)^3+(c-2)^3)/((a-3)*(c-2)*(b-6))", [], [],
  "subs(((a-3)^3+(b-6)^3+(c-2)^3)/((a-3)*(c-2)*(b-6)),[a,b,c],[4,7,0])",
  "Si p + q + r = 0, entonces p³ + q³ + r³ = 3pqr.",
  "Los tres términos que se elevan al cubo suman 0.",
  ["(a − 3) + (b − 6) + (c − 2) = a + b + c − 11 = 0.",
   "Con p = a − 3, q = b − 6 y r = c − 2, p + q + r = 0 y p³ + q³ + r³ = 3pqr.",
   "A = 3pqr/(pqr) = 3."],
  "Con a = 4, b = 7 y c = 0: p = 1, q = 1 y r = −2. El numerador es 1 + 1 − 8 = −6 y el denominador −2. El cociente es 3.",
  answer="3", answerVal="3",
  mistake="No notar que los tres sumandos suman 0 y desarrollar los cubos.")

F(15, "alg", 1, "Factoriza:\nP(m, n) = 3mn − 6m² + 12m", [], [], "factor(3*m*n-6*m^2+12*m)",
  FAC + " El factor común es el mayor monomio que divide a todos los términos.",
  "Los tres términos tienen 3 y m en común.",
  ["Coeficientes: 3, 6 y 12. Su máximo común divisor es 3.", "La letra m aparece en todos los términos con exponente al menos 1.",
   "Factor común: 3m. Dividimos: 3mn/3m = n, 6m²/3m = 2m y 12m/3m = 4.", "P = 3m(n − 2m + 4)."],
  "Al multiplicar: 3m·n − 3m·2m + 3m·4 = 3mn − 6m² + 12m.",
  answer="P = 3m(n − 2m + 4)", answerVal="factor(3*m*(n-2*m+4))",
  mistake="Olvidar dividir también el signo: −6m²/3m = −2m.")

F(16, "alg", 2, "Factoriza:\nP(x, y) = 3x²y³ + 6x³y² + 9x⁴y\ne indica la cantidad de factores primos.", [], [], "size(factors(3*x^2*y^3+6*x^3*y^2+9*x^4*y))/2-1",
  FAC, "Se saca el factor común y se revisa si el trinomio que queda se puede factorizar más.",
  ["Factor común: 3x²y. Dividimos: y², 2xy y 3x².", "P = 3x²y(y² + 2xy + 3x²).",
   "El trinomio y² + 2xy + 3x² no se factoriza con enteros: su discriminante en y es 4x² − 12x² < 0.",
   "Factores primos distintos: x, y y y² + 2xy + 3x². Son 3."],
  "El 3 es constante y no cuenta. La x aparece al cuadrado, pero es un solo factor primo.",
  answer="3 factores primos: x, y y y² + 2xy + 3x²", answerVal="3",
  note="Se cuentan factores primos distintos y sin la constante, como es costumbre en esta clase de ejercicios.",
  mistake="Contar x² como dos factores o contar el 3. Ninguna de las dos cosas se hace con los factores primos distintos.")

F(17, "alg", 2, "Factoriza:\nP(x, y) = 2x² + y² − xy − 2xy", [], [], "factor(2*x^2+y^2-x*y-2*x*y)",
  FAC + " Para un trinomio, se usa el método del aspa simple.",
  "Primero se reducen los términos semejantes: −xy − 2xy = −3xy.",
  ["P = 2x² − 3xy + y².", "Aspa simple: 2x² = (2x)(x) y y² = (−y)(−y).", "Los productos cruzados: (2x)(−y) + (x)(−y) = −3xy, que coincide.", "P = (2x − y)(x − y)."],
  "(2x − y)(x − y) = 2x² − 2xy − xy + y² = 2x² − 3xy + y².",
  answer="P = (2x − y)(x − y)", answerVal="factor((2*x-y)*(x-y))",
  mistake="No reducir −xy − 2xy y tratar el polinomio como si tuviera cuatro términos.")

F(18, "alg", 2, "Factoriza:\nP(m, n) = 3m² + n² − mn − 3mn\ne indica el factor primo con mayor suma de coeficientes.", [], [], "factor(3*m^2+n^2-m*n-3*m*n)",
  FAC + " La suma de coeficientes de un factor se obtiene reemplazando todas las letras por 1.",
  "Se reduce, se factoriza y luego se evalúa cada factor en 1.",
  ["Reducimos: −mn − 3mn = −4mn. P = 3m² − 4mn + n².", "Aspa simple: 3m² = (3m)(m) y n² = (−n)(−n). Cruzado: (3m)(−n) + (m)(−n) = −4mn.",
   "P = (3m − n)(m − n).", "Suma de coeficientes: de 3m − n es 3 − 1 = 2 y de m − n es 1 − 1 = 0. El mayor es 2."],
  "(3m − n)(m − n) = 3m² − 3mn − mn + n² = 3m² − 4mn + n².",
  answer="3m − n", answerVal="factor((3*m-n)*(m-n))",
  note="La comprobación con Giac verifica la factorización; la elección del factor está explicada en los pasos.",
  mistake="Elegir m − n por ser el primero que aparece. Su suma de coeficientes es 0.")

F(19, "alg", 2, "Factoriza:\nP(x) = 6x² − x − 2\nindica la suma de factores primos.", [], [], "expand(factors(6*x^2-x-2)[0]+factors(6*x^2-x-2)[2])",
  FAC + " Se usa el aspa simple.",
  "6x² se descompone como (3x)(2x) o (6x)(x), y −2 como (−2)(1) o (2)(−1).",
  ["Probamos (3x)(2x) con (−2)(1): cruzado 3x·1 + 2x·(−2) = 3x − 4x = −x. Coincide.", "P = (3x − 2)(2x + 1).",
   "Suma de factores: (3x − 2) + (2x + 1) = 5x − 1."],
  "(3x − 2)(2x + 1) = 6x² + 3x − 4x − 2 = 6x² − x − 2.",
  answer="5x − 1", answerVal="5*x-1",
  mistake="Sumar los coeficientes de x y olvidar los términos independientes: 5x.")

F(20, "alg", 1, "Factoriza:\nP(x) = x² − 3x − 40\ne indica el factor primo con mayor término independiente.", [], [],
  "when(subs(factors(x^2-3*x-40)[0],x=0)>subs(factors(x^2-3*x-40)[2],x=0),factors(x^2-3*x-40)[0],factors(x^2-3*x-40)[2])",
  FAC, "Se buscan dos números que sumen −3 y multipliquen −40.",
  ["Los números son −8 y 5: −8 + 5 = −3 y −8·5 = −40.", "P = (x − 8)(x + 5).",
   "Términos independientes: −8 y 5. El mayor es 5, que pertenece a x + 5."],
  "(x − 8)(x + 5) = x² + 5x − 8x − 40 = x² − 3x − 40.",
  answer="x + 5", answerVal="x+5",
  mistake="Tomar el valor absoluto y elegir x − 8. El mayor número es 5, no −8.")

F(21, "alg", 2, "Factoriza:\nF(x) = x⁴ − 5x² + 4\ne indica la cantidad de factores primos.", [], [], "size(factors(x^4-5*x^2+4))/2",
  FAC + " Un trinomio en x² se factoriza con aspa simple tomando x² como una sola cantidad.",
  "x⁴ − 5x² + 4 tiene forma de trinomio con u = x².",
  ["Con u = x²: u² − 5u + 4 = (u − 4)(u − 1).", "Volvemos a x: (x² − 4)(x² − 1).",
   "Cada factor es diferencia de cuadrados: (x − 2)(x + 2)(x − 1)(x + 1).", "Son 4 factores primos."],
  "(x² − 4)(x² − 1) = x⁴ − x² − 4x² + 4 = x⁴ − 5x² + 4.",
  answer="4 factores primos: x − 2, x + 2, x − 1 y x + 1", answerVal="4",
  mistake="Detenerse en (x² − 4)(x² − 1) y contar 2 factores. Cada uno todavía se factoriza.")

F(22, "alg", 3, "Factoriza:\nP(x, y) = 3x² + 7xy + 2y² + 7y + 12x + 6", [], [],
  "when(factor(3*x^2+7*x*y+2*y^2+7*y+12*x+6)==3*x^2+7*x*y+12*x+2*y^2+7*y+6,1,0)",
  FAC + " Para un polinomio de seis términos se usa el aspa doble.",
  "Se intenta el aspa doble y se comprueba si los términos lineales coinciden.",
  ["Parte cuadrática: 3x² + 7xy + 2y² = (3x + y)(x + 2y).",
   "Se buscan números a y b tales que (3x + y + a)(x + 2y + b) dé los demás términos: 3b + a = 12, b + 2a = 7 y ab = 6.",
   "De las dos primeras, b = 17/5, que no cumple ab = 6.",
   "Como no existe esa pareja, P no se factoriza con coeficientes enteros ni racionales."],
  "Como polinomio en y, P tiene discriminante 25x² + 2x + 1, que no es un cuadrado perfecto: no hay factorización racional.",
  answer="No se puede factorizar con coeficientes racionales", answerVal="1",
  warn="Con el enunciado tal cual, el polinomio no se factoriza. Es probable que la ficha tenga una errata en los términos lineales.",
  note="Si los términos lineales fueran 9x + 8y, quedaría (3x + y + 3)(x + 2y + 2). Es solo una hipótesis y la academia debe confirmar el enunciado.",
  review=True,
  mistake="Forzar una factorización con números que no cumplen las tres condiciones del aspa doble.")

F(23, "alg", 3, "Factoriza:\nP(x) = x⁴ + 3x³ + 7x² + 7x + 6", [], [], "factor(x^4+3*x^3+7*x^2+7*x+6)",
  FAC + " Un polinomio de cuarto grado sin raíces racionales puede factorizarse como producto de dos trinomios cuadráticos.",
  "Se plantea P = (x² + ax + b)(x² + cx + d) y se buscan enteros.",
  ["Suponemos P = (x² + ax + b)(x² + cx + d). Entonces a + c = 3, b + d + ac = 7, ad + bc = 7 y bd = 6.",
   "Probamos b = 2 y d = 3: bd = 6. Entonces ac = 2 y a + c = 3, con a = 1 y c = 2.",
   "Comprobamos ad + bc = 1·3 + 2·2 = 7, que coincide.", "P = (x² + x + 2)(x² + 2x + 3)."],
  "(x² + x + 2)(x² + 2x + 3) = x⁴ + 2x³ + 3x² + x³ + 2x² + 3x + 2x² + 4x + 6 = x⁴ + 3x³ + 7x² + 7x + 6.",
  answer="P = (x² + x + 2)(x² + 2x + 3)", answerVal="factor((x^2+x+2)*(x^2+2*x+3))",
  mistake="Buscar raíces enteras. Los divisores de 6 no son raíces de este polinomio.")

F(24, "alg", 3, "Factoriza:\nP(x) = x⁴ + 5x³ + 13x² + 17x + 12", [], [], "factor(x^4+5*x^3+13*x^2+17*x+12)",
  FAC + " Se usa el mismo método de dos trinomios cuadráticos.",
  "Se plantea P = (x² + ax + b)(x² + cx + d) con bd = 12.",
  ["Condiciones: a + c = 5, b + d + ac = 13, ad + bc = 17 y bd = 12.",
   "Probamos b = 3 y d = 4: bd = 12. Entonces ac = 6 y a + c = 5, con a = 2 y c = 3.",
   "Comprobamos ad + bc = 2·4 + 3·3 = 17, que coincide.", "P = (x² + 2x + 3)(x² + 3x + 4)."],
  "(x² + 2x + 3)(x² + 3x + 4) = x⁴ + 3x³ + 4x² + 2x³ + 6x² + 8x + 3x² + 9x + 12 = x⁴ + 5x³ + 13x² + 17x + 12.",
  answer="P = (x² + 2x + 3)(x² + 3x + 4)", answerVal="factor((x^2+2*x+3)*(x^2+3*x+4))",
  mistake="Probar b = 2 y d = 6 sin comprobar ad + bc. Esa pareja no da 17.")

NOTA = "Es el problema %d de la sección Ahora hazlo tú de la ficha."

F(25, "alg", 1, "Factoriza:\nP(a, b) = 9a² − 4b²\ne indica la suma de factores primos.",
  ["6a", "3a", "4b", "−6a", "9a"], ["6*a", "3*a", "4*b", "-6*a", "9*a"], "expand(factors(9*a^2-4*b^2)[0]+factors(9*a^2-4*b^2)[2])",
  NOT + " " + FAC, "9a² − 4b² es una diferencia de cuadrados: (3a)² − (2b)².",
  ["9a² − 4b² = (3a)² − (2b)² = (3a − 2b)(3a + 2b).", "Suma de factores primos: (3a − 2b) + (3a + 2b) = 6a."],
  "(3a − 2b)(3a + 2b) = 9a² − 4b².", ok="a", note=NOTA % 1,
  trap="3*a", trap_opt="b", mistake="Sumar solo uno de los factores o tomar 3a, la alternativa b.", **LAB)

F(26, "alg", 3, "Factoriza:\nP(x) = x⁴ − 5x³ + 3x² + 2x + 8\ne indica la cantidad de factores primos lineales.",
  ["1", "0", "3", "4", "2"], ["1", "0", "3", "4", "2"],
  "size(select(f->degree(f,x)==1,seq(factors(x^4-5*x^3+3*x^2+2*x+8)[2*k],k,0,size(factors(x^4-5*x^3+3*x^2+2*x+8))/2-1)))",
  FAC + " Un factor lineal x − r corresponde a una raíz r. Las raíces enteras posibles dividen al término independiente.",
  "Se buscan raíces enteras entre los divisores de 8 con la regla de Ruffini.",
  ["Probamos x = 2: 16 − 40 + 12 + 4 + 8 = 0. Entonces x − 2 es factor.", "Probamos x = 4: 256 − 320 + 48 + 8 + 8 = 0. Entonces x − 4 es factor.",
   "Dividiendo, P = (x − 2)(x − 4)(x² + x + 1).", "x² + x + 1 tiene discriminante 1 − 4 = −3 < 0: no se factoriza.", "Factores primos lineales: x − 2 y x − 4. Son 2."],
  "(x − 2)(x − 4) = x² − 6x + 8 y (x² − 6x + 8)(x² + x + 1) = x⁴ − 5x³ + 3x² + 2x + 8.",
  ok="e", note=NOTA % 2,
  trap="size(factors(x^4-5*x^3+3*x^2+2*x+8))/2", trap_opt="c", mistake="Contar también x² + x + 1 y responder 3, la alternativa c. Ese factor no es lineal.", **LAB)

F(27, "alg", 3, "Factoriza:\n5x⁴ + 22x³ + 21x² + 16x + 6\ne indica la cantidad de factores primos.",
  ["1", "2", "3", "4", "5"], ["1", "2", "3", "4", "5"],
  "when(expand((x^2+4*x+2)*(5*x^2+2*x+3)-(5*x^4+22*x^3+21*x^2+16*x+6))==0 and sqrt(8)!=round(sqrt(8)) and 4-60<0,2,-1)",
  FAC + " Se factoriza sobre los racionales, es decir con coeficientes enteros.",
  "Se plantea el producto de dos trinomios cuadráticos con coeficientes enteros.",
  ["Suponemos P = (x² + ax + b)(5x² + cx + d). El producto de los términos independientes es bd = 6 y el coeficiente principal 5 viene de 1·5.",
   "Probamos a = 4, b = 2, c = 2 y d = 3: 5x⁴ + 2x³ + 3x² + 20x³ + 8x² + 12x + 10x² + 4x + 6 = 5x⁴ + 22x³ + 21x² + 16x + 6. Coincide.",
   "P = (x² + 4x + 2)(5x² + 2x + 3).", "x² + 4x + 2 tiene discriminante 8, que no es cuadrado perfecto, y 5x² + 2x + 3 tiene discriminante −56. Ninguno se factoriza con racionales.",
   "Son 2 factores primos."],
  "El producto de los dos trinomios se comprobó término por término en el paso 2.",
  ok="b", note=NOTA % 3 + " Sobre los números reales, x² + 4x + 2 se factorizaría como (x + 2 − √2)(x + 2 + √2) y habría 3 factores; la clave se toma sobre los racionales, que es lo habitual.",
  review=True, mistake="Factorizar sobre los reales y responder 3, la alternativa c.", **LAB)

F(28, "alg", 1, "Si x + 1/x = 3, calcula x² + 1/x²",
  ["1", "7", "2", "−3", "4"], ["1", "7", "2", "-3", "4"], "simplify(subs(x^2+1/x^2,x=(3+sqrt(5))/2))",
  NOT, "Se eleva al cuadrado la condición. El doble producto de x por 1/x es 2.",
  ["(x + 1/x)² = x² + 2·x·(1/x) + 1/x² = x² + 2 + 1/x².", "3² = x² + 1/x² + 2, así que x² + 1/x² = 9 − 2 = 7."],
  "Con x = (3 + √5)/2: x + 1/x = 3 y x² = (7 + 3√5)/2, 1/x² = (7 − 3√5)/2. Su suma es 7.",
  ok="b", note=NOTA % 4, mistake="Olvidar restar el 2 del doble producto y responder 9. Ninguna alternativa recoge ese error.", **LAB)

F(29, "alg", 2, "Si\n[figura]\nReduce:\n§G=((a+b+c)*(a+b-c))/(a*b)",
  ["1", "2", "3", "§sqrt(2)", "§sqrt(3)"], ["1", "2", "3", "sqrt(2)", "sqrt(3)"], "(3+4+5)*(3+4-5)/(3*4)",
  NOT + " Teorema de Pitágoras: en un triángulo rectángulo, a² + b² = c².",
  "Arriba hay una suma por diferencia: (a + b)² − c².",
  ["(a + b + c)(a + b − c) = (a + b)² − c² = a² + 2ab + b² − c².", "Por Pitágoras, a² + b² = c². Entonces el numerador es 2ab.", "G = 2ab/ab = 2."],
  "Con el triángulo 3, 4, 5: (12)(2)/(12) = 2.",
  ok="b", note=NOTA % 5 + " En la figura, a y b son los catetos y c la hipotenusa.", fig="fa2_29",
  trap="sqrt(2)", trap_opt="d", mistake="Recordar √2 de los triángulos isósceles y responder la alternativa d. Aquí el triángulo no es isósceles.", **LAB)
