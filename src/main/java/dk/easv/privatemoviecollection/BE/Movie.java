package dk.easv.privatemoviecollection.BE;

import javafx.scene.control.IndexRange;

import java.sql.Date;
import java.time.LocalDate;

public class Movie {

    private int id;
    private String title;
    private int rating;

    public Movie(int id, String title, int rating) {
        this.id = id;
        this.title = title;
        this.rating = rating;
    }

    public Movie(int id, String name, float rating, String fileLink, LocalDate lastView) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", rating=" + rating +
                '}';
    }

    public String getName() {
    }

    public Date getDate() {
    }

    public String getFileLink() {
    }

    public LocalDate getLocalDate() {
    }
}
