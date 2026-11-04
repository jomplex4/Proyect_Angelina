"""Ficha de Aritmética, semana 3, de Junior Burga: operaciones con intervalos e inecuaciones. 29 problemas, 5 alternativas."""
from model import ficha
F = ficha(3, "arit")
LAB = dict(letters="abcde")
INT = ("Un intervalo se escribe con sus extremos. El extremo incluido lleva corchete cerrado y el no incluido lleva corchete abierto, que en la ficha también se escribe con < o >. "
       "Al multiplicar o dividir una desigualdad por un número negativo, el sentido se invierte.")
INEQ = "Una inecuación lineal se resuelve como una ecuación, cuidando de invertir el sentido si se multiplica o divide por un negativo."
PLAN = "En los problemas de planteo, los datos se vuelven desigualdades con la misma incógnita. Si la incógnita cuenta objetos, solo valen números enteros."

F(1, "ineq", 2, "Luego de resolver:\n§x/2-1/4>2*x+1/3\n, que valor no puede tomar x",
  ["− 3", "− 6", "− 8", "− 1", "0"], ["-3", "-6", "-8", "-1", "0"], "select(v->not(v<solve(x/2-1/4=2*x+1/3,x)[0]),[-3,-6,-8,-1,0])[0]",
  INEQ, "Se resuelve la desigualdad y se busca la alternativa que no está en el conjunto solución.",
  ["x/2 − 2x > 1/3 + 1/4, es decir −3x/2 > 7/12.", "Al multiplicar por −2/3 el sentido se invierte: x < −7/18, que es x < −0,39.", "Valen −3, −6, −8 y −1. El 0 no es menor que −0,39."],
  "−1 < −0,39 sí cumple, y 0 > −0,39 no.", ok="e", **LAB, mistake="No invertir el sentido y obtener x > −7/18, con lo que −3 parecería el valor que no puede tomar.")

F(2, "ineq", 2, "Resolver:\n§4*x/3-1<=3*x/5+2",
  ["R", "φ", "<−∞, 45/11>", "<45/11, +∞>", "<−∞, 45/11]"], ["[-inf,inf,0,0]", "[]", "[-inf,45/11,0,0]", "[45/11,inf,0,0]", "[-inf,45/11,0,1]"],
  "[-inf,solve(4*x/3-1=3*x/5+2,x)[0],0,1]", INT + " " + INEQ, "Se pasan las x a un lado y el desigual es \"menor o igual\", así que el extremo se incluye.",
  ["4x/3 − 3x/5 ≤ 2 + 1, es decir 11x/15 ≤ 3.", "x ≤ 45/11.", "Como la desigualdad es ≤, el extremo 45/11 se incluye: <−∞, 45/11]."], "Con x = 45/11: 60/11 − 1 = 49/11 y 27/11 + 2 = 49/11. Se cumple la igualdad.", ok="e", **LAB,
  note="Las alternativas se comprueban como [inicio, fin, ¿incluye el inicio?, ¿incluye el fin?]. φ es el conjunto vacío.",
  mistake="Escribir el extremo abierto por descuido, alternativa c. Con ≤ el extremo sí se incluye.", trap="[-inf,45/11,0,0]", trap_opt="c")

F(3, "ineq", 2, "Resolver:\n§(2*x+6)/3-x/4<5",
  ["x ∈ <−∞, 36/5>", "x ∈ <−1, 1>", "x ∈ <36/5, +∞>", "N.A.", "x ∈ <1, 36/5>"], ["[-inf,36/5,0,0]", "[-1,1,0,0]", "[36/5,inf,0,0]", "undef", "[1,36/5,0,0]"],
  "[-inf,solve((2*x+6)/3-x/4=5,x)[0],0,0]", INT + " " + INEQ, "Se multiplica por 12 para quitar denominadores. Como 12 es positivo, el sentido no cambia.",
  ["4(2x + 6) − 3x < 60.", "8x + 24 − 3x < 60, es decir 5x < 36.", "x < 36/5: <−∞, 36/5>."], "Con x = 7: 20/3 − 7/4 = 4,92 < 5. Con x = 8: 22/3 − 2 = 5,33 > 5.", ok="a", **LAB,
  mistake="Olvidar multiplicar el 5 por 12 y obtener 5x < −24.")

