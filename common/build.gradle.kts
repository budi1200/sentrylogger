plugins {
    id("sentrylogger.base-conventions")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.log4j.core)
    compileOnly(libs.slf4j)
    implementation(libs.sentry.log4j2)

    implementation(platform(libs.configurate.bom))
    implementation(libs.configurate.hocon)
    implementation(libs.configurate.extra.kotlin)

    compileOnly(libs.adventure)
    compileOnly(libs.adventure.minimessage)
}

kotlin {
    jvmToolchain(libs.versions.toolchain.get().toInt())
}