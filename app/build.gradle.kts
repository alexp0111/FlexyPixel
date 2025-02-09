plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
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
    buildFeatures {
        viewBinding = true
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
    /** Modules */
    implementation(project(":core"))
    implementation(project(":core-ui"))

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

    /** Dagger2 */
    implementation(localDeps.dagger)
    kapt(localDeps.compiler)

    /** Glide */
    implementation(localDeps.glide)

    /** Cicerone */
    implementation(localDeps.cicerone)

    /** Neumorphism */
    implementation(localDeps.neumorphism)

    /** Gson */
    implementation(localDeps.gson)

    /** Room */
    implementation(localDeps.room)
    kapt(localDeps.roomCompiler)

    /** Unit testing */
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(platform(unitTestDeps.jUnitBom))
    testImplementation(unitTestDeps.robolectric)
    testImplementation(unitTestDeps.mockito)

    /** Instrumented testing */
    androidTestImplementation(uiTestDeps.extJUnit)
    androidTestImplementation(uiTestDeps.espresso)
    androidTestImplementation(uiTestDeps.rules)
    androidTestImplementation(uiTestDeps.runner)

    /** Documentation */
    implementation(docs.kotlinDoc)
}