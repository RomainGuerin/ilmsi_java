module com.e3in.java {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.xml.bind;
    requires jakarta.activation;
    requires org.glassfish.jaxb.core;
    requires org.glassfish.jaxb.runtime;

    opens com.e3in.java to javafx.fxml, jakarta.xml.bind;
    opens com.e3in.java.controller to javafx.fxml, jakarta.xml.bind;
    opens com.e3in.java.model to javafx.fxml, jakarta.xml.bind;

    exports com.e3in.java;
    exports com.e3in.java.controller;
    exports com.e3in.java.model;
}
