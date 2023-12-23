package me.karlito.seax.listeners

import me.karlito.seax.SeaX.Companion.guiMap
import me.karlito.seax.islands.IslandHandler
import me.karlito.seax.trading_companies.voyages.SMVoyages
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class InventoryClickListener : Listener { // Used for voyage system

    @EventHandler
    fun inventorClickEvent(event: InventoryClickEvent) {
        val player = event.whoClicked as Player
        val playerUUID = event.whoClicked.uniqueId
        val item = event.currentItem
        val itemMeta = item!!.itemMeta


        if (itemMeta.hasCustomModelData() && itemMeta.customModelData == 4867) {
            event.isCancelled = true
        }

        if (event.inventory == guiMap[playerUUID]) {
            if (event.currentItem == null) return
            if (event.currentItem!!.itemMeta.hasCustomModelData()) {
                when (event.currentItem?.itemMeta?.customModelData) {
                    1788 -> {
                        event.isCancelled = true
                        SMVoyages().voyageEvent1(player)
                        event.clickedInventory?.close()
                    }
                    4693 -> {
                        event.isCancelled = true
                        val item = event.currentItem
                        val itemMeta = item!!.itemMeta
                        val itemDisplayName = itemMeta.displayName
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
            }
            event.isCancelled = true
        }
    }
}

