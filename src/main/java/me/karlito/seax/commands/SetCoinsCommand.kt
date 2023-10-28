package me.karlito.seax.commands

import me.karlito.seax.datastore.DatabaseUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.sql.SQLException


class SetCoinsCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, p1: Command, p2: String, args: Array<out String>): Boolean {

        if (sender is Player){
            if (args.size != 2) {
                sender.sendMessage("Provide arguments <player> <amount>")
                return true
            }
            // get Player
            val playerName = args.get(index = 0)
            val target = Bukkit.getPlayer(playerName)
            if (target == null) {
                sender.sendMessage("Player Not Found")
                return true
            }

            // get Amount
            var amount = 0
            try {
                amount = Integer.parseInt(args[1])
            } catch (_ : SQLException) {
                sender.sendMessage("An invalid amount was provided Also make sure that the amount is a whole number")
            }

            //Update players coins in DB
            try {
                DatabaseUtils().updatePlayerCoins(target, amount)
            } catch (_ : SQLException) {
                sender.sendMessage("An error occurred while updating the player coins")
            }



        }

        return true
    }
}