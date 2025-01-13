package dk.easv.privatemoviecollection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MovieMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MovieMain.class.getResource("/dk/easv/privatemoviecollection/FXML/MainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 950, 432);
        stage.setTitle("Private Movie Collection");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

    }

    public static void main(String[] args) {
        launch();
        System.out.println("we are ready");
    }
}