package me.danny.libinput.providers

import com.comphenix.protocol.*
import com.comphenix.protocol.events.*
import com.comphenix.protocol.wrappers.*
import me.danny.libinput.*
import org.bukkit.*
import org.bukkit.entity.*
import java.util.*

/*
Inspired by the public resource on the spigot forums:
https://www.spigotmc.org/threads/signmenu-1-16-5-get-player-sign-input.249381/

Adapted to Kotlin and updated to 1.19 by Danny
 */
class SignInput : InputProvider {

    companion object {
        fun isAvailable(): Boolean = Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")

        private val inEditor: MutableMap<UUID, SignMenu> = mutableMapOf()

        init {
            if (isAvailable()) {
                ProtocolLibrary.getProtocolManager()
                    .addPacketListener(object : PacketAdapter(InputAPI.instance(), PacketType.Play.Client.UPDATE_SIGN) {
                        override fun onPacketReceiving(event: PacketEvent) {
                            val player = event.player
                            val menu = inEditor.remove(player.uniqueId) ?: return
                            event.isCancelled = true

                            val input = event.packet.stringArrays.optionRead(0)
                                .orElse(arrayOf("", "", "", ""))[menu.promptIndex]

                            Bukkit.getScheduler().runTask(InputAPI.instance(), Runnable {
                                player.sendBlockChange(menu.loc, menu.loc.block.blockData)
                                menu.callback.accept(player, SingleLine(input))
                            })
                        }
                    })
            }
        }
    }

    private var signMaterial: Material = Material.OAK_WALL_SIGN
    private var lines: Array<String> = arrayOf("", "^^^^^", "Please type input", "on the first line")
    private var promptIndex: Int = 0

    fun withMaterial(sign: Material): SignInput {
        if (!Tag.SIGNS.isTagged(sign)) throw IllegalArgumentException("Cannot use a non-sign material!")
        signMaterial = sign
        return this
    }

    fun withLines(lines: Array<String>): SignInput {
        val corrected = if (lines.size > 4) lines.copyOfRange(0, 4)
        else if (lines.size < 4) {
            val mutList = lines.toMutableList()
            while (mutList.size != 4) mutList += ""
            mutList.toTypedArray()
        } else lines

        this.lines = corrected
        return this
    }

    fun withLine(index: Int, line: String): SignInput {
        if (!(0 until 4).contains(index)) throw IllegalArgumentException("Line index must be 0, 1, 2, or 3.")
        lines[index] = line
        return this
    }

    fun withPromptAtLine(index: Int): SignInput {
        if (!(0 until 4).contains(index)) throw IllegalArgumentException("Line index must be 0, 1, 2, or 3.")
        promptIndex = index
        return this
    }

    override fun getInput(player: Player, callback: OutputCallback) {
        val menu = SignMenu(player.uniqueId, callback, signMaterial, lines, promptIndex)
        menu.openSign()
        inEditor += player.uniqueId to menu
    }

    private class SignMenu(
        val uuid: UUID,
        val callback: OutputCallback,
        val material: Material,
        val lines: Array<String>,
        val promptIndex: Int
    ) {

        lateinit var loc: Location

        fun openSign() {
            val player = Bukkit.getPlayer(uuid) ?: return

            loc = player.location
            val oppositeY = if (loc.blockY < player.world.maxHeight / 2) player.world.maxHeight - 1
            else player.world.minHeight + 1
            val pos = BlockPosition(loc.blockX, oppositeY, loc.blockZ)

            player.sendBlockChange(pos.toLocation(player.world), material.createBlockData())
            player.sendSignChange(pos.toLocation(player.world), lines)

            val openSign = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR)
            openSign.blockPositionModifier.write(0, pos)
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, openSign)
        }
    }
}