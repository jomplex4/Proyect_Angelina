"""Fichas de Álgebra, semana 4, de Jean Sanchez. Hay dos fichas con la misma semana:
a) Ecuaciones, ecuaciones de segundo grado y sistemas, 15 problemas.
b) Ecuaciones cuadráticas y ecuaciones lineales, 18 problemas. En esta, los problemas 6 y 7 son iguales."""
from model import ficha
LAB = dict(letters="abcde")
LAB4 = dict(letters="abcd")
SUMPROD = "Para ax² + bx + c = 0 con raíces r y s: suma r + s = −b/a y producto rs = c/a. El discriminante es b² − 4ac."

# ---------------- a) segundo grado y sistemas ----------------
F = ficha(4, "alg", tag="", part="segundo grado y sistemas")

F(1, "ecu", 1, "Una raíz de la ecuación\n§x^2+k*x+5=0\nes −1; hallar el valor de \"k\".",
  ["2", "4", "6", "8", "10"], ["2", "4", "6", "8", "10"], "solve(1-k+5=0,k)[0]",
  "Una raíz de una ecuación es un valor que, al reemplazar a x, la hace verdadera.", "Se reemplaza x = −1 y se despeja k.",
  ["Reemplazamos x = −1: (−1)² + k·(−1) + 5 = 0.", "1 − k + 5 = 0, así que k = 6."],
  "Con k = 6: x² + 6x + 5 = (x + 1)(x + 5), que se anula en x = −1.", ok="c", **LAB,
  mistake="Escribir 1 + k + 5 = 0 y obtener k = −6, que no está entre las alternativas.")

F(2, "ecu", 1, "Resuelve:\n§5*x^2+4*x=6-3*x",
  ["3/5", "2", "{−3/5; −2}", "{2; 3/5}", "{3/5; −2}"], ["3/5", "2", "sort([-3/5,-2])", "sort([2,3/5])", "sort([3/5,-2])"], "sort([solve(5*x^2+4*x=6-3*x,x)[0],solve(5*x^2+4*x=6-3*x,x)[1]])",
  "Una ecuación de segundo grado se iguala a cero y se factoriza.", "Se pasan todos los términos a un lado.",
  ["5x² + 4x + 3x − 6 = 0, es decir 5x² + 7x − 6 = 0.", "Aspa simple: (5x − 3)(x + 2) = 0.", "5x − 3 = 0 da x = 3/5 y x + 2 = 0 da x = −2."],
  "5·(9/25) + 7·(3/5) − 6 = 9/5 + 21/5 − 6 = 0, y con x = −2: 20 − 14 − 6 = 0.", ok="e", **LAB,
  mistake="Olvidar pasar 3x al lado izquierdo con signo positivo: 5x² + x − 6 = 0 da otras raíces.")

F(3, "ecu", 1, "Resuelve:\n§9*x^2-a^2=0",
  ["±a/3", "±3", "±2a", "±a/2", "±2"], ["[-5/3,5/3]", "[-3,3]", "[-10,10]", "[-5/2,5/2]", "[-2,2]"], "sort([subs(solve(9*x^2-a^2=0,x)[0],a=5),subs(solve(9*x^2-a^2=0,x)[1],a=5)])",
  "Diferencia de cuadrados: u² − v² = (u − v)(u + v).", "9x² es (3x)² y a² es a².",
  ["9x² − a² = (3x − a)(3x + a) = 0.", "3x = a o 3x = −a, así que x = ±a/3."],
  "Con a = 6: x = ±2 y 9·4 − 36 = 0.", ok="a", note="Las alternativas se comprueban con a = 5.", **LAB,
  mistake="Pensar que x² = a² y responder ±a. Falta el 9 que acompaña a x².")

F(4, "ecu", 2, "Resuelve:\n§5*x^2-8*x+2=0\n, dar como respuesta la mayor raíz.",
  ["§4/5*sqrt(6)", "§4/5", "§(4+sqrt(6))/5", "§(4-sqrt(6))/5", "§sqrt(6)/5"], ["4/5*sqrt(6)", "4/5", "(4+sqrt(6))/5", "(4-sqrt(6))/5", "sqrt(6)/5"], "max(solve(5*x^2-8*x+2=0,x))",
  "Fórmula general: x = (−b ± √(b² − 4ac))/(2a).", "Con a = 5, b = −8 y c = 2.",
  ["Discriminante: 64 − 40 = 24, y √24 = 2√6.", "x = (8 ± 2√6)/10 = (4 ± √6)/5.", "La mayor raíz es la del signo más: (4 + √6)/5."],
  "(4 + √6)/5 ≈ 1,29 y (4 − √6)/5 ≈ 0,31. Ambas cumplen 5x² − 8x + 2 = 0.", ok="c", **LAB,
  trap="(4-sqrt(6))/5", trap_opt="d", mistake="Elegir la menor raíz, (4 − √6)/5, alternativa d.")

