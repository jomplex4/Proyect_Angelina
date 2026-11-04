"""Ficha de Aritmética, semana 4, de Junior Burga: porcentajes, aumentos y descuentos sucesivos, precio de compra y venta. 30 problemas, 5 alternativas."""
from model import ficha
F = ficha(4, "arit")
LAB = dict(letters="abcde")
PC = "Un porcentaje r% de una cantidad es r/100 de ella. Un aumento de r% multiplica por 1 + r/100 y un descuento de r% multiplica por 1 − r/100. Los aumentos y descuentos sucesivos se multiplican, no se suman."
AR = " El área de una figura cambia con el cuadrado del factor de su lado o su radio."
PV = " Precio de venta = costo + ganancia. Una ganancia sobre el costo se calcula con el costo, y sobre el precio de venta, con el precio de venta."

F(1, "porc", 1, "Hallar el 40% del 20% de 200.", ["16", "32", "26", "24", "N.A."], ["16", "32", "26", "24", "undef"], "40/100*20/100*200", PC, "Se calcula de adentro hacia afuera.",
  ["20% de 200 = 40.", "40% de 40 = 16."], "0,4 · 0,2 = 0,08 y 0,08 · 200 = 16.", ok="a", **LAB, mistake="Sumar los porcentajes: 60% de 200 = 120.")

F(2, "porc", 1, "Calcular el 20% del 30% del 80% de 5/8 de 8000", ["200", "240", "320", "400", "250"], ["200", "240", "320", "400", "250"], "20/100*30/100*80/100*5/8*8000", PC, "Se aplica cada parte, una después de la otra.",
  ["5/8 de 8000 = 5000.", "80% de 5000 = 4000.", "30% de 4000 = 1200.", "20% de 1200 = 240."], "0,2 · 0,3 · 0,8 = 0,048 y 0,048 · 5000 = 240.", ok="b", **LAB, mistake="Sumar 20% + 30% + 80% en vez de aplicarlos uno tras otro.")

F(3, "porc", 2, "Calcular el 20% del 30% del 80% del 50 por 80 de 8000.", ["240", "270", "250", "280", "260"], ["240", "270", "250", "280", "260"], "20/100*30/100*80/100*(50/80)*8000", PC + " El 50 por 80 de una cantidad es 50/80 de ella.", "50 por 80 es la fracción 50/80, igual a 5/8.",
  ["50/80 de 8000 = 5000.", "80% de 5000 = 4000, 30% de 4000 = 1200 y 20% de 1200 = 240."], "Es el mismo cálculo del problema 2.", ok="a", **LAB,
  note="El 50 por 80 se entiende como la fracción 50/80. Con esa lectura el problema coincide con el 2.", mistake="Leer 50 por 80 como un producto, 4000, y obtener otro resultado.")

F(4, "porc", 2, "¿A qué único descuento equivale dos aumentos sucesivos del 5% y 10%?", ["14,5%", "20,5%", "30,5%", "18,5%", "N.A."], ["29/2", "41/2", "61/2", "37/2", "31/2"], "((105/100)*(110/100)-1)*100", PC,
  "Dos aumentos sucesivos equivalen a un solo aumento, no a un descuento.", ["Factor: 1,05 · 1,10 = 1,155.", "El aumento único es 15,5%.", "Ninguna alternativa a, b, c ni d vale 15,5%, así que la respuesta es N.A."], "5% + 10% = 15% y el 5% de 10% suma 0,5%: 15,5%.", ok="e", **LAB,
  note="El enunciado habla de un descuento, pero dos aumentos equivalen a un aumento. De todas formas, 15,5% no está entre las alternativas a a d.", mistake="Sumar 5% + 10% y marcar 14,5% por redondear. Los aumentos sucesivos suman además su producto.")

