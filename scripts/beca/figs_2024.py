"""Figuras redibujadas del examen 2024. Coordenadas tomadas de la página; las gráficas usan sus propios datos."""
import math
from figs import fig
from curva_e24_15 import PTS as CURVA15

@fig("x24_05")
def _(f):
    f.pg([(195, 183), (316, 183), (316, 300), (195, 300)]); f.ln(256, 183, 256, 300, 0, True)
    for x0 in (359, 430):
        f.pg([(x0, 183), (x0+61, 183), (x0+61, 300), (x0, 300)]); f.ln(x0, 241, x0+61, 241, 0, True)
    for x0, y0 in ((529, 178), (599, 178), (529, 246), (599, 246)):
        f.pg([(x0, y0), (x0+61, y0), (x0+61, y0+58), (x0, y0+58)]); f.ln(x0+30, y0, x0+30, y0+58, 0, True)
    f.t(256, 322, "Acción 1"); f.t(425, 322, "Acción 2"); f.t(595, 322, "Acción 3"); f.t(763, 243, "…"); f.t(770, 322, "…")

@fig("x24_06")
def _(f):
    top = [(398, 676), (452, 661), (515, 686), (462, 703)]
    f.pg(top, 7, True); f.pg(top)
    for x, y in ((404, 680), (432, 699), (458, 703), (508, 688)): f.ln(x, y, x, y+48, 0, w=1.6)
    f.ln(448, 676, 448, 649); f.ln(448, 649, 524, 649); f.arrow((448, 649), (524, 649)); f.t(484, 633, "Tablero")
    tv = [(533, 622), (652, 622), (628, 680), (557, 680)]
    f.pg(tv, 7, True); f.pg(tv); f.t(592, 692, "Vista de arriba")

@fig("x24_11")
def _(f):
    cx, rx, ry, top, lvl, bot = 491, 81, 12, 240, 313, 435
    f.pg(f.arc_pts(cx, lvl, rx, ry, 0, 180)[::-1] + [(cx-rx, lvl)] + f.arc_pts(cx, bot, rx, ry, 180, 0)[::-1][::-1], 7, True)
    f.pg([(cx-rx, lvl), (cx+rx, lvl), (cx+rx, bot), (cx-rx, bot)], 7, True)
    f.el(cx, top, rx, ry); f.ln(cx-rx, top, cx-rx, bot); f.ln(cx+rx, top, cx+rx, bot)
    f.pl(f.arc_pts(cx, bot, rx, ry, 0, 180)); f.pl(f.arc_pts(cx, lvl, rx, ry, 180, 360), 5, True); f.pl(f.arc_pts(cx, lvl, rx, ry, 0, 180), 5)
    f.t(cx, 288, "agua (400 ml)", 0, 0.8); f.t(cx, 388, "alcohol (1 000 ml)", 0, 0.8)
    f.ln(580, 322, 630, 322); f.arrow((580, 322), (630, 322)); f.t(690, 322, "nivel de alcohol", 0, 0.8)

@fig("x24_15", 66)
def _(f):
    X = lambda d: 60 + d*20; Y = lambda T: 400 - T*10
    for T in range(0, 36, 5):
        f.ln(X(0), Y(T), X(30), Y(T), 5, T > 0, 0.6); f.t(X(0)-14, Y(T), str(T), 5, 0.75)
    for d in range(0, 31, 5):
        if d: f.ln(X(d), Y(0), X(d), Y(35), 5, True, 0.6)
        f.t(X(d), Y(0)+16, str(d), 5, 0.75)
    f.ln(X(0), Y(0), X(0), Y(35), 0); f.ln(X(0), Y(0), X(30), Y(0), 0)
    f.pl([(X(d), Y(T)) for d, T in CURVA15], 0, w=1.6)
    f.t(X(0)-6, Y(35)-20, "Temperatura (°C)", 0, 0.7); f.t(X(30), Y(0)+32, "Días", 0, 0.7)
    f.t(X(15)+20, Y(35)-48, "Temperatura promedio diaria del mes de febrero en la ciudad de Arequipa", 0, 0.62)