F(5, "ecu", 2, "¿Para qué valor de \"m\" la ecuación\n§m*x^2-m*x+1=0\ntiene como C.S un conjunto unitario?",
  ["0", "2", "3", "−4", "4"], ["0", "2", "3", "-4", "4"], "max(solve(m^2-4*m=0,m))",
  "Una ecuación cuadrática tiene una sola solución cuando su discriminante es 0. Si m = 0 la ecuación ya no es cuadrática.",
  "Se iguala a cero el discriminante y se descarta el valor que hace desaparecer el término cuadrático.",
  ["Discriminante: m² − 4m = 0, es decir m(m − 4) = 0, con m = 0 o m = 4.", "Si m = 0, la ecuación queda 1 = 0, que no tiene solución: C.S. vacío, no unitario.", "Entonces m = 4: 4x² − 4x + 1 = (2x − 1)² = 0, con la única solución x = 1/2."],
  "Con m = 4, el conjunto solución es {1/2}.", ok="e", **LAB,
  trap="0", trap_opt="a", mistake="Aceptar m = 0 porque anula el discriminante. Con m = 0 no queda ninguna solución, alternativa a.")

F(6, "ecu", 1, "Calcula \"m\" en la siguiente ecuación:\n§(m+1)*x^2-(m+8)*x+10=0\n, para que la suma de raíces sea 9/2.",
  ["1", "2", "3", "5", "6"], ["1", "2", "3", "5", "6"], "solve((m+8)/(m+1)=9/2,m)[0]",
  SUMPROD, "La suma de raíces es (m + 8)/(m + 1).",
  ["(m + 8)/(m + 1) = 9/2.", "2(m + 8) = 9(m + 1), es decir 2m + 16 = 9m + 9.", "7 = 7m y m = 1."],
  "Con m = 1: 2x² − 9x + 10 = 0, con suma 9/2.", ok="a", **LAB,
  mistake="Usar −(m + 8)/(m + 1) como suma, con el signo cambiado dos veces.")

F(7, "ecu", 1, "Halla el valor de \"m\" en la siguiente ecuación:\n§(m-2)*x^2-5*x+2*m=0\n, para que sea el producto de raíces sea 6.",
  ["1", "3", "4", "5", "6"], ["1", "3", "4", "5", "6"], "solve(2*m/(m-2)=6,m)[0]",
  SUMPROD, "El producto de raíces es 2m/(m − 2).",
  ["2m/(m − 2) = 6.", "2m = 6m − 12, así que 4m = 12 y m = 3."],
  "Con m = 3: x² − 5x + 6 = 0, con producto 6.", ok="b", note="El enunciado dice para que sea el producto de raíces sea 6, tal como está en la ficha.", **LAB,
  mistake="Tomar el producto como c/a con el signo cambiado.")

F(8, "ecu", 1, "En la ecuación x² − nx + 36 = 0, halla \"n\" si se cumple:\n§1/x1+1/x2=5/12",
  ["25", "18", "12", "24", "15"], ["25", "18", "12", "24", "15"], "solve(n/36=5/12,n)[0]",
  SUMPROD, "La suma de los inversos es 1/x₁ + 1/x₂ = (x₁ + x₂)/(x₁x₂).",
  ["Suma de raíces: x₁ + x₂ = n. Producto: x₁x₂ = 36.", "1/x₁ + 1/x₂ = n/36 = 5/12.", "n = 36·5/12 = 15."],
  "Con n = 15: x² − 15x + 36 = (x − 3)(x − 12), con 1/3 + 1/12 = 5/12.", ok="e", **LAB,
  mistake="Invertir la fracción y calcular 36·12/5.")

