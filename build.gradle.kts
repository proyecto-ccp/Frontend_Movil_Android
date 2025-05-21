// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("org.sonarqube") version "3.3" apply false
    id("jacoco")
}

subprojects {
    
    afterEvaluate {
        if (project.hasProperty("sonarqube")) {
            project.plugins.apply("org.sonarqube")
        }
    }
}
