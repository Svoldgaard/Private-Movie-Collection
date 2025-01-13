package dk.easv.privatemoviecollection.GUI.Controller;

import dk.easv.privatemoviecollection.BE.Category;
import dk.easv.privatemoviecollection.BE.Movie;
import dk.easv.privatemoviecollection.GUI.Model.MovieModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class EditMovieController {

    private MovieModel movieModel;
    private Movie movie;

    @FXML
    private TextField txtEditRating;
    @FXML
    private TextField txtEditMovieTitle;
    @FXML
    private TextField txtEditFileLink;
    @FXML
    private ListView<Category> lstCategory;


    public void setMovie(MovieModel movieModel, Movie movie) {
        this.movieModel = movieModel;
        this.movie = movie;

        txtEditMovieTitle.setText(movie.getName());
        txtEditRating.setText(String.valueOf(movie.getRating()));
        txtEditFileLink.setText(movie.getFileLink());

    }

    @FXML
    private void btnSave(ActionEvent actionEvent) throws Exception {

        if(movie != null){
            // Update the movie object with new values
             movie.setName(txtEditMovieTitle.getText());
             movie.setRating(Float.parseFloat(txtEditRating.getText()));
             movie.setFileLink(txtEditFileLink.getText());
             movie.setCategory(new Category(txtEditMovieTitle.getText()));

            // Notify the model about the update
            movieModel.updateMovie(movie);

            // Notify the observable list that the data has changed
            /*ObservableList<Movie> movies = movieModel.getObservableMovies();
            int index = movie.indexOf(movie);
            if (index != -1){
                // Replace the old instance in the observable list
                movies.set(index, movie);
            }*/

            ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();

        }
    }

    @FXML
    private void btnCancel(ActionEvent actionEvent) {
        ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void btnFile(ActionEvent actionEvent) {
    }
}


