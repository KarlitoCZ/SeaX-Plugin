package me.karlito.seax.listeners

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

class PlayerTrowListener : Listener {

    @EventHandler
    fun playerItemTrow(event: PlayerDropItemEvent) {
        val plugin = Bukkit.getPluginManager().getPlugin("SeaX")
        val item = event.itemDrop.itemStack

        val itemMeta = item.itemMeta
        if (itemMeta.hasCustomModelData()) {
            when (itemMeta.customModelData) {
                4867 -> {
                    event.isCancelled = true
                }
            }
        }


    }

}