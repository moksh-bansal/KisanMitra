plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.appdev.kisanmitra"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.appdev.kisanmitra"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    // Import the BoM for the Firebase platform
    implementation (platform("com.google.firebase:firebase-bom:34.9.0")) // Check for latest version on the documentation

    // Add the dependencies for the Firebase products you want to use (e.g., Authentication)
    implementation ("com.google.firebase:firebase-auth")
    // For Cloud Firestore
    implementation ("com.google.firebase:firebase-firestore")
    // For Analytics
    implementation ("com.google.firebase:firebase-analytics")
    // Material Design
    implementation("com.google.android.material:material:1.13.0")
    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    // Firebase Storage
    implementation("com.google.firebase:firebase-storage")
    // Glide for images
    implementation("com.github.bumptech.glide:glide:4.16.0")
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // OkHttp logging (optional but useful)
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.google.android.gms:play-services-location:21.2.0")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("org.tensorflow:tensorflow-lite:+")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}