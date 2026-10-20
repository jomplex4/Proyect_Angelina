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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
private fun Expr(src: String, fs: Float, color: androidx.compose.ui.graphics.Color = Brand.text) {
    val row = remember(src) { Parse.row(src) ?: Row(listOf(Str(src))) }
    Box(Modifier.horizontalScroll(rememberScrollState())) { MathView(row, fs, color = color) }
}

@Composable
private fun TextInput(vm: MainViewModel, id: String) {
    val v by vm.text(id).collectAsState()
    Box(Modifier.fillMaxWidth().height(44.dp).background(Brand.surface, RoundedCornerShape(10.dp)).border(1.dp, Brand.line, RoundedCornerShape(10.dp)).padding(horizontal = 10.dp), contentAlignment = Alignment.CenterStart) {
        BasicTextField(v, { vm.text(id).value = it }, Modifier.fillMaxWidth(), textStyle = TextStyle(color = Brand.text, fontFamily = Brand.math, fontSize = 17.sp), singleLine = true,
            cursorBrush = SolidColor(Brand.accentHi), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, autoCorrectEnabled = false))
    }
}

/** Tabla de frecuencias dibujada (la primera fila son los encabezados). */
@Composable
fun TableView(t: List<List<String>>) {
    Box(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
        Column(Modifier.background(Brand.surface, RoundedCornerShape(10.dp)).border(1.dp, Brand.line, RoundedCornerShape(10.dp)).padding(6.dp)) {
            t.forEachIndexed { r, row ->
                Row(Modifier.background(if (r == 0) Brand.keyOp else if (r == t.lastIndex) Brand.keyNum else Brand.surface)) {
                    row.forEachIndexed { c, cell ->
                        Box(Modifier.width(if (c == 0) 96.dp else 62.dp).padding(horizontal = 4.dp, vertical = 7.dp), contentAlignment = Alignment.Center) {
                            BasicText(cell, style = TextStyle(color = if (r == 0) Brand.violet else Brand.text, fontFamily = Brand.math, fontSize = 13.sp))
                        }
                    }
                }
            }
        }
    }
}

/** Resolver paso a paso: lista de temas y, al elegir uno, datos, pasos y resultado. */
@Composable
fun SolverScreen(vm: MainViewModel) {
    val id by vm.solverId.collectAsState()
    val s = id?.let { Solvers.byId(it) }
    if (s == null) SolverList(vm) else SolverDetail(vm, s)
}

@Composable
private fun SolverList(vm: MainViewModel) {
    var sec by remember { mutableIntStateOf(0) }
    val list = remember(sec) { Solvers.all.filter { it.sec == Solvers.secs[sec].id } }
    Column(Modifier.fillMaxSize().padding(horizontal = 10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        BasicText("Elige un tema, escribe tus datos y verás la fórmula, la sustitución y cada paso.", style = label(13, Brand.textDim))
        Chips(Solvers.secs.map { it.name }, sec) { sec = it }
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            for (s in list) Column(
                Modifier.fillMaxWidth().background(Brand.surface, RoundedCornerShape(12.dp)).pointerInput(s.id) { detectTapGestures { vm.openSolver(s.id) } }.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                BasicText(s.title, style = label(16, Brand.text, true))
                BasicText(s.desc, style = label(13, Brand.textDim))
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SolverDetail(vm: MainViewModel, s: Solver) {
    val out by vm.solveOut.collectAsState()
    val busy by vm.busy.collectAsState()
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Pill("←", bg = Brand.keyNum) { vm.openSolver(null) }
            BasicText(s.title, Modifier.weight(1f), style = label(17, Brand.text, true))
        }
        BasicText(s.desc, style = label(13, Brand.textDim))
        Box(Modifier.fillMaxWidth().background(Brand.surface, RoundedCornerShape(12.dp)).padding(10.dp)) {
            if (s.formula.startsWith("T:")) BasicText(s.formula.removePrefix("T:"), style = TextStyle(color = Brand.text, fontFamily = Brand.math, fontSize = 18.sp))
            else Expr(s.formula, 20f)
        }
        Caption("Tus datos")
        for (i in s.inputs) {
            if (i.list) Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                BasicText(i.label, style = label(13, Brand.textDim)); TextInput(vm, "SV:${s.id}:${i.id}")
            } else Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                BasicText(i.label, Modifier.weight(0.9f), style = label(13, Brand.textDim))
                Fld(vm, "SV:${s.id}:${i.id}", Modifier.weight(1f), fs = 17f, h = 42.dp)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Pill(if (busy) "Calculando..." else "Resolver", Modifier.weight(1.4f), bg = Brand.accent) { vm.runSolver() }
            Pill("Ejemplo", Modifier.weight(1f)) { vm.solverExample(s) }
            Pill("Limpiar", Modifier.weight(1f)) { vm.solverClear(s) }
        }
        out?.let { o ->
            o.error?.let { BasicText(it, style = label(14, Brand.error)) }
            o.table?.let { Caption("Tabla de frecuencias"); TableView(it) }
            if (o.lines.isNotEmpty()) Caption("Paso a paso")
            o.lines.forEachIndexed { k, l ->
                val last = k == o.lines.lastIndex
                Row(
                    Modifier.fillMaxWidth().background(if (last) Brand.keyOp else Brand.surface, RoundedCornerShape(12.dp))
                        .border(if (last) 1.dp else 0.dp, Brand.accent, RoundedCornerShape(12.dp)).padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(Modifier.size(24.dp).background(Brand.keyOp, CircleShape), contentAlignment = Alignment.Center) {
                        BasicText("${k + 1}", style = label(12, Brand.violet, true))
                    }
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        BasicText(l.label, style = label(14, Brand.text))
                        l.shown?.let { Expr(it, 17f, Brand.textDim) }
                        if (l.result != null) {
                            if (l.text) BasicText(l.result, style = label(16, Brand.text, true))
                            else Row(verticalAlignment = Alignment.CenterVertically) {
                                BasicText("= ", style = label(18, Brand.textDim))
                                Expr(boolEs(l.result) ?: l.result, 20f)
                            }
                            l.approx?.let { BasicText("≈ $it", style = label(14, Brand.textDim)) }
                        }
                    }
                }
            }
        }
        if (s.note.isNotEmpty()) BasicText("💡 " + s.note, style = label(13, Brand.textDim))
        s.links.firstOrNull()?.let { t -> Pill("Ver la fórmula en el formulario", bg = Brand.keyNum, fg = Brand.violet) { vm.openFormulas(t) } }
        Spacer(Modifier.height(10.dp))
    }
}
