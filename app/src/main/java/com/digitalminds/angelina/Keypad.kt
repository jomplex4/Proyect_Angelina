package com.digitalminds.angelina

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import kotlinx.coroutines.launch
import kotlin.math.abs

private class Above : PopupPositionProvider {
    override fun calculatePosition(anchorBounds: IntRect, windowSize: IntSize, layoutDirection: LayoutDirection, popupContentSize: IntSize): IntOffset {
        val x = (anchorBounds.center.x - popupContentSize.width / 2).coerceIn(4, maxOf(4, windowSize.width - popupContentSize.width - 4))
        return IntOffset(x, maxOf(4, anchorBounds.top - popupContentSize.height - 8))
    }
}

/** Insignia "?" que abre la ayuda de una opción. */
@Composable
fun HelpDot(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier.size(22.dp).background(Brand.keyOp, CircleShape).border(1.dp, Brand.violet, CircleShape)
            .pointerInput(onClick) { detectTapGestures { onClick() } },
        contentAlignment = Alignment.Center
    ) { BasicText("?", style = TextStyle(color = Brand.violet, fontFamily = Brand.math, fontSize = 13.sp, fontWeight = FontWeight.Bold)) }
}

/** Ventana de variantes: tocar inserta, "?" abre la ayuda de esa variante. */
@Composable
private fun VariantsPopup(k: Key, onDismiss: () -> Unit, onKey: (KA) -> Unit, onHelp: (Key) -> Unit) {
    Popup(popupPositionProvider = Above(), onDismissRequest = onDismiss) {
        Column(
            Modifier.background(Brand.surface, RoundedCornerShape(14.dp)).border(1.dp, Brand.line, RoundedCornerShape(14.dp)).padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (row in k.more.chunked(4)) Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                for ((lab, act) in row) Row(
                    Modifier.height(42.dp).background(Brand.keyNum, RoundedCornerShape(9.dp)).padding(start = 4.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier.width(if (lab.length > 6) 88.dp else if (lab.length > 3) 62.dp else 40.dp).height(42.dp)
                            .pointerInput(lab) { detectTapGestures { onDismiss(); onKey(act) } },
                        contentAlignment = Alignment.Center
                    ) { BasicText(lab, style = TextStyle(color = Brand.text, fontFamily = Brand.math, fontSize = 15.sp), maxLines = 1) }
                    if (!k.tapMenu && KeyHelp.of(lab) != null) HelpDot { onDismiss(); onHelp(Key(lab, act)) }
                }
            }
        }
    }
}

/**
 * Gestos de cada tecla:
 * tocar = escribir; mantener pulsada = ventana de ayuda; deslizar arriba o abajo = variantes.
 * El deslizamiento horizontal se deja libre para cambiar de página.
 */
