package de.florianschmitt.service.data

import de.florianschmitt.model.entities.ESystemUser
import de.florianschmitt.service.SystemUserService
import de.florianschmitt.system.util.DevPostgresProfile
import de.florianschmitt.system.util.DevProfile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@DevProfile
@DevPostgresProfile
@Component
class AddDevUsersRunner : CommandLineRunner {

    @Autowired
    private lateinit var userService: SystemUserService

    @Throws(Exception::class)
    override fun run(vararg args: String) {
        addAdminUser("admin@connection.de")
        addTeamUser("user@connection.de")
        addFinanceTeamUser("financeUser@connection.de")
    }

    private fun addAdminUser(email: String) {
        if (userService.findByEmail(email).isPresent)
            return

        val user = ESystemUser()
        user.email = email
        user.clearTextPassword = "admin123"
        user.firstname = "Admin"
        user.lastname = "User"
        user.hasAdminRight = true
        userService.save(user)
    }

    private fun addTeamUser(email: String) {
        if (userService.findByEmail(email).isPresent)
            return

        val user = ESystemUser()
        user.email = email
        user.clearTextPassword = "user123"
        user.firstname = "Team"
        user.lastname = "User"
        user.hasAdminRight = false
        userService.save(user)
    }

    private fun addFinanceTeamUser(email: String) {
        if (userService.findByEmail(email).isPresent)
            return

        val user = ESystemUser()
        user.email = email
        user.clearTextPassword = "financeuser123"
        user.firstname = "Team Finance"
        user.lastname = "User"
        user.hasAdminRight = false
        user.hasFinanceRight = true
        userService.save(user)
    }
}
