package me.karlito.seax.itemsystem

import io.lumine.mythic.bukkit.MythicBukkit
import org.bukkit.entity.Entity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerToggleSneakEvent


class InteractEvent : Listener{

    val lootArray = arrayOf("SunkenChest", "Skull") //that you can pick up USE MYTHIC-MOB NAME

    private val attachedEntities = mutableMapOf<String, Entity>()

    @EventHandler
    fun onInteractEntity(event: PlayerInteractAtEntityEvent) {
        val entity = event.rightClicked
        val player = event.player
        val playerName = event.player.name

        val mythicMob = MythicBukkit.inst().mobManager.getActiveMob(entity.uniqueId).orElse(null)
        if (mythicMob != null && mythicMob.mobType in lootArray) {
            event.player.sendMessage("DETECTED")
            if (!attachedEntities.containsKey(playerName)) {
                entity.teleport(player.location)
                player.addPassenger(entity)

                attachedEntities[playerName] = entity
            }

        }


    }

    @EventHandler
    fun onSneak (event: PlayerToggleSneakEvent) {
        val player = event.player
        val playerName = player.name

        if (event.isSneaking && attachedEntities.containsKey(playerName)) {
            val attachedEntity = attachedEntities[playerName]
            attachedEntity?.removePassenger(player)
            attachedEntities.remove(playerName)
        }
    }
}