package me.karlito.seax.listeners

import me.karlito.seax.crew.scoreboard.ScoreBoardHandler
import me.karlito.seax.datastore.DatabaseUtils
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.sql.SQLException
import java.util.*


class PlayerJoinListener : Listener {
    @EventHandler
    @Throws(SQLException::class)
    fun inJoin(e: PlayerJoinEvent) {
        ScoreBoardHandler().createScoreBoard(e.player)
        //if the player is new, add them to the database
        if (!e.player.hasPlayedBefore()) {
            //add the player to the database
            DatabaseUtils().addPlayerData(e.player)
        }
    }
}