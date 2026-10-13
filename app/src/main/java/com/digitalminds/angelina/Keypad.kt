package com.digitalminds.angelina

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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

enum class S { AC, DEL, EQ, LEFT, RIGHT, LP, RP, COMMA, FRAC, MIXED, SQRT, NROOT, CBRT, POW, SQUARE, CUBE, INV, EXP10, EXPE, X10, ANS, MAT2, MAT3, MAT4, LST, BASE }

sealed interface KA {
    data class T(val s: String) : KA
    data class F(val name: String, val pre: List<String>) : KA
    data class Sp(val s: S) : KA
}

class Key(val label: String, val act: KA, val role: Int = 0, val more: List<Pair<String, KA>> = emptyList(), val tapMenu: Boolean = false)

object Keys {
    private fun f(label: String, name: String, vararg pre: String, role: Int = 0, more: List<Pair<String, KA>> = emptyList()) =
        Key(label, KA.F(name, if (pre.isEmpty()) listOf("") else pre.toList()), role, more)

    private fun fv(label: String, name: String, vararg pre: String) = label to (KA.F(name, if (pre.isEmpty()) listOf("") else pre.toList()) as KA)
    private fun sp(label: String, s: S, role: Int = 0, more: List<Pair<String, KA>> = emptyList()) = Key(label, KA.Sp(s), role, more)
    private fun t(label: String, s: String = label, role: Int = 0, more: List<Pair<String, KA>> = emptyList()) = Key(label, KA.T(s), role, more)
    private fun tv(label: String, s: String = label) = label to (KA.T(s) as KA)
    private fun sv(label: String, s: S) = label to (KA.Sp(s) as KA)

