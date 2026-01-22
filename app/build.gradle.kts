plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.assiggment"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.assiggment"
        minSdk = 24
        targetSdk = 36
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
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material)
    implementation(libs.coil.compose)
    
    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler) // Note: Using kapt for Hilt (requires plugin) or ksp if migrated. Sticking to kapt standard for now or verifying if ksp needed. 
                             // Wait, Hilt uses KAPT usually or KSP recently. 
                             // I need to apply 'kotlin-kapt' plugin if I use kapt.
                             // Actually, let's use ksp if possible? No, standard Hilt usually implies KAPT unless specified.
                             // Let me check if I added the kapt plugin. I didn't.
                             // I should use ksp or kapt. Google recommends KSP now.
                             // But for simplicity/compatibility in this context I'll check what's standard.
                             // Wait, I didn't add the KAPT plugin to the version catalog.
                             // I'll stick to KAPT for Hilt for now, I should add the id.
                             // Actually let's assume KAPT is needed. content below adds kapt plugin.
    
    // Retrofit & OkHttp
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp.logging.interceptor)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
