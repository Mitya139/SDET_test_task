plugins {
    kotlin("jvm") version "2.3.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

val intellijToolsVersion = "262.4852.50"
val junitVersion = "5.13.4"
val kodeinVersion = "7.26.1"
val coroutinesVersion = "1.10.2"

repositories {
    mavenCentral()
    maven(url = "https://cache-redirector.jetbrains.com/intellij-dependencies")
    maven(url = "https://www.jetbrains.com/intellij-repository/releases")
    maven(url = "https://www.jetbrains.com/intellij-repository/snapshots")
    maven(url = "https://download.jetbrains.com/teamcity-repository")
    maven(url = "https://cache-redirector.jetbrains.com/packages.jetbrains.team/maven/p/grazi/grazie-platform-public")
}

kotlin {
    jvmToolchain(25)
}

dependencies {
    testImplementation("com.jetbrains.intellij.tools:ide-starter-squashed:$intellijToolsVersion")
    testImplementation("com.jetbrains.intellij.tools:ide-starter-junit5:$intellijToolsVersion")
    testImplementation("com.jetbrains.intellij.tools:ide-starter-driver:$intellijToolsVersion")

    testImplementation("com.jetbrains.intellij.driver:driver-client:$intellijToolsVersion")
    testImplementation("com.jetbrains.intellij.driver:driver-sdk:$intellijToolsVersion")
    testImplementation("com.jetbrains.intellij.driver:driver-model:$intellijToolsVersion")

    val junitBom = platform("org.junit:junit-bom:$junitVersion")
    testImplementation(junitBom)
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("org.kodein.di:kodein-di-jvm:$kodeinVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:$coroutinesVersion")
}

tasks.test {
    useJUnitPlatform()
    systemProperty("java.awt.headless", "false")

    testLogging {
        events("passed", "skipped", "failed", "standardOut", "standardError")
    }
}
