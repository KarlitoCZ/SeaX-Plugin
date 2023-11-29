package me.karlito.seax.listeners

import me.karlito.seax.SeaX
import me.karlito.seax.crew.CrewHandler
import me.karlito.seax.voyages.SMVoyages
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class InventoryClickListener : Listener {

    @EventHandler
    fun inventorClickEvent(event: InventoryClickEvent) {
        val player = event.whoClicked as Player
        val playerUUID = event.whoClicked.uniqueId
        val sender = SeaX.pendingInvitations.remove(player) ?: return
        if(event.inventory ==  SeaX.guiMap[playerUUID]) {
            if(event.currentItem == null) return
            if(event.currentItem!!.itemMeta.hasCustomModelData()) {
                when(event.currentItem?.itemMeta?.customModelData) {
                    1788 -> {
                        event.isCancelled = true
                        SMVoyages().voyageEvent1(player)
                        event.clickedInventory?.close()
                    }
                    3565 -> {
                        event.isCancelled = true
                        CrewHandler().addPlayer(sender, player)
                        event.clickedInventory?.close()
                    }
                    3566 -> {
                        event.isCancelled = true

                        event.clickedInventory?.close()
                    }
                }
            }
            event.isCancelled = true
        }

    }
}