F(5, "porc", 1, "Dos descuentos sucesivos del 20% y 40%. ¿A qué único descuento equivale?", ["48%", "52%", "44%", "36%", "N.A."], ["48", "52", "44", "36", "undef"], "(1-(80/100)*(60/100))*100", PC, "Se paga 80% y luego 60% de eso.",
  ["Se paga 0,80 · 0,60 = 0,48 del precio.", "El descuento único es 100% − 48% = 52%."], "Con precio 100: 80 y luego 48. Descuento 52.", ok="b", **LAB, mistake="Sumar 20% + 40% = 60%.")

F(6, "porc", 1, "Tres descuentos sucesivos del 20%, 50% y 10% equivale a un único descuento de:", ["80%", "50%", "62%", "64%", "72%"], ["80", "50", "62", "64", "72"], "(1-(8/10)*(5/10)*(9/10))*100", PC, "Se paga 80%, luego 50% y luego 90%.",
  ["Se paga 0,8 · 0,5 · 0,9 = 0,36.", "Descuento único: 100% − 36% = 64%."], "Con 100: 80, 40 y 36.", ok="d", **LAB, mistake="Sumar 20% + 50% + 10% = 80%.", trap="80", trap_opt="a")

F(7, "porc", 2, "Dos descuentos sucesivos del 20% y 30%, seguidos por un incremento de 50%, ¿a qué único aumento o descuento equivale?", ["- 16%", "+ 2%", "- 12%", "+ 16%", "- 8%"], ["-16", "2", "-12", "16", "-8"], "((8/10)*(7/10)*(15/10)-1)*100", PC, "Se multiplican los tres factores.",
  ["Factores: 0,8 · 0,7 · 1,5 = 0,84.", "0,84 es 16% menos que 1: descuento de 16%."], "Con 100: 80, 56 y 84.", ok="a", **LAB, mistake="Sumar −20 − 30 + 50 = 0, y concluir que no cambia.")

F(8, "porc", 3, "El 30% del 20% de los 2/5 de un número equivale al 24% del 0,01% de 1000. Hallar dicho número.", ["100", "0,2", "1", "120", "N.A."], ["100", "1/5", "1", "120", "undef"], "solve(30/100*20/100*(2/5)*N=24/100*(1/10000*1000),N)[0]", PC,
  "Se calcula el lado derecho y luego se despeja el número.", ["0,01% de 1000 = 0,01/100 · 1000 = 0,1.", "24% de 0,1 = 0,024.", "Lado izquierdo: 0,3 · 0,2 · 0,4 · N = 0,024N.", "0,024N = 0,024, así que N = 1."], "0,3 · 0,2 · 0,4 = 0,024.", ok="c", **LAB, mistake="Leer 0,01% como 0,01 y obtener otro resultado.")

F(9, "porc", 3, "¿Cuál es el número cuyo, 10% de los 2/3 de su 21% equivale al 20% de los 3/10 de 7?", ["10", "20", "30", "40", "60"], ["10", "20", "30", "40", "60"], "solve(1/10*(2/3)*(21/100)*N=20/100*(3/10)*7,N)[0]", PC, "Se arma una ecuación con el número N.",
  ["Izquierda: 0,1 · 2/3 · 0,21 · N = 0,014N.", "Derecha: 0,2 · 0,3 · 7 = 0,42.", "0,014N = 0,42, así que N = 30."], "0,014 · 30 = 0,42.", ok="c", **LAB, mistake="Olvidar el 2/3 y obtener N = 20.")

F(10, "porc", 3, "El 15% de un número es el 12% de otro. Entonces el 30% de la suma de los números es:", ["36% del mayor", "35% del mayor", "45% del mayor", "54% del menor", "54% del mayor"], ["[36,1]", "[35,1]", "[45,1]", "[54,0]", "[54,1]"],
  "[30/100*(solve(15/100*A=12/100,A)[0]+1)*100,1]", PC, "Si 15% de A = 12% de B, se escribe A en función de B y se ve cuál es mayor.",
  ["15% de A = 12% de B da A = 0,8B. El mayor es B.", "A + B = 1,8B.", "30% de 1,8B = 0,54B, es decir 54% del mayor."], "Con B = 100: A = 80 y 30% de 180 = 54.", ok="e", **LAB,
  note="Las alternativas se comprueban como [porcentaje, 1 si es del mayor y 0 si es del menor].", mistake="Referir el 54% al menor, alternativa d. 54% de A = 43,2, no 54.")

