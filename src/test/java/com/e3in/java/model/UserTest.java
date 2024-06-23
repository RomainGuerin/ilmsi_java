package com.e3in.java.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testGetSetId() {
        user.setId(2);
        assertEquals(2, user.getId());
    }

    @Test
    void testGetSetEmail() {
        user.setEmail("newemail@example.com");
        assertEquals("newemail@example.com", user.getEmail());
    }

    @Test
    void testGetSetPassword() {
        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPassword());
    }

    @Test
    void testIsAdmin_valid() {
        User newUser = new User(4, "newadmin@example.com", true);
        assertEquals(4, newUser.getId());
        assertEquals("newadmin@example.com", newUser.getEmail());
        assertTrue(newUser.isAdmin());
    }

    @Test
    void testIsAdmin_invalid() {
        assertFalse(user.isAdmin());
    }

    @Test
    void testIfEmailPasswordAreValid_valid() {
        User newUser = new User("newuser@", "password", false);
        assertFalse(newUser.isEmailInvalid());
        assertFalse(newUser.isPasswordInvalid());
    }

    @Test
    void testIfEmailPasswordAreValid_invalid() {
        User newUser = new User("newuser", "");
        assertEquals("newuser", newUser.getEmail());
        assertTrue(newUser.isEmailInvalid());
        assertEquals("", newUser.getPassword());
        assertTrue(newUser.isPasswordInvalid());
    }
}