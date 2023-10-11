package me.karlito.seax

import me.karlito.seax.commands.DashCommand
import me.karlito.seax.commands.DefaultGuicmd
import me.karlito.seax.commands.IaGui
import me.karlito.seax.commands.IronAxe
import me.karlito.seax.datastore.PersistentDataPlugin
import me.karlito.seax.listeners.InventoryClickListener
import me.karlito.seax.listeners.InventoryCloseListener
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class SeaX : JavaPlugin() {

    val plugin = this

    fun getPlugin(): SeaX {
        return plugin
    }
    companion object {
        val guiMap: MutableMap<UUID, Inventory> = mutableMapOf()

        var instance: SeaX? = null
            private set
    }

    override fun onEnable() {
        logger.info("The Seas Are Now Safe")
        registercommands()
        registerlisteners()
        instance = this


        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            /*
             * We register the EventListener here, when PlaceholderAPI is installed.
             * Since all events are in the main class (this class), we simply use "this"
             */
        } else {
            logger.info("Could not find PlaceholderAPI! This plugin is required.")
            Bukkit.getPluginManager().disablePlugin(this)
        }

    }


    private fun registercommands() {
        getCommand("IronAxe")?.setExecutor(IronAxe())
        getCommand("dash")?.setExecutor(DashCommand())
        getCommand("testgui")?.setExecutor(IaGui())
        getCommand("defGui")?.setExecutor(DefaultGuicmd())
        getCommand("money")!!.setExecutor(PersistentDataPlugin())

        logger.info("Registered Commands")
    }

    private fun registerlisteners() {
        Bukkit.getServer().pluginManager.registerEvents(InventoryCloseListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(InventoryClickListener(), this)

        logger.info("Registered Event Listeners")
    }
    override fun onDisable() {
        logger.info("The Seas Are Down ")
    }
}
