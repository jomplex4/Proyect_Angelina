"""Figuras de la ficha de Estadística, semana 1: gráficos circulares, de barras y una pizza."""
import math
from figs import fig
from figs_s1geo import onc

def pie(f, cx, cy, r, parts):
    """parts: lista de (grados, color). Empieza arriba y avanza en el sentido de las agujas del reloj."""
    a = -90.0
    for deg, col in parts:
        f.ar(cx, cy, r, a, deg, col, True); a += deg
    a = -90.0
    for deg, col in parts:
        p = onc((cx, cy), r, a); f.ln(cx, cy, p[0], p[1], 6, w=1.4); a += deg
    f.ci(cx, cy, r, 6)

def legend(f, x, y, items, dy=34, size=0.7):
    """Leyenda con el texto a la derecha del cuadro de color. El texto se centra, así que se estima su ancho."""
    for k, (col, txt) in enumerate(items):
        yy = y + k*dy
        f.pg([(x, yy-8), (x+18, yy-8), (x+18, yy+8), (x, yy+8)], col, True)
        f.t(x + 30 + len(txt)*9.6*size/0.7/2, yy, txt, 0, size)

@fig("fe1_cafe")
def _(f):
    pie(f, 190, 200, 160, [(90, 2), (54, 5), (72, 4), (108, 3), (36, 1)])
    f.t(190+70, 200-70, "25%", 6, 0.8); f.t(190+105, 200+55, "15%", 6, 0.8); f.t(190+10, 200+110, "20%", 6, 0.8); f.t(190-95, 200-5, "30%", 6, 0.8); f.t(190-45, 200-120, "10%", 6, 0.8)
    f.t(190, 14, "DISTRIBUCIÓN DE VENTAS EN CAFETERÍA", 0, 0.62)
    legend(f, 400, 110, [(2, "Sándwiches (25%)"), (5, "Ensaladas (15%)"), (4, "Sopas (20%)"), (3, "Bebidas (30%)"), (1, "Postres (10%)")], 36)

@fig("fe1_cursos")
def _(f):
    pie(f, 190, 200, 160, [(90, 2), (54, 3), (36, 5), (72, 4), (108, 1)])
    f.t(190+70, 200-70, "90°", 6, 0.8); f.t(190+110, 200+30, "54°", 6, 0.8); f.t(190+60, 200+118, "36°", 6, 0.8); f.t(190-60, 200+98, "72°", 6, 0.8); f.t(190-75, 200-55, "a°", 6, 0.95)
    f.t(190, 14, "PREFERENCIAS DE ÁREAS CURRICULARES DE UNA I.E.", 0, 0.62)
    legend(f, 400, 110, [(2, "Matemática: 90°"), (3, "CTA: 54°"), (5, "Historia: 36°"), (4, "Comunicación: 72°"), (1, "Otros: a°")], 36)

@fig("fe1_pizza")
def _(f):
    cx, cy, r = 250, 210, 170
    f.disc(cx, cy, r, 7); f.ci(cx, cy, r, 4); f.ci(cx, cy, r-14, 5)
    for (dx, dy) in ((-60, -80), (50, -90), (90, 10), (-100, 30), (-10, 90), (30, 40), (-40, -20), (110, -50)):
        f.disc(cx+dx, cy+dy, 13, 1)
    a0 = -140
    f.ar(cx, cy, r+2, a0, 70, 6, True)
    p1, p2 = onc((cx, cy), r, a0), onc((cx, cy), r, a0+70)
    f.ln(cx, cy, *p1); f.ln(cx, cy, *p2); f.ang((cx, cy), p1, p2, 52, 2)
    m = onc((cx, cy), 82, a0+35); f.t(m[0], m[1], "x", 0, 1.0)

@fig("fe1_deportes", 66)
def _(f):
    X = lambda i: 120 + i*95; Y = lambda v: 330 - v*28
    vals = [("Atletismo", 4), ("Ciclismo", 8), ("Básquet", 6), ("Natación", 2)]
    for v in range(0, 9, 2): f.ln(70, Y(v), 480, Y(v), 5, v > 0, 0.5); f.t(52, Y(v), str(v), 5, 0.65)
    f.ln(70, Y(0), 480, Y(0))
    for i, (n, v) in enumerate(vals):
        f.pg([(X(i)-28, Y(0)), (X(i)+28, Y(0)), (X(i)+28, Y(v)), (X(i)-28, Y(v))], 2, True); f.pg([(X(i)-28, Y(0)), (X(i)+28, Y(0)), (X(i)+28, Y(v)), (X(i)-28, Y(v))])
        f.t(X(i), Y(0)+18, n, 0, 0.7)
    f.t(275, 80, "PREFERENCIA DE DISCIPLINAS DEPORTIVAS", 0, 0.7)

@fig("fe1_ventas", 70)
def _(f):
    mes = ["ene", "feb", "mar", "abril", "may", "jun"]
    muj = [70, 60, 50, 40, 30, 40]; var = [60, 40, 60, 70, 50, 30]
    X = lambda i: 110 + i*80; Y = lambda v: 400 - v*4.2
    for v in range(0, 81, 10): f.ln(70, Y(v), 570, Y(v), 5, False, 0.45); f.t(50, Y(v), str(v), 5, 0.65)
    f.ln(70, Y(0), 570, Y(0))
    for i in range(6):
        for vals, col, dx in ((muj, 3, -26), (var, 5, 2)):
            x0 = X(i)+dx; v = vals[i]
            f.pg([(x0, Y(0)), (x0+24, Y(0)), (x0+24, Y(v)), (x0, Y(v))], col, True); f.pg([(x0, Y(0)), (x0+24, Y(0)), (x0+24, Y(v)), (x0, Y(v))])
        f.t(X(i), Y(0)+18, mes[i], 0, 0.75)
    legend(f, 600, 200, [(3, "Mujeres"), (5, "Varones")], 34)
