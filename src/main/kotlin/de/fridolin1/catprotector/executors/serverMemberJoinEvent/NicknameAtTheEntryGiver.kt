package de.fridolin1.catprotector.executors.serverMemberJoinEvent

import de.fridolin1.catprotector.durable.Config.Companion.config
import de.fridolin1.catprotector.listeners.serverMemberJoinEvent.ServerMemberJoinEventExecutor
import org.javacord.api.event.server.member.ServerMemberJoinEvent

class NicknameAtTheEntryGiver: ServerMemberJoinEventExecutor{
    override fun execute(serverMemberJoinEvent: ServerMemberJoinEvent) {
        val user = serverMemberJoinEvent.user
        val server = serverMemberJoinEvent.server
        var name = user.getDisplayName(server)
        name = name[0].toString().uppercase() + name.substring(1)
        user.updateNickname(server, config.prefix + name + config.suffix)
    }
}