import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
}

group = "io.github.changwook987"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    // https://mvnrepository.com/artifact/com.1stleg/jnativehook
    implementation("com.1stleg:jnativehook:2.1.0")
    implementation("org.json:json:20220320")
}

tasks {
    test {
        useJUnitPlatform()
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    create<Jar>("fatjar") {
        from(sourceSets["main"].output)

        manifest {
            attributes["Main-Class"] = "io.github.changwook987.keyViewer.Main"
        }

        archiveBaseName.set("KeyViewer")
        archiveVersion.set("")

        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        doLast {
            copy {
                from(archiveFile)
                into(rootDir)
            }
        }
    }
}