"""Figuras de la ficha de Geometría, semana 3. Casi todas construidas con las medidas del enunciado para que queden exactas."""
import math
from figs import fig
from figs_s1geo import onc, inter_line_circle, foot
from figs_s2geo import inter, ang_of

def circ3(a, b, c):
    ax, ay = a; bx, by = b; cx, cy = c
    d = 2*(ax*(by-cy)+bx*(cy-ay)+cx*(ay-by))
    ux = ((ax*ax+ay*ay)*(by-cy)+(bx*bx+by*by)*(cy-ay)+(cx*cx+cy*cy)*(ay-by))/d
    uy = ((ax*ax+ay*ay)*(cx-bx)+(bx*bx+by*by)*(ax-cx)+(cx*cx+cy*cy)*(bx-ax))/d
    return (ux, uy), math.dist((ux, uy), a)

def S(p, k=40, o=(0, 0)): return (o[0]+p[0]*k, o[1]+p[1]*k)

@fig("f3g_01")
def _(f):
    O, R = (243, 393), 75
    A, P, B, N, C = (onc(O, R, a) for a in (-158, -92, -18, 40, 128))
    Q = inter(A, B, P, N); M = inter(C, B, P, N)
    f.ci(*O, R); f.ln(*A, *B); f.ln(*P, *N); f.ln(*C, *B)
    for p, l, dx, dy in ((A, "A", -10, -8), (P, "P", 0, -12), (B, "B", 10, -6), (N, "N", 9, 8), (C, "C", -8, 10), (Q, "Q", 2, -10), (M, "M", -4, 12)):
        f.t(p[0]+dx, p[1]+dy, l, 0, 0.9)

@fig("f3g_02")
def _(f):
    O, r = (330, 670), 80
    D = math.sqrt(7)*r                        # potencia del punto: 4·6 = 24 con radio 2
    E = (O[0]-D*math.cos(math.radians(8)), O[1]+D*math.sin(math.radians(8)))
    dist = r*math.cos(math.radians(30))       # cuerda que abarca 60°
    base = math.atan2(O[1]-E[1], O[0]-E[0]); off = math.asin(dist/D)
    t = base-off; far = (E[0]+3*D*math.cos(t), E[1]+3*D*math.sin(t))
    F1, F2 = inter_line_circle(E, far, O, r)
    b2 = base+0.25; far2 = (E[0]+3*D*math.cos(b2), E[1]+3*D*math.sin(b2)); G1, G2 = inter_line_circle(E, far2, O, r)
    f.ci(*O, r); f.dot(*O); f.ln(*E, *F2); f.ln(*E, *G2)
    rr = onc(O, r, 35); f.ln(*O, *rr); f.arrow(O, rr)
    a1, a2 = ang_of(O, F1), ang_of(O, F2); arc = f.arc_pts(O[0], O[1], r+14, r+14, a1, a2, 12); f.pl(arc); f.arrow(arc[1], arc[0]); f.arrow(arc[-2], arc[-1])
    m = onc(O, r+30, (a1+a2)/2); f.t(m[0], m[1], "x")
    f.t((E[0]+F1[0])/2-4, (E[1]+F1[1])/2-12, "4"); f.t((E[0]+G1[0])/2, (E[1]+G1[1])/2+14, "3"); f.t((G1[0]+G2[0])/2-4, (G1[1]+G2[1])/2+14, "5")
    f.t(O[0]-10, O[1]+12, "O"); f.t((O[0]+rr[0])/2+8, (O[1]+rr[1])/2-12, "2")

@fig("f3g_03")
def _(f):
    T, L, R = (237, 1830), (117, 2092), (765, 2092)
    f.tri(T, L, R); f.ra(T, L, R, 22); f.ang(R, L, T, 46)
    f.t(155, 1975, "√a"); f.t(510, 1905, "6"); f.t(440, 2128, "√(2a+7)"); f.t(700, 2072, "x")