F(11, "porc", 2, "Si \"A\" es 150% de \"B\", ¿Qué porcentaje es B de \"A + B\"?", ["25%", "30%", "40%", "20%", "40%"], ["25", "30", "40", "20", "40"], "1/(1+15/10)*100", PC, "Si A es 150% de B, A = 1,5B.",
  ["A = 1,5B, así que A + B = 2,5B.", "B/(A + B) = 1/2,5 = 0,4 = 40%."], "Con B = 20: A = 30, A + B = 50 y 20/50 = 40%.", ok="c", **LAB,
  warn="Las alternativas c y e dicen lo mismo, 40%. Las dos son correctas.", review=True,
  note="En la ficha las alternativas c y e son ambas 40%. Cualquiera de las dos se califica como correcta.", mistake="Calcular A/B o A/(A + B) y obtener 60%.")

F(12, "porc", 3, "Si el 84% N es igual al 105% de (N - 120). ¿Qué porcentaje de \"N\" representa 192?", ["24", "36", "48", "16", "32"], ["24", "36", "48", "16", "32"], "192/solve(84/100*N=105/100*(N-120),N)[0]*100", PC, "Primero se halla N con la ecuación y luego el porcentaje.",
  ["0,84N = 1,05(N − 120) = 1,05N − 126.", "126 = 0,21N, así que N = 600.", "192/600 = 0,32 = 32%."], "0,84 · 600 = 504 y 1,05 · 480 = 504.", ok="e", **LAB, mistake="Responder N = 600, que no está entre las alternativas.")

F(13, "porc", 3, "El 10% del 20% de a es igual al 30% del 40% de b. ¿Qué porcentaje de (3a + 2b) es (2a – 3b)?", ["30%", "45%", "80%", "75%", "25%"], ["30", "45", "80", "75", "25"],
  "subs((2*a-3*b)/(3*a+2*b)*100,[a,b],[solve(10/100*20/100*a=30/100*40/100*1,a)[0],1])", PC, "Se halla la relación entre a y b.",
  ["0,1 · 0,2 · a = 0,3 · 0,4 · b, es decir 0,02a = 0,12b, así que a = 6b.", "3a + 2b = 20b y 2a − 3b = 9b.", "9b/20b = 0,45 = 45%."], "Con b = 1 y a = 6: 9/20 = 45%.", ok="b", **LAB, mistake="Calcular (3a + 2b)/(2a − 3b) en vez de al revés y obtener 222%.")

F(14, "porc", 1, "¿En qué porcentaje aumenta el área de un cuadrado, cuando su lado aumenta en 20%?", ["20%", "21%", "44%", "55%", "80%"], ["20", "21", "44", "55", "80"], "((1+20/100)^2-1)*100", PC + AR, "El lado se multiplica por 1,2.",
  ["Lado: ×1,2.", "Área: ×1,2² = 1,44.", "Aumenta 44%."], "Con lado 10: área 100. Con lado 12: área 144.", ok="c", **LAB, mistake="Responder 20%, el mismo del lado, alternativa a.", trap="20", trap_opt="a")

F(15, "porc", 2, "¿En qué porcentaje varía el área de un cuadrado si su lado varía en un 100%?", ["400%", "200%", "300%", "280%", "350%"], ["400", "200", "300", "280", "350"], "((1+100/100)^2-1)*100", PC + AR, "Un aumento de 100% duplica el lado.",
  ["Lado: ×2.", "Área: ×4.", "Aumenta 300%."], "Con lado 1: área 1. Con lado 2: área 4. Aumento de 3 sobre 1.", ok="c", **LAB, mistake="Responder 400%, alternativa a. El área final es 400% del inicial, pero el aumento es 300%.", trap="400", trap_opt="a")

