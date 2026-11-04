"""Ficha de Estadística, semana 1, de Sergio Huanca: porcentajes e interpretación de datos. 26 preguntas."""
from model import ficha
F = ficha(1, "est")
L4 = dict(letters="abcd")
PC = "Un porcentaje r% de una cantidad es r/100 de ella. Un aumento de r% multiplica por 1 + r/100 y un descuento de r% por 1 − r/100. Los cambios sucesivos se multiplican."
PIE = "En un gráfico circular, cada sector es proporcional a su cantidad: 360° corresponden al total, así que a x° le toca el porcentaje x/360."
BAR = "En un gráfico de barras, la altura de cada barra es la cantidad que representa. Para un porcentaje se divide la cantidad entre el total."
OFE = [["", "Oferta en tienda 1", "Oferta en tienda 2"], ["Descuento en todo electrodomésticos", "40 %", "30 %"], ["Descuento adicional pagando en efectivo", "+10 %", "+20 %"]]
TF = "true"

F(1, "porc", 1, "En una tienda comercial, una casaca cuesta S/ 300. Si por liquidación se le aplica un descuento del 30%, ¿cuánto se paga finalmente?",
  ["S/ 90", "S/ 240", "S/ 210", "S/ 270"], ["90", "240", "210", "270"], "300*(1-30/100)", PC, "Con un descuento de 30% se paga 70%.",
  ["Se paga 100% − 30% = 70% del precio.", "70% de 300 = 210."], "El descuento es 90 y 300 − 90 = 210.", ok="c", **L4, mistake="Responder el descuento, S/ 90, alternativa a, en vez de lo que se paga.", trap="90", trap_opt="a")

F(2, "porc", 1, "Luego de aplicarle un aumento del 25% al precio de una tablet, su costo final es de S/ 750. ¿Cuál era su precio original?",
  ["S/ 560", "S/ 600", "S/ 580", "S/ 625"], ["560", "600", "580", "625"], "solve(X*(1+25/100)=750,X)[0]", PC, "El precio final es 125% del original.",
  ["1,25 · original = 750.", "original = 750/1,25 = 600."], "600 · 1,25 = 750.", ok="b", **L4, mistake="Restar el 25% de 750 y obtener 562,5. El 25% es del precio original, no del final.")

F(3, "porc", 1, "Un escritorio de oficina cuesta S/ 1200. Primero recibe un descuento del 20% y luego, sobre dicho monto rebajado, se le aplica otro descuento adicional del 15%. ¿Cuánto se paga al final?",
  ["S/ 780", "S/ 840", "S/ 816", "S/ 900"], ["780", "840", "816", "900"], "1200*(1-20/100)*(1-15/100)", PC, "Cada descuento se aplica sobre el precio ya rebajado.",
  ["Primer descuento: 1200 · 0,80 = 960.", "Segundo descuento: 960 · 0,85 = 816."], "Descuento único: 1 − 0,8 · 0,85 = 32%, y 1200 · 0,68 = 816.", ok="c", **L4, mistake="Sumar los descuentos, 35%, y obtener 780, alternativa a.", trap="1200*(1-35/100)", trap_opt="a")

F(4, "porc", 1, "Una bicicleta cuesta S/ 600. Se le aplica un descuento del 20% y, posteriormente, sobre el valor ya descontado, se recarga un 15% por el servicio de ensamblaje e instalación. ¿Cuál es el precio final?",
  ["S/ 552", "S/ 528", "S/ 540", "S/ 570"], ["552", "528", "540", "570"], "600*(1-20/100)*(1+15/100)", PC, "El recargo se aplica sobre el valor ya descontado.",
  ["Con descuento: 600 · 0,80 = 480.", "Con recargo: 480 · 1,15 = 552."], "El recargo es 72 y 480 + 72 = 552.", ok="a", **L4, mistake="Restar 20% y sumar 15% sobre 600: 600 − 120 + 90 = 570, alternativa d.", trap="600-120+90", trap_opt="d")

