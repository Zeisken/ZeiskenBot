package de.fridolin1.catprotector.listeners.programStopEvent

import org.javacord.api.DiscordApi

class ProgramStopEventListener {
    companion object {
        @JvmStatic
        private val executors = ArrayList<ProgramStopEventExecutor>()

        fun register(executor: ProgramStopEventExecutor) {
            executors.add(executor)
        }

        fun onProgramStop(api: DiscordApi) {
            for (executor in executors) executor.onStop(api)
        }
    }
}