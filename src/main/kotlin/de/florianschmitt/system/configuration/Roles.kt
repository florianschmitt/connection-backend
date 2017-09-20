package de.florianschmitt.system.configuration

object Roles {

    val ADMIN = "ADMIN"
    val SYSTEMUSER = "SYSTEMUSER"
    val FINANCE = "FINANCE"
    val REQUESTER = "REQUESTER"

    fun authorityName(role: String): String {
        return "ROLE_" + role
    }
}