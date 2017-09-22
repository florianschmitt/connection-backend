package de.florianschmitt.rest.admin

import de.florianschmitt.model.entities.EVolunteer
import de.florianschmitt.model.rest.EVolunteerDTO
import de.florianschmitt.service.VolunteerService
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.domain.Sort.Order
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/volunteer")
internal class VolunteerController : BaseAdminRestController<EVolunteer, EVolunteerDTO, VolunteerService>() {

    override val defaultSortForAll: Sort
        get() = Sort.by(Order(Direction.ASC, "lastname"))

}
