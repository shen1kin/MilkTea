plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.smartstudent"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.smartstudent"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    packagingOptions {
        // 解决 proto 文件冲突
        resources.excludes.add("google/protobuf/field_mask.proto")
        resources.excludes.add("google/protobuf/*.proto")
    }

    configurations.all {
        resolutionStrategy {
            // 强制使用 protobuf-javalite 并统一版本
            force("com.google.protobuf:protobuf-javalite:3.25.5")
            // 排除 protobuf-java
            exclude(group = "com.google.protobuf", module = "protobuf-java")
        }
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.android.sdk)
    implementation (libs.gson) // gson
    implementation ("androidx.recyclerview:recyclerview:1.3.1") //RecyclerView CoordinatorLayout
    implementation("com.jinrishici:android-sdk:1.5") // poem
    implementation ("androidx.appcompat:appcompat:1.6.1") //AppCompat
    implementation ("com.google.android.material:material:1.12.0")//Material Components


    //Jackson
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.15.3")
    implementation ("com.fasterxml.jackson.core:jackson-core:2.15.3")
    implementation ("com.fasterxml.jackson.core:jackson-annotations:2.15.3")





    // MySQL 连接器配置
    implementation(libs.mysql.mysql.connector.java) {
        exclude(group = "com.google.protobuf", module = "protobuf-java")
    }

    implementation(libs.gson)
    implementation(libs.activity)

    // Firebase Firestore 配置
    implementation(libs.firebase.firestore) {
        exclude(group = "com.google.protobuf", module = "protobuf-java")
    }
    implementation(libs.androidx.junit)
    implementation(libs.common)
    implementation(libs.firebase.crashlytics.buildtools)

    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
}