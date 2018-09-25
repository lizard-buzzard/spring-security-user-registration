package com.lizard.buzzard.web.dto;

import com.lizard.buzzard.validation.CurrentUserPasswordValidator;
import com.lizard.buzzard.validation.PasswordChangeConfirmationValidator;
import com.lizard.buzzard.validation.PasswordConstraintValidator;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@PasswordChangeConfirmationValidator
public class ViewFormChangePassword {

    @CurrentUserPasswordValidator
    private String currentpassword;

    @NotNull
    @PasswordConstraintValidator
    private String newpassword;

    @NotNull
    @Size(min = 1, message="{dto.user.size.passwordConfirmation}")
    private String confirmedpassword;

    public ViewFormChangePassword() {
    }

    public String getCurrentpassword() {
        return currentpassword;
    }

    public void setCurrentpassword(String currentpassword) {
        this.currentpassword = currentpassword;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public String getConfirmedpassword() {
        return confirmedpassword;
    }

    public void setConfirmedpassword(String confirmedpassword) {
        this.confirmedpassword = confirmedpassword;
    }
}
