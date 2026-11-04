"""Ficha de Estadística, semana 4, de Sergio Huanca: probabilidades y repaso. 27 preguntas, 4 alternativas."""
from model import ficha
F = ficha(4, "est")
L4 = dict(letters="abcd")
TF = "true"
PRB = ("Probabilidad de un suceso: casos favorables entre casos posibles, cuando todos son igual de probables. Si dos sucesos son independientes, la probabilidad de que ocurran ambos es el producto. "
       "La probabilidad de al menos uno se calcula como 1 menos la probabilidad de que no ocurra ninguno.")
MX = "seq(max(when((irem(n,2)==0 and n<8) or (irem(n,2)==1 and n>7),1,0),when(n>6,2,0),when(irem(n,2)==1 and n<9,8,0),when(irem(n,2)==0 and n>14,1000,0)),n,1,15)"
HUGO = ("El reconocido científico Hugo Heredia hizo una investigación sobre las virtudes que poseen cada hombre de Stone Bridge City, a través de una encuesta (virtudes indispensables según un grupo selecto de 1000 mujeres a nivel global), y descubrió lo siguiente:\n"
        "a. Posee recursos económicos: 1 de cada 2 hombres.\nb. Es ambicioso y estudioso: 1 de cada 4 hombres.\nc. Le demuestra amor y compromiso: 1 de cada 2 hombres\nd. Es emocionalmente estable: 1 de cada 4 hombres\ne. Buena presencia y salud: 1 de cada 2 hombres\nResponder las preguntas 16, 17 y 18.\n")

F(1, "prob", 1, "¿Cuál es la probabilidad de obtener dos sellos en el lanzamiento de tres monedas?", ["S/ 242", "S/ 252", "S/ 258", "S/ 260"], ["242", "252", "258", "260"], "binomial(3,2)/8", PRB, "Hay 8 resultados posibles al lanzar tres monedas.",
  ["Resultados posibles: 2·2·2 = 8.", "Con exactamente dos sellos: C(3, 2) = 3 resultados: SSC, SCS y CSS.", "Probabilidad: 3/8."], "3/8 = 0,375.", answer="3/8. Las alternativas están en soles y no corresponden a una probabilidad.", answerVal="3/8",
  warn="Con el enunciado tal cual, la respuesta es 3/8. Las alternativas están en soles, que no tiene sentido para una probabilidad, y ninguna recoge 3/8. La ficha tiene una errata.",
  note="Si fuera al menos dos sellos, la probabilidad sería 4/8 = 1/2.", review=True, **L4, mistake="Elegir una alternativa en soles por no saber cuál usar.")

F(2, "prob", 2, "Al lanzar dos dados convencionales, ¿cuál es la probabilidad de que la suma de sus puntos sea un número primo?", ["5/12", "7/18", "1/3", "15/36"], ["5/12", "7/18", "1/3", "15/36"], "(1+2+4+6+2)/36",
  PRB + " Los números primos hasta 12 son 2, 3, 5, 7 y 11.", "Se cuentan las formas de obtener cada suma prima.",
  ["Suma 2: 1 forma. Suma 3: 2. Suma 5: 4. Suma 7: 6. Suma 11: 2.", "Casos favorables: 1 + 2 + 4 + 6 + 2 = 15 de 36.", "15/36 = 5/12."], "15/36 se simplifica dividiendo entre 3.", ok="a", also=(3,), **L4,
  warn="Las alternativas a y d valen lo mismo: 5/12 es igual a 15/36. Las dos son correctas.", review=True,
  note="En la app se califican como correctas las dos alternativas.", mistake="Contar las sumas primas sin ponderar por sus formas: 5 sumas de 11 posibles.")

F(3, "prob", 1, "Se lanzan dos dados, ¿cuál es la probabilidad de obtener una suma menor que 6?", ["5/18", "7/36", "5/36", "1/4"], ["5/18", "7/36", "5/36", "1/4"], "(1+2+3+4)/36", PRB, "Las sumas menores que 6 son 2, 3, 4 y 5.",
  ["Suma 2: 1 forma. Suma 3: 2. Suma 4: 3. Suma 5: 4.", "Casos favorables: 10 de 36.", "10/36 = 5/18."], "10/36 ≈ 0,278.", ok="a", **L4, mistake="Contar solo 4 sumas distintas y obtener 4/36.")

