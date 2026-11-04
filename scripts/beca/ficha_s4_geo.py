"""Ficha de Geometría, semana 4, de Nick Arias: razones trigonométricas y sólidos geométricos. 30 preguntas."""
from model import ficha
F = ficha(4, "geo")
U3 = lambda xs: ["%s u³" % x for x in xs]
U2 = lambda xs: ["%s u²" % x for x in xs]
PI = lambda xs: [x.replace("π", "*pi") if x != "π" else "pi" for x in xs]
RT = "En un triángulo rectángulo: sen = opuesto/hipotenusa, cos = adyacente/hipotenusa, tan = opuesto/adyacente; csc, sec y cot son sus inversas."

F(1, "trig", 1, "Si para un ángulo agudo α se cumple que cscα = 3, efectúe\nE = √2 cotα + 9 senα",
  ["2", "5", "7", "4", "3"], ["2", "5", "7", "4", "3"], "sqrt(2)*sqrt(3^2-1)+9/3",
  RT, "csc α = 3/1 da hipotenusa 3 y cateto opuesto 1. El otro cateto sale por Pitágoras.",
  ["csc α = 3 = hipotenusa/opuesto: hipotenusa 3 y cateto opuesto 1.",
   "Cateto adyacente: √(9 − 1) = √8 = 2√2.",
   "cot α = 2√2/1 = 2√2 y sen α = 1/3.",
   "E = √2·2√2 + 9·(1/3) = 4 + 3 = 7."],
  "sen α = 1/3 y csc α = 3 son inversos. √2·2√2 = 2·2 = 4.",
  ok="C", mistake="Tomar cot α = 1/(2√2), es decir invertir la razón. Así E no da ninguna alternativa.")

F(2, "trig", 1, "Si un ángulo φ cumple que cotφ = 1,5, efectúe\nP = √13 secφ + tanφ",
  ["3", "5", "§13/3", "13", "8"], ["3", "5", "13/3", "13", "8"], "sqrt(13)*sqrt(13)/3+2/3",
  RT, "1,5 = 3/2: cateto adyacente 3 y opuesto 2. La hipotenusa es √13.",
  ["cot φ = 3/2: adyacente 3 y opuesto 2.",
   "Hipotenusa: √(9 + 4) = √13.",
   "sec φ = √13/3 y tan φ = 2/3.",
   "P = √13·√13/3 + 2/3 = 13/3 + 2/3 = 15/3 = 5."],
  "tan φ·cot φ = (2/3)·(3/2) = 1, como debe ser.",
  ok="B", trap="sqrt(13)*sqrt(13)/3", trap_opt="C", mistake="Olvidar sumar tan φ y quedarse con 13/3, alternativa C.")

F(3, "trig", 1, "Reduzca la siguiente expresión:\nE = (4sen30° + √3 tan60° − cot45°) / (cos²30° · cot37°)",
  ["§sqrt(2)", "§sqrt(3)", "2", "3", "4"], ["sqrt(2)", "sqrt(3)", "2", "3", "4"], "(4*1/2+sqrt(3)*sqrt(3)-1)/((sqrt(3)/2)^2*4/3)",
  "Valores notables: sen 30° = 1/2, tan 60° = √3, cot 45° = 1, cos 30° = √3/2 y cot 37° = 4/3.",
  "Se reemplaza cada razón por su valor.",
  ["Numerador: 4·(1/2) + √3·√3 − 1 = 2 + 3 − 1 = 4.",
   "Denominador: (√3/2)²·(4/3) = (3/4)·(4/3) = 1.",
   "E = 4."],
  "Es la misma expresión de la pregunta 21 de la semana 1, y da lo mismo.",
  ok="E", mistake="Usar cot 37° = 3/4. Entonces el denominador sería 9/16 y E no daría una alternativa.")

