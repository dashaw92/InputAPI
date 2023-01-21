package me.danny.libinput.providers

import me.danny.libinput.OutputCallback
import org.bukkit.entity.Player

class KeyboardProvider(player: Player, prompt: List<String>, callback: OutputCallback) : InputProvider(player, prompt, callback) {
    override fun getInput() {

    }

}