F(16, "porc", 2, "¿En qué porcentaje varía el área de un rectángulo cuando su largo aumenta en un 20% y su ancho se disminuye en un 50%?", ["80%", "40%", "50%", "69%", "95%"], ["80", "40", "50", "69", "95"], "abs((12/10)*(5/10)-1)*100", PC, "El área es largo por ancho: se multiplican los dos factores.",
  ["Largo: ×1,2. Ancho: ×0,5.", "Área: ×0,6.", "Disminuye 40%."], "Con 10 por 10: área 100. Con 12 por 5: área 60.", ok="b", **LAB, mistake="Restar 20 − 50 = −30 y marcar otro valor.")

F(17, "porc", 2, "¿En qué porcentaje varía el área de un círculo cuando su radio se reduce en 30%?", ["49%", "51%", "30%", "20%", "N.A."], ["49", "51", "30", "20", "undef"], "abs((7/10)^2-1)*100", PC + AR, "El radio se multiplica por 0,7.",
  ["Radio: ×0,7.", "Área: ×0,49.", "Disminuye 51%."], "Con radio 10: área 100π. Con radio 7: 49π.", ok="b", **LAB, mistake="Responder 49%, alternativa a. El área final es 49% del inicial, pero la variación es 51%.", trap="49", trap_opt="a")

F(18, "porc", 3, "Si la longitud de una circunferencia aumenta en 40%. ¿Qué ocurre con el área del círculo?", ["Aumenta 40%", "Aumenta 44%", "Aumenta 69%", "Aumenta 32%", "Aumenta 96%"], ["40", "44", "69", "32", "96"], "((14/10)^2-1)*100", PC + AR,
  "La longitud de la circunferencia es proporcional al radio.", ["Longitud = 2πr, así que el radio también aumenta 40%: ×1,4.", "Área: ×1,4² = 1,96.", "Aumenta 96%."], "Con radio 10: longitud 20π y área 100π. Con radio 14: 28π y 196π.", ok="e", **LAB, mistake="Responder 40%, la variación de la longitud, alternativa a.", trap="40", trap_opt="a")

F(19, "porc", 3, "En un triángulo si la base aumenta en 25%. ¿En cuánto debo disminuir la altura para que el área no varíe?", ["37,5%", "12,5%", "25%", "50%", "20%"], ["75/2", "25/2", "25", "50", "20"], "(1-1/(1+25/100))*100", PC,
  "El área es base por altura entre 2: el producto base por altura debe ser el mismo.", ["Base: ×1,25.", "La altura debe multiplicarse por 1/1,25 = 0,8.", "Debe disminuir 20%."], "Con base 100 y altura 100: 10000. Con base 125 y altura 80: 10000.", ok="e", **LAB, mistake="Disminuir 25%, alternativa c. Con 0,75 · 1,25 = 0,9375 el área baja.", trap="25", trap_opt="c")

F(20, "porc", 3, "Si \"A\" aumenta en 10% y \"B\" aumenta en 20% entonces\n§((3*A^3)*(2*B))/5\n, se incrementa aproximadamente en:", ["140%", "145%", "150%", "152%", "60%"], ["140", "145", "150", "152", "60"], "round(((11/10)^3*(12/10)-1)*100)", PC,
  "La expresión es proporcional a A³ por B: los números 3, 2 y 5 no cambian.", ["A se multiplica por 1,1, así que A³ se multiplica por 1,331.", "B se multiplica por 1,2.", "La expresión se multiplica por 1,331 · 1,2 = 1,5972.", "Aumenta aproximadamente 60%."], "1,5972 − 1 = 0,5972, que es 59,72%, cerca de 60%.", ok="e", **LAB,
  mistake="Sumar 3 · 10% + 20% = 50% y marcar 150%, alternativa c.", trap="150", trap_opt="c")

F(21, "porc", 3, "En una reunión el 60% del número de hombres es igual al 20% del número de mujeres. ¿Qué porcentaje del total son hombres?", ["40%", "25%", "30%", "15%", "75%"], ["40", "25", "30", "15", "75"], "1/(1+solve(60/100*1=20/100*M,M)[0])*100", PC, "Se escribe el número de mujeres en función del de hombres.",
  ["0,6H = 0,2M, así que M = 3H.", "Total: H + 3H = 4H.", "Hombres: H/4H = 25%."], "Con H = 10 y M = 30: 6 = 6 y 10/40 = 25%.", ok="b", **LAB, mistake="Dividir H entre M y obtener 33%.")

