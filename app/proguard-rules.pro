# Giac JNI: native code looks these up by name
-keep class javagiac.** { *; }
-keepclasseswithmembernames class * { native <methods>; }
-keep class com.digitalminds.angelina.CasService { *; }
-dontwarn javax.annotation.**
