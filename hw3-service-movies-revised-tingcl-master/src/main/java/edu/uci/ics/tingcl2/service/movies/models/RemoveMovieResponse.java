package edu.uci.ics.tingcl2.service.movies.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RemoveMovieResponse {
    private int resultCode;
    private String message;

    public RemoveMovieResponse(@JsonProperty(value = "resultCode", required = true) int resultCode){
        this.resultCode = resultCode;
        this.message = message;
        switch(this.resultCode){
            case -1:
                message = "Internal server error.";
                break;
            case 141:
                message = "User has insufficient privilege.";
                break;
            case 240:
                message = "Movie successfully removed.";
                break;
            case 241:
                message = "Could not remove movie.";
                break;
            case 242:
                message = "Movie has been already removed.";
                break;
        }
    }
    public int getResultCode() {
        return resultCode;
    }
    public String getMessage() {
        return message;
    }
}
