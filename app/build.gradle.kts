plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.services)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp.android)
    alias(libs.plugins.vkid.manifest.placeholders)

    id("kotlin-parcelize")
    kotlin("plugin.serialization").version(libs.versions.kotlin.serialization)
}

android {
    namespace = "com.example.reflect"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.reflect"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Room
//    implementation(libs.room.runtime)
//    implementation(libs.androidx.legacy.support.v4)
//    implementation(libs.androidx.lifecycle.livedata.ktx)
//    implementation(libs.androidx.lifecycle.viewmodel.ktx)
//    implementation(libs.androidx.fragment.ktx)
//    ksp(libs.room.compiler)
//    implementation(libs.room.ktx)
//    testImplementation(libs.room.testing)

    // Hilt
    implementation (libs.hilt.android)
    ksp (libs.hilt.compiler)

    // ViewBinding
    implementation(libs.vbpd)

    // Splash API
    implementation(libs.androidx.core.splashscreen)

    // Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.navigation.dynamic)
    implementation(libs.navigation.testing)
    implementation(libs.kotlinx.serialization.json)

    // PinView
    implementation(libs.pin.view)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.logging.interceptor)

    // Charts
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Lottie
    implementation(libs.lottie)

    // Export to Excel
    implementation(libs.apache.poi)
    
    // Yandex metrica
    implementation(libs.yandex.analytics)

    // Leak Canary
//    debugImplementation(libs.leakcanary)

    // VK SDK
    implementation(libs.vkid.sdk)
//    implementation(libs.vkid.onetap.xml)

    // Desugaring
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // WorkManager
    implementation(libs.work.runtime)
    implementation(libs.work.runtime.ktx)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.transition)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
