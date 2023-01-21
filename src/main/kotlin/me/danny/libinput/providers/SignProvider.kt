package me.danny.libinput.providers

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.wrappers.BlockPosition
import me.danny.libinput.InputAPI
import me.danny.libinput.OutputCallback
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

/*
Inspired by the public resource on the spigot forums:
https://www.spigotmc.org/threads/signmenu-1-16-5-get-player-sign-input.249381/

Adapted to Kotlin and updated to 1.19 by Danny
 */
class SignProvider(player: Player, prompt: List<String>, callback: OutputCallback) : InputProvider(player, prompt, callback) {

    companion object {
        private val inEditor: MutableMap<UUID, SignMenu> = mutableMapOf()

        init {
            ProtocolLibrary.getProtocolManager().addPacketListener(object : PacketAdapter(InputAPI.instance(), PacketType.Play.Client.UPDATE_SIGN) {
                override fun onPacketReceiving(event: PacketEvent) {
                    val player = event.player
                    val menu = inEditor[player.uniqueId] ?: return
                    event.isCancelled = true

                    val input = event.packet.stringArrays.read(0)[0]
                    Bukkit.getScheduler().runTaskLater(InputAPI.instance(), Runnable {
                        player.sendBlockChange(menu.loc, menu.loc.block.blockData)
                    }, 1)

                    inEditor.remove(player.uniqueId)
                    menu.callback(player, input)
                }
            })
        }
    }

    override fun getInput() {
        val menu = SignMenu(player.uniqueId, callback)
        menu.openSign(prompt)
        inEditor += player.uniqueId to menu
    }

    private class SignMenu(val uuid: UUID, val callback: OutputCallback) {

        lateinit var loc: Location

        fun openSign(prompt: List<String>) {
            val player = Bukkit.getPlayer(uuid) ?: return

            loc = player.location
            val oppositeY = if(loc.blockY < player.world.maxHeight / 2) player.world.maxHeight - 1
            else player.world.minHeight + 1
            val pos = BlockPosition(loc.blockX, oppositeY, loc.blockZ)

            val lines = padLinesToFour(prompt)

            player.sendBlockChange(pos.toLocation(player.world), Material.OAK_SIGN.createBlockData())
            player.sendSignChange(pos.toLocation(player.world), lines)

            val openSign = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR)
            openSign.blockPositionModifier.write(0, pos)
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, openSign)
        }

        private fun padLinesToFour(prompt: List<String>): Array<String> {
            return if(prompt.isEmpty()) arrayOf("", "", "", "")
            else if(prompt.size > 4) prompt.subList(0, 4).toTypedArray()
            else if(prompt.size < 4) {
                val mutPrompt = prompt.toMutableList()
                while(mutPrompt.size < 4) mutPrompt += ""
                mutPrompt.toTypedArray()
            } else prompt.toTypedArray()
        }
    }
}