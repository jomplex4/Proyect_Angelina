package com.digitalminds.angelina

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color as AColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.digitalminds.angelina.Editor
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/** Manual DI: one object, initialised once in the main process. */
object Services {
    lateinit var app: Application
    lateinit var store: Store
    lateinit var cas: GiacClient
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    fun init(a: Application) {
        app = a
        store = Store(a.filesDir, scope)
        cas = GiacClient(a)
        cas.bind()
        scope.launch { cas.warmUp() }
    }
}

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (Application.getProcessName() == packageName) Services.init(this)
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(SystemBarStyle.dark(AColor.BLACK), SystemBarStyle.dark(AColor.BLACK))
        super.onCreate(savedInstanceState)
        setContent {
            val vm: MainViewModel = viewModel()
            AppRoot(vm)
        }
    }
}

// ---------- shared widgets ----------

fun label(size: Int = 13, color: Color = Brand.textDim, bold: Boolean = false, align: TextAlign = TextAlign.Start) =
    TextStyle(color = color, fontFamily = Brand.sans, fontSize = size.sp, fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal, textAlign = align)

@Composable
fun Pill(text: String, modifier: Modifier = Modifier, bg: Color = Brand.keyOp, fg: Color = Brand.text, selected: Boolean = false, onClick: () -> Unit) {
    Box(
        modifier
            .height(38.dp)
            .background(if (selected) Brand.accent else bg, RoundedCornerShape(10.dp))
            .pointerInput(onClick) { detectTapGestures { onClick() } }
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) { BasicText(text, style = label(14, fg, selected), maxLines = 1) }
}

@Composable
fun Fld(vm: MainViewModel, id: String, modifier: Modifier = Modifier, fs: Float = 18f, h: Dp = 44.dp) {
    val ed by vm.field(id).collectAsState()
    val focused = vm.focus.collectAsState().value == id
    val shape = RoundedCornerShape(10.dp)
    Box(modifier.height(h).background(Brand.surface, shape).border(if (focused) 1.5.dp else 1.dp, if (focused) Brand.accent else Brand.line, shape)) {
        EditorView(ed, fs, focused, Modifier.fillMaxSize().padding(horizontal = 8.dp), onPlace = { vm.place(id, it) }, onSwipe = { vm.focusOn(id); vm.swipe(it) })
    }
}

@Composable
fun Caption(text: String, modifier: Modifier = Modifier) = BasicText(text.uppercase(), modifier, style = label(11, Brand.textDim).copy(letterSpacing = 1.4.sp))

@Composable
fun BusyLine(on: Boolean) {
    if (!on) { Spacer(Modifier.height(2.dp)); return }
    val p = rememberInfiniteTransition(label = "busy").animateFloat(0f, 1f, infiniteRepeatable(tween(1100, easing = LinearEasing), RepeatMode.Restart), label = "p")
    Box(Modifier.fillMaxWidth().height(2.dp).drawBehind {
        val w = size.width * 0.3f
        val x = (size.width + w) * p.value - w
        drawLine(Brand.accentHi, Offset(x.coerceAtLeast(0f), 1f), Offset((x + w).coerceAtMost(size.width), 1f), strokeWidth = 4f)
    })
}

fun copy(ctx: Context, text: String) {
    ctx.getSystemService(ClipboardManager::class.java).setPrimaryClip(ClipData.newPlainText("ANGELINA", text))
}

// ---------- root ----------

