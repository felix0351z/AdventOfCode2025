plugins {
    kotlin("jvm") version "2.2.20"
}

group = "de.felix0351.aoc2025"
version = "20.25"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}