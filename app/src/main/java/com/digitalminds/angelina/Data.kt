package com.digitalminds.angelina

class GuideEntry(val title: String, val goal: String, val steps: List<String>, val example: String, val tags: String = "")

object Guide {
    private fun g(t: String, goal: String, ex: String, tags: String, vararg steps: String) = GuideEntry(t, goal, steps.toList(), ex, tags)

    val entries: List<GuideEntry> = listOf(
        g("Matrices: add rows and columns", "Make a matrix bigger or smaller while you type it.", "det([[1,2,3],[4,5,6],[7,8,10]])", "matrix size rows columns fila columna",
            "Tap [M] on page 6 to insert a 2 x 2 matrix. The line 'Matriz activa' and its buttons appear above the entry line.", "Tap a cell, type a value, then press + fila or + columna.",
            "Press - fila or - columna to remove a row or column. Existing values are kept.", "Press = to compute det, inv or eig. The maximum size is 8 x 8."),
        g("Cotangent, secant, cosecant", "Use cot, sec and csc, and their inverses.", "cot(pi/4)", "cot sec csc reciprocal trig cotangent secant cosecant",
            "Open page 2 and tap cot, sec or csc.", "For the inverses, long-press the key and choose cot inverse, sec inverse or csc inverse.", "In Degrees mode the result follows the angle unit set in Settings."),
        g("Inverse trig: sin inverse", "Angle whose sine is a given value.", "simplify((asin(1/2))/(pi/180))", "arcsin inverse sin sin-1 angle",
            "Open page 2 and tap sin inverse (sin-1).", "Type the value, for example 1/2, then press =.", "In Degrees mode the answer is in degrees."),
        g("Cube root", "Compute the real cube root of a number.", "simplify(surd(27,3))", "root cbrt radical",
            "Open page 1 and long-press the square root key.", "Choose the nth root template and type 3 as the index.", "Type the radicand and press =."),
        g("Nth root", "Compute a root of any index, such as the 5th root of 32.", "simplify(surd(32,5))", "root radical index",
            "Open page 2 and tap the nth root key.", "Type the index in the small box, then move right and type the radicand.", "Press =."),
        g("Log base b", "Compute a logarithm in any base.", "logb(8,2)", "logarithm base",
            "Open page 2 and tap the log base key.", "Type the number first, then press comma and type the base.", "Press ="),
        g("ln and e^x", "Natural logarithm and the exponential function.", "ln(exp(2))", "exp natural",
            "Tap ln on page 1 and the e^x key on page 2.", "ln and e^x undo each other, so ln(e^2) is 2."),
        g("Powers", "Raise a number or expression to a power.", "2^10", "power exponent square",
            "Type the base, tap the ^ key, type the exponent.", "Press the right arrow gesture (swipe right on the entry line) to leave the exponent."),
        g("Fractions", "Add and simplify fractions exactly.", "1/2+1/3", "fraction rational",
            "Type 1, tap a/b, type 2, then move right.", "Add the next fraction and press =."),
        g("Mixed numbers", "Show improper fractions as mixed numbers.", "7/3", "mixed fraction improper",
            "Open Settings and turn on Mixed fractions.", "Enter 7/3 and press =; the result shows as 2 1/3."),
        g("Exact and decimal (S<=>D)", "Switch a result between exact and decimal form.", "sqrt(2)", "exact decimal toggle approximate",
            "Press = to get the exact result.", "Tap the result to switch to decimal and tap again to go back."),
        g("Definite integral", "Area under a curve between two limits.", "int(x^2,x,0,3)", "integral area",
            "Open page 4 and tap the definite integral key.", "Fill the function, the variable and both limits, then press ="),
        g("Indefinite integral", "Find an antiderivative.", "int(x*exp(x),x)", "integral antiderivative primitive",
            "Open page 4 and tap the integral key.", "Type the function; the variable x is already filled in."),
        g("Derivative", "Differentiate a function.", "diff(sin(x)^2,x)", "derivative differentiate slope",
            "Open page 4 and tap d/dx.", "Type the function and keep x as the variable."),
        g("Nth derivative", "Differentiate several times.", "diff(x^3,x,2)", "derivative second order",
            "Tap d/dx, then press comma until the last box.", "Type the order, here 2."),
        g("Partial derivative", "Differentiate with respect to one variable of several.", "diff(x^2*y,y)", "partial derivative multivariable",
            "Tap d/dx and replace the variable with y."),
        g("Limits", "Value a function approaches.", "limit(sin(x)/x,x,0)", "limit approach",
            "Open page 4 and tap lim.", "Fill the function, the variable and the point.", "Add a fourth argument 1 or -1 for one-sided limits."),
        g("Sums", "Add terms of a sequence.", "sum(k^2,k,1,10)", "sigma series summation",
            "Tap the sigma key on page 4.", "Fill the term, the index and both bounds."),
        g("Products", "Multiply terms of a sequence.", "product(k,k,1,5)", "pi product factorial",
            "Tap the pi key on page 4.", "Fill the term, the index and both bounds."),
        g("Solve an equation", "Find the roots of an equation.", "solve(x^2-5*x+6=0,x)", "equation roots quadratic polynomial",
            "Open page 4 and tap solve.", "Type the equation using the = option from the ineq key on page 5.", "Keep x as the unknown."),
        g("Systems of equations", "Solve 2 to 4 unknowns at once.", "solve([x+y+z=6,x-y=0,z=3],[x,y,z])", "system linear simultaneous",
            "Tap solve and open a list with the [ ] variant of the ( key.", "Separate equations with commas, then list the unknowns."),
        g("Inequalities", "Solve an inequality.", "solve(x^2-4>=0,x)", "inequality greater less",
            "Tap the ineq key on page 5 and pick >=.", "Use it inside solve."),
        g("Taylor series", "Polynomial approximation around a point.", "taylor(sin(x),x=0,5)", "series expansion maclaurin",
            "Tap taylor on page 4.", "Fill the function, the point as x=0 and the order."),
        g("Matrix determinant", "Determinant of a square matrix.", "det([[1,2],[3,4]])", "matrix determinant",
            "Tap [M] on page 6 to insert a matrix, or open Matrix mode.", "Fill the cells and tap det."),
        g("Matrix inverse", "Inverse of a matrix.", "inv([[1,2],[3,4]])", "matrix inverse",
            "Insert a matrix and tap inv on page 6."),
        g("Eigenvalues", "Eigenvalues of a matrix.", "eigenvals([[2,1],[1,2]])", "matrix eigen eig vector",
            "Insert a matrix and tap eig on page 6.", "Long-press eig for eigenvectors."),
        g("Linear system", "Solve equations written as lines.", "linsolve([x+y=3,x-y=1],[x,y])", "matrix linear system",
            "Tap linsolve on page 5.", "Fill the equation list and the unknown list.", "Matrix mode also solves Ax=b."),
        g("Vector cross product", "Cross product of 3D vectors.", "cross([1,2,3],[4,5,6])", "vector cross",
            "Tap cross on page 6 and fill two vectors."),
        g("Vector dot product", "Dot product of vectors.", "dot([1,2,3],[4,5,6])", "vector dot scalar",
            "Tap dot on page 6 and fill two vectors."),
        g("Vector norm", "Length of a vector.", "norm([3,4])", "vector norm magnitude length",
            "Long-press dot on page 6 and choose norm."),
        g("Complex numbers", "Use i, magnitude, argument and conjugate.", "abs(3+4*i)", "complex imaginary modulus",
            "Tap i on page 6.", "Use abs, arg, re, im and conj from the search palette."),
        g("Polar form", "Convert a complex number to polar coordinates.", "polar_coordinates(1+i)", "complex polar angle",
            "Type the number and tap polar on page 6.", "The result is the pair [r, angle]."),
        g("Laplace transform", "Transform a time function to the s domain.", "laplace(sin(2*t),t,s)", "laplace transform mechatronics",
            "Tap Laplace on page 4.", "Fill the function, t and s."),
        g("Inverse Laplace", "Go back from s to t.", "ilaplace(1/(s^2+4),s,t)", "inverse laplace transfer function",
            "Long-press Laplace on page 4 and pick the inverse."),
        g("Z transform", "Transform a sequence to the z domain.", "ztrans(1/2^n,n,z)", "z transform discrete",
            "Long-press Laplace on page 4 and pick Ztrans."),
        g("Differential equation", "Solve an ordinary differential equation.", "desolve(y'=x*y,x,y)", "ode differential equation",
            "Tap desolve on page 4.", "Long-press x to get y' and type the equation, then x and y."),
        g("Unit conversion", "Convert km/h to m/s with exact factors.", "72*1000/3600", "unit convert speed length",
            "Open Modes, then Units.", "Pick a category, a source unit and a target unit.", "The expression is sent to Home and evaluated exactly."),
        g("Base conversion", "Convert an integer to another base.", "convert(255,base,16)", "binary hex octal base",
            "Tap Bin-Hex on page 6 to cycle the display base.", "Long-press it for and, or, xor, not and shifts."),
        g("Regression", "Fit a line through data points.", "linear_regression([1,2,3,4],[2,4,6,8.5])", "regression fit linear statistics",
            "Open Stats mode and enter x and y columns.", "Pick a regression type and read the coefficients and R2."),
        g("Ans and variables", "Reuse the last answer and store values.", "(2+3)*2", "ans previous variable store",
            "Press Ans to insert the previous result.", "Store a value with :=, for example A:=5.", "Variables A to Z and x, y, z, t are saved between sessions."),
        g("Store a variable", "Save a number in a letter.", "A:=5", "variable assign store",
            "Long-press x to open the letter grid.", "Pick A, then := and a value."),
        g("Writing a program", "Define your own function.", "f(x):=x^2+1", "program function define script",
            "Open Programs mode and tap a template.", "Press Run; the function becomes callable from Home."),
        g("Graph: find a zero", "Locate where a function crosses zero.", "fsolve(cos(x)=x,x=0.7)", "graph zero root",
            "Open Graph mode, enter the function and trace near the crossing.", "Tap Zero."),
        g("Graph: find a minimum", "Locate a minimum with the derivative.", "fsolve(diff(x^2-4*x,x)=0,x=1)", "graph minimum maximum extremum",
            "Trace close to the minimum and tap Min."),
        g("Graph: intersection", "Where two functions are equal.", "fsolve(x^2=x+2,x=1)", "graph intersection crossing",
            "Enter two functions and trace near the crossing, then tap Intersect."),
        g("Ratio and proportion", "Solve a proportion with one unknown.", "solve(3/4=x/20,x)", "ratio proportion rule of three",
            "Write both ratios as fractions with = between them.", "Solve for x.")
    )

