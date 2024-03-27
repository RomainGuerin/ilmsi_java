module com.e3in.java {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.xml.bind;
    requires jakarta.activation;
    requires org.glassfish.jaxb.core;
    requires org.glassfish.jaxb.runtime;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.apache.poi.ooxml.schemas;

    opens com.e3in.java to javafx.fxml, jakarta.xml.bind;
    opens com.e3in.java.controller to javafx.fxml, jakarta.xml.bind;
    opens com.e3in.java.model to javafx.fxml, jakarta.xml.bind;

    exports com.e3in.java;
    exports com.e3in.java.controller;
    exports com.e3in.java.model;
    exports com.e3in.java.utils;
    opens com.e3in.java.utils to jakarta.xml.bind, javafx.fxml;
}
