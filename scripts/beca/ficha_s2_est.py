"""Ficha de Estadística, semana 2, de Sergio Huanca: medidas de tendencia central. 26 preguntas, 4 alternativas."""
from model import ficha
F = ficha(2, "est")
L4 = dict(letters="abcd")
TF = "true"
MED = ("Media: la suma de los datos entre la cantidad de datos. Mediana: el dato central cuando se ordenan; si son una cantidad par, el promedio de los dos centrales. "
       "Moda: el dato que más se repite. Media ponderada: cada dato se multiplica por su frecuencia y se divide entre el total de datos.")
ROB = "En un laboratorio de robótica, los tiempos de ensamblaje (en minutos) de 5 piezas por un brazo mecánico son: 10, 12, 16, 12, 15.\n"
TEX = "Los costos diarios de producción (en soles) de 7 microempresas textileras son: 400, 450, 550, 500, 600, 3200, 700.\n"
VEN = "El gráfico muestra las ventas realizadas en miles de soles por varones y mujeres en una tienda comercial:\n"
EQ = "En la siguiente tabla se muestra los equipos de fútbol de la I.E Fe y Alegría, conformados por integrantes del equipo A y B, con un total de 15 integrantes por equipo.\n[tabla]\n"
TEQ = [["Edad (años)", "Equipo A", "Equipo B"], ["11", "3", "2"], ["12", "7", "2"], ["13", "4", "5"], ["14", "1", "6"], ["", "n = 15", "n = 15"]]

F(1, "est", 1, "Las calificaciones de 5 becarios en un taller de redacción son: 11, 13, 15, 17, 19. Calcule la media.", ["14", "15", "17", "16"], ["14", "15", "17", "16"], "(11+13+15+17+19)/5",
  MED, "Se suman las cinco notas y se divide entre 5.", ["Suma: 11 + 13 + 15 + 17 + 19 = 75.", "Media: 75/5 = 15."], "Las notas son simétricas alrededor de 15.", ok="b", **L4, mistake="Dividir entre 4 en vez de 5 y obtener 18,75.")

F(2, "est", 1, "En un control de calidad de baterías, las duraciones en horas de 6 muestras fueron: 12, 10, 12, 14, 12, 18. Calcule la suma de la media y la moda.", ["23", "25", "27", "29"], ["23", "25", "27", "29"], "(12+10+12+14+12+18)/6+12",
  MED, "Se calcula la media y se busca el dato que más se repite.", ["Media: 78/6 = 13.", "Moda: 12, que aparece 3 veces.", "Suma: 13 + 12 = 25."], "10 + 12 + 12 + 12 + 14 + 18 = 78.", ok="b", **L4, mistake="Tomar la mediana, 12, en vez de la media, y obtener 24.")

F(3, "est", 1, ROB + "Calcule el promedio de estos tiempos de ensamblaje.", ["13", "12", "13.2", "13.8"], ["13", "12", "66/5", "69/5"], "(10+12+16+12+15)/5",
  MED, "El promedio es la media.", ["Suma: 10 + 12 + 16 + 12 + 15 = 65.", "Media: 65/5 = 13."], "13 · 5 = 65.", ok="a", **L4, note="Las alternativas se comprueban como fracciones: 13,2 es 66/5 y 13,8 es 69/5.", mistake="Dividir entre 4 y obtener 16,25.")

F(4, "est", 1, ROB + "Calcule la suma de la mediana y la moda de dichos tiempos.", ["24", "27", "28", "25.2"], ["24", "27", "28", "126/5"], "sort([10,12,16,12,15])[2]+12",
  MED, "Se ordenan los tiempos para hallar la mediana.", ["Ordenados: 10, 12, 12, 15, 16.", "Mediana: el dato del medio, 12. Moda: 12, que aparece dos veces.", "Suma: 12 + 12 = 24."], "El dato 12 aparece 2 veces y los demás una.", ok="a", **L4,
  note="Las alternativas se comprueban como fracciones: 25,2 es 126/5.", mistake="Usar la media, 13, en vez de la mediana, y obtener 25.")

