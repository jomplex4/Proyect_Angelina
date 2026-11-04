"""Figura de la ficha de Álgebra, semana 2, problema Ahora hazlo tú 5: triángulo rectángulo con catetos a y b e hipotenusa c."""
from figs import fig

@fig("fa2_29")
def _(f):
    L, R, T = (315, 1275), (470, 1275), (470, 1152)
    f.tri(L, R, T); f.ra(R, L, T, 14)
    f.t(372, 1196, "c"); f.t(492, 1213, "b"); f.t(392, 1298, "a")
