package me.karlito.seax

import me.karlito.seax.commnads.EnderBow
import org.bukkit.plugin.java.JavaPlugin

class SeaX : JavaPlugin() {

    companion object {
        var instance: SeaX? = null
            private set
    }
    override fun onEnable() {
        logger.info("The Seas Are Now Safe")
        registercommands()
        instance = this
    }


    private fun registercommands() {
        val cmd = getCommand("enderBow")?.setExecutor(EnderBow())
        logger.info("Registered Commands $cmd")
    }
    override fun onDisable() {
        logger.info("The Seas Are Down ")
    }
}