@fig("f3g_04")
def _(f):
    k = 40; h = math.sqrt(20)
    L, H, D, R, V = S((0, 0)), S((4, 0)), S((6, 0)), S((9, 0)), S((4, -h))
    f.tri(L, V, R); f.ln(*V, *H); f.ln(*V, *D); f.ra(H, V, R, 10); f.ra(V, L, R, 10)
    f.ang(V, H, D, 40); f.ang(V, D, R, 52)
    y = 24
    f.tick((L[0], y), (H[0], y), 5); f.tick((H[0], y), (D[0], y), 5); f.tick((D[0], y), (R[0], y), 5)
    f.t((L[0]+H[0])/2, y+14, "4"); f.t((H[0]+D[0])/2, y+14, "2"); f.t((D[0]+R[0])/2, y+14, "x")
    f.t(V[0]+10, V[1]+50, "α"); f.t(V[0]+28, V[1]+40, "α")

@fig("f3g_05")
def _(f):
    s, rs = (660, 731.4), 63.4; b, rb = (660, 806), 107.6
    yab = 718; dxa = math.sqrt(rs*rs-(yab-s[1])**2); A, B = (s[0]-dxa, yab), (s[0]+dxa, yab)
    P = (805, yab)
    d = math.dist(P, s); t = math.asin(rs/d); base = math.atan2(s[1]-P[1], s[0]-P[0])
    ang = base - t if True else base + t
    cand = []
    for sg in (-1, 1):
        a = base + sg*t; L = math.sqrt(d*d-rs*rs); T = (P[0]+L*math.cos(a), P[1]+L*math.sin(a)); cand.append(T)
    T = max(cand, key=lambda q: q[1])
    far = (P[0]+(T[0]-P[0])*3, P[1]+(T[1]-P[1])*3); Q, C = inter_line_circle(P, far, b, rb)
    f.ci(*s, rs); f.ci(*b, rb); f.ln(A[0]-10, yab, *P); f.ln(*P, *C)
    f.t(A[0]-10, A[1]-10, "A", 0, 0.9); f.t(B[0]+4, B[1]-10, "B", 0, 0.9); f.t(P[0]+8, P[1], "P", 0, 0.9)
    f.t(Q[0]+8, Q[1]+8, "Q", 0, 0.9); f.t(T[0], T[1]+12, "T", 0, 0.9); f.t(C[0]-10, C[1]+6, "C", 0, 0.9)

@fig("f3g_06")
def _(f):
    O, R = (475, 834), 150
    A = onc(O, R, ang_of(O, (355, 740))); y = 911.5
    ux, uy = A[0]-O[0], A[1]-O[1]; B = (A[0]-uy*(y-A[1])/ux*(-1)*(-1), y)
    B = (A[0] + (y-A[1])*(uy/ux)*(-1), y) if False else (A[0] - (y-A[1])*uy/ux, y)
    xs = sorted(O[0]+s*math.sqrt(R*R-(y-O[1])**2) for s in (-1, 1)); C, D = (A[0], y), (xs[1], y)
    f.ci(*O, R); f.ln(B[0], B[1], A[0]+(A[0]-B[0])*0.45, A[1]+(A[1]-B[1])*0.45); f.ln(*B, *D); f.ln(*A, *C); f.ra(C, A, D, 12)
    f.ang(B, D, A, 30)
    f.t(A[0]-14, A[1]-12, "A"); f.t(B[0]-4, B[1]+20, "B"); f.t(C[0]-6, C[1]+20, "C"); f.t(D[0]+6, D[1]+20, "D"); f.t(B[0]+44, B[1]-22, "x")