@Composable
fun AppRoot(vm: MainViewModel) {
    val screen by vm.screen.collectAsState()
    val overlay by vm.overlay.collectAsState()
    BackHandler(enabled = screen != Screen.Home || overlay != 0) { if (overlay != 0) vm.overlay.value = 0 else vm.go(Screen.Home) }
    BoxWithConstraints(Modifier.fillMaxSize().background(Brand.bg).windowInsetsPadding(WindowInsets.safeDrawing)) {
        val kh = maxHeight * 0.47f
        Column(Modifier.fillMaxSize()) {
            TopBar(vm, screen)
            Box(Modifier.weight(1f).fillMaxWidth()) {
                when (screen) {
                    Screen.Home -> HomeScreen(vm)
                    Screen.Matrix -> MatrixScreen(vm)
                    Screen.Stats -> StatsScreen(vm)
                    Screen.Table -> TableScreen(vm)
                    Screen.Graph -> GraphScreen(vm)
                    Screen.Programs -> ProgramsScreen(vm)
                    Screen.Units -> UnitsScreen(vm)
                    Screen.Guide -> GuideScreen(vm)
                    Screen.Settings -> SettingsScreen(vm)
                }
            }
            if (screen == Screen.Home || screen == Screen.Matrix || screen == Screen.Stats || screen == Screen.Table || screen == Screen.Graph) {
                val s = remember { vm.settings.value }
                Keypad(
                    Modifier.fillMaxWidth().height(kh).padding(horizontal = 8.dp, vertical = 6.dp),
                    s.page, s.haptics, onPage = { vm.setPage(it) }, onKey = { vm.onKey(it) }
                )
            }
        }
        if (overlay == 1) ModesOverlay(vm)
        if (overlay == 2) SearchOverlay(vm)
    }
}

@Composable
private fun TopBar(vm: MainViewModel, screen: Screen) {
    val busy by vm.busy.collectAsState()
    val st by vm.settings.collectAsState()
    Column {
        Row(Modifier.fillMaxWidth().height(46.dp).padding(horizontal = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            TopIcon(R.drawable.ic_modes) { vm.overlay.value = if (vm.overlay.value == 1) 0 else 1 }
            TopIcon(R.drawable.ic_search) { vm.overlay.value = if (vm.overlay.value == 2) 0 else 2 }
            TopIcon(R.drawable.ic_guide) { vm.go(Screen.Guide) }
            TopIcon(R.drawable.ic_settings) { vm.go(Screen.Settings) }
            Spacer(Modifier.weight(1f))
            if (busy) Pill("Cancel", bg = Brand.keyOp, fg = Brand.error) { vm.cancel() }
            else {
                val name = when (screen) {
                    Screen.Home -> "Home"; Screen.Matrix -> "Matrix"; Screen.Stats -> "Stats"; Screen.Table -> "Table"; Screen.Graph -> "Graph"
                    Screen.Programs -> "Programs"; Screen.Units -> "Units"; Screen.Guide -> "Guide"; Screen.Settings -> "Settings"
                }
                BasicText(name.uppercase(), style = label(12, Brand.text, true).copy(letterSpacing = 1.6.sp))
                Spacer(Modifier.width(10.dp))
                Box(Modifier.pointerInput(Unit) { detectTapGestures { vm.cycleAngle() } }.padding(vertical = 8.dp)) {
                    val a = when (st.angle) { 0 -> "DEG"; 2 -> "GRAD"; else -> "RAD" }
                    BasicText(a + if (st.base != 10) " " + when (st.base) { 2 -> "BIN"; 8 -> "OCT"; else -> "HEX" } else "", style = label(12, Brand.accentHi, true).copy(letterSpacing = 1.4.sp))
                }
                Spacer(Modifier.width(8.dp))
            }
        }
        BusyLine(busy)
    }
}

@Composable
private fun TopIcon(res: Int, onClick: () -> Unit) {
    Box(Modifier.size(42.dp).pointerInput(onClick) { detectTapGestures { onClick() } }, contentAlignment = Alignment.Center) {
        Image(painterResource(res), null, Modifier.size(22.dp), colorFilter = ColorFilter.tint(Brand.textDim))
    }
}

// ---------- Home ----------

@Composable
private fun HomeScreen(vm: MainViewModel) {
    val hist by vm.history.collectAsState()
    val st by vm.settings.collectAsState()
    val err by vm.error.collectAsState()
    Column(Modifier.fillMaxSize()) {
        Box(Modifier.weight(1f).fillMaxWidth()) {
            if (hist.isEmpty()) {
                Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painterResource(R.drawable.ic_mark), null, Modifier.size(width = 96.dp, height = 57.dp))
                    Spacer(Modifier.height(10.dp))
                    Caption("Type an expression and press =")
                    Spacer(Modifier.height(14.dp))
                    Column(Modifier.padding(horizontal = 28.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        for (tip in listOf(
                            "Matrix: page 6, tap [M], then use + fila / + columna",
                            "Long-press any key for variants and inverses",
                            "Search any function with the magnifier",
                            "Swipe the keys left or right for more pages"
                        )) BasicText(tip, style = label(13, Brand.textDim, align = TextAlign.Center), modifier = Modifier.fillMaxWidth())
                    }
                }
            } else {
                val list = remember(hist) { hist.asReversed() }
                LazyColumn(Modifier.fillMaxSize(), reverseLayout = true) {
                    items(list, key = { it.id }) { h -> HistRow(h, st, vm) }
                }
            }
        }
        Column(Modifier.fillMaxWidth().background(Brand.surface, RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)).padding(horizontal = 10.dp, vertical = 8.dp)) {
            Row(Modifier.fillMaxWidth().padding(bottom = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                BasicText(err ?: "", Modifier.weight(1f).padding(start = 4.dp), style = label(13, Brand.error), maxLines = 2)
                Pill("Verify", bg = Brand.keyNum, fg = Brand.violet) { vm.verify() }
            }
            MatBar(vm)
            Fld(vm, "home", Modifier.fillMaxWidth(), fs = 24f, h = 70.dp)
        }
    }
}

