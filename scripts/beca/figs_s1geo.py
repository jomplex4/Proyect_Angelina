"""Figuras de la ficha de Geometría, semana 1. Coordenadas de la página a 200 ppp o construidas con la geometría del enunciado."""
import math
from figs import fig

def onc(c, r, deg):
    return (c[0] + r*math.cos(math.radians(deg)), c[1] + r*math.sin(math.radians(deg)))

def inter_line_circle(p, q, c, r):
    """Puntos donde la recta p-q corta la circunferencia, ordenados desde p."""
    dx, dy = q[0]-p[0], q[1]-p[1]; fx, fy = p[0]-c[0], p[1]-c[1]
    a = dx*dx+dy*dy; b = 2*(fx*dx+fy*dy); cc = fx*fx+fy*fy-r*r
    disc = math.sqrt(max(0, b*b-4*a*cc))
    return [(p[0]+t*dx, p[1]+t*dy) for t in sorted(((-b-disc)/(2*a), (-b+disc)/(2*a)))]

def foot(p, a, b):
    dx, dy = b[0]-a[0], b[1]-a[1]; t = ((p[0]-a[0])*dx+(p[1]-a[1])*dy)/(dx*dx+dy*dy)
    return (a[0]+t*dx, a[1]+t*dy)

@fig("f1g_01")
def _(f):
    A, B, C, D = (148, 1137), (481, 882), (677, 1137), (481, 1017)
    f.ln(82, 1137, 717, 1137); f.ln(*A, 573, 811); f.ln(*B, *C); f.ln(*A, *D); f.ln(*D, *C); f.ra(B, A, C, 18)
    f.ang(A, (82, 1137), B, 26); f.ang(A, C, D, 52); f.ang(C, B, (717, 1137), 26); f.ang(C, A, D, 40); f.ang(D, A, C, 20)
    f.t(115, 1094, "6α"); f.t(235, 1124, "α"); f.t(148, 1162, "A"); f.t(470, 866, "B"); f.t(481, 998, "D"); f.t(473, 1062, "x")
    f.t(613, 1124, "β"); f.t(690, 1094, "6β"); f.t(682, 1164, "C")

@fig("f1g_02")
def _(f):
    P, M, BL, T, R, BR, X = (321, 1484), (293, 1622), (271, 1724), (679, 1427), (726, 1624), (757, 1775), (448, 1792)
    f.ln(*BL, *P); f.ln(*P, *T); f.ln(*T, *BR); f.ln(*M, *R); f.ln(*M, *X); f.ln(*X, *T)
    f.ang(P, M, T, 20); f.ang(R, M, BR, 22); f.ang(M, R, X, 26); f.ang(M, X, BL, 18); f.ang(T, P, X, 22); f.ang(T, X, R, 30); f.ang(X, M, T, 18)
    f.t(368, 1520, "130°"); f.t(660, 1655, "110°"); f.t(347, 1642, "α"); f.t(306, 1668, "α"); f.t(645, 1440, "β"); f.t(676, 1470, "β"); f.t(446, 1760, "x")

@fig("f1g_04")
def _(f):
    A, B, C, D, E = (940, 893), (1028, 706), (1120, 893), (1312, 818), (1504, 893)
    f.ln(*A, *E); f.ln(*A, *B); f.ln(*B, *C); f.ln(*B, *E); f.ln(*C, *D)
    f.ang(B, A, C, 26); f.ang(E, C, B, 46)
    f.t(1028, 688, "B"); f.t(1028, 740, "4x"); f.t(1325, 800, "D"); f.t(1418, 876, "x"); f.t(930, 916, "A"); f.t(1120, 916, "C"); f.t(1506, 916, "E")

@fig("f1g_05")
def _(f):
    A, B, C, D = (1032, 1466), (1110, 1254), (1278, 1254), (1424, 1466)
    f.pg([A, B, C, D]); f.ang(B, A, C, 22); f.ang(D, A, C, 24)
    f.t(1105, 1236, "B"); f.t(1195, 1238, "6"); f.t(1280, 1238, "C"); f.t(1150, 1290, "110°"); f.t(1058, 1340, "8"); f.t(1378, 1448, "55°")
    f.t(1027, 1486, "A"); f.t(1430, 1488, "D")

