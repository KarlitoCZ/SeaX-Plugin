package me.karlito.seax.commands


import dev.lone.itemsadder.api.FontImages.FontImageWrapper
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class IaGui : CommandExecutor {

    override fun onCommand(sender: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        if(sender !is Player) return false

        val itemToAdd = ItemStack(Material.DIAMOND_SWORD)

            val inventory = TexturedInventoryWrapper(
            null,
            27,
            ChatColor.BLACK.toString() + "",
            FontImageWrapper("_iainternal:red_gui")
        )




        inventory.showInventory(sender)
    return true
    }

}