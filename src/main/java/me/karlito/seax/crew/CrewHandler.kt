package me.karlito.seax.crew

import me.karlito.seax.SeaX
import me.karlito.seax.SeaX.Companion.crewMap
import me.karlito.seax.SeaX.Companion.inviteMap
import me.karlito.seax.crew.scoreboard.ScoreBoardHandler
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.*

class CrewHandler {
    val crewId: MutableMap<String, UUID> = mutableMapOf()

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
        println("THE CREW ID ${this.crewId[player.name]}")
        println("THE CREWS $crewMap")

        ScoreBoardHandler().updateAllMemberScoreBoard(members)

        return crewId
    }

    fun getMembers(player: Player): List<String>? {
        val members = crewMap[this.crewId[player.name]]
        //player.sendMessage("From getMembers $members")
        return members
    }



    fun guiRequest(invited: Player, inviter: Player) {
        val inviterName = inviter.name
        val requestInventory = Bukkit.createInventory(
            invited,
            27,
            Component.text("Invite from $inviterName").color(TextColor.color(0, 0, 0))
        )
        // Yes Item
        val yesItem = ItemStack(Material.GREEN_CONCRETE)
        val yesMeta = yesItem.itemMeta

        yesMeta.displayName(Component.text("${ChatColor.GREEN}${ChatColor.BOLD}Yes"))

        yesMeta.setCustomModelData(3565)
        yesItem.itemMeta = yesMeta

        // No Item
        val noItem = ItemStack(Material.RED_CONCRETE)
        val noMeta = noItem.itemMeta

        noMeta.displayName(Component.text("${ChatColor.RED}${ChatColor.BOLD}No"))



        noMeta.setCustomModelData(3566)
        noItem.itemMeta = noMeta

        requestInventory.setItem(15, noItem)
        requestInventory.setItem(11, yesItem)

        inviteMap[invited.name] = inviter.name
        SeaX.guiMap[invited.uniqueId] = requestInventory


        invited.openInventory(requestInventory)
    }

    fun testId(sender: Player) {
        println("TEST TEST ${sender.name}")
        println("TEST TEST ${crewId[sender.name]}")
    }

    fun addPlayer(sender: Player, target: Player){
        val senderName = sender.name
        val crewId = crewId[senderName]
        //val members = crewMap[this.crewId[sender.name]]

        println("Inviter: $senderName")
        println("Invited: ${target.name}")
        println("Inviter CrewId: $crewId")

        if (crewId != null) {
            val members = crewMap[crewId]

            if (members != null) {
                val size = members.size.minus(1)
                size.let { members.add(it, target.name) }
                sender.sendMessage("${ChatColor.BLUE}[Crew System]${ChatColor.GOLD} ${target.name} added!")
                ScoreBoardHandler().updateScoreBoard(sender, members)
                ScoreBoardHandler().updateAllMemberScoreBoard(members)
            }
        } else {
            sender.sendMessage("${ChatColor.BLUE}[Crew System]${ChatColor.GOLD} Unable to add player. Crew information not found.")
        }
    }
    fun removeCrew(sender: Player) {
        val members = crewMap[crewId[sender.name]]
        members?.clear()
        ScoreBoardHandler().updateAllMemberScoreBoard(members)
        crewMap.remove(crewId[sender.name])
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
                crewHandler.addPlayer(sender, target)
                //sender.sendMessage("${ChatColor.BLUE}[Crew System]${ChatColor.GOLD} Invite send to ${target.name}")
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

class InventoryClickListenerInvite : Listener {

    @EventHandler
    fun inventorClickEventInvite(event: InventoryClickEvent) {
        val player = event.whoClicked as Player
        val playerUUID = event.whoClicked.uniqueId
        //inviteMap[player.name] ?: return
        val inviter = inviteMap[player.name]!!
        println(inviter)


        if(event.inventory ==  SeaX.guiMap[playerUUID]) {
            if(event.currentItem == null) return
            if(event.currentItem?.itemMeta?.hasCustomModelData() == true) {
                when(event.currentItem?.itemMeta?.customModelData) {
                    3565 -> {
                        event.isCancelled = true
                        //CrewHandler().testId(inviter)
                        if(inviteMap.containsKey(player.name)) inviteMap.remove(player.name)
                        event.clickedInventory?.close()
                    }
                    3566 -> {
                        event.isCancelled = true
                        event.clickedInventory?.close()
                        if(inviteMap.containsKey(player.name)) inviteMap.remove(player.name)
                    }
                }
            }
            event.isCancelled = true
        }

    }
}



