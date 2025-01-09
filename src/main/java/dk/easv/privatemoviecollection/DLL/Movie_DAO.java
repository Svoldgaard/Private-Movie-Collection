package dk.easv.privatemoviecollection.DLL;
// Project import
import dk.easv.privatemoviecollection.BE.Category;
import dk.easv.privatemoviecollection.BE.Movie;
import dk.easv.privatemoviecollection.DLL.DBConnection.DB_Connect;
// Java import
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Movie_DAO implements IMovieDataAccess {

    @Override
    public List<Movie> getAllMovies() throws Exception {
        DB_Connect dbConnect = new DB_Connect();
        ArrayList<Movie> allMovies = new ArrayList<>();

        try (Connection conn = dbConnect.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = """
            SELECT m.ID, m.Name, m.Rating, c.CName
            FROM Movie m, Category c, CatMovie cm
            WHERE m.ID = cm.MovieID
            AND cm.CategoryID = c.ID
            """;

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("Name");
                float rating = rs.getFloat("Rating");
                String categoryName = rs.getString("CName");

                // Retrieve the category object using the category name
                Category category = new Category(); // Initialize an empty category
                category.setName(categoryName);  // Set the category name from the DB query

                // Create the movie object and set the category
                Movie movie = new Movie(id, name, rating);
                movie.setCategory(category);  // Associate the movie with the category

                allMovies.add(movie);
            }
            return allMovies;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get movies from database", ex);
        }
    }

    @Override
    public Movie createMovie(Movie movie) throws Exception
    {
        String sql ="INSERT INTO dbo.Movie (ID, Name, Rating, FileLink, LastView) VALUES (?,?,?,?,?)";
        DB_Connect dbConnect = new DB_Connect();

        try(Connection conn =dbConnect.getConnection())
        {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(2, movie.getName());
            stmt.setFloat(3, movie.getRating());
            stmt.setString(4, movie.getFileLink());
            stmt.setDate(5, movie.getLastView());


            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int id = rs.getInt(1);

            Movie createdMovie = new Movie (id, movie.getName(), movie.getRating(), movie.getFileLink(), movie.getLastView().toLocalDate(), movie.getPersonalRating());

            return createdMovie;
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not create Movie", ex);
        }
    }

    @Override
    public void updateMovie (Movie movie) throws Exception
    {
        String sql = "UPDATE dbo.Movie SET Name = ?, Rating = ?, FileLink = ?, LastView = ? WHERE ID = ?";
        DB_Connect dbConnect = new DB_Connect();

        try(Connection conn = dbConnect.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1,movie.getId());
            stmt.setString(2, movie.getName());
            stmt.setFloat(3, movie.getRating());
            stmt.setString(4, movie.getFileLink());
            stmt.setDate(5, movie.getLastView());

            stmt.executeUpdate();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not update Movie", ex);
        }
    }

    @Override
    public void deleteMovie(Movie movie) throws Exception
    {
        String sql = "DELETE FROM dbo.CatMovie WHERE  = ?";
        DB_Connect dbConnect = new DB_Connect();

        try(Connection conn = dbConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, movie.getId());

            stmt.executeUpdate();
        }
        catch(SQLException ex)
        {
            throw new Exception("Could not delete Movie", ex);
        }

    }


}
