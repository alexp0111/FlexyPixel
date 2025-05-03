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
            library("lifecycleRuntime", "androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
            library("lifecycleViewModel", "androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
            library("appCompat", "androidx.appcompat:appcompat:1.6.1")
            library("materialDesign", "com.google.android.material:material:1.11.0")
            library("constraintLayout", "androidx.constraintlayout:constraintlayout:2.1.4")
            library("swipeRefreshLayout", "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
            library("activityCompose", "androidx.activity:activity-compose:1.9.3")

            library("dagger", "com.google.dagger:dagger:2.51")
            library("compiler", "com.google.dagger:dagger-compiler:2.51")

            library("glide", "com.github.bumptech.glide:glide:4.10.0")
            library("glideCompose", "com.github.bumptech.glide:compose:1.0.0-beta01")
            library("gson", "com.google.code.gson:gson:2.10.1")
            library("cicerone", "com.github.terrakok:cicerone:7.1")
            library("neumorphism", "com.github.fornewid:neumorphism:0.3.2")

            library("room", "androidx.room:room-ktx:2.6.1")
            library("roomCompiler", "androidx.room:room-compiler:2.6.1")

            library("composeNeomorphism", "me.nikhilchaudhari:composeNeumorphism:1.0.0-alpha02")

            library("retrofit", "com.squareup.retrofit2:retrofit:2.11.0")
            library("retrofitConverter", "com.squareup.retrofit2:converter-gson:2.11.0")
            library("loggingInterceptor", "com.squareup.okhttp3:logging-interceptor:4.12.0")


            library("securityCrypto", "androidx.security:security-crypto:1.0.0")

            library("composeRuntime", "androidx.compose.runtime:runtime:1.7.0")
            library("composeMaterial", "androidx.compose.material3:material3-android:1.3.1")
            library("composeFoundation", "androidx.compose.foundation:foundation-android:1.7.0")
            library("composeUiToolingPreview", "androidx.compose.ui:ui-tooling-preview-android:1.7.6")
            library("composeUiTooling", "androidx.compose.ui:ui-tooling:1.7.6")
        }
        create("unitTestDeps") {
            library("jUnitBom", "org.junit:junit-bom:5.11.4")
            library("robolectric", "org.robolectric:robolectric:4.4")
            library("mockito", "org.mockito:mockito-core:5.1.1")
        }
        create("uiTestDeps") {
            library("extJUnit", "androidx.test.ext:junit:1.1.5")
            library("espresso", "androidx.test.espresso:espresso-core:3.5.1")
            library("rules", "androidx.test:rules:1.5.0")
            library("runner", "androidx.test:runner:1.5.2")
        }
        create("docs") {
            library("kotlinDoc", "org.jetbrains.dokka:android-documentation-plugin:1.9.20")
        }
    }
}

rootProject.name = "FlexyPixel"
include(":app")
include(":core")
include(":core-ui")
include(":local-repo")
include(":bt-service")
include(":media-loader")
include(":remote-repo")
