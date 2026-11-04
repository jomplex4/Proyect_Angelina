"""Ficha de Estadística, semana 3, de Sergio Huanca: frecuencias y análisis combinatorio. 25 preguntas y 7 de Las fijas del gran Sergio, 4 alternativas."""
from model import ficha
F = ficha(3, "est")
L4 = dict(letters="abcd")
TF = "true"
FREQ = ("En una tabla de frecuencias agrupadas: fi es la frecuencia absoluta, Fi la acumulada, hi la relativa y Hi la relativa acumulada. "
        "La marca de clase de un intervalo es el promedio de sus extremos. La media aproximada es la suma de marca por frecuencia dividida entre el total. Los porcentajes salen de dividir entre el total.")
COMB = "Principio de multiplicación: si hay m maneras de hacer una cosa y n de hacer otra, hay m·n maneras de hacer ambas. Un orden con n objetos distintos tiene n! maneras. Combinaciones: C(n, k) elige k de n sin importar el orden."
PESO = "En un estudio sobre el peso (en kg) de 40 postulantes a una beca deportiva, se construyó la siguiente tabla de frecuencias agrupadas: (Responder desde el 1 al 5)\n[tabla]\n"
TPESO = [["Peso (kg)", "Xi", "fi", "Fi", "hi", "Hi"], ["[48; 54)", "51", "2", "2", "2/40", "2/40"], ["[54; 60)", "57", "6", "8", "6/40", "8/40"], ["[60; 66)", "63", "14", "22", "14/40", "22/40"],
         ["[66; 72)", "69", "10", "32", "10/40", "32/40"], ["[72; 78)", "75", "5", "37", "5/40", "37/40"], ["[78; 84)", "81", "3", "40", "3/40", "40/40"]]
T6 = [["Ii", "Xi", "fi", "Fi"], ["[ ; )", "", "5", ""], ["[16; )", "", "m", "12"], ["[ ; )", "n", "", ""], ["[28; k]", "", "10", "30"]]
T9 = [["Ventas (S/ 1000)", "fi", "Fi"], ["[2.0; 3.0)", "8", "8"], ["[3.0; 4.0)", "18", "26"], ["[4.0; 5.0)", "", "40"], ["[5.0; 6.0)", "6", "46"], ["[6.0; 7.0]", "4", "50"]]
T12 = [["Ingreso (S/)", "fi", "Fi", "hi"], ["[200; 250)", "15", "15", "0.15"], ["[250; 300)", "35", "50", "0.35"], ["[300; 350)", "", "", "0.25"], ["[350; 400)", "", "", "0.15"], ["[400; 450]", "", "100", "0.10"]]
ING = "La siguiente tabla refleja los ingresos semanales (en soles) de 100 trabajadores informales en un mercado regional: (Responder las preguntas del 12 al 16)\n[tabla]\n"

F(1, "est", 1, PESO + "¿Qué porcentaje de postulantes pesan menos de 66 kg?", ["50%", "55%", "60%", "65%"], ["50", "55", "60", "65"], "22/40*100", FREQ, "Los que pesan menos de 66 kg son los de los tres primeros intervalos.",
  ["Frecuencia acumulada hasta [60; 66): 22.", "22/40 = 0,55 = 55%."], "22 de 40 es 55%.", ok="b", **L4, table=TPESO, note="La tabla es compartida por las preguntas 1 a 5.", mistake="Contar también el intervalo [66; 72) y obtener 80%.")

F(2, "est", 1, PESO + "Calcule la suma de la marca de clase del segundo intervalo con la del quinto intervalo.", ["128", "130", "132", "135"], ["128", "130", "132", "135"], "(54+60)/2+(72+78)/2", FREQ, "La marca de clase es el punto medio del intervalo.",
  ["Segundo intervalo [54; 60): marca (54 + 60)/2 = 57.", "Quinto intervalo [72; 78): marca (72 + 78)/2 = 75.", "Suma: 57 + 75 = 132."], "Las marcas coinciden con la columna Xi de la tabla: 57 y 75.", ok="c", **L4, table=TPESO, mistake="Tomar el cuarto intervalo en lugar del quinto.")

