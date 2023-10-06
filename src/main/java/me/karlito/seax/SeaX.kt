package me.karlito.seax

import me.karlito.seax.commnads.EnderBow
import org.bukkit.plugin.java.JavaPlugin

class SeaX : JavaPlugin() {
    override fun onEnable() {
        logger.info("The Seas Are Now Safe")
        registercommads()
    }

    private fun registercommads() {
        getCommand("enderbow")?.setExecutor(EnderBow())
        logger.info("Registered Commands")
    }
    override fun onDisable() {
        logger.info("The Seas Are Down ")
    }
}
