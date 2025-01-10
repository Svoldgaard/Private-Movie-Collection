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
import java.net.ContentHandlerFactory;

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
    private Object treeView;

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
        try {
            String movieTitle = txtMovieTitle.getText().trim();
            float rating = Float.parseFloat(txtRating.getText().trim());
            ObservableList<String> selectedCategories = lstCategory.getSelectionModel().getSelectedItems();

            if (movieTitle.isEmpty() || selectedCategories.isEmpty() || rating < 0) {
                showAlert("Error", "Please fill in all fields correctly and select at least one category.");
                return;
            }

            // Create the first category (adjust for multiple categories if needed)
            Category category = new Category();
            category.setName(selectedCategories.get(0));

            // Create the Movie object
            Movie newMovie = new Movie(0, movieTitle, rating, 0.0f, category); // Adjust personalRating as needed

            // Save the movie
            MovieModel movieModel = new MovieModel();
            Movie savedMovie = movieModel.addMovie(newMovie);

            // Refresh the movie list in the MovieController
            if (movieController != null) {
                movieController.refreshMovie();
            }

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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Video file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.mpeg4"));
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            txtFileLink.setText(file.getAbsolutePath());
        }
    }


    public void MovieMain(MovieController movieController) {
    }

}
