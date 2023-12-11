package me.karlito.seax.listeners

import me.karlito.seax.crew.CrewHandler
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class OnLeaveListener : Listener{

    @EventHandler
    fun onPlayerLeave(event: PlayerQuitEvent) {

        CrewHandler().removePlayer(event.player)

    }

}