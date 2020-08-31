package edu.uci.ics.tingcl2.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.tingcl2.service.api_gateway.models.Model;

@JsonIgnoreProperties(value = { "dataValid" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchMovieResponseModel extends Model {
    @JsonProperty(value = "resultCode")
    private int resultCode;
    @JsonProperty(value = "message")
    private String message;
    @JsonProperty(value = "movies")
    private MovieModel[] movies;

    public SearchMovieResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                                    @JsonProperty(value = "movies", required = true) MovieModel[] movies){
        this.resultCode = resultCode;
        this.movies = movies;
        switch(this.resultCode){
            case -1:
                message = "Internal server error.";
                break;
            case 210:
                message = "Found movies with search parameters.";
                break;
            case 211:
                message = "No movies found with search parameters.";
                break;
        }

    }
    @JsonProperty(value = "resultCode")
    public int getResultCode() { return resultCode; }

    @JsonProperty(value = "message")
    public String getMessage() { return message; }

    @JsonProperty(value = "movies")
    public MovieModel[] getMovies() { return movies; }
}