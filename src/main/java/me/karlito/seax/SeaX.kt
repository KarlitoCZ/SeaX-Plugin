package me.karlito.seax

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import me.karlito.seax.commands.*
import me.karlito.seax.listeners.InventoryClickListener
import me.karlito.seax.listeners.InventoryCloseListener
import org.bson.Document
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
class SeaX : JavaPlugin() {



    companion object {
        val guiMap: MutableMap<UUID, Inventory> = mutableMapOf()

    }


    override fun onEnable() {
        logger.info("The Seas Are Now Safe")
        registercommands()
        registerlisteners()




        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
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
        getCommand("mongo")!!.setExecutor(MongoCommand())

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
