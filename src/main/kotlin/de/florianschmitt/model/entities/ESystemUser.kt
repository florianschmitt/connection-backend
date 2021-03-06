package de.florianschmitt.model.entities

import de.florianschmitt.system.configuration.Roles
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Transient
import javax.validation.constraints.Size

@Entity
class ESystemUser : EAbstractUser(), UserDetails {

    // BCrypt
    @Column(nullable = false, length = 60)
    @Size(min = 60, max = 60)
    var hashedPassword: String? = null

    // only used, when setting or changing password
    @Transient
    var clearTextPassword: String? = null

    @Column(nullable = false)
    var hasAdminRight = false

    @Column(nullable = false)
    var hasFinanceRight = false

    override fun getAuthorities(): Collection<GrantedAuthority> {
        val result = mutableSetOf(Roles.REQUESTER, Roles.SYSTEMUSER);

        if (hasFinanceRight) result.add(Roles.FINANCE)
        if (hasAdminRight) result.add(Roles.ADMIN)

        return result.map(Roles::authorityName)
                .map(::SimpleGrantedAuthority)
                .toSet()
    }

    val hasClearTextPassword
        get() = clearTextPassword?.isNotBlank() ?: false

    override fun getUsername() = email
    override fun getPassword() = hashedPassword
    override fun isEnabled() = isActive
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
}