F(9, "ecu", 3, "Para qué valor de \"n\" las raíces de la ecuación\n§x^2-(n+3)*x+n^2/4+1=0\n, se diferencian en 2.",
  ["−1/6", "−1/4", "−1/5", "−1/2", "−1/3"], ["-1/6", "-1/4", "-1/5", "-1/2", "-1/3"], "solve(6*n+5=4,n)[0]",
  SUMPROD + " La diferencia de las raíces cumple (r − s)² = (r + s)² − 4rs.", "Se escribe el cuadrado de la diferencia con la suma y el producto.",
  ["Suma: r + s = n + 3. Producto: rs = n²/4 + 1.", "(r − s)² = (n + 3)² − 4(n²/4 + 1) = n² + 6n + 9 − n² − 4 = 6n + 5.", "La diferencia es 2, así que 6n + 5 = 4 y n = −1/6."],
  "Con n = −1/6 el discriminante vale 6n + 5 = 4, positivo, y las raíces son reales.", ok="a", **LAB,
  mistake="Igualar 6n + 5 = 2 sin elevar al cuadrado la diferencia.")

F(10, "ecu", 1, "Forma la ecuación cuadrática cuyas raíces son 2/3 y −1/5.",
  ["§x^2-x-2=0", "§x^2-15*x-15=0", "§x^2-7*x-2=0", "§8*x^2-5*x-7=0", "§15*x^2-7*x-2=0"],
  ["expand(x^2-x-2)", "expand(x^2-15*x-15)", "expand(x^2-7*x-2)", "expand(8*x^2-5*x-7)", "expand(15*x^2-7*x-2)"], "expand(15*(x^2-(2/3-1/5)*x+(2/3)*(-1/5)))",
  "La ecuación de raíces r y s es x² − (r + s)x + rs = 0.", "Se calculan la suma y el producto y se quitan los denominadores.",
  ["Suma: 2/3 − 1/5 = 10/15 − 3/15 = 7/15. Producto: (2/3)(−1/5) = −2/15.", "x² − (7/15)x − 2/15 = 0.", "Multiplicamos por 15: 15x² − 7x − 2 = 0."],
  "Factorizando: 15x² − 7x − 2 = (3x − 2)(5x + 1), con raíces 2/3 y −1/5.", ok="e", **LAB,
  trap="expand(x^2-7*x-2)", trap_opt="c", mistake="Usar 7 y −2 como suma y producto sin dividir entre 15, alternativa c.")

F(11, "ecu", 1, "Resuelve y calcular «x.y»\n§4*x+3*y=8\n§x-2*y=2",
  ["0", "6", "5", "2", "3"], ["0", "6", "5", "2", "3"], "solve([4*x+3*y=8,x-2*y=2],[x,y])[0][0]*solve([4*x+3*y=8,x-2*y=2],[x,y])[0][1]",
  "Un sistema lineal de dos ecuaciones se resuelve por sustitución o por reducción.", "De la segunda ecuación se despeja x.",
  ["De x − 2y = 2: x = 2 + 2y.", "Reemplazamos: 4(2 + 2y) + 3y = 8, es decir 8 + 11y = 8 y y = 0.", "x = 2. El producto es x·y = 2·0 = 0."],
  "4·2 + 3·0 = 8 y 2 − 0 = 2.", ok="a", note="En la ficha las dos ecuaciones están unidas por una llave, que forma un sistema.", **LAB,
  mistake="Responder x = 2 en lugar de x·y.")

F(12, "ecu", 2, "Encontrar el valor de x:\n§(a-b*x)/b+(b-a*x)/a=x",
  ["§(a^3+b^2)/(3*a*b)", "§(a^2-b^2)/(3*a*b)", "§(a^2+b^2)/(3*a*b)", "§a^2+b^2", "§3*a*b"],
  ["(a^3+b^2)/(3*a*b)", "(a^2-b^2)/(3*a*b)", "(a^2+b^2)/(3*a*b)", "a^2+b^2", "3*a*b"], "solve((a-b*x)/b+(b-a*x)/a=x,x)[0]",
  "Una ecuación literal se resuelve igual que una numérica: se reparte y se agrupan los términos con x.", "Cada fracción se separa en dos partes.",
  ["(a − bx)/b = a/b − x y (b − ax)/a = b/a − x.", "La ecuación queda a/b + b/a − 2x = x, es decir a/b + b/a = 3x.", "a/b + b/a = (a² + b²)/(ab), así que x = (a² + b²)/(3ab)."],
  "Con a = 1 y b = 2: x = 5/6 y (1 − 2·5/6)/2 + (2 − 5/6)/1 = −1/3 + 7/6 = 5/6.", ok="c", **LAB,
  note="La alternativa a dice a³ + b², tal como está impresa en la ficha.",
  mistake="Pasar x al otro lado con el signo equivocado y obtener 2x en lugar de 3x.")

