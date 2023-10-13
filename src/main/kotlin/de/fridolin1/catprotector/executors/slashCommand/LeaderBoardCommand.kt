package de.fridolin1.catprotector.executors.slashCommand

import de.fridolin1.catprotector.durable.PointAccountDatabaseExchanger
import de.fridolin1.catprotector.listeners.slashCommand.SlashCommandExecutor
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.interaction.SlashCommandInteraction
import java.awt.Color

class LeaderBoardCommand : SlashCommandExecutor {
    override fun execute(slashCommandInteraction: SlashCommandInteraction) {
        val server = slashCommandInteraction.server.get()
        val serverID = server.id
        val leaderBoard = PointAccountDatabaseExchanger.leaderboard(serverID, 10)
        val embed = EmbedBuilder()
        embed.setTitle("Leaderboard")
        embed.setColor(Color(218, 165, 32))
        leaderBoard.forEachIndexed { i, account ->
            var name = server.getMemberById(account.userID).get().name
            name = name[0].uppercaseChar() + name.substring(1)
            embed.addField(
                name,
                "Place ${i + 1} with ${account.accountBalance} Points"
            )
        }
        slashCommandInteraction.createImmediateResponder().addEmbed(embed).respond()
    }
}