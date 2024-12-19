package me.danny.libinput

import me.danny.libinput.providers.Input
import me.danny.libinput.providers.conversation.ServerReloadListener
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.util.function.BiConsumer

typealias OutputCallback = BiConsumer<Player, Input>

class InputAPI : JavaPlugin(), Listener {

    companion object {
        fun instance(): InputAPI = getPlugin(InputAPI::class.java)
    }

    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(ServerReloadListener(), this)
        Bukkit.getPluginManager().registerEvents(this, this)

//        getCommand("testinput")!!.setExecutor(this)
    }

//    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
//        if (label != "testinput") return true;
//        if (sender !is Player) return true;
//
//        SignInput()
//            .withLines(arrayOf("", "^^", "test", "lol"))
//            .withMaterial(Material.PALE_OAK_SIGN)
//            .getInput(sender) { pl, it ->
//                pl.sendMessage("You sent $it")
//            }
//        return true;
//    }
}