F(4, "trig", 2, "Si la hipotenusa de un triángulo rectángulo mide 51 m y la tangente de uno de los ángulos agudos del triángulo es 8/15, luego el perímetro y el área de dicho triángulo son",
  ["100 m y 380 m².", "80 m y 120 m².", "160 m y 480 m².", "60 m y 180 m².", "120 m y 540 m²."],
  ["[100,380]", "[80,120]", "[160,480]", "[60,180]", "[120,540]"], "[(8+15+17)*51/17,8*15*(51/17)^2/2]",
  "Si tan = 8/15, los catetos son 8k y 15k y la hipotenusa 17k, por el triángulo 8, 15, 17.",
  "Con la hipotenusa 51 se halla k.",
  ["Catetos 8k y 15k; hipotenusa √(64k² + 225k²) = 17k.",
   "17k = 51, así que k = 3. Catetos 24 y 45.",
   "Perímetro: 24 + 45 + 51 = 120 m. Área: 24·45/2 = 540 m²."],
  "24² + 45² = 576 + 2025 = 2601 = 51².",
  ok="E", mistake="Tomar k = 1 y calcular con 8, 15 y 17. Ese triángulo tiene perímetro 40 y área 60.")

F(5, "trig", 2, "Del gráfico, halle el valor de x si tanα = 1/3 y cotβ = 5/3.",
  ["1", "2", "3", "4", "5"], [], "",
  RT, "Es la misma figura de la pregunta 18 de la semana 1: ninguna longitud está dada.",
  ["Sea h el lado vertical. tan α = x/h = 1/3, así que x = h/3.",
   "cot β = base/h = 5/3: la base completa mide 5h/3.",
   "El tramo de la izquierda mide 5h/3 − h/3 = 4h/3 = 4x.",
   "Sin una longitud dada, x no queda determinado."],
  "Con h = 3, x = 1; con h = 6, x = 2. Ambos cumplen los datos.",
  answer="No se puede determinar con los datos dados. Solo se sabe que el tramo izquierdo de la base mide 4x.",
  warn="A la ficha le falta un dato de longitud: con los datos dados, x no queda determinado.", review=True, fig="f1g_18",
  mistake="Suponer una altura que no está en el enunciado.")

F(6, "trig", 3, "Del gráfico, halle el valor x.",
  ["13", "12", "15", "20", "25"], ["13", "12", "15", "20", "25"], "sqrt(15^2+5^2)*(3/5*15/sqrt(250)+4/5*5/sqrt(250))",
  RT + " cos(a − b) = cos a·cos b + sen a·sen b.",
  "Se traza la diagonal AC. El ángulo BAC es 53° menos el ángulo CAD.",
  ["AC = √(15² + 5²) = √250 = 5√10. En el triángulo ACD: cos θ = 15/(5√10) = 3/√10 y sen θ = 1/√10, con θ = ∠CAD.",
   "∠BAC = 53° − θ.",
   "cos(53° − θ) = (3/5)(3/√10) + (4/5)(1/√10) = 13/(5√10).",
   "El triángulo ABC es recto en B: x = AB = AC·cos ∠BAC = 5√10·13/(5√10) = 13."],
  "Con A en el origen, B queda en (7,8; 10,4) y C en (15; 5). AB = √(7,8² + 10,4²) = 13 y BA·BC = 0, ángulo recto en B.",
  ok="A", fig="f4g_06", mistake="Pensar que AB es la hipotenusa de un triángulo de 53° con cateto 15 y responder 25.")

F(7, "trig", 3, "En la figura, si AD = 2DC, calcule tanθ.",
  ["§1/3", "§sqrt(3)/5", "§sqrt(3)/2", "5", "§sqrt(3)"], ["1/3", "sqrt(3)/5", "sqrt(3)/2", "5", "sqrt(3)"],
  "(1/sqrt(3)-1/(3*sqrt(3)))/(1+1/sqrt(3)*1/(3*sqrt(3)))",
  RT + " tan(a − b) = (tan a − tan b)/(1 + tan a·tan b).",
  "θ es la diferencia entre el ángulo ABC y el ángulo DBC.",
  ["Sea DC = 1, AD = 2 y AC = 3. Con ∠A = 60°: BC = AC·tan 60° = 3√3.",
   "∠ABC = 30°, así que tan ∠ABC = 1/√3.",
   "En el triángulo DBC: tan ∠DBC = DC/BC = 1/(3√3).",
   "θ = ∠ABC − ∠DBC: tan θ = (1/√3 − 1/(3√3))/(1 + 1/9) = (2/(3√3))·(9/10) = 3/(5√3) = √3/5."],
  "Con coordenadas A = (0; 0), D = (2; 0) y B = (3; 3√3): BA = (−3; −3√3) y BD = (−1; −3√3). El seno y el coseno del ángulo entre ellos dan tan θ = √3/5.",
  ok="B", fig="f4g_07", mistake="Restar las tangentes directamente: 1/√3 − 1/(3√3) = 2/(3√3). Hay que usar la fórmula de la diferencia.")

