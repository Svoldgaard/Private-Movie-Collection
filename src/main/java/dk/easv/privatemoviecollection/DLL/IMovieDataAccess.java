package dk.easv.privatemoviecollection.DLL;
// Project import
import dk.easv.privatemoviecollection.BE.Movie;
// Java import
import java.util.List;

public interface IMovieDataAccess {
    List<Movie> getAllMovies() throws Exception;

    Movie createMovie(Movie newMovie) throws Exception;

    void updateMovie(Movie movie) throws Exception;

    void deleteMovie(Movie movie) throws Exception;
}