F(3, "est", 1, PESO + "Determine la suma de las frecuencias absolutas de los últimos tres intervalos de la tabla.", ["16", "18", "20", "22"], ["16", "18", "20", "22"], "10+5+3", FREQ, "Las frecuencias absolutas son la columna fi.",
  ["Últimos tres intervalos: [66; 72), [72; 78) y [78; 84).", "fi: 10, 5 y 3.", "Suma: 10 + 5 + 3 = 18."], "40 − 22 = 18, porque las demás suman 22.", ok="b", **L4, table=TPESO, mistake="Sumar las frecuencias acumuladas, 32 + 37 + 40, en lugar de las absolutas.")

F(4, "est", 2, PESO + "Calcule la media aproximada.", ["64.8 kg", "66.3 kg", "67.2 kg", "68.0 kg"], ["648/10", "663/10", "672/10", "680/10"], "(51*2+57*6+63*14+69*10+75*5+81*3)/40", FREQ, "Se multiplica cada marca de clase por su frecuencia.",
  ["Productos: 51·2 = 102, 57·6 = 342, 63·14 = 882, 69·10 = 690, 75·5 = 375 y 81·3 = 243.", "Suma: 2634.", "Media: 2634/40 = 65,85 kg."], "65,85 está entre las marcas 63 y 69, más cerca de 63 por la frecuencia alta.",
  answer="65,85 kg, que es 1317/20. Ninguna alternativa lo recoge.", answerVal="1317/20", warn="Con la tabla tal cual, la media es 65,85 kg y ninguna alternativa lo recoge. La ficha tiene una errata en las alternativas.", review=True, **L4, table=TPESO,
  mistake="Marcar 66,3 kg por cercanía. El valor exacto es 65,85.")

F(5, "est", 1, PESO + "Reste la frecuencia absoluta del cuarto intervalo a la frecuencia acumulada del tercer intervalo", ["10", "12", "14", "16"], ["10", "12", "14", "16"], "22-10", FREQ, "F3 es la acumulada del tercer intervalo y f4 la absoluta del cuarto.",
  ["Frecuencia acumulada del tercer intervalo: F3 = 22.", "Frecuencia absoluta del cuarto intervalo: f4 = 10.", "22 − 10 = 12."], "F3 = 2 + 6 + 14 = 22.", ok="b", **L4, table=TPESO, mistake="Usar f3 = 14 en lugar de F3 = 22.")

F(6, "est", 3, "En una distribución de ancho de clase constante, se tiene:\n[tabla]\nDetermine la suma de m + n + k.", ["63", "65", "68", "72"], ["63", "65", "68", "72"], "7+25+34", FREQ + " Si el ancho es constante, cada intervalo empieza donde termina el anterior.",
  "Con el ancho w constante, el segundo intervalo es [16; 16 + w) y el cuarto empieza en 16 + 2w = 28.",
  ["Intervalos de ancho w: [16 − w; 16), [16; 16 + w), [16 + w; 16 + 2w) y [28; k]. Entonces 16 + 2w = 28, así que w = 6.", "Intervalos: [10; 16), [16; 22), [22; 28) y [28; 34]. Entonces k = 34.", "F1 = 5 y F2 = 12, así que m = 12 − 5 = 7.", "F4 = 30 y f4 = 10, así que F3 = 20 y f3 = 8. La marca del tercer intervalo es n = (22 + 28)/2 = 25.", "m + n + k = 7 + 25 + 34 = 66."],
  "Las frecuencias 5, 7, 8 y 10 suman 30, el total. Con esos datos la media es 23,6, como en la pregunta 7.", answer="66. Ninguna alternativa lo recoge.", answerVal="66",
  warn="Con la tabla tal cual, la suma es 66 y ninguna alternativa lo recoge. La ficha tiene una errata.",
  note="Si el último intervalo se tomara como [28; 33], con enteros, la suma sería 65, alternativa b. Pero entonces la media de la pregunta 7 no coincidiría con ninguna alternativa. Es solo una hipótesis y la academia debe confirmarlo.", review=True, **L4, table=T6,
  mistake="Olvidar que n es la marca de clase del tercer intervalo y usar su frecuencia, 8.")

