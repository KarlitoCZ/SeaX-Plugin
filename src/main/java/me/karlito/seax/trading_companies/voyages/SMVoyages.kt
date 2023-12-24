package me.karlito.seax.trading_companies.voyages

import com.connorlinfoot.titleapi.TitleAPI
import me.karlito.seax.SeaX.Companion.playerActiveVoyage
import me.karlito.seax.SeaX.Companion.playerStageVoyage
import me.karlito.seax.crew.CrewHandler
import me.karlito.seax.datastore.DatabaseUtils
import me.karlito.seax.islands.IslandHandler
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class SMVoyages {

    fun smVoyageEvent1(player: Player) { // Get the lost treasure

        val playerCoins = DatabaseUtils().getPlayerCoins(player)

        if (playerCoins > 300) {
            val crewHandler = CrewHandler()
            val members = crewHandler.getMembers(player)
            if (members != null) {
                // TO DO
            } else {
                if (playerActiveVoyage[player.uniqueId] == null) {
                    playerActiveVoyage[player.uniqueId] = true
                    playerStageVoyage[player.uniqueId] = 1
                    val finalCost = playerCoins - 300
                    DatabaseUtils().updatePlayerCoins(player, finalCost)
                    val title = "§6Voyage Started"
                    val subtitle = "Follow The Instructions In Chat"
                    TitleAPI.sendTitle(player,20,50,20,title,subtitle)

                    val island = IslandHandler().pickRandomIsland()

                    player.sendMessage("${ChatColor.DARK_RED}[Skull Merchants]${ChatColor.RESET} Go to ${ChatColor.BLUE}$island${ChatColor.RESET} and find the treasure,\nBe careful It is guarded by skeletons!")


                } else {
                    player.sendMessage("${ChatColor.RED}You are on a voyage right now!")
                }
            }
        } else {
            player.sendMessage("${ChatColor.RED}Not enough coins!")
        }


    }

    fun voyageCancel(player: Player) {

    }

}


