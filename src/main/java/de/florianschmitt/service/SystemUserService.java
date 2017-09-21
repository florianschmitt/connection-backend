package de.florianschmitt.service;

import de.florianschmitt.model.entities.ESystemUser;
import de.florianschmitt.repository.SystemUserRepository;
import de.florianschmitt.rest.exception.SystemMustHaveAtLeastASingleActiveAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SystemUserService extends AbstractPageableAdminService<ESystemUser, SystemUserRepository> {

    @Autowired
    private PasswordEncoder encoder;

    public Optional<ESystemUser> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    @Transactional
    public void save(ESystemUser user) {
        handlePasswordHash(user);
        checkIfSystemHasAtLeastOneActiveAdmin(user);
        repository.save(user);
    }

    @Override
    @Transactional
    public void deleteOne(long id) {
        checkIfUserIsLastRemainingAdmin(id);
        super.deleteOne(id);
    }

    private void handlePasswordHash(ESystemUser user) {
        if (user.isNew()) {
            doHash(user, true);
        } else {
            if (user.hasCleartextPassword()) {
                doHash(user, false);
            } else {
                // copy hashed password from database
                ESystemUser dbUser = repository.findById(user.getId()).orElseThrow(IllegalStateException::new);
                user.setHashedPassword(dbUser.getHashedPassword());
            }
        }
    }

    private void checkIfSystemHasAtLeastOneActiveAdmin(ESystemUser user) {
        boolean userIsNew = user.isNew();
        if (userIsNew) {
            return;
        }

        boolean isActive = user.isActive();
        boolean hasAdminRight = user.getHasAdminRight();

        if (isActive && hasAdminRight) {
            return;
        }

        checkIfUserIsLastRemainingAdmin(user.getId());
    }

    private void checkIfUserIsLastRemainingAdmin(long id) {
        long countByIsActiveTrue = repository.countByIsActiveTrueAndHasAdminRightTrueAndIdNot(id);
        if (countByIsActiveTrue == 0) {
            throw new SystemMustHaveAtLeastASingleActiveAdmin();
        }
    }

    private void doHash(ESystemUser user, boolean throwIfEmpty) {
        if (!user.hasCleartextPassword()) {
            if (throwIfEmpty) {
                throw new RuntimeException("provided clear text password cannot be empty");
            } else {
                return;
            }
        }

        String clearTextPassword = user.getCleartextPassword();
        String hash = encoder.encode(clearTextPassword);
        user.setHashedPassword(hash);
    }
}
