package de.florianschmitt.service;

import de.florianschmitt.model.entities.EVolunteer;
import de.florianschmitt.repository.VolunteerRepository;
import org.springframework.stereotype.Service;

@Service
public class VolunteerService extends AbstractPageableAdminService<EVolunteer, VolunteerRepository> {
}
