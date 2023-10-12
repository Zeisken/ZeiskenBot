package de.fridolin1.catprotector.executors

import org.javacord.api.entity.message.MessageFlag
import org.javacord.api.interaction.SlashCommandInteraction

fun sendEphemeralResponse(slashCommandInteraction: SlashCommandInteraction, message: String) {
    slashCommandInteraction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(message).respond()
}