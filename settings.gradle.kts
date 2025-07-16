enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

includeBuild("gradle/build-logic")

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "SentryLogger"

setup("sentrylogger-common", "common")
sequenceOf(
    "paper",
    "velocity"
).forEach(::platform)

fun setup(name: String, dir: String) {
    include(name)
    project(":$name").projectDir = file(dir)
}

fun platform(name: String) = setup("sentrylogger-$name", "platform/$name")