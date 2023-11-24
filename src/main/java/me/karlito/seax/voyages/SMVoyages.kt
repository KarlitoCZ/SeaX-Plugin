package me.karlito.seax.voyages

import com.connorlinfoot.titleapi.TitleAPI
import org.bukkit.entity.Player

class SMVoyages {

    fun voyageEvent1(player: Player) {
        val title = "hello"
        val subtitle = "hi"


        TitleAPI.sendTitle(player,20,100,20,title,subtitle);
    }

}


