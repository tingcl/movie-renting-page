package edu.uci.ics.tingcl2.service.billing.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteRequestModel {
    @JsonProperty(value = "email")
    String email;
    @JsonProperty(value = "movieId")
    String movieId;
    public DeleteRequestModel(@JsonProperty(value = "email", required = true) String email,
                              @JsonProperty(value = "movieId", required = true) String movieId){
        this.email = email;
        this.movieId = movieId;
    }

    public String getEmail() {
        return email;
    }

    public String getMovieId() {
        return movieId;
    }
}
