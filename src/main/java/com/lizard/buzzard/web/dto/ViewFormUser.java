package com.lizard.buzzard.web.dto;


import com.lizard.buzzard.validation.EmailConstraintValidator;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ViewFormUser {
    @NotNull
    @Size(min=2, max=50, message="{dto.user.size.firstname}")
    private String firstname;

    @NotNull
    @Size(min=2, max=50, message="{dto.user.size.lastname}")
    private String lastname;

    // TODO:     @ValidPassword
    @NotNull
    private String password;

    @NotNull
    @Size(min = 1, message="{dto.user.size.passwordConfirmation}")
    private String passwordconfirmed;

    @EmailConstraintValidator
    private String email;

    private Integer role;

    public ViewFormUser() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordconfirmed() {
        return passwordconfirmed;
    }

    public void setPasswordconfirmed(String passwordconfirmed) {
        this.passwordconfirmed = passwordconfirmed;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
}
