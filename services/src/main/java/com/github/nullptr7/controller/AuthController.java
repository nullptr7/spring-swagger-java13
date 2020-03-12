package com.github.nullptr7.controller;

import com.github.nullptr7.config.WebSecurityConf;
import com.github.nullptr7.models.security.AdminUser;
import com.github.nullptr7.models.security.AuthenticatedResponse;
import com.github.nullptr7.repo.AdminRepository;
import com.github.nullptr7.services.AdminUserService;
import com.github.nullptr7.services.JwtUtilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Function;

@Slf4j
@RestController
@Import(WebSecurityConf.class)
@EnableJpaRepositories(basePackages = "com.github.nullptr7.repo")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Autowired
    private AdminUserService adminUserService;

    @RequestMapping(value = "/addAdmin", method = RequestMethod.POST)
    public ResponseEntity<?> addAdminUSer(@RequestBody AdminUser adminUser) {
        var savedAdminUser = adminRepository.save(adminUser);
        return ResponseEntity.ok(savedAdminUser);
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AdminUser authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(getAuth.apply(authenticationRequest));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = adminUserService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtilService.generateToken(userDetails);
        return ResponseEntity.ok(AuthenticatedResponse.builder().jwt(jwt).build());
    }

    private Function<AdminUser, UsernamePasswordAuthenticationToken> getAuth = authenticationRequest ->
            new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());

}