F(5, "porc", 2, "En una academia de 80 alumnos, el 60% son hombres. Si el 25% de los hombres y el 40% de las mujeres usan lentes, ¿qué porcentaje del total de alumnos usan lentes?",
  ["29%", "33%", "31%", "35%"], ["29", "33", "31", "35"], "(25/100*(60/100*80)+40/100*(40/100*80))/80*100", PC, "Se cuentan los que usan lentes entre hombres y entre mujeres por separado.",
  ["Hombres: 60% de 80 = 48. Mujeres: 80 − 48 = 32.", "Usan lentes: 25% de 48 = 12 y 40% de 32 = 12,8. Total 24,8.", "24,8/80 = 0,31 = 31%."], "0,6 · 0,25 + 0,4 · 0,4 = 0,15 + 0,16 = 0,31.", ok="c", **L4, mistake="Promediar 25% y 40%: 32,5%, que no está entre las alternativas.")

F(6, "porc", 2, "El costo de un equipo de sonido subió un 20% y, un mes después, sobre el nuevo valor, aumentó otro 10%. Si el precio final del equipo es de S/ 792, ¿cuál era su valor inicial?",
  ["S/ 580", "S/ 620", "S/ 600", "S/ 640"], ["580", "620", "600", "640"], "solve(X*(1+20/100)*(1+10/100)=792,X)[0]", PC, "Los dos aumentos se multiplican: 1,2 · 1,1 = 1,32.",
  ["Precio final = inicial · 1,32.", "inicial = 792/1,32 = 600."], "600 · 1,2 = 720 y 720 · 1,1 = 792.", ok="c", **L4, mistake="Restar 30% de 792 y obtener 554,4.")

F(7, "porc", 3, "Se sabe que M = X · Y³. ¿Cómo varía M si la variable X aumenta en 20% y la variable Y disminuye en 10%?",
  ["Disminuye 12.58%", "Aumenta 8.24%", "Disminuye 12.52%", "Disminuye 15.40%"], ["-1258/100", "824/100", "-1252/100", "-1540/100"], "((12/10)*(9/10)^3-1)*100", PC + " Si una variable está al cubo, su factor también.",
  "X se multiplica por 1,2 y Y³ por 0,9³.", ["Factor de X: 1,2. Factor de Y³: 0,9³ = 0,729.", "M se multiplica por 1,2 · 0,729 = 0,8748.", "Disminuye 100% − 87,48% = 12,52%."], "Con X = 10 e Y = 10: M = 10000. Con X = 12 e Y = 9: 12 · 729 = 8748.", ok="c", **L4,
  mistake="Olvidar el cubo y calcular 1,2 · 0,9 = 1,08, aumento de 8%, que se parece a la alternativa b.")

F(8, "porc", 2, "Marina desea comprar una licuadora por lo que visita dos tiendas en las que encuentra la licuadora que quiere con un mismo precio de etiqueta (precio sin descuento), pero con las ofertas que se muestran a continuación:\n[tabla]\nSegún la información mostrada para estas ofertas, ¿cuál de las siguientes afirmaciones es correcta para la compra de Marina?",
  ["Le conviene la oferta, ya sea de la tienda 1 o de la tienda 2, porque el descuento total en cada una de estas es el mismo",
   "Le conviene comprar en la tienda 2 porque el descuento total en la tienda 1 equivale al 54 % del precio de etiqueta y en la tienda 2 el descuento total equivale al 56 % del precio de etiqueta.",
   "Le conviene comprar en la tienda 1 porque el descuento total en esta tienda equivale al 46 % del precio de etiqueta y en la tienda 2 el descuento total equivale al 44 % del precio de etiqueta.",
   "Le conviene comprar en la tienda 2 porque el descuento total obtenido en la tienda 1 equivale a 4% y en la tienda 2 equivale a 6%"],
  ["(1-60/100*90/100==1-70/100*80/100)", "(1-60/100*90/100==54/100 and 1-70/100*80/100==56/100)", "(1-60/100*90/100==46/100 and 1-70/100*80/100==44/100)", "(1-60/100*90/100==4/100 and 1-70/100*80/100==6/100)"], TF,
  PC, "Se calcula qué fracción del precio de etiqueta se paga en cada tienda.", ["Tienda 1: se paga 0,60 · 0,90 = 0,54, es decir 54%. El descuento total es 46%.", "Tienda 2: se paga 0,70 · 0,80 = 0,56. El descuento total es 44%.", "Conviene la tienda 1, con 46% frente a 44%."],
  "Con una licuadora de S/ 100: en la tienda 1 se paga 54 y en la tienda 2, 56.", ok="c", **L4, table=OFE,
  note="En la ficha las ofertas son dos carteles; aquí sus datos están en la tabla. Es el mismo problema que el 14 del examen 2024.", mistake="Sumar 40% + 10% = 30% + 20%, como en la alternativa a.")