F(4, "prob", 1, "De una baraja de 52 cartas se extrae una al azar. ¿Cuál es la probabilidad de que sea una figura (J, Q o K) de color negro?", ["3/13", "6/13", "3/26", "1/26"], ["3/13", "6/13", "3/26", "1/26"], "6/52", PRB,
  "Hay 4 palos: 2 negros y 2 rojos. Cada palo tiene 3 figuras J, Q y K.", ["Palos negros: 2. Figuras por palo: 3.", "Casos favorables: 2·3 = 6 de 52.", "6/52 = 3/26."], "3/26 ≈ 0,115.", ok="c", **L4, mistake="Contar las 12 figuras y obtener 12/52 = 3/13, alternativa a.", trap="12/52", trap_opt="a")

F(5, "prob", 2, "Una urna contiene tarjetas numeradas del 12 al 35 inclusive. Si se extrae una tarjeta al azar, ¿cuál es la probabilidad de que su suma de cifras sea un número impar?", ["1/2", "11/24", "13/24", "7/12"], ["1/2", "11/24", "13/24", "7/12"],
  "size(select(n->irem(iquo(n,10)+irem(n,10),2)==1,seq(n,n,12,35)))/(35-12+1)", PRB, "Hay 35 − 12 + 1 = 24 tarjetas. Se cuentan las de suma de cifras impar.",
  ["Del 12 al 19: 12, 14, 16 y 18, 4 números.", "Del 20 al 29: 21, 23, 25, 27 y 29, 5 números.", "Del 30 al 35: 30, 32 y 34, 3 números.", "Casos favorables: 4 + 5 + 3 = 12 de 24. Probabilidad 1/2."], "En cada decena se alternan pares e impares de suma de cifras.", ok="a", **L4, mistake="Contar mal una decena y obtener 11/24 o 13/24.")

F(6, "prob", 1, "Marina tiene tres frascos con caramelos que solo se distinguen por el color.\nFrasco 1: 6 rojos, 7 verdes y 5 blancos\nFrasco 2: 7 rojos, 1 verde y 1 blanco\nFrasco 3: 1 verde, 1 rojo y 10 blancos\nSi se une el contenido de los tres frascos en un frasco nuevo y se extrae al azar un caramelo de dicho frasco, ¿cuál de las afirmaciones es correcta?",
  ["Es posible obtener un caramelo azul", "Es seguro obtener un caramelo blanco", "Es posible obtener un caramelo verde", "Es imposible obtener un caramelo verde"], ["false", "false", "(7+1+1>0)", "(7+1+1==0)"], TF,
  "Un suceso es seguro si tiene probabilidad 1, imposible si tiene probabilidad 0 y posible si está entre 0 y 1.", "Se mira si hay caramelos de cada color en el frasco nuevo.",
  ["Verdes: 7 + 1 + 1 = 9. Hay, así que sacar uno verde es posible.", "Blancos: 5 + 1 + 10 = 16, pero también hay rojos y verdes: no es seguro.", "Azules: no hay ninguno, así que sacar uno azul es imposible."], "Total de caramelos: 18 + 9 + 12 = 39, con rojos 14, verdes 9 y blancos 16.", ok="c", **L4,
  note="Las alternativas se comprueban como afirmaciones: solo la c es verdadera.", mistake="Elegir la b porque los blancos son la mayoría. Mayoría no es lo mismo que seguro.")