@fig("x24_17")
def _(f):
    P = (1014, 421); A = (505, 661); B = (613, 661); g = 690
    f.pg([(700, g), (860, 560), (900, 575), (1014, 421), (1080, 520), (1100, 500), (1220, 525), (1300, g)], 7, True)
    f.ln(420, g, 1300, g); f.ln(1014, 421, 1014, g, 0, True); f.ln(A[0], 661, 1014, 661, 0, True)
    f.ln(*A, *P); f.ln(*B, *P); f.ang(A, (700, 661), P, 40); f.ang(B, (800, 661), P, 40)
    for x in (A[0], B[0]):
        f.ln(x, 661, x, g); f.ln(x, 670, x-10, g); f.ln(x, 670, x+10, g); f.ln(x-14, 652, x-14, 690, 0, w=2.2); f.ci(x-14, 645, 6)
    f.ln(1070, 421, 1070, g); f.arrow((1070, 600), (1070, 421)); f.arrow((1070, 500), (1070, g)); f.ln(1040, 421, 1110, 421); f.ln(1040, g, 1110, g)
    f.t(590, 643, "64°"); f.t(690, 643, "76°"); f.t(A[0]+14, 678, "A"); f.t(B[0]+14, 678, "B")
    f.t(1160, 540, "ALTURA DE", 0, 0.75); f.t(1160, 565, "LA MONTAÑA", 0, 0.75)

@fig("x24_18")
def _(f):
    O = (908, 1604); r = 152
    f.ci(*O, 178, 5); f.ci(*O, 168, 5); f.ci(*O, r)
    for R in (45, 30): f.ci(*O, R, 5)
    for a in (-111, 165, -13, 68):
        cx, cy = O[0]+92*math.cos(math.radians(a)), O[1]+92*math.sin(math.radians(a))
        f.ci(cx, cy, 24, 5); f.pg([(cx+11*math.cos(math.radians(k*60)), cy+11*math.sin(math.radians(k*60))) for k in range(6)], 5, True)
    pt = lambda a: (O[0]+r*math.cos(math.radians(a)), O[1]+r*math.sin(math.radians(a)))
    A, B, C = pt(-62), pt(11), pt(82)
    f.ln(*O, *A); f.ln(*O, *B); f.ln(*A, *B, 0, w=2.6); f.ln(*B, *C, 0, w=2.6); f.dot(*O)
    L = pt(197); f.ln(*O, *L); f.arrow(O, L)
    f.ang(O, A, B, 55); f.ang(B, A, C, 30)
    f.t(A[0]+8, A[1]-16, "A"); f.t(B[0]+18, B[1]+4, "B"); f.t(C[0]-6, C[1]+18, "C"); f.t(O[0]-14, O[1]+22, "O")
    f.t(860, 1572, "R"); f.t(968, 1562, "θ"); f.t(990, 1640, "α")

@fig("x24_20")
def _(f):
    A, B, C, P = (368, 843), (546, 718), (632, 878), (515, 812)
    f.tri(A, B, C); f.ln(*P, *A, 5); f.ln(*P, *B, 5); f.ln(*P, *C, 5); f.ci(*P, 10, 3); f.dot(*P, 3)
    for x0, y0, w, h in ((322, 800, 34, 43), (525, 676, 40, 42), (632, 838, 46, 40)):
        f.pg([(x0, y0), (x0+w, y0), (x0+w, y0+h), (x0, y0+h)], 5)
    f.t(342, 860, "A"); f.t(580, 700, "B"); f.t(692, 862, "C")

@fig("x24_21", 78)
def _(f):
    cam = [19, 73, 103, 189, 254, 273, 640, 972, 1083, 1139]; tot = [83, 145, 172, 259, 328, 336, 699, 1033, 1144, 1180]
    pct = [23, 50, 60, 73, 77, 81, 92, 94, 95, 97]
    X = lambda i: 70 + i*52; Y = lambda v: 420 - v*0.25; YP = lambda p: Y(12.7*p)
    for v in range(0, 1401, 200):
        f.ln(X(0)-26, Y(v), X(9)+26, Y(v), 5, v > 0, 0.6); f.t(X(0)-44, Y(v), str(v), 5, 0.6)
    for p in range(0, 101, 20): f.t(X(9)+48, YP(p), "%d%%" % p, 5, 0.6)
    f.ln(X(0)-26, Y(0), X(0)-26, Y(1400), 0); f.ln(X(9)+26, Y(0), X(9)+26, Y(1400), 0)
    for i in range(10):
        a, b = X(i)-22, X(i)
        f.pg([(a, Y(0)), (b, Y(0)), (b, Y(cam[i])), (a, Y(cam[i]))], 7, True); f.pg([(a, Y(0)), (b, Y(0)), (b, Y(cam[i])), (a, Y(cam[i]))])
        c, d = X(i), X(i)+22
        f.pg([(c, Y(0)), (d, Y(0)), (d, Y(tot[i])), (c, Y(tot[i]))], 2, True); f.pg([(c, Y(0)), (d, Y(0)), (d, Y(tot[i])), (c, Y(tot[i]))])
        f.t(X(i)-11, Y(cam[i])-9, str(cam[i]), 0, 0.5); f.t(X(i)+11, Y(tot[i])-9 - (10 if abs(tot[i]-cam[i]) < 60 else 0), str(tot[i]), 2, 0.5)
        f.t(X(i), Y(0)+14, str(2004+i), 0, 0.6)
    pts = [(X(i), YP(p)) for i, p in enumerate(pct)]
    f.pl(pts, 4, w=1.5)
    for (x, y), p in zip(pts, pct): f.dot(x, y, 4); f.t(x, y-14, "%d%%" % p, 4, 0.6)
    f.t(X(4.5), Y(1400)-18, "Producción de gas natural en el período 2004 - 2013", 0, 0.7); f.t(X(0)-36, Y(1400)-18, "MMPCD", 0, 0.6)
    lx = X(1); ly = Y(0)+34
    f.pg([(lx, ly-5), (lx+16, ly-5), (lx+16, ly+5), (lx, ly+5)], 7, True); f.t(lx+50, ly, "Camisea", 0, 0.6)
    f.pg([(lx+120, ly-5), (lx+136, ly-5), (lx+136, ly+5), (lx+120, ly+5)], 2, True); f.t(lx+175, ly, "Total País", 0, 0.6)
    f.ln(lx+250, ly, lx+275, ly, 4, w=1.5); f.dot(lx+262, ly, 4); f.t(lx+345, ly, "% Camisea/Total País", 4, 0.6)

