package me.karlito.seax

import me.karlito.seax.commands.DashCommand
import me.karlito.seax.commands.IaGui
import me.karlito.seax.commands.IronAxe
import me.karlito.seax.crew.CrewCommandTabCompletion
import me.karlito.seax.crew.CrewCommands
import me.karlito.seax.crew.InventoryClickListenerInvite
import me.karlito.seax.datastore.DatabaseUtils
import me.karlito.seax.itemsystem.InteractEvent
import me.karlito.seax.listeners.*
import me.karlito.seax.npcs.NpcHandler
import me.karlito.seax.safezones.MoveEvent
import me.karlito.seax.safezones.SafeZones
import me.karlito.seax.trading_companies.selling.NpcInteract
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.java.JavaPlugin
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*


class SeaX : JavaPlugin() {

    companion object {
        val guiMap: MutableMap<UUID, Inventory> = mutableMapOf()
        val inviteMap = HashMap<String, String>()
        val crewMap: MutableMap<UUID, MutableList<String>> = mutableMapOf()
        var connection : Connection? = null
        val attachedEntities : MutableMap<String, Entity> = mutableMapOf()
        val playerActiveVoyage : MutableMap<UUID, Boolean> = mutableMapOf()
        val voyageLoot: MutableMap<UUID, MutableList<UUID>> = mutableMapOf()
        val crewActiveVoyage : MutableMap<MutableList<String>, Boolean> = mutableMapOf()
        val crewVoyageLoot : MutableMap<MutableList<String>, MutableList<UUID>> = mutableMapOf()

    }

    override fun onEnable() {
        logger.info("The Seas Are Now Safe")
        registercommands()
        registerlisteners()

        saveDefaultConfig()

        SafeZones().registerAllSafeZones()

        NpcHandler().createNpcs()

        //val url = "jdbc:mysql://aws.connect.psdb.cloud/seax-database?sslMode=VERIFY_IDENTITY"
        //val user = "vjywbb4nphu4f81wxu5m"
        //val pass = "pscale_pw_ucxIORienh18agRsfgL7YnktYr8j69xRroStpQy7l5T"

        val url : String = config.get("database.url") as String
        val user : String = config.get("database.user") as String
        val pass : String = config.get("database.pass") as String


        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            connection = DriverManager.getConnection(url, user, pass)
            if (connection != null) {
            logger.info("DATABASE $connection")
            DatabaseUtils().database()
            } else {
                logger.warning("Setup The Plugin SeaX")
            }
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
        getCommand("crew")?.setExecutor(CrewCommands())
        getCommand("crew")?.tabCompleter = CrewCommandTabCompletion()


        logger.info("Registered Commands and Tab Completions")
    }

    private fun registerlisteners() {
        Bukkit.getServer().pluginManager.registerEvents(InventoryClickListenerInvite(), this)
        Bukkit.getServer().pluginManager.registerEvents(InventoryCloseListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(InventoryClickListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(PlayerJoinListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(InteractEvent(), this)
        Bukkit.getServer().pluginManager.registerEvents(NpcInteract(), this)
        Bukkit.getServer().pluginManager.registerEvents(OnLeaveListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(MoveEvent(), this)
        Bukkit.getServer().pluginManager.registerEvents(ClickListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(PlayerDeathListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(PlayerRespawnListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(PlayerTrowListener(), this)
        //Bukkit.getServer().pluginManager.registerEvents(MythicSpawnListener(), this)

        logger.info("Registered Event Listeners")
    }
    override fun onDisable() {
        NpcHandler().removeNpcs()
        logger.info("The Seas Are Down ")
        try {
            DatabaseUtils().closeConnection()
        } catch (_: SQLException){
            println("Failed To Connect")
        }
    }

}