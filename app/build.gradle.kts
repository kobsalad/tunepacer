plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

val buildSpotSpeeder = tasks.register<Exec>("buildSpotSpeeder") {
    workingDir = file("src/main/assets/extensions/spotspeeder")
    val isWindows = System.getProperty("os.name").lowercase().contains("windows")
    if (isWindows) {
        commandLine("cmd", "/c", "npm ci && npm run build")
    } else {
        commandLine("sh", "-c", "npm ci && npm run build")
    }

    inputs.file(file("src/main/assets/extensions/spotspeeder/package.json"))
    inputs.file(file("src/main/assets/extensions/spotspeeder/package-lock.json"))
    inputs.dir(file("src/main/assets/extensions/spotspeeder/src"))
    outputs.dir(file("src/main/assets/extensions/spotspeeder/dist"))
}

tasks.named("preBuild").configure {
    dependsOn(buildSpotSpeeder)
}

android {
    namespace = "com.tunepacer"
    compileSdk {
        version = release(37) {
            minorApiLevel = 2
        }
    }

    defaultConfig {
        applicationId = "com.tunepacer"
        minSdk = 36
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }

    lint {
        checkAllWarnings = true
        warningsAsErrors = true
        abortOnError = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.geckoview)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
