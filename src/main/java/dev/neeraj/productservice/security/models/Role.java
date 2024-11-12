package dev.neeraj.productservice.security.models;

import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority {
    private final String authority;

    public Role(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    @Override
    public String toString() {
        return authority;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Role role = (Role) obj;
        return authority.equals(role.authority);
    }
}
