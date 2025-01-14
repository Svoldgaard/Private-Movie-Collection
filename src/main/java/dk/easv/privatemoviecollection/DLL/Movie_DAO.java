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
            SELECT m.ID, m.Name, m.Rating, m.PersonalRating, m.LastView, m.FileLink ,c.CName
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
                float personalRating = rs.getFloat("PersonalRating");
                Date lastView = rs.getDate("LastView");
                String fileLink = rs.getString("FileLink");


                Category category = new Category();
                category.setName(categoryName);


                Movie movie = new Movie(id, name, rating, fileLink, lastView.toLocalDate(), personalRating);
                movie.setCategory(category);

                allMovies.add(movie);
            }
            return allMovies;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get movies from database", ex);
        }
    }

    @Override
    public Movie createMovie(Movie movie) throws Exception {
        DB_Connect dbConnect = new DB_Connect();

        try (Connection conn = dbConnect.getConnection()) {
            conn.setAutoCommit(false);

            String movieSql = "INSERT INTO Movie (Name, Rating, FileLink, LastView) VALUES (?, ?, ?, ?)";
            try (PreparedStatement movieStmt = conn.prepareStatement(movieSql, Statement.RETURN_GENERATED_KEYS)) {
                movieStmt.setString(1, movie.getName());
                movieStmt.setFloat(2, movie.getRating());
                movieStmt.setString(3, movie.getFileLink());
                movieStmt.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));

                movieStmt.executeUpdate();

                ResultSet rs = movieStmt.getGeneratedKeys();
                if (!rs.next()) {
                    throw new SQLException("Failed to retrieve Movie ID.");
                }
                int movieId = rs.getInt(1);
                movie.setId(movieId);
            }

            // Save the movie's category relation in the CatMovie table
            String categorySql = "SELECT ID FROM Category WHERE CName = ?";
            int categoryId;
            try (PreparedStatement categoryStmt = conn.prepareStatement(categorySql)) {
                categoryStmt.setString(1, movie.getCategory().getName());
                ResultSet rs = categoryStmt.executeQuery();
                if (!rs.next()) {
                    throw new SQLException("Category not found: " + movie.getCategory().getName());
                }
                categoryId = rs.getInt("ID");
            }

            String catMovieSql = "INSERT INTO CatMovie (MovieID, CategoryID) VALUES (?, ?)";
            try (PreparedStatement catMovieStmt = conn.prepareStatement(catMovieSql)) {
                catMovieStmt.setInt(1, movie.getId());
                catMovieStmt.setInt(2, categoryId);
                catMovieStmt.executeUpdate();
            }

            conn.commit();
            return movie;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not create Movie", ex);
        }
    }




    @Override
    public void updateMovie (Movie movie) throws Exception
    {
        String sql = "UPDATE dbo.Movie SET Name = ?, Rating = ?, FileLink = ?, LastView = ?, PersonalRating = ? WHERE ID = ?";
        DB_Connect dbConnect = new DB_Connect();

        try (Connection conn = dbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, movie.getName());
            stmt.setFloat(2, movie.getRating());
            stmt.setString(3, movie.getFileLink());
            stmt.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
            stmt.setFloat(5, movie.getPersonalRating());
            stmt.setInt(6, movie.getId());

            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not update Movie", ex);
        }
    }


    @Override
    public void deleteMovie(Movie movie) throws Exception {
        DB_Connect dbConnect = new DB_Connect();

        String checkCategorySql = "SELECT COUNT(*) FROM CatMovie WHERE MovieID = ?";

        try (Connection conn = dbConnect.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkCategorySql)) {

            checkStmt.setInt(1, movie.getId());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);

                if (count > 1) {
                    String sql = "DELETE FROM CatMovie WHERE MovieID = ? AND CategoryID = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setInt(1, movie.getId());
                        stmt.setInt(2, movie.getCategory().getID());
                        stmt.executeUpdate();
                    }
                }
                else {
                    String deleteCatMovieSql = "DELETE FROM CatMovie WHERE MovieID = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(deleteCatMovieSql)) {
                        stmt.setInt(1, movie.getId());
                        stmt.executeUpdate();
                    }

                    String deleteMovieSql = "DELETE FROM Movie WHERE ID = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(deleteMovieSql)) {
                        stmt.setInt(1, movie.getId());
                        stmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException ex) {
            throw new Exception("Could not delete Movie", ex);
        }

    }


}
