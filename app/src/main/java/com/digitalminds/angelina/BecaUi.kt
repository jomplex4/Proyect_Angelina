package com.digitalminds.angelina

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val ok = Color(0xFF3DDC97)
private fun mmss(s: Long) = "%d:%02d".format(s / 60, s % 60)
private fun stars(l: Int) = "★".repeat(l) + "☆".repeat(3 - l)

/** Una línea de enunciado o alternativa: si empieza con § se dibuja como fórmula; si no se puede leer, como texto. */
@Composable
private fun RichLine(s: String, fs: Int = 17, modifier: Modifier = Modifier) {
    val row = remember(s) { if (s.startsWith("§")) Parse.row(s.drop(1)) else null }
    if (row != null) Box(modifier.horizontalScroll(rememberScrollState())) { MathView(row, fs.toFloat()) }
    else BasicText(s.removePrefix("§"), modifier, style = TextStyle(color = Brand.text, fontFamily = Brand.math, fontSize = fs.sp))
}

/** Enunciado completo: texto, fórmulas y la tabla en el lugar marcado con [tabla]. */
@Composable
private fun Statement(p: BProb) {
    val lines = p.text.split("\n")
    for (l in lines) when {
        l == "[tabla]" -> if (p.table.isNotEmpty()) TableView(p.table)
        l.isNotBlank() -> RichLine(l)
    }
    if (p.table.isNotEmpty() && "[tabla]" !in lines) TableView(p.table)
}

@Composable
private fun BecaSection(title: String, body: String, color: Color = Brand.text) {
    if (body.isEmpty()) return
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        BasicText(title, style = label(13, Brand.textDim, true))
        BasicText(body, style = label(14, color))
    }
}

private fun origin(p: BProb): String = when (p.src) {
    "oficial" -> "Examen oficial ${p.year}" + (if (p.num > 0) " · pregunta ${p.num}" else "")
    "ficha" -> "Ficha " + p.group + (if (p.num > 0) " · pregunta ${p.num}" else "")
    else -> if (p.year > 0) "Examen oficial ${p.year}" else "Problema propio"
}

/** Beca 18: inicio, pregunta (práctica o simulacro) y resultados. */
@Composable
fun BecaScreen(vm: MainViewModel) {
    val run by vm.becaRun.collectAsState()
    val r = run
    when {
        r == null -> BecaHome(vm)
        r.results -> BecaResults(vm, r)
        else -> BecaQuestion(vm, r)
    }
}

@Composable
private fun Bar(frac: Float, color: Color = Brand.accentHi) {
    Box(Modifier.fillMaxWidth().height(6.dp).background(Brand.keyOp, RoundedCornerShape(3.dp))) {
        Box(Modifier.fillMaxWidth(frac.coerceIn(0f, 1f)).height(6.dp).background(color, RoundedCornerShape(3.dp)))
    }
}

