package dk.easv.privatemoviecollection.DLL;

// Project imports
import dk.easv.privatemoviecollection.BE.Category;
// Java imports
import java.util.List;

public interface ICategoryDataAccess {
    List<Category> getAllCategories() throws Exception;

    Category createCategory(Category newCategory) throws Exception;

    void updateCategory(Category category) throws Exception;

    void deleteCategory(Category category) throws Exception;
}
