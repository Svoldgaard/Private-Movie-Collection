package dk.easv.privatemoviecollection.GUI.Controller;

// Project import
import dk.easv.privatemoviecollection.BE.Movie;
import dk.easv.privatemoviecollection.GUI.Model.MovieModel;
import dk.easv.privatemoviecollection.MovieMain;

// Java import
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MovieController {

    @FXML
    private TextField txtSearch;

    @FXML
    private TreeView<String> treeView;

    @FXML
    private Slider movieDuration;

    private MovieModel movieModel;


    private Map<String, TreeItem<String>> categoryNodes = new HashMap<>();


    public MovieController() {
        try {
            movieModel = new MovieModel();
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
    }

    private void displayError(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong");
        alert.setHeaderText("");
        alert.showAndWait();

    }

    public void initialize() {
        initializeTreeView();

        // Listen to search input and update the tree dynamically
        txtSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                List<Movie> searchResults = movieModel.searchMovie(newValue);
                updateTreeView(searchResults);  // Update tree with search results
            } catch (Exception e) {
                displayError(e);
                e.printStackTrace();
            }
        });
    }

    // test data to show it works
    // this has to be removed / changed when CRUD is working

    private void initializeTreeView() {
        TreeItem<String> rootItem = new TreeItem<>("Movie Collection");
        rootItem.setExpanded(true);
        categoryNodes.clear();  // Clear previous nodes to avoid duplication

        try {
            List<Movie> movies = movieModel.getAllMovies();
            for (Movie movie : movies) {
                addMovieToCategory(movie.getCategory(), movie.getName());
            }
        } catch (Exception e) {
            displayError(e);
        }

        categoryNodes.values().forEach(rootItem.getChildren()::add);
        treeView.setRoot(rootItem);
        treeView.setShowRoot(true);
    }

    private void updateTreeView(List<Movie> movies) {
        TreeItem<String> rootItem = new TreeItem<>("Movie Collection");
        rootItem.setExpanded(true);
        categoryNodes.clear();  // Clear existing categories

        for (Movie movie : movies) {
            addMovieToCategory(movie.getCategory(), movie.getName());
        }

        categoryNodes.values().forEach(rootItem.getChildren()::add);
        treeView.setRoot(rootItem);
        treeView.setShowRoot(true);
    }

    /*
    this addMovieToCategory has to be changed when CRUD is implemented to take the correct info 
     */

    public void addMovieToCategory(String category, String movieTitle) {
        TreeItem<String> movieNode = new TreeItem<>(movieTitle);

        // Check if category exists, otherwise create it
        if (!categoryNodes.containsKey(category)) {
            TreeItem<String> categoryNode = new TreeItem<>(category);
            categoryNodes.put(category, categoryNode);
        }

        // Add movie under the correct category
        categoryNodes.get(category).getChildren().add(movieNode);
    }

    @FXML
    private void btnAddMovie(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MovieMain.class.getResource("/dk/easv/privatemoviecollection/FXML/AddMovie.fxml"));
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
        FXMLLoader fxmlLoader = new FXMLLoader(MovieMain.class.getResource("/dk/easv/privatemoviecollection/FXML/AddCategory.fxml"));
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
        FXMLLoader fxmlLoader = new FXMLLoader(MovieMain.class.getResource("/dk/easv/privatemoviecollection/FXML/AddRating.fxml"));
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