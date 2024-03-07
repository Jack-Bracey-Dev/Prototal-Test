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

    public String checkForErrors() {
        StringBuilder builder = new StringBuilder();

        if (Strings.isBlank(this.currentPassword))
            builder.append("Missing element 'currentPassword'").append("\n");

        if (Strings.isBlank(this.newPassword))
            builder.append("Missing element 'newPassword'").append("\n");

        if (Strings.isBlank(this.newPasswordCopy))
            builder.append("Missing element 'newPasswordCopy'").append("\n");

        if (Strings.isNotBlank(this.newPassword) && Strings.isNotBlank(this.newPasswordCopy))
            if (!this.newPassword.equals(this.newPasswordCopy))
                builder.append("'newPassword' and 'newPasswordCopy' do not match");

        return builder.toString();
    }
}
