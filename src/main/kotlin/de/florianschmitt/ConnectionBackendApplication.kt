package de.florianschmitt

import de.florianschmitt.model.entities.BaseEntity
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters

@EntityScan(basePackageClasses = [BaseEntity::class, Jsr310JpaConverters::class])
@SpringBootApplication
class ConnectionBackendApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(ConnectionBackendApplication::class.java, *args)
        }
    }
}
