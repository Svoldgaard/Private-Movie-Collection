package dk.easv.privatemoviecollection.GUI.Controller;

// Java import
import dk.easv.privatemoviecollection.BE.Movie;
import dk.easv.privatemoviecollection.MovieMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MovieController {

    @FXML
    private TextField txtSearch;

    @FXML
    private TreeView treeView;

    @FXML
    private Slider movieDuration;

    @FXML
    private void btnAddMovie(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MovieMain.class.getResource("AddMovie.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 611, 400);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void btnDeleteMovie(ActionEvent actionEvent) {
    }

    @FXML
    private void btnAddCategory(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MovieMain.class.getResource("AddCategory.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 611, 400);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void btnDeleteCategory(ActionEvent actionEvent) {
    }

    @FXML
    private void btnAddRating(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MovieMain.class.getResource("AddRating.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 611, 400);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void btnEditRating(ActionEvent actionEvent) throws IOException {
           }

    @FXML
    private void btnPlayPause(ActionEvent actionEvent) {
    }
}