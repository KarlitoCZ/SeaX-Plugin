package me.karlito.seax.listeners

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.ItemStack

class PlayerRespawnListener : Listener {

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        val player: Player = event.player

        val itemStack = ItemStack(Material.COMPASS, 1)
        val itemMeta = itemStack.itemMeta
        itemMeta.setCustomModelData(4867)
        itemMeta.setDisplayName("${ChatColor.BLUE}${player.name}'s Compass")
        itemStack.itemMeta = itemMeta
        player.inventory.setItem(8, itemStack)

    }

}