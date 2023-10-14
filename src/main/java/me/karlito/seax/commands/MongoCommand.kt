package me.karlito.seax.commands

import kotlinx.coroutines.runBlocking
import me.karlito.seax.datastore.LevelsUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MongoCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        if (sender is Player){
            runBlocking {
                LevelsUtils().createPlayerDocument(sender)
                sender.sendMessage("MongoCommand")

            }
        }
        return true
    }
}