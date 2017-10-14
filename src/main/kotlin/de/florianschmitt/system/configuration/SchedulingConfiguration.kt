package de.florianschmitt.system.configuration

import de.florianschmitt.system.util.NotTestProfile
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

@NotTestProfile
@Configuration
@EnableScheduling
class SchedulingConfiguration