package com.compilit.core.api.security;

import java.util.Collection;
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;

public interface User {

    /**
     * @return the granted authorities of this user
     */
    Collection<GrantedAuthority> getAuthorities();

    /**
     * @return a boolean representing non-expiration of the account
     */
    boolean isAccountNonExpired();
    /**
     * @return a boolean representing if the is account isn't locked
     */
    boolean isAccountNonLocked();
    /**
     * @return a boolean representing non-expiration of the credentials
     */
    boolean isCredentialsNonExpired();
    /**
     * @return a boolean about the enablement of the account
     */
    boolean isEnabled();

    /**
     * @return the username of this user
     */
    String getUsername();

    /**
     * @return the password of this user
     */
    String getPassword();

    /**
     * @return the external user id
     */
    UUID getUserId();
}
