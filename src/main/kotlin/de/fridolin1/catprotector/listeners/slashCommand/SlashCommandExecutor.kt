package de.fridolin1.catprotector.listeners.slashCommand

import org.javacord.api.interaction.SlashCommandInteraction

interface SlashCommandExecutor {
    fun execute(slashCommandInteraction: SlashCommandInteraction)
}