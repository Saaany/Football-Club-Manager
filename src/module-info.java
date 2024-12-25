module myjfx {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    opens sample to javafx.graphics, javafx.fxml;
    opens sample.Controllers to javafx.fxml, javafx.graphics;
}