package me.karlito.seax.commnads


import dev.lone.itemsadder.api.FontImages.FontImageWrapper
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class IaGui : CommandExecutor {

    override fun onCommand(sender: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        if(sender is Player) {

            val inventory = TexturedInventoryWrapper(
            null,
            54,
            ChatColor.BLACK.toString() + "Test \nTest",
            FontImageWrapper("_iainternal:blank_menu")
        )

        inventory.showInventory(sender)
        }
    return true
    }

}