import org.apache.tools.ant.filters.ReplaceTokens
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.3.50"
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

group = "net.savagelabs"
version = "v1.5"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
    maven("https://nexus.savagelabs.net/repository/maven-releases/")
    maven("https://rayzr.dev/repo/")
    maven("https://libraries.minecraft.net")
    maven("http://repo.dmulloy2.net/nexus/repository/public/")
    maven("http://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.codemc.org/repository/maven-public")
}

dependencies {
    implementation("org.ocpsoft.prettytime:prettytime:4.0.1.Final")
    implementation("com.github.stefvanschie.inventoryframework:IF:0.5.18")
    implementation("org.bstats:bstats-bukkit:1.7")
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("net.prosavage:BasePlugin:1.7.4")
    implementation("me.rayzr522:jsonmessage:1.2.0")
    implementation("com.cryptomorin:XSeries:6.0.0")
    implementation("io.papermc:paperlib:1.0.2")
    implementation(project(":SavagePluginX"))


    compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.50")
    compileOnly("org.jetbrains.kotlin:kotlin-reflect:1.3.50")
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    compileOnly("org.spigotmc:spigot-api:1.16.1-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.9.2")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    processResources {
        println("Task ***** processResources*****")
        filter<ReplaceTokens>("tokens" to mapOf(
            "project.version" to project.version
        ))
    }

    val build by existing {
        dependsOn(shadowJar)
    }

    val copyResources by registering(Copy::class) {
        from("src/main/resources")
        into(buildDir.resolve("resources/main"))
        dependsOn(processResources)
    }


    val shadowJar = named<ShadowJar>("shadowJar") {
        dependsOn(copyResources)
        mergeServiceFiles()
        exclude("META-INF/*.DSA")
        exclude("META-INF/*.RSA")
//        relocate(
//            "com.github.stefvanschie.inventoryframework",
//            "net.savagelabs.skyblockx.shade.stefvanschie.inventoryframework"
//        )
//        relocate("com.google.gson", "net.savagelabs.skyblockx.shade.com.google.gson")
//        relocate("net.prosavage.baseplugin", "net.savagelabs.skyblockx.shade.baseplugin")
//        relocate("org.jetbrains.kotlin", "net.savagelabs.skyblockx.shade.kotlin")
//        relocate("me.rayzr522.jsonmessage", "net.savagelabs.skyblockx.shade.jsonmessage")
//        relocate("org.bstats", "net.savagelabs.skyblockx.shade.bstats")
//        relocate("io.papermc.lib", "net.savagelabs.skyblockx.shade.paperlib")
//        relocate("kotlin", "net.savagelabs.skyblockx.shade.kotlin")
//        relocate("org.jetbrains.annotations", "net.savagelabs.skyblockx.shade.jetbrains-annotations")
//        relocate("com.cryptomorin.xseries", "net.savagelabs.skyblockx.shade.xseries")
//        relocate("fonts", "net.savagelabs.skyblockx.shade.fonts")
        archiveBaseName.set("SkyblockX")
        minimize()
    }



    val ci = task("ci") {
        dependsOn(clean)
        dependsOn(shadowJar)
    }
}