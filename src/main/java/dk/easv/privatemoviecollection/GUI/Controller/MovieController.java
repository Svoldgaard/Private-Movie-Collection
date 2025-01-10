package dk.easv.privatemoviecollection.GUI.Controller;

// Project imports
import dk.easv.privatemoviecollection.BE.Category;
import dk.easv.privatemoviecollection.BE.Movie;
import dk.easv.privatemoviecollection.GUI.Model.CategoryModel;
import dk.easv.privatemoviecollection.DLL.DBConnection.DB_Connect;
import dk.easv.privatemoviecollection.GUI.Model.MovieModel;
import dk.easv.privatemoviecollection.MovieMain;

// Java imports
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
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

        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        colCategory.setCellValueFactory(cellData -> {
            Category category = cellData.getValue().getCategory();
            return new SimpleStringProperty(category != null ? category.getName() : "No Category");
        });
        colPRating.setCellValueFactory(new PropertyValueFactory<>("personalRating"));


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


        movieDuration.setValue(0);
    }

    public void refreshMovie() {
        tblMovie.setItems(movieModel.getObservableMovies());
    }

    public void refreshCategory() {
        lstCategory.setItems(categoryModel.getObservableCategory());
    }



    private void playVideo(String fileName) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();  // Stop any currently playing video
        }

        try {
            // Get the file path from the database (make sure it's correctly formatted)
            // Example of resolving the file path
            String filePath = "file:" + getClass().getResource("/dk/easv/privatemoviecollection/Movie/" + fileName).toExternalForm();

            // Create a Media instance using the resolved file path
            javafx.scene.media.Media media = new javafx.scene.media.Media(filePath);

            // Create a MediaPlayer instance to handle the playback of the video
            mediaPlayer = new javafx.scene.media.MediaPlayer(media);

            // Set the MediaPlayer to the MediaView
            mediaView.setMediaPlayer(mediaPlayer);

            // Start playing the video as soon as it's ready
            mediaPlayer.setOnReady(() -> mediaPlayer.play());

            // Update the duration slider as the video plays
            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                movieDuration.setValue(newValue.toSeconds());
            });

            // Set the total duration on the slider when the video is ready
            mediaPlayer.setOnReady(() -> {
                movieDuration.setMax(mediaPlayer.getTotalDuration().toSeconds());
            });

            // Allow the user to seek within the video using the slider
            movieDuration.setOnMouseReleased(event -> {
                if (mediaPlayer != null) {
                    mediaPlayer.seek(Duration.seconds(movieDuration.getValue()));
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to play the selected video.");
        }
    }


    @FXML
    private void btnPlayPause(ActionEvent actionEvent) {
        if (mediaPlayer != null) {
            javafx.scene.media.MediaPlayer.Status status = mediaPlayer.getStatus();
            if (status == javafx.scene.media.MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
            } else if (status == javafx.scene.media.MediaPlayer.Status.PAUSED || status == javafx.scene.media.MediaPlayer.Status.READY) {
                mediaPlayer.play();
            }
        } else {
            showAlert("No Video", "No video is currently selected.");
        }
    }


    private void stopVideo() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }


    @FXML
    private void handleMovieSelection() {
        // Get the selected movie from the TableView
        Movie selectedMovie = tblMovie.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            // Ensure the movie has a valid filePath
            String filePath = selectedMovie.getFilePath();  // assuming you have filePath set correctly in your Movie object

            if (filePath != null && !filePath.isEmpty()) {
                playVideo(filePath);  // Call playVideo with the absolute path
            } else {
                showAlert("Error", "No file path associated with this movie.");
            }
        } else {
            showAlert("Error", "Please select a movie from the table.");
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


    @FXML
    private void btnEditRating(ActionEvent actionEvent) {

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
            //controller.MovieMain(this);
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
            // Show a confirmation alert before deletion
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete this category?");
            alert.setContentText("This action cannot be undone.");

            // Wait for user response
            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

            // If user confirms, delete the category
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
