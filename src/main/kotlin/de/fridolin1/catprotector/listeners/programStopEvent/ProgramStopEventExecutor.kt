package de.fridolin1.catprotector.listeners.programStopEvent

import org.javacord.api.DiscordApi

interface ProgramStopEventExecutor {
    fun onStop(api: DiscordApi)
}