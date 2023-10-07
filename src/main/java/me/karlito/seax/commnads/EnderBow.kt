package me.karlito.seax.commnads

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


class EnderBow : CommandExecutor {
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
        val enderBow : ItemStack = ItemStack(Material.BOW)
        val itemMeta = enderBow.itemMeta
        val lore = itemMeta.lore ?: mutableListOf()

        itemMeta.displayName(Component.text("Ender Bow").color(TextColor.color(164, 46, 140)))
        lore.add(Component.text("Test :rare:").toString())
        itemMeta.setCustomModelData(2936)

        enderBow.itemMeta = itemMeta

        val playerInventory = sender.inventory
        playerInventory.addItem(enderBow)

        return true
    }
}