@Composable
private fun MatBar(vm: MainViewModel) {
    val ed by vm.field("home").collectAsState()
    if (Editor.matAt(ed) == null) return
    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(bottom = 6.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Pill("+ fila", bg = Brand.keyOp) { vm.matResize(1, 0) }
        Pill("- fila", bg = Brand.keyOp) { vm.matResize(-1, 0) }
        Pill("+ columna", bg = Brand.keyOp) { vm.matResize(0, 1) }
        Pill("- columna", bg = Brand.keyOp) { vm.matResize(0, -1) }
        BasicText("Matriz activa", style = label(12, Brand.textDim))
    }
}

@Composable
private fun HistRow(h: Hist, s: Settings, vm: MainViewModel) {
    val ctx = LocalContext.current
    var menu by remember { mutableStateOf(false) }
    val inRow = remember(h.input) { Parse.row(h.input) ?: Row(listOf(Str(h.input))) }
    val res = remember(h.id, h.dec, s.eng, s.sig, s.mixed, s.base) {
        val t = (if (h.dec && h.approx != null) h.approx else h.exact) ?: ""
        Fmt.base(t, s.base)?.let { Row(listOf(Str(it))) } ?: Fmt.rows(Parse.row(t) ?: Row(listOf(Str(t))), s.eng, s.sig, s.mixed)
    }
    Column(Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 8.dp)) {
        Box(Modifier.fillMaxWidth().pointerInput(h.id) { detectTapGestures { vm.recall(h) } }) {
            Box(Modifier.horizontalScroll(rememberScrollState())) { MathView(inRow, 15f, color = Brand.textDim) }
        }
        Spacer(Modifier.height(4.dp))
        Box(Modifier.fillMaxWidth().pointerInput(h.id) { detectTapGestures(onTap = { vm.toggle(h.id) }, onLongPress = { menu = true }) }, contentAlignment = Alignment.CenterEnd) {
            Box(Modifier.horizontalScroll(rememberScrollState())) { MathView(res, 26f) }
            if (menu) Popup(alignment = Alignment.TopEnd, onDismissRequest = { menu = false }) {
                Column(Modifier.background(Brand.surface, RoundedCornerShape(12.dp)).border(1.dp, Brand.line, RoundedCornerShape(12.dp)).padding(6.dp)) {
                    for ((t, a) in listOf<Pair<String, () -> Unit>>(
                        "Copy result" to { copy(ctx, (if (h.dec && h.approx != null) h.approx else h.exact) ?: "") },
                        "Use as Ans" to { vm.useAns(h) },
                        "Copy input" to { copy(ctx, h.input) },
                        "Reuse input" to { vm.recall(h) }
                    )) Box(Modifier.width(150.dp).height(40.dp).pointerInput(t) { detectTapGestures { menu = false; a() } }.padding(horizontal = 10.dp), contentAlignment = Alignment.CenterStart) {
                        BasicText(t, style = label(15, Brand.text))
                    }
                }
            }
        }
    }
}

