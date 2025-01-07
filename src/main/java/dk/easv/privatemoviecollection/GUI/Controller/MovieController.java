package dk.easv.privatemoviecollection.GUI.Controller;

// Project import
import dk.easv.privatemoviecollection.MovieMain;

// Java import
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class MovieController {

    @FXML
    private TextField txtSearch;

    @FXML
    private TreeView<String> treeView;

    @FXML
    private Slider movieDuration;


    private Map<String, TreeItem<String>> categoryNodes = new HashMap<>();


    public MovieController() {

    }

    public void initialize() {
        initializeTreeView();
    }

    // test data to show it works
    // this has to be removed / changed when CRUD is working

    private void initializeTreeView() {
        // Root node for the entire tree
        TreeItem<String> rootItem = new TreeItem<>("Movie Collection");
        rootItem.setExpanded(true);

        // Sample categories and movies
        addMovieToCategory("Action", "Die Hard");
        addMovieToCategory("Action", "Mad Max");
        addMovieToCategory("Comedy", "The Hangover");
        addMovieToCategory("Horror", "The Conjuring");

        // Add categories to the root node
        categoryNodes.values().forEach(rootItem.getChildren()::add);

        // Set root to the TreeView
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