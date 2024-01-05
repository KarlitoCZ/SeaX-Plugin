package me.karlito.seax.gui

import dev.lone.itemsadder.api.FontImages.FontImageWrapper
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper
import dev.lone.itemsadder.api.ItemsAdder.getCustomItem
import me.karlito.seax.SeaX.Companion.guiMap
import me.karlito.seax.datastore.DatabaseUtils
import me.karlito.seax.levels.LevelCalculate
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class Guis {

    val plugin = Bukkit.getPluginManager().getPlugin("SeaX")
    val config = plugin!!.config

    fun checkGui(sender : Player) {
        if (guiMap[sender.uniqueId] != null)
        {
            val inv: Inventory? = guiMap[sender.uniqueId]
            inv!!.close()
        }

    }

    fun openSMgui(sender: Player) {

        checkGui(sender)

        val smInventory = Bukkit.createInventory(sender, 27, Component.text("").color(TextColor.color(0, 0, 0)))
        val texture = FontImageWrapper("seax:red_gui")

        // Store
        val itemsSection = config.getConfigurationSection("stores.sm") ?: return
        for (itemName in itemsSection.getKeys(false)) {
            val item = itemsSection.getConfigurationSection(itemName)

            if (item != null) {
                val guiIndex = item.getInt("gui_index")
                val id = item.getInt("id")
                val coinsPrice = item.getInt("price.coins")
                val silverPrice = item.getInt("price.silver")
                val level = item.getInt("level")

                val itemStack = getCustomItem("seax:$itemName") ?: return
                val itemStackMeta = itemStack.itemMeta
                val lore4 = "${ChatColor.BLUE}Level : $level"
                val lore5 = ""
                val lore1 = "${ChatColor.GOLD}Coins : $coinsPrice"
                val lore0 = ""
                val lore2 = "${ChatColor.GRAY}Silver : $silverPrice"
                val lore3 = if (DatabaseUtils().hasPlayerStoreItem(sender, id)) {
                    "${ChatColor.GREEN}${ChatColor.BOLD}Purchased"
                } else {
                    "${ChatColor.GREEN}${ChatColor.BOLD}BUY"
                }
                itemStackMeta.lore = listOf(lore4, lore5, lore1, lore2, lore0, lore3)

                itemStack.itemMeta = itemStackMeta
                smInventory.setItem(guiIndex, itemStack)

            }
        }

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

        // Voyage 1
        val voyage1 = ItemStack(Material.MAP)
        val voyageName = config.getString("voyages.voyage1.name")
        val costSilver = config.getInt("voyages.voyage1.costSilver")
        val costCoins = config.getInt("voyages.voyage1.costCoins")
        val levelReq = config.getInt("voyages.voyage1.level")
        val voyagemeta = voyage1.itemMeta
        voyagemeta.displayName(Component.text("${ChatColor.RED}${ChatColor.BOLD}$voyageName"))
        val lore1 = "${ChatColor.GOLD}Level : $levelReq"
        val lore2 = if (costSilver == 0) {
            "${ChatColor.GOLD}Cost : $costCoins Coins"
        } else if (costCoins != 0){
            "${ChatColor.GOLD}Cost : $costCoins Coins, ${ChatColor.GRAY}$costSilver Silver"
        } else {
            "${ChatColor.GOLD}Cost : ${ChatColor.GRAY}$costSilver Silver"
        }
        val lore3 = ""
        val lore4 = "${ChatColor.GOLD}Go to a random island and get the treasure."
        voyagemeta.lore = listOf(lore1, lore2, lore3, lore4)
        voyagemeta.setCustomModelData(1788)
        voyage1.itemMeta = voyagemeta

        smInventory.setItem(6, voyage1)
        smInventory.setItem(10, level)


        guiMap[sender.uniqueId] = smInventory

        sender.openInventory(smInventory)
        TexturedInventoryWrapper.setPlayerInventoryTexture(sender, texture)

    }

    fun openCompassGui(sender: Player) {

        checkGui(sender)

        val compassInventory = Bukkit.createInventory(sender, 27, Component.text("").color(TextColor.color(0, 0, 0)))
        val texture = FontImageWrapper("seax:compass_gui")

        val islandsSection = config.getConfigurationSection("islands") ?: return

        var index = 0

        for (islandName in islandsSection.getKeys(false)) {
            val islandLocation = islandsSection.getConfigurationSection(islandName)
            val name =  islandsSection.get("$islandName.name")

            if ( islandLocation != null) {
                val islandX = islandLocation.getInt("x")
                val islandY = islandLocation.getInt("y")
                val islandZ = islandLocation.getInt("z")

                val compass = ItemStack(Material.MAP)
                val compassMeta = compass.itemMeta

                val lore1 = "${ChatColor.GOLD}Click to navigate."

                compassMeta.displayName(Component.text("${ChatColor.BLUE}${ChatColor.BOLD}$name"))

                compassMeta.lore = listOf(lore1)

                compassMeta.setCustomModelData(4693)
                compass.itemMeta = compassMeta

                compassInventory.setItem(index, compass)

                index++
            }
        }



        guiMap[sender.uniqueId] = compassInventory

        sender.openInventory(compassInventory)
        TexturedInventoryWrapper.setPlayerInventoryTexture(sender, texture)
    }



}
