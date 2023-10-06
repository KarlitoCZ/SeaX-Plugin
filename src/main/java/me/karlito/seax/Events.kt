package me.karlito.seax

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class playerJumpListener : Listener {

    @EventHandler
    fun jumpPlayerEvent (event: PlayerJumpEvent) {
        val plr = event.player

        plr.sendMessage("&4You Jumped")
    }
}