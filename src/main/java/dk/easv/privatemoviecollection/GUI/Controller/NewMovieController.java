package dk.easv.privatemoviecollection.GUI.Controller;

//Project import
import dk.easv.privatemoviecollection.BE.Movie;
// Java import
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.IndexRange;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



public class NewMovieController {

    @FXML
    private TextField txtRating;
    @FXML
    private TextField txtCategory;
    @FXML
    private TextField txtMovieTitle;

    private Movie movie;

    @FXML
    private void btnSave(ActionEvent actionEvent) {
    }

    @FXML
    private void btnCancel(ActionEvent actionEvent) {
        ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
    }
}
