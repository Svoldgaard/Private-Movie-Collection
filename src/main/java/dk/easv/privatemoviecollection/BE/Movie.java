package dk.easv.privatemoviecollection.BE;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Movie {

    private int id;
    private String name;
    private float rating;
    private String fileLink;
    private LocalDate lastView;  // Use LocalDate instead of Date
    private float personalRating;
    private Category category;
    private List<Category> categories;
    private String filePath;


    public Movie(int id, String name, float rating, String fileLink, LocalDate lastView, float personalRating) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.fileLink = fileLink;
        this.personalRating = personalRating;
        this.lastView = lastView;
        this.categories = new ArrayList<>();
    }

    public Movie(int id, String name, float rating, float personalRating, List<Category> categories) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.personalRating = personalRating;
        this.categories = categories;  // Store the list of categories
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

    public LocalDate getLastView() {
        return lastView;
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
            return "No Date";  
        }
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
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
