package me.danny.libinput.providers

import me.danny.libinput.OutputCallback
import org.bukkit.entity.Player

sealed class InputProvider(val player: Player, val prompt: List<String>, val callback: OutputCallback) {
    abstract fun getInput()
}