package de.florianschmitt.service.data

import de.florianschmitt.model.entities.ESystemUser
import de.florianschmitt.service.SystemUserService
import de.florianschmitt.system.util.DevPostgresProfile
import de.florianschmitt.system.util.ProductiveProfile
import de.florianschmitt.system.util.TestingProfile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@ProductiveProfile
@TestingProfile
@DevPostgresProfile
@Component
class AddProdAdminRunner : CommandLineRunner {

    @Autowired
    private lateinit var userService: SystemUserService

    @Throws(Exception::class)
    override fun run(vararg args: String) {
        addAdminUser()
    }

    private fun addAdminUser() {
        if (userService.findByEmail(DEFAULT_EMAIL).isPresent) {
            return
        }

        val user = ESystemUser()
        user.email = DEFAULT_EMAIL
        user.clearTextPassword = "admin123"
        user.firstname = "Admin"
        user.lastname = "User"
        user.hasAdminRight = true
        userService.save(user)
    }

    companion object {

        private val DEFAULT_EMAIL = "admin@connection.de"
    }
}
