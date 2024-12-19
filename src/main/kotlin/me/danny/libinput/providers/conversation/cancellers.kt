package me.danny.libinput.providers.conversation

import me.danny.libinput.*
import org.bukkit.conversations.*
import org.bukkit.event.*
import org.bukkit.event.server.*

class ServerReloadCanceller : ConversationCanceller {
    private var conversation: Conversation? = null

    init {
        ServerReloadListener.attachListener(this)
    }

    override fun setConversation(conversation: Conversation) {
        this.conversation = conversation
    }

    override fun cancelBasedOnInput(context: ConversationContext, input: String): Boolean {
        return false
    }

    override fun clone(): ConversationCanceller {
        return this
    }

    fun reloadCancel() {
        conversation!!.abandon(ConversationAbandonedEvent(conversation!!, this))
    }
}

class ServerReloadListener : Listener {
    @EventHandler
    fun onReload(event: PluginDisableEvent) {
        if (event.plugin.javaClass != InputAPI::class.java) return
        cancellers.forEach(ServerReloadCanceller::reloadCancel)
        cancellers.clear()
    }

    companion object {
        private val cancellers: MutableSet<ServerReloadCanceller> = HashSet()
        fun attachListener(canceller: ServerReloadCanceller) = apply { cancellers += canceller }
    }
}