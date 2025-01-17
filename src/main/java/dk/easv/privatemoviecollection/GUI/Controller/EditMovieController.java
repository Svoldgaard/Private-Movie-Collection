package dk.easv.privatemoviecollection.GUI.Controller;

import dk.easv.privatemoviecollection.BE.Category;
import dk.easv.privatemoviecollection.BE.Movie;
import dk.easv.privatemoviecollection.GUI.Model.CategoryModel;
import dk.easv.privatemoviecollection.GUI.Model.MovieModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

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
    private ListView<String> lstCategory;
    private ObservableList<String> categoryList;
    private CategoryModel categoryModel;

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


