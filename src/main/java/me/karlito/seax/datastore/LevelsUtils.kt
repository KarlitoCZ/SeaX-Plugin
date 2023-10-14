package me.karlito.seax.datastore

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.bson.Document
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class LevelsUtils : JavaPlugin() {

    private val mongoClient = MongoClient.create("mongodb+srv://<BOT>:<password!123>@seaxdb.99qs2ww.mongodb.net/?retryWrites=true&w=majority")
    private val database = mongoClient.getDatabase("player_data")
    fun getCollection(): MongoCollection<Document> {
        val collection = database.getCollection<Document>("data")
        return collection
    }
    suspend fun createPlayerDocument(p : Player) {
        val playerdoc : Document = Document("uuid", p.uniqueId.toString())
            .append("Coins", 10000)
            .append("Dabloons", 0)
            .append("Silver-Coins", 0)
            .append("Join-Date", Date())
        //SeaX().getC.insertOne(playerdoc)
        System.out.println("Player Document Created")
    }
}