F(13, "ecu", 1, "Calcular p.q si la ecuación en x\n§2*p*x+3=8*x-3*q+12\nes indeterminada.",
  ["3", "7", "6", "4", "12"], ["3", "7", "6", "4", "12"], "solve([2*p=8,3=-3*q+12],[p,q])[0][0]*solve([2*p=8,3=-3*q+12],[p,q])[0][1]",
  "Una ecuación en x es indeterminada si se cumple para todo x. Eso pasa cuando los coeficientes de x son iguales y los términos independientes también.", "Se igualan por separado los términos con x y los términos sin x.",
  ["Términos con x: 2p = 8, así que p = 4.", "Términos independientes: 3 = −3q + 12, así que 3q = 9 y q = 3.", "p·q = 12."],
  "Con p = 4 y q = 3: 8x + 3 = 8x − 9 + 12 = 8x + 3, una igualdad para todo x.", ok="e", **LAB,
  mistake="Sumar p + q = 7, alternativa b, en lugar de multiplicar.")

F(14, "ecu", 3, "Resuelve:\n§3/(x+1)+1/(y+1)=7\n§1/(x+1)-1/(y+1)=13\nIndicar el valor de \"x\".",
  ["3/5", "4/5", "−4/5", "3/4", "1/4"], ["3/5", "4/5", "-4/5", "3/4", "1/4"], "1/solve([3*u+v=7,u-v=13],[u,v])[0][0]-1",
  "Un sistema con fracciones se vuelve lineal con un cambio de variable.", "Sean u = 1/(x + 1) y v = 1/(y + 1).",
  ["El sistema queda 3u + v = 7 y u − v = 13.", "Sumamos: 4u = 20, así que u = 5. Entonces v = 5 − 13 = −8.", "1/(x + 1) = 5 da x + 1 = 1/5 y x = −4/5."],
  "Con x = −4/5: 1/(x + 1) = 5, y con y = −9/8: 1/(y + 1) = −8. Entonces 15 − 8 = 7 y 5 + 8 = 13.", ok="c", **LAB,
  note="En la ficha las dos ecuaciones forman un sistema con llave.",
  mistake="Responder u = 5 sin deshacer el cambio de variable.")

F(15, "ecu", 2, "Dado el sistema:\n§3*a*x+2*b*y=16\n§x+2*y=8\nque tiene infinitas soluciones indicar el valor de:\n§E=(3*a+b)/2",
  ["2", "5", "6", "4", "3"], ["2", "5", "6", "4", "3"], "(3*solve(3*a=2,a)[0]+solve(2*b=4,b)[0])/2",
  "Un sistema lineal tiene infinitas soluciones cuando las dos ecuaciones son proporcionales.", "Los coeficientes y el término independiente guardan la misma razón.",
  ["16/8 = 2, así que la primera ecuación es el doble de la segunda.", "3a/1 = 2, de donde 3a = 2. Y 2b/2 = 2, de donde b = 2.", "E = (3a + b)/2 = (2 + 2)/2 = 2."],
  "Con a = 2/3 y b = 2: 2x + 4y = 16 es el doble de x + 2y = 8.", ok="a", **LAB,
  mistake="Igualar solo los coeficientes de x y olvidar que b se halla con la razón 2.")

# ---------------- b) cuadráticas y lineales ----------------
G = ficha(4, "alg", tag="b", part="cuadráticas y lineales")

G(1, "ecu", 2, "Resuelve e indica la menor raíz de:\n§(3*x+1)/(4*x-3)=(x+2)/(x-6)",
  ["22", "−22", "0", "11", "12"], ["22", "-22", "0", "11", "12"], "min(solve((3*x+1)*(x-6)=(x+2)*(4*x-3),x))",
  "Una ecuación con fracciones se resuelve multiplicando en cruz.", "Se multiplica en cruz y se pasan todos los términos a un lado.",
  ["(3x + 1)(x − 6) = (x + 2)(4x − 3).", "3x² − 17x − 6 = 4x² + 5x − 6.", "0 = x² + 22x = x(x + 22), con x = 0 o x = −22.", "La menor raíz es −22."],
  "Con x = −22: −65/−91 = 5/7 y −20/−28 = 5/7. Con x = 0: −1/3 en ambos lados.", ok="b", **LAB,
  trap="22", trap_opt="a", mistake="Perder el signo al despejar: de x(x + 22) = 0 sale x = −22, no 22, alternativa a.")

