package com.e3in.java.utils;

import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CommonTest {

    @Test
    void getAboutViewFXML() {
        URL resource = getClass().getResource("/view/AboutView.fxml");
        assertNotNull(resource, "AboutView.fxml devrait être présent dans les fichiers de resources");
    }

    @Test
    void testGetCurrentDateTime() {
        String dateTime = Common.getCurrentDateTime();
        assertNotNull(dateTime);
        assertEquals(16, dateTime.length());
    }
}
