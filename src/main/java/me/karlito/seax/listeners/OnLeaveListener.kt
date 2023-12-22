package me.karlito.seax.listeners

import me.karlito.seax.crew.CrewHandler
import me.karlito.seax.itemsystem.ItemHoldHandler
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class OnLeaveListener : Listener{

    @EventHandler
    fun onPlayerLeave(event: PlayerQuitEvent) {

        val itemHoldHandler = ItemHoldHandler()
        itemHoldHandler.stopTask(event.player)
        CrewHandler().removePlayer(event.player)
        val item = event.player.inventory.getItem(8)
        if (item != null) {
            event.player.inventory.remove(item)
        }
    }

}