F(7, "est", 2, "Del ejercicio anterior, calcule la media.", ["23.2", "22.6", "23.6", "708"], ["116/5", "113/5", "118/5", "708"], "(13*5+19*7+25*8+31*10)/30", FREQ, "Se usa la tabla de la pregunta 6 completa.",
  ["Marcas de clase: 13, 19, 25 y 31. Frecuencias: 5, 7, 8 y 10.", "Suma de marca por frecuencia: 65 + 133 + 200 + 310 = 708.", "Media: 708/30 = 23,6."], "23,6 está entre 19 y 25, cerca de 25 por las frecuencias altas.", ok="c", **L4, table=T6,
  note="Las alternativas se comprueban como fracciones: 23,2 es 116/5, 22,6 es 113/5 y 23,6 es 118/5.", mistake="Responder 708, la suma sin dividir entre 30, alternativa d.", trap="708", trap_opt="d")

F(8, "est", 3, "En un reporte censal de salud pública se agrupó la edad de 120 pobladores en el siguiente cuadro de frecuencias:\n[tabla]\n¿Cuántas personas censadas tienen 32 años a más?", ["66", "72", "84", "78"], ["66", "72", "84", "78"], "36*(36-32)/(36-28)+28+14+6", FREQ + " Dentro de un intervalo se supone que los datos se reparten por igual.",
  "El 32 cae a la mitad del intervalo [28; 36): solo se cuenta la parte desde 32 hasta 36.",
  ["Del intervalo [28; 36) hay 36 personas, repartidas por igual en 8 años. De 32 a 36 hay 4 años: la mitad, 18 personas.", "Los demás intervalos desde 36 suman 28 + 14 + 6 = 48.", "Total: 18 + 48 = 66."], "120 − (12 + 24 + 18) = 66, donde 18 son las del tramo 28 a 32.", ok="a", **L4,
  table=[["Edad (Años)", "fi"], ["[12; 20)", "12"], ["[20; 28)", "24"], ["[28; 36)", "36"], ["[36; 44)", "28"], ["[44; 52)", "14"], ["[52; 60]", "6"]],
  note="Se supone que las edades se reparten por igual dentro de cada intervalo.", mistake="Contar el intervalo [28; 36) completo y obtener 84, alternativa c.", trap="36+28+14+6", trap_opt="c")

F(9, "est", 1, "Un analista socioeconómico estudia el nivel de ventas diarias (en miles de soles) de 50 pequeños comercios (Responder preguntas 9 y 10):\n[tabla]\nHalle el porcentaje de comercios con ventas de 3000 a menos de 5000 soles.", ["52%", "60%", "64%", "67%"], ["52", "60", "64", "67"], "(18+(40-26))/50*100", FREQ,
  "La frecuencia del intervalo [4.0; 5.0) falta y se calcula con las acumuladas.", ["Frecuencia de [4.0; 5.0): 40 − 26 = 14.", "De 3000 a menos de 5000 son [3.0; 4.0) y [4.0; 5.0): 18 + 14 = 32.", "32/50 = 0,64 = 64%."], "8 + 18 + 14 + 6 + 4 = 50.", ok="c", **L4, table=T9, note="La tabla es compartida por las preguntas 9 y 10.", mistake="Usar solo el intervalo [3.0; 4.0) y obtener 36%.")

