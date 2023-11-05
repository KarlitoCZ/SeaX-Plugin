package me.karlito.seax

import me.karlito.seax.commands.*
import me.karlito.seax.datastore.DatabaseUtils
import me.karlito.seax.gui.SMgui
import me.karlito.seax.itemsystem.InteractEvent
import me.karlito.seax.listeners.InventoryClickListener
import me.karlito.seax.listeners.InventoryCloseListener
import me.karlito.seax.listeners.PlayerJoinListener
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.java.JavaPlugin
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*


class SeaX : JavaPlugin() {



    companion object {
        val guiMap: MutableMap<UUID, Inventory> = mutableMapOf()

        var connection : Connection? = null
    }

    //var pointsDatabase: Unit? = null

    override fun onEnable() {
        logger.info("The Seas Are Now Safe")
        registercommands()
        registerlisteners()


        val url = "jdbc:mysql://aws.connect.psdb.cloud/seax-database?sslMode=VERIFY_IDENTITY"
        val user = "vjywbb4nphu4f81wxu5m"
        val pass = "pscale_pw_ucxIORienh18agRsfgL7YnktYr8j69xRroStpQy7l5T"

        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            connection = DriverManager.getConnection(url, user, pass)

            logger.warning("DATABASE $connection")

            //if (!dataFolder.exists()){
               // dataFolder.mkdirs()
            //}

            DatabaseUtils().pointDatabase()

        } catch (sqlException: Exception) {
            logger.warning("Exception $sqlException")
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
        getCommand("defGui")?.setExecutor(SMgui())
        getCommand("getcoins")?.setExecutor(GetCoinsCommand())
        getCommand("setcoins")?.setExecutor(SetCoinsCommand())

        logger.info("Registered Commands")
    }

    private fun registerlisteners() {
        Bukkit.getServer().pluginManager.registerEvents(InventoryCloseListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(InventoryClickListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(PlayerJoinListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(InteractEvent(), this)
        //Bukkit.getServer().pluginManager.registerEvents(MythicSpawnListener(), this)

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