F(9, "porc", 2, "Said posee un cupón con el 15% de descuento sobre cualquier compra. Al ingresar a una tienda, ve un abrigo que ya presenta un descuento automático del 20%. Si Said aplica su cupón al comprar dicho abrigo, ¿qué porcentaje de descuento total obtendrá?",
  ["32%", "28%", "35%", "30%"], ["32", "28", "35", "30"], "(1-(85/100)*(80/100))*100", PC, "Se paga 80% con el descuento automático y 85% con el cupón.",
  ["Se paga 0,80 · 0,85 = 0,68.", "Descuento total: 100% − 68% = 32%."], "Con S/ 100: 80 y luego 68.", ok="a", **L4, mistake="Sumar 15% + 20% = 35%, alternativa c.", trap="35", trap_opt="c")

F(10, "porc", 2, "Paulo desea adquirir una refrigeradora de S/ 2000 y evalúa dos establecimientos:\nTIENDA A: 25% de descuento directo + 10% adicional por aniversario.\nTIENDA B: 30% de descuento directo + 5% adicional con tarjeta de la tienda.\n¿En qué tienda le conviene realizar la compra y cuánto ahorra con respecto a la otra?",
  ["En la Tienda B, ahorrando S/ 10 respecto a A.", "En la Tienda A, ahorrando S/ 20 respecto a B.", "En la Tienda B, ahorrando S/ 20 respecto a A.", "En cualquiera de las dos, pagan exactamente lo mismo."],
  ["(2000*75/100*90/100-2000*70/100*95/100==10)", "(2000*70/100*95/100-2000*75/100*90/100==20)", "(2000*75/100*90/100-2000*70/100*95/100==20)", "(2000*75/100*90/100==2000*70/100*95/100)"], TF,
  PC, "Se calcula el precio final en cada tienda y se comparan.", ["Tienda A: 2000 · 0,75 = 1500 y 1500 · 0,90 = 1350.", "Tienda B: 2000 · 0,70 = 1400 y 1400 · 0,95 = 1330.", "En B se paga 1330 y en A 1350: B conviene y ahorra 20."],
  "1350 − 1330 = 20.", ok="c", **L4, note="Las alternativas se comprueban como condiciones: solo la c es verdadera.", mistake="Sumar los descuentos: A da 35% y B da 35%, y concluir que pagan lo mismo, alternativa d.")

F(11, "est", 1, "En una comunidad vecinal se realizó una encuesta sobre la cantidad de integrantes que conforman cada núcleo familiar, obteniendo la siguiente tabla:\n[tabla]\n¿Cuántas familias componen en total la comunidad vecinal y cuántas tienen de 7 a más integrantes?",
  ["600 y 120", "600 y 80", "550 y 120", "600 y 230"], ["[600,120]", "[600,80]", "[550,120]", "[600,230]"], "[120+250+110+80+40,80+40]",
  "En una tabla de frecuencias, el total es la suma de las frecuencias y los que están en un grupo o más se suman.", "De 7 a más integrantes son los dos últimos grupos: [7 - 9> y [9 - 11>.",
  ["Total: 120 + 250 + 110 + 80 + 40 = 600 familias.", "De 7 a más integrantes: 80 + 40 = 120 familias."], "120 + 250 + 110 = 480 y 600 − 480 = 120.", ok="a", **L4,
  table=[["Composición (N° integrantes)", "N° de familias"], ["[1 - 3>", "120"], ["[3 - 5>", "250"], ["[5 - 7>", "110"], ["[7 - 9>", "80"], ["[9 - 11>", "40"]],
  note="En la ficha la segunda alternativa está rotulada d por error. Aquí se muestra como b.", mistake="Contar solo el grupo [7 - 9> y obtener 80, alternativa b.", trap="[600,80]", trap_opt="b")

