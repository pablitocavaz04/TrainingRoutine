plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.trainingroutine_pablocavaz"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.trainingroutine_pablocavaz"
        minSdk = 24
        targetSdk = 35
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    //COIL
    implementation(libs.coil)
    implementation(libs.coil.network.okhttp)
    //HILT
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    //GSON
    implementation(libs.gson)
    //ROOM
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    //SHAREDPREFERENCES
    implementation(libs.androidx.security.crypto)
    //GSON
    implementation(libs.gson)
    //CORUTINAS
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    //RETROFIT
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    //Navigation
    implementation (libs.androidx.navigation.ui.ktx.v274)
    implementation (libs.androidx.navigation.fragment.ktx.v274)
    //Fragment
    implementation (libs.androidx.fragment.ktx)
    //Lotties
    implementation (libs.lottie.v600)
    implementation (libs.android.lottie)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation (libs.androidx.constraintlayout)
    //Testting Libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}