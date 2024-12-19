import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.1.0"
    id("org.ajoberstar.grgit") version ("5.3.0")
}

group = "me.danny"
version = "${getHash()}-dev"
val spigotVersion = "1.21.4-R0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven(url = "https://repo.dmulloy2.net/repository/public/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:$spigotVersion")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.3.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "23"
}

tasks.named<Copy>("processResources") {
    filesMatching("plugin.yml") {
        expand("git_commit" to version)
    }
}

fun getHash() = grgit.head()?.abbreviatedId ?: "unknown"