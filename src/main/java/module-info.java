module com.example.todoapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens com.example.todoapp to javafx.fxml, com.fasterxml.jackson.databind;
    exports com.example.todoapp;
}