@Composable
private fun KeyBtn(k: Key, modifier: Modifier, haptics: Boolean, onKey: (KA) -> Unit, onHelp: (Key) -> Unit) {
    val view = LocalView.current
    val sc = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()
    var pop by remember { mutableStateOf(false) }
    val bg = when (k.role) { 1 -> Brand.keyNum; 2 -> Brand.keyOp; 3 -> Brand.accent; else -> Brand.keyFn }
    val slot = k === Keys.emptySlot
    val fs = when { k.label.length <= 2 -> 20.sp; k.label.length <= 5 -> 16.sp; k.label.length <= 7 -> 13.sp; else -> 11.5.sp }
    val hasHelp = KeyHelp.of(k.label) != null
    Box(
        modifier
            .graphicsLayer { scaleX = sc.value; scaleY = sc.value }
            .background(bg, RoundedCornerShape(11.dp))
            .border(if (k.role == 0) 1.dp else 0.dp, Brand.line, RoundedCornerShape(11.dp))
            .pointerInput(k) {
                awaitEachGesture {
                    val down = awaitFirstDown()
                    scope.launch { sc.animateTo(0.93f, tween(60)) }
                    var outcome = 0 // 0 cancelado, 1 toque, 2 deslizar vertical
                    val slop = viewConfiguration.touchSlop
                    val done = withTimeoutOrNull(viewConfiguration.longPressTimeoutMillis) {
                        while (true) {
                            val ev = awaitPointerEvent()
                            val c = ev.changes.firstOrNull { it.id == down.id } ?: break
                            if (c.isConsumed) break
                            if (!c.pressed) { outcome = 1; c.consume(); break }
                            val d = c.position - down.position
                            if (abs(d.y) > slop && abs(d.y) > abs(d.x) * 1.2f) { outcome = 2; c.consume(); break }
                            if (abs(d.x) > slop * 1.5f) break
                        }
                        true
                    }
                    if (done == null) {
                        // Pulsación mantenida: ayuda (o variantes si la tecla no tiene ayuda).
                        if (haptics) view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                        if (hasHelp) onHelp(k) else if (k.more.isNotEmpty()) pop = true
                        while (true) {
                            val ev = awaitPointerEvent()
                            ev.changes.forEach { it.consume() }
                            if (ev.changes.none { it.pressed }) break
                        }
                    } else when (outcome) {
                        1 -> {
                            if (haptics) view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                            if (k.tapMenu) pop = true else onKey(k.act)
                        }
                        2 -> if (k.more.isNotEmpty()) {
                            if (haptics) view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                            pop = true
                        }
                    }
                    scope.launch { sc.animateTo(1f, tween(90)) }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        BasicText(k.label, style = TextStyle(color = if (slot) Brand.textDim else Brand.text, fontFamily = Brand.math, fontSize = fs, fontWeight = if (k.role == 0) FontWeight.Normal else FontWeight.Bold, textAlign = TextAlign.Center), maxLines = 1)
        // Puntito arriba a la derecha: la tecla tiene variantes (deslizar arriba o abajo).
        if (k.more.isNotEmpty()) Box(Modifier.align(Alignment.TopEnd).padding(top = 4.dp, end = 5.dp).size(4.dp).background(Brand.violet.copy(alpha = 0.8f), CircleShape))
        if (pop) VariantsPopup(k, { pop = false }, onKey, onHelp)
    }
}

@Composable
private fun Dots(state: PagerState, pages: List<KeyPage>) {
    val cur = state.currentPage.coerceIn(0, pages.size - 1)
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        BasicText(pages[cur].name.uppercase(), style = TextStyle(color = Brand.textDim, fontFamily = Brand.sans, fontSize = 10.sp, letterSpacing = 1.2.sp))
        Box(Modifier.width(8.dp))
        for (i in pages.indices) Box(Modifier.padding(horizontal = 2.5.dp).size(if (i == cur) 7.dp else 5.dp).background(if (i == cur) Brand.accentHi else Brand.textDim.copy(alpha = 0.5f), CircleShape))
    }
}

/**
 * Teclado: páginas deslizables (en el orden que eligió el usuario) más las filas fijas.
 * onUse avisa qué página se usó, para ordenar por uso.
 */
@Composable
fun Keypad(
    modifier: Modifier, pages: List<KeyPage>, startPage: Int, haptics: Boolean, onPage: (Int) -> Unit, onKey: (KA) -> Unit,
    onHelp: (Key) -> Unit = {}, onUse: (String) -> Unit = {}
) {
    val pager = rememberPagerState(initialPage = startPage.coerceIn(0, maxOf(0, pages.size - 1)), pageCount = { pages.size })
    LaunchedEffect(pager) { snapshotFlow { pager.currentPage }.collect { onPage(it) } }
    val gap = 6.dp
    Column(modifier, verticalArrangement = Arrangement.spacedBy(gap)) {
        HorizontalPager(pager, Modifier.fillMaxWidth().weight(2.12f), pageSpacing = 0.dp) { p ->
            val pg = pages[p]
            val use: (KA) -> Unit = { onUse(pg.id); onKey(it) }
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(gap)) {
                for (r in 0 until 2) Row(Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(gap)) {
                    for (c in 0 until 5) {
                        val k = pg.keys.getOrNull(r * 5 + c)
                        if (k == null) Box(Modifier.weight(1f)) else KeyBtn(k, Modifier.weight(1f).fillMaxSize(), haptics, use, onHelp)
                    }
                }
            }
        }
        Box(Modifier.fillMaxWidth().weight(0.3f)) { Dots(pager, pages) }
        Row(Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(gap)) {
            for (k in Keys.fixed) KeyBtn(k, Modifier.weight(1f).fillMaxSize(), haptics, onKey, onHelp)
        }
        for (row in Keys.numeric) Row(Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(gap)) {
            for (k in row) KeyBtn(k, Modifier.weight(1f).fillMaxSize(), haptics, onKey, onHelp)
        }
    }
}