@Composable
private fun BecaHome(vm: MainViewModel) {
    val st by vm.becaStats.collectAsState()
    var limit by remember { mutableIntStateOf(1) }
    var level by remember { mutableIntStateOf(0) }
    val limits = listOf(0, 45, 60, 90)
    val every = Beca.all + Beca.exams + Beca.fichas
    val total = every.size.coerceAtLeast(1)
    val done = every.count { (st.rec[it.id]?.tries ?: 0) > 0 }
    val good = every.count { st.rec[it.id]?.last == true }
    val wrong = st.rec.count { !it.value.last && Beca.byId(it.key) != null }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        BasicText("Razonamiento Matemático", style = label(20, Brand.text, true))
        BasicText("${Beca.all.size} problemas propios, ${Beca.exams.size} preguntas de exámenes oficiales y ${Beca.fichas.size} ejercicios de las fichas de la academia, con solución explicada.", style = label(13, Brand.textDim))
        Column(Modifier.fillMaxWidth().background(Brand.surface, RoundedCornerShape(14.dp)).padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row { BasicText("Resueltos: $done de $total", Modifier.weight(1f), style = label(14, Brand.text)); BasicText("Bien la última vez: $good", style = label(14, ok)) }
            Bar(done / total.toFloat())
        }
        Column(Modifier.fillMaxWidth().background(Brand.surface, RoundedCornerShape(14.dp)).border(1.dp, Brand.accent, RoundedCornerShape(14.dp)).padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            BasicText("Simulacro", style = label(17, Brand.text, true))
            BasicText("${Beca.SIM_SIZE} preguntas de todos los temas (propias, oficiales y de las fichas), en orden aleatorio. Se corrige al terminar.", style = label(13, Brand.textDim))
            Chips(listOf("Mixto", "Nivel 1 ★", "Nivel 2 ★★", "Nivel 3 ★★★"), level) { level = it }
            Chips(limits.map { if (it == 0) "Sin límite" else "$it min" }, limit) { limit = it }
            Pill("Empezar simulacro", Modifier.fillMaxWidth(), bg = Brand.accent) { vm.becaSim(limits[limit], level) }
        }
        Caption("Exámenes oficiales anteriores (solo matemática)")
        for (y in Beca.years) {
            val ps = Beca.exam(y)
            val g = ps.count { st.rec[it.id]?.last == true }
            Column(Modifier.fillMaxWidth().background(Brand.surface, RoundedCornerShape(12.dp)).padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row { BasicText("Examen $y", Modifier.weight(1f), style = label(16, Brand.text, true)); BasicText("$g / ${ps.size} bien", style = label(13, Brand.textDim)) }
                Bar(g / ps.size.toFloat(), ok)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Pill("Practicar", Modifier.weight(1f)) { vm.becaExam(y, false, 0) }
                    Pill("Como examen", Modifier.weight(1f), bg = Brand.keyOp, fg = Brand.orange) { vm.becaExam(y, true, limits[limit]) }
                }
            }
        }
        BasicText("Claves oficiales verificadas una por una. Donde el enunciado oficial tiene un problema, la solución lo advierte.", style = label(11, Brand.textDim))
        if (Beca.fichaGroups.isNotEmpty()) {
            Caption("Fichas de la academia, por semana y curso")
            for (gr in Beca.fichaGroups) {
                val ps = Beca.ofFicha(gr)
                val g = ps.count { st.rec[it.id]?.last == true }
                Column(
                    Modifier.fillMaxWidth().background(Brand.surface, RoundedCornerShape(12.dp)).pointerInput(gr) { detectTapGestures { vm.becaFicha(gr) } }.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row { BasicText(gr, Modifier.weight(1f), style = label(15, Brand.text, true)); BasicText("$g / ${ps.size}", style = label(13, Brand.textDim)) }
                    Bar(if (ps.isEmpty()) 0f else g / ps.size.toFloat(), ok)
                }
            }
            BasicText("Los enunciados se muestran tal como están en la ficha. Si una ficha trae un error, la pregunta lo advierte y muestra aparte la respuesta correcta.", style = label(11, Brand.textDim))
        }
        if (wrong > 0) Pill("Repasar mis errores ($wrong)", Modifier.fillMaxWidth(), bg = Brand.keyOp, fg = Brand.orange) { vm.becaMistakes() }
        Caption("Practicar por tema (se corrige al instante)")
        for (t in Beca.topics) {
            val ps = Beca.ofTopic(t.id)
            val g = ps.count { st.rec[it.id]?.last == true }
            Column(
                Modifier.fillMaxWidth().background(Brand.surface, RoundedCornerShape(12.dp)).pointerInput(t.id) { detectTapGestures { vm.becaTopic(t.id) } }.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row { BasicText(t.name, Modifier.weight(1f), style = label(15, Brand.text, true)); BasicText("$g / ${ps.size}", style = label(13, Brand.textDim)) }
                Bar(if (ps.isEmpty()) 0f else g / ps.size.toFloat(), ok)
            }
        }
        if (st.sims.isNotEmpty()) {
            Caption("Tus simulacros")
            val fmt = remember { SimpleDateFormat("dd/MM HH:mm", Locale("es", "PE")) }
            for (s in st.sims.takeLast(8).asReversed()) Row(Modifier.fillMaxWidth().background(Brand.surface, RoundedCornerShape(10.dp)).padding(10.dp)) {
                BasicText(fmt.format(Date(s.time)), Modifier.weight(1f), style = label(13, Brand.textDim))
                BasicText("${s.score} / ${s.total}", Modifier.width(70.dp), style = label(14, Brand.text, true))
                BasicText(mmss(s.secs.toLong()), style = label(13, Brand.textDim))
            }
        }
        Pill("Borrar mi progreso", bg = Brand.keyNum, fg = Brand.textDim) { vm.becaResetProgress() }
        BasicText("Los problemas son originales y siguen el estilo de los temarios de preparación; el examen oficial puede variar.", style = label(11, Brand.textDim))
        Spacer(Modifier.height(10.dp))
    }
}

