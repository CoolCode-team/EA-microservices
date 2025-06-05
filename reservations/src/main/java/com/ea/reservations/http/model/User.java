package com.ea.reservations.http.model;

import lombok.Getter;

@Getter
public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;

    public User(String id, String firstName, String lastName, String email, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
