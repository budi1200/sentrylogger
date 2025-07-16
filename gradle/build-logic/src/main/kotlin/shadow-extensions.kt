import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.Project
import org.gradle.api.tasks.TaskContainer

fun ShadowJar.commonRelocation(pkg: String) {
    relocate(pkg, "${project.group}.${project.rootProject.name.lowercase()}.lib.$pkg")
}

fun ShadowJar.platformRelocation(platform: String, pkg: String) {
    relocate(pkg, "${project.group}.${project.rootProject.name.lowercase()}.lib.platform_$platform.$pkg")
}

fun ShadowJar.commonConfiguration() {
    commonRelocation("kotlin")
    commonRelocation("org.spongepowered.configurate")
    commonRelocation("com.typesafe.config")
    commonRelocation("io.leangen.geantyref")
    commonRelocation("org.incendo.cloud")

    dependencies {
        exclude(dependency("org.jetbrains:annotations"))
    }

    from(project.rootProject.file("LICENSE")) {
        rename { "license-${project.rootProject.name.lowercase()}.txt" }
    }
}

/**
 * Creates a buildAndPush task that copies the output of the shadowJar task to the specified destination.
 *
 * @param destinationPath The path where the JAR file should be copied to
 */
fun TaskContainer.createBuildAndPushTask(project: Project, destinationPath: String) {
    register("buildAndPush") {
        val depends = "shadowJar"

        dependsOn(depends)
        doLast {
            val jar = project.tasks.getByName(depends).outputs.files.singleFile
            val pluginDir = project.file(destinationPath)
            project.copy {
                from(jar)
                into(pluginDir)
            }
        }
    }
}
