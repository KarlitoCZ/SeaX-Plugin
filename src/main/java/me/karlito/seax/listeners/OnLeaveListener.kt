package me.karlito.seax.listeners

import me.karlito.seax.SeaX.Companion.playerActiveVoyage
import me.karlito.seax.crew.CrewHandler
import me.karlito.seax.itemsystem.ItemHoldHandler
import me.karlito.seax.trading_companies.voyages.VoyageHandler
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class OnLeaveListener : Listener{

    @EventHandler
    fun onPlayerLeave(event: PlayerQuitEvent) {
        val crewHandler = CrewHandler()
        val members = crewHandler.getMembers(event.player)
        val itemHoldHandler = ItemHoldHandler()
        itemHoldHandler.stopTask(event.player)
        CrewHandler().removePlayer(event.player)
        val item = event.player.inventory.getItem(8)
        if (playerActiveVoyage[event.player.uniqueId] != null) {
            VoyageHandler().voyageCancel(event.player)
        }
        if (item != null) {
            event.player.inventory.remove(item)
        }
    }

}