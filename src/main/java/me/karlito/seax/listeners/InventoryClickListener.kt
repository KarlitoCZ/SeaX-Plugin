package me.karlito.seax.listeners

import dev.lone.itemsadder.api.ItemsAdder.getCustomItemName
import me.karlito.seax.SeaX.Companion.guiMap
import me.karlito.seax.datastore.DatabaseUtils
import me.karlito.seax.islands.IslandHandler
import me.karlito.seax.levels.LevelCalculate
import me.karlito.seax.trading_companies.voyages.SMVoyages
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class InventoryClickListener : Listener { // Used for voyage system

    val plugin = Bukkit.getPluginManager().getPlugin("SeaX")
    val config = plugin!!.config

    @EventHandler
    fun inventorClickEvent(event: InventoryClickEvent) {
        val player = event.whoClicked as Player
        val playerUUID = event.whoClicked.uniqueId
        val item = event.currentItem
        val itemMeta = item?.itemMeta
        val levelReq = config.getInt("voyages.voyage1.level")
        val playerSMxp = DatabaseUtils().getPlayerSMxp(player)
        val playerSTxp = DatabaseUtils().getPlayerSTxp(player)
        val playerWDxp = DatabaseUtils().getPlayerWDxp(player)
        val customItem = getCustomItemName(item)
        

        if (itemMeta != null) {
            if (itemMeta.hasCustomModelData() && itemMeta.customModelData == 4867) {
                event.isCancelled = true
            }
        }

        if (event.inventory == guiMap[playerUUID]) {
            if (event.currentItem == null) return
            if (event.currentItem!!.itemMeta.hasCustomModelData()) {
                when (event.currentItem?.itemMeta?.customModelData) {

                    1788 -> {
                        val (currentLevel, remainingXpMax) = LevelCalculate().calculateLevel(playerSMxp)
                        if (currentLevel >= levelReq) {
                            event.isCancelled = true
                            SMVoyages().smVoyageEvent1(player)
                            event.clickedInventory?.close()
                        }
                    }
                    4693 -> {
                        event.isCancelled = true
                        val itemh = event.currentItem
                        val itemMetah = itemh!!.itemMeta
                        val itemDisplayName = itemMetah.displayName
                        val itemName = ChatColor.stripColor(itemDisplayName)
                        if (itemName != null) {
                            val location = IslandHandler().findIslandByName(itemName)
                            println("$location")
                            val compassItem = player.inventory.getItem(8)
                            val compassType = Material.COMPASS
                            if (compassItem != null && compassItem.type == compassType ) {
                                player.compassTarget = location!!
                                compassItem.addUnsafeEnchantment(Enchantment.DURABILITY, 1)
                                compassItem.lore = listOf("" ,"${ChatColor.YELLOW}Navigating to ${ChatColor.BLUE}${ChatColor.BOLD}$itemName")
                            }
                        }
                        event.clickedInventory?.close()
                    }
                }
                if (customItem != null) {
                    
                }
            }
            event.isCancelled = true
        }
    }
}

