    package dk.easv.privatemoviecollection.GUI.Model;

    import dk.easv.privatemoviecollection.BE.Movie;
    import dk.easv.privatemoviecollection.BLL.MovieManager;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;

    import java.util.List;


    public class MovieModel {
        private MovieManager movieManager;
        private List<Movie> allMovies;

        private ObservableList<Movie> tblMovie;

        public MovieModel() throws Exception {
            movieManager = new MovieManager();
            tblMovie = FXCollections.observableArrayList();
            tblMovie.addAll(movieManager.getAllMovies());
        }


       public List<Movie> searchMovie(String query) throws Exception {
            return movieManager.searchMovies(query);
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
