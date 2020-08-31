package edu.uci.ics.tingcl2.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.tingcl2.service.api_gateway.models.RequestModel;

@JsonIgnoreProperties(value = { "dataValid" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertStarInRequestModel extends RequestModel {
    private String starid;
    private String movieid;


    @JsonCreator
    public InsertStarInRequestModel (
            @JsonProperty(value = "starid") String starid,
            @JsonProperty(value = "movieid") String movieid){
        this.starid = starid;
        this.movieid = movieid;
    }

    public String getStarid() {
        return starid;
    }

    public String getMovieid() {
        return movieid;
    }
}
