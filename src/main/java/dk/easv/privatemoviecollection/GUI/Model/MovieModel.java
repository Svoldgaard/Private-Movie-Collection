    package dk.easv.privatemoviecollection.GUI.Model;

    import dk.easv.privatemoviecollection.BE.Movie;
    import dk.easv.privatemoviecollection.BLL.MovieManager;

    import java.util.List;

    public class MovieModel {
        private MovieManager movieManager;
        private List<Movie> allMovies;

        public MovieModel() throws Exception {
            movieManager = new MovieManager();
            loadAllMovies();
        }


        public void loadAllMovies() throws Exception {
            allMovies = movieManager.getAllMovies();
        }


        public List<Movie> getAllMovies() {
            return allMovies;
        }


        public List<Movie> searchMovie(String query) throws Exception {
            return movieManager.searchMovies(query);
        }


        public Movie addMovie(Movie movie) throws Exception {
            Movie createdMovie = movieManager.createMovie(movie);
            allMovies.add(createdMovie);
            return createdMovie;
        }


        public void deleteMovie(Movie movie) throws Exception {
            movieManager.deleteMovie(movie);
            allMovies.remove(movie);
        }


        public void updateMovie(Movie movie) throws Exception {
            movieManager.updateMovie(movie);
            loadAllMovies();
        }
    }