F(10, "est", 2, "Un analista socioeconómico estudia el nivel de ventas diarias (en miles de soles) de 50 pequeños comercios (Responder preguntas 9 y 10):\n[tabla]\nCalcule la venta diaria promedio estimada (en miles de soles) del total de comercios.", ["s/ 4.08", "s/ 4.12", "s/ 4.24", "s/ 4.35"], ["408/100", "412/100", "424/100", "435/100"],
  "(5/2*8+7/2*18+9/2*14+11/2*6+13/2*4)/50", FREQ, "Se usan las marcas de clase 2,5; 3,5; 4,5; 5,5 y 6,5, con las frecuencias 8, 18, 14, 6 y 4.",
  ["2,5·8 = 20, 3,5·18 = 63, 4,5·14 = 63, 5,5·6 = 33 y 6,5·4 = 26.", "Suma: 205.", "Media: 205/50 = 4,10."], "4,10 está entre 3,5 y 4,5, donde están la mayoría de comercios.",
  answer="4,10 miles de soles. Ninguna alternativa lo recoge.", answerVal="41/10", warn="Con la tabla tal cual, la media es 4,10 y ninguna alternativa lo recoge. La ficha tiene una errata en las alternativas.", review=True, **L4, table=T9,
  mistake="Marcar 4,12 por cercanía. El valor exacto es 4,10.")

F(11, "est", 1, "Un comerciante de zapatos ha identificado los ingresos de sus ventas durante los 30 días del mes de abril. La información está presentada en la siguiente tabla.\n[tabla]\n¿Qué porcentajes de los días totales del mes tuvo ingresos inferiores a s/ 2200?", ["10%", "30%", "40%", "60%"], ["10", "30", "40", "60"], "(6+1/10*30+9)/30*100", FREQ,
  "Hay que hallar la frecuencia del segundo intervalo con hi = 0,1.", ["Segundo intervalo: f2 = 0,1 · 30 = 3.", "Inferiores a 2200: los tres primeros intervalos, 6 + 3 + 9 = 18 días.", "18/30 = 0,6 = 60%."], "H3 = 18/30 = 0,6, y H4 = 0,90 implica f4 = 9, f5 = 3, total 30.", ok="d", **L4,
  table=[["Ingresos por día (S/)", "fi", "hi", "Hi"], ["[400 – 1 000[", "6", "", ""], ["[1 000 – 1 600[", "", "0,1", ""], ["[1 600 – 2 200[", "9", "", ""], ["[2 200 – 2 800[", "", "", "0,90"], ["[2 800 – 3 400[", "3", "", "1"]],
  mistake="Olvidar la frecuencia de 3 días del segundo intervalo y obtener 50%.")

F(12, "est", 1, ING + "¿Cuántos trabajadores perciben un ingreso menor a 350 soles semanales?", ["65", "70", "75", "80"], ["65", "70", "75", "80"], "15+35+25", FREQ, "Se completa la tabla con hi: f = hi · 100.",
  ["f3 = 0,25 · 100 = 25.", "Menores de 350: los tres primeros intervalos, 15 + 35 + 25 = 75."], "F3 = 75.", ok="c", **L4, table=T12, note="La tabla es compartida por las preguntas 12 a 16.", mistake="Sumar solo los dos primeros intervalos y obtener 50.")

F(13, "est", 1, ING + "¿Cuántos trabajadores obtienen un ingreso de al menos 300 pero menor de 400 soles?", ["35", "40", "45", "50"], ["35", "40", "45", "50"], "25+15", FREQ, "Son los intervalos [300; 350) y [350; 400).",
  ["f3 = 25 y f4 = 15.", "Suma: 25 + 15 = 40."], "0,25 + 0,15 = 0,40 de 100 trabajadores.", ok="b", **L4, table=T12, mistake="Incluir el intervalo [400; 450] y obtener 50.")