F(5, "est", 2, ROB + "Si se registra una sexta pieza que demoró 24 minutos, ¿en cuánto varía la mediana de los tiempos de prueba?", ["0.5", "1", "1.5", "2"], ["1/2", "1", "3/2", "2"], "(12+15)/2-12",
  MED, "Con 6 datos la mediana es el promedio de los dos centrales.", ["Antes: ordenados 10, 12, 12, 15, 16 con mediana 12.", "Ahora: 10, 12, 12, 15, 16, 24. Los centrales son 12 y 15: mediana 13,5.", "La mediana varía 13,5 − 12 = 1,5."], "13,5 es el punto medio entre 12 y 15.", ok="c", **L4,
  mistake="Tomar el nuevo dato 24 como si fuera la mediana.")

F(6, "est", 1, TEX + "Calcule la mediana de los costos de producción.", ["500", "550", "600", "700"], ["500", "550", "600", "700"], "sort([400,450,550,500,600,3200,700])[3]",
  MED, "Se ordenan los 7 costos y se toma el del medio.", ["Ordenados: 400, 450, 500, 550, 600, 700, 3200.", "El cuarto dato es el del medio: 550."], "Hay 3 datos a cada lado de 550.", ok="b", **L4, mistake="Tomar el tercer dato, 500, sin ordenar bien.")

F(7, "est", 1, TEX + "Calcule la media aritmética de los costos de producción.", ["850", "900", "914.3", "1020"], ["abs(850-6400/7)<1/20", "abs(900-6400/7)<1/20", "abs(9143/10-6400/7)<1/20", "abs(1020-6400/7)<1/20"], TF,
  MED, "El dato 3200 es muy grande y sube la media.", ["Suma: 400 + 450 + 550 + 500 + 600 + 3200 + 700 = 6400.", "Media: 6400/7 ≈ 914,29, es decir 914,3."], "914,29 · 7 = 6400.", ok="c", **L4,
  note="Las alternativas se comprueban por cercanía al valor 6400/7.", mistake="Calcular la media sin el dato 3200: 3200/6 = 533,3.")

F(8, "est", 2, TEX + "Si la empresa textilera que produce 3200 soles decide retirarse, cuál sería la nueva mediana", ["575", "500", "525", "No cambia"], ["575", "500", "525", "550"],
  "(sort([400,450,550,500,600,700])[2]+sort([400,450,550,500,600,700])[3])/2", MED, "Quedan 6 costos: la mediana es el promedio de los dos centrales.",
  ["Quedan 400, 450, 500, 550, 600, 700.", "Los dos centrales son 500 y 550: mediana (500 + 550)/2 = 525."], "525 está entre 500 y 550.", ok="c", **L4,
  note="La alternativa d, No cambia, equivale a una mediana de 550, que era la anterior.", mistake="Pensar que la mediana no cambia porque se retiró el dato más grande, alternativa d.")

F(9, "est", 3, "Jun revisaba sus rachas de TikTok y notó que, entre sus 5 contactos principales (con rachas permitidas de entre 3 a 30 días), el promedio de días era 16, la mediana 15 y la moda 14. Si se sabe con su mejor amigo tiene la mayor racha posible. ¿Cuántos días de racha tiene Juan con su mejor amigo?",
  ["19", "20", "21", "22"], ["19", "20", "21", "22"], "max(select(e->size(select(d->d>=16 and d<=e and 14+14+15+d+e==80,seq(d,d,15,30)))>0,seq(e,e,15,30)))",
  MED, "Con 5 datos ordenados, la mediana es el tercero. Para tener moda 14, el 14 debe repetirse más que cualquier otro.",
  ["Suma: 5 · 16 = 80. El tercer dato es 15.", "Para que 14 sea la moda y la mediana sea 15, los dos primeros datos son 14 y 14.", "Quedan 14 + 14 + 15 + d + e = 80, es decir d + e = 37, con 15 < d ≤ e para que 15 no empate a 14 como moda.", "La racha máxima se logra con d = 16 y e = 21. Datos: 14, 14, 15, 16, 21."],
  "Media: 80/5 = 16. Mediana: 15. Moda: 14, que aparece dos veces y los demás una.", ok="c", **L4,
  note="En el enunciado el amigo se llama Jun y luego Juan, tal como está en la ficha. Con d = 15 y e = 22 la moda sería doble, 14 y 15, y no valdría.", mistake="Elegir 22, alternativa d, que obliga a que 14 y 15 sean ambos moda.")

