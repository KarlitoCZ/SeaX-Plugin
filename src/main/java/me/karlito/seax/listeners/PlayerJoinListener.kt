package me.karlito.seax.listeners

import me.karlito.seax.crew.CrewHandler
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
        val player = e.player
        val members = CrewHandler().getMembers(player)
        ScoreBoardHandler().createScoreBoard(e.player)

        if (!e.player.hasPlayedBefore()) {
            //add the player to the database
            DatabaseUtils().addPlayerData(e.player)
        }
    }
}