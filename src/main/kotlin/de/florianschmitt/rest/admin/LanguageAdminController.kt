package de.florianschmitt.rest.admin

import de.florianschmitt.model.entities.ELanguage
import de.florianschmitt.model.rest.ELanguageAdminDTO
import de.florianschmitt.service.LanguageService
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.domain.Sort.Order
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/language")
internal class LanguageAdminController : BaseAdminRestController<ELanguage, ELanguageAdminDTO, LanguageService>() {

    override val defaultSortForAll: Sort
        get() = Sort.by(Order(Direction.ASC, "viewOrder"))

}
