package com.github.nullptr7.repo;

import com.github.nullptr7.models.security.AdminUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends CrudRepository<AdminUser, Long> {

    AdminUser findAdminUserByUsername(String username);
}
