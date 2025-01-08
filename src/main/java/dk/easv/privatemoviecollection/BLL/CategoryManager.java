package dk.easv.privatemoviecollection.BLL;

// Project imports
import dk.easv.privatemoviecollection.BE.Category;
import dk.easv.privatemoviecollection.DLL.Category_DB;
//Java imports
import java.io.IOException;
import java.util.List;

public class CategoryManager {

    private Category_DB categoryDb;

    public CategoryManager() throws IOException {
        categoryDb = new Category_DB();
    }

    public List<Category> getAllCategories() throws Exception {
        return categoryDb.getAllCategories();
    }

     public Category createCategory(Category newCategory) throws Exception {
        return categoryDb.createCategory(newCategory);
    }

    public void deleteCategory(Category selectedCategory) throws Exception {
        categoryDb.deleteCategory(selectedCategory);
    }

    public void updateCategory(Category updatedCategory) throws Exception {
        categoryDb.updateCategory(updatedCategory);
    }
}
