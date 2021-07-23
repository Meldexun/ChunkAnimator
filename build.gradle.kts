import java.time.Instant
import java.time.format.DateTimeFormatter

fun property(key: String) = project.findProperty(key).toString()

plugins {
    id("java")
    id("net.minecraftforge.gradle")
    id("idea")
    id("maven-publish")
}

val modName = property("modName")
val modId = property("modId")
val modVersion = property("modVersion")

val mcVersion = property("mcVersion")

version = "$mcVersion-$modVersion"
group = property("group")

minecraft {
    mappings("official", mcVersion)
    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))

    runs {
        create("client") {
            workingDirectory = file("run").absolutePath

            property("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")
            property("forge.logging.console.level", "debug")

            mods {
                create(modId) {
                    source(sourceSets.main.get())
                }
            }
        }

        create("server") {
            workingDirectory = file("run").absolutePath

            property("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")
            property("forge.logging.console.level", "debug")

            mods {
                create(modId) {
                    source(sourceSets.main.get())
                }
            }
        }
    }
}

dependencies {
    minecraft("net.minecraftforge:forge:$mcVersion-${property("forgeVersion")}")
}

java {
    withSourcesJar()

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
    }
}

tasks.jar {
    manifest.attributes(
            "Specification-Title" to project.name,
            "Specification-Vendor" to "lumien231", // lumien231 was the original author of this mod.
            "Specification-Version" to project.version,
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version,
            "Implementation-Vendor" to "Harley O'Connor", // I am the author of this port.
            "Implementation-Timestamp" to DateTimeFormatter.ISO_INSTANT.format(Instant.now())
    )

    finalizedBy("reobfJar")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "$modName-$mcVersion"
            version = modVersion

            from(components["java"])

            pom {
                name.set(modName)
                url.set("https://github.com/Harleyoc1/$modName")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://mit-license.org")
                    }
                }
                developers {
                    developer {
                        id.set("Harleyoc1")
                        name.set("Harley O'Connor")
                        email.set("harleyoc1@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/Harleyoc1/$modName.git")
                    developerConnection.set("scm:git:ssh://github.com/Harleyoc1/$modName.git")
                    url.set("https://github.com/Harleyoc1/$modName")
                }
            }
        }
    }
    repositories {
        maven("file:///${project.projectDir}/mcmodsrepo")
        if (hasProperty("harleyOConnorMavenUsername") && hasProperty("harleyOConnorMavenPassword")) {
            maven("https://harleyoconnor.com/maven") {
                credentials {
                    username = property("harleyOConnorMavenUsername")
                    password = property("harleyOConnorMavenPassword")
                }
            }
        } else {
            logger.log(LogLevel.WARN, "Credentials for maven not detected; it will be disabled.")
        }
    }
}
