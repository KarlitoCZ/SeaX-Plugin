package me.karlito.seax.crew.scoreboard

import me.karlito.seax.datastore.DatabaseUtils
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.*

class ScoreBoardHandler {
    /*
    fun createScoreBoard(player: Player) {
        val manager: ScoreboardManager = Bukkit.getScoreboardManager()
        val scoreboard: Scoreboard = manager.newScoreboard
        val coins = DatabaseUtils().getPlayerCoins(player)
        val silver = DatabaseUtils().getPlayerSilver(player)
        // Create an objective
        val objective: Objective = scoreboard.registerNewObjective("${ChatColor.GOLD}  ᴘɪʀᴀᴛᴇ ᴄʀᴀꜰᴛ  ", "placeholder")
        objective.displaySlot = DisplaySlot.SIDEBAR


        val score1: Score = objective.getScore(" ")
        score1.score = 9
        val score2: Score = objective.getScore("${ChatColor.GOLD}⛃ Coins $coins")
        score2.score = 8
        val score3: Score = objective.getScore("${ChatColor.GRAY}⛃ Silver $silver")
        score3.score = 7
        val score4: Score = objective.getScore("")
        score4.score = 6
        val score5: Score = objective.getScore("${ChatColor.BLUE}Crew :")
        score5.score = 5

        player.scoreboard = scoreboard
    }

    fun deleteAllMemberScoreBoard(members: MutableList<String>?) {

        if (members != null) {
            for (member in members) {
                val player = Bukkit.getPlayer(member)
                val manager = Bukkit.getScoreboardManager()
                val newScoreboard = manager.mainScoreboard

                player?.scoreboard = newScoreboard


            }
        }


    }

    fun updateAllMemberScoreBoard(members: MutableList<String>?) {


        if (members != null) {

            for (member in members) {
                val player = Bukkit.getPlayerExact(member)
                val scoreboard: Scoreboard = player!!.scoreboard
                val objective: Objective? = scoreboard.getObjective("${ChatColor.GOLD}  ᴘɪʀᴀᴛᴇ ᴄʀᴀꜰᴛ  ")
                val score1: Score = objective!!.getScore("    - $member")
                score1.score = 1
                player.sendMessage(member)
                player.scoreboard = scoreboard
            }
        }
    }
     */

    fun createScoreboard(player : Player) {
        // Create an objective

        val manager: ScoreboardManager = Bukkit.getScoreboardManager()
        val scoreboard: Scoreboard = manager.newScoreboard
        val coins = DatabaseUtils().getPlayerCoins(player)
        val silver = DatabaseUtils().getPlayerSilver(player)

        val objective: Objective = scoreboard.registerNewObjective("${ChatColor.GOLD}  ᴘɪʀᴀᴛᴇ ᴄʀᴀꜰᴛ  ", "placeholder")
        objective.displaySlot = DisplaySlot.SIDEBAR

        // Set scores for individual player information
        val score1: Score = objective.getScore("${ChatColor.GOLD}⛃ Coins $coins")
        score1.score = 8
        val score2: Score = objective.getScore("${ChatColor.GRAY}⛃ Silver $silver")
        score2.score = 7
        val score3: Score = objective.getScore("")
        score3.score = 6

        // Set a dummy score for individual player
        val score4: Score = objective.getScore("${ChatColor.BLUE}Crew :")
        score4.score = 5
    }

    fun updateScoreBoard(player: Player, crewMembers: List<String>) {
        val manager: ScoreboardManager = Bukkit.getScoreboardManager()
        val scoreboard: Scoreboard = manager.newScoreboard
        val coins = DatabaseUtils().getPlayerCoins(player)
        val silver = DatabaseUtils().getPlayerSilver(player)

        // Create an objective
        val objective: Objective? = scoreboard.getObjective("${ChatColor.GOLD}  ᴘɪʀᴀᴛᴇ ᴄʀᴀꜰᴛ  ")
        objective?.displaySlot = DisplaySlot.SIDEBAR

        // Set scores for individual player information
        val score1: Score = objective!!.getScore("${ChatColor.GOLD}⛃ Coins $coins")
        score1.score = 8
        val score2: Score = objective.getScore("${ChatColor.GRAY}⛃ Silver $silver")
        score2.score = 7
        val score3: Score = objective.getScore("")
        score3.score = 6

        // Set a dummy score for individual player
        val score4: Score = objective.getScore("${ChatColor.BLUE}Crew :")
        score4.score = 5

        // Set scores for crew members
        for ((index, crewMember) in crewMembers.withIndex()) {
            val score: Score = objective.getScore("${ChatColor.BLUE}${index + 1}. $crewMember")
            score.score = 4 - index  // Reverse the order to show higher scores first
        }

        player.scoreboard = scoreboard
    }

    // Function to update scoreboards for all crew members
    fun updateAllMemberScoreBoard(crewMembers: List<String>?) {
        if (crewMembers != null) {
            for (member in crewMembers) {
                val player = Bukkit.getPlayerExact(member)
                if (player != null) {
                    updateScoreBoard(player, crewMembers)
                }
            }
        }
    }
}








