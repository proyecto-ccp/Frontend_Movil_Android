import org.gradle.testing.jacoco.tasks.JacocoReport

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("org.sonarqube")
    id("kotlin-parcelize")
    id("jacoco")
}

android {
    namespace = "com.uxdesign.cpp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.uxdesign.cpp"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "4.7.8"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        getByName("debug") {
            isTestCoverageEnabled = true  // Habilita la cobertura de pruebas en Debug
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

jacoco {
    toolVersion = "0.8.7" // Asegúrate de usar una versión compatible de JaCoCo
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn(tasks.named("testDebugUnitTest")) // Ejecuta las pruebas unitarias antes de generar el reporte

    reports {
        html.required.set(true)
        html.outputLocation.set(file("$buildDir/reports/jacoco/html"))

        xml.required.set(true)
        xml.outputLocation.set(file("$buildDir/reports/jacoco/xml"))
    }


    // Define las fuentes y clases compiladas para calcular la cobertura
    sourceDirectories.setFrom(files("src/main/java"))
    classDirectories.setFrom(fileTree("build/tmp/kotlin-classes/debug") {
        include("**/*.class")
    }) // Asegúrate de que esta ruta sea correcta

    // Define la ubicación del archivo de ejecución de JaCoCo
    executionData.setFrom(fileTree("build/outputs/unit_test_code_coverage/debugUnitTest") { include("testDebugUnitTest.exec") })

}

dependencies {

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.crashlytics.buildtools)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation(libs.androidx.junit.ktx)
    //implementation(libs.core.ktx)

    // Dependencias de JUnit y Mockito
    testImplementation("org.mockito:mockito-core:4.8.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.squareup.okhttp3:okhttp:4.9.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.0")
    testImplementation("org.robolectric:robolectric:4.6.1")

    // Dependencias de AndroidTest
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

sonarqube {
    properties {
        property("sonar.projectKey", "proyecto-ccp_Frontend_Movil_Android")
        property("sonar.projectName", "Frontend Movil Android")
        property("sonar.organization", "proyecto-ccp-1")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.login", "2cad78399830ff55cddc8a9c9fa1be13073d0937")
        property("sonar.sources", "src/main/java")
        property("sonar.tests", "src/test/java")

        // 🟢 Cambia la ruta de clases compiladas
        property("sonar.java.binaries", "build/tmp/kotlin-classes/debug")

        // Opcional: Reportes de pruebas
        property("sonar.junit.reportPaths", "build/test-results/testDebugUnitTest")

        // 🟢 Reporte de cobertura de JaCoCo
        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/xml/report.xml")

        property("sonar.junit.reportPaths", "build/test-results/testDebugUnitTest") // Reporte de pruebas JUnit
        property("sonar.jacoco.reportPaths", "build/reports/jacoco/testDebugUnitTest.exec") // Reporte de JaCoCo
    }
}
