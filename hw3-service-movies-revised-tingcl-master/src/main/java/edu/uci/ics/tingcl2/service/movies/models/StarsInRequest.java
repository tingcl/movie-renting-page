package edu.uci.ics.tingcl2.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StarsInRequest {
    private String starid;
    private String movieid;


    @JsonCreator
    public StarsInRequest (
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