G(2, "ecu", 2, "Resuelve e indica la menor solución de:\n§3*x^2-x-1=0",
  ["§(-1+sqrt(13))/2", "§(1-sqrt(5))/6", "§(1-sqrt(13))/6", "§1-sqrt(13)", "§1+sqrt(5)"],
  ["(-1+sqrt(13))/2", "(1-sqrt(5))/6", "(1-sqrt(13))/6", "1-sqrt(13)", "1+sqrt(5)"], "min(solve(3*x^2-x-1=0,x))",
  "Fórmula general: x = (−b ± √(b² − 4ac))/(2a).", "Con a = 3, b = −1 y c = −1.",
  ["Discriminante: (−1)² − 4·3·(−1) = 1 + 12 = 13.", "x = (1 ± √13)/6.", "La menor solución es (1 − √13)/6."],
  "(1 − √13)/6 ≈ −0,434 y 3·0,188 + 0,434 − 1 ≈ 0.", ok="c", **LAB,
  mistake="Dividir solo √13 entre 6, o usar el denominador 2 en lugar de 2a = 6.")

G(3, "ecu", 2, "Calcula la suma de las inversas de las raíces de la ecuación:\n§2*x^2-3*x+4=0",
  ["−3/4", "4/3", "3/4", "−4/3"], ["-3/4", "4/3", "3/4", "-4/3"], "(3/2)/(4/2)",
  SUMPROD + " La suma de los inversos es (r + s)/(rs).", "No hace falta hallar las raíces.",
  ["Suma: 3/2. Producto: 4/2 = 2.", "1/r + 1/s = (r + s)/(rs) = (3/2)/2 = 3/4."],
  "Las raíces son complejas, porque el discriminante es 9 − 32 < 0, pero las fórmulas de suma y producto siguen valiendo.", ok="c", **LAB4,
  trap="-3/4", trap_opt="a", mistake="Olvidar el signo: la suma es −b/a = 3/2, no −3/2. Resulta −3/4, alternativa a.")

G(4, "ecu", 1, "Si r y s son raíces de la ecuación:\n§x^2-4*x+2=0\nCalcula el valor de r² + s²",
  ["4", "22", "14", "16", "12"], ["4", "22", "14", "16", "12"], "simplify(solve(x^2-4*x+2=0,x)[0]^2+solve(x^2-4*x+2=0,x)[1]^2)",
  SUMPROD + " Además r² + s² = (r + s)² − 2rs.", "Con la suma y el producto basta.",
  ["Suma: 4. Producto: 2.", "r² + s² = 4² − 2·2 = 16 − 4 = 12."],
  "Las raíces son 2 ± √2: sus cuadrados suman (6 + 4√2) + (6 − 4√2) = 12.", ok="e", **LAB,
  trap="16", trap_opt="d", mistake="Olvidar restar 2rs y responder 16, alternativa d.")

G(5, "ecu", 2, "Si r y s son raíces de:\n§x^2+3*x-2=0\nCalcula r³ + s³",
  ["−27", "27", "36", "−45", "18"], ["-27", "27", "36", "-45", "18"], "simplify(solve(x^2+3*x-2=0,x)[0]^3+solve(x^2+3*x-2=0,x)[1]^3)",
  SUMPROD + " Además r³ + s³ = (r + s)³ − 3rs(r + s).", "Se usa la identidad del cubo de una suma.",
  ["Suma: −3. Producto: −2.", "r³ + s³ = (−3)³ − 3·(−2)·(−3) = −27 − 18 = −45."],
  "Las raíces son (−3 ± √17)/2. Sus cubos suman −45.", ok="d", **LAB,
  trap="(-3)^3", trap_opt="a", mistake="Quedarse con (r + s)³ = −27 y olvidar el término −3rs(r + s), alternativa a.")

G(6, "ecu", 1, "Si r y s son raíces de:\n§x^2-7*x+5=0\nCalcula (r + 1)(s + 1)",
  ["13", "12", "3", "−2"], ["13", "12", "3", "-2"], "simplify((solve(x^2-7*x+5=0,x)[0]+1)*(solve(x^2-7*x+5=0,x)[1]+1))",
  SUMPROD, "(r + 1)(s + 1) = rs + r + s + 1.",
  ["Suma: 7. Producto: 5.", "(r + 1)(s + 1) = 5 + 7 + 1 = 13."],
  "Las raíces son (7 ± √29)/2. Su (r + 1)(s + 1) también da 13.", ok="a", **LAB4,
  trap="5+7", trap_opt="b", mistake="Olvidar el 1 final y responder 12, alternativa b.")

