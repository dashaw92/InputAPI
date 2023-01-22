package me.danny.libinput.providers

import me.danny.libinput.OutputCallback
import org.bukkit.entity.Player

sealed interface InputProvider {
    fun getInput(player: Player, callback: OutputCallback)
}