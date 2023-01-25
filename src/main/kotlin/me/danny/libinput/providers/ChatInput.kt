package me.danny.libinput.providers

import me.danny.libinput.OutputCallback
import me.danny.libinput.providers.conversation.ConversationStarter
import org.bukkit.entity.Player

class ChatInput : InputProvider {
    private var promptText = "Please provide input:"
    private var prefix = "&6[InputAPI] &e"
    private var numberOfLines = 1
    private val escapeWords: MutableList<String> = mutableListOf()
    private var infoMessage: List<String> = listOf(
        "&7Your next message will be captured.",
        "&7Please type your input in chat."
    )

    fun requestLines(numberOfLines: Int): ChatInput {
        if(numberOfLines > 0) this.numberOfLines = numberOfLines
        return this
    }

    fun withPrompt(prompt: String) = apply { promptText = prompt }
    fun withPrefix(prefix: String) = apply { this.prefix = prefix }
    fun withEscapeWords(vararg words: String) = apply { words.forEach(escapeWords::add) }
    fun withInfoMessage(message: List<String>) = apply { infoMessage = message }

    override fun getInput(player: Player, callback: OutputCallback) {
        ConversationStarter.getForPlayer(player, promptText, prefix, callback, numberOfLines, infoMessage, *escapeWords.toTypedArray())
            .begin()
    }

}
