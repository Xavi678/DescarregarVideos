import com.android.build.api.dsl.Packaging

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("plugin.serialization").version("2.0.0")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.dagger.hilt)

}

android {
    namespace = "com.ivax.descarregarvideos"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ivax.descarregarvideos"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    buildTypes {
        release {
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        compose=true
    }
    packaging{
        resources.excludes.addAll(
            listOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",
                "META-INF/versions/9"
        )
        )
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.json)
    implementation(libs.android.room.ktx)
    implementation(libs.android.room.runtime)
    implementation(libs.dagger.hilt)
    implementation(libs.android.composer.runtime)
    implementation(libs.android.exoplayer)
    implementation(libs.android.exoplayer.dash)
    implementation(libs.android.exoplayer.ui)
    implementation(libs.android.exoplayer.ui.compose)
    implementation(libs.android.exoplayer.session)
    implementation(libs.android.splash.screen)
    implementation(libs.android.compose.ui)
    implementation(libs.android.compose.ui.tooling.preview)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui.android)
    debugImplementation(libs.android.compose.ui.tooling)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    implementation(libs.android.compose.material3)
    implementation(libs.android.activity.compose)
    implementation(libs.android.lifecycle.viewmodel.compose)
    implementation(libs.coil.compose)
    implementation(libs.android.livedata.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.fragment.compose)
    implementation(libs.androidx.navigation.ui.compose)
    implementation(libs.androidx.navigation.dynamic.features.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.material.icons.extended)
    ksp(libs.dagger.compiler)
    ksp(libs.android.room.ksp)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}


