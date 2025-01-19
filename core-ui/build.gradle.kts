plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "ru.alexp0111.core_ui"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    buildFeatures {
        compose = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {
    /** Base core libraries */
    implementation(localDeps.kotlin)
    implementation(localDeps.lifecycleRuntime)
    implementation(localDeps.lifecycleViewModel)
    implementation(localDeps.appCompat)
    implementation(localDeps.materialDesign)
    implementation(localDeps.constraintLayout)
    implementation(localDeps.swipeRefreshLayout)

    /** Jetpack Compose */
    implementation(localDeps.composeRuntime)
    implementation(localDeps.composeMaterial)
    implementation(localDeps.composeUiToolingPreview)
    implementation(localDeps.composeUiTooling)
    implementation(localDeps.composeNeomorphism)
}