@fig("f3g_07")
def _(f):
    k = 30; V = (300, 300); u = math.radians(90)  # bisectriz hacia abajo
    dA, dB = math.radians(90+37), math.radians(90-37)
    A = (V[0]+2*k*math.cos(dA), V[1]+2*k*math.sin(dA)); B = (V[0]+6*k*math.cos(dB), V[1]+6*k*math.sin(dB))
    c, R = circ3(V, A, B); D = (V[0], V[1]+5*k)
    f.ci(*c, R); f.ln(*V, *A); f.ln(*V, *B); f.ln(*V, *D)
    f.ang(V, A, D, 26); f.ang(V, D, B, 32)
    f.t(A[0]-8, A[1]-14, "2"); f.t(B[0]+4, B[1]-16, "6"); f.t(V[0]+10, V[1]+70, "x"); f.t(V[0]-22, V[1]+34, "37°", 0, 0.8); f.t(V[0]+22, V[1]+38, "37°", 0, 0.8)

@fig("f3g_09")
def _(f):
    k = 30; h = math.sqrt(22.5); r = h*math.sqrt(2); o = (300, 300)
    P0 = lambda x, y: (o[0]+x*k, o[1]-y*k)
    A, B, C, D = P0(-h, -h), P0(-h, h), P0(h, h), P0(h, -h)
    w = math.sqrt(r*r-9); P, Q, N = P0(-w, 3), P0(w, 3), P0(3, 3)
    f.ci(o[0], o[1], r*k); f.pg([A, B, C, D]); f.ln(*A, *C); f.ln(*P, *Q)
    f.t(B[0]-10, B[1]-12, "B"); f.t(C[0]+10, C[1]-12, "C"); f.t(A[0]-8, A[1]+12, "A"); f.t(D[0]+10, D[1]+12, "D")
    f.t(P[0]-12, P[1], "P"); f.t(Q[0]+12, Q[1], "Q"); f.t(N[0]-4, N[1]-14, "N")

@fig("f3g_10")
def _(f):
    k = 30; o = (40, 300)
    P0 = lambda x, y=0: (o[0]+x*k, o[1]-y*k)
    A, H, C, D = P0(0), P0(9), P0(10), P0(13); B, M = P0(9, 6), P0(9, 3)
    f.pl(f.arc_pts(o[0]+6.5*k, o[1], 6.5*k, 6.5*k, 180, 360, 50)); f.pl(f.arc_pts(o[0]+5*k, o[1], 5*k, 5*k, 180, 360, 44))
    f.ln(*A, *D); f.ln(*H, *B); f.ra(H, B, A, 10)
    f.t(A[0], A[1]+18, "A"); f.t(H[0]-6, H[1]+18, "H"); f.t(C[0]+4, C[1]+18, "C"); f.t(D[0], D[1]+18, "D"); f.t(B[0]+12, B[1]-10, "B"); f.t(M[0]+14, M[1]-4, "M")

@fig("f3g_11")
def _(f):
    k = 26; o = (150, 200)
    P0 = lambda x, y: (o[0]+x*k, o[1]+y*k)
    O, R = P0(5, 1), math.sqrt(50)*k
    f.ci(*O, R); f.ln(*P0(-2, 0), *P0(12, 0)); f.ln(*P0(0, -4), *P0(0, 6)); f.ra(P0(0, 0), P0(-2, 0), P0(0, -4), 10); f.dot(*O)
    e = onc(O, R, 42); f.ln(*O, *e, 0, True); f.arrow(O, e)
    f.t(*P0(0.5, -2), "4"); f.t(*P0(-1, -0.5), "2"); f.t(*P0(0.5, 3), "6"); f.t(O[0]-8, O[1]-14, "O"); f.t((O[0]+e[0])/2+10, (O[1]+e[1])/2-12, "R")

@fig("f3g_12")
def _(f):
    k = 50; A = (60, 400); C = (60+6*k, 400)
    a, c = math.radians(70), math.radians(180-70-math.degrees(math.atan2(6, 4)))
    B = inter(A, (A[0]+math.cos(a), A[1]-math.sin(a)), C, (C[0]-math.cos(c), C[1]-math.sin(c)))
    Fb = foot(B, A, C); Fc = foot(C, A, B); H = inter(B, Fb, C, Fc)
    f.tri(A, B, C); f.ln(*B, *Fb); f.ln(*C, *Fc); f.ra(Fb, B, C, 10); f.ra(Fc, C, B, 10)
    f.tick((B[0]+14, B[1]), (H[0]+14, H[1]), 5); f.t(B[0]+28, (B[1]+H[1])/2, "4"); f.tick((A[0], A[1]+20), (C[0], C[1]+20), 5); f.t((A[0]+C[0])/2, A[1]+34, "6")
    f.t(B[0], B[1]-14, "B"); f.t(H[0]+14, H[1]+2, "H"); f.t(A[0]-12, A[1], "A"); f.t(C[0]+12, C[1], "C")

