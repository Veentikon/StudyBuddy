module com.example.studyboddy {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.studyboddy to javafx.fxml;
    exports com.example.studyboddy;
}