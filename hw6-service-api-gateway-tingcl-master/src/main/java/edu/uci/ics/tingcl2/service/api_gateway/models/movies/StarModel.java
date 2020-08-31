package edu.uci.ics.tingcl2.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = { "dataValid" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StarModel {
    private String id;
    private String name;
    private Integer birthYear;
    private MovieModel[] movies;

    @JsonCreator
    public StarModel(@JsonProperty(value = "id") String id,
                     @JsonProperty(value = "name") String name,
                     @JsonProperty(value = "birthYear") Integer birthYear,
                     @JsonProperty(value = "movies") MovieModel[] movies){
        this.id = id;
        this.name = name;
        this.birthYear = birthYear;
        this.movies = movies;
    }
    @JsonProperty("id")
    public String getId() { return id; }
    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("birthYear")
    public Integer getBirthYear() { return birthYear; }
    @JsonProperty("movies")
    public MovieModel[] getMovies() { return movies; }
}
