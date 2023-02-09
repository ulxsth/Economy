import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.0.0" // https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow
}

group = "io.github.ulxsth"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
}

dependencies {
    testImplementation(kotlin("test"))
    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")

    // Json
    implementation(group = "com.fasterxml.jackson.core", name = "jackson-annotations", version = "2.13.2")
    implementation(group = "com.fasterxml.jackson.core", name = "jackson-databind", version = "2.13.2.2")
    implementation(group = "com.fasterxml.jackson.module", name = "jackson-module-kotlin", version = "2.13.2")
    implementation(group = "com.fasterxml.jackson.datatype", name = "jackson-datatype-jsr310", version = "2.13.2")

}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}