G(7, "ecu", 1, "Si r y s son raíces de:\n§x^2-7*x+5=0\nCalcula (r + 1)(s + 1)",
  ["13", "12", "3", "−2"], ["13", "12", "3", "-2"], "simplify((solve(x^2-7*x+5=0,x)[0]+1)*(solve(x^2-7*x+5=0,x)[1]+1))",
  SUMPROD, "(r + 1)(s + 1) = rs + r + s + 1.",
  ["Suma: 7. Producto: 5.", "(r + 1)(s + 1) = 5 + 7 + 1 = 13."],
  "Las raíces son (7 ± √29)/2. Su (r + 1)(s + 1) también da 13.", ok="a", **LAB4,
  note="Este problema es igual al 6 de la misma ficha.", trap="5+7", trap_opt="b", mistake="Olvidar el 1 final y responder 12, alternativa b.")

G(8, "ecu", 2, "Calcula el producto de las raíces de la décima ecuación:\n§x^2+x-13=0\n§x^2+8*x-8=0\n§x^2+27*x-27=0",
  ["729", "1000", "−1000", "−729", "812"], ["729", "1000", "-1000", "-729", "812"], "-10^3",
  SUMPROD + " Una sucesión de ecuaciones se resuelve buscando el patrón.", "Cada ecuación tiene la forma x² + n³x − n³ = 0, con n = 1, 2, 3, …",
  ["Segunda ecuación: n = 2 y n³ = 8. Tercera: n = 3 y n³ = 27.", "La décima ecuación es x² + 1000x − 1000 = 0.", "Producto de raíces: c/a = −1000."],
  "En la segunda ecuación el producto es −8 = −2³ y en la tercera −27 = −3³.", ok="c", **LAB,
  warn="La primera ecuación impresa, x² + x − 13 = 0, rompe el patrón de las otras dos. Probablemente es x² + x − 1 = 0.",
  note="El patrón sale de las ecuaciones segunda y tercera, así que la respuesta no depende de la primera.", review=True,
  trap="1000", trap_opt="b", mistake="Perder el signo del producto y responder 1000, alternativa b.")

G(9, "ecu", 2, "De un juego de 32 cartas, se sacan primero \"x\" cartas y tres más; luego se saca la mitad de lo que resta. Si todavía quedan 10 cartas. ¿Cuántas cartas sacó la primera vez?",
  ["9", "14", "12", "8", "10"], ["9", "14", "12", "8", "10"], "solve((29-x)/2=10,x)[0]+3",
  "Un problema de planteo se resuelve con una ecuación en la que se sigue lo que pasa paso a paso.", "La primera vez se sacan x + 3 cartas, no solo x.",
  ["Primera vez: x + 3 cartas. Quedan 32 − (x + 3) = 29 − x.", "Luego se saca la mitad y quedan 10: (29 − x)/2 = 10.", "29 − x = 20 y x = 9.", "La primera vez se sacaron x + 3 = 12 cartas."],
  "Quedan 20 cartas, se sacan 10 y quedan 10.", ok="c", **LAB,
  trap="solve((29-x)/2=10,x)[0]", trap_opt="a", mistake="Responder x = 9, alternativa a, y no sumar las tres cartas.")

G(10, "ecu", 2, "Al resolver la ecuación :\n§x^2-sqrt(x+a)=44/(x-3)\nse obtuvo como una de sus soluciones el valor 5, hallar el valor de \"a\".",
  ["3", "4", "9", "16", "11"], ["3", "4", "9", "16", "11"], "solve(25-sqrt(5+a)=44/(5-3),a)[0]",
  "Si x = 5 es solución, al reemplazarla la ecuación se cumple.", "Se reemplaza x = 5 y queda una ecuación con a.",
  ["25 − √(5 + a) = 44/2 = 22.", "√(5 + a) = 3.", "5 + a = 9 y a = 4."],
  "Con a = 4: 25 − √9 = 22 y 44/(5 − 3) = 22.", ok="b", **LAB,
  trap="9", trap_opt="c", mistake="Quedarse con 5 + a = 9 y responder 9, alternativa c.")

