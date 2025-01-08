package dk.easv.privatemoviecollection.GUI.Controller;

//Project import
import dk.easv.privatemoviecollection.BE.Movie;
// Java import
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

import static java.lang.Float.*;


public class NewMovieController {

    public TextField txtFileLink;
    @FXML
    private TextField txtRating;
    @FXML
    private TextField txtMovieTitle;
    @FXML
    private ListView<String> lstCategory;

    private Movie movie;
    private ObservableList<String> categoryList;


    public void initialize() {

        categoryList = FXCollections.observableArrayList();

        // much data to check if it works
        categoryList.addAll("Action", "Comedy", "Drama", "Horror", "Sci-Fi");

        lstCategory.setItems(categoryList);
    }

    @FXML
    private void btnSave(ActionEvent actionEvent) {
        try {
            // Collect data from text fields
            String movieTitle = txtMovieTitle.getText().trim();
            Float rating = Float.parseFloat(txtRating.getText().trim());

            // Basic validation
            if (movieTitle.isEmpty() || rating < 0) {
                showAlert("Error", "Please fill in all fields correctly.");
                return;
            }

            // Create new movie object
            Movie newMovie = new Movie(0, movieTitle, rating);

            // Save movie to database
            MovieModel movieModel = new MovieModel();
            Movie savedMovie = movieModel.addMovie(newMovie);

            // Update TreeView by notifying the main controller
            MovieController mainController = new MovieController();
            mainController.addMovieToCategory(savedMovie.getCategory(), savedMovie.getName());

            // Close the window after successful save
            ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();

            showAlert("Success", "Movie added successfully!");

        } catch (NumberFormatException e) {
            showAlert("Error", "Rating must be a valid number.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to add movie.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void btnCancel(ActionEvent actionEvent) {
        ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
    }

    public void btnFile(ActionEvent actionEvent) {
    }
}
