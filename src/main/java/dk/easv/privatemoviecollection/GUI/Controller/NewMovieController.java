package dk.easv.privatemoviecollection.GUI.Controller;

//Project import
import dk.easv.privatemoviecollection.BE.Movie;
import dk.easv.privatemoviecollection.BE.Category;  // Add this import for Category
import dk.easv.privatemoviecollection.GUI.Model.CategoryModel;  // Add import for CategoryModel
import dk.easv.privatemoviecollection.GUI.Model.MovieModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.EventObject;
import java.util.List;

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

    private CategoryModel categoryModel;
    private MovieController movieController;

    public void initialize() {

        categoryList = FXCollections.observableArrayList();

        try {
            categoryModel = new CategoryModel();
            List<Category> categories = categoryModel.getAllCategories();

            for (Category category : categories) {
                categoryList.add(category.getName());
            }

            lstCategory.setItems(categoryList);

        } catch (Exception e) {
            showAlert("Error", "Failed to load categories from the database.");
            e.printStackTrace();
        }
    }

    public void setMovieController(MovieController movieController) {
        this.movieController = movieController;
    }

    @FXML
    private void btnSave(ActionEvent actionEvent) {
        // To save when making a new movie
        try {
            String movieTitle = txtMovieTitle.getText().trim();
            float rating = Float.parseFloat(txtRating.getText().trim());
            ObservableList<String> selectedCategories = lstCategory.getSelectionModel().getSelectedItems();
            String fileLink = txtFileLink.getText().trim();  // Get the file link

            if (movieTitle.isEmpty() || selectedCategories.isEmpty() || rating < 0 || fileLink.isEmpty()) {
                showAlert("Error", "Please fill in all fields correctly and select at least one category.");
                return;
            }

            // Create the first category (adjust for multiple categories if needed)
            Category category = new Category();
            category.setName(selectedCategories.get(0));

            // Create the Movie object with the file link
            Movie newMovie = new Movie(0, movieTitle, rating, 0.0f, category);
            newMovie.setFileLink(fileLink);  // Set the file link for the movie

            // Save the movie
            MovieModel movieModel = new MovieModel();
            Movie savedMovie = movieModel.addMovie(newMovie);

            // Refresh the movie list in the MovieController
            if (movieController != null) {
                movieController.refreshMovie();
            }

            // Close the add movie window
            ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
            showAlert("Success", "Movie added successfully!");

        } catch (NumberFormatException e) {
            showAlert("Error", "Rating must be a valid number.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to add movie.");
        }
        // To Save when movie has been edited
        if(movie != null){
            movie.setName(txtMovieTitle.getText());
            movie.setRating(Float.parseFloat(txtRating.getText()));
            movie.setFileLink(txtFileLink.getText());

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

    @FXML
    private void btnFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Video file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.mpeg4"));
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            // Get the file path and move it to the dedicated resources folder
            String userMoviesDirectory = "src/main/resources/dk/easv/privatemoviecollection/Movie";  // Folder inside resources
            File movieDir = new File(userMoviesDirectory);
            if (!movieDir.exists()) {
                movieDir.mkdirs();  // Create directory if it doesn't exist
            }

            // Copy the file to the Movies directory
            File destinationFile = new File(movieDir, file.getName());
            try {
                Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                txtFileLink.setText("dk/easv/privatemoviecollection/Movie/" + file.getName()); // Relative path to resources
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to copy the file.");
            }
        }
    }

    void setMovie(Movie selectedMovie) {
        this.movie = selectedMovie;
        txtMovieTitle.setText(movie.getName());
        txtRating.setText(String.valueOf(movie.getRating()));
        txtFileLink.setText(movie.getFileLink());

    }



}
