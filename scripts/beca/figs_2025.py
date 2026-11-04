"""Figuras redibujadas del examen 2025."""
import math
from figs import fig

@fig("x25_06")
def _(f):
    def glass(x, water):
        f.pl([(x, 0), (x+4, 40), (x+30, 40), (x+34, 0)])
        if water: f.pl([(x+1, 8), (x+8, 6), (x+16, 9), (x+24, 6), (x+33, 8)], 2)
        else: f.pg([(x+3.6, 34), (x+30.4, 34), (x+30, 40), (x+4, 40)], 4, True)
    for i in range(3): glass(i*38, True)
    for i in range(5): glass(122+i*38, False)
    for a, b, lab in ((0, 110, "Agua"), (122, 308, "Limonada")):
        m = (a+b)/2; f.pl([(a, 46), (a+3, 50), (m-4, 50), (m, 56), (m+4, 50), (b-3, 50), (b, 46)]); f.t(m, 72, lab)

@fig("x25_12", 72)
def _(f):
    X = lambda t: 40 + t*40; Y = lambda h: 400 - h*36
    for k in range(0, 15):
        f.ln(X(k/2), Y(0), X(k/2), Y(9.6), 5, False, 0.35 if k % 2 else 0.6)
    for k in range(0, 20):
        f.ln(X(0), Y(k/2), X(7.4), Y(k/2), 5, False, 0.35 if k % 2 else 0.6)
    f.ln(X(0), Y(0), X(7.5), Y(0)); f.arrow((X(7), Y(0)), (X(7.5), Y(0))); f.ln(X(0), Y(0), X(0), Y(9.8)); f.arrow((X(0), Y(9)), (X(0), Y(9.8)))
    for t in range(0, 8): f.t(X(t), Y(0)+16, str(t), 0, 0.7)
    for h in range(1, 10): f.t(X(0)-14, Y(h), str(h), 0, 0.7)
    f.pl([(X(k*6/48), Y(6*(k*6/48)-(k*6/48)**2)) for k in range(49)], 0, w=1.6)
    f.t(X(2.35), Y(8.2), "f", 0, 0.8); f.t(X(0)-30, Y(9.8)-6, "Altura (m)", 0, 0.75); f.t(X(7.5)+44, Y(0), "Tiempo(s)", 0, 0.75)

@fig("x25_18", 66)
def _(f):
    pts = [(0, 0), (1, 6), (2, 14), (3, 24), (4, 36), (5, 50), (6, 66), (7, 84), (8, 104), (9, 126), (10, 150), (11, 176), (12, 204)]
    X = lambda x: 90 + x*34; Y = lambda y: 420 - y*1.4
    for y in range(0, 251, 50): f.ln(X(0), Y(y), X(14), Y(y), 5, False, 0.5); f.t(X(0)-18, Y(y), str(y), 5, 0.65)
    for x in range(0, 15, 2): f.t(X(x), Y(0)+16, str(x), 5, 0.65)
    for x, y in pts: f.dot(X(x), Y(y), 5); f.t(X(x)-4, Y(y)-14, "(%d,%d)" % (x, y), 0, 0.5)
    f.t(X(7), Y(250)-20, "VOLUMEN DE VENTAS DE LA CREMA", 0, 0.65); f.t(X(7), Y(0)+34, "Nº de semanas", 0, 0.65)
    f.t(X(0)-80, Y(150), "Cantidad", 0, 0.62); f.t(X(0)-80, Y(150)+14, "de cremas", 0, 0.62); f.t(X(0)-80, Y(150)+28, "vendidas", 0, 0.62)

@fig("x25_19")
def _(f):
    cx, rx, ry, top, lvl, bot = 318, 85, 15, 207, 288, 368
    f.pg([(160, 318), (503, 318), (480, 425), (140, 425)], 7, True); f.pg([(160, 318), (503, 318), (480, 425), (140, 425)])
    f.pg([(cx-rx, top), (cx+rx, top), (cx+rx, bot), (cx-rx, bot)], 6, True)
    f.pg(f.arc_pts(cx, lvl, rx, ry, 180, 360) + f.arc_pts(cx, bot, rx, ry, 0, 180), 5, True)
    f.el(cx, lvl, rx, ry, 0); f.el(cx, top, rx, ry); f.ln(cx-rx, top, cx-rx, bot); f.ln(cx+rx, top, cx+rx, bot)
    f.pl(f.arc_pts(cx, bot, rx, ry, 0, 180))
    f.dot(cx, top); f.ln(cx, top, cx+rx, top); f.arrow((cx, top), (cx+rx, top)); f.ln(cx+60, top-8, cx+72, top-40, 0, True)
    f.t(cx+118, top-44, "r = 0,5 m"); f.tick((432, top), (432, bot), 10); f.t(480, 278, "h =1,2 m")

