package edu.uci.ics.tingcl2.service.movies.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetStarIdResponse {
    private int resultCode;
    private String message;
    private StarModel star;

    public GetStarIdResponse(@JsonProperty(value = "resultCode", required = true) int resultCode,
                              @JsonProperty(value = "star", required = true) StarModel star){
        this.resultCode = resultCode;
        this.star = star;
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
    public int getResultCode() { return resultCode; }

    public String getMessage() { return message; }

    public StarModel getStar() { return star; }
}
