package me.karlito.seax.listeners

import me.karlito.seax.SeaX
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent

class InventoryCloseListener : Listener {

    @EventHandler
    fun inventoryCloseEvent(event: InventoryCloseEvent) {
        val playerUUID = event.player.uniqueId

        if(SeaX.guiMap.containsKey(playerUUID)) SeaX.guiMap.remove(playerUUID)
    }
}