package dk.easv.privatemoviecollection.GUI.Controller;

// Project imports
import dk.easv.privatemoviecollection.BE.Movie;
import dk.easv.privatemoviecollection.GUI.Model.MovieModel;
//Java Imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RatingController {

    @FXML
    private TextField txtRating;

    private Movie movie;
    private MovieController movieController;

    public void setMovieController(MovieController movieController) {
        this.movieController = movieController;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        txtRating.setText(String.valueOf(movie.getPersonalRating()));
    }

    @FXML
    private void btnSave(ActionEvent actionEvent) {

        try {
            String ratingText = txtRating.getText();
            if(ratingText == null || ratingText.isEmpty()){
                showAlert("Error", "Rating can't be empty.");
                return;
            }

            float newRating = 0;
            try{
                newRating = Float.parseFloat(ratingText);
            }
            catch(NumberFormatException e){
                showAlert("Error", "Please enter a valid number for the rating.");
            }

            if(newRating < 0 || newRating > 10){
                showAlert("Error", "Rating must be between 0 and 10.");
                return;
            }

            movie.setPersonalRating(newRating);

            MovieModel movieModel = new MovieModel();
            movieModel.updateMovie(movie);

            ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();

        }
        catch(Exception ex){
            ex.printStackTrace();
            showAlert("Error", "An unexpected error occured." + ex.getMessage());
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

}
