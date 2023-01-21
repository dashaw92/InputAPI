package me.danny.libinput

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.plugin.java.JavaPlugin

typealias OutputCallback = (Player, String?) -> Unit

class InputAPI : JavaPlugin(), Listener {

    companion object {
        fun getInput(player: Player, options: InputOptions, callback: OutputCallback) {
            options.provide(player, callback)
        }

        fun instance(): InputAPI = getPlugin(InputAPI::class.java)
    }

    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this)
    }

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        handleInput(event.player)
    }

    private fun handleInput(player: Player, input: String? = null) {
        if(input.isNullOrBlank()) {
            getInput(player, InputOptions(listOf("", "^^^^^", "DannyShop", "Search")), ::handleInput)
            return
        }
        player.msg("&eYou sent: &d$input")
    }
}