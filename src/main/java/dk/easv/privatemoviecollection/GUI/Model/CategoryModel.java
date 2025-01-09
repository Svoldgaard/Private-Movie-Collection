package dk.easv.privatemoviecollection.GUI.Model;

// Project imports
import dk.easv.privatemoviecollection.BE.Category;
import dk.easv.privatemoviecollection.BLL.CategoryManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
// Java imports
import java.util.List;

public class CategoryModel {

    private CategoryManager categoryManager;

    private ObservableList<Category> lstCategory;

    public CategoryModel() throws Exception {
        categoryManager = new CategoryManager();
        lstCategory = FXCollections.observableArrayList();
        lstCategory.addAll(categoryManager.getAllCategories());

    }

    public List<Category> getAllCategories() {
        return lstCategory;
    }


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

    }

    public ObservableList<Category> getObservableCategory() {
        return lstCategory;
    }
}
