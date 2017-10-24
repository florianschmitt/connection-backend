package de.florianschmitt.system.configuration

import de.florianschmitt.system.util.NotTestProfile
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler


@NotTestProfile
@Configuration
@EnableScheduling
class SchedulingConfiguration {
    @Bean
    fun taskScheduler(): TaskScheduler {
        return ConcurrentTaskScheduler()
    }
}