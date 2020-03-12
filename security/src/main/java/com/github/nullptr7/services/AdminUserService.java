package com.github.nullptr7.services;

import com.github.nullptr7.models.security.AdminUser;
import com.github.nullptr7.repo.AdminRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class AdminUserService implements UserDetailsService {

    private AdminRepository adminRepository;

    public AdminUserService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AdminUser adminUser = adminRepository.findAdminUserByUsername(username);

        return new User(adminUser.getUsername(), adminUser.getPassword(), emptyList());
    }
}
