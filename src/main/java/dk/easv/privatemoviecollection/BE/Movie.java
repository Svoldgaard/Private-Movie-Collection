package dk.easv.privatemoviecollection.BE;

import java.sql.Date;
import java.time.LocalDate;

public class Movie {

    private int id;
    private String name;
    private float rating;
    private String fileLink;
    private LocalDate lastView;
    private float personalRating;
    private Category category;

    public Movie(int id, String name, float rating, String fileLink, LocalDate lastView,float personalRating) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.fileLink = fileLink;
        this.lastView = lastView;
        this.personalRating = personalRating;

    }

    public Movie(int id, String name, float rating, float personalRating) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.personalRating = personalRating;

    }

    public float getPersonalRating() {
        return personalRating;
    }

    public void setPersonalRating(float personalRating) {
        this.personalRating = personalRating;
    }

    public Movie(int id, String name, float rating, String fileLink, LocalDate lastView, Category category) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.fileLink = fileLink;
        this.lastView = lastView;
        this.category = category;
    }

    public Movie(int id, String movieTitle, Float rating) {
        this.id = id;
        this.name = movieTitle;
        this.rating = rating;

    }

    public Movie(int i, String movieTitle, Float rating, String selectedCategory) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public Date getLastView() {
        return Date.valueOf(lastView);
    }

    public void setLastView(LocalDate lastView) {
        this.lastView = lastView;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", category=" + (category != null ? category.getName() : "No Category") +
                '}';
    }
}
