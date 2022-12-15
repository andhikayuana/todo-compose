pluginManagement {
    plugins {
        id("org.jetbrains.kotlin.jvm") version ("1.6.10")
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id.equals("com.google.gms.google-services")) {
                useModule("com.google.gms:google-services:4.3.13")
            }
        }
    }
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "TodoCompose"
include(":app")
