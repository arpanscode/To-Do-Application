module sample.todoapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.ooxml;
    requires java.desktop;


    opens sample.todoapp to javafx.fxml;
    exports sample.todoapp;
}
