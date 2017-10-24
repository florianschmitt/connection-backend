package de.florianschmitt.rest.admin

import de.florianschmitt.model.entities.ESystemUser
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.core.context.SecurityContextHolder

object Helper {
    val currentUser: ESystemUser
        get() {
            val authentication = SecurityContextHolder.getContext().authentication
            if (authentication !is AnonymousAuthenticationToken) {
                return authentication.principal as ESystemUser
            }
            throw AuthenticationCredentialsNotFoundException("could not get user")
        }
}