package me.karlito.seax.listeners

import me.karlito.seax.gui.Guis
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class ClickListener : Listener {

    @EventHandler
    fun playerItemInteract(event: PlayerInteractEvent) {

        val item = event.item
        if (item != null) {
            val itemMeta = item.itemMeta
            if (itemMeta.hasCustomModelData() && itemMeta.customModelData == 4693) {
                Guis().openCompassGui(event.player)
            }
        }

    }

}