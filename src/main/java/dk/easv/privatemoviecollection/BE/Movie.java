package dk.easv.privatemoviecollection.BE;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Movie {

    private int id;
    private String name;
    private float rating;
    private String fileLink;
    private LocalDate lastView;  // Use LocalDate instead of Date
    private float personalRating;
    private Category category;
    private String filePath;

    // Constructor
    public Movie(int id, String name, float rating, String fileLink, LocalDate lastView, float personalRating) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.fileLink = fileLink;
        this.personalRating = personalRating;
        this.lastView = lastView;
    }

    public Movie(int id, String name, float rating, float personalRating) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.personalRating = personalRating;
    }

    public Movie(int id, String name, float rating, String fileLink, Category category, float personalRating) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.fileLink = fileLink;
        this.category = category;
        this.personalRating = personalRating;
    }

    // Default constructor
    public Movie() {
        this.id = 0;
        this.name = "";
        this.rating = 0.0f;
        this.fileLink = "";
        this.lastView = LocalDate.now();  // Use current date by default
        this.personalRating = 0.0f;
        this.category = null;
    }

    public Movie(int i, String movieTitle, float rating, float v, Category category) {
        this.id = i;
        this.name = movieTitle;
        this.rating = rating;
        this.personalRating = v;
        this.category = category;
    }

    // Getters and Setters
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

    public LocalDate getLastView() {
        return lastView;  // Return as LocalDate
    }

    public void setLastView(LocalDate lastView) {
        this.lastView = lastView;
    }

    public float getPersonalRating() {
        return personalRating;
    }

    public void setPersonalRating(float personalRating) {
        this.personalRating = personalRating;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    // This method will return the formatted string for displaying in the TableView
    public String getFormattedLastView() {
        if (lastView != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return lastView.format(formatter);  // Format as "yyyy-MM-dd"
        } else {
            return "No Date";  // Or return an empty string if you prefer
        }
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", category=" + (category != null ? category.getName() : "No Category") +
                ", lastView=" + (lastView != null ? lastView : "No Date") +
                '}';
    }
}