@fig("f1g_06")
def _(f):
    A, B, C, D, E = (1062, 1931), (1300, 1788), (1535, 1931), (1300, 2073), (1193, 1931)
    f.pg([A, B, C, D]); f.ln(*A, *C); f.ln(*E, *B); f.ang(E, C, B, 24)
    f.t(1045, 1932, "A"); f.t(1300, 1770, "B"); f.t(1553, 1932, "C"); f.t(1300, 2094, "D"); f.t(1182, 1952, "E"); f.t(1252, 1906, "53°")

@fig("f1g_07")
def _(f):
    s, al = 38.0, 31.5          # escala y mitad del ángulo A
    A = (160, 968); u2 = (math.cos(math.radians(2*al)), -math.sin(math.radians(2*al)))
    B = (A[0]+7*s*u2[0], A[1]+7*s*u2[1]); D = (A[0]+12*s, A[1]); C = (D[0]+7*s*u2[0], D[1]+7*s*u2[1])
    F = (B[0]+7*s, B[1]); E = (D[0]+2*s*u2[0], D[1]+2*s*u2[1])
    f.pg([A, B, C, D]); f.ln(*A, *F); f.ln(*F, *E); f.ra(F, A, E, 12)
    f.ang(A, D, F, 30); f.ang(A, F, B, 40)
    f.t(A[0]-10, A[1]+14, "A"); f.t(B[0]-6, B[1]-14, "B"); f.t(F[0]+2, F[1]-14, "F"); f.t(C[0]+10, C[1]-12, "C"); f.t(E[0]+14, E[1]+2, "E")
    f.t(D[0]+8, D[1]+16, "D"); f.t((A[0]+B[0])/2-14, (A[1]+B[1])/2, "7"); f.t((A[0]+D[0])/2, A[1]+18, "12"); f.t(A[0]+44, A[1]-12, "α"); f.t(A[0]+26, A[1]-40, "α")

@fig("f1g_09")
def _(f):
    E, O, R = (118, 1907), (575, 1907), 180
    A, B = (O[0]-R, O[1]), (O[0]+R, O[1])
    f.ln(*E, *B); f.pl(f.arc_pts(O[0], O[1], R, R, 180, 360, 40)); f.dot(*O)
    a = math.radians(-21); far = (E[0]+900*math.cos(a), E[1]+900*math.sin(a))
    D, C = inter_line_circle(E, far, O, R)
    f.ln(*E, *C); f.ang(E, B, C, 36)
    aD = math.degrees(math.atan2(D[1]-O[1], D[0]-O[0])); aC = math.degrees(math.atan2(C[1]-O[1], C[0]-O[0]))
    arc = f.arc_pts(O[0], O[1], R+16, R+16, aD, aC, 12); f.pl(arc); f.arrow(arc[1], arc[0]); f.arrow(arc[-2], arc[-1])
    m = onc(O, R+40, (aD+aC)/2); f.t(m[0], m[1], "40°")
    f.t(E[0], E[1]+20, "E"); f.t(A[0], A[1]+20, "A"); f.t(O[0], O[1]+20, "O"); f.t(B[0]+4, B[1]+20, "B")
    f.t(D[0]-12, D[1]-14, "D"); f.t(C[0]+6, C[1]-18, "C"); f.t(E[0]+60, E[1]-14, "x")

@fig("f1g_11")
def _(f):
    O, R = (1023, 1580), 390
    A, B = (O[0], O[1]-R), (O[0]+R, O[1]); D = (O[0]+0.6*R, O[1]); C = (O[0]+0.8*R, O[1]-0.6*R)
    f.ln(*O, *A); f.ln(*O, *B); f.pl(f.arc_pts(O[0], O[1], R, R, 270, 360, 40)); f.ra(O, A, B, 22)
    f.ln(*D, *C); f.ln(*C, *B)
    arc = f.arc_pts(O[0], O[1], R+30, R+30, -36.87, 0, 14); f.pl(arc); f.arrow(arc[1], arc[0]); f.arrow(arc[-2], arc[-1])
    f.t(O[0]-18, O[1]+16, "O"); f.t(A[0]-14, A[1]-14, "A"); f.t(B[0]+12, B[1]+18, "B"); f.t(D[0], D[1]+20, "D"); f.t(C[0]+2, C[1]-20, "C")
    f.t((O[0]+D[0])/2, O[1]+20, "3a"); f.t((D[0]+B[0])/2, O[1]+20, "2a"); m = onc(O, R+56, -18); f.t(m[0], m[1], "x")

