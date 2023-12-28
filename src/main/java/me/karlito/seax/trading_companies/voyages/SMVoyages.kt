package me.karlito.seax.trading_companies.voyages

import com.connorlinfoot.titleapi.TitleAPI
import io.lumine.mythic.bukkit.BukkitAdapter
import io.lumine.mythic.bukkit.MythicBukkit
import me.karlito.seax.SeaX
import me.karlito.seax.SeaX.Companion.crewActiveVoyage
import me.karlito.seax.SeaX.Companion.crewVoyageLoot
import me.karlito.seax.SeaX.Companion.voyageLoot
import me.karlito.seax.crew.CrewHandler
import me.karlito.seax.datastore.DatabaseUtils
import me.karlito.seax.islands.IslandHandler
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*
import kotlin.random.Random


class SMVoyages {

    val plugin = Bukkit.getPluginManager().getPlugin("SeaX")
    val config = plugin!!.config

    fun smVoyageEvent1(player: Player) { // Get the lost treasure

        val island = IslandHandler().getRandomIsland(1)

        val x = config.getDouble("islands.$island.voyageLootSpot.x")
        val y = config.getDouble("islands.$island.voyageLootSpot.y")
        val z = config.getDouble("islands.$island.voyageLootSpot.z")

        val cost = 300
        val playerCoins = DatabaseUtils().getPlayerCoins(player)

        if (playerCoins > cost) {
            val crewHandler = CrewHandler()
            val membersList = crewHandler.getMembers(player)
            if (membersList != null && crewActiveVoyage[membersList] == null) {
                crewActiveVoyage[membersList] = true
                val finalCost = playerCoins - cost
                DatabaseUtils().updatePlayerCoins(player, finalCost)
                for (member in membersList) {
                    val memberPl = Bukkit.getPlayer(member)
                    val title = "§6Voyage Started By ${player.name}"
                    val subtitle = "Follow The Instructions In Chat"
                    TitleAPI.sendTitle(memberPl, 20, 50, 20, title, subtitle)
                }

                player.sendMessage("${ChatColor.DARK_RED}[Skull Merchants]${ChatColor.RESET} Go to ${ChatColor.BLUE}$island${ChatColor.RESET} and find the treasure,\nBe careful it's guarded by skeletons!")

                val lootNumber = Random.nextInt(3, 6)

                for (i in 1..lootNumber) {
                    val lootSection = VoyageHandler().getRandomLootType(1)
                    val lootSectionString = lootSection?.name
                    val members = crewHandler.getMembers(player)

                    val lootLocation = VoyageHandler().randomizeLocation(x, y, z)
                    val mob = MythicBukkit.inst().mobManager.getMythicMob("$lootSectionString").orElse(null)
                    if (mob != null) {
                        val loot = mob.spawn(BukkitAdapter.adapt(lootLocation), 1.0)
                        if (crewVoyageLoot[members] == null) {
                            val lootList = mutableListOf<UUID>()
                            lootList.add(loot.uniqueId)
                            if (members != null) {
                                crewVoyageLoot[members] = lootList
                            }
                        } else {
                            val size = crewVoyageLoot[members]!!.size.minus(1)
                            size.let { crewVoyageLoot[members]!!.add(it, loot.uniqueId) }
                        }

                    }
                }
            } else {
                player.sendMessage("${ChatColor.RED}You are on a voyage right now!")
            }
            if (SeaX.playerActiveVoyage[player.uniqueId] == null && membersList == null) {
                SeaX.playerActiveVoyage[player.uniqueId] = true
                val finalCost = playerCoins - cost
                DatabaseUtils().updatePlayerCoins(player, finalCost)
                val title = "§6Voyage Started"
                val subtitle = "Follow The Instructions In Chat"
                TitleAPI.sendTitle(player, 20, 50, 20, title, subtitle)

                player.sendMessage("${ChatColor.DARK_RED}[Skull Merchants]${ChatColor.RESET} Go to ${ChatColor.BLUE}$island${ChatColor.RESET} and find the treasure,\nBe careful it's guarded by skeletons!")


                val lootNumber = Random.nextInt(3, 6)
                // config.contains("loot-table.${mythicMob.mobType}"
                for (i in 1..lootNumber) {
                    val lootSection = VoyageHandler().getRandomLootType(1)
                    val lootSectionString = lootSection?.name

                    val lootLocation = VoyageHandler().randomizeLocation(x, y, z)
                    val mob = MythicBukkit.inst().mobManager.getMythicMob("$lootSectionString").orElse(null)
                    if (mob != null) {
                        val loot = mob.spawn(BukkitAdapter.adapt(lootLocation), 1.0)

                        if (voyageLoot[player.uniqueId] == null) {
                            val lootList = mutableListOf<UUID>()
                            lootList.add(loot.uniqueId)
                            voyageLoot[player.uniqueId] = lootList
                        } else {
                            val size = voyageLoot[player.uniqueId]!!.size.minus(1)
                            size.let { voyageLoot[player.uniqueId]!!.add(it, loot.uniqueId) }
                        }

                    }
                }
            } else {
                player.sendMessage("${ChatColor.RED}You are on a voyage right now!")
            }
        }
    }
}