    fun search(q: String): List<GuideEntry> {
        val k = q.trim().lowercase()
        if (k.isEmpty()) return entries
        return entries.filter { (it.title + " " + it.goal + " " + it.tags).lowercase().contains(k) }
    }
}

class UnitDef(val name: String, val factor: String)
class UnitCat(val name: String, val units: List<UnitDef>)

object Units {
    private fun u(n: String, f: String) = UnitDef(n, f)
    val cats = listOf(
        UnitCat("Length", listOf(u("m", "1"), u("km", "1000"), u("cm", "1/100"), u("mm", "1/1000"), u("um", "1/1000000"), u("in", "254/10000"), u("ft", "3048/10000"), u("yd", "9144/10000"), u("mi", "1609344/1000"), u("nmi", "1852"))),
        UnitCat("Mass", listOf(u("kg", "1"), u("g", "1/1000"), u("mg", "1/1000000"), u("t", "1000"), u("lb", "45359237/100000000"), u("oz", "28349523125/1000000000000"))),
        UnitCat("Time", listOf(u("s", "1"), u("ms", "1/1000"), u("us", "1/1000000"), u("min", "60"), u("h", "3600"), u("day", "86400"))),
        UnitCat("Speed", listOf(u("m/s", "1"), u("km/h", "1000/3600"), u("mph", "1609344/3600000"), u("kn", "1852/3600"))),
        UnitCat("Force", listOf(u("N", "1"), u("kN", "1000"), u("lbf", "44482216152605/10000000000000"), u("kgf", "980665/100000"))),
        UnitCat("Pressure", listOf(u("Pa", "1"), u("kPa", "1000"), u("MPa", "1000000"), u("bar", "100000"), u("atm", "101325"), u("psi", "6894757293168/1000000000"), u("mmHg", "133322387415/1000000000"))),
        UnitCat("Energy", listOf(u("J", "1"), u("kJ", "1000"), u("Wh", "3600"), u("kWh", "3600000"), u("cal", "4184/1000"), u("kcal", "4184"), u("eV", "1602176634/10000000000000000000000000000"))),
        UnitCat("Power", listOf(u("W", "1"), u("kW", "1000"), u("hp", "7456998715822702/10000000000000"))),
        UnitCat("Angle", listOf(u("rad", "1"), u("deg", "pi/180"), u("grad", "pi/200"), u("rev", "2*pi"), u("arcmin", "pi/10800"))),
        UnitCat("Frequency", listOf(u("Hz", "1"), u("kHz", "1000"), u("MHz", "1000000"), u("GHz", "1000000000"), u("rpm", "1/60"))),
        UnitCat("Temperature", listOf(u("C", "C"), u("F", "F"), u("K", "K")))
    )