F(22, "porc", 2, "En una granja de aves, el 40% es de gallinas. Si se ha vendido el 205 de gallinas. ¿En qué porcentaje ha disminuido el número de aves?", ["10%", "6%", "8%", "12%", "7%"], ["10", "6", "8", "12", "7"], "(20/100*40/100)*100", PC,
  "Las gallinas son 40% del total y se vende una parte de ellas.", ["Se vende el 20% de las gallinas: 20% de 40% = 8% del total.", "Las aves disminuyen 8%."], "Con 100 aves: 40 gallinas, se venden 8. Quedan 92.", ok="c", **LAB,
  warn="La ficha dice el 205 de gallinas, que no tiene sentido. Se entiende el 20%, con el que el resultado coincide con la alternativa c.",
  note="Es muy probable que el 205 sea 20%. La academia debe confirmarlo.", review=True, mistake="Tomar el 20% del total de aves, y no de las gallinas.")

F(23, "porc", 3, "En una compañía trabajan 160 personas donde el 25% son mujeres. ¿Cuántas mujeres deben controlarse para que el 40% del personal sea de mujeres?", ["60", "80", "25", "30", "40"], ["60", "80", "25", "30", "40"], "solve((40+x)/(160+x)=40/100,x)[0]", PC,
  "Se entiende que se suman mujeres al personal, así que el total también crece.", ["Mujeres actuales: 25% de 160 = 40.", "Con x mujeres más, el total es 160 + x y las mujeres 40 + x.", "(40 + x)/(160 + x) = 0,4 da 40 + x = 64 + 0,4x, es decir 0,6x = 24 y x = 40."], "Con x = 40: 80 de 200 personas, que es 40%.", ok="e", **LAB,
  note="Controlarse parece una errata de contratarse. Si en lugar de sumar se cambiaran hombres por mujeres, el total seguiría en 160 y harían falta 24 mujeres, que no está entre las alternativas.", mistake="Mantener el total en 160 y obtener 24 mujeres, que no está entre las alternativas.")

F(24, "porc", 3, "En una granja el 25% del total de animales son pollos, el 30% son conejos y los restantes cuyes. Si el número de pollos fuera el doble. ¿Qué porcentaje del total serán los cuyes?", ["24%", "36%", "40%", "48%", "54%"], ["24", "36", "40", "48", "54"], "45/(2*25+30+45)*100", PC,
  "Cuyes son 100% − 25% − 30% = 45%. Si los pollos se duplican, el total cambia.", ["Con 100 animales: 25 pollos, 30 conejos y 45 cuyes.", "Con el doble de pollos: 50 pollos, 30 conejos y 45 cuyes. Total 125.", "Cuyes: 45/125 = 36%."], "45/125 = 0,36.", ok="b", **LAB, mistake="Dejar el total en 100 y responder 45%, que no está entre las alternativas.")

F(25, "porc", 3, "Si Juan pierde el 40% del dinero que tiene y luego gana el 50% de lo que le queda estaría perdiendo S/. 57 800. ¿Cuánto tenía Juan?", ["S/. 578 000", "520 000", "600 000", "N.A.", "480 000"], ["578000", "520000", "600000", "undef", "480000"], "solve(60/100*X*(1+50/100)-X=-57800,X)[0]", PC,
  "Se sigue lo que pasa con el dinero X: queda 60%, luego sube 50%.", ["Después de perder 40%: 0,6X.", "Gana 50% de 0,6X: 0,9X en total.", "Pierde 0,1X = 57 800, así que X = 578 000."], "0,9 · 578 000 = 520 200 y 578 000 − 520 200 = 57 800.", ok="a", **LAB, mistake="Calcular 57 800 · 10 = 578 000 sin ver que el 10% viene de 0,9.")

