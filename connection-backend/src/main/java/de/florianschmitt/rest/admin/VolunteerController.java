package de.florianschmitt.rest.admin;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.florianschmitt.model.entities.EVolunteer;
import de.florianschmitt.model.rest.EVolunteerDTO;
import de.florianschmitt.service.VolunteerService;

@RestController
@RequestMapping("/admin/volunteer")
class VolunteerController extends BaseAdminRestController<EVolunteer, EVolunteerDTO, VolunteerService> {

    @Override
    protected Sort getDefaultSortForAll() {
        return new Sort(new Order(Direction.ASC, "lastname"));
    }

}
