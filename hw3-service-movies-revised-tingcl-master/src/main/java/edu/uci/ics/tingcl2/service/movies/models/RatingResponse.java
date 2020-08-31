package edu.uci.ics.tingcl2.service.movies.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RatingResponse {
    @JsonProperty(value = "resultCode")
    private int resultCode;
    @JsonProperty(value = "message")
    private String message;

    public RatingResponse(@JsonProperty(value = "resultCode", required = true) int resultCode){
        this.resultCode = resultCode;
        switch(this.resultCode){
            case -3:
                message = "JSON parse exception.";
                break;
            case -2:
                message = "JSON mapping exception.";
                break;
            case -1:
                message = "Internal server error.";
                break;
            case 211:
                message = "No movies found with search parameters.";
                break;
            case 250:
                message = "Rating successfully updated.";
                break;
            case 251:
                message = "Could not update rating.";
                break;
        }

    }
    @JsonProperty(value = "resultCode")
    public int getResultCode() { return resultCode; }

    @JsonProperty(value = "message")
    public String getMessage() { return message; }
}
