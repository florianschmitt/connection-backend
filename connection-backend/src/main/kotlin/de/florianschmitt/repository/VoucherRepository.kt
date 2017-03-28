package de.florianschmitt.repository

import java.util.Optional

import org.springframework.data.repository.CrudRepository

import de.florianschmitt.model.entities.ERequest
import de.florianschmitt.model.entities.EVoucher

interface VoucherRepository : CrudRepository<EVoucher, Long> {
    fun findByIdentifier(identifier: String): Optional<EVoucher>

    fun findByRequest(request: ERequest): Collection<EVoucher>
}
