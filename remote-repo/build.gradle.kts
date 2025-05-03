import com.android.tools.build.jetifier.core.utils.Log
import java.util.Properties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "ru.alexp0111.remote_repo"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        var baseUrl = "http://your_url_here/"
        try {
            val properties = Properties()
            project.rootDir.resolve("local.properties").inputStream().use { properties.load(it) }
            baseUrl = properties["BASE_URL"] as String
        } catch (_: Exception) {}

        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        buildConfig = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    /** Modules */
    implementation(project(":core"))

    /** Gson */
    implementation(localDeps.gson)

    /** Dagger2 */
    implementation(localDeps.dagger)
    kapt(localDeps.compiler)

    /** Retrofit */
    implementation(localDeps.retrofit)
    implementation(localDeps.retrofitConverter)
    implementation(localDeps.loggingInterceptor)

    /** Security */
    implementation(localDeps.securityCrypto)
}