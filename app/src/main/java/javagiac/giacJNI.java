package javagiac;

/** Minimal JNI surface of GeoGebra's Giac wrapper (libjavagiac.so). Names and signatures match src/jni/cpp/giac_wrap.cxx. */
public final class giacJNI {
    static { System.loadLibrary("javagiac"); }

    private giacJNI() {}

    public static native long new_context__SWIG_0();
    public static native void delete_context(long ctx);
    public static native long new_gen__SWIG_25(String s, long ctx, Object ctxObj);
    public static native void delete_gen(long gen);
    public static native long gen_eval(long gen, Object genObj, int level, long ctx, Object ctxObj);
    public static native String gen_print__SWIG_0(long gen, Object genObj, long ctx, Object ctxObj);
}
