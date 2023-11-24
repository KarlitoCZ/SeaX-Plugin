package me.karlito.seax.gui

import dev.lone.itemsadder.api.FontImages.FontImageWrapper
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper
import me.karlito.seax.SeaX
import me.karlito.seax.datastore.DatabaseUtils
import me.karlito.seax.levels.LevelCalculate
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
    override fun onCommand(sender: CommandSender, p1: Command, p2: String, args: Array<out String>): Boolean {
        if(sender !is Player) return false

        val smInventory = Bukkit.createInventory(sender, 27, Component.text("").color(TextColor.color(0, 0, 0)))
        val texture = FontImageWrapper("_iainternal:red_gui")


        // Level Item
        val level = ItemStack(Material.MAGMA_CREAM)
        val levelmeta = level.itemMeta
        val smXP = DatabaseUtils().getPlayerSMxp(sender)
        val (smlevel, remainingXpMax) = LevelCalculate().calculateLevel(smXP)

        val lorelevel1 = "${ChatColor.GOLD}Level : $smlevel"
        var lorelevel2: String? = null

        if (smlevel >= 20) {
            lorelevel2 = "${ChatColor.GOLD}Remaining Xp : ${ChatColor.GREEN}${ChatColor.BOLD}MAX LEVEL"
            
        } else {
            lorelevel2 = "${ChatColor.GOLD}Remaining Xp : $smXP/$remainingXpMax"
        }

        levelmeta.displayName(Component.text("${ChatColor.RED}${ChatColor.BOLD}Skull Merchants Level"))

        levelmeta.lore = listOf(lorelevel1, lorelevel2)

        levelmeta.setCustomModelData(3487)
        level.itemMeta = levelmeta

        // Buried treasure
        val voyage1 = ItemStack(Material.MAP)
        val voyagemeta = voyage1.itemMeta
        voyagemeta.displayName(Component.text("${ChatColor.RED}${ChatColor.BOLD}Voyage for the buried treasure"))
        val lore1 = "${ChatColor.GOLD}Go around the map and find buried loot"
        val lore2 = "${ChatColor.GOLD}and return it to the Skull Merchants."
        voyagemeta.lore = listOf(lore1, lore2)
        voyagemeta.setCustomModelData(1788)
        voyage1.itemMeta = voyagemeta

        smInventory.setItem(6, voyage1)
        smInventory.setItem(10, level)


        SeaX.guiMap[sender.uniqueId] = smInventory

        sender.openInventory(smInventory)
        TexturedInventoryWrapper.setPlayerInventoryTexture(sender, texture)

        return true
    }
}