package de.fridolin1.catprotector.durable

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException

object H2DatabaseManager {
    private lateinit var connection: Connection

    @JvmStatic
    fun init() {
        val databaseURL = "jdbc:h2:./database;AUTO_SERVER=TRUE"
        val user = "sa"
        val password = ""

        try {
            Class.forName("org.h2.Driver")
            connection = DriverManager.getConnection(databaseURL, user, password)
            println("Connected to the database")
        } catch (ex: ClassNotFoundException) {
            println("Could not find database driver class")
            ex.printStackTrace()
        } catch (ex: SQLException) {
            println("An error occurred. Maybe user/password is invalid")
            ex.printStackTrace()
        }
    }

    @JvmStatic
    fun executeUpdate(sql: String, vararg values: Any) {
        val statement = connection.prepareStatement(sql)
        values.onEachIndexed { i, value ->
            statement.setObject(i + 1, value)
        }
        statement.executeUpdate()
        statement.close()
    }

    @JvmStatic
    fun executeSelect(sql: String, vararg values: Any): ResultSet {
        val statement = connection.prepareStatement(sql)
        values.onEachIndexed { i, value ->
            statement.setObject(i + 1, value)
        }
        return statement.executeQuery()
    }

    @JvmStatic
    fun close() {
        try {
            connection.close()
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }
}