F(7, "prob", 1, "Una perrita tiene una camada de 4 cachorros. ¿Cuál es la probabilidad de que nazcan exactamente tres machos?", ["1/2", "5/16", "1/4", "3/8"], ["1/2", "5/16", "1/4", "3/8"], "binomial(4,3)/2^4", PRB, "Cada cachorro es macho o hembra con igual probabilidad. Hay 2⁴ = 16 combinaciones.",
  ["Con exactamente tres machos: C(4, 3) = 4 combinaciones.", "Probabilidad: 4/16 = 1/4."], "Las combinaciones son MMMH, MMHM, MHMM y HMMM.", ok="c", **L4, mistake="Calcular 3/4, la fracción de machos, sin considerar todas las combinaciones.")

F(8, "prob", 2, "En un salón de 50 alumnos, 35 postulan a San Marcos y 25 a la UNI. Si 10 postulan a ambas, ¿cuál es la probabilidad de que al elegir un alumno al azar postule solo a una de ellas?", ["2/5", "3/5", "1/2", "3/10"], ["2/5", "3/5", "1/2", "3/10"], "((35-10)+(25-10))/50",
  PRB + " Para dos conjuntos: solo uno = total de cada uno menos la parte común.", "Se restan los que postulan a ambas de cada grupo.",
  ["Solo San Marcos: 35 − 10 = 25. Solo la UNI: 25 − 10 = 15.", "Solo una universidad: 25 + 15 = 40 alumnos de 50.", "Probabilidad: 40/50 = 4/5."], "Los que postulan a alguna: 35 + 25 − 10 = 50, todo el salón.",
  answer="4/5. Ninguna alternativa lo recoge.", answerVal="4/5", warn="Con el enunciado tal cual, la probabilidad es 4/5 y ninguna alternativa lo recoge. La ficha tiene una errata.", review=True, **L4,
  mistake="Marcar 3/5 o 3/10 por parecerse a los datos. Con estos datos todos los alumnos postulan a alguna universidad.")

F(9, "prob", 3, "En un instituto superior, el 60% de los estudiantes son varones. De las mujeres, el 30% estudia becadas, mientras que de los varones el 40% estudia becados. Si se elige un estudiante al azar y resulta ser becado, ¿cuál es la probabilidad de que sea mujer?",
  ["12/35", "18/35", "9/35", "9/27"], ["12/35", "18/35", "9/35", "9/27"], "(4/10*3/10)/(4/10*3/10+6/10*4/10)", PRB + " Probabilidad condicional: entre los becados, la parte que son mujeres.", "Se calcula qué fracción del total son mujeres becadas y varones becados.",
  ["Mujeres becadas: 40% · 30% = 12% del total.", "Varones becados: 60% · 40% = 24% del total.", "Becados: 12% + 24% = 36%. Entre ellos, mujeres: 12/36 = 1/3.", "9/27 = 1/3."], "12/36 y 9/27 son la misma fracción, 1/3.", ok="d", **L4, mistake="Calcular 30% de 40% o dividir entre el total de estudiantes, y obtener 12%.")

F(10, "prob", 3, "Mía participa en un juego de la feria de su ciudad, en el cual se ofrece que todos reciban un premio. El juego consiste en sacar una bola de billar (numeradas del 1 al 15) de una canasta y escoger solo un premio de la siguiente tabla, según el número indicado en la bola de billar. Cada vez que Mía juega debe pagar S/ 5 por el ticket.\n[tabla]\nSegún esta información, ¿cuál de las siguientes afirmaciones es falsa?",
  ["Es seguro que Mía reciba al menos S/ 20 si juega 10 veces.", "Es posible que Mía pierda S/ 3 en una jugada.", "Es imposible que Mía gane S/ 1 000 en una jugada.", "Es seguro que Mía reclame S/ 1 de premio como mínimo en una jugada."],
  ["not(10*min(%s)>=20)" % MX, "not(size(select(v->v==2,%s))>0)" % MX, "not(size(select(v->v==1000,%s))==0)" % MX, "not(min(%s)>=1)" % MX], TF,
  PRB, "Se mira qué premio se puede reclamar con cada bola, del 1 al 15.",
  ["Premio S/ 1: pares menores que 8 e impares mayores que 7. Premio S/ 2: números mayores que 6. Premio S/ 8: impares menores de 9. Premio S/ 1 000: pares mayores que 14, y no hay ninguno entre 1 y 15.", "Con las bolas 2, 4 y 6 el mejor premio es S/ 1. Entonces en 10 jugadas se puede ganar solo S/ 10, no 20. La afirmación a es falsa.", "La b es verdadera: con la bola 8 se gana S/ 2 y se pierde S/ 3. La c es verdadera: no hay bola par mayor que 14. La d es verdadera: toda bola da al menos S/ 1."], "Los premios posibles por bola son 1, 2 u 8, y ninguno llega a 1000.", ok="a", **L4,
  table=[["Resultado", "Premio"], ["Obtener un número par menor que 8 o impar mayor que 7", "S/ 1,00"], ["Obtener un número mayor que 6", "S/ 2,00"], ["Obtener un número impar menor de 9", "S/ 8,00"], ["Obtener un número par mayor a 14", "S/ 1 000,00"]],
  note="Las alternativas se comprueban buscando cuál afirmación es falsa. Se supone que Mía reclama el mayor premio posible con cada bola.", mistake="Sumar S/ 2 por jugada y concluir que se ganan al menos S/ 20 en diez jugadas.")

