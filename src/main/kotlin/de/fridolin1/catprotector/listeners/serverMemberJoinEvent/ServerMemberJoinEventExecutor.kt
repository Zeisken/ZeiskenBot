package de.fridolin1.catprotector.listeners.serverMemberJoinEvent

import org.javacord.api.event.server.member.ServerMemberJoinEvent

interface ServerMemberJoinEventExecutor {
    fun execute(serverMemberJoinEvent: ServerMemberJoinEvent)
}