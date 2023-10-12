package de.fridolin1.catprotector

import com.google.gson.Gson
import de.fridolin1.catprotector.durable.Config
import de.fridolin1.catprotector.durable.H2DatabaseManager
import de.fridolin1.catprotector.durable.PointAccountDatabaseExchanger
import de.fridolin1.catprotector.executors.serverMemberJoinEvent.InternalJoinLogger
import de.fridolin1.catprotector.executors.serverMemberJoinEvent.NicknameAtTheEntryGiver
import de.fridolin1.catprotector.executors.slashCommand.Ping
import de.fridolin1.catprotector.executors.slashCommand.Pong
import de.fridolin1.catprotector.executors.slashCommand.MoneyCommand
import de.fridolin1.catprotector.executors.slashCommand.BankCommand
import de.fridolin1.catprotector.listeners.serverMemberJoinEvent.ServerMemberJoinEventListener
import de.fridolin1.catprotector.listeners.slashCommand.SlashCommandListener
import de.fridolin1.catprotector.pointAccount.Account
import org.javacord.api.DiscordApi
import org.javacord.api.DiscordApiBuilder
import org.javacord.api.entity.permission.PermissionType
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters.next
import java.util.*
import java.util.concurrent.TimeUnit

fun main() {
    //TODO delete payment/account when role/user/server gets deleted
    val token = getToken()
    val api: DiscordApi = DiscordApiBuilder().setAllIntents().setToken(token).login().join()
    println("Successfully logged in to the discord api")

    H2DatabaseManager.init()
    PointAccountDatabaseExchanger.init()

    Config.config = getConfig()

    SlashCommandListener.init(api)
    SlashCommandListener.register("ping", "A simple ping-pong command", api, Ping())
    SlashCommandListener.register("pong", "A simple pong-ping command", api, Pong())
    SlashCommandListener.register(
        "money",
        "Set the amount of money that each person with this role get",
        api,
        MoneyCommand(),
        MoneyCommand.getSlashCommandOptionList(),
        PermissionType.ADMINISTRATOR
    )
    SlashCommandListener.register(
        "bank",
        "Handle you money",
        api,
        BankCommand(),
        BankCommand.getSlashCommandOptionList()
    )
    println("Successfully registered all slash commands")

    ServerMemberJoinEventListener.init(api)
    ServerMemberJoinEventListener.register(InternalJoinLogger())
    ServerMemberJoinEventListener.register(NicknameAtTheEntryGiver())
    println("Successfully registered all server member join event listener")

    setupSchedule(api)
    println("Discord bot is Online")
}

//Thanks to ChatGPT
fun setupSchedule(api: DiscordApi) {
    val timer = Timer()
    val startTime = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"))
    startTime.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    startTime.set(Calendar.HOUR_OF_DAY, 18)
    startTime.set(Calendar.MINUTE, 0)
    startTime.set(Calendar.SECOND, 0)
    if (startTime.before(Calendar.getInstance())) {
        startTime.add(Calendar.WEEK_OF_YEAR, 1)
    }
    val interval = 7 * 24 * 60 * 60 * 1000L
    timer.scheduleAtFixedRate(object : TimerTask() {
        override fun run() {
            val payments = PointAccountDatabaseExchanger.getAllPayments()
            for (payment in payments) {
                val server = api.getServerById(payment.serverID).get()
                val role = server.getRoleById(payment.roleID).get()
                val members = server.members.filter { it.getRoles(server).contains(role) }
                for (member in members) {
                    Account.getAccount(server.id, member.id).accountBalance += payment.payment
                }
            }
        }
    }, startTime.time, interval)
}

fun getToken(): String {
    val tokenFile = File("token.txt")
    if (!tokenFile.exists()) throw Error("Please create the token.txt file and insert the token")
    val reader = BufferedReader(FileReader(tokenFile))
    val token = reader.readLine()
    if (token.isNullOrEmpty()) throw Error("The token.txt file is empty")
    return token
}

fun getConfig(): Config {
    val file = File("config.json")
    if (!file.exists()) {
        file.createNewFile()
        file.writeText("{\"prefix\": \"\",\"suffix\": \"\"}", Charsets.UTF_8)
    }
    val reader = BufferedReader(FileReader(file))
    val raw = reader.readLine()
    return Gson().fromJson(raw, Config::class.java)
}