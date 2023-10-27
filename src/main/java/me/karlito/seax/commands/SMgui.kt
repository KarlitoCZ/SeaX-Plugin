package me.karlito.seax.commands

import dev.lone.itemsadder.api.FontImages.FontImageWrapper
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper
import me.karlito.seax.SeaX
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class SMgui : CommandExecutor {
    override fun onCommand(sender: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        if(sender !is Player) return false

        val smInventory = Bukkit.createInventory(sender, 27, Component.text("").color(TextColor.color(0, 0, 0)))
        val texture = FontImageWrapper("_iainternal:red_gui");

        // Buried treasure
        val level = ItemStack(Material.MAP)
        val levelmeta = level.itemMeta
        levelmeta.displayName(Component.text("Skull Merchants Level"))
        val lorelevel1 = "${ChatColor.GOLD}Level : "
        levelmeta.lore = listOf(lorelevel1)
        levelmeta.setCustomModelData(1788)
        level.itemMeta = levelmeta

        // Buried treasure
        val voyagemetarial = ItemStack(Material.MAP)
        val voyagemeta = voyagemetarial.itemMeta
        voyagemeta.displayName(Component.text("Voyage for the buried treasure"))
        val lore1 = "${ChatColor.GOLD}Go around the map and find buried loot"
        val lore2 = "${ChatColor.GOLD}and return it to the Skull Merchants."
        voyagemeta.lore = listOf(lore1, lore2)
        voyagemeta.setCustomModelData(1788)
        voyagemetarial.itemMeta = voyagemeta

        smInventory.setItem(6, voyagemetarial)


        SeaX.guiMap[sender.uniqueId] = smInventory

        sender.openInventory(smInventory)
        TexturedInventoryWrapper.setPlayerInventoryTexture(sender, texture)

        return true
    }
}