F(8, "trig", 2, "Si ABCD es cuadrado, calcule csc²α.",
  ["14", "15", "16", "17", "18"], ["14", "15", "16", "17", "18"], "1+(1/(1-3/4))^2",
  "csc²α = 1 + cot²α. Triángulo notable de 37° y 53°.",
  "Es la misma figura de la pregunta 22 de la semana 1.",
  ["En el triángulo ABE, recto en E: ∠BAE = 53°, así que ∠FAD = 37°.",
   "Con lado 4: DF = 4·tan 37° = 3 y CF = 1.",
   "cot α = BC/CF = 4, así que csc²α = 1 + 16 = 17."],
  "BF = √17 y csc α = √17/1.",
  ok="D", fig="f1g_22", trap="(1/(1-3/4))^2", trap_opt="C", mistake="Quedarse con cot²α = 16, alternativa C.")

F(9, "trig", 3, "Del gráfico, calcule 5senα·cscβ.",
  ["§5*sqrt(2)", "§3*sqrt(2)", "§sqrt(2)", "§4*sqrt(2)", "§2*sqrt(2)"], ["5*sqrt(2)", "3*sqrt(2)", "sqrt(2)", "4*sqrt(2)", "2*sqrt(2)"], "5*(4/5)/(sqrt(2)/2)",
  "Ley de senos: en todo triángulo, cada lado entre el seno del ángulo opuesto da lo mismo.",
  "D es punto medio de AC y BD es común a los dos triángulos. La ley de senos los relaciona.",
  ["En el triángulo ABD: AD/sen α = BD/sen 53°, así que sen α = AD·sen 53°/BD.",
   "En el triángulo DBC: DC/sen β = BD/sen 45°, así que sen β = DC·sen 45°/BD.",
   "Como AD = DC: sen α/sen β = sen 53°/sen 45° = (4/5)/(√2/2) = 8/(5√2).",
   "5 sen α·csc β = 5·8/(5√2) = 8/√2 = 4√2."],
  "4√2 ≈ 5,66, y sen α/sen β ≈ 1,13: α es un poco mayor que β, como en la figura.",
  ok="D", fig="f4g_09", mistake="Usar los ángulos 53° y 45° como si fueran α y β.")

F(10, "trig", 3, "En un triángulo obtusángulo ABC, se cumple que AB = 3, BC = 2√2 y m∢ABC = 135°. Calcule secA · cscA.",
  ["2,5", "2,6", "2,7", "2,8", "2,9"], ["25/10", "26/10", "27/10", "28/10", "29/10"],
  "1/((2*sqrt(2)*sqrt(2)/2/sqrt(29))*((9+29-8)/(6*sqrt(29))))",
  "Ley de cosenos: AC² = AB² + BC² − 2·AB·BC·cos B. Ley de senos para sen A. cos 135° = −√2/2.",
  "Primero se halla AC; luego sen A y cos A.",
  ["AC² = 9 + 8 − 2·3·2√2·(−√2/2) = 17 + 12 = 29, así que AC = √29.",
   "Ley de senos: sen A = BC·sen B/AC = 2√2·(√2/2)/√29 = 2/√29.",
   "cos A = √(1 − 4/29) = 5/√29.",
   "sec A·csc A = (√29/5)(√29/2) = 29/10 = 2,9."],
  "Con cosenos: cos A = (9 + 29 − 8)/(2·3·√29) = 30/(6√29) = 5/√29, igual que antes.",
  ok="E", mistake="Usar cos 135° = +√2/2. Entonces AC² = 5 y el resultado no coincide.")

