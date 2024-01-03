package me.karlito.seax.trading_companies.voyages

import com.connorlinfoot.titleapi.TitleAPI
import io.lumine.mythic.bukkit.MythicBukkit
import me.karlito.seax.SeaX
import me.karlito.seax.crew.CrewHandler
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random

class VoyageHandler {

    companion object {
        val voyageItemMap: MutableMap<UUID, ItemStack> = mutableMapOf()
    }

    val plugin = Bukkit.getPluginManager().getPlugin("SeaX")
    val config = plugin!!.config

    fun givePlayerVoyageItem(player: Player, lore : String, voyage : String) {
        val voyageName = config.getString("voyages.$voyage.name")

        val paper = ItemStack(Material.PAPER)
        val paperMeta = paper.itemMeta
        paperMeta.displayName(Component.text("${ChatColor.GREEN}${ChatColor.BOLD}Active Voyage"))
        val lore1 = "${ChatColor.BLUE}$voyageName"
        val lore2 = "${ChatColor.GOLD}$lore"
        paperMeta.lore = listOf(lore1, lore2)

        paper.itemMeta = paperMeta
        voyageItemMap[player.uniqueId] = paper
        val inventory = player.inventory
        for (i in 0 until inventory.size) {
            if (inventory.getItem(i) == null || inventory.getItem(i)!!.type == Material.AIR) {
                inventory.setItem(i, paper)
                break
            }
        }
    }
    fun voyageFinish(player: Player) {
        val crewHandler = CrewHandler()
        val members = crewHandler.getMembers(player)

        if (members != null) {
            SeaX.crewVoyageLoot.remove(members)
            SeaX.crewActiveVoyage.remove(members)
            for (member in members) {
                val memberPl = Bukkit.getPlayer(member)
                val item = voyageItemMap[memberPl!!.uniqueId]
                val title = "§6Voyage Finished"
                val subtitle = "Go and sell your loot or go exploring."
                TitleAPI.sendTitle(memberPl, 20, 30, 20, title, subtitle)
                player.inventory.remove(item!!)
                voyageItemMap.remove(memberPl.uniqueId)
            }
        } else {
            SeaX.voyageLoot.remove(player.uniqueId)
            SeaX.playerActiveVoyage.remove(player.uniqueId)
            val item = voyageItemMap[player.uniqueId]
            val title = "§6Voyage Finished"
            val subtitle = "Go and sell your loot or go exploring."
            TitleAPI.sendTitle(player, 20, 30, 20, title, subtitle)
            player.inventory.remove(item!!)
            voyageItemMap.remove(player.uniqueId)
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
                    val item = voyageItemMap[memberPl!!.uniqueId]
                    val title = "§6Voyage Canceled"
                    val subtitle = "Your crew was deleted"
                    TitleAPI.sendTitle(memberPl, 20, 30, 20, title, subtitle)
                    memberPl.inventory.remove(item!!)
                    voyageItemMap.remove(memberPl.uniqueId)
                }
                SeaX.crewVoyageLoot.remove(members)
                SeaX.crewActiveVoyage.remove(members)

            }
        } else {
            if (SeaX.voyageLoot[player.uniqueId] != null) {
                val item = voyageItemMap[player.uniqueId]
                player.inventory.remove(item!!)
                for (loot in SeaX.voyageLoot[player.uniqueId]!!) {
                    val mythicMob = MythicBukkit.inst().mobManager.getActiveMob(loot).orElse(null)
                    mythicMob.despawn()
                }
                SeaX.voyageLoot.remove(player.uniqueId)
                SeaX.playerActiveVoyage.remove(player.uniqueId)
                voyageItemMap.remove(player.uniqueId)
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