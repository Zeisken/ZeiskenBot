package de.fridolin1.catprotector.executors.slashCommand

import de.fridolin1.catprotector.listeners.slashCommand.SlashCommandExecutor
import org.javacord.api.interaction.SlashCommandInteraction
import org.javacord.api.interaction.SlashCommandOption
import org.javacord.api.interaction.SlashCommandOptionChoice
import org.javacord.api.interaction.SlashCommandOptionType
import de.fridolin1.catprotector.executors.*
import de.fridolin1.catprotector.pointAccount.Account

class BankCommand : SlashCommandExecutor {
    override fun execute(slashCommandInteraction: SlashCommandInteraction) {
        val serverID = slashCommandInteraction.server.get().id
        val fullCommand = slashCommandInteraction.fullCommandName.split(" ")
        val user = slashCommandInteraction.user
        val userAccount = Account.getAccount(serverID, user.id)
        when (fullCommand[1]) {
            "get" -> {
                if (slashCommandInteraction.arguments[0].decimalValue.get() == 0.0) { //Account balance
                    sendEphemeralResponse(
                        slashCommandInteraction,
                        "You current account balance is ${userAccount.accountBalance}"
                    )
                } else if (slashCommandInteraction.arguments[0].decimalValue.get() == 1.0) { //Log
                    //TODO
                    sendEphemeralResponse(slashCommandInteraction, "Sorry, this features in not implemented yet")
                } else
                    sendEphemeralResponse(slashCommandInteraction, "Sorry, there was an internal Server error")
            }

            "transfer" -> {
                val targetUser = slashCommandInteraction.arguments[0].userValue.get()
                val amount = slashCommandInteraction.arguments[1].longValue.get()
                val targetAccount = Account.getAccount(serverID, targetUser.id)
                if (userAccount.transfer(targetAccount, amount))
                    sendEphemeralResponse(slashCommandInteraction, "Successfully send $amount to ${targetUser.name}")
                else if (amount <= 0)
                    sendEphemeralResponse(slashCommandInteraction, "You can't send nothing or less then zero :)")
                else if (amount > userAccount.accountBalance)
                    sendEphemeralResponse(slashCommandInteraction, "Oh no, your account balance is to low")
            }

            else -> sendEphemeralResponse(slashCommandInteraction, "Sorry, there was an internal Server error")
        }
    }

    companion object {
        @JvmStatic
        fun getSlashCommandOptionList(): List<SlashCommandOption> {
            return listOf(
                SlashCommandOption.createWithOptions(
                    SlashCommandOptionType.SUB_COMMAND, "transfer", "send money to another user",
                    listOf(
                        SlashCommandOption.create(
                            SlashCommandOptionType.USER,
                            "target",
                            "the person you want to send the money to",
                            true
                        ),
                        SlashCommandOption.create(
                            SlashCommandOptionType.LONG,
                            "value",
                            "the amount of money that you want to sent",
                            true
                        )
                    )
                ),
                SlashCommandOption.createWithOptions(
                    SlashCommandOptionType.SUB_COMMAND, "get", "get some information",
                    listOf(
                        SlashCommandOption.createWithChoices(
                            SlashCommandOptionType.DECIMAL, "get", "get some information", true,
                            listOf(
                                SlashCommandOptionChoice.create("balance", 0),
                                SlashCommandOptionChoice.create("log", 1)
                            )
                        )
                    )
                ),
//
//                SlashCommandOption.createWithChoices(
//                    SlashCommandOptionType.DECIMAL, "get", "get some information", true,
//                    listOf(
//                        SlashCommandOptionChoice.create("balance", 0),
//                        SlashCommandOptionChoice.create("log", 1)
//                    )
//                )
            )
        }
    }
}