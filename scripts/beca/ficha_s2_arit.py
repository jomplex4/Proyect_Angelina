"""Ficha de Aritmética, semana 2, de Junior Burga: interés simple. 20 problemas, 5 alternativas."""
from model import ficha
F = ficha(2, "arit")
LAB = dict(letters="abcde")
IS = ("Interés simple: I = C·r·t, con la tasa r y el tiempo t en la misma unidad. El monto es M = C + I. "
      "Un año tiene 12 meses, un mes comercial 30 días, un trimestre 3 meses y una quincena 15 días.")

F(1, "porc", 1, "Calcular el interés producido por S/. 3000 impuestos durante 3 años, al 30%",
  ["S/. 2000", "2500", "2700", "2800", "2900"], ["2000", "2500", "2700", "2800", "2900"], "3000*30/100*3", IS, "La tasa es anual y el tiempo está en años: se pueden usar tal cual.",
  ["I = 3000 · 0,30 · 3.", "I = 2700."], "Cada año produce 900 y en 3 años 2700.", ok="c", **LAB, mistake="Olvidar multiplicar por los 3 años y responder 900.")

F(2, "porc", 2, "Determinar el interés generado al depositar S/. 3600 al 5% trimestral durante 7 meses.",
  ["S/. 100", "300", "340", "420", "500"], ["100", "300", "340", "420", "500"], "3600*(5/100)/3*7", IS, "La tasa es trimestral y el tiempo en meses: se pasa la tasa a mensual.",
  ["5% trimestral es 5%/3 por mes.", "I = 3600 · (0,05/3) · 7.", "3600 · 0,05 = 180 por trimestre, es decir 60 por mes, y 60 · 7 = 420."], "En 6 meses son 2 trimestres: 360, y el mes que falta suma 60. Total 420.", ok="d", **LAB,
  mistake="Usar 7 como si fueran trimestres: 3600 · 0,05 · 7 = 1260.")

F(3, "porc", 1, "¿Cuál es el capital que se coloca al 25% durante 3 años, para obtener un interés de S/. 1620?",
  ["S/. 1620", "1920", "200", "2006", "2160"], ["1620", "1920", "200", "2006", "2160"], "solve(C*25/100*3=1620,C)[0]", IS, "Se despeja el capital de I = C·r·t.",
  ["1620 = C · 0,25 · 3 = 0,75C.", "C = 1620/0,75 = 2160."], "2160 · 0,25 · 3 = 1620.", ok="e", **LAB, mistake="Responder 1620, el mismo interés, sin dividir entre 0,75.")

F(4, "porc", 2, "¿A qué tasa de interés la suma de S/. 20000 llegaría a un monto de S/. 28000 colocada a un interés simple en 1 año 4 meses?",
  ["10%", "20", "30", "35", "40"], ["10", "20", "30", "35", "40"], "solve(20000*r*(16/12)=8000,r)[0]*100", IS, "El interés es el monto menos el capital, y 1 año 4 meses son 16 meses.",
  ["Interés: 28000 − 20000 = 8000.", "Tiempo: 16 meses = 4/3 de año.", "8000 = 20000 · r · 4/3, así que r = 8000 · 3/80000 = 0,30 = 30% anual."], "20000 · 0,30 · 4/3 = 8000.", ok="c", **LAB,
  note="Se entiende la tasa como anual.", mistake="Usar 16 como años, lo que da r = 2,5%.")

F(5, "porc", 2, "¿Cuál es el monto producido por un capital de S/. 7200 colocados al 4% anual durante 3 años y 4 meses?",
  ["S/. 7500", "8000", "8160", "9000", "9160"], ["7500", "8000", "8160", "9000", "9160"], "7200+7200*(4/100)*(10/3)", IS, "3 años 4 meses son 40 meses, es decir 10/3 de año. El monto incluye el capital.",
  ["I = 7200 · 0,04 · 10/3 = 960.", "M = 7200 + 960 = 8160."], "El 4% anual de 7200 es 288 y 288 · 10/3 = 960.", ok="c", **LAB,
  mistake="Responder el interés, 960, sin sumar el capital.")

