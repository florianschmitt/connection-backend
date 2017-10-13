package de.florianschmitt.service.base

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

import java.io.Serializable

internal interface BasePageableAdminService<ENTITY : Serializable> : BaseAdminService<ENTITY> {
    fun findAll(pageable: Pageable): Page<ENTITY>
}
