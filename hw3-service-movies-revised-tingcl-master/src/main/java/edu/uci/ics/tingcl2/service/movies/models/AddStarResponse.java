package edu.uci.ics.tingcl2.service.movies.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddStarResponse {
    private int resultCode;
    private String message;

    public AddStarResponse(@JsonProperty(value = "resultCode", required = true) int resultCode){
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
            case 220:
                message = "Star successfully added.";
                break;
            case 221:
                message = "Could not add star.";
                break;
            case 222:
                message = "Star already exists.";
                break;
        }
    }
    public int getResultCode() { return resultCode; }
    public String getMessage() { return message; }
}
