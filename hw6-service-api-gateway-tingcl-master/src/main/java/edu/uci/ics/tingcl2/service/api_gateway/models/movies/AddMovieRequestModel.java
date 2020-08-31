package edu.uci.ics.tingcl2.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.tingcl2.service.api_gateway.models.RequestModel;

@JsonIgnoreProperties(value = { "dataValid" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddMovieRequestModel extends RequestModel {
    private String title;
    private String director;
    private int year;
    private String backdrop_path;
    private Integer budget;
    private String overview;
    private String poster_path;
    private Integer revenue;
    private GenreModel[] genres;

    @JsonCreator
    public AddMovieRequestModel(
            @JsonProperty(value = "title", required = true) String title,
            @JsonProperty(value = "director", required = true) String director,
            @JsonProperty(value = "year", required = true) int year,
            @JsonProperty(value = "backdrop_path") String backdrop_path,
            @JsonProperty(value = "budget") Integer budget,
            @JsonProperty(value = "overview") String overview,
            @JsonProperty(value = "poster_path") String poster_path,
            @JsonProperty(value = "revenue") Integer revenue,
            @JsonProperty(value = "genres", required = true) GenreModel[] genres){
        this.title = title;
        this.director = director;
        this.year = year;
        this.backdrop_path = backdrop_path;
        this.budget = budget;
        this.overview = overview;
        this.poster_path = poster_path;
        this.revenue = revenue;
        this.genres = genres;
    }
    public String getTitle() {
        return title;
    }
    public String getDirector() {
        return director;
    }
    public int getYear() {
        return year;
    }
    public String getBackdrop_path() {
        return backdrop_path;
    }
    public Integer getBudget() {
        return budget;
    }
    public String getOverview() {
        return overview;
    }
    public String getPoster_path() {
        return poster_path;
    }
    public Integer getRevenue() {
        return revenue;
    }
    public GenreModel[] getGenres() {
        return genres;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDirector(String director) {
        this.director = director;
    }
    public void setYear(int year) { this.year = year; }
    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }
    public void setBudget(Integer budget) {
        this.budget = budget;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }
    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }
    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }
    public void setGenres(GenreModel[] genres) {
        this.genres = genres;
    }
}