@Composable
private fun BecaQuestion(vm: MainViewModel, r: BecaRun) {
    val p = r.ids.getOrNull(r.cur)?.let { Beca.byId(it) } ?: return
    val chosen = r.answers[p.id]
    val reveal = r.finished || (!r.sim && chosen != null)
    var now by remember { mutableLongStateOf(System.currentTimeMillis()) }
    if (r.sim && !r.finished) LaunchedEffect(r.startMs) {
        while (true) {
            delay(1000); now = System.currentTimeMillis()
            if (r.limitMin > 0 && now - r.startMs >= r.limitMin * 60_000L) { vm.becaFinish(); break }
        }
    }
    val scroll = rememberScrollState()
    LaunchedEffect(r.cur) { scroll.scrollTo(0) }
    Column(Modifier.fillMaxSize().verticalScroll(scroll).padding(horizontal = 12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Pill("←", bg = Brand.keyNum) { vm.becaBack() }
            Column(Modifier.weight(1f)) {
                BasicText("${r.title} · ${r.cur + 1} de ${r.ids.size}", style = label(14, Brand.text, true))
                BasicText(origin(p) + " · " + Beca.topicName(p.topic) + "  " + stars(p.level), style = label(12, Brand.textDim))
            }
            if (r.sim && !r.finished) {
                val el = (now - r.startMs) / 1000
                val t = if (r.limitMin > 0) maxOf(0L, r.limitMin * 60L - el) else el
                BasicText((if (r.limitMin > 0) "⏳ " else "⏱ ") + mmss(t), style = label(15, if (r.limitMin > 0 && t < 300) Brand.error else Brand.orange, true))
            }
        }
        Statement(p)
        if (p.fig.isNotEmpty()) Box(Modifier.fillMaxWidth().background(Brand.surface, RoundedCornerShape(10.dp)).padding(6.dp)) { FigureView(p.fig) }
        if (p.warn.isNotEmpty()) BasicText("⚠ " + p.warn, Modifier.fillMaxWidth().background(Brand.surface, RoundedCornerShape(10.dp)).border(1.dp, Brand.orange, RoundedCornerShape(10.dp)).padding(10.dp), style = label(13, Brand.orange))
        if (p.opts.isEmpty() && !r.finished) {
            if (chosen == null) Pill("Ver la solución", Modifier.fillMaxWidth(), bg = Brand.accent) { vm.becaReveal() }
            else if (chosen < 0) Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Pill("Lo resolví bien", Modifier.weight(1f), bg = Brand.keyOp, fg = ok) { vm.becaSelf(true) }
                Pill("Me equivoqué", Modifier.weight(1f), bg = Brand.keyOp, fg = Brand.error) { vm.becaSelf(false) }
            }
        }
        for (i in p.opts.indices) {
            val isC = p.isRight(i)
            val border = when {
                reveal && isC -> ok
                reveal && chosen == i -> Brand.error
                chosen == i -> Brand.violet
                else -> Brand.line
            }
            Row(
                Modifier.fillMaxWidth().background(if (chosen == i) Brand.keyOp else Brand.surface, RoundedCornerShape(12.dp))
                    .border(if (border == Brand.line) 1.dp else 2.dp, border, RoundedCornerShape(12.dp))
                    .pointerInput(p.id, i, r.finished) { detectTapGestures { vm.becaAnswer(i) } }.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(Modifier.size(28.dp).background(if (border == Brand.line) Brand.keyOp else border, CircleShape), contentAlignment = Alignment.Center) {
                    BasicText(p.letter(i), style = label(14, if (border == Brand.line) Brand.text else Brand.bg, true))
                }
                RichLine(p.opts[i], 17, Modifier.weight(1f))
                if (reveal && isC) BasicText("✓", style = label(18, ok, true))
                if (reveal && chosen == i && !isC) BasicText("✗", style = label(18, Brand.error, true))
            }
        }
        if (reveal) {
            val msg = when {
                p.opts.isEmpty() -> when (chosen) { 1 -> "Bien resuelto"; 2 -> "Revisa la solución paso a paso"; else -> "Solución" }
                p.correct < 0 -> "Ninguna alternativa es correcta. Lee el aviso."
                chosen == null -> "No respondiste. La respuesta es ${p.letter(p.correct)}."
                p.isRight(chosen) -> "¡Correcto!"
                else -> "Incorrecto. La respuesta es ${p.letter(p.correct)}."
            }
            BasicText(msg, style = label(16, if ((p.opts.isNotEmpty() && p.isRight(chosen)) || (p.opts.isEmpty() && chosen != 2)) ok else Brand.error, true))
            Column(Modifier.fillMaxWidth().background(Brand.surface, RoundedCornerShape(12.dp)).padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (p.official >= 0) BasicText("Clave oficial: ${p.letter(p.official)}" + (if (p.correct >= 0) " · Respuesta calculada: ${p.letter(p.correct)}" else ""), style = label(13, Brand.textDim, true))
                if (p.answer.isNotEmpty()) BasicText("Respuesta correcta calculada: " + p.answer, style = label(15, ok, true))
                BecaSection("Base teórica", p.theory)
                BecaSection("Idea clave", p.idea, Brand.blue)
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    BasicText(if (p.theory.isEmpty()) "Solución" else "Resolución", style = label(13, Brand.textDim, true))
                    p.steps.forEachIndexed { k, st -> BasicText("${k + 1}. $st", style = label(14, Brand.text)) }
                }
                BecaSection("Error común", p.mistake, Brand.orange)
                BecaSection("Comprobación", p.verify, ok)
                if (p.note.isNotEmpty()) BasicText("Nota: " + p.note, style = label(13, Brand.orange))
            }
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (p.calc.isNotEmpty()) Pill("Probar en la calculadora", bg = Brand.keyOp, fg = Brand.blue) { vm.sendExpr(p.calc, true) }
                if (p.solver.isNotEmpty()) Pill("Paso a paso", bg = Brand.keyOp, fg = Brand.violet) { vm.openSolver(p.solver) }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Pill("Anterior", Modifier.weight(1f)) { vm.becaGo(r.cur - 1) }
            if (r.cur < r.ids.lastIndex) Pill("Siguiente", Modifier.weight(1f), bg = if (reveal || r.sim) Brand.accent else Brand.keyOp) { vm.becaGo(r.cur + 1) }
            else Pill(if (r.finished) "Ver resultados" else "Terminar", Modifier.weight(1f), bg = Brand.accent) { vm.becaFinish() }
        }
        if (r.sim) {
            Caption(if (r.finished) "Toca un número para revisar" else "Respondidas: ${r.answers.size} de ${r.ids.size}")
            for (row in r.ids.indices.chunked(10)) Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                for (k in row) {
                    val id = r.ids[k]; val a = r.answers[id]; val good = Beca.byId(id)?.isRight(a) == true
                    val bg = when {
                        r.finished && good -> ok
                        r.finished -> Brand.error
                        a != null -> Brand.violet
                        else -> Brand.keyOp
                    }
                    Box(
                        Modifier.size(30.dp).background(bg.copy(alpha = if (k == r.cur) 1f else 0.55f), RoundedCornerShape(7.dp))
                            .border(if (k == r.cur) 2.dp else 0.dp, Brand.text, RoundedCornerShape(7.dp))
                            .pointerInput(k) { detectTapGestures { vm.becaGo(k) } },
                        contentAlignment = Alignment.Center
                    ) { BasicText("${k + 1}", style = label(11, Brand.text, true)) }
                }
            }
            if (!r.finished) Pill("Terminar y corregir", Modifier.fillMaxWidth(), bg = Brand.accent) { vm.becaFinish() }
        }
        Spacer(Modifier.height(10.dp))
    }
}

