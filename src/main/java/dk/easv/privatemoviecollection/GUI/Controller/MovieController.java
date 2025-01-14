package dk.easv.privatemoviecollection.GUI.Controller;

// Project imports
import dk.easv.privatemoviecollection.BE.Category;
import dk.easv.privatemoviecollection.BE.Movie;
import dk.easv.privatemoviecollection.GUI.Model.CategoryModel;
import dk.easv.privatemoviecollection.GUI.Model.MovieModel;
import dk.easv.privatemoviecollection.MovieMain;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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
    public TextField txtFileLink;
    @FXML
    private TextField txtRating;
    @FXML
    private TextField txtMovieTitle;

    @FXML
    private TableColumn<Movie, String> colName;

    @FXML
    private TableColumn<Movie, Float> colRating;

    @FXML
    private TableColumn<Movie, String> colCategory;

    @FXML
    private TableColumn<Movie, Float> colPRating;

    @FXML
    public TableColumn<Movie, LocalDate> colLastView;

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
        colLastView.setCellValueFactory(new PropertyValueFactory<>("formattedLastView"));
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

        showWarningIfNeeded();


        // Media player
        file = new File("src/main/resources/dk/easv/privatemoviecollection/Movie/215926_tiny.mp4");
        media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);

        // set icon
        setButtonIcon(btnPlayPause, "/dk/easv/privatemoviecollection/Icon/playbutton.png");


    }

    public void refreshMovie() {
        Platform.runLater(() -> {
            try {
                movieModel.refreshMovie();
                tblMovie.setItems(movieModel.getObservableMovies());  // Update ListView
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to refresh categories.");
            }
        });
    }

    public void refreshCategory() throws Exception {
        Platform.runLater(() -> {
            try {
                categoryModel.refreshCategories();
                lstCategory.setItems(categoryModel.getObservableCategory());  // Update ListView
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to refresh categories.");
            }
        });
    }


    @FXML
    private void btnPlayPause(ActionEvent actionEvent) {
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            setButtonIcon(btnPlayPause, "/dk/easv/privatemoviecollection/Icon/playbutton.png");
        } else {
            mediaPlayer.play();
            setButtonIcon(btnPlayPause, "/dk/easv/privatemoviecollection/Icon/pause.png");
        }
    }


    @FXML
    private void handleMovieSelection() throws IOException, URISyntaxException {
        Movie selectedMovie = tblMovie.getSelectionModel().getSelectedItem();

        if (selectedMovie != null) {
            String filePath = selectedMovie.getFileLink();
            System.out.println("File path: " + filePath);

            URL resource = getClass().getResource("/" + filePath);
            if (resource != null) {
                media = new Media(resource.toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaView.setMediaPlayer(mediaPlayer);
                mediaPlayer.play();
            } else {
                showAlert("File Not Found", "The movie file could not be found.");
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
        stage.showAndWait();
        refreshMovie();
    }


    @FXML
    private void btnAddCategory(ActionEvent actionEvent) throws Exception {
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

    void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void btnEditMovie(ActionEvent actionEvent) throws IOException {
        Movie selectedMovie = tblMovie.getSelectionModel().getSelectedItem();
        if (selectedMovie == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No selection.");
            alert.setHeaderText("No movie selected.");
            alert.setContentText("Please select a movie from the table.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dk/easv/privatemoviecollection/FXML/EditMovie.fxml"));
            Parent root = fxmlLoader.load();

            EditMovieController controller = fxmlLoader.getController();
            controller.setMovie(movieModel, selectedMovie);

            Stage stage = new Stage();
            stage.setTitle("Edit Movie");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();

            tblMovie.refresh();
        }
        catch(IOException ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to load the movie editor.");
        }

        try {
            float rating = Float.parseFloat(txtRating.getText());
            String movieTitle = txtMovieTitle.getText();

            if(movieTitle.isEmpty()){
                showAlert("Error", "Movie Title can't be empty.");
                return;
            }

            selectedMovie.setName(movieTitle);
            selectedMovie.setRating(rating);
            selectedMovie.setFileLink(txtFileLink.getText());
        }
        catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid number for the rating.");
        }
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
        Movie selectedMovie = tblMovie.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dk/easv/privatemoviecollection/FXML/AddRating.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);

        RatingController controller = fxmlLoader.getController();
        controller.setMovieController(this);
        controller.setMovie(selectedMovie);

        stage.showAndWait();

        refreshMovie();
    }

    private void showWarningIfNeeded() {
        ObservableList<Movie> allMovies = movieModel.getObservableMovies();

        List<Movie> moviesToWarn = allMovies.stream()
                .filter(movie -> {
                    boolean isLowRating = movie.getPersonalRating() < 6;
                    boolean isNotWatchedIn2Years = movie.getLastView() == null ||
                            movie.getLastView().isBefore(LocalDate.now().minusYears(2));

                    return isLowRating || isNotWatchedIn2Years;
                })
                .toList();

        if (!moviesToWarn.isEmpty()) {
            StringBuilder warningMessage = new StringBuilder("Please review the following movies:\n");
            for (Movie movie : moviesToWarn) {
                warningMessage.append("- ").append(movie.getName()).append("\n");
            }

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Movies to Review");
            alert.setHeaderText("Some movies require your attention:");
            alert.setContentText(warningMessage.toString());
            alert.showAndWait();
        }
    }

    private void setButtonIcon(Button button, String iconPath) {
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath)));
        button.setGraphic(new ImageView(icon)); // Set the button graphic
    }
}
