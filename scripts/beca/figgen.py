"""Escala las figuras a la caja 100 x 60, escribe BecaFigs.kt y dibuja vistas previas con la misma lógica de FigureView."""
import math, os
from PIL import Image, ImageDraw, ImageFont

COL = {0: (255, 255, 255), 1: (255, 42, 31), 2: (91, 155, 255), 3: (167, 139, 250), 4: (255, 154, 77), 5: (154, 156, 164), 6: (21, 21, 23), 7: (58, 60, 68)}
BG = (21, 21, 23)

def bbox(p):
    xs, ys = [], []
    for q in p:
        k = q[0]
        if k == 'Ln': xs += [q[1], q[3]]; ys += [q[2], q[4]]
        elif k == 'Pg': xs += q[1][0::2]; ys += q[1][1::2]
        elif k == 'Ci': xs += [q[1]-q[3], q[1]+q[3]]; ys += [q[2]-q[3], q[2]+q[3]]
        elif k == 'El': xs += [q[1]-q[3], q[1]+q[3]]; ys += [q[2]-q[4], q[2]+q[4]]
        elif k == 'Ar':
            if q[7] or abs(q[5]) >= 359: xs += [q[1]-q[3], q[1]+q[3]]; ys += [q[2]-q[3], q[2]+q[3]]
            else: xs.append(q[1]); ys.append(q[2])
        elif k == 'Tx': w = 6*len(q[3])*q[5]; xs += [q[1]-w, q[1]+w]; ys += [q[2]-11*q[5], q[2]+11*q[5]]
        elif k == 'Dt': xs.append(q[1]); ys.append(q[2])
    return min(xs), min(ys), max(xs), max(ys)

def scale(p, W=100.0, H=60.0, m=3.0):
    x0, y0, x1, y1 = bbox(p)
    s = min((W-2*m)/(x1-x0), (H-2*m)/(y1-y0))
    ox = (W - (x1-x0)*s)/2 - x0*s; oy = (H - (y1-y0)*s)/2 - y0*s
    X = lambda x: x*s+ox; Y = lambda y: y*s+oy
    out = []
    for q in p:
        k = q[0]
        if k == 'Ln': out.append(('Ln', X(q[1]), Y(q[2]), X(q[3]), Y(q[4]), *q[5:]))
        elif k == 'Pg': out.append(('Pg', [X(v) if i % 2 == 0 else Y(v) for i, v in enumerate(q[1])], *q[2:]))
        elif k == 'Ci': out.append(('Ci', X(q[1]), Y(q[2]), q[3]*s, *q[4:]))
        elif k == 'El': out.append(('El', X(q[1]), Y(q[2]), q[3]*s, q[4]*s, *q[5:]))
        elif k == 'Ar': out.append(('Ar', X(q[1]), Y(q[2]), q[3]*s, *q[4:]))
        elif k == 'Tx': out.append(('Tx', X(q[1]), Y(q[2]), *q[3:]))
        elif k == 'Dt': out.append(('Dt', X(q[1]), Y(q[2]), *q[3:]))
    return out

def f(v): return ("%.2f" % v).rstrip('0').rstrip('.') + "f"
def b(v): return "true" if v else "false"
def kstr(s): return '"' + s.replace('\\', '\\\\').replace('"', '\\"').replace('$', '\\$').replace('\n', '\\n') + '"'

def kt(q):
    k = q[0]
    if k == 'Ln': return "Ln(%s, %s, %s, %s, %d, %s, %s)" % (f(q[1]), f(q[2]), f(q[3]), f(q[4]), q[5], b(q[6]), f(q[7]))
    if k == 'Pg': return "Pg(listOf(%s), %d, %s, %s)" % (", ".join(f(v) for v in q[1]), q[2], b(q[3]), b(q[4]))
    if k == 'Ci': return "Ci(%s, %s, %s, %d, %s)" % (f(q[1]), f(q[2]), f(q[3]), q[4], b(q[5]))
    if k == 'El': return "El(%s, %s, %s, %s, %d, %s)" % (f(q[1]), f(q[2]), f(q[3]), f(q[4]), q[5], b(q[6]))
    if k == 'Ar': return "Ar(%s, %s, %s, %s, %s, %d, %s)" % (f(q[1]), f(q[2]), f(q[3]), f(q[4]), f(q[5]), q[6], b(q[7]))
    if k == 'Tx': return "Tx(%s, %s, %s, %d, %s)" % (f(q[1]), f(q[2]), kstr(q[3]), q[4], f(q[5]))
    if k == 'Dt': return "Dt(%s, %s, %d)" % (f(q[1]), f(q[2]), q[3])

