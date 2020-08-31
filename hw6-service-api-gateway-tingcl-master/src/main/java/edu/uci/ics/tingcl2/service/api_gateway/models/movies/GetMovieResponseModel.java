package edu.uci.ics.tingcl2.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.tingcl2.service.api_gateway.utilities.ResultCodes;

@JsonIgnoreProperties(value = { "dataValid" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetMovieResponseModel {
    @JsonProperty(value = "resultCode", required = true)
    private int resultCode;
    @JsonProperty(value = "message", required = true)
    private String message;
    @JsonProperty(value = "movies")
    private MovieModel movies;
    @JsonProperty(value = "response")
    private VerifyPrivilegeResponseModel response;

    @JsonCreator
    public GetMovieResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                                 @JsonProperty(value = "movies") MovieModel movies,
                                 @JsonProperty(value = "response") VerifyPrivilegeResponseModel response) {
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(resultCode);
        this.movies = movies;
        this.response = response;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    public MovieModel getMovies() {
        return movies;
    }

    public VerifyPrivilegeResponseModel getResponse() {
        return response;
    }
}
