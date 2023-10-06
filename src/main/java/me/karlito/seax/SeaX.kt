package me.karlito.seax

import org.bukkit.plugin.java.JavaPlugin

class SeaX : JavaPlugin() {
    override fun onEnable() {
        logger.info("The Seas Are Now Safe")

        registerListeners()
    }

    fun registerListeners() {
        server.pluginManager.registerEvents(playerJumpListener(), this)

        logger.info("Register Listeners")
    }
    override fun onDisable() {
        logger.info("The Seas Are Down ")
    }
}
