plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
    alias(libs.plugins.compose.compiler)
    kotlin("plugin.serialization") version "2.0.0"
    id("com.google.dagger.hilt.android")
    id ("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.safekeys"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.safekeys"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.safekeys.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
//    implementation(libs.androidx.runner)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("androidx.compose.material:material-icons-extended-android:1.5.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.1")

    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    //annotationProcessor("androidx.room:room-compiler:$room_version")
    ksp("androidx.room:room-compiler:$room_version")

    implementation ("org.mindrot:jbcrypt:0.4")

    implementation("androidx.navigation:navigation-compose:2.8.0-beta02")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    val hilt_version = "2.51.1"
    implementation("com.google.dagger:hilt-android:$hilt_version")
    ksp("com.google.dagger:hilt-android-compiler:$hilt_version")
    ksp("androidx.hilt:hilt-compiler:1.2.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    //local unit tests
    testImplementation("androidx.test:core:1.4.0")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
    testImplementation("com.google.truth:truth:1.1.3")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.8")

    //instrumentation tests
    androidTestImplementation("com.google.dagger:hilt-android-testing:$hilt_version")
    kspAndroidTest("com.google.dagger:hilt-android-compiler:$hilt_version")
    androidTestImplementation("junit:junit:4.13.2")
    androidTestImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.1")
    androidTestImplementation ("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation ("com.google.truth:truth:1.1.3")
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test:core-ktx:1.6.1")
    androidTestImplementation(libs.androidx.runner)
    //androidTestImplementation ("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation ("androidx.test:rules:1.6.1")
//    androidTestImplementation("io.mockk:mockk-android:1.10.5")
    // Test rules and transitive dependencies:
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.6.8")
// Needed for createComposeRule(), but not for createAndroidComposeRule<YourActivity>():
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.8")

}