F(10, "est", 2, "Una estación agroclimática que opera en una ciudad conocida como el ombligo del mundo, registró la temperatura diaria en 4 mediciones consecutivas obteniendo una media de 16°C y una mediana de 17°C. Si la temperatura mínima registrada fue 10°C, determine la máxima temperatura posible en una de las lecturas.",
  ["20°C", "21°C", "22°C", "24°C"], ["20", "21", "22", "24"], "4*16-2*17-10", MED, "Con 4 datos la mediana es el promedio de los dos centrales.",
  ["Suma total: 4 · 16 = 64. Los dos centrales suman 2 · 17 = 34.", "El mínimo es 10 y el máximo es M: 10 + 34 + M = 64.", "M = 20."], "10, 14, 20, 20: media 16, mediana 17 y máximo 20.", ok="a", **L4,
  mistake="Tomar solo la media y suponer que el máximo es 22, para tener datos simétricos.")

F(11, "est", 1, "El tiempo de espera (en minutos) de 10 usuarios en una ventanilla de trámite de un hospital fue: 18, 25, 12, 15, 20, 22, 15, 30, 18, 15. Determine la mediana de los tiempos de espera.",
  ["16.5", "15", "18", "19"], ["33/2", "15", "18", "19"], "(sort([18,25,12,15,20,22,15,30,18,15])[4]+sort([18,25,12,15,20,22,15,30,18,15])[5])/2", MED, "Con 10 datos ordenados, la mediana es el promedio del quinto y el sexto.",
  ["Ordenados: 12, 15, 15, 15, 18, 18, 20, 22, 25, 30.", "Quinto y sexto: 18 y 18.", "Mediana: (18 + 18)/2 = 18."], "Hay 5 datos a cada lado de los dos centrales.", ok="c", **L4,
  note="Las alternativas se comprueban como fracciones: 16,5 es 33/2.", mistake="Responder la moda, 15, alternativa b.", trap="15", trap_opt="b")

F(12, "est", 3, "Un comité evalúa a 5 proyectos de innovación socioambiental. La mediana de los puntajes otorgados es 14, la media es 13.6 y la moda es 10. Si no hay proyectos con igual puntaje salvo los correspondientes a la moda, calcule la suma del puntaje más alto y el más bajo.",
  ["24", "26", "28", "30"], ["24", "26", "28", "30"], "10+min(select(e->size(select(d->d>14 and d<e and 10+10+14+d+e==68,seq(d,d,15,40)))>0,seq(e,e,15,40)))",
  MED, "La moda 10 se repite dos veces y es el mínimo. Los demás puntajes son distintos.",
  ["Suma de puntajes: 5 · 13,6 = 68.", "Ordenados: 10, 10, 14, d, e, con 14 < d < e.", "10 + 10 + 14 + d + e = 68, así que d + e = 34.", "Con enteros: d = 15 y e = 19, o d = 16 y e = 18. La suma del más alto y el más bajo es 29 o 28.", "La alternativa que aparece es 28."],
  "Con 10, 10, 14, 16, 18: suma 68, mediana 14 y moda 10.", ok="c", **L4, review=True,
  note="Con puntajes enteros también sirve 10, 10, 14, 15, 19, que daría 29, pero 29 no está entre las alternativas. La clave 28 corresponde a la otra solución.",
  mistake="No notar que hay dos soluciones y elegir una alternativa sin comprobar cuál aparece.")

F(13, "est", 1, "El promedio de producción de 8 parcelas agrícolas de quinua es de 15 toneladas y el de otras 12 parcelas es de 20 toneladas. ¿Cuál es el rendimiento promedio global de las 20 parcelas?",
  ["18", "18.5", "17", "19"], ["18", "37/2", "17", "19"], "(8*15+12*20)/20", MED, "El promedio global se calcula con las sumas totales, no promediando los dos promedios.",
  ["Toneladas del primer grupo: 8 · 15 = 120. Del segundo: 12 · 20 = 240.", "Promedio global: (120 + 240)/20 = 18."], "El promedio queda más cerca de 20 porque hay más parcelas de ese grupo.", ok="a", **L4,
  note="Las alternativas se comprueban como fracciones: 18,5 es 37/2.", mistake="Promediar 15 y 20 y obtener 17,5.")

F(14, "est", 2, "El promedio de edad de 6 asistentes ejecutivas es de 35 años. Si se integra una séptima asistente, la media de edad de todo el grupo sube a 38 años. ¿Qué edad tiene la nueva asistente?",
  ["48", "52", "56", "60"], ["48", "52", "56", "60"], "7*38-6*35", MED, "La suma de edades antes y después permite hallar la edad nueva.",
  ["Suma de las 6 edades: 6 · 35 = 210.", "Suma de las 7 edades: 7 · 38 = 266.", "Edad de la nueva asistente: 266 − 210 = 56."], "266/7 = 38.", ok="c", **L4, mistake="Sumar 38 + 3 · 7 = 59 sin usar las sumas.")

