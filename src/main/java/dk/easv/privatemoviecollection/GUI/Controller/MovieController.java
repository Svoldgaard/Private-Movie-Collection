package dk.easv.privatemoviecollection.GUI.Controller;

// Project imports
import dk.easv.privatemoviecollection.BE.Category;
import dk.easv.privatemoviecollection.BE.Movie;
import dk.easv.privatemoviecollection.GUI.Model.CategoryModel;
import dk.easv.privatemoviecollection.DLL.DBConnection.DB_Connect;
import dk.easv.privatemoviecollection.GUI.Model.MovieModel;
import dk.easv.privatemoviecollection.MovieMain;

// Java imports
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private Slider movieDuration;

    @FXML
    private ListView<Category> lstCategory;

    @FXML
    private TableView<Movie> tblMovie;

    private MovieModel movieModel;
    private CategoryModel categoryModel;
    @FXML
    private TableColumn<Movie, Integer> colID;
    @FXML
    private TableColumn<Movie, String> colName;
    @FXML
    private TableColumn<Movie, Float> colRating;
    @FXML
    private TableColumn<Movie, String> colCategory;
    @FXML
    private TableColumn<Movie, Float> colPRating;

    public MovieController() {
        try {
            movieModel = new MovieModel();
            categoryModel = new CategoryModel();

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

        txtSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                List<Movie> searchResults = movieModel.searchMovie(newValue);
            } catch (Exception e) {
                displayError(e);
                e.printStackTrace();
            }
        });

        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("IMDB Rating"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("Category"));
        colPRating.setCellValueFactory(new PropertyValueFactory<>("Personal Rating"));

        tblMovie.setItems(movieModel.getObservableMovies());
        lstCategory.setItems(categoryModel.getObservableCategory());


    }

    public void refreshMovie() {
        tblMovie.setItems(movieModel.getObservableMovies());
    }

    public void refreshCategory(){

        lstCategory.setItems(categoryModel.getObservableCategory());
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

    }

    @FXML
    private void btnDeleteMovie(ActionEvent actionEvent) throws Exception {
        Movie selectedMovie = tblMovie.getSelectionModel().getSelectedItem();

        if(selectedMovie != null){
            movieModel.deleteMovie(selectedMovie);
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

            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void btnDeleteCategory(ActionEvent actionEvent) throws Exception {
        Category selectedCategory = lstCategory.getSelectionModel().getSelectedItem();
        if(selectedCategory != null){
            categoryModel.deleteCategory(selectedCategory);
        }
    }

    public void btnAddRating(ActionEvent actionEvent) {
    }
}