package dk.easv.privatemoviecollection.GUI.Controller;

//Project import
import dk.easv.privatemoviecollection.BE.Movie;
// Java import
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



public class NewMovieController {

    @FXML
    private TextField txtRating;
    @FXML
    private TextField txtCategory;
    @FXML
    private TextField txtMovieTitle;
    @FXML
    private ListView<String> lstCategory;

    private Movie movie;
    private ObservableList<String> categoryList;


    public void initialize() {

        categoryList = FXCollections.observableArrayList();

        // much data to check if it works
        categoryList.addAll("Action", "Comedy", "Drama", "Horror", "Sci-Fi");

        lstCategory.setItems(categoryList);
    }

    @FXML
    private void btnSave(ActionEvent actionEvent) {
    }

    @FXML
    private void btnCancel(ActionEvent actionEvent) {
        ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
    }
}
