package me.karlito.seax

import me.karlito.seax.commands.*
import me.karlito.seax.datastore.DatabaseUtils
import me.karlito.seax.listeners.InventoryClickListener
import me.karlito.seax.listeners.InventoryCloseListener
import me.karlito.seax.listeners.PlayerJoinListener
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.java.JavaPlugin
import java.sql.SQLException
import java.util.*

class SeaX : JavaPlugin() {



    companion object {
        val guiMap: MutableMap<UUID, Inventory> = mutableMapOf()


    }

    var pointsDatabase: Unit? = null

    override fun onEnable() {
        logger.info("The Seas Are Now Safe")
        registercommands()
        registerlisteners()

        try {

            if (!dataFolder.exists()){
                dataFolder.mkdirs()
            }

            pointsDatabase = DatabaseUtils().pointDatabase(dataFolder.absolutePath + "/seax.db")

        } catch (sqlException: Exception) {
            println("Exception")
            Bukkit.getPluginManager().disablePlugin(this)
        }


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
        getCommand("getcoins")?.setExecutor(GetCoinsCommand())
        getCommand("setcoins")?.setExecutor(SetCoinsCommand())

        logger.info("Registered Commands")
    }

    private fun registerlisteners() {
        Bukkit.getServer().pluginManager.registerEvents(InventoryCloseListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(InventoryClickListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(PlayerJoinListener(), this)

        logger.info("Registered Event Listeners")
    }
    override fun onDisable() {
        logger.info("The Seas Are Down ")
        try {
            DatabaseUtils().closeConnection()
        } catch (_: SQLException){
            println("Failed To Connect")
        }
    }

}