F(11, "prob", 2, "Se lanzan simultáneamente una moneda y dos dados. ¿Cuál es la probabilidad de obtener cara en la moneda y una suma de 7 en los dados?", ["1/12", "1/24", "5/36", "1/6"], ["1/12", "1/24", "5/36", "1/6"], "1/2*6/36", PRB, "La moneda y los dados son independientes: se multiplican las probabilidades.",
  ["Cara: 1/2.", "Suma 7 con dos dados: 6 de 36, es decir 1/6.", "Probabilidad: 1/2 · 1/6 = 1/12."], "1/12 ≈ 0,083.", ok="a", **L4, mistake="Sumar 1/2 + 1/6 en lugar de multiplicar.")

F(12, "prob", 2, "Si lanzamos 3 dados, ¿cuál es la probabilidad de obtener 4 como suma de puntos?", ["1/216", "5/72", "5/216", "1/72"], ["1/216", "5/72", "5/216", "1/72"], "3/216", PRB, "Con tres dados, la suma 4 solo se logra con 1, 1 y 2.",
  ["Resultados posibles: 6³ = 216.", "Combinaciones con suma 4: 1 + 1 + 2 en 3 órdenes: 112, 121 y 211.", "Probabilidad: 3/216 = 1/72."], "El mínimo de tres dados es 3, y 4 es la suma siguiente.", ok="d", **L4, mistake="Contar una sola combinación y obtener 1/216.", trap="1/216", trap_opt="a")

F(13, "prob", 1, "Una maestra realiza una actividad para recordar las propiedades de los números primos y probabilidades. Ella ha traído una bolsa que contiene tarjetas. Cada una de estas tarjetas tiene escrito solo uno de los siguientes números primos: 2, 3, 5, 7, 11, 13, 17, 19.\nLa maestra le pide a Dora que extraiga, al azar, una tarjeta de la bolsa.\nSegún esta información, ¿cuál es la probabilidad que Dora extraiga una tarjeta que contenga un número primo?",
  ["7/8", "4/8", "1", "0"], ["7/8", "4/8", "1", "0"], "8/8", PRB, "Todas las tarjetas tienen números primos.", ["Hay 8 tarjetas y las 8 tienen un número primo.", "Casos favorables: 8 de 8. Probabilidad 1, que es un suceso seguro."], "Ningún número de la lista es compuesto.", ok="c", **L4, mistake="Contar los números impares y obtener 7/8, por excluir el 2.")

F(14, "prob", 2, "¿Cuál es la probabilidad de que al ordenar en fila a 2 hombres y 2 mujeres, los 2 hombres estén juntos?", ["3/4", "1/4", "1/3", "1/2"], ["3/4", "1/4", "1/3", "1/2"], "(factorial(3)*factorial(2))/factorial(4)", PRB, "Los dos hombres juntos se cuentan como un bloque.",
  ["Total de ordenaciones de 4 personas: 4! = 24.", "Con los hombres juntos: el bloque y las dos mujeres se ordenan de 3! = 6 maneras, y los hombres entre sí de 2! = 2. Total 12.", "Probabilidad: 12/24 = 1/2."], "Con letras HHMM hay 6 ordenaciones y 3 tienen los hombres juntos: HHMM, MHHM y MMHH.", ok="d", **L4, mistake="Olvidar que los hombres se pueden ordenar entre sí y obtener 6/24 = 1/4.")

