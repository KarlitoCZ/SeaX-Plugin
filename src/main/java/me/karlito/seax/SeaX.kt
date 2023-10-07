package me.karlito.seax

import me.karlito.seax.commands.DashCommand
import me.karlito.seax.commands.IaGui
import me.karlito.seax.commands.IronAxe
import org.bukkit.Bukkit
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

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            /*
             * We register the EventListener here, when PlaceholderAPI is installed.
             * Since all events are in the main class (this class), we simply use "this"
             */
        } else {
            /*
             * We inform about the fact that PlaceholderAPI isn't installed and then
             * disable this plugin to prevent issues.
             */
            logger.info("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this)
        }

    }


    private fun registercommands() {
        getCommand("IronAxe")?.setExecutor(IronAxe())
        getCommand("dash")?.setExecutor(DashCommand())
        getCommand("testgui")?.setExecutor(IaGui())

        logger.info("Registered Commands")
    }
    override fun onDisable() {
        logger.info("The Seas Are Down ")
    }
}