F(11, "geo", 2, "Calcule el volumen del prisma regular mostrado.",
  U3(["32", "28", "36", "24", "20"]), ["32", "28", "36", "24", "20"], "(5*4/5)^2/2*(5*3/5)",
  "Un prisma regular cuadrangular tiene base cuadrada. Su diagonal forma un triángulo rectángulo con la arista lateral y la diagonal de la base. Volumen = área de la base × altura.",
  "La diagonal 5 forma 53° con la arista vertical: es el triángulo 3, 4, 5.",
  ["Altura: 5·cos 53° = 5·(3/5) = 3.",
   "Diagonal de la base: 5·sen 53° = 4.",
   "La base es un cuadrado de diagonal 4: área 4²/2 = 8.",
   "Volumen: 8·3 = 24 u³."],
  "Lado de la base 2√2: diagonal del prisma √(8 + 8 + 9) = √25 = 5.",
  ok="D", fig="f4g_11", mistake="Tomar 4 como lado de la base en lugar de su diagonal. Así sale 48, que no está.")

F(12, "geo", 1, "Calcule el volumen de un prisma recto de altura 6 si su base está limitada por un triángulo rectángulo de hipotenusa 5 y cateto 4.",
  U3(["36", "45", "32", "40", "54"]), ["36", "45", "32", "40", "54"], "3*4/2*6",
  "Volumen del prisma = área de la base × altura.", "El otro cateto es 3, por el triángulo 3, 4, 5.",
  ["Cateto que falta: √(25 − 16) = 3.", "Área de la base: 3·4/2 = 6.", "Volumen: 6·6 = 36 u³."],
  "36 = 3·4·6/2.", ok="A", mistake="Olvidar dividir entre 2 el área del triángulo: 72, que no está.")

F(13, "geo", 3, "La base de un prisma es una región triangular de lados 2; 3 y 4, y se encuentra inscrita en una circunferencia de radio R. Si la altura del prisma mide 5R, calcule su volumen.",
  U3(["36", "40", "24", "20", "30"]), ["36", "40", "24", "20", "30"], "5*2*3*4/4",
  "El área de un triángulo de lados a, b y c con circunradio R es abc/(4R).",
  "No hace falta calcular R: al multiplicar el área por 5R, la R se cancela.",
  ["Área de la base: S = abc/(4R) = 2·3·4/(4R) = 6/R.",
   "Volumen: S·h = (6/R)·5R = 30 u³."],
  "Con Herón: S = √(4,5·2,5·1,5·0,5) = 3√15/4 y R = 24/(3√15) = 8/√15. S·5R = (3√15/4)·(40/√15) = 30.",
  ok="E", mistake="Intentar hallar R primero y cometer un error de cálculo. La fórmula abc/(4R) lo evita.")

F(14, "geo", 1, "Las dimensiones de un rectoedro miden x, (x+1), y (x+2). Halle el valor de x si su diagonal mide √77.",
  ["2", "3", "4", "5", "6"], ["2", "3", "4", "5", "6"], "max(solve(t^2+(t+1)^2+(t+2)^2=77,t))",
  "La diagonal de un rectoedro de dimensiones a, b y c es √(a² + b² + c²).", "Se iguala la suma de cuadrados a 77.",
  ["x² + (x + 1)² + (x + 2)² = 77.", "3x² + 6x + 5 = 77, es decir x² + 2x − 24 = 0.", "(x + 6)(x − 4) = 0, así que x = 4."],
  "16 + 25 + 36 = 77.", ok="C", mistake="Olvidar los dobles productos al desarrollar los cuadrados.")

F(15, "geo", 2, "Calcule el volumen de un prisma triangular regular de altura 4√3 si el desarrollo de su superficie lateral es una región rectangular de diagonal 2√21.",
  U3(["18", "12", "15", "24", "30"]), ["18", "12", "15", "24", "30"], "sqrt(3)/4*(sqrt(84-48)/3)^2*4*sqrt(3)",
  "El desarrollo lateral de un prisma es un rectángulo de base igual al perímetro de la base y altura igual a la del prisma. El área de un triángulo equilátero de lado a es a²√3/4.",
  "Con la diagonal del rectángulo se obtiene el perímetro de la base.",
  ["El rectángulo tiene lados 3a y 4√3: (3a)² + 48 = (2√21)² = 84.",
   "9a² = 36, así que a = 2.",
   "Área de la base: 4√3/4 = √3. Volumen: √3·4√3 = 12 u³."],
  "Perímetro 6 y altura 4√3: diagonal √(36 + 48) = √84 = 2√21.",
  ok="B", mistake="Tomar a como el ancho del rectángulo. El ancho es el perímetro 3a.")

