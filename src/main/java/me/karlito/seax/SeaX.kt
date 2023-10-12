package me.karlito.seax

import com.mongodb.client.MongoClients
import me.karlito.seax.commands.DashCommand
import me.karlito.seax.commands.DefaultGuicmd
import me.karlito.seax.commands.IaGui
import me.karlito.seax.commands.IronAxe
import me.karlito.seax.listeners.InventoryClickListener
import me.karlito.seax.listeners.InventoryCloseListener
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
        val mongoClient = MongoClients.create("mongodb+srv://<BOT>:<password!123>@seaxdb.99qs2ww.mongodb.net/?retryWrites=true&w=majority")
        val database = mongoClient.getDatabase("sample_weatherdata")
        val collection = database.getCollection("data")

        logger.info("$collection")
        logger.info("Connected To A Database")


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
        //getCommand("money")!!.setExecutor(PersistentDataPlugin())

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