F(4, "ineq", 2, "Resolver:   −2 < −4 + 3x ≤ 5",
  ["x ∈ <2/3, 3]", "x ∈ <3, +∞>", "x ∈ <1/3, 3>", "N.A.", "x ∈ <−∞, 2/3>"], ["[2/3,3,0,1]", "[3,inf,0,0]", "[1/3,3,0,0]", "undef", "[-inf,2/3,0,0]"],
  "[solve(-4+3*x=-2,x)[0],solve(-4+3*x=5,x)[0],0,1]", INT + " " + INEQ, "Es una doble desigualdad: se opera en las tres partes a la vez.",
  ["Sumamos 4 a las tres partes: 2 < 3x ≤ 9.", "Dividimos entre 3: 2/3 < x ≤ 3.", "El extremo 2/3 no se incluye y el 3 sí: <2/3, 3]."], "Con x = 3: −4 + 9 = 5 ≤ 5. Con x = 2/3: −4 + 2 = −2, que no es mayor que −2.", ok="a", **LAB,
  mistake="Cerrar el extremo 2/3, porque la primera desigualdad es estricta.")

F(5, "ineq", 2, "Hallar el conjunto solución de:\n2x + 1 ≤ x + 3 < 4x − 1",
  ["[2, +∞[", "[4/3, +∞[", "φ", "<4/3, 2>", "<4/3, +2]"], ["[2,inf,1,0]", "[4/3,inf,1,0]", "[]", "[4/3,2,0,0]", "[4/3,2,0,1]"],
  "[solve(x+3=4*x-1,x)[0],solve(2*x+1=x+3,x)[0],0,1]", INT + " " + INEQ, "Una desigualdad doble se separa en dos y se intersectan las soluciones.",
  ["Primera: 2x + 1 ≤ x + 3 da x ≤ 2.", "Segunda: x + 3 < 4x − 1 da 4 < 3x, es decir x > 4/3.", "Intersección: 4/3 < x ≤ 2, que es <4/3, 2]."], "Con x = 2: 5 ≤ 5 < 7. Con x = 4/3: 13/3 no es menor que 13/3.", ok="e", **LAB,
  note="La alternativa e está escrita <4/3, +2] en la ficha. Es <4/3, 2].", mistake="Responder <4/3, 2>, alternativa d, sin incluir el 2. Con ≤ el 2 sí está.", trap="[4/3,2,0,0]", trap_opt="d")

F(6, "ineq", 2, "Si x ∈ ]2, 4[ y luego\n§1/(2*x+3)\n, a qué intervalo pertenece:",
  ["]1/11, 1/7[", "]−1/2, 1/6[", "]1/5, 1/3[", "]1/12, 3/4[", "]1/13, 1/6["], ["[1/11,1/7,0,0]", "[-1/2,1/6,0,0]", "[1/5,1/3,0,0]", "[1/12,3/4,0,0]", "[1/13,1/6,0,0]"],
  "[1/(2*4+3),1/(2*2+3),0,0]", INT + " Al invertir números positivos, el orden se invierte.", "Primero se halla el intervalo de 2x + 3 y luego se invierte.",
  ["2 < x < 4, así que 4 < 2x < 8 y 7 < 2x + 3 < 11.", "Al tomar el inverso de números positivos, el orden se invierte: 1/11 < 1/(2x + 3) < 1/7.", "Intervalo: ]1/11, 1/7[."], "Con x = 3: 1/9, que está entre 1/11 y 1/7.", ok="a", **LAB,
  mistake="Escribir 1/7 < ... < 1/11 sin invertir el orden.")

F(7, "ineq", 2, "Si: (2x − 1) ∈ [−5; 7>, entonces, ¿a qué intervalo pertenece \"x\"?",
  ["x ∈ [−2 ; 4]", "x ∈ <−2 ; 4>", "x ∈ <−2 ; 4]", "x ∈ [−4 ; 2>", "x ∈ [−2 ; 4>"], ["[-2,4,1,1]", "[-2,4,0,0]", "[-2,4,0,1]", "[-4,2,1,0]", "[-2,4,1,0]"],
  "[solve(2*x-1=-5,x)[0],solve(2*x-1=7,x)[0],1,0]", INT, "Se despeja x en la doble desigualdad y se conserva si cada extremo se incluye o no.",
  ["−5 ≤ 2x − 1 < 7.", "Sumamos 1: −4 ≤ 2x < 8.", "Dividimos entre 2: −2 ≤ x < 4. El −2 se incluye y el 4 no: [−2 ; 4>."], "Con x = −2: 2x − 1 = −5, incluido. Con x = 4: 7, que no está.", ok="e", **LAB,
  mistake="Cerrar el 4 por costumbre, alternativa a. El 7 del enunciado no se incluye.")

