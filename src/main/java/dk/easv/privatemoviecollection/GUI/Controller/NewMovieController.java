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
import javafx.stage.Stage;

import java.net.ContentHandlerFactory;

import java.util.List;

public class NewMovieController {

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

    public void initialize(Object selectedTreeView) {

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

    @FXML
    private void btnSave(ActionEvent actionEvent) {
        try {
            String movieTitle = txtMovieTitle.getText().trim();
            Float rating = Float.parseFloat(txtRating.getText().trim());
            String selectedCategory = lstCategory.getSelectionModel().getSelectedItem();  // Get selected category

            if (movieTitle.isEmpty() || rating < 0 || selectedCategory == null) {
                showAlert("Error", "Please fill in all fields correctly.");
                return;
            }

            Movie newMovie = new Movie(0, movieTitle, rating, selectedCategory);

            MovieModel movieModel = new MovieModel();
            Movie savedMovie = movieModel.addMovie(newMovie);

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

    void setNewMovieController(TreeItem<String> selectedItem) {
    }

    void MovieMain(MovieController movieController) {
    }

    void setMovieController(MovieController movieController) {
    }

    void initializeForEdit(Object selectedTreeView) {
        String currentMovieTitle = txtMovieTitle.getText().trim();
        movie = (Movie) selectedTreeView;

    }


}
