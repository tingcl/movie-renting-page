package edu.uci.ics.tingcl2.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.tingcl2.service.api_gateway.models.Model;
import edu.uci.ics.tingcl2.service.api_gateway.utilities.ResultCodes;

@JsonIgnoreProperties(value = { "dataValid" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddMovieResponseModel extends Model {
    @JsonProperty(value = "resultCode", required = true)
    private int resultCode;
    @JsonProperty(value = "message", required = true)
    private String message;
    @JsonProperty(value = "movieid", required = true)
    private String movieid;
    @JsonProperty(value = "genreid", required = true)
    private int[] genreid;
    public AddMovieResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                                 @JsonProperty(value = "movieid", required = true) String movieid,
                                 @JsonProperty(value = "genreid", required = true) int[] genreid){
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(resultCode);
        this.movieid = movieid;
        this.genreid = genreid;
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