F(8, "ineq", 2, "Si:\n§1/(2*x+8)\n∈ [1/12 ; 1/6]\nentonces: x ∈ [m ; n]. Halle: mn",
  ["2", "−2", "−15", "−6", "−3"], ["2", "-2", "-15", "-6", "-3"], "solve(2*x+8=6,x)[0]*solve(2*x+8=12,x)[0]", INT + " Al invertir números positivos, el orden se invierte.", "Se invierte la doble desigualdad y se despeja x.",
  ["1/12 ≤ 1/(2x + 8) ≤ 1/6. Al invertir, 6 ≤ 2x + 8 ≤ 12.", "Restamos 8: −2 ≤ 2x ≤ 4. Dividimos entre 2: −1 ≤ x ≤ 2.", "m = −1 y n = 2, así que mn = −2."], "Con x = −1: 1/6 y con x = 2: 1/12. Los dos extremos del intervalo.", ok="b", **LAB,
  mistake="Intercambiar m y n o multiplicar mal y obtener −3.")

F(9, "ineq", 2, "Dar como respuesta el número de soluciones enteras\n2x − 5 < x + 3 < 3x − 7",
  ["1", "2", "3", "4", "5"], ["1", "2", "3", "4", "5"], "size(seq(x,x,floor(solve(x+3=3*x-7,x)[0])+1,ceil(solve(2*x-5=x+3,x)[0])-1))", INEQ, "Se separan las dos desigualdades y se intersectan.",
  ["Primera: 2x − 5 < x + 3 da x < 8.", "Segunda: x + 3 < 3x − 7 da 10 < 2x, es decir x > 5.", "5 < x < 8: los enteros son 6 y 7. Son 2 soluciones."], "Con x = 6: 7 < 9 < 11. Con x = 7: 9 < 10 < 14.", ok="b", **LAB,
  mistake="Contar el 5 y el 8, que no están porque las desigualdades son estrictas.")

F(10, "ineq", 3, "Si: x ∈ <−2;4>\nAdemás\n§(x+1)/(x+3)\n∈ <−c; a/b>\nHallar: \"a + b\"",
  ["2", "4", "16", "6", "12"], ["2", "4", "16", "6", "12"], "numer((4+1)/(4+3))+denom((4+1)/(4+3))", INT, "(x + 1)/(x + 3) = 1 − 2/(x + 3). Se sigue el intervalo paso a paso.",
  ["−2 < x < 4, así que 1 < x + 3 < 7.", "Al invertir: 1/7 < 1/(x + 3) < 1. Por 2: 2/7 < 2/(x + 3) < 2.", "Al multiplicar por −1: −2 < −2/(x + 3) < −2/7. Sumamos 1: −1 < (x + 1)/(x + 3) < 5/7.", "Entonces −c = −1, es decir c = 1, y a/b = 5/7. Con a = 5 y b = 7, a + b = 12."],
  "Con x = 4: 5/7, el extremo de arriba. Con x = −2: −1/1 = −1, el de abajo.", ok="e", **LAB, note="Se toma a/b = 5/7 como fracción irreducible.", mistake="Olvidar que multiplicar por −1 invierte el orden de la desigualdad.")

F(11, "ineq", 3, "El número de plumas contenidas en una caja es tal que su duplo, disminuido en 86, es mayor que 200. De la caja se sacan 17 y quedan menos que la diferencia entre 200 y la mitad de las que había inicialmente. ¿Cuántas eran éstas?",
  ["156", "188", "144", "123", "132"], ["156", "188", "144", "123", "132"], "max(select(n->2*n-86>200 and n-17<200-n/2,seq(n,n,100,200)))", PLAN, "Dos desigualdades sobre el mismo número n.",
  ["2n − 86 > 200 da n > 143.", "n − 17 < 200 − n/2 da 3n/2 < 217, es decir n < 144,67.", "143 < n < 144,67 y n entero: n = 144."], "Con n = 144: 288 − 86 = 202 > 200, y 127 < 200 − 72 = 128.", ok="c", **LAB, mistake="Olvidar que n debe ser entero y dejar el intervalo sin decidir.")

