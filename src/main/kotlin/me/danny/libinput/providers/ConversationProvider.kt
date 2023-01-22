package me.danny.libinput.providers

import me.danny.libinput.OutputCallback
import me.danny.libinput.providers.conversation.ConversationStarter
import org.bukkit.entity.Player

class ConversationProvider : InputProvider {
    private var promptText = "Please provide input:"
    private var prefix = "&6[InputAPI] &e"
    private val escapeWords: MutableList<String> = mutableListOf()
    private var infoMessage: List<String> = listOf(
        "&7Your next message will be captured.",
        "&7Please type your input in chat."
    )

    fun withPrompt(prompt: String) = apply { promptText = prompt }
    fun withPrefix(prefix: String) = apply { this.prefix = prefix }
    fun withEscapeWords(vararg words: String) = apply { words.forEach(escapeWords::add) }
    fun withInfoMessage(message: List<String>) = apply { infoMessage = message }

    override fun getInput(player: Player, callback: OutputCallback) {
        ConversationStarter.getForPlayer(player, promptText, prefix, callback, infoMessage, *escapeWords.toTypedArray())
            .begin()
    }

}
