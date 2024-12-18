plugins {
    // alias(libs.plugins.android.application)
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.planetze"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.planetze"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    testImplementation (libs.junit.junit.v412)
    testImplementation(libs.mockito.mockito.all)
    testImplementation(libs.mockito.all)
    implementation("androidx.recyclerview:recyclerview:1.3.0")

    testImplementation (libs.mockito.core.v4110)

    // BOM for firebase platform
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    // Firebase Products
    implementation(libs.google.firebase.database)
    implementation(libs.firebase.auth)

    // External Resources
    implementation("com.github.cjhandroid:WaveProgressBar:v1.0.0")
    implementation("com.github.anastr:speedviewlib:1.6.1")
    implementation("com.github.bumptech.glide:glide:4.15.1")

}