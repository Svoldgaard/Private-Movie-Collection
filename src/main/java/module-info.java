module dk.easv.privatemoviecollection {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;  // Add this line

    opens dk.easv.privatemoviecollection to javafx.fxml;
    exports dk.easv.privatemoviecollection;
    exports dk.easv.privatemoviecollection.GUI.Controller;
    opens dk.easv.privatemoviecollection.GUI.Controller to javafx.fxml;
    exports dk.easv.privatemoviecollection.GUI.Model;
    opens dk.easv.privatemoviecollection.GUI.Model to javafx.fxml;
}
