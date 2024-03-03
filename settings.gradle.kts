pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
    }

    versionCatalogs {
        create("localDeps") {
            library("kotlin", "androidx.core:core-ktx:1.12.0")
            library("appCompat", "androidx.appcompat:appcompat:1.6.1")
            library("materialDesign", "com.google.android.material:material:1.11.0")
            library("constraintLayout", "androidx.constraintlayout:constraintlayout:2.1.4")

            library("dagger", "com.google.dagger:dagger:2.51")
            library("compiler", "com.google.dagger:dagger-compiler:2.51")

            library("glide", "com.github.bumptech.glide:glide:3.7.0")
            library("cicerone", "com.github.terrakok:cicerone:7.1")
            library("neumorphism", "com.github.fornewid:neumorphism:0.3.2")
        }
        create("unitTestDeps") {
            library("jUnit", "junit:junit:4.13.2")
            library("robolectric", "org.robolectric:robolectric:4.4")
            library("mockito", "org.mockito:mockito-core:5.1.1")
        }
        create("uiTestDeps") {
            library("extJUnit", "androidx.test.ext:junit:1.1.5")
            library("espresso", "androidx.test.espresso:espresso-core:3.5.1")
            library("rules", "androidx.test:rules:1.5.0")
            library("runner", "androidx.test:runner:1.5.2")
        }
    }
}

rootProject.name = "FlexyPixel"
include(":app")
 