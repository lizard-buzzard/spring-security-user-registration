package com.lizard.buzzard.web.dto;

import java.util.Objects;

public class UserWithAuthorityRights {
    private String firstname;
    private String lastname;
    private boolean enabled;
    private String email;

    private String roleWithPrivileges;

    public UserWithAuthorityRights() {
    }

    public String getRoleWithPrivileges() {
        return roleWithPrivileges;
    }

    public void setRoleWithPrivileges(String roleWithPrivileges) {
        this.roleWithPrivileges = roleWithPrivileges;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserWithAuthorityRights)) return false;
        UserWithAuthorityRights that = (UserWithAuthorityRights) o;
        return Objects.equals(getEmail(), that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail());
    }

    @Override
    public String toString() {
        return "UserWithAuthorityRights{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", enabled=" + enabled +
                ", email='" + email + '\'' +
                ", roleWithPrivileges='" + roleWithPrivileges + '\'' +
                '}';
    }
}
