package de.florianschmitt.rest.admin;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.florianschmitt.model.entities.ELanguage;
import de.florianschmitt.model.rest.ELanguageAdminDTO;
import de.florianschmitt.service.LanguageService;

@RestController
@RequestMapping("/admin/language")
class LanguageAdminController extends BaseAdminRestController<ELanguage, ELanguageAdminDTO, LanguageService> {

    @Override
    protected Sort getDefaultSortForAll() {
        return new Sort(new Order(Direction.ASC, "viewOrder"));
    }

}