F(26, "porc", 2, "Un objeto se ofrece con el 20% de recargo y luego se descuenta el 20% por factura. El descuento es del:", ["0%", "5%", "6%", "4%", "10%"], ["0", "5", "6", "4", "10"], "(1-(12/10)*(8/10))*100", PC, "Un recargo de 20% multiplica por 1,2 y un descuento de 20% por 0,8.",
  ["Factor: 1,2 · 0,8 = 0,96.", "El precio final es 96% del original: 4% menos."], "Con 100: 120 y luego 96.", ok="d", **LAB,
  note="El descuento neto se mide respecto al precio original. El descuento se aplica sobre el precio con recargo.", mistake="Pensar que 20% − 20% = 0% y marcar la alternativa a.", trap="0", trap_opt="a")

F(27, "porc", 1, "Un comerciante compró una bicicleta a 1200 soles y lo vendió ganando el 20% del costo. ¿A cuánto la vendió?", ["S/. 1440", "S/. 1400", "S/. 1300", "S/. 1700", "N.A."], ["1440", "1400", "1300", "1700", "undef"], "1200*(1+20/100)", PC + PV, "La ganancia es 20% del costo.",
  ["Ganancia: 20% de 1200 = 240.", "Precio de venta: 1200 + 240 = 1440."], "1200 · 1,2 = 1440.", ok="a", **LAB, mistake="Responder solo la ganancia, 240.")

F(28, "porc", 2, "Un libro que costo 140 soles se vende ganando el 20% del precio de venta. ¿En cuánto se vendió?", ["S/. 150", "S/. 180", "S/. 200", "S/. 175", "S/. 130"], ["150", "180", "200", "175", "130"], "solve(PV-140=20/100*PV,PV)[0]", PC + PV, "La ganancia es el 20% del precio de venta, no del costo.",
  ["Sea PV el precio de venta. Ganancia: PV − 140 = 0,2PV.", "0,8PV = 140, así que PV = 175."], "175 − 140 = 35 y 35 es el 20% de 175.", ok="d", **LAB, mistake="Calcular 20% de 140 y obtener 168, que no está.")

F(29, "porc", 3, "Un comerciante vende las dos últimas bicicletas que le quedan en S/. 1200 cada una. En una ganó el 25% en la otra perdió el 25% ¿qué afirmación es correcta?", ["no gano ni perdió", "ganó S/. 160", "perdió S/. 160", "perdió S/. 200", "N.A."], ["0", "160", "-160", "-200", "undef"],
  "1200+1200-(1200/(1+25/100)+1200/(1-25/100))", PC + PV, "Cada porcentaje es del costo de esa bicicleta, que es distinto en cada una.",
  ["La que ganó 25%: 1200 = 1,25 · costo, así que el costo fue 960.", "La que perdió 25%: 1200 = 0,75 · costo, así que el costo fue 1600.", "Costo total: 2560. Ventas: 2400. Perdió 160."], "960 + 1600 = 2560 y 2560 − 2400 = 160.", ok="c", **LAB, mistake="Pensar que 25% − 25% = 0 y marcar la alternativa a.")

F(30, "porc", 3, "Un artículo se ha vendido en S/. 5400 donde se está ganando el 35% del costo. ¿A cómo se debió vender el mismo artículo para ganar el 20% del precio de venta?", ["S/. 4000", "S/. 3500", "S/. 4500", "S/. 4800", "S/. 5000"], ["4000", "3500", "4500", "4800", "5000"], "(5400/(1+35/100))/(1-20/100)", PC + PV, "Primero se halla el costo y luego el nuevo precio.",
  ["Costo: 5400 = 1,35 · costo, así que costo = 4000.", "Con ganancia de 20% del precio de venta: PV − 4000 = 0,2PV, es decir 0,8PV = 4000.", "PV = 5000."], "5000 − 4000 = 1000, que es 20% de 5000.", ok="e", **LAB, mistake="Responder el costo, 4000, alternativa a, que no incluye ninguna ganancia.", trap="4000", trap_opt="a")
