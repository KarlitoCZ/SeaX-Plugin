package me.karlito.seax.islands

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.ConfigurationSection


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



}