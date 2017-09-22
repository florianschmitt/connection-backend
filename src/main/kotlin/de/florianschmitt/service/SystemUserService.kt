package de.florianschmitt.service

import de.florianschmitt.model.entities.ESystemUser
import de.florianschmitt.repository.SystemUserRepository
import de.florianschmitt.rest.exception.SystemMustHaveAtLeastASingleActiveAdmin
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SystemUserService : AbstractPageableAdminService<ESystemUser, SystemUserRepository>() {

    @Autowired
    private lateinit var encoder: PasswordEncoder

    fun findByEmail(email: String) = repository.findByEmail(email)

    @Transactional
    override fun save(user: ESystemUser): ESystemUser {
        handlePasswordHash(user)
        checkIfSystemHasAtLeastOneActiveAdmin(user)
        return super.save(user)
    }

    @Transactional
    override fun deleteOne(id: Long) {
        checkIfUserIsLastRemainingAdmin(id)
        super.deleteOne(id)
    }

    private fun handlePasswordHash(user: ESystemUser) {
        if (user.isNew) {
            doHash(user, true)
        } else {
            if (user.hasClearTextPassword) {
                doHash(user, false)
            } else {
                // copy hashed password from database
                val dbUser = findOne(user.id) ?: throw IllegalStateException()
                user.hashedPassword = dbUser.hashedPassword
            }
        }
    }

    private fun checkIfSystemHasAtLeastOneActiveAdmin(user: ESystemUser) {
        if (user.isNew) {
            return
        }

        if (user.isActive && user.hasAdminRight) {
            return
        }

        checkIfUserIsLastRemainingAdmin(user.id)
    }

    private fun checkIfUserIsLastRemainingAdmin(id: Long) {
        val countByIsActiveTrue = repository.countByIsActiveTrueAndHasAdminRightTrueAndIdNot(id)
        if (countByIsActiveTrue == 0L) {
            throw SystemMustHaveAtLeastASingleActiveAdmin()
        }
    }

    private fun doHash(user: ESystemUser, throwIfEmpty: Boolean) {
        if (!user.hasClearTextPassword) {
            if (throwIfEmpty) {
                throw RuntimeException("provided clear text password cannot be empty")
            } else {
                return
            }
        }

        user.hashedPassword = encoder.encode(user.clearTextPassword)
    }
}
