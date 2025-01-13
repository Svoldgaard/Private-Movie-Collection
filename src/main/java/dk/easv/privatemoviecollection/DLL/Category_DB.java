package dk.easv.privatemoviecollection.DLL;

// Project imports
import dk.easv.privatemoviecollection.BE.Category;
import dk.easv.privatemoviecollection.DLL.DBConnection.DB_Connect;
// Java imports
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Category_DB implements ICategoryDataAccess {

    @Override
    public List<Category> getAllCategories() throws Exception {
        DB_Connect dbConnect = new DB_Connect();
        ArrayList<Category> allCategories = new ArrayList<>();

        try (Connection conn = dbConnect.getConnection(); Statement stmt = conn.createStatement()) {
            // Debugging: Log the actual SQL query to confirm it's correct
            String sql = "SELECT * FROM category";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("CName");

                Category category = new Category(id, name);
                allCategories.add(category);
            }

            return allCategories;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get all categories", ex);
        }
    }


    @Override
    public Category createCategory(Category category) throws Exception {
        DB_Connect dbConnect = new DB_Connect();
        String insertCategorySQL = "INSERT INTO dbo.Category(CName) VALUES (?)";

        try (Connection conn = dbConnect.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(insertCategorySQL, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, category.getName());

            int affectedRows = stmt.executeUpdate();


            if (affectedRows == 0) {
                throw new SQLException("Creating category failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int newId = generatedKeys.getInt(1);
                    return new Category(newId, category.getName());
                } else {
                    throw new SQLException("Creating category failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not create category", ex);
        }
    }

    @Override
    public void updateCategory(Category category) throws Exception
    {
        String sql = "UPDATE dbo.Category SET Name = ? WHERE ID = ?";
        DB_Connect dbConnect = new DB_Connect();

        try (Connection conn = dbConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, category.getName());
            stmt.setInt(2, category.getID());

            stmt.executeUpdate();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not update category", ex);
        }
    }

    @Override
    public void deleteCategory(Category category) throws Exception
    {
        String sql = "DELETE FROM dbo.Category WHERE ID = ?";
        DB_Connect dbConnect = new DB_Connect();

        try (Connection conn = dbConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, category.getID());

            stmt.executeUpdate();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not delete category", ex);
        }
    }

}
