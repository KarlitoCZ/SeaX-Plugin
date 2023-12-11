package me.karlito.seax.datastore

import me.karlito.seax.SeaX.Companion.connection
import me.karlito.seax.crew.CrewHandler
import me.karlito.seax.crew.scoreboard.ScoreBoardHandler
import org.bukkit.entity.Player
import java.sql.SQLException


class DatabaseUtils {


        fun database() {

            //val connection = connection
            val statement = connection?.createStatement()
            statement?.execute("""CREATE TABLE IF NOT EXISTS players (
                                          uuid varchar(36) NOT NULL PRIMARY KEY,
                                          username varchar(50) NOT NULL,
                                          coins int UNSIGNED NOT NULL DEFAULT 0,
                                          silver smallint UNSIGNED NOT NULL DEFAULT 0,
                                          sm_xp int UNSIGNED NOT NULL DEFAULT 0,
                                          st_xp int UNSIGNED NOT NULL DEFAULT 0,
                                          wd_xp int UNSIGNED NOT NULL DEFAULT 0
                                       );""")
            statement?.close()
        }

    fun closeConnection() {
        if (connection != null && !connection!!.isClosed) {
            connection?.close()
        }
    }
        fun addPlayerData(player : Player) {
            if (connection != null) {
            connection!!.prepareStatement("INSERT IGNORE INTO players (uuid, username) VALUES (?, ?))").use { preparedStatement ->
                preparedStatement.setString(1, player.uniqueId.toString())
                preparedStatement.setString(2, player.name)
                preparedStatement.executeUpdate()
            }
            }
        }

        @Throws(SQLException::class)
        fun playerExists(player: Player): Boolean {


            connection!!.prepareStatement("SELECT * FROM players WHERE uuid = ?").use { preparedStatement ->
                preparedStatement.setString(1, player.uniqueId.toString())
                val resultSet = preparedStatement?.executeQuery()
                return resultSet?.next() == true
            }
        }

        // --- Update player stats functions
        @Throws(SQLException::class)
        fun updatePlayerCoins(player: Player, coins: Int) {
            val scoreboard = ScoreBoardHandler()
            val members = CrewHandler().getMembers(player)
            if (connection != null) {
            //if the player doesn't exist, add them
                if (!playerExists(player)) {
                addPlayerData(player)
                }
                connection!!.prepareStatement("UPDATE players SET coins = ? WHERE uuid = ?").use { preparedStatement ->
                    preparedStatement.setInt(1, coins)
                    preparedStatement.setString(2, player.uniqueId.toString())
                    preparedStatement.executeUpdate()
                }
                scoreboard.deleteScoreboard(player)
                scoreboard.createScoreBoard(player)
                if (members != null) {
                    scoreboard.updateScoreBoard(player, members)
                }
            }
        }

        @Throws(SQLException::class)
        fun updatePlayerSilver(player: Player, silver: Int) {
            val scoreboard = ScoreBoardHandler()
            val members = CrewHandler().getMembers(player)
            if (connection != null) {
                //if the player doesn't exist, add them
                if (!playerExists(player)) {
                    addPlayerData(player)
                }
                connection!!.prepareStatement("UPDATE players SET silver = ? WHERE uuid = ?").use { preparedStatement ->
                    preparedStatement.setInt(1, silver)
                    preparedStatement.setString(2, player.uniqueId.toString())
                    preparedStatement.executeUpdate()
                }
                scoreboard.deleteScoreboard(player)
                scoreboard.createScoreBoard(player)
                if (members != null) {
                    scoreboard.updateScoreBoard(player, members)
                }
            }
        }

        @Throws(SQLException::class)
        fun updatePlayerSMxp(player: Player, smExp: Int) {
            if (connection != null) {
                //if the player doesn't exist, add them
                if (!playerExists(player)) {
                    addPlayerData(player)
                }
                connection!!.prepareStatement("UPDATE players SET sm_xp = ? WHERE uuid = ?").use { preparedStatement ->
                    preparedStatement.setInt(1, smExp)
                    preparedStatement.setString(2, player.uniqueId.toString())
                    preparedStatement.executeUpdate()
                }
            }
        }

        @Throws(SQLException::class)
        fun updatePlayerSTxp(player: Player, stExp: Int) {
            if (connection != null) {
                //if the player doesn't exist, add them
                if (!playerExists(player)) {
                    addPlayerData(player)
                }
                connection!!.prepareStatement("UPDATE players SET st_xp = ? WHERE uuid = ?").use { preparedStatement ->
                    preparedStatement.setInt(1, stExp)
                    preparedStatement.setString(2, player.uniqueId.toString())
                    preparedStatement.executeUpdate()
                }
            }
        }

        @Throws(SQLException::class)
        fun updatePlayerWDxp(player: Player, wdExp: Int) {
            if (connection != null) {
                //if the player doesn't exist, add them
                if (!playerExists(player)) {
                    addPlayerData(player)
                }
                connection!!.prepareStatement("UPDATE players SET wd_xp = ? WHERE uuid = ?").use { preparedStatement ->
                    preparedStatement.setInt(1, wdExp)
                    preparedStatement.setString(2, player.uniqueId.toString())
                    preparedStatement.executeUpdate()
                }
            }
        }



        // --- Get player stats functions
        @Throws(SQLException::class)
        fun getPlayerCoins(player: Player): Int {

                connection?.prepareStatement("SELECT coins FROM players WHERE uuid = ?").use { preparedStatement ->
                    preparedStatement?.setString(1, player.uniqueId.toString())
                    val resultSet = preparedStatement?.executeQuery()
                    return if (resultSet?.next() == true) {
                        resultSet.getInt("coins")
                    } else {
                        0 // Return 0 if the player has no points
                }
            }
        }

        @Throws(SQLException::class)
        fun getPlayerSilver(player: Player): Int {

            connection?.prepareStatement("SELECT silver FROM players WHERE uuid = ?").use { preparedStatement ->
                preparedStatement?.setString(1, player.uniqueId.toString())
                val resultSet = preparedStatement?.executeQuery()
                return if (resultSet?.next() == true) {
                    resultSet.getInt("silver")
                } else {
                    0 // Return 0 if the player has no points
                }
            }
        }

        @Throws(SQLException::class)
        fun getPlayerSMxp(player: Player): Int {

            connection?.prepareStatement("SELECT sm_xp FROM players WHERE uuid = ?").use { preparedStatement ->
                preparedStatement?.setString(1, player.uniqueId.toString())
                val resultSet = preparedStatement?.executeQuery()
                return if (resultSet?.next() == true) {
                    resultSet.getInt("sm_xp")
                } else {
                    0 // Return 0 if the player has no points
                }
            }
        }

        @Throws(SQLException::class)
        fun getPlayerSTxp(player: Player): Int {

            connection?.prepareStatement("SELECT st_xp FROM players WHERE uuid = ?").use { preparedStatement ->
                preparedStatement?.setString(1, player.uniqueId.toString())
                val resultSet = preparedStatement?.executeQuery()
                return if (resultSet?.next() == true) {
                    resultSet.getInt("st_xp")
                } else {
                    0 // Return 0 if the player has no points
                }
            }
        }

        fun getPlayerWDxp(player: Player): Int {

            connection?.prepareStatement("SELECT wd_xp FROM players WHERE uuid = ?").use { preparedStatement ->
                preparedStatement?.setString(1, player.uniqueId.toString())
                val resultSet = preparedStatement?.executeQuery()
                return if (resultSet?.next() == true) {
                    resultSet.getInt("wd_xp")
                } else {
                    0 // Return 0 if the player has no points
                }
            }
        }


}