package edu.uci.ics.tingcl2.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.tingcl2.service.api_gateway.models.RequestModel;

@JsonIgnoreProperties(value = { "dataValid" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdatingRatingRequestModel extends RequestModel {
    private String id;
    private float rating;


    @JsonCreator
    public UpdatingRatingRequestModel(
            @JsonProperty(value = "id") String id,
            @JsonProperty(value = "rating") float rating) {
        this.id = id;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public float getRating() {
        return rating;
    }
}
