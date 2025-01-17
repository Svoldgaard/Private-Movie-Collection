package dk.easv.privatemoviecollection.GUI.Controller;

//Project import
import dk.easv.privatemoviecollection.BE.Movie;
import dk.easv.privatemoviecollection.BE.Category;
import dk.easv.privatemoviecollection.GUI.Model.CategoryModel;
import dk.easv.privatemoviecollection.GUI.Model.MovieModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class NewMovieController {

    @FXML
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
            lstCategory.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

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
        try {
            String movieTitle = txtMovieTitle.getText().trim();
            float rating = Float.parseFloat(txtRating.getText().trim());
            ObservableList<String> selectedCategories = lstCategory.getSelectionModel().getSelectedItems();
            String fileLink = txtFileLink.getText().trim();  // Get the file link

            if (movieTitle.isEmpty() || selectedCategories.isEmpty() || rating < 0 || fileLink.isEmpty()) {
                showAlert("Error", "Please fill in all fields correctly and select at least one category.");
                return;
            }

            // Create a list of categories based on selected categories
            List<Category> categoryList = new ArrayList<>();
            for (String categoryName : selectedCategories) {
                Category category = new Category();
                category.setName(categoryName);
                categoryList.add(category);
            }

            // Create the Movie object with the file link and multiple categories
            Movie newMovie = new Movie(0, movieTitle, rating, 0.0f, categoryList);
            newMovie.setFileLink(fileLink);

            // Save the movie
            MovieModel movieModel = new MovieModel();
            movieModel.addMovie(newMovie);

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
