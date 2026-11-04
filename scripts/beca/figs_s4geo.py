"""Figuras de la ficha de Geometría, semana 4: razones trigonométricas y sólidos."""
import math
from figs import fig
from figs_s2geo import inter
from figs_s1geo import foot

D = 1/0.556   # la vista de la ficha está a 0,556 de la página a 200 ppp

def cone(f, apex, c, rx, ry, label=None):
    f.ln(*apex, c[0]-rx, c[1]); f.ln(*apex, c[0]+rx, c[1])
    f.pl(f.arc_pts(c[0], c[1], rx, ry, 180, 360, 30), 0, True); f.pl(f.arc_pts(c[0], c[1], rx, ry, 0, 180, 30))

def cylinder(f, x0, x1, top, bot, ry):
    cx, rx = (x0+x1)/2, (x1-x0)/2
    f.el(cx, top, rx, ry); f.ln(x0, top, x0, bot); f.ln(x1, top, x1, bot)
    f.pl(f.arc_pts(cx, bot, rx, ry, 180, 360, 30), 0, True); f.pl(f.arc_pts(cx, bot, rx, ry, 0, 180, 30))

@fig("f4g_06")
def _(f):
    k = 22; o = (40, 300); P0 = lambda x, y: (o[0]+x*k, o[1]-y*k)
    A, Dd, C, B = P0(0, 0), P0(15, 0), P0(15, 5), P0(7.8, 10.4)
    f.pg([A, B, C, Dd]); f.ra(B, A, C, 10); f.ra(Dd, A, C, 10); f.ang(A, Dd, B, 30)
    f.t(A[0]-8, A[1]+14, "A"); f.t(B[0]-4, B[1]-14, "B"); f.t(C[0]+12, C[1], "C"); f.t(Dd[0]+8, Dd[1]+14, "D")
    f.t((A[0]+B[0])/2-12, (A[1]+B[1])/2, "x"); f.t((A[0]+Dd[0])/2, A[1]+16, "15"); f.t(C[0]+12, (C[1]+Dd[1])/2, "5"); f.t(A[0]+52, A[1]-14, "53°")

@fig("f4g_07")
def _(f):
    A, Dd, C, B = (582*D, 1162*D), (710*D, 1162*D), (820*D, 1162*D), (820*D, 1030*D)
    f.tri(A, B, C); f.ln(*B, *Dd); f.ra(C, A, B, 14); f.ang(A, C, B, 40); f.ang(B, A, Dd, 50)
    f.t(A[0]-6, A[1]+20, "A"); f.t(Dd[0], Dd[1]+20, "D"); f.t(C[0]+8, C[1]+20, "C"); f.t(B[0]+10, B[1]-12, "B")
    f.t(A[0]+64, A[1]-14, "60°"); f.t(B[0]-30, B[1]+34, "θ")

@fig("f4g_09")
def _(f):
    A, Dd, C, B = (95*D, 977*D), (248*D, 977*D), (378*D, 977*D), (222*D, 826*D)
    f.tri(A, B, C); f.ln(*B, *Dd); f.ang(A, C, B, 40); f.ang(C, B, A, 40); f.ang(B, A, Dd, 40); f.ang(B, Dd, C, 52)
    for p in ((A[0]+Dd[0])/2, (Dd[0]+C[0])/2): f.ln(p-4, A[1]-8, p+4, A[1]+8); f.ln(p+2, A[1]-8, p+10, A[1]+8)
    f.t(A[0]-6, A[1]+20, "A"); f.t(Dd[0], Dd[1]+20, "D"); f.t(C[0]+8, C[1]+20, "C"); f.t(B[0], B[1]-16, "B")
    f.t(A[0]+64, A[1]-16, "53°"); f.t(C[0]-62, C[1]-16, "45°"); f.t(B[0]-14, B[1]+44, "α"); f.t(B[0]+14, B[1]+44, "β")

@fig("f4g_11")
def _(f):
    FTL, FTR, FBR, FBL = (1130, 1021), (1336, 1021), (1336, 1291), (1130, 1291)
    BTL, BTR, BBR, BBL = (1192, 940), (1398, 940), (1398, 1209), (1192, 1209)
    f.pg([FTL, FTR, FBR, FBL]); f.pl([FTL, BTL, BTR, FTR]); f.ln(*BTR, *BBR); f.ln(*BBR, *FBR)
    f.ln(*BTL, *BBL, 0, True); f.ln(*BBL, *BBR, 0, True); f.ln(*BBL, *FBL, 0, True)
    f.ln(*FTL, *BBR); f.ang(FTL, FBL, BBR, 40)
    f.t(1160, 1082, "53°"); f.t(1268, 1118, "5")

