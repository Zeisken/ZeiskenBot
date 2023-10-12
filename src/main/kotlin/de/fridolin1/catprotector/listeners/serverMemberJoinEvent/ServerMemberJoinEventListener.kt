package de.fridolin1.catprotector.listeners.serverMemberJoinEvent

import org.javacord.api.DiscordApi

class ServerMemberJoinEventListener {
    companion object {
        @JvmStatic
        val executors = ArrayList<ServerMemberJoinEventExecutor>()

        @JvmStatic
        fun init(api: DiscordApi) {
            api.addServerMemberJoinListener {
                for (executor in executors) executor.execute(it)
            }
        }

        @JvmStatic
        fun register(executor: ServerMemberJoinEventExecutor) {
            executors.add(executor)
        }
    }
}