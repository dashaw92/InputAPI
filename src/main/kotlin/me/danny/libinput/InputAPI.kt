package me.danny.libinput

import me.danny.libinput.providers.*
import me.danny.libinput.providers.conversation.*
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.*
import org.bukkit.event.player.*
import org.bukkit.plugin.java.*

typealias OutputCallback = (Player, Input) -> Unit

class InputAPI : JavaPlugin(), Listener {

    companion object {
        fun instance(): InputAPI = getPlugin(InputAPI::class.java)
    }

    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(ServerReloadListener(), this)
        Bukkit.getPluginManager().registerEvents(this, this)
    }

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        val provider = when (event.message.lowercase()) {
            "sign" -> SignInput()
                .withLines(arrayOf("", "^^^^^", "[DannyShop]", "Search"))
                .withPromptAtLine(0)
                .withMaterial(Material.ACACIA_WALL_SIGN)

            "chat" -> {
                ChatInput()
                    .requestLines(5)
                    .withPrefix("&4&l[DannyShop]&e ")
                    .withPrompt("&6Enter item to search for:".color())
                    .withEscapeWords("cancel")
            }

            else -> return
        }

        event.isCancelled = true
        provider.getInput(event.player, ::handleInput)
    }

    private fun handleInput(player: Player, input: Input) {
        when (input) {
            is SingleLine -> player.msg("&eYou sent: \"&d${input.line}&e\"")
            is MultipleLines -> {
                player.msg("&eWow! You sent a lot of messages!")
                input.lines.forEachIndexed { idx, it ->
                    player.msg("&e${idx + 1}. \"&d$it&e\"")
                }
                player.msg("&eThat's all of them!")
            }
        }

    }
}