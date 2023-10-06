package me.karlito.seax.commnads

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class DashCommand : CommandExecutor {

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        // Check if the command sender is a player
        if(sender is Player) {
            if (sender.isOp) {
                sender.sendMessage(Component.text("You Dashed").color(TextColor.color(140, 144, 139)).decorate(TextDecoration.BOLD))
                sender.velocity = sender.velocity.add(Vector(10.0, 0.5, 0.0))
            }
        }
        return true
    }
}