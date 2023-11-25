package me.karlito.seax.crew.scoreboard

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.*

class ScoreBoardHandler {

    fun createScoreBoard(player : Player) {
        val manager: ScoreboardManager = Bukkit.getScoreboardManager()
        val scoreboard: Scoreboard = manager.newScoreboard

        // Create an objective
        val objective: Objective = scoreboard.registerNewObjective("${ChatColor.GOLD}  ᴘɪʀᴀᴛᴇ ᴄʀᴀꜰᴛ  ", "placeholder")
        objective.displaySlot = DisplaySlot.SIDEBAR


        val score1: Score = objective.getScore("")
        score1.score = 8
        val score2: Score = objective.getScore("${ChatColor.GOLD}⛃ Coins WIP")
        score2.score = 7
        val score3: Score = objective.getScore("${ChatColor.GRAY}⛃ Silver WIP")
        score3.score = 6
        val score4: Score = objective.getScore("${ChatColor.BLUE}Crew :")
        score4.score = 5
        val score5: Score = objective.getScore("  Join a crew!")
        score5.score = 4
        val score6: Score = objective.getScore("  Join a crew!")
        score6.score = 3
        val score7: Score = objective.getScore("  Join a crew!")
        score7.score = 2
        val score8: Score = objective.getScore("  Join a crew!")
        score8.score = 1

        player.scoreboard = scoreboard
    }

}