F(16, "geo", 3, "Calcule el área de la superficie lateral del cilindro circular recto.",
  U2(["8π", "6π", "4π", "9π", "5π"]), PI(["8π", "6π", "4π", "9π", "5π"]), "pi*4*cos(pi/12)*(sqrt(6)-sqrt(2))",
  "Área lateral del cilindro: 2πr·h. sen 15° = (√6 − √2)/4 y cos 15° = (√6 + √2)/4.",
  "Las diagonales de la sección axial forman 30° frente a la altura. Cada mitad de diagonal y la altura forman un triángulo isósceles.",
  ["La sección axial es un rectángulo de altura h = √6 − √2 y base 2r. Sus diagonales miden d y se cortan formando 30° frente a la altura.",
   "En el triángulo isósceles formado por dos mitades de diagonal y la altura: h = d·sen 15°, así que d = (√6 − √2)/((√6 − √2)/4) = 4.",
   "Base: 2r = d·cos 15° = √6 + √2.",
   "Área lateral: π·2r·h = π(√6 + √2)(√6 − √2) = π(6 − 2) = 4π u²."],
  "d² = h² + (2r)² = (8 − 4√3) + (8 + 4√3) = 16, así que d = 4.",
  ok="C", fig="f4g_16", mistake="Tomar el ángulo de 30° como el que forma la diagonal con la base.")

F(17, "geo", 1, "Halle la longitud del radio de la base de un cilindro circular recto si su volumen es numéricamente igual al área de su superficie lateral.",
  ["4", "6", "8", "2", "3"], ["4", "6", "8", "2", "3"], "2*pi*h/(pi*h)",
  "Volumen: πr²h. Área lateral: 2πrh.", "Se igualan y se simplifica πrh.",
  ["πr²h = 2πrh.", "Dividimos entre πrh: r = 2."], "Con r = 2: πr²h = 4πh y 2πrh = 4πh.",
  ok="D", mistake="Igualar con el área total en lugar de la lateral.")

F(18, "geo", 1, "Calcule el volumen de un cilindro inscrito en un cubo de arista 6.",
  U3(["54π", "36π", "45π", "48π", "60π"]), PI(["54π", "36π", "45π", "48π", "60π"]), "pi*3^2*6",
  "Un cilindro inscrito en un cubo tiene diámetro y altura iguales a la arista.", "Radio 3 y altura 6.",
  ["Radio: 6/2 = 3. Altura: 6.", "Volumen: π·9·6 = 54π u³."], "54π ≈ 169,6, menor que el cubo, 216.",
  ok="A", mistake="Usar 6 como radio: 216π, que no está.")

F(19, "geo", 2, "Calcule volumen del cilindro circular recto mostrado.",
  U3(["15π", "12π", "9π", "8π", "6π"]), PI(["15π", "12π", "9π", "8π", "6π"]), "pi*(4*sin(pi/3)/2)^2*4*cos(pi/3)",
  "La diagonal de la sección axial forma un triángulo rectángulo con el diámetro y la altura. Triángulo de 30° y 60°.",
  "El 60° está entre la diagonal y la generatriz vertical.",
  ["Altura: 4·cos 60° = 2.", "Diámetro: 4·sen 60° = 2√3, así que r = √3.", "Volumen: π·3·2 = 6π u³."],
  "(2√3)² + 2² = 12 + 4 = 16 = 4².",
  ok="E", fig="f4g_19", mistake="Medir el 60° con la base: altura 2√3 y radio 1, volumen 2√3π, que no está.")

F(20, "geo", 2, "Calcule el área de la superficie lateral del cilindro circular recto si O y O' son centros y S es igual a 12 u².",
  U2(["12π", "18π", "20π", "24π", "36π"]), PI(["12π", "18π", "20π", "24π", "36π"]), "2*pi*12",
  "El área de un rombo es el producto de sus diagonales entre 2. Área lateral del cilindro: 2πrh.",
  "El cuadrilátero S tiene como diagonales la altura h y el diámetro 2r.",
  ["Las diagonales de S son OO' = h y el segmento horizontal = 2r.", "S = h·2r/2 = rh = 12.", "Área lateral: 2πrh = 2π·12 = 24π u²."],
  "Si r = 2 y h = 6: S = 12 y el área lateral es 2π·2·6 = 24π.",
  ok="D", fig="f4g_20", trap="pi*12", trap_opt="A", mistake="Olvidar el 2 de la fórmula y responder 12π, alternativa A.")

