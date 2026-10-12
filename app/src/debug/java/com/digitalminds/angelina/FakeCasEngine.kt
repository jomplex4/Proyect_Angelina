package com.digitalminds.angelina

/** Debug and @Preview only. Returns canned strings; it never pretends to do math. */
class FakeCasEngine : CasEngine {
    override suspend fun eval(expr: String): CasResult =
        CasResult(out = if (expr.contains("int")) "(x-1)*exp(x)" else "5/6")
}
