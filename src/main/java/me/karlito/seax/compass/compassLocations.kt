package me.karlito.seax.compass

import org.bukkit.Bukkit
import org.bukkit.Location


class IslandHandler() {

    val plugin = Bukkit.getPluginManager().getPlugin("SeaX")
    val config = plugin!!.config

    fun getIslandName(location: Location): String? {
        val islandsSection = config.getConfigurationSection("islands") ?: return null

        for (key in islandsSection.getKeys(false)) {
            val islandLocation = islandsSection.getConfigurationSection(key)

            if (islandLocation != null &&
                islandLocation.getInt("x") == location.blockX &&
                islandLocation.getInt("y") == location.blockY &&
                islandLocation.getInt("z") == location.blockZ
            ) {
                return key
            }
        }
        return null
    }

    fun getLocationKey(location: Location, islandName: String): String {
        return "${islandName}:${location.blockX}:${location.blockY}:${location.blockZ}"
    }

}