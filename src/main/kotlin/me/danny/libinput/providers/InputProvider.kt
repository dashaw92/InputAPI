package me.danny.libinput.providers

import me.danny.libinput.*
import org.bukkit.entity.*

sealed interface InputProvider {
    fun getInput(player: Player, callback: OutputCallback)
}