F(15, "prob", 2, "La probabilidad de que el futbolista más conocido como \"El comandante\" anote un gol al patear un penal es 0,9. ¿Cuál es la probabilidad de que anote por lo menos un gol al patear 2 penales?", ["0,84", "0,9", "0,09", "0,99"], ["84/100", "9/10", "9/100", "99/100"], "1-(1-9/10)^2", PRB, "Por lo menos un gol es lo contrario de fallar los dos.",
  ["Probabilidad de fallar un penal: 1 − 0,9 = 0,1.", "Fallar los dos: 0,1 · 0,1 = 0,01.", "Por lo menos un gol: 1 − 0,01 = 0,99."], "0,9 + 0,1·0,9 = 0,99.", ok="d", **L4, note="Las alternativas se comprueban como fracciones.", mistake="Responder 0,9, alternativa b, que es la probabilidad de un solo penal.", trap="9/10", trap_opt="b")

F(16, "prob", 2, HUGO + "¿Cuál es la probabilidad de que ella encuentre a su hombre ideal que cumpla todas las características?", ["1 de cada 64 hombres", "1 de cada 128 hombres", "1 de cada 16 hombres", "Solo vive en Tik Tok"], ["1/64", "1/128", "1/16", "undef"], "(1/2)*(1/4)*(1/2)*(1/4)*(1/2)", PRB,
  "Si las características son independientes, se multiplican las probabilidades de las cinco.", ["Las cinco probabilidades son 1/2, 1/4, 1/2, 1/4 y 1/2.", "Producto: 1/2 · 1/4 · 1/2 · 1/4 · 1/2 = 1/128."], "2·4·2·4·2 = 128.", ok="b", **L4, note="El enunciado y las preguntas 16 a 18 son compartidos. La alternativa d es una broma de la ficha.", mistake="Contar solo los incisos con 1/4 y obtener 1/16.")

F(17, "prob", 2, HUGO + "Si ella decide obviar el inciso c y d para tener mayores opciones, ¿cuál es la probabilidad de encontrar a un hombre compatible?", ["1 de cada 64 hombres", "1 de cada 8 hombres", "1 de cada 16 hombres", "Regreso con mi ex"], ["1/64", "1/8", "1/16", "undef"], "(1/2)*(1/4)*(1/2)", PRB,
  "Se multiplican solo los incisos que quedan: a, b y e.", ["Incisos a, b y e: 1/2, 1/4 y 1/2.", "Producto: 1/16."], "2·4·2 = 16.", ok="c", **L4, mistake="Multiplicar también los incisos c y d y obtener 1/64, alternativa a.", trap="1/64", trap_opt="a")

F(18, "prob", 2, HUGO + "Si ella elige a un hombre que cumple con los incisos a,b y e, ¿cuál es la probabilidad de que este resulte ser su hombre ideal (cumple con todos los incisos)?", ["Me quedo soltera", "1 de cada 8 hombres", "1 de cada 16 hombres", "1 de cada 4 hombres"], ["undef", "1/8", "1/16", "1/4"], "(1/2)*(1/4)", PRB,
  "Ya cumple a, b y e. Solo faltan c y d.", ["Faltan los incisos c y d: 1/2 y 1/4.", "Producto: 1/8."], "2·4 = 8.", ok="b", **L4, mistake="Multiplicar los cinco incisos otra vez y obtener 1/128.")

