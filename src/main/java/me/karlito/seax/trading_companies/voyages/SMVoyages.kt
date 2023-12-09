package me.karlito.seax.trading_companies.voyages

import com.connorlinfoot.titleapi.TitleAPI
import me.karlito.seax.datastore.DatabaseUtils
import org.bukkit.entity.Player

class SMVoyages {

    fun voyageEvent1(player: Player) { // Test Voyage 1 Level 5 Cost 300 coins

        val playerCoins = DatabaseUtils().getPlayerCoins(player)

        if (playerCoins > 300) {
            val finalCost = playerCoins - 300
            DatabaseUtils().updatePlayerCoins(player, finalCost)
            val title = "§6Voyage Started"
            val subtitle = "placeholder text"


            TitleAPI.sendTitle(player,20,50,20,title,subtitle);
        }


    }

}