F(12, "ineq", 3, "Un número entero y positivo, es tal que la tercera parte del que le precede, disminuida en 10, es mayor que 14, y que la cuarta parte del que le sigue, aumentada en 10, es menor que 29. ¿Con qué cifra comienza el número?",
  ["5", "7", "9", "8", "4"], ["5", "7", "9", "8", "4"], "floor(select(n->(n-1)/3-10>14 and (n+1)/4+10<29,seq(n,n,1,200))[0]/10)", PLAN, "El que le precede es n − 1 y el que le sigue es n + 1.",
  ["(n − 1)/3 − 10 > 14 da (n − 1)/3 > 24, es decir n > 73.", "(n + 1)/4 + 10 < 29 da (n + 1)/4 < 19, es decir n < 75.", "73 < n < 75 y n entero: n = 74.", "El número empieza con 7."], "Con n = 74: 73/3 − 10 = 14,33 > 14 y 75/4 + 10 = 28,75 < 29.", ok="b", **LAB, mistake="Responder 74, que no es una cifra.")

F(13, "ineq", 3, "Entre Pedro y Luis tienen menos de 8 hijos. Luis tiene más hijos que Ramón y aunque Pedro tuviera tres hijos menos, seguiría teniendo más hijos que Ramón. ¿Cuántos hijos tiene Ramón?",
  ["2", "1", "3", "5", "N.A."], ["2", "1", "3", "5", "undef"],
  "max(select(R->size(select(L->size(select(P->P+L<8 and L>R and P-3>R,seq(P,P,0,10)))>0,seq(L,L,0,10)))>0,seq(R,R,0,10)))",
  PLAN, "Con R los hijos de Ramón: L ≥ R + 1 y P − 3 ≥ R + 1, luego P + L ≥ 2R + 5.",
  ["Sean P, L y R los hijos de Pedro, Luis y Ramón. Se pide P + L < 8, L > R y P − 3 > R.", "Con enteros: L ≥ R + 1 y P ≥ R + 4, así que P + L ≥ 2R + 5.", "2R + 5 ≤ 7 da R ≤ 1.", "Como el enunciado habla de los hijos de Ramón, se toma R = 1: por ejemplo Pedro 5, Luis 2 y Ramón 1."],
  "Con R = 1, P = 5 y L = 2: 5 + 2 = 7 < 8, 2 > 1 y 5 − 3 = 2 > 1.", ok="b", **LAB, review=True,
  note="Con R = 0 también se cumplen las condiciones, por ejemplo Pedro 4 y Luis 1. La respuesta 1 supone que Ramón tiene al menos un hijo. La academia debe confirmar la clave.",
  mistake="Aceptar R = 0 sin notar que el enunciado se refiere a los hijos de Ramón.")

F(14, "ineq", 3, "La tercera parte de cierto número disminuida en 3 es mayor que 25; pero la cuarta parte del mismo número disminuida en 2 es menor que 20. ¿De qué número como máximo se trata?",
  ["84", "85", "87", "81", "80"], ["84", "85", "87", "81", "80"], "max(select(N->N/3-3>25 and N/4-2<20,seq(N,N,0,200)))", PLAN, "Dos desigualdades sobre el mismo número, que se asume entero.",
  ["N/3 − 3 > 25 da N/3 > 28, es decir N > 84.", "N/4 − 2 < 20 da N/4 < 22, es decir N < 88.", "84 < N < 88: N puede ser 85, 86 o 87. El máximo es 87."], "Con N = 87: 29 − 3 = 26 > 25 y 21,75 − 2 = 19,75 < 20.", ok="c", **LAB, note="Se entiende que el número es entero.", mistake="Marcar 85, el menor valor posible, en vez del máximo.")

F(15, "ineq", 3, "El número de plumas contenidas en una caja es tal, que su duplo disminuido en 86, es mayor que 200. De la caja se sacan 17 plumas y quedan menos que la diferencia entre 200 y la mitad de plumas que había al inicio. ¿Cuántas eran éstas?",
  ["140", "142", "144", "146", "138"], ["140", "142", "144", "146", "138"], "max(select(n->2*n-86>200 and n-17<200-n/2,seq(n,n,100,200)))", PLAN, "Es el mismo problema que el 11 de esta ficha.",
  ["2n − 86 > 200 da n > 143.", "n − 17 < 200 − n/2 da n < 144,67.", "n = 144."], "Con n = 144: 202 > 200 y 127 < 128.", ok="c", **LAB, note="Este problema es igual al 11 de la misma ficha, con otras alternativas.", mistake="Elegir 146 o 142 por cercanía sin comprobar la segunda condición.")

