module com.example.java {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;

    opens com.e3in.java to javafx.fxml;

    exports com.e3in.java;
    exports com.e3in.java.controller;
    opens com.e3in.java.controller to javafx.fxml;
    exports com.e3in.java.model;
    opens com.e3in.java.model to javafx.fxml;
}