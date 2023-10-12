package de.fridolin1.catprotector.pointAccount

import de.fridolin1.catprotector.durable.PointAccountDatabaseExchanger

data class TransactionLogEntry (val source: Account, val destination: Account, val amount: Long) {
    init {
        PointAccountDatabaseExchanger.log(source, destination, amount)
    }
}