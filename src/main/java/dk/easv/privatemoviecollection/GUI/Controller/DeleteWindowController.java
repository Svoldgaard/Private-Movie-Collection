package dk.easv.privatemoviecollection.GUI.Controller;

import dk.easv.privatemoviecollection.MovieMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DeleteWindowController {
    public void btnDelete1(ActionEvent actionEvent) {
    }

    public void btnDelete2(ActionEvent actionEvent) {
    }

    public void btnContinue(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MovieMain.class.getResource("/dk/easv/privatemoviecollection/FXML/MainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 611, 400);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
