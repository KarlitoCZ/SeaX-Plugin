package me.karlito.seax.trading_companies.gui

import dev.lone.itemsadder.api.FontImages.FontImageWrapper
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper
import me.karlito.seax.SeaX.Companion.guiMap
import me.karlito.seax.datastore.DatabaseUtils
import me.karlito.seax.levels.LevelCalculate
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Guis {
    fun openSMgui(sender: Player) {

        if (guiMap[sender.uniqueId] == null) {

            val smInventory = Bukkit.createInventory(sender, 27, Component.text("").color(TextColor.color(0, 0, 0)))
            val texture = FontImageWrapper("_iainternal:red_gui")

            // Level Item
            val level = ItemStack(Material.MAGMA_CREAM)
            val levelmeta = level.itemMeta
            val smXP = DatabaseUtils().getPlayerSMxp(sender)
            val (smlevel, remainingXpMax) = LevelCalculate().calculateLevel(smXP)

            val lorelevel1 = "${ChatColor.GOLD}Level : $smlevel"

            val lorelevel2 = if (smlevel >= LevelCalculate.maxLvl) {
                "${ChatColor.GOLD}Remaining Xp : ${ChatColor.GREEN}${ChatColor.BOLD}MAX LEVEL"

            } else {
                "${ChatColor.GOLD}Remaining Xp : $smXP/$remainingXpMax"
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


            guiMap[sender.uniqueId] = smInventory

            sender.openInventory(smInventory)
            TexturedInventoryWrapper.setPlayerInventoryTexture(sender, texture)

        }
    }
}