package dk.easv.privatemoviecollection.GUI.Model;

import dk.easv.privatemoviecollection.BE.Movie;
import dk.easv.privatemoviecollection.BLL.MovieManager;

import java.util.List;

public class MovieModel {
    private MovieManager movieManager;
    private List<Movie> allMovies;

    public MovieModel() throws Exception {
        movieManager = new MovieManager();
        loadAllMovies();  // Preload movies at initialization
    }

    // Load all movies from the database
    public void loadAllMovies() throws Exception {
        allMovies = movieManager.getAllMovies();
    }

    // Return all movies (for tree population)
    public List<Movie> getAllMovies() {
        return allMovies;
    }

    // Perform search and update the list dynamically
    public List<Movie> searchMovie(String query) throws Exception {
        return movieManager.searchMovies(query);
    }

    // Add a new movie to the database and update the list
    public Movie addMovie(Movie movie) throws Exception {
        Movie createdMovie = movieManager.createMovie(movie);
        allMovies.add(createdMovie);
        return createdMovie;
    }

    // Delete a movie from the database and remove from the list
    public void deleteMovie(Movie movie) throws Exception {
        movieManager.deleteMovie(movie);
        allMovies.remove(movie);
    }

    // Update movie and refresh list
    public void updateMovie(Movie movie) throws Exception {
        movieManager.updateMovie(movie);
        loadAllMovies();  // Reload to reflect updates
    }
}
