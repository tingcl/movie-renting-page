package edu.uci.ics.tingcl2.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetMovieIdResponse {
    @JsonProperty(value = "resultCode", required = true)
    private int resultCode;
    @JsonProperty(value = "message", required = true)
    private String message;
    @JsonProperty(value = "movies")
    private MovieModel movies;
    @JsonProperty(value = "response")
    private VerifyPrivilegeResponse response;

    @JsonCreator
    public GetMovieIdResponse(@JsonProperty(value = "resultCode", required = true) int resultCode,
                              @JsonProperty(value = "movies") MovieModel movies,
                              @JsonProperty(value = "response") VerifyPrivilegeResponse response){
        this.resultCode = resultCode;
        switch(this.resultCode){
            case -1:
                message = "Internal server error.";
                break;
            case 141:
                message = "User has insufficient privilege.";
                break;
            case 210:
                message = "Found movies with search parameters.";
                break;
            case 211:
                message = "No movies found with search parameters.";
                break;
        }
        this.movies = movies;
        this.response = response;

    }
    public int getResultCode() { return resultCode; }
    public String getMessage() { return message; }
    public MovieModel getMovies() { return movies; }
    public VerifyPrivilegeResponse getResponse() { return response; }
}

