plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "ru.alexp0111.core"
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
    api(localDeps.kotlin)
    implementation(localDeps.lifecycleRuntime)
    implementation(localDeps.lifecycleViewModel)
    implementation(localDeps.appCompat)
    implementation(localDeps.materialDesign)
    implementation(localDeps.constraintLayout)
    implementation(localDeps.swipeRefreshLayout)

    /** Dagger2 */
    implementation(localDeps.dagger)
    kapt(localDeps.compiler)

    /** Jetpack Compose */
    implementation(localDeps.composeRuntime)
    implementation(localDeps.composeMaterial)
    implementation(localDeps.composeUiToolingPreview)
    implementation(localDeps.composeUiTooling)
    implementation(localDeps.composeNeomorphism)

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
}