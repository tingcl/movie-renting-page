package edu.uci.ics.tingcl2.service.movies.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StarsInResponse {
    @JsonProperty(value = "resultCode")
    private int resultCode;
    @JsonProperty(value = "message")
    private String message;

    public StarsInResponse(@JsonProperty(value = "resultCode", required = true) int resultCode){
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
            case 141:
                message = "User has insufficient privilege.";
                break;
            case 221:
                message = "No movies found with search parameters.";
                break;
            case 230:
                message = "Star successfully added to movie.";
                break;
            case 231:
                message = "Could not add star to movie.";
                break;
            case 232:
                message = "Star already exists in movie.";
                break;
        }

    }
    @JsonProperty(value = "resultCode")
    public int getResultCode() { return resultCode; }

    @JsonProperty(value = "message")
    public String getMessage() { return message; }
}
