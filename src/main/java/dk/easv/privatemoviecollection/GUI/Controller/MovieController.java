package dk.easv.privatemoviecollection.GUI.Controller;

// Project imports
import dk.easv.privatemoviecollection.BE.Category;
import dk.easv.privatemoviecollection.BE.Movie;
import dk.easv.privatemoviecollection.GUI.Model.CategoryModel;
import dk.easv.privatemoviecollection.DLL.DBConnection.DB_Connect;
import dk.easv.privatemoviecollection.GUI.Model.MovieModel;
import dk.easv.privatemoviecollection.MovieMain;

// Java imports
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
    private TreeView<Object> treeView;

    @FXML
    private Slider movieDuration;

    private MovieModel movieModel;
    private CategoryModel categoryModel;

    private final Map<String, TreeItem<Object>> categoryNodes = new HashMap<>();

    public MovieController() {
        try {
            movieModel = new MovieModel();
            categoryModel = new CategoryModel();

            categoryModel.loadAllCategories();
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
    }

    private void displayError(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong");
        alert.setHeaderText(null);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    public void initialize() {
        try {
            initializeTreeView();
        } catch (Exception e) {
            displayError(e);
        }


        txtSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                List<Movie> searchResults = movieModel.searchMovie(newValue);
                updateTreeView(searchResults);
            } catch (Exception e) {
                displayError(e);
                e.printStackTrace();
            }
        });
    }

    private void initializeTreeView() throws Exception {
        TreeItem<Object> rootItem = new TreeItem<>("Movie Collection");
        rootItem.setExpanded(true);

        categoryNodes.clear();

        List<Category> categories = categoryModel.getAllCategories();
        for (Category category : categories) {
            TreeItem<Object> categoryNode = new TreeItem<>(category);
            categoryNode.setExpanded(true);
            categoryNodes.put(category.getName(), categoryNode);
            rootItem.getChildren().add(categoryNode);
        }

        List<Movie> movies = movieModel.getAllMovies();
        for (Movie movie : movies) {

            addMovieToCategory(movie.getCategory(), movie.getName());
        }

        treeView.setRoot(rootItem);
        treeView.setShowRoot(true);

        treeView.setCellFactory(tv -> new TreeCell<>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else if (item instanceof Movie) {
                    setText(((Movie) item).getName());
                } else if (item instanceof Category) {

                    Category category = (Category) item;
                    if (category != null && category.getName() != null) {
                        setText(category.getName());
                    } else {
                        setText("No Category");
                    }
                } else {
                    setText(item.toString());
                }
            }
        });
    }



    private void updateTreeView(List<Movie> movies) {
        TreeItem<Object> rootItem = new TreeItem<>("Movie Collection");
        rootItem.setExpanded(true);

        categoryNodes.clear();

        for (Movie movie : movies) {
            addMovieToCategory(movie.getCategory(), String.valueOf(movie));
        }


        categoryNodes.values().forEach(rootItem.getChildren()::add);

        treeView.setRoot(rootItem);
        treeView.setShowRoot(true);
    }

    void addMovieToCategory(Category category, String movie) {
        TreeItem<Object> movieNode = new TreeItem<>(movie);

        categoryNodes.computeIfAbsent(category.getName(), name -> {
            TreeItem<Object> categoryNode = new TreeItem<>(category);
            categoryNode.setExpanded(true);
            return categoryNode;
        });

        categoryNodes.get(category.getName()).getChildren().add(movieNode);
    }

    @FXML
    private void btnAddMovie(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MovieMain.class.getResource("/dk/easv/privatemoviecollection/FXML/AddMovie.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void btnAddCategory(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MovieMain.class.getResource("/dk/easv/privatemoviecollection/FXML/AddCategory.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();


        try {
            categoryModel.loadAllCategories();
            initializeTreeView();
        } catch (Exception e) {
            displayError(e);
        }
    }

    @FXML
    private void btnDeleteMovie(ActionEvent actionEvent) {
        TreeItem<Object> selectedItem = treeView.getSelectionModel().getSelectedItem();

        if (selectedItem != null && selectedItem.getValue() instanceof Movie) {
            Movie selectedMovie = (Movie) selectedItem.getValue();
            try {
                movieModel.deleteMovie(selectedMovie);
                selectedItem.getParent().getChildren().remove(selectedItem);
                showAlert("Success", "Movie removed: " + selectedMovie.getName());
            } catch (Exception e) {
                displayError(e);
                e.printStackTrace();
            }
        } else {
            showAlert("Error", "Please select a movie to delete.");
        }
    }

    @FXML
    private void btnEditRating(ActionEvent actionEvent) {
    }

    @FXML
    private void btnPlayPause(ActionEvent actionEvent) {
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


    public void btnDeleteCategory(ActionEvent actionEvent) {
    }

    public void btnAddRating(ActionEvent actionEvent) {
    }
}