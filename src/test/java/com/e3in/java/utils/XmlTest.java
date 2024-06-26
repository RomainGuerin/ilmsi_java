package com.e3in.java.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XmlTest {

    @Test
    void testValidateXml_validXml() {
        String xmlFilePath = "src/test/resources/xmlDAO.xml";
        assertTrue(Xml.validateXml(xmlFilePath));
    }

    @Test
    void testValidateXml_invalidXml() {
        String xmlFilePath = "src/test/resources/xmlDAO_invalid.xml";
        assertFalse(Xml.validateXml(xmlFilePath));
    }
}
