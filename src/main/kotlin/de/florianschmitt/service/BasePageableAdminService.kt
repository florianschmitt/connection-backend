package de.florianschmitt.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

import java.io.Serializable

interface BasePageableAdminService<ENTITY : Serializable> : BaseAdminService<ENTITY> {
    fun findAll(pageable: Pageable): Page<ENTITY>
}
