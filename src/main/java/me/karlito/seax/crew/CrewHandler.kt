package me.karlito.seax.crew

import me.karlito.seax.SeaX.Companion.crewMap
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class CrewHandler {
    private val members = mutableListOf<String>()
    private var crewId: UUID? = null
    fun createCrew(player: Player): UUID {
        val crewId = UUID.randomUUID()
        members.add(0, player.name)
        crewMap[crewId] = members
        this.crewId = crewId
        return crewId
    }

    fun getMembers(): MutableList<String> {
        return members
    }
    fun addPlayer(player: Player): MutableList<String> {
        val size = members.size - 1
        members.add(size, player.name)
        return members
    }

    fun removeCrew() {
        members.clear()
        crewMap.remove(this.crewId)
    }
}

class CrewCommands : CommandExecutor {

    private val crewHandler = CrewHandler()
    private val crewMembers = crewHandler.getMembers()
    override fun onCommand(sender: CommandSender, p1: Command, p2: String, args: Array<out String>): Boolean {
        if (sender is Player) {

            if (args.size == 2) {
                if (args.contains("invite")) {
                    val playerName = args.get(index = 1)
                    val target = Bukkit.getPlayer(playerName)
                    if (target == null) {
                        sender.sendMessage("Player Not Found")
                        return true
                    }
                    if (crewMembers.contains(target.name)) {
                        sender.sendMessage("${target.name} is already in this crew")
                        return false
                    }


                    val members = crewHandler.addPlayer(target)
                    sender.sendMessage("Player Found")
                    sender.sendMessage("Added! Members: $members")
                    return true
                }
            }

            if (args.size == 1) {
                if (args.contains("create")) {
                    if (crewMembers.contains(sender.name)) {
                        sender.sendMessage("You are in a crew please delete it first")
                        return false
                    }
                    sender.sendMessage("${ChatColor.BLUE}[Crew System]${ChatColor.GOLD} Crew has been created")
                    val crewId = crewHandler.createCrew(sender)
                    sender.sendMessage("Crew created! Crew ID: $crewId, Members: ${crewMap[crewId]}")
                    return true
                }
                if (args.contains("delete")) {
                    if (crewMembers.contains(sender.name)) {
                    sender.sendMessage("${ChatColor.BLUE}[Crew System]${ChatColor.GOLD} Crew has been deleted")
                    crewHandler.removeCrew()
                    return true
                    }
                }

            } else {
                sender.sendMessage(
                    """
                        ${ChatColor.GOLD}Try this   
                        /crew create
                        /crew invite
                        /crew delete
                    """.trimIndent()
                )
            }

        }
        return true
    }
}



