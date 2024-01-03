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
import me.karlito.seax.trading_companies.selling.NpcInteract
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
        val islandName = config.getString("islands.$island.name")

        val playerCoins = DatabaseUtils().getPlayerCoins(player)
        val playerSilver = DatabaseUtils().getPlayerSilver(player)
        val costCoins = config.getInt("voyages.voyage1.costCoins")
        val costSilver = config.getInt("voyages.voyage1.costSilver")
        val voyageName = config.getString("voyages.voyage1.name")

        val teu = DatabaseUtils().hasPlayerStoreItem(player, 1)

        if (playerCoins >= costCoins && playerSilver >= costSilver) {

            if (NpcInteract().isOnCooldown(player)) {
                return
            }

            val cooldownSeconds = 0.5
            NpcInteract().setCooldown(player, cooldownSeconds)

            val crewHandler = CrewHandler()
            val membersList = crewHandler.getMembers(player)
            if (membersList != null && crewActiveVoyage[membersList] == null) {
                crewActiveVoyage[membersList] = true
                val finalCostCoins = playerCoins - costCoins
                val finalCostSilver = playerSilver - costSilver
                DatabaseUtils().updatePlayerCoins(player, finalCostCoins)
                DatabaseUtils().updatePlayerSilver(player, finalCostSilver)
                for (member in membersList) {
                    val memberPl = Bukkit.getPlayer(member)
                    val title = "§6Voyage Started By ${player.name}"
                    val subtitle = "Follow The Instructions In Chat"
                    TitleAPI.sendTitle(memberPl, 20, 50, 20, title, subtitle)
                    if (memberPl == null) return
                    VoyageHandler().givePlayerVoyageItem(memberPl, "Go and get the treasures on $islandName", "voyage1")

                }

                player.sendMessage("${ChatColor.DARK_RED}[Skull Merchants]${ChatColor.RESET} Go to ${ChatColor.BLUE}$islandName${ChatColor.RESET} and find the treasure,\nBe careful it's guarded by skeletons!")

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
            } else if (membersList != null){
                player.sendMessage("${ChatColor.RED}You are on a voyage right now!")
            }
            if (SeaX.playerActiveVoyage[player.uniqueId] == null && membersList == null) {
                SeaX.playerActiveVoyage[player.uniqueId] = true
                val finalCostCoins = playerCoins - costCoins
                val finalCostSilver = playerSilver - costSilver
                DatabaseUtils().updatePlayerCoins(player, finalCostCoins)
                DatabaseUtils().updatePlayerSilver(player, finalCostSilver)
                val title = "§6Voyage Started"
                val subtitle = "Follow The Instructions In Chat"
                TitleAPI.sendTitle(player, 20, 50, 20, title, subtitle)

                VoyageHandler().givePlayerVoyageItem(player, "Go and get the treasures on $islandName", "voyage1")

                player.sendMessage("${ChatColor.DARK_RED}[Skull Merchants]${ChatColor.RESET} Go to ${ChatColor.BLUE}$islandName${ChatColor.RESET} and find the treasure,\nBe careful it's guarded by skeletons!")


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
        } else {
            player.sendMessage("${ChatColor.RED}Insufficient funds.")
        }
    }
}







