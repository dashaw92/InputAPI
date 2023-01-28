package me.danny.libinput

import me.danny.libinput.providers.*
import me.danny.libinput.providers.conversation.*
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.*
import org.bukkit.plugin.java.*
import java.util.function.*

typealias OutputCallback = BiConsumer<Player, Input>

class InputAPI : JavaPlugin(), Listener {

    companion object {
        fun instance(): InputAPI = getPlugin(InputAPI::class.java)
    }

    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(ServerReloadListener(), this)
        Bukkit.getPluginManager().registerEvents(this, this)
    }
}