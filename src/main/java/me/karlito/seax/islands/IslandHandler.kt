package me.karlito.seax.islands

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.ConfigurationSection
import java.util.concurrent.ThreadLocalRandom


class IslandHandler() {

    val plugin = Bukkit.getPluginManager().getPlugin("SeaX")
    val config = plugin!!.config

    fun findIslandByName(nameIsland: String): Location?{

        val key = "name"

        val islandsSection: ConfigurationSection? = config.getConfigurationSection("islands")

        if (islandsSection != null) {
            for (islandName in islandsSection.getKeys(false)) {
                val islandSection = islandsSection.getConfigurationSection(islandName)
                val value = islandSection?.getString(key)

                if (value == nameIsland) {

                    val world: World? = Bukkit.getWorld("${config.get("world")}")
                    val x : Double = config.get("islands.$islandName.x") as Double
                    val y : Double = config.get("islands.$islandName.y") as Double
                    val z : Double = config.get("islands.$islandName.z") as Double
                    val loc = Location(world, x, y, z)

                    return loc
                }
            }
        }
        return null
    }

    fun getRandomIsland(level : Int): String? {
        val islands = config.getConfigurationSection("islands")?.getKeys(false) ?: return null

        if (islands.isEmpty()) {
            return null
        }

        val randomIsland = islands.elementAt(ThreadLocalRandom.current().nextInt(islands.size))
        val voyageLevel = config.getInt("islands.$randomIsland.voyageLevel")
        return if (voyageLevel == 1) {
            randomIsland
        } else {
            getRandomIsland(level)
        }
    }

    fun printAllIslandsFromYAML() {
        val islandsSection = config.getConfigurationSection("islands") ?: return

        for (islandName in islandsSection.getKeys(false)) {
            val islandLocation = islandsSection.getConfigurationSection(islandName)

            if (islandLocation != null) {
                val islandX = islandLocation.getDouble("x")
                val islandY = islandLocation.getDouble("y")
                val islandZ = islandLocation.getDouble("z")

                println("Island Name: $islandName, Location: ($islandX, $islandY, $islandZ)")
            }
        }
    }



}