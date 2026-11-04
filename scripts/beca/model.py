"""Modelo de un problema de Beca 18 tal como se guarda en la app.
Reglas: el enunciado y las alternativas se copian completos y con las mismas palabras;
toda aclaración propia va en 'note' o 'warn', nunca dentro del enunciado.
Una línea del enunciado que empieza con § se dibuja como fórmula; su resto usa sintaxis de Giac."""

PROBS = {}

def P(id, topic, level, text, opts, vals, check, theory, idea, steps, verify, key=None, ok=None,
      trap="", trap_opt=None, mistake="", fig="", calc="", solver="", table=None, note="", warn="",
      letters="", year=0, src="oficial", group="", num=0, answer="", review=False, week=0, course="", answerVal="", also=()):
    letters = letters or "ABCDE"[:len(opts)]
    L = lambda c: letters.index(c) if c else -1
    p = dict(id=id, topic=topic, level=level, text=text, opts=opts, vals=vals, check=check,
             theory=theory, idea=idea, steps=steps, verify=verify, official=L(key),
             correct=L(ok or key), trap=trap, trapOpt=L(trap_opt), mistake=mistake, fig=fig,
             calc=calc, solver=solver, table=table or [], note=note, warn=warn,
             letters=letters if letters != "ABCDE"[:len(opts)] else "", year=year, src=src,
             group=group, num=num, answer=answer, review=review, week=week, course=course, answerVal=answerVal, alsoRight=list(also))
    assert id not in PROBS, id
    PROBS[id] = p
    return p

COURSES = {"geo": "Geometría", "alg": "Álgebra", "arit": "Aritmética", "est": "Estadística"}

def ficha(week, course, tag="", part=""):
    """Devuelve una función para registrar ejercicios de una ficha. id: s<semana><curso><tag>_<nn>.
    Si hay dos fichas del mismo curso y semana, tag y part las distinguen."""
    def F(num, topic, level, text, opts, vals, check, theory, idea, steps, verify, ok=None, **k):
        letters = k.pop("letters", "")
        return P("s%d%s%s_%02d" % (week, course, tag, num), topic, level, text, opts, vals, check, theory, idea, steps, verify,
                 key=None, ok=ok, src="ficha", group="Semana %d · %s" % (week, COURSES[course]) + (" · " + part if part else ""), num=num,
                 week=week, course=course, letters=letters, **k)
    return F