F(14, "est", 1, ING + "Calcule la suma de todas las frecuencias relativas acumulativas.", ["0.9", "1", "3.3", "2.3"], ["9/10", "1", "33/10", "23/10"], "(15+50+75+90+100)/100", FREQ, "Las relativas acumuladas son H = F/100.",
  ["Fi: 15, 50, 75, 90 y 100.", "Hi: 0,15; 0,50; 0,75; 0,90 y 1,00.", "Suma: 0,15 + 0,50 + 0,75 + 0,90 + 1,00 = 3,30."], "330/100 = 3,3.", ok="c", **L4, table=T12, note="Las alternativas se comprueban como fracciones.", mistake="Sumar las relativas simples, que suman 1, alternativa b.", trap="1", trap_opt="b")

F(15, "est", 2, ING + "¿Cuál sería el ingreso semanal promedio por trabajador?", ["300", "310", "280", "350"], ["300", "310", "280", "350"], "(225*15+275*35+325*25+375*15+425*10)/100", FREQ, "Se usan las marcas de clase 225, 275, 325, 375 y 425.",
  ["Productos: 225·15 = 3375, 275·35 = 9625, 325·25 = 8125, 375·15 = 5625 y 425·10 = 4250.", "Suma: 31 000.", "Promedio: 31 000/100 = 310."], "310 está dentro del intervalo [300; 350).", ok="b", **L4, table=T12, mistake="Promediar las cinco marcas sin ponderar: 325.")

F(16, "est", 2, ING + "¿Qué cantidad de personas ganan menos que el promedio estimado?", ["50", "55", "35", "75"], ["50", "55", "35", "75"], "50+25*(310-300)/50", FREQ + " Dentro de un intervalo se supone que los datos se reparten por igual.",
  "El promedio, 310, cae dentro del intervalo [300; 350).", ["Menos de 300: 15 + 35 = 50 personas.", "Del intervalo [300; 350), con 25 personas en 50 soles, hasta 310 hay 10 soles: 25·10/50 = 5 personas.", "Total: 50 + 5 = 55."], "55 de 100 ganan menos de 310.", ok="b", **L4, table=T12,
  note="Se usa el promedio 310 de la pregunta 15 y se supone una distribución uniforme dentro del intervalo.", mistake="Responder 50 y dejar fuera a las personas entre 300 y 310.")

F(17, "prob", 1, "Un consultor en imagen personal dispone de 4 camisas, 3 sacos, 3 pantalones y 2 pares de zapatos. ¿De cuántas maneras distintas se puede vestir usando una prenda de cada tipo?", ["144", "64", "12", "72"], ["144", "64", "12", "72"], "4*3*3*2", COMB, "Se multiplican las opciones de cada prenda.",
  ["4 camisas · 3 sacos · 3 pantalones · 2 zapatos.", "4·3·3·2 = 72."], "4·3 = 12, 12·3 = 36 y 36·2 = 72.", ok="d", **L4, mistake="Sumar las prendas: 4 + 3 + 3 + 2 = 12, alternativa c.", trap="4+3+3+2", trap_opt="c")

F(18, "prob", 1, "¿Cuántos códigos de 3 cifras significativas se pueden formar empleando los dígitos {1, 3, 5, 7, 9}?", ["30", "60", "120", "125"], ["30", "60", "120", "125"], "5^3", COMB + " Cuando se pueden repetir cifras, cada lugar tiene todas las opciones.", "No se dice que las cifras sean distintas, así que se pueden repetir.",
  ["Cada una de las 3 posiciones tiene 5 opciones.", "5·5·5 = 125."], "5³ = 125.", ok="d", **L4, mistake="Suponer que no se repiten y calcular 5·4·3 = 60, alternativa b.", trap="5*4*3", trap_opt="b")

