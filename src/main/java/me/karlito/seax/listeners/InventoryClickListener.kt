package me.karlito.seax.listeners

import me.karlito.seax.SeaX
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class InventoryClickListener : Listener {

    @EventHandler
    fun inventorClickEvent(event: InventoryClickEvent) {
        val playerUUID = event.whoClicked.uniqueId

        if(event.inventory ==  SeaX.guiMap[playerUUID]) {
            if(event.currentItem == null) return
            if(event.currentItem!!.itemMeta.hasCustomModelData()) {
                when(event.currentItem?.itemMeta?.customModelData) {
                    1788 -> {
                        event.whoClicked.sendMessage("You clicked on the Super Paper")
                    }
                }
            }
            else {
            }
            event.isCancelled = true
        }

    }
}

