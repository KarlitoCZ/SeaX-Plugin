package me.karlito.seax.listeners

import me.karlito.seax.SeaX.Companion.crewActiveVoyage
import me.karlito.seax.SeaX.Companion.playerActiveVoyage
import me.karlito.seax.crew.CrewHandler
import me.karlito.seax.trading_companies.voyages.VoyageHandler.Companion.voyageItemMap
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.ItemStack

class PlayerRespawnListener : Listener {

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        val player: Player = event.player

        val itemStack = ItemStack(Material.COMPASS, 1)
        val itemMeta = itemStack.itemMeta
        itemMeta.setCustomModelData(4867)
        itemMeta.setDisplayName("${ChatColor.BLUE}${player.name}'s Compass")
        itemStack.itemMeta = itemMeta
        player.inventory.setItem(8, itemStack)
        val members = CrewHandler().getMembers(player)
        val item = voyageItemMap[player.uniqueId]


        if (crewActiveVoyage[members] != null) {
            val inventory = player.inventory
            for (i in 0 until inventory.size) {
                if (inventory.getItem(i) == null || inventory.getItem(i)!!.type == Material.AIR) {
                    inventory.setItem(i, item)
                    break
                }
            }

        } else if (playerActiveVoyage[player.uniqueId] != null) {
            val inventory = player.inventory
            for (i in 0 until inventory.size) {
                if (inventory.getItem(i) == null || inventory.getItem(i)!!.type == Material.AIR) {
                    inventory.setItem(i, item)
                    break
                }
            }
        }


    }

}