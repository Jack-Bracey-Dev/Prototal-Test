package com.jackbracey.prototaltest.Pojo;

import org.apache.logging.log4j.util.Strings;

public class PasswordChangeDetails {

    private String currentPassword;

    private String newPassword;

    private String newPasswordCopy;

    public PasswordChangeDetails(String currentPassword, String newPassword, String newPasswordCopy) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.newPasswordCopy = newPasswordCopy;
    }

    public PasswordChangeDetails() {
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordCopy() {
        return newPasswordCopy;
    }

    public void setNewPasswordCopy(String newPasswordCopy) {
        this.newPasswordCopy = newPasswordCopy;
    }

    public boolean isValid() {
        return Strings.isNotBlank(this.currentPassword)
                && Strings.isNotBlank(this.newPassword)
                && Strings.isNotBlank(this.newPasswordCopy)
                && this.newPassword.equals(this.newPasswordCopy);
    }
}
