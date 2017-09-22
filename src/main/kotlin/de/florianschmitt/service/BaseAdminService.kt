package de.florianschmitt.service

import java.io.Serializable
import java.util.Optional

interface BaseAdminService<ENTITY : Serializable> {
    fun save(entity: ENTITY): ENTITY
    fun findAll(): Iterable<ENTITY>
    fun findOne(id: Long): ENTITY?
    fun deleteOne(id: Long)
}
