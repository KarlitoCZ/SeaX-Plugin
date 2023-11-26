package me.karlito.seax.commands

import me.karlito.seax.crew.scoreboard.ScoreBoardHandler
import me.karlito.seax.datastore.DatabaseUtils
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.sql.SQLException


class GetCoinsCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, p1: Command, p2: String, args: Array<out String>): Boolean {
        try {
            if (args.isEmpty() && sender is Player) {
                val coins: Int = DatabaseUtils().getPlayerCoins(sender)

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2☠&r&aYou have &e$coins&a coins!"))
                return true
            } else {

                //Get the player
                val playerName = args[0]
                val player = Bukkit.getServer().getPlayer(playerName)
                if (player == null) {
                    sender.sendMessage("Player not found!")
                    return true
                }

                //Get the amount
                val amount: Int = DatabaseUtils().getPlayerCoins(player)
                sender.sendMessage(
                    ChatColor.translateAlternateColorCodes(
                        '&',
                        "&a" + player.name + " has &e" + amount + "&a points!"
                    )
                )
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            sender.sendMessage("An error occurred while getting your points. Try again later.")
            return true
        }


        return true
    }

}