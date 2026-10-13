#!/usr/bin/env bash
# Builds app/libs/giac-android.aar (libjavagiac.so for armeabi-v7a and arm64-v8a) from GeoGebra's Giac mirror.
# Works on Linux, macOS or WSL. Requirements: JDK 17+, git, Android SDK, Android NDK 28.0.12916984 (the version GeoGebra's CI uses) or newer r28.
#   ANDROID_HOME                 Android SDK path
#   ANDROID_NDK_HOME             NDK path, e.g. $ANDROID_HOME/ndk/28.0.12916984
#   GIAC_SRC (optional)          where to clone the sources (default: ./giac-src)
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
SRC="${GIAC_SRC:-$ROOT/giac-src}"
SDK="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-}}"
NDK="${ANDROID_NDK_HOME:-${ANDROID_NDK_ROOT:-}}"
[ -n "$SDK" ] || { echo "ERROR: set ANDROID_HOME to your Android SDK."; exit 1; }
[ -d "$NDK" ] || { echo "ERROR: set ANDROID_NDK_HOME to an installed NDK (r28 or newer)."; exit 1; }

case "$(uname -s)" in
  Linux) HOST=linux-x86_64 ;;
  Darwin) HOST=darwin-x86_64 ;;
  *) echo "ERROR: unsupported OS. Use Linux, macOS or WSL."; exit 1 ;;
esac
TOOLCHAIN="$NDK/toolchains/llvm/prebuilt/$HOST"
[ -d "$TOOLCHAIN" ] || { echo "ERROR: NDK toolchain not found at $TOOLCHAIN"; exit 1; }

[ -d "$SRC/.git" ] || git clone --depth 1 https://github.com/geogebra/giac "$SRC"

# Clang wrappers for API 35 (used by the Giac build) live in bin/.
# copyLibcShared (giac-android/build.gradle) searches every PATH entry for <entry>/../<abi>/libc++_shared.so
# for the four ABIs, and fails if any is missing. GeoGebra's CI puts one sysroot folder on PATH; all four are added here to be safe.
export PATH="$TOOLCHAIN/bin:$PATH"
for triple in arm-linux-androideabi i686-linux-android x86_64-linux-android aarch64-linux-android; do
  export PATH="$TOOLCHAIN/sysroot/usr/lib/$triple:$PATH"
done

echo "sdk.dir=$SDK" > "$SRC/local.properties"
cd "$SRC"
chmod +x gradlew

# GeoGebra's own CI deletes these two calculator-only sources before every JNI/Android build
# (see Jenkinsfile in the Giac repo). The javagiac target compiles every file in src/giac/cpp, so they must go.
rm -f src/giac/cpp/kdisplay.cc src/giac/cpp/k_csdk.c

# Verified task names (build.gradle and giac-android/build.gradle in the Giac repo).
# x86 and x86_64 are skipped: the app ships only armeabi-v7a and arm64-v8a.
./gradlew --no-daemon \
  :androidCopyEabiLibjavagiacSo :androidCopyArm64LibjavagiacSo \
  :giac-android:copyLibcShared :giac-android:assembleRelease \
  -x :androidCopyX86LibjavagiacSo -x :androidCopyX86_64LibjavagiacSo

AAR="$(find giac-android/build/outputs/aar -name '*release*.aar' | head -n 1 || true)"
[ -n "$AAR" ] && [ -f "$AAR" ] || { echo "ERROR: AAR was not produced. Read the first error above."; exit 1; }
mkdir -p "$ROOT/app/libs"
cp "$AAR" "$ROOT/app/libs/giac-android.aar"

echo "AAR contents:"
unzip -l "$ROOT/app/libs/giac-android.aar" | grep -E "libjavagiac|libc\+\+_shared" || true
echo "Done: $ROOT/app/libs/giac-android.aar"
