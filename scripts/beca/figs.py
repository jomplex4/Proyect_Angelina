"""Figuras de Beca 18 como datos. Coordenadas en píxeles de la página original (200 ppp);
el generador las escala a la caja de 100 x 60 que usa la app. Colores: 0 texto, 1 rojo,
2 azul, 3 violeta, 4 naranja, 5 gris, 6 fondo, 7 sombreado."""
import math

class F:
    def __init__(self): self.p = []
    # primitivas básicas
    def ln(self, x1, y1, x2, y2, c=0, dash=False, w=1.0): self.p.append(('Ln', x1, y1, x2, y2, c, dash, w)); return self
    def pl(self, pts, c=0, dash=False, w=1.0):
        for a, b in zip(pts, pts[1:]): self.ln(a[0], a[1], b[0], b[1], c, dash, w)
        return self
    def pg(self, pts, c=0, fill=False, dash=False): self.p.append(('Pg', [v for q in pts for v in q], c, fill, dash)); return self
    def ci(self, x, y, r, c=0, dash=False): self.p.append(('Ci', x, y, r, c, dash)); return self
    def disc(self, x, y, r, c=0): self.p.append(('Ar', x, y, r, 0.0, 360.0, c, True)); return self
    def el(self, x, y, rx, ry, c=0, back=False): self.p.append(('El', x, y, rx, ry, c, back)); return self
    def ar(self, x, y, r, a0, sw, c=2, wedge=False): self.p.append(('Ar', x, y, r, a0, sw, c, wedge)); return self
    def t(self, x, y, s, c=0, size=1.0): self.p.append(('Tx', x, y, s, c, size)); return self
    def dot(self, x, y, c=0): self.p.append(('Dt', x, y, c)); return self
    # ayudas
    def tri(self, a, b, c, col=0): return self.pg([a, b, c], col)
    def ang(self, v, p, q, r=12, c=2):
        a0 = math.degrees(math.atan2(p[1]-v[1], p[0]-v[0])); a1 = math.degrees(math.atan2(q[1]-v[1], q[0]-v[0]))
        sw = a1 - a0
        while sw > 180: sw -= 360
        while sw < -180: sw += 360
        return self.ar(v[0], v[1], r, a0, sw, c)
    def ra(self, v, p, q, s=10, c=5):
        def u(a):
            d = math.hypot(a[0]-v[0], a[1]-v[1]); return ((a[0]-v[0])/d*s, (a[1]-v[1])/d*s)
        (ux, uy), (wx, wy) = u(p), u(q)
        return self.pl([(v[0]+ux, v[1]+uy), (v[0]+ux+wx, v[1]+uy+wy), (v[0]+wx, v[1]+wy)], c)
    def arc_pts(self, cx, cy, rx, ry, a0, a1, n=16):
        return [(cx+rx*math.cos(math.radians(a0+(a1-a0)*i/n)), cy+ry*math.sin(math.radians(a0+(a1-a0)*i/n))) for i in range(n+1)]
    def bez(self, p0, p1, p2, n=14):
        return [((1-t)**2*p0[0]+2*(1-t)*t*p1[0]+t*t*p2[0], (1-t)**2*p0[1]+2*(1-t)*t*p1[1]+t*t*p2[1]) for t in [i/n for i in range(n+1)]]
    def tick(self, a, b, L=6, c=0):
        """Cota: segmento a-b con marcas perpendiculares en los extremos."""
        dx, dy = b[0]-a[0], b[1]-a[1]; d = math.hypot(dx, dy); nx, ny = -dy/d*L, dx/d*L
        self.ln(a[0], a[1], b[0], b[1], c)
        for q in (a, b): self.ln(q[0]-nx, q[1]-ny, q[0]+nx, q[1]+ny, c)
        return self
    def arrow(self, a, b, c=0, L=9):
        """Flecha en b apuntando desde a."""
        dx, dy = b[0]-a[0], b[1]-a[1]; d = math.hypot(dx, dy); ux, uy = dx/d, dy/d
        for s in (1, -1):
            self.ln(b[0], b[1], b[0]-ux*L - s*uy*L*0.55, b[1]-uy*L + s*ux*L*0.55, c)
        return self
    def cyl(self, x, top, bot, rx, ry, c=0):
        self.pg(self.arc_pts(x, top, rx, ry, 180, 360) + self.arc_pts(x, bot, rx, ry, 0, 180)[::-1][::-1], 6, True)
        self.el(x, top, rx, ry, c); self.ln(x-rx, top, x-rx, bot, c); self.ln(x+rx, top, x+rx, bot, c)
        return self.pl(self.arc_pts(x, bot, rx, ry, 0, 180), c)
    def cube(self, x0, y0, s, d, c=0, fill=5):
        self.pg([(x0, y0), (x0+s, y0), (x0+s, y0+s), (x0, y0+s)], fill, True)
        self.pg([(x0, y0), (x0+d, y0-d), (x0+s+d, y0-d), (x0+s, y0)], fill, True)
        self.pg([(x0+s, y0), (x0+s+d, y0-d), (x0+s+d, y0+s-d), (x0+s, y0+s)], fill, True)
        self.pg([(x0, y0), (x0+s, y0), (x0+s, y0+s), (x0, y0+s)], c)
        self.pl([(x0, y0), (x0+d, y0-d), (x0+s+d, y0-d), (x0+s+d, y0+s-d), (x0+s, y0+s)], c)
        return self.ln(x0+s, y0, x0+s+d, y0-d, c)

FIGS = {}
HEIGHT = {}   # alto de la caja si no es 60; el ancho siempre es 100
def fig(name, h=60):
    def deco(fn):
        f = F(); fn(f); FIGS[name] = f.p
        if h != 60: HEIGHT[name] = h
        return fn
    return deco
