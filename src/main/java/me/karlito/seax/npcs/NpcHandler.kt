package me.karlito.seax.npcs


import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.NPC
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.EntityType


class NpcHandler {


    fun createNpcs() {

        val npc: NPC = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Seller")
        val world: World? = Bukkit.getWorld("world")
        val loc: Location = Location(world, 716.0, 64.0, 79.0)


        if (world != null) {
            val spawnReason: net.citizensnpcs.api.event.SpawnReason = net.citizensnpcs.api.event.SpawnReason.CREATE // You might want to use a more appropriate SpawnReason

            npc.spawn(loc, spawnReason)


        } else {
            // Handle the case where the world is not found (perhaps log an error or use a default world).
        }
    }

    fun removeNpcs() {
        val npcRegistry = CitizensAPI.getNPCRegistry()

        for (npc in npcRegistry) {
            npc.despawn()
            npcRegistry.deregister(npc)
        }
    }
}



