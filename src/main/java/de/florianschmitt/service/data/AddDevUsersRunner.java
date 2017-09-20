package de.florianschmitt.service.data;

import de.florianschmitt.model.entities.ESystemUser;
import de.florianschmitt.service.SystemUserService;
import de.florianschmitt.system.util.DevProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@DevProfile
@Component
public class AddDevUsersRunner implements CommandLineRunner {

    @Autowired
    private SystemUserService userService;

    @Override
    public void run(String... args) throws Exception {
        addAdminUser();
        addTeamUser();
        addFinanceTeamUser();
    }

    private void addAdminUser() {
        ESystemUser user = new ESystemUser();
        user.setEmail("admin@connection.de");
        user.setCleartextPassword("admin123");
        user.setFirstname("Admin");
        user.setLastname("User");
        user.setHasAdminRight(true);
        userService.save(user);
    }

    private void addTeamUser() {
        ESystemUser user = new ESystemUser();
        user.setEmail("user@connection.de");
        user.setCleartextPassword("user123");
        user.setFirstname("Team");
        user.setLastname("User");
        user.setHasAdminRight(false);
        userService.save(user);
    }

    private void addFinanceTeamUser() {
        ESystemUser user = new ESystemUser();
        user.setEmail("financeUser@connection.de");
        user.setCleartextPassword("financeuser123");
        user.setFirstname("Team Finance");
        user.setLastname("User");
        user.setHasAdminRight(false);
        user.setHasFinanceRight(true);
        userService.save(user);
    }
}
