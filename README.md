# ANGELINA

Personal scientific and CAS calculator for Android 12 to 17 (minSdk 31, targetSdk 37). Kotlin, Jetpack Compose, one Activity, MVVM.
Math engine: Giac (the HP Prime CAS). Dark theme, portrait only, English UI, decimal point ".".
Package: `com.digitalminds.angelina`. Brand: DIGITALMINDS family.

> Status: this project was written without access to Maven or the Android SDK, so the Android and Compose code has never been compiled.
> What was run: the pure Kotlin core (`Expr.kt`, `NumEval.kt`, `Data.kt`) was compiled with kotlinc 2.2.20 and its 42 unit tests pass,
> including a comparison of 145 golden expressions against real Giac 1.9.0.93. See "What is verified" at the end.

## 1. Push to GitHub and get the APK

1. Create an empty repository on GitHub (private is fine).
2. In this folder run:
   ```
   git init
   git add .
   git commit -m "ANGELINA first delivery"
   git branch -M main
   git remote add origin https://github.com/<you>/angelina.git
   git push -u origin main
   ```
3. Open the repository, tab **Actions**, workflow **build**. When it turns green open the run and download the artifact
   **angelina-debug-apk** (one APK per ABI: `app-arm64-v8a-debug.apk`, `app-armeabi-v7a-debug.apk`). Install the one that matches your phone
   (almost every phone from the last 8 years is arm64-v8a).
4. The workflow builds Giac first (cached afterwards). That step is best effort: if it fails the APK still builds, but the calculator will say
   "The math engine is not installed" until `app/libs/giac-android.aar` exists.

## 2. Build Giac locally and wire it

