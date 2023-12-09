package me.karlito.seax.trading_companies.selling

import io.lumine.mythic.bukkit.MythicBukkit
import me.karlito.seax.SeaX.Companion.attachedEntities
import me.karlito.seax.crew.CrewHandler
import me.karlito.seax.datastore.DatabaseUtils
import me.karlito.seax.itemsystem.ItemHoldHandler
import net.citizensnpcs.util.NMS
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent

class SellSystem {

    val itemHoldHandler = ItemHoldHandler()
    fun sellLoot(player: Player) {

        val members = CrewHandler().getMembers(player)
        val entity = attachedEntities[player.name]
        val mythicMob = MythicBukkit.inst().mobManager.getActiveMob(entity?.uniqueId).orElse(null)

        if (members != null) { // sell loot send the coins to all the members of the crew
        } else { // sell loot send the coins only to that player
            if (entity != null) {
                itemHoldHandler.stopTask(player)
                entity.remove()
                val coins = DatabaseUtils().getPlayerCoins(player)
                DatabaseUtils().updatePlayerCoins(player, coins + 200)
                attachedEntities.remove(player.name)
                player.sendMessage("You sold ${mythicMob.mobType}")
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


