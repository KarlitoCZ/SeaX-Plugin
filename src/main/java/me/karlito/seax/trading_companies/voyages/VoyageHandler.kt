package me.karlito.seax.trading_companies.voyages

import com.connorlinfoot.titleapi.TitleAPI
import io.lumine.mythic.bukkit.MythicBukkit
import me.karlito.seax.SeaX
import me.karlito.seax.crew.CrewHandler
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random

class VoyageHandler {

    val plugin = Bukkit.getPluginManager().getPlugin("SeaX")
    val config = plugin!!.config

    fun voyageFinish(player: Player) {
        val crewHandler = CrewHandler()
        val members = crewHandler.getMembers(player)
        if (members != null) {
            SeaX.crewVoyageLoot.remove(members)
            SeaX.crewActiveVoyage.remove(members)
            for (member in members) {
                val memberPl = Bukkit.getPlayer(member)
                val title = "§6Voyage Finished"
                val subtitle = "Go and sell your loot or go exploring."
                TitleAPI.sendTitle(memberPl, 20, 30, 20, title, subtitle)
            }
        } else {
            SeaX.voyageLoot.remove(player.uniqueId)
            SeaX.playerActiveVoyage.remove(player.uniqueId)
            val title = "§6Voyage Finished"
            val subtitle = "Go and sell your loot or go exploring."
            TitleAPI.sendTitle(player, 20, 30, 20, title, subtitle)
        }
    }

    fun voyageCancel(player: Player) {
        val crewHandler = CrewHandler()
        val members = crewHandler.getMembers(player)
        if (members != null) {
            if (SeaX.crewVoyageLoot[members] != null) {
                for (loot in SeaX.crewVoyageLoot[members]!!) {
                    val mythicMob = MythicBukkit.inst().mobManager.getActiveMob(loot).orElse(null)
                    mythicMob.despawn()
                }
                for (member in members) {
                    val memberPl = Bukkit.getPlayer(member)
                    val title = "§6Voyage Canceled"
                    val subtitle = "Your crew was deleted"
                    TitleAPI.sendTitle(memberPl, 20, 30, 20, title, subtitle)
                }
                SeaX.crewVoyageLoot.remove(members)
                SeaX.crewActiveVoyage.remove(members)
            }
        } else {
            if (SeaX.voyageLoot[player.uniqueId] != null) {
                for (loot in SeaX.voyageLoot[player.uniqueId]!!) {
                    val mythicMob = MythicBukkit.inst().mobManager.getActiveMob(loot).orElse(null)
                    mythicMob.despawn()
                }
                SeaX.voyageLoot.remove(player.uniqueId)
                SeaX.playerActiveVoyage.remove(player.uniqueId)
            }
        }
    }
    fun getRandomLootType(level: Int): ConfigurationSection? {
        val loot = config.getConfigurationSection("loot-table") ?: return null
        val lootKeys = loot.getKeys(false)

        if (lootKeys.isEmpty()) {
            return null
        }
        val randomLootKey = lootKeys.elementAt(ThreadLocalRandom.current().nextInt(lootKeys.size))
        val randomLoot = loot.getConfigurationSection(randomLootKey)
        val lootSectionString = randomLoot?.name
        val voyageLevel = config.getInt("loot-table.$lootSectionString.voyageLevel")
        return if (voyageLevel == level) {
            randomLoot
        } else {
            getRandomLootType(level)
        }
    }


    fun randomizeLocation(x: Double, y: Double, z: Double): Location {

        var modifiedX = x
        var modifiedZ = z

        for (i in 1..1) {
            modifiedX += Random.nextDouble(-4.0, 6.0)
            modifiedZ += Random.nextDouble(-3.0, 5.0)

        }

        val worldString = config.getString("world")
        val world = Bukkit.getWorld(worldString!!)

        return Location(world, modifiedX, y, modifiedZ)
    }

}