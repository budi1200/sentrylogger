plugins {
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.cloud.core)
    implementation(libs.cloud.kotlin.extensions)
    implementation(libs.cloud.minecraft.extras)
}

version = (version as String)
group = group as String