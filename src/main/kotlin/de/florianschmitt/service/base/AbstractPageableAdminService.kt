package de.florianschmitt.service.base

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import java.io.Serializable

abstract class AbstractPageableAdminService<ENTITY : Serializable, REPOSITORY : PagingAndSortingRepository<ENTITY, Long>> : AbstractAdminService<ENTITY, REPOSITORY>(), BasePageableAdminService<ENTITY> {

    @Autowired
    final override lateinit var repository: REPOSITORY

    override fun findAll(pageable: Pageable) = repository.findAll(pageable)
}
