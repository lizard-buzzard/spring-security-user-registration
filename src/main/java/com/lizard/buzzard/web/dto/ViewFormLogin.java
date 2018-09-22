package com.lizard.buzzard.web.dto;

import javax.validation.constraints.Size;

public class ViewFormLogin {

    //    @Email
    @Size(min = 2, max = 50, message = "{dto.user.size.firstname}")
    private String email;

    //    @NotNull
    @Size(min = 2, max = 50, message = "{dto.user.size.lastname}")
    private String password;

    public ViewFormLogin() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
