package de.fridolin1.catprotector.listeners.slashCommand

import org.javacord.api.DiscordApi
import org.javacord.api.entity.permission.PermissionType
import org.javacord.api.interaction.SlashCommand
import org.javacord.api.interaction.SlashCommandOption

class SlashCommandListener {
    companion object {
        @JvmStatic
        val executors = HashMap<String, SlashCommandExecutor>()

        @JvmStatic
        fun init(api: DiscordApi) {
            api.addSlashCommandCreateListener {
                val slashCommandInteraction = it.slashCommandInteraction
                executors[slashCommandInteraction.commandName]?.execute(slashCommandInteraction)
            }
        }

        @JvmStatic
        fun register(
            command: String,
            description: String,
            api: DiscordApi,
            executor: SlashCommandExecutor,
            vararg permission: PermissionType
        ) {
            executors[command] = executor
            if (permission.isEmpty())
                SlashCommand.with(command, description).createGlobal(api).join()
            else
                SlashCommand.with(command, description).setDefaultEnabledForPermissions(*permission).createGlobal(api)
                    .join()
        }

        @JvmStatic
        fun register(
            command: String,
            description: String,
            api: DiscordApi,
            executor: SlashCommandExecutor,
            slashCommandOptionList: List<SlashCommandOption>,
            vararg permission: PermissionType
        ) {
            executors[command] = executor
            if (permission.isEmpty())
                SlashCommand.with(command, description, slashCommandOptionList).createGlobal(api).join()
            else
                SlashCommand.with(command, description, slashCommandOptionList)
                    .setDefaultEnabledForPermissions(*permission).createGlobal(api).join()
        }
    }
}