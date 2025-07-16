plugins {
    id("sentrylogger.base-conventions")
    alias(libs.plugins.shadow)
}

tasks {
    jar {
        archiveClassifier.set("unshaded")
    }

    shadowJar {
        archiveClassifier.set(null as String?)
        commonConfiguration()
        commonRelocation("io.sentry")

        minimize {
            exclude(dependency("org.incendo:cloud-minecraft-extras:.*")) // Minimization strips out too much
        }
    }
}
