package me.karlito.seax.commands

import me.clip.placeholderapi.PlaceholderAPI
import me.karlito.seax.datastore.DatabaseUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


class IronAxe : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        // Check if the command sender is a player
        if (sender !is Player) {
            sender.sendMessage("Only players can use this command.")

            return true
        }
        val enderBow : ItemStack = ItemStack(Material.IRON_AXE)
        val itemMeta = enderBow.itemMeta


        // Create formatted and multi-line lore
        val loreLine1 = PlaceholderAPI.setPlaceholders(sender,"${ChatColor.RESET}${ChatColor.WHITE}%img_cosmetic%")
        val loreLine2 = "${ChatColor.GOLD}This item is only a cosmetic."
        DatabaseUtils().addPlayerData(sender)
        // Add the lore lines to the item
        itemMeta.lore = listOf(loreLine1, loreLine2)

        itemMeta.displayName(Component.text("Best Axe Ever").color(TextColor.color(164, 46, 140)))
        itemMeta.setCustomModelData(2936)



        enderBow.itemMeta = itemMeta

        val playerInventory = sender.inventory
        playerInventory.addItem(enderBow)

        return true
    }
}