    val pages: List<List<Key>> = listOf(
        listOf(
            f("sin", "sin", more = listOf(fv("sin⁻¹", "asin"), fv("sinh", "sinh"), fv("sinh⁻¹", "asinh"))),
            f("cos", "cos", more = listOf(fv("cos⁻¹", "acos"), fv("cosh", "cosh"), fv("cosh⁻¹", "acosh"))),
            f("tan", "tan", more = listOf(fv("tan⁻¹", "atan"), fv("tanh", "tanh"), fv("tanh⁻¹", "atanh"))),
            f("ln", "ln", more = listOf(sv("eˣ", S.EXPE), fv("log b", "logb", "", ""))),
            f("log", "log10", more = listOf(sv("10ˣ", S.EXP10), fv("log b", "logb", "", ""))),
            sp("√", S.SQRT, more = listOf(sv("ⁿ√", S.NROOT), sv("∛", S.CBRT))),
            sp("x²", S.SQUARE, more = listOf(sv("x³", S.CUBE), sv("x⁻¹", S.INV))),
            sp("^", S.POW),
            t("π", more = listOf(tv("i"), tv("∞"))),
            t("e", more = listOf(sv("eˣ", S.EXPE)))
        ),
        listOf(
            f("sin⁻¹", "asin", more = listOf(fv("sinh⁻¹", "asinh"))),
            f("cos⁻¹", "acos", more = listOf(fv("cosh⁻¹", "acosh"))),
            f("tan⁻¹", "atan", more = listOf(fv("tanh⁻¹", "atanh"))),
            f("cot", "cot", more = listOf(fv("cot⁻¹", "acot"))),
            f("sec", "sec", more = listOf(fv("sec⁻¹", "asec"))),
            f("csc", "csc", more = listOf(fv("csc⁻¹", "acsc"))),
            f("sinh", "sinh", more = listOf(fv("sinh⁻¹", "asinh"))),
            f("cosh", "cosh", more = listOf(fv("cosh⁻¹", "acosh"))),
            f("tanh", "tanh", more = listOf(fv("tanh⁻¹", "atanh"))),
            f("abs", "abs", more = listOf(fv("sign", "sign"), fv("conj", "conj")))
        ),
        listOf(
            t("n!", "!"), f("nCr", "comb", "", ""), f("nPr", "perm", "", ""),
            f("mod", "irem", "", "", more = listOf(fv("quo", "iquo", "", ""), fv("factor", "ifactor"), fv("prime?", "isprime"), fv("next", "nextprime"))),
            f("gcd", "gcd", "", ""), f("lcm", "lcm", "", ""),
            f("floor", "floor", more = listOf(fv("round", "round"), fv("frac", "fPart"), fv("int", "iPart"))),
            f("ceil", "ceil", more = listOf(fv("▶DMS", "dms"), fv("DMS▶", "fromdms", "", "", ""))),
            f("rand", "rand", "0", "1", more = listOf(fv("randint", "randint", "1", "6"))), sp("10ˣ", S.EXP10)
        ),
        listOf(
            f("d/dx", "diff", "", "x", more = listOf(fv("dⁿ/dxⁿ", "diff", "", "x", "2"), fv("∂/∂y", "diff", "", "y"))),
            f("∫", "int", "", "x"), f("∫[a,b]", "int", "", "x", "", ""),
            f("lim", "limit", "", "x", "", more = listOf(fv("lim⁺", "limit", "", "x", "", "1"), fv("lim⁻", "limit", "", "x", "", "-1"))),
            f("Σ", "sum", "", "k", "1", ""), f("Π", "product", "", "k", "1", ""),
            f("taylor", "taylor", "", "x=0", "5", more = listOf(fv("series", "series", "", "x=0", "5"))),
            f("solve", "solve", "", "x", more = listOf(fv("csolve", "csolve", "", "x"), fv("fsolve", "fsolve", "", "x=0"))),
            f("desolve", "desolve", "", "x", "y"),
            f("Laplace", "laplace", "", "t", "s", more = listOf(fv("iLaplace", "ilaplace", "", "s", "t"), fv("Ztrans", "ztrans", "", "n", "z"), fv("Fourier", "fourier", "", "t", "w")))
        ),
        listOf(
            f("simplify", "simplify"), f("factor", "factor"), f("expand", "expand"), f("partfrac", "partfrac"), f("subst", "subst", "", "x="),
            f("linsolve", "linsolve", "[]", "[]"), f("roots", "roots"), f("fsolve", "fsolve", "", "x=0"),
            Key("ineq", KA.T("<"), 0, listOf(tv("="), tv("<"), tv(">"), tv("≤"), tv("≥"), tv("≠"), tv(" and "), tv(" or ")), true),
            f("▶a/b", "exact")
        ),
        listOf(
            sp("[M]", S.MAT2, more = listOf(sv("3×3", S.MAT3), sv("4×4", S.MAT4), sv("[ ]", S.LST))),
            f("det", "det", more = listOf(fv("Aᵀ", "transpose"), fv("rref", "rref"), fv("LU", "lu"), fv("QR", "qr"), fv("trace", "trace"))),
            f("inv", "inv"), f("eig", "eigenvals", more = listOf(fv("eigvec", "eigenvects"))),
            f("dot", "dot", "", "", more = listOf(fv("norm", "norm"), fv("cross", "cross", "", ""))),
            f("cross", "cross", "", ""), t("i", more = listOf(fv("re", "re"), fv("im", "im"), fv("arg", "arg"), fv("conj", "conj"))), f("▶polar", "polar_coordinates"), f("▶rect", "normal"),
            Key("Bin-Hex", KA.Sp(S.BASE), 0, listOf(fv("and", "bitand", "", ""), fv("or", "bitor", "", ""), fv("xor", "bitxor", "", ""), fv("not", "not"), fv("shl", "shl", "", ""), fv("shr", "shr", "", ""), tv("0x"), tv("0b"), tv("A"), tv("B"), tv("C"), tv("D"), tv("E"), tv("F")))
        )
    )

    private val letters = ('A'..'Z').map { tv(it.toString()) }

    val fixed: List<Key> = listOf(
        sp("(", S.LP), sp(")", S.RP),
        Key("a/b", KA.Sp(S.FRAC), 0, listOf(sv("a b/c", S.MIXED))),
        Key("x", KA.T("x"), 0, listOf(tv("y"), tv("z"), tv("t"), tv("y'"), tv("y''"), tv(":=")) + letters),
        Key(",", KA.Sp(S.COMMA), 0, listOf(sv("[ ]", S.LST), tv(":="), tv("="), tv("!")))
    )

    val numeric: List<List<Key>> = listOf(
        listOf(t("7", role = 1), t("8", role = 1), t("9", role = 1), sp("⌫", S.DEL, 2), sp("AC", S.AC, 2)),
        listOf(t("4", role = 1), t("5", role = 1), t("6", role = 1), t("×", role = 2), t("÷", role = 2)),
        listOf(t("1", role = 1), t("2", role = 1), t("3", role = 1), t("+", role = 2), t("−", role = 2)),
        listOf(t("0", role = 1), t(".", role = 1), sp("×10ˣ", S.X10, 2), sp("Ans", S.ANS, 2, more = listOf(tv("PreAns"))), sp("=", S.EQ, 3))
    )
}

