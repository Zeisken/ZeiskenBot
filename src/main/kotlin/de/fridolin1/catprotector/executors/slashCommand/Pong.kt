package de.fridolin1.catprotector.executors.slashCommand

import de.fridolin1.catprotector.listeners.slashCommand.SlashCommandExecutor
import org.javacord.api.entity.message.MessageFlag
import org.javacord.api.interaction.SlashCommandInteraction

class Pong: SlashCommandExecutor {
    override fun execute(slashCommandInteraction: SlashCommandInteraction) {
        slashCommandInteraction.createImmediateResponder()
            .setContent("Ping!")
            .setFlags(MessageFlag.EPHEMERAL)
            .respond()
    }
}