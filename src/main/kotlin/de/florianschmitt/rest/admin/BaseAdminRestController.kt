package de.florianschmitt.rest.admin

import de.florianschmitt.model.entities.BaseEntity
import de.florianschmitt.service.BasePageableAdminService
import de.florianschmitt.service.util.DTOMapper
import de.florianschmitt.system.util.ReflectionUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.Serializable
import javax.validation.Valid

@RestController
internal abstract class BaseAdminRestController<ENTITY : BaseEntity, DTO : Serializable, SERVICE : BasePageableAdminService<ENTITY>> {

    @Autowired
    private lateinit var service: SERVICE

    @Autowired
    private lateinit var mapper: DTOMapper

    @GetMapping(path = arrayOf("/get/{id}"), produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    fun get(@PathVariable("id") id: Long): ResponseEntity<DTO> {
        val element = service.findOne(id) ?: return ResponseEntity.notFound().build()
        val dto = mapToDto(element)
        return ResponseEntity.ok(dto)
    }

    @GetMapping(path = arrayOf("/all"), produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    fun all(): ResponseEntity<Page<DTO>> {
        val pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSortForAll)
        val entities = service.findAll(pageable)
        val result = mapToDto(entities)
        return ResponseEntity.ok(result)
    }

    @PostMapping(path = arrayOf("/save"))
    @ResponseStatus(HttpStatus.OK)
    fun save(@RequestBody @Valid dto: DTO) {
        val entity = mapToEntity(dto)
        service.save(entity)
    }

    @DeleteMapping(path = arrayOf("/delete/{id}"))
    @ResponseStatus(HttpStatus.OK)
    fun delete(@PathVariable("id") id: Long) {
        service.deleteOne(id)
    }

    protected fun mapToEntity(dto: DTO) = mapper.map(dto, entityClass)
    protected fun mapToDto(element: ENTITY) = mapper.map(element, dtoClass)
    final private fun mapToDto(elements: Collection<ENTITY>) = elements.map { mapToDto(it) }
    final private fun mapToDto(elements: Page<ENTITY>) = elements.map { mapToDto(it) }

    protected val defaultSortForAll: Sort
        get() = Sort.unsorted()

    protected val dtoClass: Class<DTO>
        get() = ReflectionUtils.classFromSecondGenericParameter(this)

    protected val entityClass: Class<ENTITY>
        get() = ReflectionUtils.classFromFirstGenericParameter(this)
}
