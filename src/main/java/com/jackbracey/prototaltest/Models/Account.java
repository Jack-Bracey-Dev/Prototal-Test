package com.jackbracey.prototaltest.Models;

import org.springframework.data.annotation.Id;

public class Account {

    @Id
    private String id;

    private String firstName;

    private String surname;

    private String email;

    private String password;

    public Account(String id,
                   String firstName,
                   String surname,
                   String email,
                   String password) {
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public Account() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    @Override
    public String toString() {
        return String.format("Account[id=%s, firstName=%s, surname=%s, username=%s, password=%s",
                this.id, this.firstName, this.surname, this.email, this.password);
    }
}
