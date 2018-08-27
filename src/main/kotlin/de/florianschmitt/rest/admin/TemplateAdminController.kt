package de.florianschmitt.rest.admin

import de.florianschmitt.model.entities.ETemplate
import de.florianschmitt.model.rest.ETemplateDTO
import de.florianschmitt.service.TemplateService
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/templates")
internal class TemplateAdminController : BaseAdminRestController<ETemplate, ETemplateDTO, TemplateService>() {

    override val defaultSortForAll: Sort
        get() = Sort.by(Sort.Order(Sort.Direction.ASC, "identifier"))

}
