package dk.easv.privatemoviecollection.DLL;
// Project import
import dk.easv.privatemoviecollection.BE.Movie;
import dk.easv.privatemoviecollection.DLL.DBConnection.DB_Connect;
// Java import
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

            while (rs.next())
            {
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
    return null;
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
