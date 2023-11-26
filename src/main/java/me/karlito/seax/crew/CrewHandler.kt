package me.karlito.seax.crew

import me.karlito.seax.SeaX.Companion.crewMap
import me.karlito.seax.crew.scoreboard.ScoreBoardHandler
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class CrewHandler {
    private val crewId: MutableMap<String, UUID> = mutableMapOf()
    fun createCrew(player: Player): UUID? {
        crewMap.forEach { (_, members) ->
            if (members.contains(player.name)) {
                player.sendMessage("${ChatColor.BLUE}[Crew System]${ChatColor.GOLD} You are already in another crew")
                return null
            }
        }
        val crewId = UUID.randomUUID()
        val members = mutableListOf<String>()
        members.add(0, player.name)
        crewMap[crewId] = members
        this.crewId[player.name] = crewId

        ScoreBoardHandler().updateScoreBoard(player, members)

        return crewId
    }

    fun getMembers(player: Player): List<String>? {
        val members = crewMap[this.crewId[player.name]]
        player.sendMessage("From getMembers $members")
        return members
    }

    fun addPlayer(sender: Player, target: Player): MutableList<String>? {
        val size = crewMap[this.crewId[sender.name]]?.size?.minus(1)
        size?.let { crewMap[this.crewId[sender.name]]?.add(it, target.name) }
        return crewMap[this.crewId[sender.name]]
    }

    fun removeCrew(sender: Player) {
        crewMap[this.crewId[sender.name]]?.clear()
        crewMap.remove(this.crewId[sender.name])
    }
}

class CrewCommands : CommandExecutor {

    private val crewHandler = CrewHandler()

    //private val crewMembers =
    override fun onCommand(sender: CommandSender, p1: Command, p2: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false
        if (args.isEmpty()) {
            sender.sendMessage(
                """
                ${ChatColor.GOLD}Try this   
                /crew create
                /crew invite
                /crew delete
            """.trimIndent()
            )
            return false
        }

        val crewMembers = crewHandler.getMembers(sender)

            if (args.contains("invite")) {
                if (args.size != 2) return true
                val playerName = args.get(index = 1)
                val target = Bukkit.getPlayer(playerName)
                if (target == null) {
                    sender.sendMessage("${ChatColor.BLUE}[Crew System]${ChatColor.GOLD} Player not found")
                    return true
                }
                if (crewMembers?.contains(sender.name) == true) {
                    if (crewMembers.contains(target.name)) {
                        sender.sendMessage("${ChatColor.BLUE}[Crew System]${ChatColor.GOLD} ${target.name} is already in the crew")
                        return true
                    }

                    val members = crewHandler.addPlayer(sender, target)
                    sender.sendMessage("${ChatColor.BLUE}[Crew System]${ChatColor.GOLD} ${target.name} added!")
                    sender.sendMessage("${ChatColor.BLUE}[Crew System]${ChatColor.GOLD} Members: $members")
                    return true

                } else {
                    sender.sendMessage("$crewMembers MEMBERS")
                    sender.sendMessage("${ChatColor.BLUE}[Crew System]${ChatColor.GOLD} You are not in a crew, cannot invite")
                }


            }

        if (args.contains("create")) {
            if (crewMembers?.contains(sender.name) == true) {
                sender.sendMessage("${ChatColor.BLUE}[Crew System]${ChatColor.GOLD} You are in a group please delete it first")
                return false
            }

            val crewId = crewHandler.createCrew(sender) ?: return false

            sender.sendMessage("${ChatColor.BLUE}[Crew System]${ChatColor.GOLD} Crew has been created")
            sender.sendMessage("${ChatColor.BLUE}[Crew System]${ChatColor.GOLD} Crew ID: $crewId, Members: ${crewMap[crewId]}")
            return true
        }
        if (args.contains("delete")) {
            if (crewMembers?.contains(sender.name) == true) {
                sender.sendMessage("${ChatColor.BLUE}[Crew System]${ChatColor.GOLD} Crew has been deleted")
                crewHandler.removeCrew(sender)
            } else {
                sender.sendMessage("${ChatColor.BLUE}[Crew System]${ChatColor.GOLD} You are not in a crew")
            }
        }

        return true
    }
}



