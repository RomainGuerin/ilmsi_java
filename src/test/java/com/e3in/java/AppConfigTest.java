package com.e3in.java;

import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AppConfigTest {

    @Test
    void getFileXmlDAO() {
        URL resource = getClass().getResource("/xmlDAO.xml");
        assertNotNull(resource, "xmlDAO.xml devrait être présent dans les fichiers de resources");
    }
}
