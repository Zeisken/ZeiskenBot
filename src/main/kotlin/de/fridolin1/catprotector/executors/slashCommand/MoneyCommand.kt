package de.fridolin1.catprotector.executors.slashCommand

import de.fridolin1.catprotector.executors.sendEphemeralResponse
import de.fridolin1.catprotector.listeners.slashCommand.SlashCommandExecutor
import de.fridolin1.catprotector.pointAccount.Account
import de.fridolin1.catprotector.pointAccount.RolePayment
import org.javacord.api.interaction.SlashCommandInteraction
import org.javacord.api.interaction.SlashCommandOption
import org.javacord.api.interaction.SlashCommandOptionType

class MoneyCommand : SlashCommandExecutor {
    override fun execute(slashCommandInteraction: SlashCommandInteraction) {
        val serverID = slashCommandInteraction.server.get().id
        val fullCommand = slashCommandInteraction.fullCommandName.split(" ")
        when (fullCommand[1]) {
            "set" -> {
                if (fullCommand[2] == "role") {
                    val role = slashCommandInteraction.arguments[0].roleValue.get()
                    val payment = slashCommandInteraction.arguments[1].longValue.get()
                    RolePayment.getRolePayment(serverID, role.id).payment = payment
                    sendEphemeralResponse(
                        slashCommandInteraction,
                        "Successfully set the payment from the ${role.name} role to $payment "
                    )
                } else if (fullCommand[2] == "user") {
                    val user = slashCommandInteraction.arguments[0].userValue.get()
                    val money = slashCommandInteraction.arguments[1].longValue.get()
                    Account.getAccount(serverID, user.id).accountBalance = money
                    sendEphemeralResponse(
                        slashCommandInteraction,
                        "Successfully set the account balance from the ${user.name} role to $money"
                    )
                } else
                    sendEphemeralResponse(slashCommandInteraction, "Oh no, something went wrong")
            }

            "get" -> {
                if (fullCommand[2] == "role") {
                    val role = slashCommandInteraction.arguments[0].roleValue.get()
                    val payment = RolePayment.getRolePayment(serverID, role.id).payment
                    sendEphemeralResponse(slashCommandInteraction, "The payment from the ${role.name} role is $payment")
                } else if (fullCommand[2] == "user") {
                    val user = slashCommandInteraction.arguments[0].userValue.get()
                    val accountBalance = Account.getAccount(serverID, user.id).accountBalance
                    sendEphemeralResponse(
                        slashCommandInteraction,
                        "The account balance from ${user.name} is $accountBalance"
                    )
                } else
                    sendEphemeralResponse(slashCommandInteraction, "Oh no, something went wrong")
            }

            "add" -> {
                val money = slashCommandInteraction.arguments[0].longValue.get()
                val user = slashCommandInteraction.arguments[1].userValue.get()
                Account.getAccount(serverID, user.id).accountBalance += money
                sendEphemeralResponse(slashCommandInteraction, "Successfully added $money to ${user.name}")
            }

            "remove" -> {
                val money = slashCommandInteraction.arguments[0].longValue.get()
                val user = slashCommandInteraction.arguments[1].userValue.get()
                Account.getAccount(serverID, user.id).accountBalance -= money
                sendEphemeralResponse(slashCommandInteraction, "Successfully removed $money to ${user.name}")
            }

            else -> {
                sendEphemeralResponse(slashCommandInteraction, "Oh no, something went wrong")
            }
        }

//        val arg0 = slashCommandInteraction.arguments[0]
//        val amount = slashCommandInteraction.arguments[0].longValue
//        val roleID = slashCommandInteraction.arguments[0].roleValue
//        slashCommandInteraction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Amount: $amount; roleID: $roleID;").respond()
    }

    companion object {
        @JvmStatic
        fun getSlashCommandOptionList(): List<SlashCommandOption> {
            return listOf(
                SlashCommandOption.createWithOptions(
                    SlashCommandOptionType.SUB_COMMAND_GROUP, "set", "set the money",
                    listOf(
                        SlashCommandOption.createWithOptions(
                            SlashCommandOptionType.SUB_COMMAND, "role", "set the payment for a role",
                            listOf(
                                SlashCommandOption.create(
                                    SlashCommandOptionType.ROLE,
                                    "role",
                                    "e.g. @Serverteam",
                                    true
                                ),
                                SlashCommandOption.create(
                                    SlashCommandOptionType.LONG,
                                    "money",
                                    "The amount of money that the members of the role get",
                                    true
                                )
                            )
                        ),
                        SlashCommandOption.createWithOptions(
                            SlashCommandOptionType.SUB_COMMAND, "user", "set the account balance for the user",
                            listOf(
                                SlashCommandOption.create(
                                    SlashCommandOptionType.USER,
                                    "user",
                                    "the user",
                                    true
                                ),
                                SlashCommandOption.create(
                                    SlashCommandOptionType.LONG,
                                    "money",
                                    "the value with which the user's account balance is overwritten",
                                    true
                                ),
                            )
                        )
                    )
                ),
                SlashCommandOption.createWithOptions(
                    SlashCommandOptionType.SUB_COMMAND_GROUP, "get", "get the money",
                    listOf(
                        SlashCommandOption.createWithOptions(
                            SlashCommandOptionType.SUB_COMMAND, "role", "get the payment for a role",
                            listOf(
                                SlashCommandOption.create(
                                    SlashCommandOptionType.ROLE,
                                    "role",
                                    "e.g. @Serverteam",
                                    true
                                )
                            )
                        ),
                        SlashCommandOption.createWithOptions(
                            SlashCommandOptionType.SUB_COMMAND, "user", "get the account balance for the user",
                            listOf(
                                SlashCommandOption.create(
                                    SlashCommandOptionType.USER,
                                    "user",
                                    "the user",
                                    true
                                )
                            )
                        )
                    )
                ),
                SlashCommandOption.createWithOptions(
                    SlashCommandOptionType.SUB_COMMAND, "add", "add money to a user",
                    listOf(
                        SlashCommandOption.create(
                            SlashCommandOptionType.LONG,
                            "money",
                            "the money that the user will receive",
                            true
                        ),
                        SlashCommandOption.create(
                            SlashCommandOptionType.USER,
                            "user",
                            "the user that will receive the money",
                            true
                        )
                    )
                ),
                SlashCommandOption.createWithOptions(
                    SlashCommandOptionType.SUB_COMMAND, "remove", "remove money from a user",
                    listOf(
                        SlashCommandOption.create(
                            SlashCommandOptionType.LONG,
                            "money",
                            "the money that the user will lose",
                            true
                        ),
                        SlashCommandOption.create(
                            SlashCommandOptionType.USER,
                            "user",
                            "the user that will lose the money",
                            true
                        )
                    )
                )
            )
        }
    }
}