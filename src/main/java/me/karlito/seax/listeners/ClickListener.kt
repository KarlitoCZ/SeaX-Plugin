package me.karlito.seax.listeners

import me.karlito.seax.gui.Guis
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class ClickListener : Listener {


    @EventHandler
    fun playerItemInteract(event: PlayerInteractEvent) {
        println("1")
        val plugin = Bukkit.getPluginManager().getPlugin("SeaX")
        val item = event.item
        val config = plugin!!.config
        if (item != null) {
            println("2")
            val itemMeta = item.itemMeta
            if (itemMeta.hasCustomModelData() && itemMeta.customModelData == 4867) {
                println("3")
                Guis().openCompassGui(event.player)
            }
        }

    }



}
