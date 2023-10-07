package me.karlito.seax

import me.karlito.seax.commnads.DashCommand
import me.karlito.seax.commnads.EnderBow
import me.karlito.seax.commnads.IaGui
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
        getCommand("enderBow")?.setExecutor(EnderBow())
        getCommand("dash")?.setExecutor(DashCommand())
        getCommand("testgui")?.setExecutor(IaGui())

        logger.info("Registered Commands")
    }
    override fun onDisable() {
        logger.info("The Seas Are Down ")
    }
}