    fun convert(v: String, cat: UnitCat, from: UnitDef, to: UnitDef): String {
        if (cat.name == "Temperature") {
            val k = when (from.name) { "C" -> "($v)+273.15"; "F" -> "(($v)-32)*5/9+273.15"; else -> "($v)" }
            return when (to.name) { "C" -> "($k)-273.15"; "F" -> "(($k)-273.15)*9/5+32"; else -> k }
        }
        return "($v)*(${from.factor})/(${to.factor})"
    }
}

class ConstDef(val name: String, val sym: String, val value: String, val unit: String)

object Consts {
    val all = listOf(
        ConstDef("Speed of light", "c", "299792458", "m/s"),
        ConstDef("Planck", "h", "6.62607015e-34", "J s"),
        ConstDef("Reduced Planck", "hbar", "1.054571817e-34", "J s"),
        ConstDef("Elementary charge", "e", "1.602176634e-19", "C"),
        ConstDef("Boltzmann", "kB", "1.380649e-23", "J/K"),
        ConstDef("Avogadro", "NA", "6.02214076e23", "1/mol"),
        ConstDef("Gas constant", "R", "8.314462618", "J/(mol K)"),
        ConstDef("Standard gravity", "g", "9.80665", "m/s2"),
        ConstDef("Gravitation", "G", "6.6743e-11", "m3/(kg s2)"),
        ConstDef("Vacuum permittivity", "eps0", "8.8541878128e-12", "F/m"),
        ConstDef("Vacuum permeability", "mu0", "1.25663706212e-6", "N/A2"),
        ConstDef("Stefan-Boltzmann", "sigma", "5.670374419e-8", "W/(m2 K4)"),
        ConstDef("Electron mass", "me", "9.1093837015e-31", "kg"),
        ConstDef("Proton mass", "mp", "1.67262192369e-27", "kg"),
        ConstDef("Faraday", "F", "96485.33212", "C/mol"),
        ConstDef("Standard atmosphere", "atm", "101325", "Pa"),
        ConstDef("Fine structure", "alpha", "7.2973525693e-3", "")
    )
}