F(16, "ineq", 3, "En un examen de RM 3 alumnos: A, B y C, resolvieron correctamente más de 13 problemas en total. Si A hubiera resuelto 3 problemas más, habría tenido mayor número de problemas resueltos que B y C juntos, sin embargo, A resolvió menor cantidad de problemas que C, y los que éste resolvió no llegaron a 8. Calcule cuántos problemas resolvió B.",
  ["1", "2", "0", "3", "4"], ["1", "2", "0", "3", "4"],
  "select(B->size(select(A->size(select(C->A+B+C>13 and A+3>B+C and A<C and C<8,seq(C,C,0,20)))>0,seq(A,A,0,20)))>0,seq(B,B,0,20))[0]",
  PLAN, "Se escribe cada dato como desigualdad y se acota C, luego A y luego B.",
  ["Datos: A + B + C > 13, A + 3 > B + C, A < C y C < 8.", "C ≤ 7 y A < C, así que A ≤ 6.", "De A + 3 > B + C se tiene B + C ≤ A + 2, y entonces A + B + C ≤ 2A + 2. Como A + B + C ≥ 14, resulta 2A + 2 ≥ 14, es decir A ≥ 6.", "A = 6 y C = 7. Entonces B + 7 ≤ 8 da B ≤ 1, y 6 + B + 7 ≥ 14 da B ≥ 1. B = 1."],
  "Con A = 6, B = 1 y C = 7: 14 > 13, 9 > 8, 6 < 7 y 7 < 8.", ok="a", **LAB, mistake="Dejar B = 0 por ser el menor, alternativa c. Con B = 0 la suma sería 13 y no cumple más de 13.")

F(17, "ineq", 3, "Se tiene un número de dos cifras. El duplo de la cifra de las decenas, restado de la cifra de las unidades, es mayor que 5, y la diferencia entre 14 veces la cifra de las unidades y la cifra de las decenas es menor que 112. ¿Cuál es el número?",
  ["81", "18", "19", "91", "72"], ["81", "18", "19", "91", "72"], "select(n->irem(n,10)-2*iquo(n,10)>5 and 14*irem(n,10)-iquo(n,10)<112,seq(n,n,10,99))[0]",
  PLAN, "Con d decenas y u unidades, el duplo de d restado de u es u − 2d.",
  ["Condiciones: u − 2d > 5 y 14u − d < 112.", "De la primera, u ≥ 2d + 6. Con d = 1: u = 8 o 9. Con d = 2: u ≥ 10, imposible.", "Con d = 1: 14u − 1 < 112 da u < 8,07, así que u = 8.", "El número es 18."],
  "u − 2d = 8 − 2 = 6 > 5 y 14·8 − 1 = 111 < 112.", ok="b", **LAB, note="Restado de la cifra de las unidades significa unidades menos el duplo de las decenas.",
  mistake="Calcular 2d − u en lugar de u − 2d. Con 2d − u > 5 varios números cumplen, como 81, 91 y 72.")

F(18, "ineq", 3, "Yo tengo tres canicas más que tú. Si duplicase el número de canicas que tengo y tú triplicases el número de canicas que tienes, el producto del número de canicas que tendríamos sería mayor que 1080. ¿Cuántas canicas tengo como mínimo?",
  ["15", "12", "13", "14", "16"], ["15", "12", "13", "14", "16"], "min(select(a->2*a*3*(a-3)>1080,seq(a,a,3,100)))", PLAN, "Si t son las canicas del otro, yo tengo t + 3. Se obtiene una desigualdad cuadrática.",
  ["Yo: t + 3. Duplicado: 2(t + 3). Tú triplicado: 3t.", "6t(t + 3) > 1080, es decir t² + 3t − 180 > 0.", "Se factoriza: (t − 12)(t + 15) > 0. Como t > 0, t > 12 y t ≥ 13.", "Yo tengo t + 3 ≥ 16."], "Con t = 13: 6·13·16 = 1248 > 1080. Con t = 12: 6·12·15 = 1080, que no es mayor.", ok="e", **LAB,
  mistake="Tomar t = 12 y responder 15, alternativa a. Con t = 12 el producto vale justo 1080 y no lo supera.")

