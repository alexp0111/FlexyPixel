plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "ru.alexp0111.flexypixel"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.alexp0111.flexypixel"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(localDeps.kotlin)
    implementation(localDeps.appCompat)
    implementation(localDeps.materialDesign)
    implementation(localDeps.constraintLayout)
    testImplementation(unitTestDeps.jUnit)
    androidTestImplementation(uiTestDeps.extJUnit)
    androidTestImplementation(uiTestDeps.espresso)
}