F(12, "est", 1, "¿Qué porcentaje representa el número de familias de 9 a más integrantes con respecto al total de familias?",
  ["5.5%", "8.33%", "6.67%", "10%"], ["11/2", "25/3", "20/3", "10"], "40/(120+250+110+80+40)*100", PC, "Es la misma tabla de la pregunta 11.",
  ["Familias de 9 a más integrantes: 40. Total: 600.", "40/600 = 0,0667 = 6,67%."], "6,67% de 600 = 40.", ok="c", **L4,
  table=[["Composición (N° integrantes)", "N° de familias"], ["[1 - 3>", "120"], ["[3 - 5>", "250"], ["[5 - 7>", "110"], ["[7 - 9>", "80"], ["[9 - 11>", "40"]],
  note="Las alternativas se comprueban como fracciones: 8,33 es 25/3 y 6,67 es 20/3.", mistake="Dividir 40 entre 480 y obtener 8,33%, alternativa b.", trap="25/3", trap_opt="b")

CAFE = "Sea la distribución de ventas en cafetería representada mediante una gráfica circular.\n"
F(13, "est", 1, CAFE + "A partir del gráfico, si se vendieron 30 postres en total, ¿cuántos sándwiches y cuántas bebidas se consumieron?",
  ["75 y 90", "75 y 80", "60 y 80", "80 y 90"], ["[75,90]", "[75,80]", "[60,80]", "[80,90]"], "[25/100*(30/(10/100)),30/100*(30/(10/100))]", PIE, "Los 30 postres son el 10% del total.",
  ["30 = 10% del total, así que el total es 300.", "Sándwiches: 25% de 300 = 75. Bebidas: 30% de 300 = 90."], "75 + 45 + 60 + 90 + 30 = 300.", ok="a", **L4, fig="fe1_cafe",
  note="El gráfico es compartido por las preguntas 13, 14 y 15.", mistake="Tomar 30 como el total y calcular 7,5 y 9.")

F(14, "est", 1, CAFE + "Si el reporte indica que se registraron un total de 45 ensaladas consumidas, ¿cuántos consumos totales se realizaron en la cafetería?",
  ["250", "350", "300", "400"], ["250", "350", "300", "400"], "45/(15/100)", PIE, "Las ensaladas son el 15% del total.",
  ["45 = 15% del total.", "Total = 45/0,15 = 300."], "15% de 300 = 45.", ok="c", **L4, fig="fe1_cafe", mistake="Multiplicar 45 · 100/15 mal y obtener 350.")

F(15, "est", 1, CAFE + "Si en un día tranquilo se consumieron únicamente 12 sopas, ¿cuántas bebidas se vendieron en dicho día?",
  ["15", "20", "18", "24"], ["15", "20", "18", "24"], "30/100*(12/(20/100))", PIE, "Las sopas son el 20% y las bebidas el 30%.",
  ["12 = 20% del total, así que el total es 60.", "Bebidas: 30% de 60 = 18."], "18/12 = 30/20.", ok="c", **L4, fig="fe1_cafe", mistake="Responder 12 · 30/20 mal, o 24, la mitad de bebidas por error de proporción.")

CUR = "El gráfico muestra las preferencias de los principales cursos que se desarrollan en una emblemática I. E. cuyo director es el emblemático Cid Herald Carrigan\nDel enunciado, resuelve las preguntas 16, 17, 18 y 19\n"
F(16, "est", 1, CUR + "¿Cuál es el valor de a°?",
  ["108°", "120°", "112°", "126°"], ["108", "120", "112", "126"], "360-(90+54+36+72)", PIE, "Todos los sectores suman 360°.",
  ["Los ángulos dados suman 90° + 54° + 36° + 72° = 252°.", "a = 360° − 252° = 108°."], "252° + 108° = 360°.", ok="a", **L4, fig="fe1_cursos",
  note="El gráfico es compartido por las preguntas 16 a 19. En el original los sectores no llevan rótulo; aquí se rotulan con los datos de la leyenda.", mistake="Sumar mal los cuatro ángulos.")

