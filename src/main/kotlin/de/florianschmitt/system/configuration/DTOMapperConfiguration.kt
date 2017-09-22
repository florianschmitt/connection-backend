package de.florianschmitt.system.configuration

import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class DTOMapperConfiguration {

    @Bean
    fun modelMapper() = ModelMapper()
}