// ---------- overlays ----------

@Composable
private fun ModesOverlay(vm: MainViewModel) {
    Box(Modifier.fillMaxSize().background(Color(0xCC000000)).pointerInput(Unit) { detectTapGestures { vm.overlay.value = 0 } }) {
        Column(
            Modifier.padding(top = 50.dp, start = 10.dp, end = 60.dp).background(Brand.surface, RoundedCornerShape(16.dp)).border(1.dp, Brand.line, RoundedCornerShape(16.dp)).padding(8.dp)
                .pointerInput(Unit) { detectTapGestures { } },
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Caption("Modes", Modifier.padding(8.dp))
            for ((n, s) in listOf("Home" to Screen.Home, "Matrix and vectors" to Screen.Matrix, "Statistics" to Screen.Stats, "Table" to Screen.Table, "Graph" to Screen.Graph,
                "Programs" to Screen.Programs, "Units and constants" to Screen.Units)) {
                Box(Modifier.fillMaxWidth().height(44.dp).background(Brand.keyNum, RoundedCornerShape(10.dp)).pointerInput(n) { detectTapGestures { vm.go(s) } }.padding(horizontal = 14.dp), contentAlignment = Alignment.CenterStart) {
                    BasicText(n, style = label(16, Brand.text))
                }
            }
        }
    }
}

@Composable
private fun SearchOverlay(vm: MainViewModel) {
    var q by remember { mutableStateOf("") }
    val fr = remember { FocusRequester() }
    LaunchedEffect(Unit) { fr.requestFocus() }
    val results = remember(q) {
        val k = q.trim().lowercase()
        if (k.isEmpty()) vm.funcs.filter { it.name in setOf("int", "diff", "limit", "sum", "solve", "simplify", "factor", "det", "inv", "laplace") }
        else {
            val alias = Aliases.map.filterKeys { it.contains(k) || k.contains(it) }.values.toSet()
            vm.funcs.mapNotNull { f ->
                val n = f.name.lowercase()
                val score = when {
                    n == k -> 0; n in alias -> 1; n.startsWith(k) -> 2; n.contains(k) -> 3; f.desc.lowercase().contains(k) -> 4; else -> -1
                }
                if (score < 0) null else Triple(score, f.name.length, f)
            }.sortedWith(compareBy<Triple<Int, Int, FDef>>({ it.first }, { it.second })).take(60).map { it.third }
        }
    }
    Column(Modifier.fillMaxSize().background(Brand.bg).padding(horizontal = 12.dp)) {
        Spacer(Modifier.height(52.dp))
        Box(Modifier.fillMaxWidth().height(48.dp).background(Brand.surface, RoundedCornerShape(12.dp)).border(1.5.dp, Brand.accent, RoundedCornerShape(12.dp)).padding(horizontal = 14.dp), contentAlignment = Alignment.CenterStart) {
            if (q.isEmpty()) BasicText("Search a function: integral, log, matrix...", style = label(16, Brand.textDim))
            BasicTextField(q, { q = it }, Modifier.fillMaxWidth().focusRequester(fr), textStyle = label(17, Brand.text), cursorBrush = SolidColor(Brand.accentHi), singleLine = true,
                keyboardOptions = KeyboardOptions(autoCorrectEnabled = false))
        }
        Spacer(Modifier.height(8.dp))
        LazyColumn(Modifier.fillMaxSize()) {
            items(results, key = { it.name }) { f ->
                Column(Modifier.fillMaxWidth().pointerInput(f.name) { detectTapGestures { vm.insertFunc(f) } }.padding(vertical = 8.dp, horizontal = 4.dp)) {
                    BasicText(f.name + "(" + List(f.argc) { "_" }.joinToString(", ") + ")", style = label(16, Brand.text, true))
                    BasicText(f.desc, style = label(13, Brand.textDim), maxLines = 1)
                }
            }
        }
    }
}