@fig("f1g_13")
def _(f):
    c, r = (548, 992), 140; y0 = c[1]+r
    A = onc(c, r, math.degrees(math.atan2(907-992, 440-548))); B = onc(c, r, math.degrees(math.atan2(1042-992, 410-548))); C = (c[0], y0)
    f.ci(*c, r)
    ux, uy = A[0]-c[0], A[1]-c[1]                      # tangente en A: perpendicular al radio
    t = (y0-A[1])/ux if ux else 0; P = (A[0]-uy*(y0-A[1])/ux, y0)
    D = (A[0]+(B[0]-A[0])*(y0-A[1])/(B[1]-A[1]), y0)
    far = (A[0]+(A[0]-P[0])*0.45, A[1]+(A[1]-P[1])*0.45)
    f.ln(*P, *far); f.ln(P[0], y0, c[0]+120, y0); f.ln(*A, *D)
    f.ang(P, (c[0], y0), A, 26); f.ang(D, P, A, 24)
    f.t(A[0]-12, A[1]-18, "A"); f.t(B[0]-18, B[1]-14, "B"); f.t(D[0]-46, D[1]-24, "120°"); f.t(D[0]+4, D[1]+22, "D"); f.t(C[0]+6, C[1]+22, "C")
    f.t(P[0]-6, P[1]+22, "P"); f.t(P[0]+40, P[1]-18, "x")

@fig("f1g_14")
def _(f):
    O, R = (440, 1908), 280
    A, E = (O[0]-R, O[1]), (O[0]+R, O[1]); B = onc(O, R, -137); C = onc(O, R, -90); D = onc(O, R, -40.6)
    f.ln(*A, *E); f.pl(f.arc_pts(O[0], O[1], R, R, 180, 360, 50)); f.dot(*O)
    f.ln(*B, *C); f.ln(*C, *D); f.ln(*B, *E); f.ln(*A, *D)
    X = ((A[0]*(D[1]-A[1])*(E[0]-B[0]) - B[0]*(E[1]-B[1])*(D[0]-A[0]) + (B[1]-A[1])*(D[0]-A[0])*(E[0]-B[0])) / ((D[1]-A[1])*(E[0]-B[0]) - (E[1]-B[1])*(D[0]-A[0])), 0)
    Xy = A[1] + (X[0]-A[0])*(D[1]-A[1])/(D[0]-A[0]); Xp = (X[0], Xy)
    f.ang(C, B, D, 26); f.ang(Xp, D, E, 46)
    f.t(C[0], C[1]-18, "C"); f.t(C[0], C[1]+52, "11x"); f.t(B[0]-16, B[1]-2, "B"); f.t(D[0]+18, D[1]-2, "D"); f.t(Xp[0]+62, Xp[1]-4, "x")
    f.t(A[0]-4, A[1]+22, "A"); f.t(O[0], O[1]+24, "O"); f.t(E[0]+4, E[1]+22, "E")

@fig("f1g_15")
def _(f):
    O, R = (1156, 945), 186
    A, C, B = (O[0]-R, O[1]), (O[0]+R, O[1]), onc(O, R, -92)
    E = (1500, C[1]+(C[1]-B[1])/(C[0]-B[0])*(1500-C[0]))
    D = inter_line_circle(A, E, O, R)[1]
    f.ci(*O, R); f.dot(*O); f.ln(*A, *C); f.ln(*B, *E); f.ln(*A, *E); f.ang(E, A, B, 40)
    aB = math.degrees(math.atan2(B[1]-O[1], B[0]-O[0])); aD = math.degrees(math.atan2(D[1]-O[1], D[0]-O[0]))
    arc = f.arc_pts(O[0], O[1], R+26, R+26, aB+4, aD-4, 30); f.pl(arc); f.arrow(arc[1], arc[0]); f.arrow(arc[-2], arc[-1])
    m = onc(O, R+44, (aB+aD)/2); f.t(m[0], m[1], "6x")
    f.t(A[0]-16, A[1], "A"); f.t(O[0]+4, O[1]+22, "O"); f.t(C[0]+16, C[1]-12, "C"); f.t(B[0]-14, B[1]-18, "B"); f.t(D[0]-6, D[1]+22, "D")
    f.t(E[0]+12, E[1]+14, "E"); f.t(E[0]-64, E[1]-26, "2x")

