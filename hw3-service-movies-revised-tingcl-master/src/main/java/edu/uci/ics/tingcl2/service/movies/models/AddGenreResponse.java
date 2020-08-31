package edu.uci.ics.tingcl2.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddGenreResponse {
    private int resultCode;
    private String message;
    @JsonCreator
    public AddGenreResponse(@JsonProperty(value = "resultCode", required = true) int resultCode) {
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
            case 217:
                message = "Genre successfully added.";
                break;
            case 218:
                message = "Genre could not be added.";
                break;
        }
    }
    @JsonProperty("resultCode")
    public int getResultCode() {
        return resultCode;
    }
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }
}
