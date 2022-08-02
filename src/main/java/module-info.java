module nguyenhoa.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    opens nguyenhoa.app to javafx.fxml;
    exports nguyenhoa.app;
}