package de.fridolin1.catprotector.executors.serverMemberJoinEvent

import de.fridolin1.catprotector.listeners.serverMemberJoinEvent.ServerMemberJoinEventExecutor
import org.javacord.api.event.server.member.ServerMemberJoinEvent

class InternalJoinLogger: ServerMemberJoinEventExecutor{
    override fun execute(serverMemberJoinEvent: ServerMemberJoinEvent) {
        println("The use ${serverMemberJoinEvent.user.name} joined the server ${serverMemberJoinEvent.server.name}")
    }
}