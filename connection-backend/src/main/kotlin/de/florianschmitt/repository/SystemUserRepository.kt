package de.florianschmitt.repository

import java.util.Optional

import org.springframework.data.repository.PagingAndSortingRepository

import de.florianschmitt.model.entities.ESystemUser

interface SystemUserRepository : PagingAndSortingRepository<ESystemUser, Long> {
    fun findByEmail(email: String): Optional<ESystemUser>

    fun countByIsActiveTrueAndHasAdminRightTrueAndIdNot(id: Long): Long
}
