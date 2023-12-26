package me.karlito.seax.trading_companies.voyages

import com.connorlinfoot.titleapi.TitleAPI
import io.lumine.mythic.bukkit.BukkitAdapter
import io.lumine.mythic.bukkit.MythicBukkit
import me.karlito.seax.SeaX.Companion.playerActiveVoyage
import me.karlito.seax.SeaX.Companion.playerStageVoyage
import me.karlito.seax.crew.CrewHandler
import me.karlito.seax.datastore.DatabaseUtils
import me.karlito.seax.islands.IslandHandler
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random


class SMVoyages {

    val plugin = Bukkit.getPluginManager().getPlugin("SeaX")
    val config = plugin!!.config

    fun getRandomLootType(): ConfigurationSection? {
        val loot = config.getConfigurationSection("loot-table") ?: return null
        val lootKeys = loot.getKeys(false)

        if (lootKeys.isEmpty()) {
            return null
        }

        val randomLootKey = lootKeys.elementAt(ThreadLocalRandom.current().nextInt(lootKeys.size))
        return loot.getConfigurationSection(randomLootKey)


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

    fun smVoyageEvent1(player: Player) { // Get the lost treasure

        val playerCoins = DatabaseUtils().getPlayerCoins(player)

        if (playerCoins > 300) {
            val crewHandler = CrewHandler()
            val members = crewHandler.getMembers(player)
            if (members != null) {
                // TO DO
            } else {
                if (playerActiveVoyage[player.uniqueId] == null) {
                    playerActiveVoyage[player.uniqueId] = true
                    playerStageVoyage[player.uniqueId] = 1
                    val finalCost = playerCoins - 300
                    DatabaseUtils().updatePlayerCoins(player, finalCost)
                    val title = "§6Voyage Started"
                    val subtitle = "Follow The Instructions In Chat"
                    TitleAPI.sendTitle(player, 20, 50, 20, title, subtitle)
                    val island = IslandHandler().getRandomIsland()

                    player.sendMessage("${ChatColor.DARK_RED}[Skull Merchants]${ChatColor.RESET} Go to ${ChatColor.BLUE}$island${ChatColor.RESET} and find the treasure,\nBe careful it's guarded by skeletons!")
                    val x = config.getDouble("islands.$island.voyageLootSpot.x")
                    val y = config.getDouble("islands.$island.voyageLootSpot.y")
                    val z = config.getDouble("islands.$island.voyageLootSpot.z")

                    val lootNumber = Random.nextInt(3, 6)
                    println(lootNumber)
                    // config.contains("loot-table.${mythicMob.mobType}"
                    for (i in 1..lootNumber) {
                        println(i)
                        val lootSection = getRandomLootType()
                        val lootSectionString = lootSection?.name
                        val lootLevel = config.getInt("loot-table.$lootSectionString.voyageLevel")

                        if (lootLevel == 1) {
                            println("Spawning")
                            val lootLocation = randomizeLocation(x, y, z)
                            val mob = MythicBukkit.inst().mobManager.getMythicMob("$lootSectionString").orElse(null)
                            if (mob != null) {
                                val loot = mob.spawn(BukkitAdapter.adapt(lootLocation), 1.0)
                                println("Spawned")
                                val entity = loot.entity.bukkitEntity
                            }
                        }
                    }


                } else {
                    player.sendMessage("${ChatColor.RED}You are on a voyage right now!")
                }
            }
        } else {
            player.sendMessage("${ChatColor.RED}Not enough coins!")
        }


    }

    fun voyageCancel(player: Player) {

    }

}


