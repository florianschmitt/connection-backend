package de.florianschmitt.system.util.schema_creator;

import de.florianschmitt.model.entities.BaseEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@SpringBootApplication
@EntityScan(basePackageClasses = {BaseEntity.class, Jsr310JpaConverters.class})
public class CreateCurrentSchemaApplication {
    public static void main(String[] args) {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "current-schema");
        SpringApplication.run(CreateCurrentSchemaApplication.class, args);
    }
}