@fig("f3g_13")
def _(f):
    k = 90; o = (300, 300); s2 = math.sqrt(2)
    P0 = lambda x, y: (o[0]+x*k, o[1]+y*k)
    L, R, M, T, Sd = P0(-s2, 0), P0(s2, 0), P0(0, 0), P0(-s2/2, -s2/2), P0(s2, s2)
    c = P0(0, 1/s2); Rr = math.sqrt(2.5)*k
    f.ci(*c, Rr); f.ln(*L, *R); f.ln(*T, *Sd); f.ln(*L, *T); f.ra(T, L, M, 10); f.ang(L, R, T, 26)
    f.t(L[0]+38, L[1]-24, "x"); f.t((T[0]+M[0])/2+14, (T[1]+M[1])/2-8, "a"); f.t((L[0]+M[0])/2, L[1]+16, "b"); f.t((M[0]+R[0])/2, M[1]+16, "b")
    f.t((M[0]+Sd[0])/2+18, (M[1]+Sd[1])/2-12, "2a")

@fig("f3g_14")
def _(f):
    k = 30; o = (160, 260)
    P0 = lambda x, y: (o[0]+x*k, o[1]+y*k)
    A, H, C, B, M = P0(-4, 0), P0(0, 0), P0(9, 0), P0(0, -6), P0(0, -3)
    f.tri(A, B, C); f.ln(*B, *H); f.ln(*A, *M); f.ra(H, B, C, 10); f.ra(B, A, C, 12); f.ang(A, H, M, 34)
    f.t(B[0], B[1]-14, "B"); f.t(M[0]+14, M[1], "M"); f.t(H[0], H[1]+16, "H"); f.t(A[0]-8, A[1]+12, "A"); f.t(C[0]+8, C[1]+12, "C"); f.t(A[0]+48, A[1]-12, "x")

@fig("f3g_15")
def _(f):
    k = 30; o = (200, 300)
    P0 = lambda x, y: (o[0]+x*k, o[1]+y*k)
    R0, O, P = P0(0, 0), P0(0, -4), P0(-5, 0); r = 5*k
    d = math.dist(P, O); L = math.sqrt(d*d-r*r); base = math.atan2(O[1]-P[1], O[0]-P[0]); t = math.asin(r/d)
    A = (P[0]+L*math.cos(base-t), P[1]+L*math.sin(base-t))
    f.ci(*O, r); f.dot(*O); f.ln(*P, *R0); f.ln(*O, *R0); f.ra(R0, O, P, 10); f.ln(*P, A[0]+(A[0]-P[0])*0.3, A[1]+(A[1]-P[1])*0.3)
    nx, ny = -(A[1]-P[1])/L*14, (A[0]-P[0])/L*14
    f.tick((P[0]+nx, P[1]+ny), (A[0]+nx, A[1]+ny), 5); f.t((P[0]+A[0])/2+nx*2, (P[1]+A[1])/2+ny*2, "x")
    f.tick((P[0], P[1]+18), (P0(-3, 0)[0], P[1]+18), 5); f.tick((P0(-3, 0)[0], P[1]+18), (R0[0], P[1]+18), 5)
    f.t(P0(-4, 0)[0], P[1]+32, "2"); f.t(P0(-1.5, 0)[0], P[1]+32, "3"); f.t(A[0]+8, A[1]-14, "A"); f.t(O[0]+4, O[1]-16, "O")
