package dk.easv.privatemoviecollection.DLL;
// Project import
import dk.easv.privatemoviecollection.BE.Movie;
import dk.easv.privatemoviecollection.DLL.DBConnection.DB_Connect;
// Java import
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Movie_DAO implements IMovieDataAccess {

    @Override
    public List<Movie> getAllMovies() throws Exception
    {
        DB_Connect dbConnect = new DB_Connect();

        ArrayList<Movie> allMovies = new ArrayList<>();

        try(Connection conn = dbConnect.getConnection();
            Statement stmt = conn .createStatement())
        {
            String sql = "SELECT * FROM movie";
            ResultSet rs = stmt.executeQuery(sql);

            // Loops through rows from the database result test
            while (rs.next())
            {
                // Map DB row to Movie object
                int id = rs.getInt("ID");
                String name = rs.getString("Name");
                float rating = rs.getFloat("Rating");
                String fileLink = rs.getString("FileLink");
                LocalDate lastView = rs.getDate("LastView").toLocalDate();

                Movie movie = new Movie(id, name, rating, fileLink, lastView);
                allMovies.add(movie);
            }
            return allMovies;
        }
        catch(SQLException ex)
        {
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

            stmt.setInt(1,movie.getId());
            stmt.setString(2, movie.getName());
            stmt.setFloat(3, movie.getRating());
            stmt.setString(4, movie.getFileLink());
            stmt.setDate(5, movie.getDate());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int id = rs.getInt(1);

         Movie createdMovie = new Movie (id, movie.getName(), movie.getRating(), movie.getFileLink(), movie.getLocalDate());

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

    }

    @Override
    public void deleteMovie(Movie movie)
    {

    }

}
