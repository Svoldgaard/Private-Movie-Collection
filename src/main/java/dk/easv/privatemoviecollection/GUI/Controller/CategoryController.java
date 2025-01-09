package dk.easv.privatemoviecollection.GUI.Controller;

import dk.easv.privatemoviecollection.BE.Category;
import dk.easv.privatemoviecollection.GUI.Model.CategoryModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;

public class CategoryController {

    @FXML
    private TextField txtCategory;

    private CategoryModel categoryModel;
    private MovieController movieController;

    public CategoryController() throws Exception {
        categoryModel = new CategoryModel();
        movieController = new MovieController();
    }

    @FXML
    private void btnSave(ActionEvent actionEvent) {

        String categoryName = txtCategory.getText();


        if (categoryName != null && !categoryName.isEmpty()) {
            Category newCategory = new Category(-1, categoryName);
            try {
                movieController = new MovieController();

                categoryModel.addCategories(newCategory);
                movieController.refreshCategory();

                ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
            } catch (Exception e) {
                e.printStackTrace();

            }
        } else {

            System.out.println("Category name cannot be empty.");
        }
    }

    @FXML
    private void btnCancel(ActionEvent actionEvent) {
        ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
    }

    void MovieMain(MovieController movieController) {
    }

    void setCategoryController(TreeItem<String> movieController) {
    }

    }
