"""Figuras de la ficha de Estadística, semana 4: dos gráficos circulares y uno de barras."""
import math
from figs import fig
from figs_s1geo import onc
from figs_s1est import pie, legend

@fig("fe4_cerveza")
def _(f):
    cx, cy, r = 200, 210, 160
    parts = [(144, 2, "40%"), (72, 4, "20%"), (72, 3, "20%"), (36, 5, "10%"), (36, 1, "10%")]
    pie(f, cx, cy, r, [(d, c) for d, c, _ in parts])
    a = -90.0
    for d, c, lab in parts:
        m = onc((cx, cy), 0.64*r, a+d/2); f.t(m[0], m[1], lab, 6, 0.8); a += d
    f.t(200, 14, "NIVELES DE EXPORTACIÓN DE CERVEZA POR MARCA, 2017", 0, 0.62)
    legend(f, 420, 110, [(2, "Cristalina"), (4, "Pilsener"), (3, "Limeña"), (5, "Loretana"), (1, "Otros")], 36)

@fig("fe4_regiones")
def _(f):
    cx, cy, r = 200, 210, 160
    a = -135.0
    parts = [(90, 2, "25%", "Tacna"), (28.8, 4, "n%", "Moquegua"), (115.2, 3, "4n%", "Puno"), (126, 5, "35%", "Arequipa")]
    for deg, col, lab, nm in parts:
        f.ar(cx, cy, r, a, deg, col, True)
        p = onc((cx, cy), r, a); f.ln(cx, cy, p[0], p[1], 6, w=1.4)
        m = onc((cx, cy), 0.62*r, a+deg/2); f.t(m[0], m[1], lab, 6, 0.8)
        a += deg
    f.ci(cx, cy, r, 6)
    f.t(200, 14, "DISTRIBUCIÓN DE POSTULANTES A BECA 18 EN CUATRO REGIONES DEL SUR", 0, 0.58)
    legend(f, 420, 110, [(2, "Tacna"), (4, "Moquegua"), (3, "Puno"), (5, "Arequipa")], 36)

@fig("fe4_canete", 66)
def _(f):
    X = lambda i: 140 + i*140; Y = lambda v: 400 - v*3.6
    hom = [60, 64, 75]; muj = [62, 64, 90]
    for v in range(0, 101, 20): f.ln(80, Y(v), 520, Y(v), 5, False, 0.45); f.t(58, Y(v), str(v), 5, 0.65)
    f.ln(80, Y(0), 520, Y(0))
    for i in range(3):
        for vals, col, dx in ((hom, 5, -34), (muj, 3, 2)):
            x0 = X(i)+dx; v = vals[i]
            f.pg([(x0, Y(0)), (x0+32, Y(0)), (x0+32, Y(v)), (x0, Y(v))], col, True); f.pg([(x0, Y(0)), (x0+32, Y(0)), (x0+32, Y(v)), (x0, Y(v))])
            f.t(x0+16, Y(v)-14, str(v), 0, 0.7)
        f.t(X(i), Y(0)+18, str(2015+i), 0, 0.75)
    f.t(90, 70, "Miles de personas", 0, 0.65)
    legend(f, 560, 170, [(5, "Hombres"), (3, "Mujeres")], 34)
