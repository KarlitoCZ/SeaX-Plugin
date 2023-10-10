package me.karlito.seax.commands

import dev.lone.itemsadder.api.FontImages.FontImageWrapper
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper
import me.karlito.seax.SeaX
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class DefaultGuicmd : CommandExecutor {
    override fun onCommand(sender: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        if(sender !is Player) return false

        val defInventory = Bukkit.createInventory(sender, 27, Component.text("").color(TextColor.color(0, 0, 0)))
        val texture = FontImageWrapper("_iainternal:red_gui");
        val paper = ItemStack(Material.PAPER)
        val papermeta = paper.itemMeta
        papermeta.displayName(Component.text("Super Paper"))
        papermeta.setCustomModelData(1788)

        paper.itemMeta = papermeta

        defInventory.setItem(6, paper)

        SeaX.guiMap[sender.uniqueId] = defInventory

        sender.openInventory(defInventory)
        TexturedInventoryWrapper.setPlayerInventoryTexture(sender, texture)

        return true
    }
}