F(21, "geo", 1, "Calcule el volumen de una pirámide regular cuadrangular, cuya apotema mide 5 y la arista básica 8.",
  U3(["32", "64", "56", "24", "30"]), ["32", "64", "56", "24", "30"], "8^2*sqrt(5^2-4^2)/3",
  "Volumen de la pirámide: área de la base × altura / 3. La apotema, la altura y la mitad de la arista básica forman un triángulo rectángulo.",
  "Con la apotema 5 y la mitad de la arista 4 sale la altura 3.",
  ["Altura: √(5² − 4²) = 3.", "Base: 8² = 64.", "Volumen: 64·3/3 = 64 u³."],
  "Triángulo 3, 4, 5.", ok="B", mistake="Usar la apotema 5 como altura: 64·5/3, que no está.")

F(22, "geo", 2, "Calcule el volumen de una pirámide regular cuadrangular, cuya altura mide 3 y la arista lateral 4.",
  U3(["16", "18", "15", "14", "36"]), ["16", "18", "15", "14", "36"], "(2*(4^2-3^2))*3/3",
  "La arista lateral, la altura y la mitad de la diagonal de la base forman un triángulo rectángulo. Un cuadrado de diagonal d tiene área d²/2.",
  "Con la arista y la altura se obtiene la semidiagonal de la base.",
  ["Semidiagonal: √(16 − 9) = √7. Diagonal: 2√7.", "Área de la base: (2√7)²/2 = 14.", "Volumen: 14·3/3 = 14 u³."],
  "Lado de la base √14: su diagonal es √14·√2 = √28 = 2√7.", ok="D", mistake="Tomar √7 como la mitad del lado en lugar de la mitad de la diagonal: 28, que no está.")

F(23, "geo", 2, "El área total de una pirámide regular cuadrangular es 96 y su apotema 5. Halle la longitud de la arista básica.",
  ["2", "3", "4", "5", "6"], ["2", "3", "4", "5", "6"], "max(solve(a^2+2*a*5=96,a))",
  "Área total = área de la base + área lateral. Área lateral = semiperímetro de la base × apotema.",
  "Con arista a: base a² y lateral 4·a·5/2.",
  ["Área lateral: (4a/2)·5 = 10a. Base: a².", "a² + 10a = 96, es decir a² + 10a − 96 = 0.", "(a + 16)(a − 6) = 0, así que a = 6."],
  "36 + 60 = 96.", ok="E", mistake="Olvidar el área de la base y resolver 10a = 96.")

F(24, "geo", 2, "Calcule el volumen de una pirámide regular cuadrangular cuya arista lateral mide 5 y forma 53° con la base.",
  U3(["24", "25", "26", "27", "28"]), ["24", "25", "26", "27", "28"], "(2*5*3/5)^2/2*(5*4/5)/3",
  "Triángulo notable de 37° y 53°. Un cuadrado de diagonal d tiene área d²/2.",
  "La arista, la altura y la semidiagonal forman el triángulo 3, 4, 5.",
  ["Altura: 5·sen 53° = 4. Semidiagonal: 5·cos 53° = 3.", "Diagonal de la base 6 y área 36/2 = 18.", "Volumen: 18·4/3 = 24 u³."],
  "3² + 4² = 5².", ok="A", mistake="Usar 6 como lado del cuadrado: 36·4/3 = 48, que no está.")

F(25, "geo", 3, "Calcule el volumen de una pirámide regular triangular, cuya arista lateral mide 13 y arista básica 5√3.",
  U3(["75√3", "69√3", "80√3", "60√3", "70√3"]), ["75*sqrt(3)", "69*sqrt(3)", "80*sqrt(3)", "60*sqrt(3)", "70*sqrt(3)"],
  "sqrt(3)/4*75*sqrt(169-25)/3",
  "En una pirámide regular triangular, la altura cae en el centro de la base; ese centro está a a/√3 de cada vértice. Área del triángulo equilátero: a²√3/4.",
  "La arista lateral, la altura y la distancia del centro al vértice forman un triángulo rectángulo.",
  ["Distancia del centro a un vértice: 5√3/√3 = 5.", "Altura: √(169 − 25) = 12.", "Área de la base: 75√3/4.", "Volumen: (75√3/4)·12/3 = 75√3 u³."],
  "Triángulo 5, 12, 13.", ok="A", mistake="Usar la mitad del lado como distancia al centro.")