@fig("f1g_16")
def _(f):
    O, R = (1130, 1728), 172
    aA = math.degrees(math.atan2(1622-1728, 990-1130)); A = onc(O, R, aA); C = onc(O, R, aA+180)
    y = C[1]; B = (O[0]-math.sqrt(R*R-(y-O[1])**2), y); Dp = (1538, y)
    Mx = inter_line_circle(A, Dp, O, R)[1]
    f.ci(*O, R); f.dot(*O); f.ln(*A, *C); f.ln(B[0]-30, y, *Dp); f.ln(*A, *Dp); f.ang(Dp, B, A, 60)
    aB = math.degrees(math.atan2(B[1]-O[1], B[0]-O[0])); aC = math.degrees(math.atan2(C[1]-O[1], C[0]-O[0]))
    arc = f.arc_pts(O[0], O[1], R+30, R+30, aC, aB, 30); f.pl(arc); f.arrow(arc[1], arc[0]); f.arrow(arc[-2], arc[-1])
    m = onc(O, R+46, (aB+aC)/2); f.t(m[0], m[1], "8x")
    f.t(A[0]-10, A[1]-18, "A"); f.t(O[0]-22, O[1]+16, "O"); f.t(Mx[0]+22, Mx[1]-12, "M"); f.t(B[0]-18, B[1]-14, "B"); f.t(C[0]+20, C[1]+18, "C")
    f.t(Dp[0]+8, Dp[1]+20, "D"); f.t(Dp[0]-80, Dp[1]-14, "x")

@fig("f1g_17")
def _(f):
    O, R = (479, 869), 140
    P = (O[0]-2*R, O[1]); A, B, C, M = onc(O, R, -120), (O[0]+R, O[1]), onc(O, R, 120), onc(O, R, -60)
    N = ((B[0]+C[0])/2, (B[1]+C[1])/2)
    f.ci(*O, R); f.dot(*O)
    for T in (A, C): f.ln(*P, P[0]+(T[0]-P[0])*1.45, P[1]+(T[1]-P[1])*1.45)
    f.ln(*P, *B); f.ln(*P, *M); f.ln(*C, *B); f.ln(*M, *N); f.ang(M, P, N, 22)
    f.t(A[0]-6, A[1]-18, "A"); f.t(M[0]+18, M[1]-14, "M"); f.t(O[0]-14, O[1]+20, "O"); f.t(B[0]+16, B[1]+2, "B"); f.t(N[0]+2, N[1]+20, "N")
    f.t(C[0]-6, C[1]+20, "C"); f.t(P[0]-14, P[1], "P"); f.t(M[0]-34, M[1]+26, "x")

@fig("f1g_18")
def _(f):
    L, T, R0, F = (169, 1592), (618, 1361), (618, 1592), (517, 1592)
    f.pg([L, T, R0]); f.ln(*T, *F); f.ra(R0, T, L, 20); f.ang(L, R0, T, 40); f.ang(T, F, R0, 40)
    f.t(244, 1572, "β"); f.t(602, 1428, "α"); f.t(568, 1612, "x")

@fig("f1g_22")
def _(f):
    B, C, D, A, F = (1022, 1444), (1404, 1444), (1404, 1823), (1022, 1823), (1404, 1617)
    E = foot(B, A, F)
    f.pg([A, B, C, D]); f.ln(*B, *F); f.ln(*A, *F); f.ln(*B, *E); f.ra(E, B, F, 14)
    f.ang(B, A, E, 26); f.ang(B, C, F, 50)
    f.t(1004, 1440, "B"); f.t(1424, 1440, "C"); f.t(1424, 1620, "F"); f.t(1007, 1842, "A"); f.t(1424, 1842, "D"); f.t(E[0]+10, E[1]+24, "E")
    f.t(1045, 1530, "37°"); f.t(1105, 1454, "α")
