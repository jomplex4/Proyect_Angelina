package com.digitalminds.angelina

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(widthDp = 360, heightDp = 720, showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun KeypadPreview() {
    val r = produceState(CasResult()) { value = FakeCasEngine().eval("int(x*exp(x),x)") }
    Column(Modifier.background(Brand.bg).padding(8.dp)) {
        MathView(Parse.row(r.value.out ?: "0") ?: Row(), 26f)
        Keypad(Modifier.fillMaxWidth().height(330.dp), 0, false, {}, {})
    }
}
