#!/usr/bin/env bash
# Builds app/libs/giac-android.aar (libjavagiac.so for armeabi-v7a and arm64-v8a) from GeoGebra's Giac mirror.
# Run on Linux, macOS or WSL. Requirements: JDK 17+, git, Android SDK, Android NDK r28 or newer.
#   ANDROID_HOME (or ANDROID_SDK_ROOT)  Android SDK path
#   ANDROID_NDK_HOME                    NDK path, e.g. $ANDROID_HOME/ndk/28.2.13676358
#   GIAC_SRC (optional)                 where to clone the sources (default: ./giac-src)
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
SRC="${GIAC_SRC:-$ROOT/giac-src}"
SDK="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-}}"
NDK="${ANDROID_NDK_HOME:-${ANDROID_NDK_ROOT:-}}"
[ -n "$SDK" ] || { echo "Set ANDROID_HOME to your Android SDK."; exit 1; }
[ -d "$NDK" ] || { echo "Set ANDROID_NDK_HOME to an installed NDK (r28 or newer)."; exit 1; }

case "$(uname -s)" in
  Linux) HOST=linux-x86_64 ;;
  Darwin) HOST=darwin-x86_64 ;;
  *) echo "Unsupported OS. Use Linux, macOS or WSL."; exit 1 ;;
esac
TOOLCHAIN="$NDK/toolchains/llvm/prebuilt/$HOST"
[ -d "$TOOLCHAIN" ] || { echo "NDK toolchain not found at $TOOLCHAIN"; exit 1; }

[ -d "$SRC/.git" ] || git clone --depth 1 https://github.com/geogebra/giac "$SRC"

# The build looks for armv7a-linux-androideabi35-clang++ and aarch64-linux-android35-clang++ in PATH,
# and finds libc++_shared.so through a sibling folder of any PATH entry (sysroot/usr/lib/<triple>).
export PATH="$TOOLCHAIN/bin:$TOOLCHAIN/sysroot/usr/lib/aarch64-linux-android:$PATH"
echo "sdk.dir=$SDK" > "$SRC/local.properties"

cd "$SRC"
# Only the two ABIs this app ships. The x86 tasks are excluded.
./gradlew androidCopyEabiLibjavagiacSo androidCopyArm64LibjavagiacSo giac-android:copyLibcShared giac-android:assembleRelease \
  -x androidCopyX86LibjavagiacSo -x androidCopyX86_64LibjavagiacSo \
  -x javagiacAndroidx86SharedLibrary -x javagiacAndroidx86_64SharedLibrary

AAR="$(find giac-android/build/outputs/aar -name '*release*.aar' | head -n 1)"
[ -f "$AAR" ] || { echo "AAR not produced."; exit 1; }
mkdir -p "$ROOT/app/libs"
cp "$AAR" "$ROOT/app/libs/giac-android.aar"

echo "AAR contents:"
unzip -l "$ROOT/app/libs/giac-android.aar" | grep -E "libjavagiac|libc\+\+_shared" || true
echo "JNI symbol check (the app needs these names):"
TMP="$(mktemp -d)"; unzip -q -o "$ROOT/app/libs/giac-android.aar" 'jni/*' -d "$TMP"
for f in "$TMP"/jni/*/libjavagiac.so; do
  n=$(strings "$f" | grep -c -E 'Java_javagiac_giacJNI_(new_1context_1_1SWIG_10|new_1gen_1_1SWIG_125|gen_1eval|gen_1print_1_1SWIG_10|delete_1gen)$' || true)
  echo "  $f: $n/5 symbols"
done
rm -rf "$TMP"
echo "Done: $ROOT/app/libs/giac-android.aar"