F(15, "est", 1, "En un centro médico se evaluó el tiempo de atención (en minutos) de 50 pacientes en consulta externa:\n[tabla]\nA partir de los datos registrados, halle la media ponderada del tiempo de consulta por paciente.",
  ["19.5", "20", "20.5", "21"], ["39/2", "20", "41/2", "21"], "(15*15+20*20+25*10+30*5)/50", MED, "Cada tiempo se multiplica por su cantidad de pacientes.",
  ["Productos: 15 · 15 = 225, 20 · 20 = 400, 25 · 10 = 250 y 30 · 5 = 150.", "Suma: 1025.", "Media: 1025/50 = 20,5."], "20,5 · 50 = 1025.", ok="c", **L4,
  table=[["Tiempo (min)", "Pacientes (fi)"], ["15", "15"], ["20", "20"], ["25", "10"], ["30", "5"]],
  note="Las alternativas se comprueban como fracciones: 19,5 es 39/2 y 20,5 es 41/2.", mistake="Promediar solo los cuatro tiempos, 22,5, sin ponderar.")

F(16, "est", 2, "Si los pacientes que requirieron 30 minutos de atención son derivados a otra área especializada, calcule el nuevo promedio de tiempo de consulta del grupo restante.",
  ["18.88", "19.44", "19.75", "20.10"], ["abs(1888/100-175/9)<5/1000", "abs(1944/100-175/9)<5/1000", "abs(1975/100-175/9)<5/1000", "abs(2010/100-175/9)<5/1000"], TF,
  MED, "Se usa la misma tabla del problema 15 sin el grupo de 30 minutos.",
  ["Quedan 15 + 20 + 10 = 45 pacientes.", "Suma de tiempos: 225 + 400 + 250 = 875.", "Nueva media: 875/45 = 19,44."], "19,44 · 45 = 875.", ok="b", **L4,
  table=[["Tiempo (min)", "Pacientes (fi)"], ["15", "15"], ["20", "20"], ["25", "10"], ["30", "5"]],
  note="Las alternativas se comprueban por cercanía al valor 175/9 ≈ 19,444.", mistake="Restar 30 de la suma y dividir entre 49.")

F(17, "est", 1, "Un jugador titular de un equipo de básquet se ha lesionado. Dilan y Jacob son dos jugadores del equipo, ellos esperan el llamado del entrenador para ser titulares. La decisión de la elección por parte del entrenador se basa en el promedio de los puntos obtenidos en los 5 últimos encuentros del entrenamiento de sus jugadores.\n[tabla]\n¿Qué jugador será seleccionado para reemplazar al titular lesionado? ¿Por qué?",
  ["Dilan, porque obtuvo 20 puntos en el segundo partido.", "Jacob, porque obtuvo el mismo puntaje en dos partidos.", "Dilan, porque obtuvo un promedio superior al de Jacob.", "Jacob, porque obtuvo el mayor promedio en los dos últimos partidos."],
  ["false", "false", "((12+20+10+10+12)/5>(14+16+8+12+12)/5)", "false"], TF, MED, "El criterio del entrenador es el promedio de los 5 partidos.",
  ["Dilan: 12 + 20 + 10 + 10 + 12 = 64, promedio 12,8.", "Jacob: 14 + 16 + 8 + 12 + 12 = 62, promedio 12,4.", "Dilan tiene mayor promedio y por eso es el elegido."], "12,8 > 12,4.", ok="c", **L4,
  table=[["", "Primer partido", "Segundo partido", "Tercer partido", "Cuarto partido", "Quinto partido"], ["Dilan", "12", "20", "10", "10", "12"], ["Jacob", "14", "16", "8", "12", "12"]],
  note="Las alternativas se comprueban como afirmaciones: solo la c cumple el criterio del promedio. Es el mismo problema que el 29 del examen 2025.", mistake="Elegir la a porque 20 es el puntaje más alto, aunque no es el criterio.")

