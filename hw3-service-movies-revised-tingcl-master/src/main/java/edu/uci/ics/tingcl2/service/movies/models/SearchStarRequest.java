package edu.uci.ics.tingcl2.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchStarRequest {
    private String name;
    private Integer birthYear;
    private String movieTitle;
    private int limit;
    private int offset;
    private String orderby;
    private String direction;

    @JsonCreator
    public SearchStarRequest (
            @JsonProperty(value = "name") String name,
            @JsonProperty(value = "birthYear") Integer birthYear,
            @JsonProperty(value = "movieTitle") String movieTitle,
            @JsonProperty(value = "limit") int limit,
            @JsonProperty(value = "offset") int offset,
            @JsonProperty(value = "orderby") String orderby,
            @JsonProperty(value = "direction") String direction){
        this.name = name;
        this.birthYear = birthYear;
        this.movieTitle = movieTitle;
        this.limit = limit;
        this.offset = offset;
        this.orderby = orderby;
        this.direction = direction;
    }

    public String getName() {
        return name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public String getMovieTitle() {
        return movieTitle;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
