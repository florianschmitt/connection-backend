package de.florianschmitt.system.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.scheduling.annotation.EnableAsync
import java.util.concurrent.Executor


@Configuration
@EnableAsync
class AsyncConfiguration {

    @Bean
    fun taskExecutor(): Executor {
        return SimpleAsyncTaskExecutor()
    }
}
