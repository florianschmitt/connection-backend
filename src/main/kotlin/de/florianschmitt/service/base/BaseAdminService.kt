package de.florianschmitt.service.base

import java.io.Serializable

internal interface BaseAdminService<ENTITY : Serializable> {
    fun save(entity: ENTITY): ENTITY
    fun findOne(id: Long): ENTITY?
    fun deleteOne(id: Long)
}