G(11, "ecu", 3, "Calcular \"x\", en :\n§1/(sqrt(x)+a)+1/(sqrt(x)+b)=1/(sqrt(x)-a)+1/(sqrt(x)-b)",
  ["a + b", "a − b", "ab", "§sqrt(a)+sqrt(b)", "§sqrt(a*b)"], ["5", "-3", "4", "3", "2"],
  "when(1/(sqrt(4)+1)+1/(sqrt(4)+4)==1/(sqrt(4)-1)+1/(sqrt(4)-4),1*4,-1)",
  "Con una raíz repetida conviene el cambio t = √x. Las fracciones con denominador común se juntan.", "Con t = √x, sumar las fracciones de cada lado y hacer el producto cruzado.",
  ["Sea t = √x. Lado izquierdo: (2t + a + b)/((t + a)(t + b)). Lado derecho: (2t − a − b)/((t − a)(t − b)).", "Con s = a + b y p = ab: (2t + s)(t² − st + p) = (2t − s)(t² + st + p).", "Desarrollando, −st² + sp = st² − sp, es decir s(p − t²) = 0.", "Entonces t² = p, es decir x = ab."],
  "Con a = 1 y b = 4, x = 4: 1/3 + 1/6 = 1/2 y 1/1 + 1/(−2) = 1/2.", ok="c", **LAB,
  note="Las alternativas se comprueban con a = 1 y b = 4. Se supone a + b distinto de 0.",
  trap="2", trap_opt="e", mistake="Quedarse con t = √x y responder √(ab), alternativa e. La incógnita es x, no √x.")

G(12, "ecu", 3, "Un comerciante tenía una determinada suma de dinero. El primer año gastó 100 pesos y aumentó a lo que quedaba un tercio de este resto. Al año siguiente, volvió a gastar 100 pesos y aumentó a la cantidad restante un tercio de ella.\nEl tercer año gastó de nuevo 100 pesos y agregó la tercera parte de lo que quedaba. Si el capital resultante es el doble del inicial. ¿Cuál fue el capital inicial?",
  ["1480", "1500", "1400", "2380", "2000"], ["1480", "1500", "1400", "2380", "2000"], "solve((((C-100)*4/3-100)*4/3-100)*4/3=2*C,C)[0]",
  "Aumentar un tercio es multiplicar por 4/3. Un proceso que se repite se plantea año por año.", "Cada año: se resta 100 y se multiplica por 4/3.",
  ["Capital inicial C. Fin del año 1: (C − 100)·4/3.", "Fin del año 2: ((C − 100)·4/3 − 100)·4/3.", "Fin del año 3: (((C − 100)·4/3 − 100)·4/3 − 100)·4/3 = 2C.", "Resolviendo, C = 1480."],
  "Con C = 1480: 1840, 2320 y 2960, que es el doble de 1480.", ok="a", **LAB,
  mistake="Aumentar un tercio como multiplicar por 1/3 en lugar de 4/3.")

G(13, "ecu", 2, "Siendo x₁ y x₂ las raíces de la ecuación: x² − x − 1 = 0 ; Hallar el valor de:\n§M=x1/x2+x2/x1",
  ["−9", "9", "−3/2", "5", "1"], ["-9", "9", "-3/2", "5", "1"], "simplify(((1)^2-2*(-1))/(-1))",
  SUMPROD + " Además x₁/x₂ + x₂/x₁ = (x₁² + x₂²)/(x₁x₂).", "Se calcula con la suma y el producto, sin hallar las raíces.",
  ["Suma: 1. Producto: −1.", "x₁² + x₂² = 1² − 2·(−1) = 3.", "M = 3/(−1) = −3.", "−3 no está entre las alternativas."],
  "Las raíces son (1 ± √5)/2. Su cociente x₁/x₂ ≈ −2,618 y x₂/x₁ ≈ −0,382, que suman −3.", ok=None,
  answer="−3", answerVal="-3",
  warn="Con el enunciado tal cual, el resultado es −3 y no está entre las alternativas. La ficha tiene una errata.", review=True, **LAB,
  mistake="Elegir 5, alternativa d, tomando x₁² + x₂² = 3 + 2 sin dividir entre el producto.")

G(14, "ecu", 1, "Determinar el valor de \"k\" si una de las raíces de la ecuación es igual a 3:\n§x^2-7*x+k=0",
  ["9", "10", "11", "12", "13"], ["9", "10", "11", "12", "13"], "solve(9-21+k=0,k)[0]",
  "Una raíz es un valor que hace verdadera la ecuación.", "Se reemplaza x = 3.",
  ["3² − 7·3 + k = 0.", "9 − 21 + k = 0 y k = 12."],
  "Con k = 12: x² − 7x + 12 = (x − 3)(x − 4).", ok="d", **LAB,
  mistake="Calcular 9 + 21 = 30 con el signo equivocado: k = −30.")

