package dk.easv.privatemoviecollection.GUI.Controller;

import dk.easv.privatemoviecollection.BE.Category;
import dk.easv.privatemoviecollection.BE.Movie;
import dk.easv.privatemoviecollection.GUI.Model.CategoryModel;
import dk.easv.privatemoviecollection.GUI.Model.MovieModel;
import dk.easv.privatemoviecollection.MovieMain;

import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class MovieController implements Initializable {

    @FXML
    public MediaView mediaView;
    @FXML
    public Button btnPlayPause;
    @FXML
    private TextField txtSearch;

    @FXML
    private Slider movieDuration;

    @FXML
    private ListView<Category> lstCategory;

    @FXML
    public TableView<Movie> tblMovie;

    private MovieModel movieModel;
    private CategoryModel categoryModel;

    @FXML
    private TableColumn<Movie, String> colName;

    @FXML
    private TableColumn<Movie, Float> colRating;

    @FXML
    private TableColumn<Movie, String> colCategory;

    @FXML
    private TableColumn<Movie, Float> colPRating;

    //@FXML
    //public TableColumn<Movie, Date> colLastView;

    @FXML
    public TableColumn<Movie, String> colFileLink;

    private File file;
    private Media media;
    private MediaPlayer mediaPlayer;



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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        colCategory.setCellValueFactory(cellData -> {
            Category category = cellData.getValue().getCategory();
            return new SimpleStringProperty(category != null ? category.getName() : "No Category");
        });
        colPRating.setCellValueFactory(new PropertyValueFactory<>("personalRating"));
        //colLastView.setCellValueFactory(new PropertyValueFactory<>("lastViewed"));
        colFileLink.setCellValueFactory(new PropertyValueFactory<>("fileLink"));

        lstCategory.setItems(categoryModel.getObservableCategory());
        tblMovie.setItems(movieModel.getObservableMovies());

        lstCategory.getSelectionModel().selectedItemProperty().addListener((observable, oldCategory, newCategory) -> {
            if (newCategory != null) {
                try {
                    ObservableList<Movie> filteredMovies = movieModel.getMoviesByCategory(newCategory);
                    tblMovie.setItems(filteredMovies);
                } catch (Exception e) {
                    displayError(e);
                }
            }
        });

        txtSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                movieModel.searchMovie(newValue);
                tblMovie.setItems(movieModel.getObservableMovies());
            } catch (Exception e) {
                displayError(e);
            }
        });


        // Media player
        file = new File("src/main/resources/dk/easv/privatemoviecollection/Movie/215926_tiny.mp4");
        media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);



    }

    public void refreshMovie() {
        tblMovie.setItems(movieModel.getObservableMovies());
    }

    public void refreshCategory() {
        lstCategory.setItems(categoryModel.getObservableCategory());
    }


    @FXML
    private void btnPlayPause(ActionEvent actionEvent) {
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();  // Pause the media player
            btnPlayPause.setText("Play"); // Change button text to "Play"
        } else {
            mediaPlayer.play();   // Play the media player
            btnPlayPause.setText("Pause"); // Change button text to "Pause"
        }
    }


    @FXML
    private void handleMovieSelection() throws IOException, URISyntaxException {
        // Get selected movie from the table view
        Movie selectedMovie = tblMovie.getSelectionModel().getSelectedItem();

        if (selectedMovie != null) {
            // Assuming selectedMovie.getFileLink() returns the file path as a string
            String filePath = selectedMovie.getFileLink();
            System.out.println("File path: " + filePath);

            // Convert the file path to a valid URI by resolving it as a resource
            URL resource = getClass().getResource("/" + filePath);
            if (resource != null) {
                media = new Media(resource.toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaView.setMediaPlayer(mediaPlayer);
                mediaPlayer.play();
            } else {
                // Handle error: resource not found
                showAlert("File Not Found", "The movie file could not be found.");
            }
        }
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

        CategoryController controller = fxmlLoader.getController();
        controller.setMovieController(this);

        stage.showAndWait();
        refreshCategory();
    }

    @FXML
    private void btnDeleteMovie(ActionEvent actionEvent) throws Exception {
        Movie selectedMovie = tblMovie.getSelectionModel().getSelectedItem();

        if (selectedMovie != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete this movie?");
            alert.setContentText("Movie: " + selectedMovie.getName() + "\nThis action cannot be undone.");

            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

            if (result == ButtonType.OK) {
                movieModel.deleteMovie(selectedMovie);
                refreshMovie();
            }
        } else {
            showAlert("No Selection", "Please select a movie to delete.");
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
    private boolean btnEditMovie() throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dk/easv/privatemoviecollection/FXML/AddMovie.fxml"));
            Parent root = fxmlLoader.load();

            NewMovieController controller = fxmlLoader.getController();
            controller.setMovieController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void btnDeleteCategory(ActionEvent actionEvent) throws Exception {
        Category selectedCategory = lstCategory.getSelectionModel().getSelectedItem();

        if (selectedCategory != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete this category?");
            alert.setContentText("This action cannot be undone.");

            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

            if (result == ButtonType.OK) {
                categoryModel.deleteCategory(selectedCategory);
                refreshCategory();
            }
        } else {
            showAlert("No Selection", "Please select a category to delete.");
        }
    }

    public void btnAddRating(ActionEvent actionEvent) throws IOException {
        System.out.println("test1");
        Movie selectedMovie = tblMovie.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dk/easv/privatemoviecollection/FXML/AddRating.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);

        System.out.println("test2");
        RatingController controller = fxmlLoader.getController();
        controller.setMovieController(this);
        controller.setMovie(selectedMovie);

        stage.showAndWait();

        refreshMovie();
    }
}
