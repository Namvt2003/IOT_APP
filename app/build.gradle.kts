plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.iot_application"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.example.iot_application"
        minSdk = 21
        //noinspection ExpiredTargetSdkVersion
        targetSdk = 21
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    //implementation(libs.appcompat)
    //implementation(libs.material)
    //implementation(libs.activity)
    //implementation(libs.constraintlayout)
    //testImplementation(libs.junit)
    //androidTestImplementation(libs.ext.junit)
    //androidTestImplementation(libs.espresso.core)
    //implementation(libs.org.eclipse.paho.client.mqttv3)
    //implementation(libs.org.eclipse.paho.android.service)
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(libs.activity)
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.1.0")
    implementation("org.eclipse.paho:org.eclipse.paho.android.service:1.1.1")
}