@fig("f4g_16")
def _(f):
    x0, x1, top, bot, ry = 197, 567, 1370, 1557, 28
    cylinder(f, x0, x1, top, bot, ry); f.dot((x0+x1)/2, top); f.dot((x0+x1)/2, bot)
    f.ln(x0, top, x1, bot); f.ln(x0, bot, x1, top)
    X = ((x0+x1)/2, (top+bot)/2); f.ang(X, (x0, top), (x0, bot), 28)
    f.tick((600, top), (600, bot), 7); f.t(652, (top+bot)/2, "√6 − √2"); f.t(X[0]-60, X[1], "30°")

@fig("f4g_19")
def _(f):
    x0, x1, top, bot, ry = 1130, 1471, 922, 1121, 30
    cylinder(f, x0, x1, top, bot, ry); f.dot((x0+x1)/2, top); f.dot((x0+x1)/2, bot)
    f.ln(x1, top, x0, bot); f.ang((x1, top), (x1, bot), (x0, bot), 40)
    f.t(1280, 1012, "4"); f.t(1428, 980, "60°")

@fig("f4g_20")
def _(f):
    x0, x1, top, bot, ry = 100, 340, 100, 330, 26
    cx = (x0+x1)/2; O, Op, L, R = (cx, top), (cx, bot), (x0, (top+bot)/2), (x1, (top+bot)/2)
    f.pg([O, R, Op, L], 7, True); cylinder(f, x0, x1, top, bot, ry); f.pg([O, R, Op, L]); f.dot(*O); f.dot(*Op)
    f.t(O[0]+12, O[1]-12, "O"); f.t(Op[0]+12, Op[1]+16, "O'"); f.t(cx, (top+bot)/2, "S")

@fig("f4g_27")
def _(f):
    apex, c, rx, ry = (262*D, 1015*D), (262*D, 1135*D), 100*D, 12*D
    cone(f, apex, c, rx, ry); f.dot(*c); e = (c[0]+rx*0.9, c[1]+6); f.ln(*c, *e, 0, True); f.arrow(c, e)
    f.ang(apex, (c[0]-rx, c[1]), (c[0]+rx, c[1]), 26)
    f.t(apex[0], apex[1]+46, "74°"); f.t(c[0]-12, c[1]+2, "O"); f.t((c[0]+e[0])/2, c[1]-12, "r")

@fig("f4g_29")
def _(f):
    apex, c, rx, ry = (683*D, 448*D), (683*D, 672*D), 135*D, 15*D
    cone(f, apex, c, rx, ry); f.dot(*c); e = (c[0]+rx*0.55, c[1]+8); f.ln(*c, *e, 0, True); f.arrow(c, e)
    f.ang(apex, (c[0]-rx, c[1]), (c[0]+rx, c[1]), 26)
    f.t(apex[0], apex[1]+48, "106°"); f.t(c[0]-12, c[1]+2, "O"); f.t((c[0]+e[0])/2, c[1]-12, "r")

@fig("f4g_30")
def _(f):
    a1, c1, r1 = (622*D, 855*D), (622*D, 945*D), 92*D
    cone(f, a1, c1, r1, 12*D); f.ra(a1, (c1[0]-r1, c1[1]), (c1[0]+r1, c1[1]), 10)
    f.t(a1[0]-56, a1[1]+44, "x"); f.t(a1[0]+56, a1[1]+44, "x"); f.t(712*D, 905*D, "< >")
    a2, c2, r2 = (800*D, 850*D), (800*D, 948*D), 52*D
    cone(f, a2, c2, r2, 10*D); f.ln(*a2, *c2); f.dot(*c2); e = (c2[0]+r2*0.9, c2[1]+5); f.ln(*c2, *e, 0, True); f.arrow(c2, e)
    f.t(a2[0]+16, (a2[1]+c2[1])/2, "√2"); f.t(c2[0]-12, c2[1]+2, "O"); f.t((c2[0]+e[0])/2, c2[1]-12, "x")
