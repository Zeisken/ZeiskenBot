package de.fridolin1.catprotector.pointAccount

class TransactionLogger {
    companion object {
        @JvmStatic
        val log = ArrayList<TransactionLogEntry>()

        @JvmStatic
        fun log(source: Account, target: Account, amount: Long) {
            log.add(TransactionLogEntry(source, target, amount))
        }
    }
}