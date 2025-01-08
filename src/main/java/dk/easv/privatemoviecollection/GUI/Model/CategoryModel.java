package dk.easv.privatemoviecollection.GUI.Model;

// Project imports
import dk.easv.privatemoviecollection.BE.Category;
import dk.easv.privatemoviecollection.BLL.CategoryManager;
// Java imports
import java.util.List;

public class CategoryModel {

    private CategoryManager categoryManager;
    private List<Category> allCategories;

    public CategoryModel() throws Exception {
        categoryManager = new CategoryManager();
        loadAllCategories();
    }

    public void loadAllCategories() throws Exception {
        allCategories = categoryManager.getAllCategories();
    }


    public List<Category> getAllCategories() {
        return allCategories;
    }


       public Category addCategories(Category category) throws Exception {
        Category createdCategory = categoryManager.createCategory(category);
        allCategories.add(createdCategory);
        return createdCategory;
    }


    public void deleteCategory(Category category) throws Exception {
        categoryManager.deleteCategory(category);
        allCategories.remove(category);
    }


    public void updateCategory(Category category) throws Exception {
        categoryManager.updateCategory(category);
        loadAllCategories();
    }
}