@fig("x25_20", 80)
def _(f):
    ap = (283, 80); FL, FR, BR, BL = (65, 410), (410, 410), (518, 305), (175, 305)
    fl, fr, br, bl = (65, 740), (410, 740), (518, 635), (175, 635)
    f.pg([FL, FR, fr, fl], 7, True); f.pg([FR, BR, br, fr], 5, True); f.pg([FL, FR, BR, BL], 7, True)
    door = [(205, 740), (205, 590)] + f.arc_pts(250, 590, 45, 50, 180, 360) + [(295, 590), (295, 740)]
    f.pg(door, 6, True)
    f.pg([FL, FR, fr, fl]); f.pl([FR, BR, br, fr]); f.ln(*FL, *BL); f.ln(*BL, *BR)
    f.ln(*BL, *bl, 0, True); f.ln(*bl, *br, 0, True); f.ln(*bl, *fl, 0, True)
    f.ln(*FL, *bl); f.ln(*fl, *BL); f.ln(*FR, *br); f.ln(*fr, *BR)
    f.ln(*ap, *FL); f.ln(*ap, *FR); f.ln(*ap, *BR); f.ln(*ap, *BL, 0, True)

@fig("x25_22")
def _(f):
    T, R, B, L = (197, 490), (263, 598), (197, 705), (130, 598)
    f.pg([T, R, B, L], 5); f.ln(*T, *B, 4, w=1.6); f.ln(*L, *R, 4, w=1.6)
    f.ln(197, 548, 265, 548, 2); f.arrow((197, 548), (265, 548), 2); f.ln(264, 597, 282, 560, 2); f.arrow((264, 597), (282, 560), 2)
    f.t(300, 548, "cercas")

@fig("x25_24")
def _(f):
    O, P, Q, T = (258, 437), (442, 437), (442, 357), (442, 272)
    f.ln(*O, *P); f.ln(*P, *T); f.ln(*O, *Q); f.ln(*O, *T); f.ra(P, O, T, 12)
    f.ang(O, P, Q, 40); f.ang(O, Q, T, 52)
    f.ln(430, 272, 430, 352, 1); f.ln(430, 360, 430, 437, 1); f.ln(424, 272, 436, 272, 1); f.ln(424, 356, 436, 356, 1); f.ln(424, 437, 436, 437, 1)
    for y in (282, 357, 437):
        f.pg([(452, y-12), (446, y), (458, y)], 0, True); f.ln(452, y, 452, y+6, 4)
    for x in (212, 245):
        f.el(x, 437, 14, 32); f.el(x, 437, 7, 16, 5); f.disc(x, 437, 6, 0)
    f.t(450, 316, "h", 1); f.t(460, 394, "2 km", 1); f.t(342, 456, "2√3 km", 1); f.t(304, 406, "30°", 0, 0.75); f.t(312, 428, "30°", 0, 0.75)

@fig("x25_25")
def _(f):
    c = (412, 927); R = 66
    V = {k: (c[0]+R*math.cos(math.radians(a)), c[1]+R*math.sin(math.radians(a))) for k, a in zip("ABCDEF", (-120, -60, 0, 60, 120, 180))}
    f.pg([V[k] for k in "ABCDEF"], 0)
    for a, b in ("AD", "BE", "CF"): f.ln(*V[a], *V[b])
    f.dot(*c)
    for k, (dx, dy) in zip("ABCDEF", ((-10, -16), (10, -16), (20, 0), (12, 16), (-12, 16), (-20, 0))): f.t(V[k][0]+dx, V[k][1]+dy, k)
    f.t(c[0]+14, c[1]-12, "o", 0, 0.8)

@fig("x25_30", 70)
def _(f):
    va, vb = [6, 10, 12, 2], [5, 11, 10, 4]; cats = ["C", "B", "A", "AD"]
    X = lambda i: 150 + i*90; Y = lambda v: 400 - v*20
    for v in range(0, 15, 2): f.ln(X(0)-45, Y(v), X(3)+45, Y(v), 5, False, 0.5); f.t(X(0)-58, Y(v), str(v), 5, 0.6)
    for i in range(4):
        for k, (vals, col, dx) in enumerate(((va, 5, -28), (vb, 7, 4))):
            x0 = X(i)+dx; v = vals[i]
            f.pg([(x0, Y(0)), (x0+24, Y(0)), (x0+24, Y(v)), (x0, Y(v))], col, True); f.pg([(x0, Y(0)), (x0+24, Y(0)), (x0+24, Y(v)), (x0, Y(v))])
            f.t(x0+12, Y(v)+10, str(v), 0, 0.55)
        f.t(X(i), Y(0)+14, cats[i], 0, 0.6)
    f.t(X(1.5), Y(14)-34, "RESULTADOS DE LOS NIVELES DE LOGRO EN EL ÁREA", 0, 0.62); f.t(X(1.5), Y(14)-18, "DE MATEMÁTICA DE LAS SECCIONES DE 4° GRADO", 0, 0.62)
    f.t(X(1.5), Y(0)+30, "NIVELES DE LOGRO", 0, 0.6)
    f.pg([(X(1)-20, Y(0)+42), (X(1)-10, Y(0)+42), (X(1)-10, Y(0)+50), (X(1)-20, Y(0)+50)], 5, True); f.t(X(1)+16, Y(0)+46, "Sección A", 0, 0.55)
    f.pg([(X(2)-20, Y(0)+42), (X(2)-10, Y(0)+42), (X(2)-10, Y(0)+50), (X(2)-20, Y(0)+50)], 7, True); f.t(X(2)+16, Y(0)+46, "Sección B", 0, 0.55)
    f.t(X(0)-110, Y(7)-8, "CANTIDAD DE", 0, 0.55); f.t(X(0)-110, Y(7)+8, "ESTUDIANTES", 0, 0.55)
