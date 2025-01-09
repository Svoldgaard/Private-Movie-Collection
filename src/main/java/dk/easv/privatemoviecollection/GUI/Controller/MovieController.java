package dk.easv.privatemoviecollection.GUI.Controller;

// Project import
import dk.easv.privatemoviecollection.BE.Movie;
import dk.easv.privatemoviecollection.DLL.DBConnection.DB_Connect;
import dk.easv.privatemoviecollection.GUI.Model.MovieModel;
import dk.easv.privatemoviecollection.MovieMain;

// Java import
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.BreakIterator;
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

    private void initializeTreeView() {
        // Root item to represent the entire movie collection
        TreeItem<String> rootItem = new TreeItem<>("Movie Collection");
        rootItem.setExpanded(true);

        // Clear previous category nodes (if any)
        categoryNodes.clear();

        try {
            // Fetch all movies from the movie model
            List<Movie> movies = movieModel.getAllMovies();
            for (Movie movie : movies) {
                // Add each movie to its respective category
                addMovieToCategory(movie.getCategory(), movie.getName());
            }
        } catch (Exception e) {
            displayError(e);
        }

        // Add all category nodes to the root item
        categoryNodes.values().forEach(rootItem.getChildren()::add);

        // Set the root item for the TreeView
        treeView.setRoot(rootItem);
        treeView.setShowRoot(true);  // Optionally, show or hide the root (Movie Collection)
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


    void addMovieToCategory(String category, String movieTitle) {
        // Create a new movie node
        TreeItem<String> movieNode = new TreeItem<>(movieTitle);

        // If the category is not already in categoryNodes, create a new category node
        if (!categoryNodes.containsKey(category)) {
            TreeItem<String> categoryNode = new TreeItem<>(category);
            categoryNode.setExpanded(true);  // Make categories expandable (optional)
            categoryNodes.put(category, categoryNode);  // Add category node to the map
        }

        // Add the movie node to its respective category
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
    private void btnAddCategory(ActionEvent actionEvent) throws IOException {
        // Open a window to add a new category
        FXMLLoader fxmlLoader = new FXMLLoader(MovieMain.class.getResource("/dk/easv/privatemoviecollection/FXML/AddCategory.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 611, 400);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

        // After category is added, update the TreeView (this should be triggered from the AddCategory controller)
        initializeTreeView();
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

    /**
     *
     * @param actionEvent
     */
    @FXML
    private void btnDeleteMovie(ActionEvent actionEvent) {
        TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();

        if (selectedItem != null && selectedItem.getParent() != null) {
            String movieTitle = selectedItem.getValue();

            try {
                Movie movieToDelete = findMovieByTitle(movieTitle);

                if (movieToDelete != null) {
                    movieModel.deleteMovie(movieToDelete);  // Delete from the database
                    selectedItem.getParent().getChildren().remove(selectedItem);  // Remove from TreeView

                    showAlert("Success", "Movie removed: " + movieTitle);
                } else {
                    showAlert("Error", "Movie not found in database.");
                }

            } catch (Exception e) {
                displayError(e);
                e.printStackTrace();
            }
        } else {
            showAlert("Error", "Please select a movie to delete.");
        }
    }

    @FXML
    private void btnDeleteCategory(ActionEvent actionEvent) {
        TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();

        if (selectedItem != null && selectedItem.getParent() == treeView.getRoot()) {
            // Confirm that it's a category node
            String categoryName = selectedItem.getValue();

            // Check if there are any movies under the category
            if (selectedItem.getChildren().isEmpty()) {
                categoryNodes.remove(categoryName);  // Remove the category from the map
                treeView.getRoot().getChildren().remove(selectedItem);  // Remove the category from TreeView
                showAlert("Success", "Category removed: " + categoryName);
            } else {
                showAlert("Error", "Category cannot be deleted because it still has movies.");
            }
        } else {
            showAlert("Error", "Please select a category to delete.");
        }
    }


    private Movie findMovieByTitle(String title) {
        for (Movie movie : movieModel.getAllMovies()) {
            if (movie.getName().equalsIgnoreCase(title)) {
                return movie;
            }
        }
        return null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private boolean btnEditMovie() throws Exception {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dk/easv/privatemoviecollection/FXML/AddMovie.fxml"));
            Parent root = fxmlLoader.load();

            NewMovieController controller = fxmlLoader.getController();
            controller.MovieMain(this);
            controller.setMovieController(this);

            Stage stage = new Stage();

            Object ActionEvent = null;
            if (ActionEvent == null) {}
            Object selectedTreeView = null;
            {
                stage.setTitle("New Playlist");
                controller.initializeForEdit(selectedTreeView);
            }

            TreeItem<String> selectMovieManager = null;
            if (ActionEvent == null) {
                stage.setTitle("Edit Playlist");
                selectMovieManager = treeView.getSelectionModel().getSelectedItem();
            }

            if (selectMovieManager != null) {
                controller.initialize(selectedTreeView);
            } else {
                System.out.println("No Category is selected to edit!");
                return false;
            }
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();

            DB_Connect databaseConenctor = new DB_Connect();
            List<Movie> movies = DB_Connect.getAllMovies();

            ObservableList<Movie> MovieList = FXCollections.observableList(movies);

            ListView<Movie> txtMovieTitel = new ListView<>(MovieList);
            boolean Category = false;
            treeView.setEditable(Category);
        }
        return false;
    }


}

