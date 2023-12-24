package me.karlito.seax.trading_companies.selling

import io.lumine.mythic.bukkit.MythicBukkit
import me.karlito.seax.SeaX.Companion.attachedEntities
import me.karlito.seax.crew.CrewHandler
import me.karlito.seax.datastore.DatabaseUtils
import me.karlito.seax.gui.Guis
import me.karlito.seax.itemsystem.ItemHoldHandler
import net.citizensnpcs.util.NMS
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent

class SellSystem {

    private val itemHoldHandler = ItemHoldHandler()
    fun sellLoot(player: Player) {

        val members = CrewHandler().getMembers(player)
        val entity = attachedEntities[player.name]

        if (members != null) {
            for (member in members) {
                val memberPl = Bukkit.getPlayer(member)!!
                    sellPlayerLoot(entity, memberPl)
            }

        } else {
            sellPlayerLoot(entity, player)
        }
    }

    fun sellPlayerLoot(entity: Entity?, player: Player) {
        if (entity != null) {
            val plugin = Bukkit.getPluginManager().getPlugin("SeaX")
            val config : FileConfiguration = plugin!!.config
            val mythicMob = MythicBukkit.inst().mobManager.getActiveMob(entity.uniqueId).orElse(null)
            itemHoldHandler.stopTask(player)
            entity.remove()
            val coins = DatabaseUtils().getPlayerCoins(player)
            val silver = DatabaseUtils().getPlayerSilver(player)
            val xp = config.get("loot-table.${mythicMob.mobType}.xp") as Int
            val coinsValue = config.get("loot-table.${mythicMob.mobType}.coins") as Int
            val silverValue = config.get("loot-table.${mythicMob.mobType}.silver") as Int
            val com = config.get("loot-table.${mythicMob.mobType}.company").toString()
            DatabaseUtils().updatePlayerCoins(player, coins + coinsValue)
            DatabaseUtils().updatePlayerSilver(player, silver + silverValue)
            var prefix : String? = null
            when (com) {
                "sm" -> {
                    prefix = "${ChatColor.DARK_RED}[Skull Merchants]"
                    val currentXP = DatabaseUtils().getPlayerSMxp(player)
                    DatabaseUtils().updatePlayerSMxp(player, xp + currentXP)
                }

                "st" -> {
                    prefix = "${ChatColor.BLUE}[Soul Traders]"
                    val currentXP = DatabaseUtils().getPlayerSTxp(player)
                    DatabaseUtils().updatePlayerSTxp(player, xp + currentXP)
                }

                "wd" -> {
                    prefix = "${ChatColor.DARK_PURPLE}[Whispering dealers]"
                    val currentXP = DatabaseUtils().getPlayerWDxp(player)
                    DatabaseUtils().updatePlayerWDxp(player, xp + currentXP)
                }
            }
            attachedEntities.remove(player.name)
            val lootName = mythicMob.mobType.replace("-", " ")
            if (silverValue < 1) {
                player.sendMessage("$prefix${ChatColor.RESET}${ChatColor.AQUA} Sold $lootName for\n${ChatColor.GOLD}- ⛃ $coinsValue Coins")
            } else {
                player.sendMessage("$prefix${ChatColor.RESET}${ChatColor.AQUA} Sold $lootName for\n${ChatColor.GOLD}- ⛃ $coinsValue Coins\n${ChatColor.GRAY}- ⛃ $silverValue Silver")
            }
        }

    }
}

class NpcInteract : Listener {
    @EventHandler
    fun onInteractEntity(event: PlayerInteractAtEntityEvent) {
        val entity = event.rightClicked
        val player = event.player
        val loot = attachedEntities[player.name]
        val playerName = event.player.name
        val sm = "SkullMerchants"
        val st = "SoulTraders"
        val wd = "WhisperingDealers"

        if (entity.hasMetadata("NPC")) {
            val npc = NMS.getNPC(entity)
            if (loot == null) {
                Guis().openSMgui(player)
            }
            when (npc.name) {
                "Skull Merchants" -> {
                    SellSystem().sellLoot(player)
                }
                "Whispering Dealers" -> {
                    SellSystem().sellLoot(player)
                }
                "Soul Traders" -> {
                    SellSystem().sellLoot(player)
                }
            }
        }
    }
}