The GeoGebra mirror (https://github.com/geogebra/giac, `giac-android`) contains only the native wrapper. Its JNI functions live in package
`javagiac`, class `giacJNI`, but the Java classes are not in that repo. So this project ships the tiny Java side itself:
`app/src/main/java/javagiac/giacJNI.java` (6 native methods, signatures copied from `src/jni/cpp/giac_wrap.cxx`).
The AAR you build therefore only has to provide `libjavagiac.so` and `libc++_shared.so`.

```
export ANDROID_HOME=$HOME/Android/Sdk
export ANDROID_NDK_HOME=$ANDROID_HOME/ndk/28.2.13676358
./scripts/build-giac.sh
```
The script clones Giac, builds only armeabi-v7a and arm64-v8a, copies the result to `app/libs/giac-android.aar` and checks that the 5 JNI symbols
the app calls are present in each `.so`. `app/build.gradle.kts` picks the AAR up with `implementation(files("libs/giac-android.aar"))` when the file exists.
Then build normally: `./gradlew assembleDebug`.

Why a separate process: the wrapper exposes no interrupt function. Giac therefore runs in `android:process=":cas"` (`CasService`, bound with a Messenger).
It owns one thread with a 16 MB stack, starts lazily and is warmed up at app start. The timeout is 10 s by default (10, 30 or 60 in Settings) and the
**Cancel** button appears while it works. Cancel or timeout kills only the `:cas` process; the UI stays alive, Android rebinds the service and your stored
variables and functions are replayed. Cost: a second process uses extra RAM (see section 3).

## 3. Measure size, start-up time and RAM

* APK size: Android Studio, **Build > Analyze APK...** on `app-arm64-v8a-release.apk`, or
  `$ANDROID_HOME/cmdline-tools/latest/bin/apkanalyzer apk summary app-arm64-v8a-release.apk`.
  Native libraries are stored compressed (`useLegacyPackaging = true`) to keep the download small; installed size is larger.
* Cold start: `adb shell am force-stop com.digitalminds.angelina && adb shell am start -W -n com.digitalminds.angelina/.MainActivity`
  and read `TotalTime`. Repeat 5 times and take the median.
* RAM: after using it for a minute, `adb shell dumpsys meminfo com.digitalminds.angelina`. The `:cas` process appears as
  `com.digitalminds.angelina:cas`; add both numbers. Look at "TOTAL PSS".
* Build the release for measuring: `./gradlew assembleRelease` (R8 full mode, resource shrinking, signed with the debug key, personal use).

## 4. Version choices

| Item | Version | Status |
|---|---|---|
| Android Gradle Plugin | 9.4.0 | Verified in the official release notes: supports API 37, needs Gradle 9.6.0, JDK 17 |
| Gradle | 9.6.0 | Verified (required by AGP 9.4.0) |
| Kotlin and Compose compiler plugin | 2.4.20 | Verified to exist (released 7 Sep 2026); compatibility with AGP 9.4.0 built-in Kotlin not tested |
| Compose BOM | 2026.09.00 | Verified to exist in the official docs; mapping to Compose 1.12.1 (current stable) not confirmed |
| activity-compose | 1.13.0 | Verified in the stable-channel list |
| lifecycle-viewmodel-compose | 2.11.0 | Verified in the stable-channel list |
| profileinstaller | 1.4.1 | Not verified as the latest. Needed so the baseline profile installs on sideloaded APKs |
| JUnit | 4.13.2 | Not verified as the latest |
| NDK (CI) | 28.2.13676358 | Verified as AGP 9.4.0 default |

If Gradle sync complains, change the numbers in `gradle/libs.versions.toml` only. A `buildscript` classpath entry for the Kotlin Gradle plugin is in
the root `build.gradle.kts` because AGP 9 bundles its own Kotlin and the Compose plugin must match.
The only dependencies beyond the allowed list are `profileinstaller` (baseline profile on sideload) and, for debug builds only, `ui-tooling-preview`.

## 5. Brand and palette roles

All colors are tokens in `Brand.kt` (two XML colors, `bg` and `icon_bg`, exist because the splash needs them).

| Role | Color |
|---|---|
| background | #000000 |
| surface (panels, function keys) | #151517 |
| number keys | #1F2023 |
| operator keys | #2B2C31 (lifted graphite, derived) |
| accent, equals key, progress | Ferrari red #DC0000, bright #FF2A1F |
| text / secondary text | #FFFFFF / #9A9CA4 |
| icon background | #BEBEBE |
| error | orchid rose #FF5C8A (never red, so it cannot be confused with the accent) |
| graph series and highlights | red, blue #5B9BFF, violet #A78BFA, orange #FF9A4D, pink #FF6FAE |

Font: Space Grotesk Regular and Bold, subset to Latin plus math symbols (17 KB each). Σ and Π are not in that font and fall back to the system font.
Logo: the metallic logo was converted to transparent WebP (it has gradients that a vector trace would lose). The adaptive icon has a monochrome layer.
Splash: native Android 12+ splash (icon on #BEBEBE, black background, "POWERED BY DIGITALMINDS" branding image where the system shows it).

## 6. Notes on behavior

* Home always evaluates through Giac. Tap a result: exact and decimal. Long-press: copy, use as Ans, copy or reuse the input.
* Angles: degrees and grads are handled by scaling trig arguments before sending to Giac (Giac always runs in radians). Inverse trig results are wrapped in `simplify` so `asin(1/2)` gives `30` in degrees.
* `mod` is sent as `irem`. Cube roots are sent as `simplify(surd(a,3))`. `stddev` is the population deviation and `stddevp` the sample one, as observed in Giac; the Stats screen labels them that way.
* Physical constants and unit factors are a compact data table (CODATA 2018 and exact definitions); conversions are sent to Giac as exact rational expressions.
* Variables A to Z and x, y, z, t are persisted: every assignment (`A:=5`, `f(x):=...`) and every run program is stored and replayed into Giac at start.
* Graph and Table use a small own evaluator (Double plus a Complex class). It is checked against Giac in `NumEvalTest` (11 golden numeric cases).
  Zero, minimum, maximum, intersection and integral on the graph are computed by Giac.

## 7. What is verified, and what is not

Verified here: the core compiles and 42 unit tests pass (serialization, cursor logic, numeric evaluator, golden file). 145 golden expressions match real Giac 1.9.0.93.
All 80+ Giac command names used by the keypad were run in real Giac. The search catalog (1,208 functions) only contains commands whose own help example evaluates in Giac.

Not verified: that the Android and Compose code compiles and looks right on a device; that `scripts/build-giac.sh` completes; that the JNI stub matches the
`libjavagiac.so` you build (the script checks the symbol names); the Giac 1.9.0.93 used for the golden file is the Linux package, the mobile build may be a newer revision.
Not verified Giac names: `iztrans` (does not evaluate in 1.9.0.93, so it is not offered). Units `_in` and temperature through Giac `convert` failed, so Units uses its own table.

Not delivered, with reason:
* Matrix mode has no separate "eigen decomposition" display beyond `eigenvals` and `eigenvects`; SVD is available through the search palette only.
* Fourier and Z-transform are available through the Laplace key long-press, not a dedicated key.
* A device-measured APK size and RAM figure: needs a real build.
