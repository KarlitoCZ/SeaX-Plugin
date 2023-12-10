package me.karlito.seax.trading_companies.selling

import io.lumine.mythic.bukkit.MythicBukkit
import me.karlito.seax.SeaX.Companion.attachedEntities
import me.karlito.seax.crew.CrewHandler
import me.karlito.seax.datastore.DatabaseUtils
import me.karlito.seax.itemsystem.ItemHoldHandler
import net.citizensnpcs.util.NMS
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent

class SellSystem {

    companion object {
        val lootMap = mutableMapOf( // coins, silver, xp
            "SunkenChest" to Triple(200, 50, 30),

        )
    }


    val itemHoldHandler = ItemHoldHandler()
    fun sellLoot(player: Player) {

        val members = CrewHandler().getMembers(player)
        println("print1")
        val entity = attachedEntities[player.name]
        println("print2 $entity, $members")

        if (members != null) { // sell loot send the coins to all the members of the crew
        } else { // sell loot send the coins only to that player
            if (entity != null) {
                println("print3")
                val mythicMob = MythicBukkit.inst().mobManager.getActiveMob(entity.uniqueId).orElse(null)
                itemHoldHandler.stopTask(player)
                entity.remove()
                val coins = DatabaseUtils().getPlayerCoins(player)
                val coinsValue = lootMap[mythicMob.mobType]?.first
                DatabaseUtils().updatePlayerCoins(player, coins + coinsValue!!)
                attachedEntities.remove(player.name)
                player.sendMessage("${ChatColor.BOLD}${ChatColor.GOLD}[Seller]${ChatColor.RESET}${ChatColor.AQUA} You sold ${mythicMob.mobType}")
            } else {
                println("Player cant sell air")
            }
        }
    }
}

class NpcInteract : Listener {
    @EventHandler
    fun onInteractEntity(event: PlayerInteractAtEntityEvent) {
        val entity = event.rightClicked
        val player = event.player
        val playerName = event.player.name

        if (entity.hasMetadata("NPC")) {
            val npc = NMS.getNPC(entity)
            when (npc.name) {
                "Seller" -> {
                    SellSystem().sellLoot(player)
                    println("Clicked the right npc")
                }
            }
        }
    }
}


