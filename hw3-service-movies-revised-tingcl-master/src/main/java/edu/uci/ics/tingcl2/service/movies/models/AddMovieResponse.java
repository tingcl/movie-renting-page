package edu.uci.ics.tingcl2.service.movies.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddMovieResponse {
    @JsonProperty(value = "resultCode", required = true)
    private int resultCode;
    @JsonProperty(value = "message", required = true)
    private String message;
    @JsonProperty(value = "movieid", required = true)
    private String movieid;
    @JsonProperty(value = "genreid", required = true)
    private int[] genreid;
    public AddMovieResponse(@JsonProperty(value = "resultCode", required = true) int resultCode,
                            @JsonProperty(value = "movieid", required = true) String movieid,
                            @JsonProperty(value = "genreid", required = true) int[] genreid){
        this.resultCode = resultCode;
        this.movieid = movieid;
        this.genreid = genreid;
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
            case 214:
                message = "Movie successfully added.";
                break;
            case 215:
                message = "Could not add movie.";
                break;
            case 216:
                message = "Movie already exists.";
                break;
        }
    }
    public int getResultCode() { return resultCode; }
    public String getMessage() {
        return message;
    }
    public String getMovieid() {
        return movieid;
    }
    public int[] getGenreid() {
        return genreid;
    }
}
