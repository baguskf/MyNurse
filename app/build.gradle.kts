plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.testfirestore"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.testfirestore"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.base)

    implementation(libs.play.services.auth)

//    implementation(libs.androidx.credentials)
//    implementation(libs.androidx.credentials.play.services.auth)
//    implementation(libs.googleid)
//    implementation(libs.firebase.auth)


    implementation ("me.biubiubiu.justifytext:library:1.1")


    implementation (platform(libs.firebase.bom))
    implementation (libs.firebase.auth.ktx)
    implementation (libs.firebase.analytics.ktx)
    implementation (libs.play.services.auth.v2041)
    implementation (libs.firebaseui.firebase.ui.firestore)

    //datepicker material
    implementation (libs.material.v1130alpha05)
    implementation(libs.firebase.firestore)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity.ktx)


}