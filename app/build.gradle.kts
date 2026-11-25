import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    id("com.google.devtools.ksp")
}

val signingPropsFile = rootProject.file("signing.properties")
val signingProps = Properties().apply {
    if (signingPropsFile.exists()) {
        load(signingPropsFile.inputStream())
    }
}

val isReleaseTask = gradle.startParameter.taskNames.any { it.contains("Release", ignoreCase = true) }

val buildSpotSpeeder = tasks.register<Exec>("buildSpotSpeeder") {
    description = "Builds the SpotSpeeder WebExtension assets using npm and Vite"
    group = "build"
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
        versionCode = 2
        versionName = "0.1.1"

    }

    signingConfigs {
        create("release") {
            val storeFilePath = System.getenv("SIGNING_STORE_FILE") ?: signingProps.getProperty("storeFile") ?: "release.keystore"
            storeFile = rootProject.file(storeFilePath)
            storePassword = System.getenv("SIGNING_STORE_PASSWORD") ?: signingProps.getProperty("storePassword") ?: ""
            keyAlias = System.getenv("SIGNING_KEY_ALIAS") ?: signingProps.getProperty("keyAlias") ?: ""
            keyPassword = System.getenv("SIGNING_KEY_PASSWORD") ?: signingProps.getProperty("keyPassword") ?: ""
        }
    }

    buildTypes {
        release {
            val releaseConfig = signingConfigs.getByName("release")
            if (releaseConfig.storeFile != null && releaseConfig.storeFile!!.exists()) {
                signingConfig = releaseConfig
            } else if (isReleaseTask) {
                throw GradleException("Release keystore not found at '${releaseConfig.storeFile?.absolutePath}'. Please place 'release.keystore' in the root project directory or configure signing.properties (see signing.properties.example).")
            }
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
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.geckoview)
    implementation(libs.play.services.location)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
