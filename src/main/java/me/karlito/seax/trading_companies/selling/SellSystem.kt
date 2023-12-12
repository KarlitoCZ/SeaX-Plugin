package me.karlito.seax.trading_companies.selling

import io.lumine.mythic.bukkit.MythicBukkit
import me.karlito.seax.SeaX.Companion.attachedEntities
import me.karlito.seax.crew.CrewHandler
import me.karlito.seax.datastore.DatabaseUtils
import me.karlito.seax.itemsystem.ItemHoldHandler
import net.citizensnpcs.util.NMS
import org.bukkit.ChatColor
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent

class SellSystem {

    companion object {
        val lootMap = mutableMapOf(
            // coins, silver, xp
            "SunkenChest" to Triple(200, 50, 30),

            )
    }


    private val itemHoldHandler = ItemHoldHandler()
    fun sellLoot(player: Player, com: String) {

        val members = CrewHandler().getMembers(player)
        val entity = attachedEntities[player.name]

        if (members != null) {



        } else {
            sellPlayerLoot(entity, player, com)
        }
    }

    fun sellPlayerLoot(entity : Entity?, player: Player, com: String) {
        val mythicMob = MythicBukkit.inst().mobManager.getActiveMob(entity!!.uniqueId).orElse(null)
        itemHoldHandler.stopTask(player)
        entity.remove()
        val coins = DatabaseUtils().getPlayerCoins(player)
        val silver = DatabaseUtils().getPlayerSilver(player)
        val xp = lootMap[mythicMob.mobType]?.third
        val coinsValue = lootMap[mythicMob.mobType]?.first
        val silverValue = lootMap[mythicMob.mobType]?.second
        DatabaseUtils().updatePlayerCoins(player, coins + coinsValue!!)
        DatabaseUtils().updatePlayerSilver(player, silver + silverValue!!)
        if (xp != null) {
            when (com) {
                "SkullMerchants" -> {
                    val currentXP = DatabaseUtils().getPlayerSMxp(player)
                    DatabaseUtils().updatePlayerSMxp(player, xp + currentXP)
                }
                "SoulTraders" -> {
                    val currentXP = DatabaseUtils().getPlayerSTxp(player)
                    DatabaseUtils().updatePlayerSTxp(player, xp + currentXP)
                }

                "WhisperingDealers" -> {
                    val currentXP = DatabaseUtils().getPlayerWDxp(player)
                    DatabaseUtils().updatePlayerWDxp(player, xp + currentXP)
                }
            }
        }
        attachedEntities.remove(player.name)
        player.sendMessage("${ChatColor.BOLD}${ChatColor.GOLD}[Seller]${ChatColor.RESET}${ChatColor.AQUA} You sold ${mythicMob.mobType}")
    }
}

class NpcInteract : Listener {
    @EventHandler
    fun onInteractEntity(event: PlayerInteractAtEntityEvent) {
        val entity = event.rightClicked
        val player = event.player
        val playerName = event.player.name
        val sm = "SkullMerchants"
        val st = "SoulTraders"
        val wd = "WhisperingDealers"

        if (entity.hasMetadata("NPC")) {
            val npc = NMS.getNPC(entity)
            when (npc.name) {
                "Seller" -> {
                    SellSystem().sellLoot(player, sm)
                    println("Clicked the right npc")
                }
            }
        }
    }
}