@Composable
private fun BecaResults(vm: MainViewModel, r: BecaRun) {
    val ps = r.ids.mapNotNull { Beca.byId(it) }
    val score = ps.count { it.isRight(r.answers[it.id]) }
    val pct = if (ps.isEmpty()) 0 else score * 100 / ps.size
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Pill("←", bg = Brand.keyNum) { vm.becaBack() }
            BasicText("Resultados: " + r.title, style = label(17, Brand.text, true))
        }
        Column(Modifier.fillMaxWidth().background(Brand.surface, RoundedCornerShape(16.dp)).padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
            BasicText("$score / ${ps.size}", style = label(44, if (pct >= 60) ok else Brand.orange, true))
            BasicText("$pct% de aciertos" + if (r.sim) " · tiempo ${mmss(r.secs.toLong())}" else "", style = label(14, Brand.textDim))
            BasicText(when {
                pct >= 85 -> "¡Excelente! Estás muy bien preparado."
                pct >= 60 -> "Buen trabajo. Repasa los temas con menos aciertos."
                else -> "Sigue practicando: revisa cada solución y vuelve a intentarlo."
            }, style = label(14, Brand.text))
        }
        Caption("Por tema")
        for (t in Beca.topics) {
            val tp = ps.filter { it.topic == t.id }
            if (tp.isEmpty()) continue
            val g = tp.count { it.isRight(r.answers[it.id]) }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row { BasicText(t.name, Modifier.weight(1f), style = label(13, Brand.text)); BasicText("$g / ${tp.size}", style = label(13, Brand.textDim)) }
                Bar(g / tp.size.toFloat(), if (g == tp.size) ok else Brand.orange)
            }
        }
        Caption("Toca una pregunta para ver su solución")
        for (row in ps.indices.chunked(10)) Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            for (k in row) {
                val good = ps[k].isRight(r.answers[ps[k].id])
                Box(Modifier.size(30.dp).background((if (good) ok else Brand.error).copy(alpha = 0.7f), RoundedCornerShape(7.dp)).pointerInput(k) { detectTapGestures { vm.becaGo(k) } }, contentAlignment = Alignment.Center) {
                    BasicText("${k + 1}", style = label(11, Brand.text, true))
                }
            }
        }
        Pill("Volver al inicio de Beca 18", Modifier.fillMaxWidth(), bg = Brand.accent) { vm.becaRun.value = null }
        Spacer(Modifier.height(10.dp))
    }
}