F(18, "est", 2, "El ingreso promedio semanal de 80 trabajadores es de S/ 600. Se retira un subgrupo de 30 trabajadores cuyo ingreso promedio era de S/ 400. ¿Cuál es el promedio de ingreso de los trabajadores que permanecen?",
  ["s/ 680", "s/ 720", "s/ 750", "s/ 780"], ["680", "720", "750", "780"], "(80*600-30*400)/50", MED, "Se restan las sumas totales de ingresos.",
  ["Total de ingresos: 80 · 600 = 48 000.", "Ingresos del subgrupo: 30 · 400 = 12 000.", "Quedan 50 trabajadores con 36 000. Promedio: 36 000/50 = 720."], "720 · 50 + 400 · 30 = 48 000.", ok="b", **L4, mistake="Hacer 600 + 120 = 720 por casualidad en otros datos; hay que usar las sumas.")

F(19, "est", 2, "El peso promedio de \"m\" maletas es \"n\" kilos. Si se agregan \"m\" kilos a cada una de las \"m\" maletas. ¿Cuál es el nuevo peso promedio?", ["n", "m+n", "2n", "2m"], ["10", "3+10", "20", "6"], "(3*10+3*3)/3", MED,
  "Si a cada dato se le suma una constante, la media también aumenta en esa constante.", ["Cada maleta pesa m kilos más.", "El promedio también sube m kilos: n + m."], "Con m = 3 y n = 10: suma 30, se agregan 9 y el promedio es 39/3 = 13 = 3 + 10.", ok="b", **L4,
  note="Las alternativas se comprueban con m = 3 y n = 10.", mistake="Pensar que el promedio no cambia, alternativa a.")

F(20, "est", 2, "Oxana tiene un pendrive de 32Gb de capacidad, al parecer un virus lo ha contaminado y desea salvar su contenido cuanto antes. Los archivos guardados tienen un tamaño que varían de 50Mb a 70Mb(1Gb = 1024Mb). ¿Cuál es el número máximo de archivos que potencialmente puede salvar?",
  ["655", "468", "474", "642"], ["655", "468", "474", "642"], "floor(32*1024/50)", "Para tener la mayor cantidad de archivos, cada uno debe ser lo más pequeño posible.", "Con el tamaño mínimo, 50 Mb, caben más archivos.",
  ["32 Gb = 32 · 1024 = 32 768 Mb.", "32 768/50 = 655,36.", "Solo cuentan archivos completos: 655."], "655 · 50 = 32 750 Mb.", ok="a", **L4, note="Es el mismo problema que el 13 del examen 2023.", mistake="Dividir entre 70 Mb y obtener 468.", trap="floor(32*1024/70)", trap_opt="b")

F(21, "est", 2, "Un supervisor agroindustrial evalúa el rendimiento de producción de harina de camote de dos líneas de procesado (A y B) durante 5 turnos de trabajo:\n[tabla]\n¿Qué línea recomendará si busca mayor promedio y estabilidad en el proceso?",
  ["Línea A, porque su promedio es 50 y tiene menor variación.", "Línea B, porque su promedio es 50 y tiene mayor regularidad.", "Línea A por llegar a 60.", "Ambas por tener el mismo promedio."],
  ["(60-40<52-48)", "(52-48<60-40 and 250/5==250/5)", "false", "false"], TF, MED + " La estabilidad se mide con cuánto se separan los datos: un rango menor indica más regularidad.", "Las dos líneas tienen el mismo promedio. Se decide por la variación.",
  ["Línea A: 45 + 50 + 40 + 55 + 60 = 250, promedio 50. Rango: 60 − 40 = 20.", "Línea B: 48 + 52 + 50 + 49 + 51 = 250, promedio 50. Rango: 52 − 48 = 4.", "Mismo promedio, pero B varía menos: es más estable."], "Las notas de B están todas a 2 o menos de 50.", ok="b", **L4,
  table=[["Línea", "T1", "T2", "T3", "T4", "T5"], ["Línea A", "45", "50", "40", "55", "60"], ["Línea B", "48", "52", "50", "49", "51"]],
  note="Las alternativas se comprueban como afirmaciones.", mistake="Elegir la d, que solo mira el promedio e ignora la estabilidad.")

F(22, "est", 2, VEN + "Hallar el promedio de ventas de varones en total.", ["50", "51", "51.66", "52.66"], ["abs(50-155/3)<1/100", "abs(51-155/3)<1/100", "abs(5166/100-155/3)<1/100", "abs(5266/100-155/3)<1/100"], TF,
  MED, "Se suman las seis barras de los varones y se divide entre 6.", ["Varones: 60 + 40 + 60 + 70 + 50 + 30 = 310.", "Promedio: 310/6 ≈ 51,67."], "310/6 = 51,666…, que se escribe 51,66 en la alternativa.", ok="c", **L4, fig="fe1_ventas",
  note="El gráfico es compartido por las preguntas 22 y 23. Las alternativas se comprueban por cercanía.", mistake="Dividir entre 5 meses y obtener 62.")

