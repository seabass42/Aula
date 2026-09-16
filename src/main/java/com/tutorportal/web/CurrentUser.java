package com.tutorportal.web;

import com.tutorportal.user.User;
import com.tutorportal.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Security gives controllers a UserDetails (email + authorities), not our own
 * User entity. This resolves one to the other in a single place.
 */
@Component
public class CurrentUser {

    private final UserRepository users;

    public CurrentUser(UserRepository users) {
        this.users = users;
    }

    public User resolve(UserDetails principal) {
        return users.findByEmail(principal.getUsername())
                .orElseThrow(() -> new IllegalStateException(
                        "Authenticated principal has no matching user row: " + principal.getUsername()));
    }
}