F(17, "est", 2, CUR + "¿Qué porcentaje de alumnos prefiere otros cursos?",
  ["25%", "32%", "30%", "35%"], ["25", "32", "30", "35"], "(360-(90+54+36+72))/360*100", PIE, "El ángulo de Otros, 108°, entre 360°.",
  ["a = 108° como en la pregunta 16.", "108/360 = 0,3 = 30%."], "30% de 360° = 108°.", ok="c", **L4, fig="fe1_cursos", mistake="Dividir 108 entre 100 y obtener 108%.")

F(18, "est", 2, CUR + "Si se sabe que 180 alumnos prefieren el área de Matemática, ¿cuántos alumnos prefieren el área de Comunicación?",
  ["120", "150", "144", "160"], ["120", "150", "144", "160"], "72/360*(180/(90/360))", PIE, "Matemática, 90°, es la cuarta parte de la circunferencia.",
  ["90° es 1/4 del total, así que el total es 4 · 180 = 720.", "Comunicación: 72/360 de 720 = 144."], "72/90 de 180 = 144.", ok="c", **L4, fig="fe1_cursos", mistake="Tomar 180 como el total y calcular 36.")

F(19, "est", 3, CUR + "Considerando la información del ejercicio anterior, ¿en cuánto excede el número de estudiantes que prefieren el sector \"Otros\" a los que prefieren \"Historia\"?",
  ["144", "150", "128", "136"], ["144", "150", "128", "136"], "((360-(90+54+36+72))-36)/360*(180/(90/360))", PIE, "El total es 720 alumnos, como en la pregunta 18.",
  ["Otros: 108° de 360° de 720 = 216.", "Historia: 36° de 360° de 720 = 72.", "Diferencia: 216 − 72 = 144."], "(108° − 36°)/360° · 720 = 144.", ok="a", **L4, fig="fe1_cursos", mistake="Calcular 108 − 36 = 72 en grados y no pasar a alumnos.")

F(20, "geo", 1, "Si cinco amigos desean repartir una pizza en partes iguales, ¿cuál es la medida del ángulo x que debería tener cada corte en la figura?",
  ["70°", "80°", "72°", "76°"], ["70", "80", "72", "76"], "360/5", "Una circunferencia completa mide 360°. Repartir en partes iguales es dividir entre la cantidad de partes.", "Cada amigo recibe un sector con el mismo ángulo.",
  ["Ángulo total: 360°.", "x = 360°/5 = 72°."], "5 · 72° = 360°.", ok="C", letters="ABCD", fig="fe1_pizza",
  note="En el original es una fotografía de una pizza; aquí se redibuja como un sector.", mistake="Dividir 360° entre 4 o entre 6 por contar mal a los amigos.")

DEP = "A partir de la siguiente gráfica estadística de preferencias deportivas:\n"
F(21, "est", 1, DEP + "¿Qué porcentaje de los encuestados prefiere Atletismo?",
  ["15%", "25%", "20%", "30%"], ["15", "25", "20", "30"], "4/(4+8+6+2)*100", BAR, "Se suman las barras para el total y se compara la de Atletismo.",
  ["Total de encuestados: 4 + 8 + 6 + 2 = 20.", "Atletismo: 4/20 = 0,2 = 20%."], "4 · 5 = 20.", ok="c", **L4, fig="fe1_deportes", note="El gráfico es compartido por las preguntas 21 y 22.", mistake="Dividir 4 entre 8 y obtener 50%.")

F(22, "est", 1, DEP + "¿A qué porcentaje del total de encuestados NO le gusta el Ciclismo?",
  ["40%", "60%", "50%", "70%"], ["40", "60", "50", "70"], "(1-8/(4+8+6+2))*100", BAR, "Los que no prefieren Ciclismo son el total menos los de Ciclismo.",
  ["Ciclismo: 8 de 20, es decir 40%.", "No le gusta: 100% − 40% = 60%."], "Los demás suman 4 + 6 + 2 = 12 y 12/20 = 60%.", ok="b", **L4, fig="fe1_deportes", mistake="Responder 40%, la alternativa a, que es el porcentaje que sí lo prefiere.", trap="40", trap_opt="a")

