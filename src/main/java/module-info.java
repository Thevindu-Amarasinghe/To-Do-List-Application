module com.example.iit01 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.iit01 to javafx.fxml;
    exports com.example.iit01;
}