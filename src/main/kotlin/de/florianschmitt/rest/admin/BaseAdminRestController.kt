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

    @RequestMapping(path = arrayOf("/get/{id}"), method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    operator fun get(@PathVariable("id") id: Long): ResponseEntity<DTO> {
        val element = service.findOne(id) ?: return ResponseEntity.notFound().build()
        val dto = mapper.map(element, dtoClass)
        return ResponseEntity.ok(dto)
    }

    @RequestMapping(path = arrayOf("/all"), method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    fun all(): ResponseEntity<Page<DTO>> {
        val pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSortForAll)
        val entities = service.findAll(pageable)
        val result = mapper.map(entities, dtoClass)
        return ResponseEntity(result, HttpStatus.OK)
    }

    @RequestMapping(path = arrayOf("/save"), method = arrayOf(RequestMethod.POST))
    fun save(@RequestBody @Valid dto: DTO): ResponseEntity<*> {
        val entity = mapper.map(dto, entityClass)
        service.save(entity)
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    @RequestMapping(path = arrayOf("/delete/{id}"), method = arrayOf(RequestMethod.DELETE))
    fun delete(@PathVariable("id") id: Long): ResponseEntity<*> {
        service.deleteOne(id)
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    protected val defaultSortForAll: Sort
        get() = Sort.unsorted()

    protected val dtoClass: Class<DTO>
        get() = ReflectionUtils.classFromSecondGenericParameter(this)

    protected val entityClass: Class<ENTITY>
        get() = ReflectionUtils.classFromFirstGenericParameter(this)
}