F(19, "est", 2, "La edad promedio de 4 hermanos donde hay dos gemelos es 19 años, el menor tiene 12 años y el mayor tiene 24 años. ¿Cuál es la edad de los gemelos, en años?", ["16", "19", "20", "21"], ["16", "19", "20", "21"], "(4*19-12-24)/2", "Edad promedio: suma de las edades entre la cantidad de hermanos.", "Se resta del total las edades conocidas.",
  ["Suma de las 4 edades: 4 · 19 = 76.", "El menor y el mayor suman 12 + 24 = 36. Los gemelos suman 76 − 36 = 40.", "Cada gemelo tiene 40/2 = 20 años."], "12 + 20 + 20 + 24 = 76 y 76/4 = 19.", ok="c", **L4, mistake="Responder 19, el promedio, alternativa b.", trap="19", trap_opt="b")

F(20, "est", 2, "Siete compañeros de estudio asisten a una función de cine y adquieren diversas golosinas para consumir durante la película. Tres de ellos compran, cada uno, un combo económico de 13 soles que consiste en canchita y gaseosa. Otros dos consumen, cada uno, el mismo combo pero le agregan a cada combo un chocolate de 2 soles. Finalmente los últimos dos jóvenes comparten un combo gigante de 22 soles. Si cada entrada al cine tiene un costo de 15 soles, ¿cuál sería el gasto promedio total, en soles, por cada joven?",
  ["13", "22", "28", "31"], ["13", "22", "28", "31"], "(3*13+2*(13+2)+22+7*15)/7", "El gasto promedio por joven es el gasto total entre los 7 jóvenes.", "Se suman todos los gastos, incluidas las entradas.",
  ["Tres combos económicos: 3 · 13 = 39.", "Dos combos con chocolate: 2 · (13 + 2) = 30.", "Un combo gigante compartido: 22.", "Entradas: 7 · 15 = 105.", "Gasto total: 39 + 30 + 22 + 105 = 196. Promedio: 196/7 = 28."], "28 · 7 = 196.", ok="c", **L4, mistake="Olvidar las entradas y obtener 13.", trap="(3*13+2*(13+2)+22)/7", trap_opt="a")

F(21, "est", 2, "El gráfico muestra los niveles de exportación de cerveza, por marca, en el año 2017.\n¿Qué ángulo central correspondería a la marca \"Limeña\" si no se considera el rubro \"Otros\"?", ["45°", "50°", "60°", "80°"], ["45", "50", "60", "80"], "20/(100-10)*360",
  "En un gráfico circular, 360° corresponden al total que se considere.", "Sin Otros, el total es el 90% de las exportaciones.",
  ["Marcas sin Otros: 40% + 20% + 20% + 10% = 90%.", "Limeña es 20 de esos 90.", "Ángulo: 20/90 · 360° = 80°."], "40 + 20 + 20 + 10 = 90, y 90 de 90 son 360°.", ok="d", **L4, fig="fe4_cerveza",
  mistake="Calcular 20% de 360° = 72°, que no está entre las alternativas, olvidando quitar Otros.")

F(22, "prob", 2, "Al lanzar tres dados a la vez, ¿cuál es la probabilidad de obtener una suma menor a 5?", ["1/128", "1/144", "1/54", "1/72"], ["1/128", "1/144", "1/54", "1/72"], "(1+3)/216", PRB, "Con tres dados, las sumas menores que 5 son 3 y 4.",
  ["Suma 3: 1 forma, 1-1-1. Suma 4: 3 formas, 1-1-2 en 3 órdenes.", "Casos favorables: 4 de 6³ = 216.", "4/216 = 1/54."], "4/216 se simplifica dividiendo entre 4.", ok="c", **L4, mistake="Contar solo la suma 4 y obtener 3/216 = 1/72, alternativa d.", trap="3/216", trap_opt="d")

F(23, "porc", 1, "En un examen de selección con 120 preguntas, un estudiante respondió correctamente el 75% del total. ¿Cuántas preguntas respondió de forma errónea o dejó en blanco?", ["30", "25", "35", "40"], ["30", "25", "35", "40"], "120-75/100*120", "Un porcentaje r% de una cantidad es r/100 de ella.", "Lo que no respondió bien es el 25% restante.",
  ["Correctas: 75% de 120 = 90.", "Erróneas o en blanco: 120 − 90 = 30."], "25% de 120 = 30.", ok="a", **L4, mistake="Responder 90, las correctas, que no está entre las alternativas.")

