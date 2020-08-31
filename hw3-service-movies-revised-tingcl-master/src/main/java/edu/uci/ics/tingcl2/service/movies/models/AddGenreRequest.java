package edu.uci.ics.tingcl2.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddGenreRequest {
    @JsonProperty(value = "name", required = true)
    private String name;
    @JsonCreator
    public AddGenreRequest(
            @JsonProperty(value = "name", required = true) String name) {
        this.name = name;
    }
    public String getName() { return name; }
}