F(19, "ineq", 3, "El número de manzanas que tengo está entre 370 y 470. Si las cuento por decenas me sobran 7, si las cuento por docenas me sobran 9 y si las cuento de 15 en 15 me sobran 12. ¿Cuántas manzanas tengo?",
  ["437", "417", "427", "447", "457"], ["437", "417", "427", "447", "457"], "select(N->irem(N,10)==7 and irem(N,12)==9 and irem(N,15)==12,seq(N,N,371,469))[0]",
  "Si al dividir entre varios números sobra siempre una cantidad que le falta 3 a cada divisor, el número más 3 es múltiplo de todos los divisores.", "Los residuos 7, 9 y 12 son los divisores menos 3.",
  ["N deja 7 al dividir entre 10, es decir N + 3 es múltiplo de 10. Igual para 12 (9) y 15 (12).", "N + 3 es múltiplo de MCM(10, 12, 15) = 60.", "N = 60k − 3: 357, 417, 477, …", "El único entre 370 y 470 es 417."], "417 = 41·10 + 7 = 34·12 + 9 = 27·15 + 12.", ok="b", **LAB, mistake="Elegir 437, que deja 7 con 10 pero no cumple con 12.")

F(20, "ineq", 2, "La cantidad de canicas que tiene Jaimito es tal que, uno más el triple de dicho número es menor que 46; y uno más su cuádruplo es más que 53. ¿Cuántas canicas tiene Jaimito?",
  ["10", "11", "12", "13", "14"], ["10", "11", "12", "13", "14"], "select(x->1+3*x<46 and 1+4*x>53,seq(x,x,0,60))[0]", PLAN, "Dos desigualdades sobre las canicas x.",
  ["1 + 3x < 46 da x < 15.", "1 + 4x > 53 da x > 13.", "13 < x < 15 y x entero: x = 14."], "Con x = 14: 43 < 46 y 57 > 53.", ok="e", **LAB, mistake="Quedarse con 13 o 15, que no cumplen alguna de las desigualdades estrictas.")

F(21, "ineq", 3, "La edad de A es a la de B como 7 es a 9. Si el quíntuplo de A excede al triple de B en más de 16 años y B no es mayor que 40 años, entonces la máxima y mínima edad que puede tener A es:",
  ["21 y 18", "24 y 21", "26 y 24", "32 y 25", "28 y 21"], ["[21,18]", "[24,21]", "[26,24]", "[32,25]", "[28,21]"],
  "[7*max(select(k->35*k-27*k>16 and 9*k<=40,seq(k,k,1,20))),7*min(select(k->35*k-27*k>16 and 9*k<=40,seq(k,k,1,20)))]",
  PLAN + " Una razón 7 a 9 se escribe A = 7k y B = 9k.", "Se escribe todo con k y se busca los valores enteros de k.",
  ["A = 7k y B = 9k.", "5A − 3B > 16 da 35k − 27k > 16, es decir 8k > 16 y k > 2.", "B ≤ 40 da 9k ≤ 40, es decir k ≤ 4,4.", "k = 3 o 4. A = 21 o 28: máxima 28 y mínima 21."], "Con k = 3: A = 21 y B = 27, y 105 − 81 = 24 > 16. Con k = 4: A = 28 y B = 36, y 140 − 108 = 32 > 16.", ok="e", **LAB,
  mistake="Permitir k = 2: A = 14 y B = 18, con 70 − 54 = 16, que no es más de 16.")

F(22, "ineq", 3, "Iván tiene más de 3 hijos y Pedro menos de 9 hijos. Si juntos tienen menos de 8 hijos, ¿Cuántos miembros como máximo tiene la familia de Pedro en la casa donde viven, sabiendo que además vive con su suegro y su suegra?",
  ["5", "9", "8", "6", "7"], ["5", "9", "8", "6", "7"], "max(select(P->P<9 and size(select(I->I>3 and I+P<8,seq(I,I,0,20)))>0,seq(P,P,0,20)))+4", PLAN,
  "Iván tiene al menos 4 hijos, así que a Pedro le quedan pocos.",
  ["Iván tiene al menos 4 hijos. Juntos tienen menos de 8, es decir a lo sumo 7.", "Pedro tiene como máximo 7 − 4 = 3 hijos.", "Familia de Pedro: él, su esposa, sus 3 hijos, su suegro y su suegra: 1 + 1 + 3 + 2 = 7."], "Con 3 hijos Pedro e Iván suman 7 hijos, que es menos de 8.", ok="e", **LAB,
  note="Se cuenta a Pedro y a su esposa, ya que el enunciado dice que vive con su suegro y su suegra.", mistake="No contar a la esposa de Pedro y obtener 6, alternativa d.")

