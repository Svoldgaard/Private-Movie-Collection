package dk.easv.privatemoviecollection.GUI.Model;

import dk.easv.privatemoviecollection.BE.Category;
import dk.easv.privatemoviecollection.BLL.CategoryManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collections;
import java.util.List;

public class CategoryModel {

    private CategoryManager categoryManager;
    private ObservableList<Category> lstCategory;

    public CategoryModel() throws Exception {
        categoryManager = new CategoryManager();
        lstCategory = FXCollections.observableArrayList();
        refreshCategories();
    }

    public List<Category> getAllCategories() { return lstCategory; }


    public Category addCategories(Category category) throws Exception {
        Category createdCategory = categoryManager.createCategory(category);
        lstCategory.add(createdCategory);
        return createdCategory;
    }

    public void deleteCategory(Category category) throws Exception {
        categoryManager.deleteCategory(category);
        lstCategory.remove(category);
    }

    public void updateCategory(Category category) throws Exception {
        categoryManager.updateCategory(category);
        refreshCategories();
    }

    // Returns an ObservableList of categories
    public ObservableList<Category> getObservableCategory() {
        return lstCategory;
    }

    private void refreshCategories() throws Exception {
        List<Category> categoriesFromDB = categoryManager.getAllCategories();
        lstCategory = FXCollections.observableArrayList(categoriesFromDB);
    }
}