F(6, "porc", 2, "¿Cuál es el capital que colocado al 6% quincenal genera en 2 meses un monto de S/. 7200?",
  ["S/. 30000", "40000", "50000", "55000", "60000"], ["30000", "40000", "50000", "55000", "60000"], "7200/(1+6/100*4)", IS, "2 meses son 4 quincenas. El monto es el capital más el interés.",
  ["En 4 quincenas el interés es 4 · 6% = 24% del capital.", "M = C · 1,24 = 7200, así que C = 7200/1,24 ≈ 5806,45.", "Ningún valor de las alternativas cumple eso."], "5806,45 · 1,24 = 7200.",
  answer="S/ 5806,45 aproximadamente, sin alternativa que lo recoja", answerVal="180000/31",
  warn="Con el enunciado tal cual, ninguna alternativa es correcta. La ficha parece tener una errata en la palabra monto.",
  note="Si 7200 fuera el interés y no el monto, el capital sería 7200/0,24 = 30000, que es la alternativa a. Es solo una hipótesis y la academia debe confirmarlo.", review=True, **LAB,
  mistake="Marcar 30000 sin avisar de que corresponde a un interés de 7200 y no a un monto.")

F(7, "porc", 3, "Se tiene un capital prestado a una determinada tasa de interés de tal manera que en 5 meses produce un monto de S/. 33750 y en 7 meses el monto producido es de S/. 35250. ¿Cuál sería el monto al terminado del año?",
  ["S/. 30000", "39000", "40000", "49000", "50000"], ["30000", "39000", "40000", "49000", "50000"], "(33750-5*(35250-33750)/2)+12*(35250-33750)/2",
  IS + " En interés simple el monto crece lo mismo cada mes.", "La diferencia de montos entre 7 y 5 meses es lo que producen 2 meses.",
  ["En 2 meses el monto aumenta 35250 − 33750 = 1500, así que cada mes produce 750.", "Capital: 33750 − 5 · 750 = 30000.", "Monto en 12 meses: 30000 + 12 · 750 = 39000."], "30000 + 7 · 750 = 35250.", ok="b", **LAB,
  mistake="Responder el capital, 30000, alternativa a, que no es el monto del año.", trap="30000", trap_opt="a")

F(8, "porc", 3, "Cuando un capital se presta durante 4 años el monto que se obtendría sería S/. 12000 pero si se prestará por 5 años sería S/. 13500. Hallar el valor de la tasa de interés.",
  ["10%", "15", "18", "20", "25"], ["10", "15", "18", "20", "25"], "(13500-12000)/(12000-4*(13500-12000))*100", IS, "La diferencia entre los montos es el interés de 1 año.",
  ["Interés de 1 año: 13500 − 12000 = 1500.", "Capital: 12000 − 4 · 1500 = 6000.", "Tasa: 1500/6000 = 0,25 = 25% anual."], "6000 · 0,25 · 4 = 6000 y 6000 + 6000 = 12000.", ok="e", **LAB,
  mistake="Dividir 1500 entre 12000 y obtener 12,5%.")

F(9, "porc", 3, "La cuarta parte de un capital se presto al 20% y el resto al 16%. Si en 8 meses se obtuvo un monto total de S/. 16700. ¿Cuánto es el capital?",
  ["S/. 15000", "14000", "13000", "12000", "N.A."], ["15000", "14000", "13000", "12000", "undef"], "solve(C*(1+(1/4*20/100+3/4*16/100)*(8/12))=16700,C)[0]",
  IS, "Cada parte tiene su propia tasa. Las tasas son anuales y el tiempo se pasa a años.",
  ["La cuarta parte es C/4 al 20% y las tres cuartas partes son 3C/4 al 16%.", "Tasa promedio anual: 0,25 · 20% + 0,75 · 16% = 5% + 12% = 17%.", "Interés en 8 meses, que son 2/3 de año: C · 0,17 · 2/3 = 0,11333C.", "Monto: 1,11333C = 16700, así que C = 15000."],
  "Con C = 15000: interés de 3750 · 0,2 · 2/3 = 500 y de 11250 · 0,16 · 2/3 = 1200. Total 15000 + 1700 = 16700.", ok="a", **LAB,
  note="Se entiende que las tasas son anuales.", mistake="Usar 8 como años. El interés sería enorme y el capital muy chico.")

