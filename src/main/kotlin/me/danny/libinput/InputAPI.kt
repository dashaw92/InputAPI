package me.danny.libinput

import me.danny.libinput.providers.conversation.ServerReloadListener
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

typealias OutputCallback = (Player, String?) -> Unit

class InputAPI : JavaPlugin(), Listener {

    companion object {
        fun instance(): InputAPI = getPlugin(InputAPI::class.java)
    }

    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(ServerReloadListener(), this)
    }

//    @EventHandler
//    fun onChat(event: AsyncPlayerChatEvent) {
//        val provider = when(event.message.lowercase()) {
//            "sign" -> SignProvider()
//                .withLines(arrayOf("", "^^^^^", "[DannyShop]", "Search"))
//                .withPromptAtLine(0)
//                .withMaterial(Material.ACACIA_WALL_SIGN)
//
//            "chat" -> {
//                ConversationProvider()
//                    .withPrefix("&4&l[DannyShop]&9 ")
//                    .withPrompt("Enter item to search for:")
//                    .withEscapeWords("cancel")
//            }
//
//            else -> return
//        }
//
//        event.isCancelled = true
//        provider.getInput(event.player, ::handleInput)
//    }
//
//    private fun handleInput(player: Player, input: String? = null) {
//        player.msg("&eYou sent: \"&d$input&e\"")
//    }
}