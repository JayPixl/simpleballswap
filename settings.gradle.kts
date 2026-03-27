// By default, this is how your built jars are called
rootProject.name = "CobblemonSimpleBallSwap-1.21.1"

pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net/")
        gradlePluginPortal()
    }
}

listOf(
    "common",
    "neoforge",
    "fabric"
).forEach { include(it)}
