plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.digitalminds.angelina"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.digitalminds.angelina"
        minSdk = 31
        targetSdk = 37
        versionCode = 4
        versionName = "1.3"
    }

    signingConfigs {
        // Fixed personal-use key kept in the repo so every APK has the same signature and updates install over each other.
        create("angelina") {
            storeFile = file("angelina.keystore")
            storePassword = "angelina123"
            keyAlias = "angelina"
            keyPassword = "angelina123"
        }
    }

    splits {
        abi {
            isEnable = true
            reset()
            include("armeabi-v7a", "arm64-v8a")
            isUniversalApk = false
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("angelina")
        }
        debug {
            signingConfig = signingConfigs.getByName("angelina")
        }
    }

    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packaging {
        jniLibs.useLegacyPackaging = true
        resources.excludes += setOf("/META-INF/{AL2.0,LGPL2.1}", "/META-INF/*.version", "DebugProbesKt.bin", "kotlin-tooling-metadata.json")
    }

    androidResources { localeFilters += "en" }
    dependenciesInfo { includeInApk = false; includeInBundle = false }
    testOptions { unitTests.isReturnDefaultValues = true }
}

composeCompiler {
    stabilityConfigurationFiles.add(layout.projectDirectory.file("stability.conf"))
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.profileinstaller)

    val giac = file("libs/giac-android.aar")
    if (giac.exists()) implementation(files(giac))

    debugImplementation(libs.compose.ui.tooling.preview)
    testImplementation(libs.junit)
}
