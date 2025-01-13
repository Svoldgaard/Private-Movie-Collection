package dk.easv.privatemoviecollection.GUI.Controller;

import dk.easv.privatemoviecollection.BE.Category;
import dk.easv.privatemoviecollection.BE.Movie;
import dk.easv.privatemoviecollection.GUI.Model.MovieModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class EditMovieController {

private Movie movie;

    @FXML
    private TextField txtEditRating;
    @FXML
    private TextField txtEditMovieTitle;
    @FXML
    private TextField txtEditFileLink;

private void btnSave(ActionEvent event) throws Exception {

    if(movie != null){
        movie.setName(txtEditMovieTitle.getText());
        movie.setRating(Float.parseFloat(txtEditRating.getText()));
        movie.setFileLink(txtEditFileLink.getText());
        movie.setCategory(new Category(txtEditMovieTitle.getText()));

        MovieModel movieModel = new MovieModel();
        movieModel.updateMovie(movie);
    }
}

    private void btnCancel(javafx.event.ActionEvent actionEvent) {
        ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
    }

}


