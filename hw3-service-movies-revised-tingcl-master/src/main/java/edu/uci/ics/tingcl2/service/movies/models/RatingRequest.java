package edu.uci.ics.tingcl2.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RatingRequest {
    private String id;
    private float rating;


    @JsonCreator
    public RatingRequest (
            @JsonProperty(value = "id") String id,
            @JsonProperty(value = "rating") float rating){
        this.id= id;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public float getRating() {
        return rating;
    }
}
