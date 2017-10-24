package de.florianschmitt.rest.admin

import de.florianschmitt.model.entities.ESystemUser
import de.florianschmitt.model.entities.EVolunteer
import de.florianschmitt.model.rest.ESystemUserDTO
import de.florianschmitt.service.SystemUserService
import de.florianschmitt.service.util.toDto
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.domain.Sort.Order
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/systemuser")
internal class SystemUserController : BaseAdminRestController<ESystemUser, ESystemUserDTO, SystemUserService>() {

    override val defaultSortForAll: Sort
        get() = Sort.by(Order(Direction.ASC, "lastname"))

    override fun mapToDto(element: ESystemUser) = element.toDto()
}