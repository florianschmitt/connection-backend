package de.florianschmitt.service.data

import de.florianschmitt.model.entities.ESystemUser
import de.florianschmitt.service.SystemUserService
import de.florianschmitt.system.util.DevProfile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@DevProfile
@Component
class AddDevUsersRunner : CommandLineRunner {

    @Autowired
    private lateinit var userService: SystemUserService

    @Throws(Exception::class)
    override fun run(vararg args: String) {
        addAdminUser()
        addTeamUser()
        addFinanceTeamUser()
    }

    private fun addAdminUser() {
        val user = ESystemUser()
        user.email = "admin@connection.de"
        user.cleartextPassword = "admin123"
        user.firstname = "Admin"
        user.lastname = "User"
        user.hasAdminRight = true
        userService.save(user)
    }

    private fun addTeamUser() {
        val user = ESystemUser()
        user.email = "user@connection.de"
        user.cleartextPassword = "user123"
        user.firstname = "Team"
        user.lastname = "User"
        user.hasAdminRight = false
        userService.save(user)
    }

    private fun addFinanceTeamUser() {
        val user = ESystemUser()
        user.email = "financeUser@connection.de"
        user.cleartextPassword = "financeuser123"
        user.firstname = "Team Finance"
        user.lastname = "User"
        user.hasAdminRight = false
        user.hasFinanceRight = true
        userService.save(user)
    }
}
