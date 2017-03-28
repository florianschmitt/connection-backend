package de.florianschmitt.service.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.florianschmitt.model.entities.ESystemUser;
import de.florianschmitt.repository.SystemUserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SystemUserRepository systemUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ESystemUser user = systemUserRepository.findByEmail(username)//
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        return user;
    }
}
