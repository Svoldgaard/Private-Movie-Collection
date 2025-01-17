package dk.easv.privatemoviecollection.GUI.Controller;

import dk.easv.privatemoviecollection.BE.Category;
import dk.easv.privatemoviecollection.GUI.Model.CategoryModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CategoryController {

    @FXML
    private TextField txtCategory;

    private CategoryModel categoryModel;
    private MovieController movieController;

    public CategoryController() throws Exception {
        categoryModel = new CategoryModel();
    }

    public void setMovieController(MovieController movieController) {
        this.movieController = movieController;
    }

    @FXML
    private void btnSave(ActionEvent actionEvent) {
        String categoryName = txtCategory.getText();
        if (categoryName != null && !categoryName.isEmpty()) {
            Category newCategory = new Category(-1, categoryName);
            try {
                categoryModel.addCategories(newCategory);
                movieController.refreshCategory();
                ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
            } catch (Exception e) {
                e.printStackTrace();
                movieController.showAlert("Error", "Failed to add the category.");
            }
        } else {
            System.out.println("Category name cannot be empty.");
        }
    }



    @FXML
    private void btnCancel(ActionEvent actionEvent) {
        ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
    }
}