F(19, "prob", 1, "¿Cuántos códigos de 3 cifras significativas distintas se pueden formar empleando los dígitos {2, 4, 6, 8}?", ["12", "24", "64", "125"], ["12", "24", "64", "125"], "4*3*2", COMB, "Si las cifras deben ser distintas, cada posición tiene una opción menos.",
  ["Primera posición: 4 opciones. Segunda: 3. Tercera: 2.", "4·3·2 = 24."], "Es el número de ordenaciones de 3 de 4 objetos.", ok="b", **L4, mistake="Calcular 4³ = 64, que permite repetir, alternativa c.", trap="4^3", trap_opt="c")

F(20, "prob", 1, "¿Cuántas palabras (con o sin sentido) se pueden formar con las letras de la palabra \"LUZ\"?", ["3", "6", "9", "27"], ["3", "6", "9", "27"], "3!", COMB, "Las tres letras son distintas y se usan todas.",
  ["Primera letra: 3 opciones. Segunda: 2. Tercera: 1.", "3·2·1 = 6."], "LUZ, LZU, ULZ, UZL, ZLU y ZUL.", ok="b", **L4, mistake="Calcular 3³ = 27, que permite repetir letras, alternativa d.", trap="3^3", trap_opt="d")

F(21, "prob", 1, "Al lanzar dos dados, ¿De cuántas maneras me pueden salir dos números pares?", ["3", "6", "9", "27"], ["3", "6", "9", "27"], "3*3", COMB, "En un dado hay 3 números pares: 2, 4 y 6.",
  ["Primer dado: 3 pares. Segundo dado: 3 pares.", "3·3 = 9."], "(2,2), (2,4), (2,6), (4,2), …, (6,6): 9 pares.", ok="c", **L4, mistake="Sumar 3 + 3 = 6, alternativa b.", trap="3+3", trap_opt="b")

F(22, "prob", 2, "Al lanzar dos dados, ¿De cuántas maneras no me puedan salir exactamente dos números pares?", ["30", "6", "9", "27"], ["30", "6", "9", "27"], "36-3*3", COMB, "No salir dos pares es todo lo que no sea que los dos sean pares.",
  ["Total de resultados: 6·6 = 36.", "Con dos pares: 3·3 = 9.", "Los demás: 36 − 9 = 27."], "27 + 9 = 36.", ok="d", **L4, mistake="Restar 36 − 6 = 30, alternativa a.", trap="36-6", trap_opt="a")

F(23, "prob", 3, "¿De cuántas maneras al lanzar dos dados y una moneda, me puedan salir al menos un número par y sello?", ["30", "6", "9", "27"], ["30", "6", "9", "27"], "(6*6-3*3)*1", COMB, "Sello en la moneda y al menos un par en los dos dados.",
  ["Dados con al menos un par: a los 36 resultados se les quitan los de dos impares, 3·3 = 9, y quedan 27.", "La moneda debe salir sello: 1 forma.", "27·1 = 27."], "Hay 36 resultados de dados con 9 de ellos sin pares.", ok="d", **L4, mistake="Multiplicar por 2 por la moneda y obtener 54, o no restar los impares.")

F(24, "prob", 3, "En una caja hay 4 bolas rojas y 6 azules. ¿De cuántas formas se pueden sacar 3 bolas si al menos 2 deben ser rojas?", ["20", "40", "36", "60"], ["20", "40", "36", "60"], "binomial(4,2)*binomial(6,1)+binomial(4,3)", COMB, "Al menos 2 rojas es exactamente 2 rojas o 3 rojas.",
  ["Con 2 rojas y 1 azul: C(4, 2)·C(6, 1) = 6·6 = 36.", "Con 3 rojas: C(4, 3) = 4.", "Total: 36 + 4 = 40."], "C(10, 3) = 120, y con a lo más 1 roja hay 80. 120 − 80 = 40.", ok="b", **L4, mistake="Contar solo el caso con 2 rojas, 36, alternativa c.", trap="36", trap_opt="c")