F(10, "porc", 1, "Calcular el interés producido por S/. 3000 impuestos al 15% durante 3 años.",
  ["S/. 500", "600", "800", "1000", "N.A."], ["500", "600", "800", "1000", "1350"], "3000*15/100*3", IS, "I = C·r·t con tasa anual y 3 años.",
  ["I = 3000 · 0,15 · 3 = 1350.", "1350 no es ninguna de las alternativas a, b, c ni d, así que la respuesta es N.A."], "Cada año produce 450 y en 3 años 1350.", ok="e", **LAB,
  note="N.A. significa ninguna de las anteriores.", mistake="Marcar 1000 por ser el valor más cercano. El valor es 1350.")

F(11, "porc", 2, "Determinar el interés generado al depositar S/. 3600 al 9% trimestral durante 8 meses.",
  ["S/. 500", "624", "700", "764", "864"], ["500", "624", "700", "764", "864"], "3600*(9/100)/3*8", IS, "Se pasa la tasa trimestral a mensual: 9%/3 = 3% por mes.",
  ["Tasa mensual: 9%/3 = 3%.", "I = 3600 · 0,03 · 8 = 864."], "3600 · 0,03 = 108 por mes y 108 · 8 = 864.", ok="e", **LAB, mistake="Usar 8 como si fueran trimestres: 3600 · 0,09 · 8 = 2592.")

F(12, "porc", 1, "¿Cuál es el capital que se coloca al 30% durante 5 años, para obtener un interés de S/. 3000?",
  ["S/. 1000", "1200", "1500", "2000", "3500"], ["1000", "1200", "1500", "2000", "3500"], "solve(C*30/100*5=3000,C)[0]", IS, "Se despeja el capital de I = C·r·t.",
  ["3000 = C · 0,30 · 5 = 1,5C.", "C = 3000/1,5 = 2000."], "2000 · 0,30 · 5 = 3000.", ok="d", **LAB, mistake="Dividir entre 0,30 sin multiplicar por los 5 años: 10000, que no está.")

F(13, "porc", 2, "¿A qué tasa de interés la suma de 20000 llegará a un monto de 30000 colocado a un interés simple en un año y 6 meses?",
  ["10%", "20", "30", "40", "N.A."], ["10", "20", "30", "40", "100/3"], "(30000-20000)/(20000*(3/2))*100", IS, "El interés es 30000 − 20000 y el tiempo es 1,5 años.",
  ["Interés: 10000.", "10000 = 20000 · r · 1,5, así que r = 10000/30000 = 1/3.", "r = 33,33...% anual, que no es ninguna de las alternativas a, b, c ni d. La respuesta es N.A."], "20000 · 0,3333 · 1,5 = 10000.", ok="e", **LAB,
  note="La ficha pone las alternativas de este problema en la página siguiente.", mistake="Redondear a 30 y marcar la alternativa c. Con 30% el monto sería 29000, no 30000.")

F(14, "porc", 2, "¿Cuál es el monto producido por un capital de S/. 8000 colocados al 9% anual durante 4 años y 7 meses?",
  ["S/. 8000", "53000", "6000", "11000", "11300"], ["8000", "53000", "6000", "11000", "11300"], "8000+8000*(9/100)*(55/12)", IS, "4 años 7 meses son 55 meses, es decir 55/12 de año.",
  ["I = 8000 · 0,09 · 55/12 = 3300.", "M = 8000 + 3300 = 11300."], "El 9% anual de 8000 es 720 y 720 · 55/12 = 3300.", ok="e", **LAB, mistake="Responder el interés, 3300, que no está, o sumar mal el capital.")

