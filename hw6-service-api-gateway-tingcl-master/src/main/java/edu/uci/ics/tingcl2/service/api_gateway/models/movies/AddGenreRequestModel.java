package edu.uci.ics.tingcl2.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.tingcl2.service.api_gateway.models.RequestModel;

@JsonIgnoreProperties(value = { "dataValid" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddGenreRequestModel extends RequestModel {
    @JsonProperty(value = "name", required = true)
    private String name;
    @JsonCreator
    public AddGenreRequestModel(
            @JsonProperty(value = "name", required = true) String name) {
        this.name = name;
    }
    public String getName() { return name; }
}
