package me.karlito.seax.listeners

import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

class PlayerDeathListener : Listener {

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        val entity = event.entity

        if (entity.type == EntityType.PLAYER) {
            val player = entity as Player

            val iterator = event.drops.iterator()
            while (iterator.hasNext()) {
                val item = iterator.next()
                if (item.type == Material.COMPASS) {
                    iterator.remove()
                    break
                }
            }
        }
    }

}