F(23, "ineq", 3, "En el mes de noviembre, cumplió años Cecilia; si el triple del día en que nació ella es menor que el día en que nació Alfredo, y además el día en que nació Cecilia supera al mes en que nació Alfredo. Si Alfredo nació después del mes de mayo y un día que tiene la particularidad de tener sus cifras iguales. ¿En qué fechas cumplieron años Cecilia y Alfredo?",
  ["7 de noviembre - 11 de junio", "7 de noviembre - 22 de junio", "6 de junio - 11 de noviembre", "9 de noviembre - 22 de junio", "N.A."],
  ["(3*7<11 and 7>6 and 6>=6)", "(3*7<22 and 7>6 and 6>=6)", "(6==11 and 3*6<11 and 6>11)", "(3*9<22 and 9>6 and 6>=6)", "false"], "true",
  "Se traducen las condiciones a desigualdades sobre el día c de Cecilia, el día a de Alfredo y su mes m.", "Alfredo nació un día de cifras iguales: 11 o 22. Cecilia en noviembre.",
  ["Condiciones: 3c < a, c > m, m ≥ 6 y a = 11 o 22.", "Si a = 11, 3c < 11 da c ≤ 3, pero c > m ≥ 6. Imposible.", "Si a = 22, 3c < 22 da c ≤ 7, y c > m ≥ 6 da c = 7 y m = 6.", "Cecilia: 7 de noviembre. Alfredo: 22 de junio."], "3·7 = 21 < 22, 7 > 6 y junio es el mes 6, después de mayo.", ok="b", **LAB,
  note="Cada alternativa se comprueba como una condición que debe ser verdadera: solo la b cumple todo.", mistake="Elegir la a, que tiene 11 de junio. Con a = 11, el triple del día de Cecilia ya no es menor que a.")

F(24, "ineq", 3, "Si el número de coches se redujera a la sexta parte se ocuparía menos de la décima parte de la capacidad del estacionamiento; pero si se tratara de duplicar el número de coches; más de ocho coches no podrán ser estacionados por falta de espacio. ¿Cuántos coches hay en el estacionamiento?",
  ["24", "35", "36", "28", "61"], ["24", "35", "36", "28", "61"], "floor(solve(5*N/3=2*N-8,N)[0])+1", PLAN, "Con N coches y capacidad K, el enunciado da dos desigualdades con dos incógnitas.",
  ["N/6 < K/10 da K > 5N/3.", "2N − K > 8 da K < 2N − 8.", "5N/3 < K < 2N − 8 exige 5N/3 < 2N − 8, es decir N > 24.", "Con esa información cualquier N mayor que 24 con un K entero entre esos límites sirve: por ejemplo N = 28 con K = 47, o N = 35 con K = 60."],
  "Con N = 35 y K = 60: 35/6 = 5,8 < 6 y 70 − 60 = 10 > 8. Con N = 28 y K = 47: 4,67 < 4,7 y 56 − 47 = 9 > 8.",
  answer="Solo se puede concluir que N es mayor que 24. Varias alternativas sirven.", answerVal="25",
  warn="Con los datos de la ficha, el número de coches no queda determinado: falta la capacidad del estacionamiento. Varias alternativas cumplen.", review=True, **LAB,
  mistake="Marcar una alternativa sin probar que las otras no sirven. Las alternativas 28, 35, 36 y 61 cumplen las dos condiciones.")

F(25, "ineq", 2, "Si el doble de la edad de Ana no es menor que 57 y el triple de la edad de Ana no es mayor que 96. ¿Cuál es la máxima diferencia entre la edad de Ana con la de Luis que tiene 20 años?",
  ["10", "11", "12", "13", "14"], ["10", "11", "12", "13", "14"], "max(seq(abs(a-20),a,ceil(57/2),floor(96/3)))", PLAN, "No es menor que se escribe ≥ y no es mayor que se escribe ≤.",
  ["2a ≥ 57 da a ≥ 28,5, es decir a ≥ 29 si la edad es entera.", "3a ≤ 96 da a ≤ 32.", "La edad de Ana está entre 29 y 32. La diferencia con 20 es mayor para a = 32: 12."], "Con a = 32: 64 ≥ 57 y 96 ≤ 96, y 32 − 20 = 12.", ok="c", **LAB, note="Se entiende que las edades son enteras.", mistake="Tomar a = 28,5 y obtener 8,5, que no está entre las alternativas.")

