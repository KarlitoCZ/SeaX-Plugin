package me.karlito.seax.itemsystem

import com.ticxo.modelengine.api.ModelEngineAPI
import com.ticxo.modelengine.api.model.ActiveModel
import com.ticxo.modelengine.api.model.ModeledEntity
import io.lumine.mythic.bukkit.events.MythicMobSpawnEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener


class MythicSpawn : Listener{

    @EventHandler
    fun spawned(event: MythicMobSpawnEvent) {
        val mob = event.entity
        val mobname = event.entity.name

        // Spawn a new entity

        val modeledEntity: ModeledEntity = ModelEngineAPI.createModeledEntity(mob)

        val activeModel: ActiveModel = ModelEngineAPI.createActiveModel(mobname)

        modeledEntity.addModel(activeModel, true)


    }
}