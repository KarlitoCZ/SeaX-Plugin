package me.karlito.seax.trading_companies.selling

import io.lumine.mythic.bukkit.MythicBukkit
import me.karlito.seax.SeaX.Companion.attachedEntities
import me.karlito.seax.crew.CrewHandler
import me.karlito.seax.datastore.DatabaseUtils
import me.karlito.seax.itemsystem.ItemHoldHandler
import net.citizensnpcs.util.NMS
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent

class SellSystem {

    companion object {
        val lootMap = mutableMapOf(
            // coins, silver, xp
            "Sunken-Chest" to Triple(500, 10, 30),
            "Pirate-King-Fortune" to Triple(1000, 0, 50),

            )
    }


    private val itemHoldHandler = ItemHoldHandler()
    fun sellLoot(player: Player, com: String) {

        val members = CrewHandler().getMembers(player)
        val entity = attachedEntities[player.name]

        if (members != null) {
            for (member in members) {
                val memberPl = Bukkit.getPlayer(member)!!
                    sellPlayerLoot(entity, memberPl, com)
            }

        } else {
            sellPlayerLoot(entity, player, com)
        }
    }

    fun sellPlayerLoot(entity: Entity?, player: Player, com: String) {
        if (entity != null) {
            val mythicMob = MythicBukkit.inst().mobManager.getActiveMob(entity.uniqueId).orElse(null)
            itemHoldHandler.stopTask(player)
            entity.remove()
            val coins = DatabaseUtils().getPlayerCoins(player)
            val silver = DatabaseUtils().getPlayerSilver(player)
            val xp = lootMap[mythicMob.mobType]?.third
            val coinsValue = lootMap[mythicMob.mobType]?.first
            val silverValue = lootMap[mythicMob.mobType]?.second
            DatabaseUtils().updatePlayerCoins(player, coins + coinsValue!!)
            DatabaseUtils().updatePlayerSilver(player, silver + silverValue!!)
            var prefix : String? = null
            if (xp != null) {
                when (com) {
                    "SkullMerchants" -> {
                        prefix = "${ChatColor.DARK_RED}${ChatColor.BOLD}[Skull Merchants]"
                        val currentXP = DatabaseUtils().getPlayerSMxp(player)
                        DatabaseUtils().updatePlayerSMxp(player, xp + currentXP)
                    }

                    "SoulTraders" -> {
                        prefix = "${ChatColor.BLUE}${ChatColor.BOLD}[Soul Traders]"
                        val currentXP = DatabaseUtils().getPlayerSTxp(player)
                        DatabaseUtils().updatePlayerSTxp(player, xp + currentXP)
                    }

                    "WhisperingDealers" -> {
                        prefix = "${ChatColor.DARK_PURPLE}${ChatColor.BOLD}[Whispering dealers]"
                        val currentXP = DatabaseUtils().getPlayerWDxp(player)
                        DatabaseUtils().updatePlayerWDxp(player, xp + currentXP)
                    }
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
        val playerName = event.player.name
        val sm = "SkullMerchants"
        val st = "SoulTraders"
        val wd = "WhisperingDealers"

        if (entity.hasMetadata("NPC")) {
            val npc = NMS.getNPC(entity)
            when (npc.name) {
                "Skull Merchants" -> {
                    SellSystem().sellLoot(player, sm)
                }
            }
        }
    }
}