def write_kotlin(figs, path, heights=None):
    heights = heights or {}
    names = sorted(figs)
    L = ["package com.digitalminds.angelina", "", "// Archivo generado por scripts/beca/gen.py. No editar a mano.", "",
         "/** Figuras redibujadas de los exámenes oficiales y de las fichas de Beca 18, en la caja de 100 x 60. */",
         "internal object BecaFigs {"]
    L.append("    val map: Map<String, () -> List<Pr>> = mapOf(")
    L += ["        %s to ::f%d," % (kstr(n), i) for i, n in enumerate(names)]
    L.append("    )")
    L.append("    /** Alto de la caja de las figuras que no miden 100 x 60. */")
    L.append("    val height: Map<String, Float> = mapOf(" + ", ".join("%s to %s" % (kstr(n), f(heights[n])) for n in sorted(heights)) + ")")
    for i, n in enumerate(names):
        prims = scale(figs[n], H=heights.get(n, 60.0))
        L.append("    private fun f%d(): List<Pr> = listOf(" % i)
        L += ["        " + kt(q) + "," for q in prims]
        L.append("    )")
    L.append("}")
    open(path, "w").write("\n".join(L) + "\n")

def render(prims, out, S=8, H=60):
    im = Image.new('RGB', (100*S, int(H*S)), BG); d = ImageDraw.Draw(im, 'RGBA')
    font = lambda sz: ImageFont.truetype('/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf', max(8, int(3.4*sz*S)))
    sw = max(2, int(S*0.38))
    P = lambda x, y: (x*S, y*S)
    def col(c, a=255): return COL[c] + (a,)
    for q in prims:
        k = q[0]
        if k == 'Ln':
            w = max(1, int(sw*q[7]))
            if q[6]:
                x1, y1, x2, y2 = q[1:5]; L = math.hypot(x2-x1, y2-y1); n = max(1, int(L/2.8))
                for i in range(n):
                    a, c2 = i/n, (i+0.57)/n
                    d.line([P(x1+(x2-x1)*a, y1+(y2-y1)*a), P(x1+(x2-x1)*c2, y1+(y2-y1)*c2)], fill=col(q[5]), width=w)
            else: d.line([P(q[1], q[2]), P(q[3], q[4])], fill=col(q[5]), width=w)
        elif k == 'Pg':
            pts = [P(q[1][i], q[1][i+1]) for i in range(0, len(q[1]), 2)]
            if q[3]: d.polygon(pts, fill=col(q[2], 255 if q[2] in (6, 7) else 217))
            else: d.line(pts + [pts[0]], fill=col(q[2]), width=sw)
        elif k == 'Ci':
            x, y = P(q[1], q[2]); r = q[3]*S; d.ellipse([x-r, y-r, x+r, y+r], outline=col(q[4]), width=sw)
        elif k == 'El':
            x, y = P(q[1], q[2]); rx, ry = q[3]*S, q[4]*S; box = [x-rx, y-ry, x+rx, y+ry]
            if q[6]: d.arc(box, 180, 360, fill=col(5), width=sw); d.arc(box, 0, 180, fill=col(q[5]), width=sw)
            else: d.ellipse(box, outline=col(q[5]), width=sw)
        elif k == 'Ar':
            x, y = P(q[1], q[2]); r = q[3]*S; box = [x-r, y-r, x+r, y+r]
            a0, swp = q[4], q[5]; s0, s1 = (a0, a0+swp) if swp >= 0 else (a0+swp, a0)
            if q[7]: d.pieslice(box, s0, s1, fill=col(q[6], 255 if q[6] in (6, 7) else 217))
            else: d.arc(box, s0, s1, fill=col(q[6]), width=sw)
        elif k == 'Tx':
            d.text(P(q[1], q[2]), q[3], fill=col(q[4]), font=font(q[5]), anchor='mm')
        elif k == 'Dt':
            x, y = P(q[1], q[2]); r = max(3, S*0.9); d.ellipse([x-r, y-r, x+r, y+r], fill=col(q[3]))
    im.save(out)

def sheet(figs, out, cols=3, S=6, heights=None):
    heights = heights or {}
    names = sorted(figs); rows = (len(names)+cols-1)//cols; HM = max([60] + list(heights.values()))
    im = Image.new('RGB', (cols*100*S+(cols+1)*10, rows*(HM*S+30)+10), (60, 60, 60)); d = ImageDraw.Draw(im)
    fnt = ImageFont.truetype('/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf', 16)
    for i, n in enumerate(names):
        H = heights.get(n, 60)
        render(scale(figs[n], H=H), '/tmp/_f.png', S, H); t = Image.open('/tmp/_f.png')
        x = 10 + (i % cols)*(100*S+10); y = 10 + (i//cols)*(HM*S+30)
        d.text((x, y), n, fill='yellow', font=fnt); im.paste(t, (x, y+20))
    im.save(out)
