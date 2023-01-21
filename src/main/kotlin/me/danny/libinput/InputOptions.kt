package me.danny.libinput

import me.danny.libinput.providers.ConversationProvider
import me.danny.libinput.providers.InputProvider
import me.danny.libinput.providers.KeyboardProvider
import me.danny.libinput.providers.SignProvider
import org.bukkit.Bukkit
import org.bukkit.entity.Player

data class InputOptions(val prompt: List<String>, val mode: InputMode) {
    //Java compatibility
    constructor(prompt: List<String>) : this(prompt, InputMode.defaultMode())

    fun provide(player: Player, callback: OutputCallback) {
        val mode = if(!mode.isAvailable()) InputMode.defaultMode()
        else mode

        mode.provide(player, prompt, callback).getInput()
    }
}

sealed interface InputMode {
    companion object {
        fun defaultMode(): InputMode {
            return if(Sign.isAvailable()) Sign
            else KeyboardMenu
        }
    }

    fun isAvailable(): Boolean = true
    fun provide(player: Player, prompt: List<String>, callback: OutputCallback): InputProvider {
        val ctor: (Player, List<String>, OutputCallback) -> InputProvider = when(this) {
            is Sign -> ::SignProvider
            is ChatConversation -> ::ConversationProvider
            is KeyboardMenu -> ::KeyboardProvider
        }

        return ctor(player, prompt, callback)
    }
}

object Sign : InputMode {
    override fun isAvailable(): Boolean = Bukkit.getServer().pluginManager.isPluginEnabled("ProtocolLib")
}

object ChatConversation : InputMode
object KeyboardMenu : InputMode