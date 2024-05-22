// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("org.jetbrains.dokka") version "1.9.20"
    kotlin("kapt") version "1.9.22" apply false
}

subprojects {
    plugins.apply("org.jetbrains.dokka")
}