F(25, "prob", 3, "De 5 números positivos y 7 negativos se eligen sin sustitución 4 números al azar y se multiplican. ¿De cuántas formas el producto será un número positivo?", ["260", "270", "240", "250"], ["260", "270", "240", "250"], "binomial(5,4)+binomial(5,2)*binomial(7,2)+binomial(7,4)", COMB + " Un producto es positivo si tiene una cantidad par de factores negativos.",
  "Se separan los casos por cuántos negativos se eligen: 0, 2 o 4.", ["0 negativos: C(5, 4) = 5.", "2 negativos: C(5, 2)·C(7, 2) = 10·21 = 210.", "4 negativos: C(7, 4) = 35.", "Total: 5 + 210 + 35 = 250."], "C(12, 4) = 495 formas en total y 495 − 250 = 245 dan producto negativo: 7·C(5, 3)·... = 245.", ok="d", **L4,
  mistake="Olvidar el caso de 0 negativos o el de 4 negativos.")

F(26, "prob", 2, "¿Cuántos números enteros de 4 cifras impares distintas se pueden estructurar utilizando únicamente los dígitos del conjunto {1, 2, 3, 5, 7, 8, 9}?", ["120", "210", "360", "840"], ["120", "210", "360", "840"], "5*4*3*2", COMB, "Las cifras impares disponibles son 1, 3, 5, 7 y 9.",
  ["Los 4 dígitos deben ser impares y distintos: se eligen de 5.", "5·4·3·2 = 120."], "Es el número de ordenaciones de 4 de 5 objetos.", ok="a", **L4, note="Es el problema 1 de Las fijas del gran Sergio.", mistake="Incluir los dígitos pares y calcular 7·6·5·4 = 840, alternativa d.", trap="7*6*5*4", trap_opt="d")

F(27, "prob", 3, "De un grupo constituido por 6 números positivos y 5 negativos, se extraen 3 números y se multiplican. ¿De cuántas formas el producto resultante será estrictamente negativo?", ["70", "75", "80", "85"], ["70", "75", "80", "85"], "5*binomial(6,2)+binomial(5,3)", COMB + " Un producto es negativo si tiene una cantidad impar de factores negativos.",
  "Se separan los casos por cuántos negativos se eligen: 1 o 3.", ["1 negativo y 2 positivos: C(5, 1)·C(6, 2) = 5·15 = 75.", "3 negativos: C(5, 3) = 10.", "Total: 75 + 10 = 85."], "C(11, 3) = 165: los productos positivos son 165 − 85 = 80.", ok="d", **L4, note="Es el problema 2 de Las fijas del gran Sergio.", mistake="Olvidar el caso de 3 negativos y obtener 75, alternativa b.", trap="75", trap_opt="b")

F(28, "prob", 2, "Un sistema de seguridad requiere la creación de una contraseña de 5 caracteres. Los primeros 2 deben ser letras vocales mayúsculas distintas y los últimos 3 dígitos numéricos del 1 al 9 sin repetir. ¿Cuántas contraseñas seguras se pueden generar?", ["10080", "12600", "14400", "18000"], ["10080", "12600", "14400", "18000"], "5*4*9*8*7", COMB,
  "Se multiplican las opciones de cada posición.", ["Vocales distintas: 5·4 = 20.", "Dígitos distintos del 1 al 9: 9·8·7 = 504.", "Total: 20·504 = 10 080."], "20 · 504 = 10 080.", ok="a", **L4, note="Es el problema 3 de Las fijas del gran Sergio.", mistake="Permitir que se repitan los dígitos: 20·729 = 14 580, que no está entre las alternativas.")

