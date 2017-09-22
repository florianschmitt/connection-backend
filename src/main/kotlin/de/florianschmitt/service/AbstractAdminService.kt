package de.florianschmitt.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional
import java.io.Serializable

abstract class AbstractAdminService<ENTITY : Serializable, REPOSITORY : CrudRepository<ENTITY, Long>> : BaseAdminService<ENTITY> {

    @Autowired
    open protected lateinit var repository: REPOSITORY

    @Transactional
    override fun save(entity: ENTITY) = repository.save(entity)

    override fun findAll() = repository.findAll()

    override fun findOne(id: Long) = repository.findById(id).orElse(null)

    @Transactional
    override fun deleteOne(id: Long) = repository.deleteById(id)
}
