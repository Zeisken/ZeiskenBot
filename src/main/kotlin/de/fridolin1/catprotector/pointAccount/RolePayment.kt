package de.fridolin1.catprotector.pointAccount

import de.fridolin1.catprotector.durable.PointAccountDatabaseExchanger

class RolePayment {
    //TODO only let the entry in the db be registered, when the payment it is not zero.
    //TODO when the payment become zero, delete the entry
    val serverID: Long
    val roleID: Long
    var payment: Long = 0
        set(value) {
            field = value
            PointAccountDatabaseExchanger.updatePayment(this)
        }

    constructor(serverID: Long, roleID: Long, payment: Long) {
        this.serverID = serverID
        this.roleID = roleID
        this.payment = payment
    }

    companion object {
        @JvmStatic
        fun getRolePayment(serverID: Long, roleID: Long): RolePayment {
            return PointAccountDatabaseExchanger.getPayment(serverID, roleID)
        }
    }
}