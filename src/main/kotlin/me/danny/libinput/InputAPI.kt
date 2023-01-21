package me.danny.libinput

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

typealias OutputCallback = (Player, String?) -> Unit

class InputAPI : JavaPlugin() {

    companion object {
        fun getInput(player: Player, options: InputOptions, callback: OutputCallback) {
            options.provide(player, callback)
        }

        fun instance(): InputAPI = getPlugin(InputAPI::class.java)
    }

    override fun onEnable() {

    }
}