F(29, "est", 3, "Sea la siguiente tabla de frecuencias:\n[tabla]\nCalcule la suma de cifras de a+b+c", ["44", "10", "8", "12"], ["44", "10", "8", "12"], "iquo(18+2+24,10)+irem(18+2+24,10)", FREQ + " Si el ancho de clase es constante, cada intervalo empieza donde termina el anterior.",
  "Con el ancho w, el cuarto intervalo empieza en 12 + 2w = 20.",
  ["Intervalos de ancho w: [12 − w; 12), [12; 12 + w), [12 + w; 12 + 2w) y [20; c]. Entonces 12 + 2w = 20 y w = 4.", "Intervalos: [8; 12), [12; 16), [16; 20) y [20; 24]. Entonces c = 24.", "F2 = 6 y f1 = 4, así que b = 6 − 4 = 2.", "a es la marca del tercer intervalo: (16 + 20)/2 = 18.", "a + b + c = 18 + 2 + 24 = 44. La suma de cifras de 44 es 4 + 4 = 8."], "Las frecuencias son 4, 2, 6 y 8, que suman 20, la última acumulada.", ok="c", **L4,
  table=[["Ii", "Xi", "fi", "Fi"], ["[ ; )", "", "4", ""], ["[12; )", "", "b", "6"], ["[ ; )", "a", "", ""], ["[20; c ]", "", "8", "20"]],
  note="Es el problema 4 de Las fijas del gran Sergio.", mistake="Responder 44, alternativa a, que es a + b + c y no la suma de sus cifras.", trap="44", trap_opt="a")

F(30, "prob", 2, "¿De cuántas maneras al lanzar 3 dados, me de al menos un número par?", ["27", "207", "64", "189"], ["27", "207", "64", "189"], "6^3-3^3", COMB, "Es más fácil contar los casos sin ningún par y restarlos del total.",
  ["Total de resultados: 6³ = 216.", "Sin ningún par, es decir los tres impares: 3³ = 27.", "Con al menos un par: 216 − 27 = 189."], "27 + 189 = 216.", ok="d", **L4, note="Es el problema 5 de Las fijas del gran Sergio.", mistake="Contar 216 − 9 = 207, alternativa b.", trap="216-9", trap_opt="b")

F(31, "est", 2, "Un servidor de almacenamiento de datos en la nube dispone de 64 GB de espacio libre (1 GB = 1024 MB). Si se desean subir paquetes de respaldos comprimidos cuyos tamaños oscilan entre 80 MB y 120 MB cada uno, ¿cuál es el número máximo teórico de paquetes completos de respaldo que caben en el servidor?",
  ["546", "682", "819", "910"], ["546", "682", "819", "910"], "floor(64*1024/80)", "Para tener la mayor cantidad de paquetes, cada uno debe ser lo más pequeño posible. Solo cuentan los paquetes completos.", "Con el tamaño mínimo, 80 MB, caben más paquetes.",
  ["64 GB = 64·1024 = 65 536 MB.", "65 536/80 = 819,2.", "Solo cuentan paquetes completos: 819."], "819 · 80 = 65 520 MB.", ok="c", **L4, note="Es el problema 6 de Las fijas del gran Sergio.", mistake="Dividir entre 120 MB y obtener 546, alternativa a.", trap="floor(64*1024/120)", trap_opt="a")

F(32, "est", 3, "La edad promedio de diez docentes es 55 años y ninguno de ellos tiene más de 65. ¿Cuál es la edad mínima que alguno de los diez podría tener si se sabe que las edades de tres de ellos son 37, 40 y 53 años?",
  ["40", "28", "30", "32"], ["40", "28", "30", "32"], "10*55-(37+40+53)-6*65", "Para que una edad sea la mínima posible, las demás deben ser lo más grandes posible.", "La suma de las diez edades es fija. El mínimo se logra con los otros seis docentes en 65.",
  ["Suma de las 10 edades: 10 · 55 = 550.", "Las tres conocidas suman 37 + 40 + 53 = 130. Las otras siete suman 420.", "Para que uno sea mínimo, los otros seis valen 65 cada uno: 6 · 65 = 390.", "Edad mínima: 420 − 390 = 30."], "Con 6 docentes de 65 y uno de 30: 390 + 30 = 420.", ok="c", **L4, note="Es el problema 7 de Las fijas del gran Sergio.", mistake="Olvidar las tres edades conocidas y calcular 550 − 10·65 = −100.")
