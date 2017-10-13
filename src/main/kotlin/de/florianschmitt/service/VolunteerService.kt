package de.florianschmitt.service

import de.florianschmitt.model.entities.EVolunteer
import de.florianschmitt.repository.VolunteerRepository
import de.florianschmitt.service.base.AbstractPageableAdminService
import org.springframework.stereotype.Service

@Service
class VolunteerService : AbstractPageableAdminService<EVolunteer, VolunteerRepository>()
