package me.karlito.seax.datastore

import me.karlito.seax.SeaX.Companion.connection
import org.bukkit.entity.Player
import java.sql.SQLException

class DatabaseUtils {


        fun pointDatabase() {

            //val connection = connection
            val statement = connection?.createStatement()
            statement?.execute("""CREATE TABLE IF NOT EXISTS players (
                                          uuid varchar(36) NOT NULL PRIMARY KEY,
                                          username varchar(50) NOT NULL,
                                          coins mediumint UNSIGNED NOT NULL DEFAULT 0
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
            connection!!.prepareStatement("INSERT INTO players (uuid, username) VALUES (?, ?)").use { preparedStatement ->
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

        @Throws(SQLException::class)
        fun updatePlayerCoins(player: Player, coins: Int) {
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
            }
        }

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


}