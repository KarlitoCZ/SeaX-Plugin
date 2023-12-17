package me.karlito.seax.safezones

import me.karlito.seax.crew.scoreboard.ScoreBoardHandler
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerMoveEvent

class SafeZones {

    val safezoneNames = mutableListOf<String>()
    val minXList = mutableListOf<Double>()
    val minYList = mutableListOf<Double>()
    val minZList = mutableListOf<Double>()
    val maxXList = mutableListOf<Double>()
    val maxYList = mutableListOf<Double>()
    val maxZList = mutableListOf<Double>()

    fun isInSafeZone(location: Location, index: Int): Boolean {
        return location.x >= minXList[index] && location.x <= maxXList[index] &&
                location.y >= minYList[index] && location.y <= maxYList[index] &&
                location.z >= minZList[index] && location.z <= maxZList[index]
    }

    fun addSafeZone(
        name: String,
        minX: Double,
        minY: Double,
        minZ: Double,
        maxX: Double,
        maxY: Double,
        maxZ: Double
    ) {
        safezoneNames.add(name)
        minXList.add(minX)
        minYList.add(minY)
        minZList.add(minZ)
        maxXList.add(maxX)
        maxYList.add(maxY)
        maxZList.add(maxZ)
    }

}

class MoveEvent : Listener {

    //  ScoreBoardHandler().updateBoardSafeZone(player, "☠ COMBAT")

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        val toLocation: Location = event.to


        // Check if the player is in any of the specified areas
        for (i in SafeZones().safezoneNames.indices) {
            if (SafeZones().isInSafeZone(toLocation, i)) {
                ScoreBoardHandler().updateBoardSafeZone(player, "☮ SAFE ZONE")
                player.player
            } else {
                ScoreBoardHandler().updateBoardSafeZone(player, "☠ COMBAT")
            }
        }
    }

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        val damager: Entity = event.damager
        if (damager is Player) {
            val toLocation: Location = damager.location
            for (i in SafeZones().safezoneNames.indices) {
                if (SafeZones().isInSafeZone(toLocation, i)) {
                    event.isCancelled = true
                    return
                }
            }
        }
    }

}