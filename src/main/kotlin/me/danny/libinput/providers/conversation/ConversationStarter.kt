package me.danny.libinput.providers.conversation

import me.danny.libinput.*
import me.danny.libinput.providers.*
import org.bukkit.conversations.*
import org.bukkit.entity.*
import org.jetbrains.annotations.*


object ConversationStarter {
    fun getForPlayer(
        player: Player,
        promptText: String,
        prefix: String,
        callback: OutputCallback,
        numberOfLines: Int,
        infoMessage: List<String>,
        vararg escapeWords: String
    ): Conversation {
        val factory = ConversationFactory(InputAPI.instance())
            .withFirstPrompt(
                MultilineMessage(
                    StringPrompt(promptText, callback, numberOfLines, escapeWords),
                    *infoMessage.toTypedArray()
                )
            )
            .withLocalEcho(false)
            .withModality(false)
            .withPrefix { prefix.color() }
            .withConversationCanceller(ServerReloadCanceller())
        val convo = factory.buildConversation(player)
        convo.context.setSessionData("input", mutableListOf<String>())
        return convo
    }
}

private inline fun <reified T> ConversationContext.sessionData(key: String): T = getSessionData(key)!! as T

class StringPrompt(
    private val prompt: String,
    private val callback: OutputCallback,
    private val numberOfLines: Int,
    private val escapeWords: Array<out String>
) : Prompt {

    override fun getPromptText(context: ConversationContext): String {
        return if (numberOfLines < 2) prompt
        else {
            val current = context.sessionData<MutableList<String>>("input").size + 1
            "&7($current/$numberOfLines) $prompt".color()
        }
    }

    override fun blocksForInput(context: ConversationContext): Boolean = true

    override fun acceptInput(context: ConversationContext, input: String?): Prompt? {
        val message = if (input in escapeWords) ""
        else input ?: ""

        val messages = context.sessionData<MutableList<String>>("input")
        messages += message

        if (messages.size >= numberOfLines) {
            callback(context.forWhom as Player, MultipleLines(messages))
            return Prompt.END_OF_CONVERSATION
        }

        return StringPrompt(prompt, callback, numberOfLines, escapeWords)
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