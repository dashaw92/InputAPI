package me.danny.libinput.providers.conversation

import me.danny.libinput.InputAPI
import me.danny.libinput.OutputCallback
import me.danny.libinput.color
import org.bukkit.conversations.*
import org.bukkit.entity.Player
import org.jetbrains.annotations.NotNull


object ConversationStarter {
    fun getForPlayer(
        player: Player,
        promptText: String,
        prefix: String,
        callback: OutputCallback,
        infoMessage: List<String>,
        vararg escapeWords: String
    ): Conversation {
        val factory = ConversationFactory(InputAPI.instance())
            .withFirstPrompt(MultilineMessage(StringPrompt(promptText, callback, escapeWords), *infoMessage.toTypedArray()))
            .withLocalEcho(false)
            .withModality(false)
            .withPrefix { prefix.color() }
            .withConversationCanceller(ServerReloadCanceller())
        return factory.buildConversation(player)
    }
}

class StringPrompt(
    private val prompt: String,
    private val callback: OutputCallback,
    private val escapeWords: Array<out String>
) : Prompt {

    override fun getPromptText(context: ConversationContext): String = prompt
    override fun blocksForInput(context: ConversationContext): Boolean = true

    override fun acceptInput(context: ConversationContext, input: String?): Prompt? {
        val message = if(input in escapeWords) ""
        else input ?: ""

        callback(context.forWhom as Player, message)
        return Prompt.END_OF_CONVERSATION
    }

}

class MultilineMessage(private val end: Prompt, vararg messages: String) : MessagePrompt() {
    private val messages: Array<out String>

    init {
        check(messages.isNotEmpty()) { "Invalid multiline message" }
        this.messages = messages
    }

    @NotNull
    override fun getPromptText(@NotNull context: ConversationContext): String {
        return messages[0].color()
    }

    override fun getNextPrompt(@NotNull context: ConversationContext): Prompt {
        return if (messages.size == 1) end else MultilineMessage(end, *messages.copyOfRange(1, messages.size))
    }
}