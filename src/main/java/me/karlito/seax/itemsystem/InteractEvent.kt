package me.karlito.seax.itemsystem

import io.lumine.mythic.bukkit.MythicBukkit
import me.karlito.seax.SeaX
import me.karlito.seax.SeaX.Companion.attachedEntities
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.scheduler.BukkitRunnable


class InteractEvent : Listener {

    private val itemHoldHandler = ItemHoldHandler()

    @EventHandler
    fun onInteractEntity(event: PlayerInteractAtEntityEvent) {
        val entity = event.rightClicked
        val player = event.player
        val playerName = event.player.name
        val plugin = Bukkit.getPluginManager().getPlugin("SeaX")
        val config: FileConfiguration = plugin!!.config

        val mythicMob = MythicBukkit.inst().mobManager.getActiveMob(entity.uniqueId).orElse(null)
        if (mythicMob != null) {
            if (config.contains("loot-table.${mythicMob.mobType}")) {
                if (!attachedEntities.containsKey(playerName)) {
                    itemHoldHandler.startTask(player, entity)

                }

            }
        }
    }

    @EventHandler
    fun onSneak(event: PlayerToggleSneakEvent) {
        val player = event.player
        val playerName = player.name
        if (event.isSneaking && attachedEntities.containsKey(playerName)) {
            val attachedEntity = attachedEntities[playerName]
            itemHoldHandler.stopTask(player)
        }
    }
}

class ItemHoldHandler() {

    val plugin = Bukkit.getPluginManager().getPlugin("SeaX")

    private val task: MutableMap<String, BukkitRunnable> = mutableMapOf()

    fun startTask(player: Player, entity: Entity) {

        if (entity !in attachedEntities.values) {
            attachedEntities[player.name] = entity

            task[player.name] = object : BukkitRunnable() {
                override fun run() {
                    val playerLocation = player.location
                    playerLocation.add(0.5, 0.5, 0.0)
                    entity.teleport(playerLocation)
                }
            }
            if (plugin != null) {
                task[player.name]?.runTaskTimer(plugin, 0L, 1L)
            }
        }
    }

    fun stopTask(player: Player) {
        task[player.name]?.cancel()
        SeaX.attachedEntities.remove(player.name)
    }

}