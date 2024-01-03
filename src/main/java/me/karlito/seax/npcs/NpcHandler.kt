package me.karlito.seax.npcs


import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.NPC
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.EntityType


class NpcHandler {


    fun createNpcs() {
        val plugin = Bukkit.getPluginManager().getPlugin("SeaX")
        val config: FileConfiguration = plugin!!.config
        val world: World? = Bukkit.getWorld("${config.get("npc-settings.world")}")


        val npc: NPC = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Skull Merchants")

        val x: Double = config.get("npc-settings.skull-merchants.cords1.x") as Double
        val y: Double = config.get("npc-settings.skull-merchants.cords1.y") as Double
        val z: Double = config.get("npc-settings.skull-merchants.cords1.z") as Double
        val yaw = config.get("npc-settings.skull-merchants.cords1.yaw").toString().toFloat()
        val loc = Location(world, x, y, z, yaw, 0F)

        if (world != null) {
            val spawnReason: net.citizensnpcs.api.event.SpawnReason = net.citizensnpcs.api.event.SpawnReason.CREATE
            npc.spawn(loc, spawnReason)


        } else {
            // Handle the case where the world is not found (perhaps log an error or use a default world).
        }
    }

    fun removeNpcs() {
        val npcRegistry = CitizensAPI.getNPCRegistry()
        if (!npcRegistry.none()) {
            for (npc in npcRegistry) {
                npc.despawn()
                npcRegistry.deregister(npc)
            }
        }
    }
}



