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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Ayuda de las opciones de Modos (no son teclas, pero también llevan "?"). */
object ModeHelp {
    val map = mapOf(
        "Inicio" to KHelp("Inicio", "Pantalla principal: escribes operaciones y ves el historial.", "Escribe con el teclado y pulsa =. Toca un resultado para cambiar entre exacto y decimal; mantenlo pulsado para copiarlo.", emptyList()),
        "Matrices y vectores" to KHelp("Matrices y vectores", "Guarda las matrices A, B y C y los vectores u y v para operar con ellos.", "Elige la matriz, ajusta filas y columnas, llena las celdas y toca la operación (det, inv, A×B...).", emptyList(), "También puedes escribir matrices en Inicio con la tecla [M]."),
        "Estadística" to KHelp("Estadística", "Media, mediana, desviación, cuartiles, regresiones y distribuciones.", "Escribe los datos en la columna X (y en Y para 2 variables) y pulsa Calcular.", emptyList(), "σ es la desviación poblacional y s la muestral."),
        "Tabla de valores" to KHelp("Tabla de valores", "Muestra f(x) y g(x) para muchos valores de x.", "Escribe la función, el inicio, el fin y el paso.", emptyList()),
        "Gráfica" to KHelp("Gráfica", "Dibuja hasta 4 funciones y una curva paramétrica.", "Escribe la función en f1. Toca la curva para recorrerla; pellizca para hacer zoom. Usa Raíz, Mín, Máx e Intersección cerca del punto.", emptyList()),
        "Programas" to KHelp("Programas", "Crea funciones propias en lenguaje Giac.", "Elige una plantilla o escribe el código y pulsa Ejecutar. Las funciones quedan disponibles en Inicio.", emptyList()),
        "Unidades y constantes" to KHelp("Unidades y constantes", "Convierte unidades con factores exactos e inserta constantes físicas.", "Elige categoría, unidad de origen y de destino, y pulsa Convertir en Inicio.", emptyList()),
        "Formulario" to KHelp("Formulario", "Fórmulas de aritmética, álgebra, geometría, trigonometría, transformaciones, estadística y probabilidad, con figuras.", "Elige una sección o busca por nombre. Toca una fórmula para ver su figura, qué significa cada letra y un ejemplo que puedes probar.", emptyList(), "Funciona sin internet."),
        "Beca 18" to KHelp("Beca 18", "75 problemas originales de Razonamiento Matemático por tema, con solución explicada, y simulacros de 30 preguntas con cronómetro.", "Practica por tema (se corrige al instante) o haz un simulacro (se corrige al terminar). Repasa tus errores cuando quieras.", emptyList(), "Las respuestas están verificadas con Giac; el examen real puede tener otros formatos."),
        "Paso a paso" to KHelp("Paso a paso", "Resuelve problemas típicos mostrando la fórmula, la sustitución de tus datos, cada paso y el resultado.", "Elige un tema, escribe tus datos (o usa los de ejemplo) y pulsa Resolver. En estadística puedes armar la tabla de frecuencias.", emptyList(), "Funciona sin internet."),
        "Manual" to KHelp("Manual", "Explica cómo hacer cada tarea con ejemplos comprobados.", "Escribe lo que quieres hacer, por ejemplo \"ecuación de grado 2\".", emptyList())
    )
}

@Composable
private fun Section(title: String, body: String) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Caption(title)
        BasicText(body, style = label(15, Brand.text))
    }
}

