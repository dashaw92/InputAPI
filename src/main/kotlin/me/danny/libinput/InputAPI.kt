package me.danny.libinput

import me.danny.libinput.providers.SignProvider
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.plugin.java.JavaPlugin

typealias OutputCallback = (Player, String?) -> Unit

class InputAPI : JavaPlugin(), Listener {

    companion object {
        fun instance(): InputAPI = getPlugin(InputAPI::class.java)
    }

    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this)
    }

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        SignProvider()
            .withLines(arrayOf("", "^^^^^", "[DannyShop]", "Search"))
            .withPromptAtLine(0)
            .withMaterial(Material.ACACIA_WALL_SIGN)
            .getInput(event.player, ::handleInput)
    }

    private fun handleInput(player: Player, input: String? = null) {
        player.msg("&eYou sent: \"&d$input&e\"")
    }
}