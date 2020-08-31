package edu.uci.ics.tingcl2.service.movies.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetGenreResponse {
    private int resultCode;
    private String message;
    private GenreModel[] genres;
    private VerifyPrivilegeResponse response;


    public GetGenreResponse(@JsonProperty(value = "resultCode", required = true) int resultCode,
                            @JsonProperty(value = "genres") GenreModel[] genres,
                            @JsonProperty(value = "response") VerifyPrivilegeResponse response){
        this.resultCode = resultCode;
        this.genres = genres;
        this.response = response;
        switch(this.resultCode){
            case -1:
                message = "Internal server error.";
                break;
            case 141:
                message = "User has insufficient privilege.";
                break;
            case 219:
                message = "Genres successfully retrieved.";
                break;
        }
    }

    public int getResultCode() { return resultCode; }

    public String getMessage() { return message; }

    public GenreModel[] getGenres() { return genres; }

    public VerifyPrivilegeResponse getResponse() { return response; }
}
