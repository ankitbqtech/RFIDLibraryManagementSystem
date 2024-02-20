module com.example.rfidlibrarymanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires Reader.API;
    requires jdemo;
    requires com.fasterxml.jackson.core;

    opens com.example.rfidlibrarymanagementsystem to javafx.fxml;
    exports com.example.rfidlibrarymanagementsystem;
}