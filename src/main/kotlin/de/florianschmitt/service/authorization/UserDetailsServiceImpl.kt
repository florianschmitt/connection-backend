package de.florianschmitt.service.authorization

import de.florianschmitt.repository.SystemUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl : UserDetailsService {

    @Autowired
    private lateinit var systemUserRepository: SystemUserRepository

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String) = systemUserRepository.findByEmail(username)
            .orElseThrow { UsernameNotFoundException("user not found") }
}
