package edu.uci.ics.tingcl2.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchMovieRequest {
    private String title;
    private String genre;
    private int year;
    private String director;
    private boolean hidden;
    private int limit;
    private int offset;
    private String orderby;
    private String direction;

    @JsonCreator
    public SearchMovieRequest (
            @JsonProperty(value = "title") String title,
            @JsonProperty(value = "genre") String genre,
            @JsonProperty(value = "year") int year,
            @JsonProperty(value = "director")String director,
            @JsonProperty(value = "hidden") boolean hidden,
            @JsonProperty(value = "limit") int limit,
            @JsonProperty(value = "offset") int offset,
            @JsonProperty(value = "orderby") String orderby,
            @JsonProperty(value = "direction") String direction
    ){
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.director = director;
        this.hidden = hidden;
        this.limit = limit;
        this.offset = offset;
        this.orderby = orderby;
        this.direction = direction;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    public String getDirector() {
        return director;
    }

    public boolean getHidden() {
        return hidden;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public String getOrderby() {
        return orderby;
    }

    public String getDirection() {
        return direction;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