/** Ventana de ayuda de una tecla o una opción. */
@Composable
fun HelpSheet(vm: MainViewModel) {
    val req by vm.help.collectAsState()
    val r = req ?: return
    val h = r.help
    val st by vm.settings.collectAsState()
    Box(Modifier.fillMaxSize().background(Color(0xCC000000)).pointerInput(Unit) { detectTapGestures { vm.help.value = null } }, contentAlignment = Alignment.BottomCenter) {
        Column(
            Modifier.fillMaxWidth().heightIn(max = 620.dp)
                .background(Brand.surface, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .border(1.dp, Brand.line, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .pointerInput(Unit) { detectTapGestures { } }
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                if (r.key != null) Box(Modifier.background(Brand.keyOp, RoundedCornerShape(9.dp)).padding(horizontal = 12.dp, vertical = 6.dp)) {
                    BasicText(r.key.label, style = TextStyle(color = Brand.text, fontFamily = Brand.math, fontSize = 18.sp, fontWeight = FontWeight.Bold))
                }
                BasicText(h.title, Modifier.weight(1f), style = label(19, Brand.text, true))
                Pill("✕", bg = Brand.keyNum) { vm.help.value = null }
            }
            Section("Para qué sirve", h.what)
            Section("Cómo se usa", h.how)
            if (h.ex.isNotEmpty()) {
                Caption("Ejemplos")
                for (e in h.ex) {
                    val inRow = remember(e.input) { Parse.row(e.input) ?: Row(listOf(Str(e.input))) }
                    val outRow = remember(e.out) { boolEs(e.out)?.let { Row(listOf(Str(it))) } ?: Parse.row(e.out) ?: Row(listOf(Str(e.out))) }
                    Row(
                        Modifier.fillMaxWidth().background(Brand.keyNum, RoundedCornerShape(12.dp)).padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Box(Modifier.horizontalScroll(rememberScrollState())) { MathView(inRow, 17f, color = Brand.textDim) }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                BasicText("= ", style = label(17, Brand.textDim))
                                Box(Modifier.horizontalScroll(rememberScrollState())) { MathView(outRow, 19f) }
                            }
                            if (e.deg) BasicText(if (st.angle == 0) "En modo GRADOS" else "Necesita modo GRADOS (Probar lo activa)", style = label(11, Brand.violet))
                        }
                        Pill("Probar", bg = Brand.accent) { vm.tryExample(e) }
                    }
                }
            }
            if (h.note.isNotEmpty()) Box(Modifier.fillMaxWidth().background(Brand.bg, RoundedCornerShape(12.dp)).border(1.dp, Brand.line, RoundedCornerShape(12.dp)).padding(10.dp)) {
                BasicText("💡 " + h.note, style = label(14, Brand.textDim))
            }
            val sib = r.siblings
            if (sib.isNotEmpty()) {
                Caption("Variantes (desliza la tecla arriba o abajo)")
                Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    for (k in sib) Row(Modifier.background(Brand.keyNum, RoundedCornerShape(10.dp)).padding(start = 4.dp, end = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.height(40.dp).padding(horizontal = 8.dp).pointerInput(k.label) { detectTapGestures { vm.help.value = null; vm.onKey(k.act) } }, contentAlignment = Alignment.Center) {
                            BasicText(k.label, style = TextStyle(color = Brand.text, fontFamily = Brand.math, fontSize = 16.sp))
                        }
                        if (KeyHelp.of(k.label) != null) HelpDot { vm.showHelp(k) }
                    }
                }
            }
            if (r.key != null) Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Pill("Insertar " + r.key.label, Modifier.weight(1f), bg = Brand.accent) { vm.help.value = null; vm.onKey(r.key.act) }
                Pill("Ver manual", Modifier.weight(1f)) { vm.help.value = null; vm.openGuide(h.title) }
            }
            if (r.key != null && Keys.byLabel.containsKey(r.key.label)) {
                val mine = r.key.label in st.myKeys
                Pill(if (mine) "★ Quitar de Mis teclas" else "☆ Agregar a Mis teclas", Modifier.fillMaxWidth(), bg = Brand.keyNum, fg = if (mine) Brand.orange else Brand.text) {
                    vm.toggleMine(r.key.label)
                }
            }
            Spacer(Modifier.height(4.dp))
        }
    }
}
