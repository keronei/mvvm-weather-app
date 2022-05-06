object Versions {

    // Version codes for all the libraries
    const val kotlin = "1.6.10"
    const val appCompat = "1.4.1"
    const val constraintLayout = "2.1.3"
    const val ktx = "1.7.0"
    const val material = "1.6.0-alpha02"
    const val dataStore = "1.0.0"
    const val ext_junit = "1.1.3"
    const val lifecycle = "2.0.0"
    const val core = "1.4.0"
    const val activityKtx = "1.3.1"
    const val navigationFragment = "2.3.5"


    // EasyPermissions
    const val easyPermission = "3.0.0"

    // Coroutines
    const val coroutines = "1.3.9"

    // Version codes for all the test libraries
    const val junit4 = "4.13.2"
    const val junit_ktx = "1.1.2"
    const val testRunner = "1.4.1-alpha03"
    const val espresso = "3.5.0-alpha03"
    const val annotation = "1.4.0"

    // Gradle Plugins
    const val ktlint = "10.2.1"
    const val detekt = "1.19.0"
    const val spotless = "6.2.2"
    const val dokka = "1.6.10"
    const val gradleVersionsPlugin = "0.42.0"
    const val gradlePlugin = "7.0.4"
    const val jacoco = "0.8.7"

    // Room
    const val room_version = "2.4.1"

    // DI - Hilt
    const val hilt_version = "2.38.1"
    const val hilt_viewmodel = "1.0.0-alpha03"
    const val hilt_compiler = "1.0.0"
    const val dagger_hilt_compiler = "2.5.0"

    // Network
    const val retrofit = "2.9.0"
    const val okhttp3 = "4.9.0"

    // Converters
    const val gson = "2.6.2"

    // Logging - timber
    const val timber = "5.0.1"

    // work manager
    const val work = "2.5.0"

}

object BuildPlugins {
    //All the build plugins are added here
    const val androidLibrary = "com.android.library"
    const val ktlintPlugin = "org.jlleitschuh.gradle.ktlint"
    const val detektPlugin = "io.gitlab.arturbosch.detekt"
    const val spotlessPlugin = "com.diffplug.spotless"
    const val dokkaPlugin = "org.jetbrains.dokka"
    const val androidApplication = "com.android.application"
    const val kotlinAndroid = "org.jetbrains.kotlin.android"
    const val kotlinParcelizePlugin = "org.jetbrains.kotlin.plugin.parcelize"
    const val gradleVersionsPlugin = "com.github.ben-manes.versions"
    const val jacocoAndroid = "com.hiya.jacoco-android"
    const val kotlinKapt = "kotlin-kapt"
    const val hiltPlugin = "dagger.hilt.android.plugin"
}

object Libraries {
    // Any Library is added here
    const val kotlinStandardLibrary = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val ktxCore = "androidx.core:core-ktx:${Versions.ktx}"
    const val materialComponents = "com.google.android.material:material:${Versions.material}"
    const val toolsBuildGradle = "com.android.tools.build:gradle:${Versions.gradlePlugin}"
    const val activityKtx = "androidx.activity:activity-ktx:${Versions.activityKtx}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.activityKtx}"
    const val navigationFragment =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigationFragment}"


    // DataStore
    const val dataStore = "androidx.datastore:datastore-preferences:${Versions.dataStore}"

    // Easy Permissions
    const val easyPermissions = "pub.devrel:easypermissions:${Versions.easyPermission}"

    // Room
    const val room = "androidx.room:room-ktx:${Versions.room_version}"
    const val room_compiler = "androidx.room:room-compiler:${Versions.room_version}"
    const val room_testing = "androidx.room:room-testing:${Versions.room_version}"

    // Hilt - DI
    const val hiltGradlePlugin =
        "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt_version}"
    const val daggerHilt = "com.google.dagger:hilt-android:${Versions.hilt_version}"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt_version}"
    const val hiltViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.hilt_viewmodel}"
    const val hiltAndroidxCompiler = "androidx.hilt:hilt-compiler:${Versions.hilt_compiler}"
    //const val daggerHiltCompiler = "com.google.dagger:hilt-compiler:${Versions.dagger_hilt_compiler}"

    // Network
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val okhttp3BOM = "com.squareup.okhttp3:okhttp-bom:${Versions.okhttp3}"
    const val okhttp3 = "com.squareup.okhttp3:okhttp"
    const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor"

    // Conversion
    const val gson = "com.squareup.retrofit2:converter-gson:${Versions.gson}"

    // LogCat
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    // Coroutines
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    // Work manager
    const val work = "androidx.work:work-runtime-ktx:${Versions.work}"
    const val hiltWork = "androidx.hilt:hilt-work:${Versions.hilt_compiler}"
    const val multiProcessWorkManager = "androidx.work:work-multiprocess:${Versions.work}"
}

object TestLibraries {
    //any test library is added here
    const val junit4 = "junit:junit:${Versions.junit4}"
    const val junit_ktx = "androidx.test.ext:junit-ktx:${Versions.junit_ktx}"
    const val testRunner = "androidx.test:runner:${Versions.testRunner}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val ext_test_junit4 = "androidx.test.ext:junit-ktx:${Versions.ext_junit}"
    const val core_testing = "androidx.arch.core:core-testing:${Versions.lifecycle}"
    const val core = "androidx.test:core:${Versions.core}"
    const val workmanager_testing = "androidx.work:work-testing:${Versions.work}"
    const val hilt_android_testing = "com.google.dagger:hilt-android-testing:${Versions.hilt_version}"
}


object AndroidSdk {
    const val minSdkVersion = 21
    const val compileSdkVersion = 31
    const val targetSdkVersion = compileSdkVersion
    const val versionCode = 1
    const val versionName = "1.0"
}