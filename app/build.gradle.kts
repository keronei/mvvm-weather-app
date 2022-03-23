plugins {
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinParcelizePlugin)
    id(BuildPlugins.ktlintPlugin)
    id(BuildPlugins.jacocoAndroid)
    id(BuildPlugins.kotlinKapt)
    id(BuildPlugins.hiltPlugin)
    id(BuildPlugins.detektPlugin)
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

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }
    }

    testOptions {
        animationsDisabled = true
        unitTests.apply {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }

    sourceSets {
        getByName("androidTest").assets.srcDir("$projectDir/schemas")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }

    dataBinding {
        android.buildFeatures.dataBinding = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    dependencies {
        implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
        implementation(Libraries.appCompat)
        implementation(Libraries.constraintLayout)
        implementation(Libraries.materialComponents)
        implementation(Libraries.activityKtx)
        implementation(Libraries.fragmentKtx)
        implementation(Libraries.navigationFragment)

        // DataStore
        implementation(Libraries.dataStore)

        // Easy permissions
        implementation(Libraries.easyPermissions)

        // Hilt - DI
        implementation(Libraries.daggerHilt)
        kapt(Libraries.hiltCompiler)
        implementation(Libraries.hiltViewModel)
        kapt(Libraries.hiltAndroidxCompiler)

        // Room
        implementation(Libraries.room)
        kapt(Libraries.room_compiler)
        androidTestImplementation(Libraries.room_testing)

        // Timber
        implementation(Libraries.timber)

        // Network
        implementation(Libraries.retrofit)
        implementation(Libraries.gson)
        implementation(platform(Libraries.okhttp3BOM))
        implementation(Libraries.okhttp3)
        implementation(Libraries.loggingInterceptor)

        // Coroutines
        implementation(Libraries.coroutines)
        implementation(Libraries.coroutinesAndroid)

        // Testing
        androidTestImplementation(TestLibraries.testRunner)
        androidTestImplementation(TestLibraries.espresso)
        testImplementation(TestLibraries.junit4)
        implementation(TestLibraries.ext_test_junit4)
        implementation(TestLibraries.core_testing)
    }
}
dependencies {
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
}