F(24, "porc", 2, "Rodrigo tiene un cupón del 20% de descuento sobre el precio a pagar por cada artículo de una tienda. Al llegar a la tienda se da con la grata sorpresa de que el producto que desea llevar ya viene con un descuento del 30%. ¿Cuál es el descuento total que obtendrá Rodrigo si usa su cupón de descuento?",
  ["56%", "44%", "70%", "50%"], ["56", "44", "70", "50"], "(1-(8/10)*(7/10))*100", "Los descuentos sucesivos se multiplican: cada uno se aplica sobre el precio ya rebajado.", "Se paga 70% con el primer descuento y 80% de eso con el cupón.",
  ["Se paga 0,70 · 0,80 = 0,56 del precio.", "El descuento total es 100% − 56% = 44%."], "Con S/ 100: 70 y luego 56. Descuento 44.", ok="b", **L4, mistake="Sumar 20% + 30% = 50%, alternativa d.", trap="50", trap_opt="d")

F(25, "prob", 3, "Si se ordenaron al azar en una fila 6 impresoras de diferente precio, encuentre la probabilidad de que la impresora de mayor precio y la impresora de menor precio hayan quedado en los extremos de la fila.", ["2/15", "1/30", "1/5", "1/15"], ["2/15", "1/30", "1/5", "1/15"], "2*factorial(4)/factorial(6)", PRB,
  "Se cuentan las filas con la más cara y la más barata en los extremos y se divide entre todas las filas.", ["Filas posibles: 6! = 720.", "La más cara y la más barata en los extremos: 2 maneras, una en cada extremo. Las otras 4 se ordenan de 4! = 24 maneras. Total 48.", "Probabilidad: 48/720 = 1/15."], "2/6 · 1/5 = 1/15 si se coloca primero una y luego la otra.", ok="d", **L4, mistake="Olvidar que las dos pueden intercambiarse y obtener 1/30.", trap="factorial(4)/factorial(6)", trap_opt="b")

F(26, "porc", 3, "El gráfico muestra la distribución del número de postulantes a Beca 18 agrupados en cuatro regiones del sur del Perú. Si la población total de postulantes en estas cuatro regiones es de 120 000 personas, ¿cuál es la cantidad total de postulantes en la región Moquegua?",
  ["8000 postulantes", "9600 postulantes", "12000 postulantes", "38400 postulantes"], ["8000", "9600", "12000", "38400"], "solve(25+35+n+4*n=100,n)[0]/100*120000",
  "En un gráfico circular los porcentajes suman 100%.", "Se halla n con la suma de los porcentajes.",
  ["25% + 35% + n% + 4n% = 100%, así que 5n = 40 y n = 8.", "Moquegua es 8% de 120 000.", "8% de 120 000 = 9600 postulantes."], "Puno es 4n = 32%: 38 400 postulantes, alternativa d.", ok="b", **L4, fig="fe4_regiones",
  mistake="Responder 38 400, alternativa d, que es la región Puno, con 4n.", trap="32/100*120000", trap_opt="d")

F(27, "est", 1, "El gráfico muestra la distribución de la población de la ciudad de Cañete según sexo desde el 2015 al 2017.\n¿En qué porcentaje aumentó la población masculina de Cañete del 2015 al 2017?", ["20%", "25%", "30%", "15%"], ["20", "25", "30", "15"], "(75-60)/60*100",
  "El aumento porcentual es el aumento entre la cantidad inicial.", "Se leen las barras de los hombres: 60 en 2015 y 75 en 2017.", ["Aumento: 75 − 60 = 15 mil.", "15/60 = 0,25 = 25%."], "60 · 1,25 = 75.", ok="b", **L4, fig="fe4_canete", mistake="Dividir entre 75 y obtener 20%, alternativa a.", trap="(75-60)/75*100", trap_opt="a")