private class Above : PopupPositionProvider {
    override fun calculatePosition(anchorBounds: IntRect, windowSize: IntSize, layoutDirection: LayoutDirection, popupContentSize: IntSize): IntOffset {
        val x = (anchorBounds.center.x - popupContentSize.width / 2).coerceIn(4, maxOf(4, windowSize.width - popupContentSize.width - 4))
        return IntOffset(x, maxOf(4, anchorBounds.top - popupContentSize.height - 8))
    }
}

@Composable
private fun KeyBtn(k: Key, modifier: Modifier, haptics: Boolean, onKey: (KA) -> Unit) {
    val view = LocalView.current
    val sc = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()
    var pop by remember { mutableStateOf(false) }
    val bg = when (k.role) { 1 -> Brand.keyNum; 2 -> Brand.keyOp; 3 -> Brand.accent; else -> Brand.keyFn }
    val fs = when { k.label.length <= 2 -> 19.sp; k.label.length <= 5 -> 15.sp; else -> 12.sp }
    Box(
        modifier
            .graphicsLayer { scaleX = sc.value; scaleY = sc.value }
            .background(bg, RoundedCornerShape(11.dp))
            .border(if (k.role == 0) 1.dp else 0.dp, Brand.line, RoundedCornerShape(11.dp))
            .pointerInput(k) {
                detectTapGestures(
                    onPress = {
                        scope.launch { sc.animateTo(0.93f, tween(60)) }
                        tryAwaitRelease()
                        scope.launch { sc.animateTo(1f, tween(90)) }
                    },
                    onTap = {
                        if (haptics) view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        if (k.tapMenu) pop = true else onKey(k.act)
                    },
                    onLongPress = {
                        if (k.more.isNotEmpty()) { if (haptics) view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS); pop = true }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        BasicText(k.label, style = TextStyle(color = Brand.text, fontFamily = Brand.sans, fontSize = fs, fontWeight = if (k.role == 0) FontWeight.Normal else FontWeight.Bold, textAlign = TextAlign.Center), maxLines = 1)
        if (pop) {
            Popup(popupPositionProvider = Above(), onDismissRequest = { pop = false }) {
                Column(
                    Modifier.background(Brand.surface, RoundedCornerShape(14.dp)).border(1.dp, Brand.line, RoundedCornerShape(14.dp)).padding(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (row in k.more.chunked(6)) Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        for ((lab, act) in row) Box(
                            Modifier.size(width = if (lab.length > 4) 64.dp else 44.dp, height = 40.dp)
                                .background(Brand.keyNum, RoundedCornerShape(9.dp))
                                .pointerInput(lab) { detectTapGestures { pop = false; onKey(act) } },
                            contentAlignment = Alignment.Center
                        ) { BasicText(lab, style = TextStyle(color = Brand.text, fontFamily = Brand.sans, fontSize = 15.sp), maxLines = 1) }
                    }
                }
            }
        }
    }
}

@Composable
private fun Dots(state: PagerState) {
    val cur = state.currentPage
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        for (i in 0 until 6) Box(Modifier.padding(horizontal = 3.dp).size(if (i == cur) 7.dp else 5.dp).background(if (i == cur) Brand.accentHi else Brand.textDim.copy(alpha = 0.5f), CircleShape))
    }
}

@Composable
fun Keypad(modifier: Modifier, startPage: Int, haptics: Boolean, onPage: (Int) -> Unit, onKey: (KA) -> Unit) {
    val pager = rememberPagerState(initialPage = startPage.coerceIn(0, 5), pageCount = { 6 })
    LaunchedEffect(pager) { snapshotFlow { pager.currentPage }.collect { onPage(it) } }
    val gap = 6.dp
    Column(modifier, verticalArrangement = Arrangement.spacedBy(gap)) {
        HorizontalPager(pager, Modifier.fillMaxWidth().weight(2.12f), pageSpacing = 0.dp) { p ->
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(gap)) {
                for (r in 0 until 2) Row(Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(gap)) {
                    for (c in 0 until 5) KeyBtn(Keys.pages[p][r * 5 + c], Modifier.weight(1f).fillMaxSize(), haptics, onKey)
                }
            }
        }
        Box(Modifier.fillMaxWidth().weight(0.3f)) { Dots(pager) }
        Row(Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(gap)) {
            for (k in Keys.fixed) KeyBtn(k, Modifier.weight(1f).fillMaxSize(), haptics, onKey)
        }
        for (row in Keys.numeric) Row(Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(gap)) {
            for (k in row) KeyBtn(k, Modifier.weight(1f).fillMaxSize(), haptics, onKey)
        }
    }
}
