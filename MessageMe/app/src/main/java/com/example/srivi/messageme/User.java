package com.example.srivi.messageme;

/**
 * Created by srivi on 25-04-2018.
 */

public class User {
    String firstName;
    String lastName;
    String email;
    String userId;

    public User() {
    }

    public User(String firstName, String lastName, String email, String userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userId = userId;
    }
}
