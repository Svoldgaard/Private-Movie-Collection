package dk.easv.privatemoviecollection.BLL.Search;

import dk.easv.privatemoviecollection.BE.Movie;
import dk.easv.privatemoviecollection.BE.Category;

import java.util.ArrayList;
import java.util.List;

public class MovieSearcher {

    public List<Movie> search(List<Movie> searchBase, String query) {
        List<Movie> searchResult = new ArrayList<>();

        for (Movie movie : searchBase) {
            if(compareToMovieTitle(query, movie) || (compareToMovieCategory(query, movie)) || (compareToImdb(query, movie)))
            {
                searchResult.add(movie);
            }
        }
        return searchResult;
    }


    private boolean compareToMovieTitle(String query, Movie movie) {
        return movie.getName().toLowerCase().contains(query.toLowerCase());
    }

    private boolean compareToMovieCategory(String query, Movie movie) {
        return movie.getCategory().getName().toLowerCase().contains(query.toLowerCase());
    }

    private boolean compareToImdb(String query, Movie movie) {
        return Float.toString(movie.getRating()).contains(query);
    }

}
