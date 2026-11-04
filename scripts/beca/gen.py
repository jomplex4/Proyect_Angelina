"""Genera los archivos Kotlin de Beca 18 a partir de los datos en Python.
Uso, desde la raíz del proyecto:  python3 scripts/beca/gen.py
Escribe app/src/main/java/com/digitalminds/angelina/BecaOficialGen.kt y BecaFigsGen.kt."""
import os, sys
HERE = os.path.dirname(os.path.abspath(__file__)); sys.path.insert(0, HERE)
from model import PROBS
from figs import FIGS, HEIGHT
from figgen import write_kotlin, kstr
import figs_2023, figs_2024, figs_2025, oficial_2023_a, oficial_2023_b, oficial_2024_a, oficial_2024_b, oficial_2025, figs_s1geo, ficha_s1_geo, figs_s2geo, ficha_s2_geo, figs_s3geo, ficha_s3_geo, figs_s4geo, ficha_s4_geo, ficha_s1_alg, figs_s2alg, ficha_s2_alg, ficha_s4_alg, ficha_s1_arit, ficha_s2_arit, ficha_s3_arit, ficha_s4_arit, figs_s1est, figs_s4est, ficha_s1_est, ficha_s2_est, ficha_s3_est, ficha_s4_est  # noqa: F401  registran datos

OUT = os.path.join(HERE, "..", "..", "app", "src", "main", "java", "com", "digitalminds", "angelina")

def klist(xs): return "listOf(" + ", ".join(kstr(x) for x in xs) + ")" if xs else "emptyList()"

# Casos que la academia debe revisar aunque su enunciado no tenga un aviso visible: la lista final sale de aquí.
FORCE_REVIEW = ["e23_27", "e23_29", "e24_24", "e25_18", "e25_19", "s2geo_22", "s4geo_28", "s3arit_28", "s4arit_04", "s4arit_23", "s4algb_18"]
for _i in FORCE_REVIEW:
    if _i in PROBS: PROBS[_i]["review"] = True

def kprob(p):
    a = ["id = %s" % kstr(p["id"]), "topic = %s" % kstr(p["topic"]), "level = %d" % p["level"], "text = %s" % kstr(p["text"]),
         "opts = %s" % klist(p["opts"]), "vals = %s" % klist(p["vals"]), "correct = %d" % p["correct"], "check = %s" % kstr(p["check"]),
         "steps = %s" % klist(p["steps"])]
    opt = [("fig", p["fig"]), ("calc", p["calc"]), ("solver", p["solver"]), ("note", p["note"]), ("theory", p["theory"]),
           ("idea", p["idea"]), ("mistake", p["mistake"]), ("verify", p["verify"]), ("warn", p["warn"]), ("trap", p["trap"]),
           ("letters", p["letters"]), ("src", p["src"]), ("group", p["group"]), ("answer", p["answer"]), ("answerVal", p["answerVal"])]
    a += ["%s = %s" % (k, kstr(v)) for k, v in opt if v]
    if p["table"]: a.append("table = listOf(" + ", ".join(klist(r) for r in p["table"]) + ")")
    for k in ("year", "num"):
        if p[k]: a.append("%s = %d" % (k, p[k]))
    for k in ("trapOpt", "official"):
        if p[k] >= 0: a.append("%s = %d" % (k, p[k]))
    if p["alsoRight"]: a.append("alsoRight = listOf(%s)" % ", ".join(str(i) for i in p["alsoRight"]))
    if p["review"]: a.append("review = true")
    return "BProb(" + ", ".join(a) + ")"

def write_probs(name, probs, path, doc):
    L = ["package com.digitalminds.angelina", "", "// Archivo generado por scripts/beca/gen.py. No editar a mano.", "",
         "/** %s */" % doc, "internal object %s {" % name]
    chunks = [probs[i:i+8] for i in range(0, len(probs), 8)]
    L.append("    val list: List<BProb> = ArrayList<BProb>().also { l -> " + "; ".join("p%d(l)" % i for i in range(len(chunks))) + " }")
    for i, ch in enumerate(chunks):
        L.append("    private fun p%d(l: MutableList<BProb>) {" % i)
        L += ["        l.add(" + kprob(p) + ")" for p in ch]
        L.append("    }")
    L.append("}")
    open(path, "w").write("\n".join(L) + "\n")

if __name__ == "__main__":
    of = sorted([p for p in PROBS.values() if p["src"] == "oficial"], key=lambda p: (p["year"], p["num"]))
    write_probs("BecaOficial", of, os.path.join(OUT, "BecaOficialGen.kt"),
                "Preguntas de matemática de los exámenes oficiales, transcritas completas desde la imagen de cada página.")
    order = {"geo": 0, "alg": 1, "arit": 2, "est": 3}
    fi = sorted([p for p in PROBS.values() if p["src"] == "ficha"], key=lambda p: (p["week"], order[p["course"]], p["group"], p["num"]))
    write_probs("BecaFichas", fi, os.path.join(OUT, "BecaFichasGen.kt"),
                "Ejercicios de las fichas de la academia, por semana y curso. Enunciados tal cual; las erratas llevan aviso.")
    print("fichas:", len(fi))
    write_kotlin(FIGS, os.path.join(OUT, "BecaFigsGen.kt"), HEIGHT)
    print("oficiales:", len(of), " figuras:", len(FIGS))