G(15, "ecu", 2, "Calcular el valor de \"n\", si las raíces de la ecuación:\n§(n-1)*x^2-5*x+3*n=7\nSon recíprocas.",
  ["2", "−2", "−4", "4", "3"], ["2", "-2", "-4", "4", "3"], "solve((3*n-7)/(n-1)=1,n)[0]",
  SUMPROD + " Dos números son recíprocos si su producto es 1.", "Se pasa el 7 al lado izquierdo y el producto de raíces debe valer 1.",
  ["(n − 1)x² − 5x + 3n − 7 = 0.", "Producto de raíces: (3n − 7)/(n − 1) = 1.", "3n − 7 = n − 1, así que 2n = 6 y n = 3."],
  "Con n = 3: 2x² − 5x + 2 = 0, con raíces 2 y 1/2.", ok="e", **LAB,
  mistake="Olvidar pasar el 7 y tomar el término independiente como 3n.")

G(16, "ecu", 3, "Resolver:\n§(x+1)/(x+2)-(x+3)/(x+4)=2/3\nY dar la diferencia entre el producto y la suma de las soluciones:",
  ["5", "15", "9", "6", "17"], ["5", "15", "9", "6", "17"],
  "11-(-6)",
  SUMPROD, "Las dos fracciones se restan con denominador común.",
  ["(x + 1)(x + 4) − (x + 3)(x + 2) = x² + 5x + 4 − x² − 5x − 6 = −2.", "Entonces −2/((x + 2)(x + 4)) = 2/3, así que (x + 2)(x + 4) = −3.", "x² + 6x + 8 = −3, es decir x² + 6x + 11 = 0.", "Producto: 11. Suma: −6. Diferencia: 11 − (−6) = 17."],
  "Las soluciones son −3 ± i√2, no reales, pero su suma y su producto sí se calculan.", ok="e", **LAB,
  note="Las soluciones de esta ecuación no son reales. La pregunta se responde con la suma y el producto.",
  trap="11-6", trap_opt="a", mistake="Restar 6 en lugar de sumar 6: la suma de soluciones es −6, así que 11 − (−6) = 17. Con el signo mal sale 5, alternativa a.")

G(17, "ecu", 2, "La diferencia de las raíces de la ecuación:\n§x^2-m*x+2=0\nes 1\nHallar: m² − 5",
  ["2", "−2", "−4", "11", "4"], ["2", "-2", "-4", "11", "4"], "solve(m^2-8=1,m)[0]^2-5",
  SUMPROD + " Además (r − s)² = (r + s)² − 4rs.", "Con la diferencia de raíces se obtiene m².",
  ["Suma: m. Producto: 2.", "(r − s)² = m² − 8 = 1², así que m² = 9.", "m² − 5 = 4."],
  "Con m = 3: x² − 3x + 2 = (x − 1)(x − 2), con raíces 1 y 2 que difieren en 1.", ok="e", **LAB,
  mistake="Escribir m² = 1 + 8 con el 8 de signo contrario.")

G(18, "ecu", 2, "Para que valor de \"a\" la ecuación:\n§x^2-(3*a-1)*x+5*a+1=0\n, admite raíces iguales.",
  ["−1", "−3", "9", "3", "0"], ["-1", "-3", "9", "3", "0"], "max(solve((3*a-1)^2-4*(5*a+1)=0,a))",
  "Las raíces son iguales cuando el discriminante es 0.", "Se iguala a 0 el discriminante y se resuelve la ecuación en a.",
  ["Discriminante: (3a − 1)² − 4(5a + 1) = 9a² − 6a + 1 − 20a − 4 = 9a² − 26a − 3.", "9a² − 26a − 3 = 0 se factoriza como (9a + 1)(a − 3) = 0.", "a = 3 o a = −1/9. Entre las alternativas solo está 3."],
  "Con a = 3: x² − 8x + 16 = (x − 4)², con raíces iguales.", ok="d", **LAB,
  note="El valor a = −1/9 también cumple la condición, pero no está entre las alternativas.",
  mistake="Pasar el signo mal al desarrollar (3a − 1)² y obtener a = −1, alternativa a.")
