module dk.easv.privatemoviecollection {
    requires javafx.controls;
    requires javafx.fxml;


    opens dk.easv.privatemoviecollection to javafx.fxml;
    exports dk.easv.privatemoviecollection;
    exports dk.easv.privatemoviecollection.GUI;
    opens dk.easv.privatemoviecollection.GUI to javafx.fxml;
    exports dk.easv.privatemoviecollection.GUI.Model;
    opens dk.easv.privatemoviecollection.GUI.Model to javafx.fxml;
}