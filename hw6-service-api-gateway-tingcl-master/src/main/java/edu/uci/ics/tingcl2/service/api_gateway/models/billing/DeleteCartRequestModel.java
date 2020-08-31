package edu.uci.ics.tingcl2.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.tingcl2.service.api_gateway.models.RequestModel;


public class DeleteCartRequestModel extends RequestModel {
    private String email;
    private String movieId;
    @JsonCreator
    public DeleteCartRequestModel(@JsonProperty(value = "email", required = true) String email,
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
