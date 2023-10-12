package de.fridolin1.catprotector.durable

import de.fridolin1.catprotector.pointAccount.Account
import de.fridolin1.catprotector.pointAccount.RolePayment

object PointAccountDatabaseExchanger {

    @JvmStatic
    fun init() {
        H2DatabaseManager.executeUpdate("CREATE TABLE IF NOT EXISTS ACCOUNT(id BIGINT PRIMARY KEY AUTO_INCREMENT, serverID BIGINT, userID BIGINT, accountBalance BIGINT)")
        H2DatabaseManager.executeUpdate("CREATE TABLE IF NOT EXISTS LOG(id BIGINT PRIMARY KEY AUTO_INCREMENT, sourceID BIGINT, targetID BIGINT, amount BIGINT)")
        H2DatabaseManager.executeUpdate("CREATE TABLE IF NOT EXISTS PAYMENT(id BIGINT PRIMARY KEY AUTO_INCREMENT, serverID BIGINT, roleID BIGINT, payment BIGINT)")
    }

    @JvmStatic
    fun updatePayment(rolePayment: RolePayment) {
        H2DatabaseManager.executeUpdate(
            "UPDATE PAYMENT SET payment = ? WHERE serverID = ? AND roleID = ?",
            rolePayment.payment,
            rolePayment.serverID,
            rolePayment.roleID
        )
    }

    @JvmStatic
    fun getPayment(serverID: Long, roleID: Long): RolePayment {
        val result = H2DatabaseManager.executeSelect(
            "SELECT payment from PAYMENT WHERE serverID = ? AND roleID = ?",
            serverID, roleID
        )
        return if (result.next()) {
            val payment = result.getLong(1)
            return RolePayment(serverID, roleID, payment)
        } else
            newRolePayment(serverID, roleID)
    }

    @JvmStatic
    fun newRolePayment(serverID: Long, roleID: Long): RolePayment {
        H2DatabaseManager.executeUpdate(
            "INSERT INTO PAYMENT(serverID, roleID, payment) VALUES(?, ?, ?)",
            serverID, roleID, 0
        )
        return RolePayment(serverID, roleID, 0)
    }

    @JvmStatic
    fun getAccount(serverID: Long, userID: Long): Account {
        val result = H2DatabaseManager.executeSelect(
            "SELECT accountBalance from ACCOUNT WHERE serverID = ? AND userID = ?",
            serverID, userID
        )
        return if (result.next()) {
            val accountBalance = result.getLong(1)
            Account(serverID, userID, accountBalance)
        } else
            newAccount(serverID, userID)
    }

    @JvmStatic
    fun updateCredit(account: Account) {
        if (!accountExist(account.serverID, account.userID))
            H2DatabaseManager.executeUpdate(
                "UPDATE ACCOUNT SET accountBalance = ? WHERE serverID = ? AND userID = ?",
                account.accountBalance,
                account.serverID,
                account.userID
            )
    }

    @JvmStatic
    fun accountExist(serverID: Long, userID: Long): Boolean {
        val resultSet = H2DatabaseManager.executeSelect(
            "SELECT COUNT(*) FROM ACCOUNT WHERE serverID = ? and userID = ?",
            serverID, userID
        )
        return resultSet.next() && resultSet.getInt(1) == 1
    }

    @JvmStatic
    fun log(source: Account, target: Account, amount: Long) {
        H2DatabaseManager.executeUpdate(
            "INSERT INTO LOG(sourceID, targetID, amount) VALUES((SELECT id FROM ACCOUNT WHERE serverID = ? AND userID = ?), (SELECT id FROM ACCOUNT WHERE serverID = ? AND userID = ?), ?)",
            source.serverID, source.userID, target.serverID, target.userID, amount
        )
    }

    @JvmStatic
    private fun newAccount(serverID: Long, userID: Long): Account {
        H2DatabaseManager.executeUpdate(
            "INSERT INTO ACCOUNT(serverID, userID, accountBalance) VALUES(?, ?, ?)",
            serverID, userID, 0
        )
        return Account(serverID, userID, 0)
    }

    @JvmStatic
    fun getAllPayments(): ArrayList<RolePayment> {
        val result = ArrayList<RolePayment>()
        val resultSet = H2DatabaseManager.executeSelect("SELECT serverID, roleID, payment from payment WHERE payment != 0")
        while (resultSet.next()) {
            result.add(RolePayment(resultSet.getLong(1), resultSet.getLong(2), resultSet.getLong(3)))
        }
        return result
    }
}