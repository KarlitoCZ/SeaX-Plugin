package me.karlito.seax.crew.scoreboard

import me.karlito.seax.datastore.DatabaseUtils
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.*

class ScoreBoardHandler {


    fun createScoreBoard(player: Player) {
        val manager: ScoreboardManager = Bukkit.getScoreboardManager()
        val scoreboard: Scoreboard = manager.newScoreboard
        val coins = DatabaseUtils().getPlayerCoins(player)
        val silver = DatabaseUtils().getPlayerSilver(player)


        val objective: Objective = scoreboard.registerNewObjective("${ChatColor.GOLD}  ᴘɪʀᴀᴛᴇ ᴄʀᴀꜰᴛ  ", "placeholder")
        objective.displaySlot = DisplaySlot.SIDEBAR


        val score1: Score = objective.getScore("     ")
        score1.score = 12
        val score9: Score = objective.getScore("   ")
        score9.score = 9
        val score4: Score = objective.getScore("  ")
        score4.score = 7
        val score5: Score = objective.getScore("${ChatColor.BLUE}Crew :")
        score5.score = 6

        val score7: Score = objective.getScore("${ChatColor.YELLOW}")
        score7.score = 1

        val score6: Score = objective.getScore("${ChatColor.YELLOW}ᴡᴡᴡ.ѕᴇᴀ-х.ᴇᴜ")
        score6.score = 0

        val team1: Team = scoreboard.registerNewTeam("team1")
        val teamKey = ChatColor.GOLD.toString()
        team1.addEntry(teamKey)
        team1.suffix = "${ChatColor.GOLD}⛃ Coins $coins"

        objective.getScore(teamKey).score = 11

        val team2: Team = scoreboard.registerNewTeam("team2")
        val teamKey2 = ChatColor.WHITE.toString()
        team2.addEntry(teamKey2)
        team2.suffix = "${ChatColor.GRAY}⛃ Silver $silver"

        objective.getScore(teamKey2).score = 10

        val team3: Team = scoreboard.registerNewTeam("team3")
        val teamKey3 = ChatColor.DARK_BLUE.toString()
        team3.addEntry(teamKey3)
        team3.suffix = "Status ${ChatColor.RED}${ChatColor.BOLD}COMBAT"

        objective.getScore(teamKey3).score = 8

        val teamEmptySlot1: Team = scoreboard.registerNewTeam("teamEmptySlot1")
        val teamKeyEmptySlot1 = ChatColor.DARK_PURPLE.toString()
        teamEmptySlot1.addEntry(teamKeyEmptySlot1)
        teamEmptySlot1.suffix = "${ChatColor.BLUE}- Crew Slot 1 "

        objective.getScore(teamKeyEmptySlot1).score = 5

        val teamEmptySlot2: Team = scoreboard.registerNewTeam("teamEmptySlot2")
        val teamKeyEmptySlot2 = ChatColor.LIGHT_PURPLE.toString()
        teamEmptySlot2.addEntry(teamKeyEmptySlot2)
        teamEmptySlot2.suffix = "${ChatColor.BLUE}- Crew Slot 2 "

        objective.getScore(teamKeyEmptySlot2).score = 4

        val teamEmptySlot3: Team = scoreboard.registerNewTeam("teamEmptySlot3")
        val teamKeyEmptySlot3 = ChatColor.GREEN.toString()
        teamEmptySlot3.addEntry(teamKeyEmptySlot3)
        teamEmptySlot3.suffix = "${ChatColor.BLUE}- Crew Slot 3 "

        objective.getScore(teamKeyEmptySlot3).score = 3

        val teamEmptySlot4: Team = scoreboard.registerNewTeam("teamEmptySlot4")
        val teamKeyEmptySlot4 = ChatColor.DARK_RED.toString()
        teamEmptySlot4.addEntry(teamKeyEmptySlot4)
        teamEmptySlot4.suffix = "${ChatColor.BLUE}- Crew Slot 4 "

        objective.getScore(teamKeyEmptySlot4).score = 2


        player.scoreboard = scoreboard
    }

    fun updateBoard(player: Player) {
        val coins = DatabaseUtils().getPlayerCoins(player)
        val silver = DatabaseUtils().getPlayerSilver(player)
        val scoreboard: Scoreboard = player.scoreboard
        val team1 = scoreboard.getTeam("team1")
        val team2 = scoreboard.getTeam("team2")

        team1?.setSuffix("${ChatColor.GOLD}⛃ Coins " + "$coins")
        team2?.setSuffix("${ChatColor.GRAY}⛃ Silver " + "$silver")
    }

    fun updateBoardSafeZone(player: Player, status : String) {
        val scoreboard: Scoreboard = player.scoreboard
        val team1 = scoreboard.getTeam("team3")

        when (status) {
            "☠ COMBAT" -> {
                team1?.setSuffix("Status ${ChatColor.RED}${ChatColor.BOLD}$status")
            }
            "☮ SAFE ZONE" -> {
                team1?.setSuffix("Status ${ChatColor.GREEN}${ChatColor.BOLD}$status")
            }
            else -> {
                println("Status Provided does not match.")
                team1?.setSuffix("Status ${ChatColor.DARK_RED}${ChatColor.BOLD}ERROR 405")
            }
        }

    }

    fun updateBoardCrewList(player: Player, crewMembers: List<String>) {

        val scoreboard: Scoreboard = player.scoreboard

        for ((index, crewMember) in crewMembers.withIndex()) {
            val team1 = scoreboard.getTeam("teamEmptySlot${index + 1}")
            team1?.setSuffix("${ChatColor.BLUE}${index + 1}. $crewMember")
            //val score: Score = objective!!.getScore("${ChatColor.BLUE}. $crewMember")
        }

        player.scoreboard = scoreboard
    }

    fun updateAllMemberScoreBoard(crewMembers: MutableList<String>?) {
        if (crewMembers != null) {
            for (member in crewMembers) {
                val player = Bukkit.getPlayerExact(member)
                if (player != null) {
                    updateBoardCrewList(player, crewMembers)
                }
            }
        }
    }

    fun wipeAllMembersScoreboard(members: MutableList<String>?) {

        if (members != null) {
            for (member in members) {
                val player = Bukkit.getPlayerExact(member)
                if (player != null) {
                    val scoreboard = player.scoreboard

                    val team1 = scoreboard.getTeam("teamEmptySlot1")
                    team1?.setSuffix("${ChatColor.BLUE}- Crew Slot 1 ")

                    val team2 = scoreboard.getTeam("teamEmptySlot2")
                    team2?.setSuffix("${ChatColor.BLUE}- Crew Slot 2 ")

                    val team3 = scoreboard.getTeam("teamEmptySlot3")
                    team3?.setSuffix("${ChatColor.BLUE}- Crew Slot 3 ")

                    val team4 = scoreboard.getTeam("teamEmptySlot4")
                    team4?.setSuffix("${ChatColor.BLUE}- Crew Slot 4 ")
                }
            }
        }

    }

}








