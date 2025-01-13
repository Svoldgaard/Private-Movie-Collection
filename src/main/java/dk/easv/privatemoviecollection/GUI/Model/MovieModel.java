    package dk.easv.privatemoviecollection.GUI.Model;

    import dk.easv.privatemoviecollection.BE.Category;
    import dk.easv.privatemoviecollection.BE.Movie;
    import dk.easv.privatemoviecollection.BLL.MovieManager;
    import dk.easv.privatemoviecollection.DLL.DBConnection.DB_Connect;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;

    import java.io.IOException;
    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.util.ArrayList;
    import java.util.List;


    public class MovieModel {
        private MovieManager movieManager;


        private ObservableList<Movie> tblMovie;

        public MovieModel() throws Exception {
            movieManager = new MovieManager();
            tblMovie = FXCollections.observableArrayList();
            tblMovie.addAll(movieManager.getAllMovies());
        }

        public ObservableList<Movie> getMoviesByCategory(Category category) throws Exception {
            ObservableList<Movie> filteredMovies = FXCollections.observableArrayList();

            for (Movie movie : tblMovie) {
                // Ensure movie has a category and it matches the selected category
                if (movie.getCategory() != null && movie.getCategory().getName().equals(category.getName())) {
                    filteredMovies.add(movie);
                }
            }

            return filteredMovies;
        }

        public void searchMovie(String query) throws Exception {
            List<Movie> searchResults = movieManager.searchMovies(query);
            tblMovie.clear();
            tblMovie.addAll(searchResults);
        }


        public Movie addMovie(Movie movie) throws Exception {
            Movie createdMovie = movieManager.createMovie(movie);
            tblMovie.add(createdMovie);
            return createdMovie;
        }


        public void deleteMovie(Movie movie) throws Exception {
            movieManager.deleteMovie(movie);
            tblMovie.remove(movie);
        }


        public void updateMovie(Movie movie) throws Exception {
            movieManager.updateMovie(movie);

        }

        public ObservableList<Movie> getObservableMovies() {
            return tblMovie;
        }

    }
