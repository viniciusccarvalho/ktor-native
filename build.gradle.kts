import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.21"
    id("com.hpe.kraal") version "0.0.15" // kraal version - for makeRelease.sh
}

group = "io.igx.kotlin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.slf4j:slf4j-simple:1.7.26")
    implementation("io.ktor:ktor-server-cio:1.1.3")
    implementation("io.ktor:ktor-gson:1.1.3")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
        // need use-experimental for Ktor CIO
        freeCompilerArgs += listOf("-Xuse-experimental=kotlin.Experimental", "-progressive")
        // disable -Werror with: ./gradlew -PwarningsAsErrors=false
        allWarningsAsErrors = project.findProperty("warningsAsErrors") != "false"
    }
}


val fatjar by tasks.creating(Jar::class) {

    from(kraal.outputZipTrees) {
        exclude("META-INF/*.SF")
        exclude("META-INF/*.DSA")
        exclude("META-INF/*.RSA")
    }

    manifest {
        attributes("Main-Class" to "io.igx.kotlin.ApplicationKt")
    }

    destinationDirectory.set(project.buildDir.resolve("fatjar"))
    archiveFileName.set("ktor-native.jar")
}

tasks.named("assemble").configure {
    dependsOn(fatjar)
}