VEN = "El gráfico muestra las ventas realizadas en miles de soles por varones y mujeres en una tienda comercial:\n"
F(23, "est", 1, VEN + "Halla la diferencia en miles de soles entre lo que vendieron las mujeres y los varones en el mes de abril.",
  ["30", "80", "95", "81"], ["30", "80", "95", "81"], "70-40", BAR, "En abril se lee la barra de cada grupo.",
  ["Abril: mujeres 40 y varones 70.", "Diferencia: 70 − 40 = 30."], "30 mil soles más en varones.", ok="a", **L4, fig="fe1_ventas", note="El gráfico es compartido por las preguntas 23, 24 y 25.", mistake="Sumar 40 + 70 = 110, que no está entre las alternativas.")

F(24, "est", 1, VEN + "Hallar la diferencia entre el total de ventas de los varones y el total de ventas de las mujeres.",
  ["50", "20", "10", "40"], ["50", "20", "10", "40"], "(60+40+60+70+50+30)-(70+60+50+40+30+40)", BAR, "Se suman las seis barras de cada grupo.",
  ["Varones: 60 + 40 + 60 + 70 + 50 + 30 = 310.", "Mujeres: 70 + 60 + 50 + 40 + 30 + 40 = 290.", "Diferencia: 310 − 290 = 20."], "310 + 290 = 600 en total.", ok="b", **L4, fig="fe1_ventas", mistake="Restar solo un mes en lugar de los seis.")

F(25, "est", 2, VEN + "¿Qué tanto por ciento del total de ventas corresponden a los varones?",
  ["55%", "52%", "60%", "80%", "38%"], ["55", "52", "60", "80", "38"], "round((60+40+60+70+50+30)/(60+40+60+70+50+30+70+60+50+40+30+40)*100)", BAR, "Se compara la suma de los varones con la suma total.",
  ["Varones: 310. Mujeres: 290. Total: 600.", "310/600 = 0,5167, que es aproximadamente 52%."], "52% de 600 = 312, muy cerca de 310.", ok="b", letters="abcde", fig="fe1_ventas",
  note="El resultado exacto es 51,67%. Se compara con la alternativa más cercana.", mistake="Elegir 60% por dividir 310 entre 520.")

F(26, "est", 2, "La científica Su Lee de Herald es una investigadora que estudia el nivel de logro alcanzado por los estudiantes de dos secciones del cuarto grado de secundaria en el Área de Matemática. Ella presenta los datos obtenidos en un diagrama de barras.\n¿Qué afirmación no es válida según el gráfico?",
  ["La suma de las cantidades de los estudiantes que alcanzaron los niveles de logro A y AD en ambas secciones es la misma.", "La cantidad de estudiantes con nivel de logro B es menor a la cantidad de estudiantes con nivel de logro A",
   "La cantidad de estudiantes de la sección B que aún no han alcanzado el nivel de logro A es mayor que la cantidad de estudiantes de la sección A.", "La cantidad de estudiantes que obtuvieron niveles de logro A o B es mayor que la sección A."],
  ["not(12+2==10+4)", "not(10+11<12+10)", "not(5+11>6+10)", "not(12+10>10+11)"], TF,
  BAR + " Para una afirmación que no es válida se busca la que resulte falsa.", "Se leen las seis barras de cada sección y se prueba cada afirmación.",
  ["Valores. Sección A: C 6, B 10, A 12, AD 2. Sección B: C 5, B 11, A 10, AD 4.", "a: A + AD en la sección A es 12 + 2 = 14 y en la B es 10 + 4 = 14. Es válida.", "b: nivel B en total 10 + 11 = 21 y nivel A 12 + 10 = 22. 21 < 22. Es válida.", "c: en la sección B no alcanzaron el nivel A los de C y B: 5 + 11 = 16. En la sección A son 6 + 10 = 16. No es mayor, es igual. No es válida.", "d: A o B en la sección A es 12 + 10 = 22 y en la B, 21. Es válida."],
  "Aunque se comparara con los 30 estudiantes de la sección A, 16 tampoco sería mayor.", ok="c", **L4, fig="x25_30",
  note="La alternativa d está escrita sin la palabra en, y se lee en la sección A. La alternativa c compara con los que no alcanzaron el nivel A de la sección A. Es el mismo problema que el 30 del examen 2025.",
  mistake="Contar el nivel AD entre los que aún no alcanzan el nivel A. AD es un nivel superior.")