def mini(f, ox, oy, lab, ymin, ymax, fn, t0, t1):
    u = 18; X = lambda t: ox + t*u; Y = lambda v: oy - v*u
    for t in range(-2, 6): f.ln(X(t), Y(ymin-0.4), X(t), Y(ymax+0.4), 5, False, 0.35)
    for v in range(ymin, ymax+1): f.ln(X(-2.5), Y(v), X(5.5), Y(v), 5, False, 0.35)
    f.ln(X(-2.5), Y(0), X(5.6), Y(0)); f.arrow((X(5), Y(0)), (X(5.6), Y(0)), 0, 6)
    f.ln(X(0), Y(ymin-0.4), X(0), Y(ymax+0.6)); f.arrow((X(0), Y(ymax)), (X(0), Y(ymax+0.6)), 0, 6)
    for t in (-2, -1, 1, 2, 3, 4, 5): f.t(X(t), Y(0)+10, str(t).replace("-", "−"), 0, 0.55)
    for v in range(ymin, ymax+1):
        if v: f.t(X(0)-9, Y(v), str(v).replace("-", "−"), 0, 0.55)
    f.t(X(0)-8, Y(0)+10, "0", 0, 0.55); f.t(X(5.4), Y(0)+20, "t", 0, 0.7); f.t(X(-1.1), Y(ymax+0.4), "f(t)", 0, 0.7)
    f.pl([(X(t0+(t1-t0)*k/40), Y(fn(t0+(t1-t0)*k/40))) for k in range(41)], 0, w=1.8)
    f.t(ox - 3.6*u, oy - (ymax+0.6)*u, lab, 0, 0.8)

@fig("x24_24", 92)
def _(f):
    fa = lambda t: 1+10*t-5*t*t
    mini(f, 80, 130, "A)", -1, 6, fa, 0, 1+math.sqrt(1.2))
    mini(f, 300, 130, "B)", -3, 5, lambda t: 5-5*(t-2)**2, 1, 3.25)
    mini(f, 80, 340, "C)", -1, 5, lambda t: 5-5*(t-1)**2, 0, 2)
    mini(f, 300, 340, "D)", -2, 6, fa, 0, 1+math.sqrt(1.56))

@fig("x24_25")
def _(f):
    cx, rx, ry, top, base, apex = 296, 62, 8, 194, 300, 382
    f.el(cx, top, rx, ry); f.el(cx, top-3, 22, 4, 5)
    f.ln(cx-rx, top, cx-rx, base); f.ln(cx+rx, top, cx+rx, base)
    f.pl(f.arc_pts(cx, base, rx, ry, 180, 360), 0, True); f.pl(f.arc_pts(cx, base, rx, ry, 0, 180))
    f.ln(cx-rx, base, cx, apex); f.ln(cx+rx, base, cx, apex)
    for x, y0 in ((cx-44, base), (cx+44, base), (cx+10, 340)): f.ln(x, y0, x, 392, 5, w=2.6)
    f.ln(cx-52, base-4, cx-36, base-4, 5, w=2.6); f.ln(cx+36, base-4, cx+52, base-4, 5, w=2.6)

@fig("x24_27")
def _(f):
    f.disc(477, 325, 70, 7); f.ci(477, 325, 70); f.disc(565, 247, 48, 7); f.ci(565, 247, 48)
    e = (565+48*math.cos(math.radians(-62)), 247+48*math.sin(math.radians(-62)))
    f.ln(565, 247, *e, 0, w=1.4); f.arrow((565, 247), e); f.t(562, 222, "R")
