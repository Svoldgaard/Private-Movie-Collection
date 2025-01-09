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
    public List<Category> getAllCategories() throws Exception
    {
        DB_Connect dbConnect = new DB_Connect();

        ArrayList<Category> allCategories = new ArrayList<>();

        try(Connection conn = dbConnect.getConnection(); Statement stmt = conn.createStatement())
        {
            String sql = "SELECT * FROM category";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next())
            {
                int id = rs.getInt("ID");
                String name = rs.getString("Name");

                Category category = new Category(id, name);
                allCategories.add(category);
            }
            return allCategories;
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not get all categories", ex);
        }
    }

    @Override
    public Category createCategory(Category category) throws Exception {
        String getMaxIdSQL = "SELECT MAX(ID) FROM dbo.Category";
        String insertCategorySQL = "INSERT INTO dbo.Category(ID, Name) VALUES (?, ?)";
        DB_Connect dbConnect = new DB_Connect();

        try (Connection conn = dbConnect.getConnection()) {
            PreparedStatement getMaxIdStmt = conn.prepareStatement(getMaxIdSQL);
            ResultSet rs = getMaxIdStmt.executeQuery();
            int newId = 1;
            if (rs.next()) {
                newId = rs.getInt(1) + 1;
            }

            PreparedStatement insertStmt = conn.prepareStatement(insertCategorySQL);
            insertStmt.setInt(1, newId);
            insertStmt.setString(2, category.getName());
            insertStmt.executeUpdate();

            Category createdCategory = new Category(newId, category.getName());
            return createdCategory;
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