object Templates {
    val programs = listOf(
        "Factorial" to "fact(n):={\n  local k,r;\n  r:=1;\n  for k from 1 to n do r:=r*k; od;\n  return r;\n};\nfact(5)",
        "Digit sum" to "digsum(n):={\n  local s;\n  s:=0;\n  while n>0 do s:=s+irem(n,10); n:=iquo(n,10); od;\n  return s;\n};\ndigsum(12345)",
        "Bisection" to "bisect(f,a,b):={\n  local m,k;\n  for k from 1 to 40 do\n    m:=(a+b)/2;\n    if f(a)*f(m)<=0 then b:=m; else a:=m; fi;\n  od;\n  return evalf(m);\n};\ng(x):=x^2-2;\nbisect(g,1,2)"
    )
}

class Dist(val name: String, val params: List<String>, val pdf: String, val cdf: String, val inv: String)

object Dists {
    val all = listOf(
        Dist("Normal", listOf("mu", "sigma"), "normald", "normald_cdf", "normald_icdf"),
        Dist("Student t", listOf("n"), "student", "student_cdf", "student_icdf"),
        Dist("Chi2", listOf("n"), "chisquare", "chisquare_cdf", "chisquare_icdf"),
        Dist("F", listOf("n", "m"), "fisher", "fisher_cdf", "fisher_icdf"),
        Dist("Binomial", listOf("n", "p"), "binomial", "binomial_cdf", "binomial_icdf"),
        Dist("Poisson", listOf("lambda"), "poisson", "poisson_cdf", "poisson_icdf")
    )
}

class FDef(val name: String, val argc: Int, val desc: String)

object Aliases {
    val map = mapOf(
        "integral" to "int", "integrate" to "int", "derivative" to "diff", "differentiate" to "diff", "cube root" to "surd", "nth root" to "surd",
        "log base" to "logb", "determinant" to "det", "inverse" to "inv", "eigenvalue" to "eigenvals", "eigenvector" to "eigenvects",
        "regression" to "linear_regression", "standard deviation" to "stddev", "average" to "mean", "combination" to "comb", "permutation" to "perm",
        "remainder" to "irem", "quotient" to "iquo", "prime" to "ifactor", "absolute" to "abs", "random" to "rand", "sigma" to "sum",
        "series" to "taylor", "differential" to "desolve", "ode" to "desolve", "equation" to "solve", "system" to "linsolve", "cross product" to "cross",
        "dot product" to "dot", "magnitude" to "norm", "z transform" to "ztrans", "gcd" to "gcd", "greatest" to "gcd", "least common" to "lcm"
    )
}
