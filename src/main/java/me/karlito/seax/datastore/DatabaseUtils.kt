package me.karlito.seax.datastore

import org.bukkit.entity.Player
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

// TODO fix this null shit
class DatabaseUtils {

        companion object {

            var connection: Connection? = null

        }



        fun pointDatabase(path : String) {

            connection = DriverManager.getConnection("jdbc:sqlite:" + path )
            //val connection = connection
            val statement = connection?.createStatement()
            statement?.execute("CREATE TABLE IF NOT EXISTS players (" +
                    "uuid TEXT PRIMARY KEY," +
                    "username TEXT NOT NULL," +
                    "coins INTEGER NOT NULL DEFAULT 0)")
            statement?.close()


        }

    fun closeConnection() {
        if (connection != null && !connection!!.isClosed) {
            connection?.close()
        }
    }




        fun addPlayerData(player : Player) {
            player.sendMessage("$connection")
            if (connection != null) {
            connection!!.prepareStatement("INSERT INTO players (uuid, username) VALUES (?, ?)").use { preparedStatement ->
                player.sendMessage("TestX2") // TODO fix this shit null
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