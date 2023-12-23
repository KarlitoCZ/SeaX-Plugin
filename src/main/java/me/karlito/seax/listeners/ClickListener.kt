package me.karlito.seax.listeners

import me.karlito.seax.gui.Guis
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class ClickListener : Listener {


    @EventHandler
    fun playerItemInteract(event: PlayerInteractEvent) {
        val plugin = Bukkit.getPluginManager().getPlugin("SeaX")
        val item = event.item

        val config = plugin!!.config
        if (item != null && event.action.isRightClick) {
            val itemMeta = item.itemMeta
            if (itemMeta.hasCustomModelData() && itemMeta.customModelData == 4867) {
                Guis().openCompassGui(event.player)
            }
        }

    }



}
