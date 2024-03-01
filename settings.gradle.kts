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
    }

    versionCatalogs {
        create("localDeps") {
            library("kotlin", "androidx.core:core-ktx:1.12.0")
            library("appCompat", "androidx.appcompat:appcompat:1.6.1")
            library("materialDesign", "com.google.android.material:material:1.11.0")
            library("constraintLayout", "androidx.constraintlayout:constraintlayout:2.1.4")
        }
        create("unitTestDeps") {
            library("jUnit", "junit:junit:4.13.2")
        }
        create("uiTestDeps") {
            library("extJUnit", "androidx.test.ext:junit:1.1.5")
            library("espresso", "androidx.test.espresso:espresso-core:3.5.1")
        }
    }
}

rootProject.name = "FlexyPixel"
include(":app")
 