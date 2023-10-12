package de.fridolin1.catprotector.pointAccount

import de.fridolin1.catprotector.durable.PointAccountDatabaseExchanger

class Account {
    //Notice: It wouldn't be smart to delete the account if the account balance become zero,
    // because on recreating the account,
    //it gets a new id and with a new id, you will lose the log
    val userID: Long
    val serverID: Long
    var accountBalance: Long = 0
        set(value) {
            field = value
            PointAccountDatabaseExchanger.updateCredit(this)
        }

    constructor(serverID: Long, userID: Long, accountBalance: Long) {
        this.userID = userID
        this.serverID = serverID
        this.accountBalance = accountBalance
    }

    fun transfer(target: Account, amount: Long): Boolean {
        if (amount <= 0) return false
        if (amount > this.accountBalance) return false
        this.accountBalance -= amount
        target.accountBalance += amount
        TransactionLogger.log(this, target, amount)
        return true
    }

    companion object {
        @JvmStatic
        fun getAccount(serverID: Long, userID: Long): Account {
            return PointAccountDatabaseExchanger.getAccount(serverID, userID)
        }
    }
}