package me.karlito.seax.commnads

import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
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
        val lore = itemMeta.lore
        val testtext = "Test Here %img_cosmetic%"

        itemMeta.displayName(Component.text("Best Axe Ever").color(TextColor.color(164, 46, 140)))
        itemMeta.setCustomModelData(2936)



        enderBow.itemMeta = itemMeta

        val playerInventory = sender.inventory
        playerInventory.addItem(enderBow)

        return true
    }
}