F(26, "geo", 1, "Calcule el volumen de un cono circular recto, cuya altura mide 6 y generatriz 7.",
  U3(["24π", "25π", "26π", "27π", "30π"]), PI(["24π", "25π", "26π", "27π", "30π"]), "pi*(49-36)*6/3",
  "Volumen del cono: πr²h/3. La generatriz, la altura y el radio forman un triángulo rectángulo.",
  "r² = 7² − 6² = 13, sin necesidad de hallar r.",
  ["r² = 49 − 36 = 13.", "Volumen: π·13·6/3 = 26π u³."], "26π = π·13·2.", ok="C", mistake="Olvidar dividir entre 3: 78π, que no está.")

F(27, "geo", 2, "Si el volumen del cono circular recto es 96π, halle el valor de r.",
  ["4", "5", "7", "6", "8"], ["4", "5", "7", "6", "8"], "3*surd(96/12,3)",
  "Volumen del cono: πr²h/3. Triángulo notable de 37° y 53°: tan 37° = 3/4.",
  "El ángulo de 74° en el vértice se divide en dos de 37°: r/h = 3/4.",
  ["r = 3k y h = 4k.", "π·9k²·4k/3 = 12πk³ = 96π, así que k³ = 8 y k = 2.", "r = 6."],
  "r = 6, h = 8: π·36·8/3 = 96π.", ok="D", fig="f4g_27", trap="4*2", trap_opt="E", mistake="Responder la altura, 8, alternativa E.")

F(28, "geo", 1, "Calcule el área de la superficie lateral de un cono circular recto cuyo desarrollo de su superficie lateral de un sector circular de radio 6 y ángulo de 300°.",
  U2(["30π", "61π", "62π", "63π", "64π"]), PI(["30π", "61π", "62π", "63π", "64π"]), "pi*6^2*300/360",
  "El desarrollo lateral del cono es un sector circular. Área del sector: πR²·(ángulo/360°).",
  "El área lateral es el área del sector.",
  ["Al desarrollar la superficie lateral se obtiene el sector, así que ambas áreas son iguales.", "Área del sector: π·6²·300°/360° = 36π·5/6 = 30π u²."], "300/360 = 5/6 y 36·5/6 = 30.", ok="A",
  note="El enunciado de la ficha dice cuyo desarrollo de su superficie lateral de un sector; se entiende que el desarrollo es un sector.",
  mistake="Usar el área del círculo completo, 36π, que no está.")

F(29, "geo", 2, "El área de la superficie lateral del cono circular recto es 80π. Halle el valor de r.",
  ["6", "7", "8", "9", "10"], ["6", "7", "8", "9", "10"], "4/5*sqrt(80*5/4)",
  "Área lateral del cono: πrg. Triángulo notable de 37° y 53°.",
  "El ángulo de 106° se divide en dos de 53°: r = g·sen 53° = 4g/5.",
  ["r = 4g/5.", "π·(4g/5)·g = 80π, así que g² = 100 y g = 10.", "r = 8."],
  "π·8·10 = 80π.", ok="C", fig="f4g_29", trap="sqrt(80*5/4)", trap_opt="E", mistake="Responder la generatriz, 10, alternativa E.")

F(30, "geo", 3, "Si los conos son equivalentes, halle el valor de x.",
  ["4", "5", "6", "7", "8"], ["4", "5", "6", "7", "8"], "6*sqrt(2)*sqrt(2)/3",
  "Sólidos equivalentes tienen el mismo volumen. Volumen del cono: πr²h/3.",
  "En el primer cono el ángulo en el vértice es recto: radio y altura miden x/√2.",
  ["Primer cono: generatrices x con ángulo recto, así que r = h = x/√2. V₁ = π(x²/2)(x/√2)/3.",
   "Segundo cono: radio x y altura √2. V₂ = πx²√2/3.",
   "Igualamos: x³/(2√2) = x²√2, así que x = 2√2·√2 = 4."],
  "Con x = 4: V₁ = π·8·2√2/3 y V₂ = π·16·√2/3. Ambos valen 16√2π/3.",
  ok="A", fig="f4g_30", mistake="Tomar x como el radio del primer cono. x es su generatriz.")
