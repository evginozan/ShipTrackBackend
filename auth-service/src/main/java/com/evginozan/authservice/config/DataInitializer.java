package com.evginozan.authservice.config;

import com.evginozan.authservice.model.Role;
import com.evginozan.authservice.model.User;
import com.evginozan.authservice.repository.RoleRepository;
import com.evginozan.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createRoles();
        createAdminUser();
    }

    private void createRoles() {
        if (roleRepository.count() == 0) {
            Role adminRole = new Role();
            adminRole.setName(Role.ERole.ROLE_ADMIN);
            roleRepository.save(adminRole);

            Role staffRole = new Role();
            staffRole.setName(Role.ERole.ROLE_STAFF);
            roleRepository.save(staffRole);

            Role customerRole = new Role();
            customerRole.setName(Role.ERole.ROLE_CUSTOMER);
            roleRepository.save(customerRole);
        }
    }

    private void createAdminUser() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@kargotakip.com");
            admin.setFullName("Sistem Yöneticisi");
            admin.setPhone("5551234567");

            Set<Role> roles = new HashSet<>();
            Role adminRole = roleRepository.findByName(Role.ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Hata: Rol bulunamadı."));
            roles.add(adminRole);
            admin.setRoles(roles);

            userRepository.save(admin);
        }
    }
}
