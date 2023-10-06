package me.karlito.seax

import org.bukkit.plugin.java.JavaPlugin

class SeaX : JavaPlugin() {
    override fun onEnable() {
        logger.info("The Seas Are Now Safe")
    }

    override fun onDisable() {
        logger.info("The Seas Are Down ")
    }
}
