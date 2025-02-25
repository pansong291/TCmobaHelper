plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.tencent.tmgp.sgame"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tencent.tmgp.sgame"
        minSdk = 24
        targetSdk = 34
        versionCode = 20190509
        versionName = "1.5"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            applicationIdSuffix = null
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.jetbrains.kotlin.reflect)
}
