plugins {
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinParcelizePlugin)
    id(BuildPlugins.ktlintPlugin)
    id(BuildPlugins.jacocoAndroid)
    id(BuildPlugins.kotlinKapt)
    id(BuildPlugins.hiltPlugin)
}

jacoco {
    toolVersion = Versions.jacoco
}

android {

    compileSdk = AndroidSdk.compileSdkVersion
    android.buildFeatures.dataBinding = true
    android.buildFeatures.viewBinding = true

    defaultConfig {
        applicationId = "com.keronei.weatherapp"
        minSdk = AndroidSdk.minSdkVersion
        targetSdk = AndroidSdk.targetSdkVersion
        versionCode = AndroidSdk.versionCode
        versionName = AndroidSdk.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions {
        animationsDisabled = true
        unitTests.apply {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    dependencies {
        implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
        implementation(Libraries.appCompat)
        implementation(Libraries.constraintLayout)
        implementation(Libraries.materialComponents)

        //Hilt - DI
        implementation(Libraries.daggerHilt)
        kapt(Libraries.hiltCompiler)
        implementation(Libraries.hiltViewModel)
        kapt(Libraries.hiltAndroidxCompiler)

        //Room
        implementation(Libraries.room)
        kapt(Libraries.room_compiler)
        androidTestImplementation(Libraries.room_testing)

        //Timber
        implementation(Libraries.timber)

        //Network
        implementation(Libraries.retrofit)
        implementation(Libraries.gson)
        implementation(platform(Libraries.okhttp3BOM))
        implementation(Libraries.okhttp3)
        implementation(Libraries.loggingInterceptor)

        //Coroutines
        implementation(Libraries.coroutines)
        implementation(Libraries.coroutinesAndroid)

        //Testing
        androidTestImplementation(TestLibraries.testRunner)
        androidTestImplementation(TestLibraries.espresso)
        androidTestImplementation(TestLibraries.annotation)

        testImplementation(TestLibraries.junit4)
    }
}