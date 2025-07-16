plugins {
    id("sentrylogger.shadow-platform")
    alias(libs.plugins.tokenReplacer)
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(projects.sentryloggerCommon)
    implementation(libs.cloud.velocity)

    compileOnly(libs.velocity.api)
    annotationProcessor(libs.velocity.api)
}

tasks {
    shadowJar {
        platformRelocation("velocity", "si.budimir.sentrylogger.common")
    }

    processResources {
        val props = mapOf(
            "version" to project.version,
        )

        inputs.properties(props)

        filesMatching("velocity-plugin.json") {
            expand(props)
        }
    }

    createBuildAndPushTask(project, "${rootProject.projectDir.parent}\\01-velocity\\plugins")
}