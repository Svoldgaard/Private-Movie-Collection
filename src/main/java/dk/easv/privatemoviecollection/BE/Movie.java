package dk.easv.privatemoviecollection.BE;

import java.sql.Date;
import java.time.LocalDate;

public class Movie {

    private int id;
    private String name;
    private float rating;
    private String fileLink;
    private LocalDate lastView;
    private String category;  // New field

    public Movie(int id, String name, float rating, String fileLink, LocalDate lastView) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.fileLink = fileLink;
        this.lastView = lastView;

    }

    public String getCategory() {
        return category;
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

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", fileLink='" + fileLink + '\'' +
                ", lastView=" + lastView +
                ", category='" + category + '\'' +
                '}';
    }
}
