plugins {
    id("sentrylogger.shadow-platform")
    id("de.eldoria.plugin-yml.paper") version "0.7.1"
}

paper {
    name = "SentryLogger"
    version = project.version.toString()
    apiVersion = "1.21"
    author = "budi1200"
    main = "si.budimir.sentrylogger.paper.SentryLoggerPaper"
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(projects.sentryloggerCommon)
    implementation(libs.cloud.paper)

    compileOnly(libs.paper.api)
}

tasks {
    shadowJar {
        platformRelocation("paper", "si.budimir.sentrylogger.common")
    }

    // Use the common buildAndPush task definition
    createBuildAndPushTask(project, "${rootProject.projectDir.parent}\\00-paper\\plugins")
}

