package de.florianschmitt.service.data;

import de.florianschmitt.model.entities.ESystemUser;
import de.florianschmitt.service.SystemUserService;
import de.florianschmitt.system.util.DevPostgresProfile;
import de.florianschmitt.system.util.ProductiveProfile;
import de.florianschmitt.system.util.TestingProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@ProductiveProfile
@TestingProfile
@DevPostgresProfile
@Component
public class AddProdAdminRunner implements CommandLineRunner {

    private static final String DEFAULT_EMAIL = "admin@connection.de";

    @Autowired
    private SystemUserService userService;

    @Override
    public void run(String... args) throws Exception {
        addAdminUser();
    }

    private void addAdminUser() {
        if (userService.findByEmail(DEFAULT_EMAIL).isPresent()) {
            return;
        }

        ESystemUser user = new ESystemUser();
        user.setEmail(DEFAULT_EMAIL);
        user.setCleartextPassword("admin123");
        user.setFirstname("Admin");
        user.setLastname("User");
        user.setHasAdminRight(true);
        userService.save(user);
    }
}
