package me.karlito.seax.listeners

import dev.lone.itemsadder.api.ItemsAdder.getCustomItem
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
        val customItem = getCustomItemName(item) ?: return
        val giveItem = getCustomItem(customItem) ?: return
        val customItemString = customItem.substringAfter(':')


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
                            if (compassItem != null && compassItem.type == compassType) {
                                player.compassTarget = location!!
                                compassItem.addUnsafeEnchantment(Enchantment.DURABILITY, 1)
                                compassItem.lore = listOf(
                                    "",
                                    "${ChatColor.YELLOW}Navigating to ${ChatColor.BLUE}${ChatColor.BOLD}$itemName"
                                )
                            }
                        }
                        event.clickedInventory?.close()
                    }
                }
                if (config.isConfigurationSection("stores.sm.$customItemString")) {
                    val playerXpSM = DatabaseUtils().getPlayerSMxp(player)
                    val (currentLevelSM, _) = LevelCalculate().calculateLevel(playerXpSM)
                    val sectionSM = config.getConfigurationSection("stores.sm.$customItemString")
                    val reqLevelSM = sectionSM?.getInt("level") ?: return
                    if (currentLevelSM >= reqLevelSM) {

                        val coins = sectionSM.getInt("price.coins")
                        val silver = sectionSM.getInt("price.silver")

                        val playerCoins = DatabaseUtils().getPlayerCoins(player)
                        val playerSilver = DatabaseUtils().getPlayerSilver(player)

                        if (playerCoins >= coins && playerSilver >= silver) {
                            DatabaseUtils().updatePlayerCoins(player, playerCoins - coins)
                            DatabaseUtils().updatePlayerSilver(player, playerSilver - silver)

                            val inventory = player.inventory

                            for (i in 0 until inventory.size) {
                                if (inventory.getItem(i) == null || inventory.getItem(i)!!.type == Material.AIR) {
                                    inventory.setItem(i, giveItem)
                                    break
                                }
                            }
                            event.clickedInventory?.close()
                        }
                    }
                }
                if (config.isConfigurationSection("stores.st.$customItemString")) {
                    val playerXpST = DatabaseUtils().getPlayerSTxp(player)
                    val (currentLevelST, _) = LevelCalculate().calculateLevel(playerXpST)
                    val sectionST = config.getConfigurationSection("stores.st.$customItemString")
                    val reqLevelST = sectionST?.getInt("level") ?: return
                    if (currentLevelST >= reqLevelST) {

                        val coins = sectionST.getInt("coins")
                        val silver = sectionST.getInt("silver")

                        val playerCoins = DatabaseUtils().getPlayerCoins(player)
                        val playerSilver = DatabaseUtils().getPlayerSilver(player)

                        if (playerCoins < coins && playerSilver < silver) return

                        DatabaseUtils().updatePlayerCoins(player, playerCoins - coins)
                        DatabaseUtils().updatePlayerSilver(player, playerSilver - silver)


                        val inventory = player.inventory

                        for (i in 0 until inventory.size) {
                            if (inventory.getItem(i) == null || inventory.getItem(i)!!.type == Material.AIR) {
                                inventory.setItem(i, giveItem)
                                break
                            }
                        }
                        event.clickedInventory?.close()
                    }
                }
                if (config.isConfigurationSection("stores.wd.$customItemString")) {
                    val playerXpWD = DatabaseUtils().getPlayerSMxp(player)
                    val (currentLevelWD, _) = LevelCalculate().calculateLevel(playerXpWD)
                    val sectionWD = config.getConfigurationSection("stores.wd.$customItemString")
                    val reqLevelWD = sectionWD?.getInt("level") ?: return
                    if (currentLevelWD >= reqLevelWD) {

                        val coins = sectionWD.getInt("coins")
                        val silver = sectionWD.getInt("silver")

                        val playerCoins = DatabaseUtils().getPlayerCoins(player)
                        val playerSilver = DatabaseUtils().getPlayerSilver(player)

                        if (playerCoins < coins && playerSilver < silver) return

                        DatabaseUtils().updatePlayerCoins(player, playerCoins - coins)
                        DatabaseUtils().updatePlayerSilver(player, playerSilver - silver)

                        val inventory = player.inventory

                        for (i in 0 until inventory.size) {
                            if (inventory.getItem(i) == null || inventory.getItem(i)!!.type == Material.AIR) {
                                inventory.setItem(i, giveItem)
                                break
                            }
                        }
                        event.clickedInventory?.close()
                    }
                }
                event.isCancelled = true
            }
        }
    }
}

