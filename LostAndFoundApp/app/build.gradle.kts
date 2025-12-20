plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)

}

android {
    namespace = "com.example.lostandfoundapp"
    compileSdk = 34 // Changed to stable version and corrected syntax

    defaultConfig {
        applicationId = "com.example.lostandfoundapp"
        minSdk = 24
        targetSdk = 34 // Changed to stable version
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    // Add kotlinOptions to match Java version
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true // Keep ViewBinding enabled
    }
}

dependencies {
    implementation(libs.androidx.core.ktx) // Added core-ktx for good measure
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material) // Material Design - using corrected alias
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview) // For RecyclerView - using corrected alias
    implementation(libs.androidx.cardview)     // For CardView - using corrected alias
    implementation(libs.androidx.swiperefreshlayout) // Optional: For pull-to-refresh - using corrected alias
    implementation(libs.androidx.viewpager2)   // For the ViewPager2 tabs - using corrected alias

    // --- Retrofit & GSON Converter (Networking) ---
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // --- OkHttp (Retrofit's network layer, needed for interceptors & WebSockets) ---
    implementation(libs.okhttp)
    implementation(libs.stomp.protocol.android)
    implementation(libs.rxjava2)
    implementation(libs.rxandroid2)

    // --- Glide (Image Loading) ---
    implementation(libs.glide)
    // --- Testing ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.ext.junit) // using corrected alias
    androidTestImplementation(libs.androidx.espresso.core) // using corrected alias
}