F(23, "est", 2, VEN + "Hallar la diferencia entre el promedio de ventas de mujeres con el de varones desde enero a mayo.", ["0", "10", "6", "30"], ["0", "10", "6", "30"], "abs((70+60+50+40+30)/5-(60+40+60+70+50)/5)", MED,
  "Solo se usan cinco meses, de enero a mayo.", ["Mujeres: 70 + 60 + 50 + 40 + 30 = 250 y 250/5 = 50.", "Varones: 60 + 40 + 60 + 70 + 50 = 280 y 280/5 = 56.", "Diferencia: 56 − 50 = 6."], "El promedio de varones es mayor en 6.", ok="c", **L4, fig="fe1_ventas",
  mistake="Incluir junio y calcular con seis meses.")

F(24, "est", 3, "El profesor Otto reporta a la dirección académica que, en una prueba de nivelación de 6 preguntas evaluadas de 1 a 22, el promedio fue 15, la mediana 14, la moda 12 y la calificación más baja fue 10. Halle la nota máxima obtenida en una de las preguntas.",
  ["18", "19", "20", "22"], ["18", "19", "20", "22"], "max(select(f->size(select(e->e<f and e>=17 and 10+12+12+16+e+f==90,seq(e,e,16,22)))>0,seq(f,f,16,22)))",
  MED, "Con 6 notas, la mediana es el promedio de la tercera y la cuarta. La moda 12 se repite dos veces.",
  ["Suma: 6 · 15 = 90. Mínima: 10.", "La moda 12 aparece dos veces, así que la segunda y la tercera nota valen 12.", "Mediana: (12 + c4)/2 = 14, así que la cuarta nota es 16.", "10 + 12 + 12 + 16 + e + f = 90, así que e + f = 40. Con f ≤ 22 y e < f, f = 22 con e = 18, o f = 21 con e = 19.", "La nota máxima entre las alternativas es 22."],
  "10, 12, 12, 16, 18, 22: suma 90, mediana 14 y moda 12.", ok="d", **L4, review=True,
  note="También sirve 10, 12, 12, 16, 19, 21, con máxima 21, que no está entre las alternativas. La clave 22 corresponde a la primera solución.", mistake="Elegir 20, que obliga a e = f = 20 y duplica una nota, haciendo bimodal la serie.")

F(25, "est", 1, EQ + "Calcule el promedio de edad en el equipo A", ["12", "12.2", "12.6", "13"], ["12", "61/5", "63/5", "13"], "(11*3+12*7+13*4+14*1)/15",
  MED, "Es una media ponderada: cada edad se multiplica por su frecuencia.", ["Equipo A: 11·3 = 33, 12·7 = 84, 13·4 = 52 y 14·1 = 14.", "Suma: 183. Media: 183/15 = 12,2."], "12,2 · 15 = 183.", ok="b", **L4, table=TEQ,
  note="Las alternativas se comprueban como fracciones: 12,2 es 61/5 y 12,6 es 63/5.", mistake="Promediar las cuatro edades, 12,5, sin ponderar.")

F(26, "est", 2, EQ + "En cuanto varía el promedio de edad comparando con el equipo B.", ["Disminuye en 0.8", "Disminuye en 0.6", "Aumenta en 0.8", "No varía, son iguales."], ["-4/5", "-3/5", "4/5", "0"],
  "(11*3+12*7+13*4+14*1)/15-(11*2+12*2+13*5+14*6)/15", MED, "Se halla la media del equipo B y se compara con la del equipo A.",
  ["Equipo B: 11·2 = 22, 12·2 = 24, 13·5 = 65 y 14·6 = 84. Suma 195 y media 195/15 = 13.", "Equipo A: 12,2.", "12,2 − 13 = −0,8: el promedio del equipo A disminuye 0,8 respecto al equipo B."], "13 − 12,2 = 0,8.", ok="a", **L4, table=TEQ,
  note="Las alternativas se comprueban como fracciones: 0,8 es 4/5 y 0,6 es 3/5.", mistake="Invertir la comparación y responder que aumenta 0,8.")