F(26, "ineq", 2, "La cantidad de canicas que tiene Jaimito es tal que, uno más el triple de dicho número es menor que 46; y uno más su cuádruplo es más que 53. ¿Cuántas canicas tiene Jaimito?",
  ["10", "11", "12", "13", "14"], ["10", "11", "12", "13", "14"], "select(x->1+3*x<46 and 1+4*x>53,seq(x,x,0,60))[0]", PLAN, "Es el mismo problema que el 20 de esta ficha.",
  ["1 + 3x < 46 da x < 15.", "1 + 4x > 53 da x > 13.", "x = 14."], "Con x = 14: 43 < 46 y 57 > 53.", ok="e", **LAB, note="Este problema es igual al 20 de la misma ficha.", mistake="Quedarse con 13 o 15.")

F(27, "ineq", 2, "El quíntuplo del número de nietos que tiene don Federico, disminuido en 1 es menor que el cuadrado de 7; y siete veces dicho número, aumentado en 8, excede al cuadrado de 8. ¿Cuántos nietos tiene don Federico?",
  ["8", "9", "10", "7", "11"], ["8", "9", "10", "7", "11"], "select(n->5*n-1<49 and 7*n+8>64,seq(n,n,0,50))[0]", PLAN, "El cuadrado de 7 es 49 y el de 8 es 64.",
  ["5n − 1 < 49 da 5n < 50, es decir n < 10.", "7n + 8 > 64 da 7n > 56, es decir n > 8.", "8 < n < 10: n = 9."], "Con n = 9: 44 < 49 y 71 > 64.", ok="b", **LAB, mistake="Confundir el cuadrado de 7 con su doble, 14. El cuadrado de 7 es 49.")

F(28, "ineq", 3, "En un aula de la ACADEMIA YOMI, el número de varones es al de mujeres como 3 es a 2. El triple del número de alumnos, menos 10 es menor que 66, mas 1 es mayor que 50. Hallar el número de mujeres del aula.",
  ["6", "12", "15", "10", "5"], ["6", "12", "15", "10", "5"], "max(select(n->irem(n,5)==0 and 3*n-10<66 and 3*n-10+1>50,seq(n,n,1,100)))*2/5", PLAN + " Una razón 3 a 2 hace que el total sea múltiplo de 5.", "Con n alumnos, n es múltiplo de 5 y las mujeres son 2n/5.",
  ["Varones 3k y mujeres 2k, con n = 5k alumnos.", "3n − 10 < 66 da n < 25,3. Y 3n − 10 + 1 > 50 da n > 19,7.", "n entero y múltiplo de 5 con 19,7 < n < 25,3: n = 20 o n = 25.", "Mujeres: 2k = 8 si n = 20 o 10 si n = 25. Solo 10 está entre las alternativas."], "Con n = 25: 3·25 − 10 = 65 < 66 y 65 + 1 = 66 > 50.", ok="d", **LAB,
  note="El enunciado admite también 8 mujeres, pero ese valor no está entre las alternativas. La respuesta de la ficha es 10.", mistake="No notar que n = 20 también cumple y quedarse con la primera idea.")

F(29, "ineq", 2, "Hallar el número entero y positivo que sumado con 11 resulte mayor que el triple de él, disminuido en 7 y que sumado con 5 resulte menos que el doble de él; disminuido en 2.",
  ["6", "7", "8", "9", "10"], ["6", "7", "8", "9", "10"], "select(n->n+11>3*n-7 and n+5<2*n-2,seq(n,n,1,50))[0]", PLAN, "El número es n: n + 11 > 3n − 7 y n + 5 < 2n − 2.",
  ["n + 11 > 3n − 7 da 18 > 2n, es decir n < 9.", "n + 5 < 2n − 2 da 7 < n.", "7 < n < 9: n = 8."], "Con n = 8: 19 > 17 y 13 < 14.", ok="c", **LAB, mistake="Elegir 9 por seguir la desigualdad de arriba. Con 9, 20 > 20 es falso.")