F(15, "porc", 2, "¿Cuál es el capital que colocado al 9% quincenal genera en 2 meses un manto de S/. 7000?",
  ["S/. 10000", "15000", "18000", "20000", "30000"], ["10000", "15000", "18000", "20000", "30000"], "7000/(1+9/100*4)", IS, "2 meses son 4 quincenas. El monto es el capital más el interés.",
  ["En 4 quincenas el interés es 4 · 9% = 36% del capital.", "M = C · 1,36 = 7000, así que C = 7000/1,36 ≈ 5147,06.", "Ningún valor de las alternativas cumple eso."], "5147,06 · 1,36 = 7000.",
  answer="S/ 5147,06 aproximadamente, sin alternativa que lo recoja", answerVal="87500/17",
  warn="Con el enunciado tal cual, ninguna alternativa es correcta. La ficha dice manto, probablemente monto, y tiene una errata en los datos.",
  note="Si el interés fuera 7200, el capital sería 7200/0,36 = 20000, que es la alternativa d. Es solo una hipótesis y la academia debe confirmarlo.", review=True, **LAB,
  mistake="Marcar 20000 por parecerse a la respuesta de otro dato. Con 7000 el capital no es 20000.")

F(16, "porc", 1, "Calcular el interés producido por S/. 3680 que se han impuesto al 30% durante 5 años.",
  ["S/. 3680", "4000", "4680", "5000", "5520"], ["3680", "4000", "4680", "5000", "5520"], "3680*30/100*5", IS, "I = C·r·t con tasa anual y 5 años.",
  ["La tasa es anual y el tiempo está en años, así que se usan tal cual.", "I = 3680 · 0,30 · 5 = 5520."], "El 30% de 3680 es 1104 y 1104 · 5 = 5520.", ok="e", **LAB, mistake="Responder 3680, el capital.")

F(17, "porc", 2, "Se deposito S/. 720 a una tasa del 30% anual, produciendo un interés de S/. 36. ¿Cuánto tiempo estuvo el capital depositado?",
  ["1 mes", "2 meses", "3 meses", "4 meses", "5 meses"], ["1", "2", "3", "4", "5"], "36/(720*(30/100))*12", IS, "Se despeja el tiempo y se pasa a meses.",
  ["36 = 720 · 0,30 · t = 216t.", "t = 36/216 = 1/6 de año.", "1/6 de año son 2 meses."], "En 1 mes el interés es 720 · 0,30/12 = 18, y en 2 meses 36.", ok="b", **LAB, mistake="Dejar el tiempo como 1/6 y marcar 1 mes.")

F(18, "porc", 3, "Si a un capital se le suma los intereses producidos en 26 meses, se obtiene una cantidad, que es al capital prestado como 63 y 50. ¿A qué tasa fue colocada?",
  ["10%", "11", "11,5", "12", "12,5"], ["10", "11", "23/2", "12", "25/2"], "(63/50-1)/26*12*100", IS, "La razón monto a capital es 63 a 50.",
  ["M/C = 63/50 = 1,26, así que el interés es 0,26C.", "En 26 meses: 0,26C = C · r · 26/12, así que r = 0,26 · 12/26 = 0,12 = 12% anual."], "50 · 0,12 · 26/12 = 13 y 50 + 13 = 63.", ok="d", **LAB,
  note="Se entiende la tasa como anual.", mistake="No pasar los 26 meses a años y obtener 1% mensual sin convertir a anual.")

F(19, "porc", 2, "Después de cuánto tiempo un capital colocado al 20% de interés simple se triplica.",
  ["1 año", "2 años", "3 años", "5 años", "10 años"], ["1", "2", "3", "5", "10"], "(3-1)/(20/100)", IS, "Triplicarse significa que el monto es 3C, es decir que el interés es 2C.",
  ["Interés: 3C − C = 2C.", "2C = C · 0,20 · t, así que t = 2/0,20 = 10 años."], "Cada año produce 0,2C y en 10 años 2C.", ok="e", **LAB, mistake="Pensar que triplicar es ganar 3C y responder 15 años.")

F(20, "porc", 2, "¿En cuánto se convertirá S/. 84000 al 1,5% mensual durante 18 días?",
  ["S/. 84156", "84256", "84356", "84756", "84956"], ["84156", "84256", "84356", "84756", "84956"], "84000+84000*(15/1000)*(18/30)", IS, "Se usa el mes comercial de 30 días: 18 días son 0,6 de mes.",
  ["I = 84000 · 0,015 · 18/30 = 756.", "Monto: 84000 + 756 = 84756."], "El 1,5% mensual de 84000 es 1260 y 1260 · 0,6 = 756.", ok="d", **LAB, mistake="Responder solo el interés, 756, o usar 18 meses.")
