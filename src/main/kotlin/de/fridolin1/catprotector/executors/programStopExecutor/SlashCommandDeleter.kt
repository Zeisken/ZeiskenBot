package de.fridolin1.catprotector.executors.programStopExecutor

import de.fridolin1.catprotector.listeners.programStopEvent.ProgramStopEventExecutor
import org.javacord.api.DiscordApi

class SlashCommandDeleter: ProgramStopEventExecutor {
    override fun onStop(api: DiscordApi) {
        api.globalSlashCommands.get().forEach { it.delete().join() }
    }
}