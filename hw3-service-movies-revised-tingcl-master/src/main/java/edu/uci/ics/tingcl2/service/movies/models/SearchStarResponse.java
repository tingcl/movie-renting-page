package edu.uci.ics.tingcl2.service.movies.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchStarResponse {
    @JsonProperty(value = "resultCode")
    private int resultCode;
    @JsonProperty(value = "message")
    private String message;
    @JsonProperty(value = "stars")
    private StarModel[] stars;

    public SearchStarResponse(@JsonProperty(value = "resultCode", required = true) int resultCode,
                               @JsonProperty(value = "stars", required = true) StarModel[] stars){
        this.resultCode = resultCode;
        this.stars = stars;
        switch(this.resultCode){
            case -1:
                message = "Internal server error.";
                break;
            case 212:
                message = "Found stars with search parameters.";
                break;
            case 213:
                message = "No stars found with search parameters.";
                break;
        }

    }
    @JsonProperty(value = "resultCode")
    public int getResultCode() { return resultCode; }

    @JsonProperty(value = "message")
    public String getMessage() { return message; }

    @JsonProperty(value = "stars")
    public StarModel[] getStars() { return stars; }
}
