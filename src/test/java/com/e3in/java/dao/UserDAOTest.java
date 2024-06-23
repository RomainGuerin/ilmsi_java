package com.e3in.java.dao;

import com.e3in.java.model.User;
import com.e3in.java.utils.Common;
import com.e3in.java.utils.Constants;
import javafx.scene.control.Alert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDAOTest {

    @Mock
    private DAOManager daoManager;

    private UserDAO userDAO;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDAO = new UserDAO(daoManager);
        user = new User("test@example.com", "password123", false);
    }

    @Test
    void testGetUserByEmailPassword_valid() {
        HashMap<String, String> resultQuery = getResultQuerySelectCall();

        when(daoManager.select(eq(Constants.USER), anyList(), any(HashMap.class))).thenReturn(resultQuery);

        User result = userDAO.getUserByEmailPassword(user);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("test@example.com", result.getEmail());
        assertFalse(result.isAdmin());
    }

    @Test
    void testGetUserByEmailPassword_invalidPassword() {
        User userTemp = new User("test@example.com", "wrongpassword");
        HashMap<String, String> resultQuery = getResultQuerySelectCall();

        when(daoManager.select(eq(Constants.USER), anyList(), any(HashMap.class))).thenReturn(resultQuery);

        try (MockedStatic<Common> mockedCommon = mockStatic(Common.class)) {
            User result = userDAO.getUserByEmailPassword(userTemp);
            assertNull(result);
            mockedCommon.verify(() -> Common.showAlert(eq(Alert.AlertType.ERROR), anyString(), anyString()), times(1));
        }
    }

    @Test
    void testGetUserByEmailPassword_invalidUser() {
        when(daoManager.select(eq(Constants.USER), anyList(), any(HashMap.class))).thenReturn(new HashMap<>());

        try (MockedStatic<Common> mockedCommon = mockStatic(Common.class)) {
            User result = userDAO.getUserByEmailPassword(user);
            assertNull(result);
            mockedCommon.verify(() -> Common.showAlert(eq(Alert.AlertType.ERROR), anyString(), anyString()), times(1));
        }
    }

    @Test
    void testCreateUser_success() {
        LinkedHashMap<String, String> userData = new LinkedHashMap<>();
        userData.put(Constants.EMAIL, user.getEmail());
        userData.put(Constants.PASSWORD, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        userData.put(Constants.TYPE, user.isAdmin() ? Constants.ADMIN : Constants.USER);

        HashMap<String, String> resultQuery = new HashMap<>();
        resultQuery.put(Constants.STATUS, Constants.SUCCESS);

        when(daoManager.insert(eq(Constants.USER), any(LinkedHashMap.class))).thenReturn(resultQuery);

        try (MockedStatic<Common> mockedCommon = mockStatic(Common.class)) {
            boolean result = userDAO.createUser(user);
            assertTrue(result);
            mockedCommon.verify(() -> Common.showAlert(eq(Alert.AlertType.INFORMATION), anyString(), anyString()), times(1));
        }
    }

    @Test
    void testCreateUser_failure() {
        when(daoManager.insert(eq(Constants.USER), any(LinkedHashMap.class))).thenReturn(new HashMap<>());

        try (MockedStatic<Common> mockedCommon = mockStatic(Common.class)) {
            boolean result = userDAO.createUser(user);
            assertFalse(result);
            mockedCommon.verify(() -> Common.showAlert(eq(Alert.AlertType.ERROR), anyString(), anyString()), times(1));
        }
    }

    @Test
    void testUpdatePassword_success() {
        user.setPassword("newpassword123");
        HashMap<String, String> resultQuery = new HashMap<>();
        resultQuery.put(Constants.STATUS, Constants.SUCCESS);

        when(daoManager.update(eq(Constants.USER), any(HashMap.class), any(HashMap.class))).thenReturn(resultQuery);

        boolean result = userDAO.updatePassword(user);
        assertTrue(result);
    }

    @Test
    void testUpdatePassword_failure() {
        user.setPassword("newpassword123");

        when(daoManager.update(eq(Constants.USER), any(HashMap.class), any(HashMap.class))).thenReturn(new HashMap<>());

        boolean result = userDAO.updatePassword(user);
        assertFalse(result);
    }

    private HashMap<String, String> getResultQuerySelectCall() {
        HashMap<String, String> resultQuery = new HashMap<>();
        resultQuery.put(Constants.ID, "1");
        resultQuery.put(Constants.EMAIL, user.getEmail());
        resultQuery.put(Constants.PASSWORD, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        resultQuery.put(Constants.TYPE, Constants.USER);
        return resultQuery;
    }

}
