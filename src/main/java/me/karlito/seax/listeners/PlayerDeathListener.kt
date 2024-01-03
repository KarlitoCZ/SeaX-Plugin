package me.karlito.seax.listeners

import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

class PlayerDeathListener : Listener {

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) { //voyageItemMap[player.uniqueId] = paper
        val entity = event.entity

        if (entity.type == EntityType.PLAYER) {
            val player = entity as Player
            val drops = event.drops
            val item1 = player.inventory.getItem(8)!!

            when {
                player.inventory.contains(Material.COMPASS) -> {
                    event.drops.removeIf { it.type == Material.COMPASS }

                }
                player.inventory.contains(Material.PAPER) -> {
                    event.drops.removeIf { it.type == Material.PAPER }

                }

            }

        }
    }

}