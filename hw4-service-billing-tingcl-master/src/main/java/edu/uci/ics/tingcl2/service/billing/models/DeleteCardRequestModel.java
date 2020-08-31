package edu.uci.ics.tingcl2.service.billing.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteCardRequestModel {
    @JsonProperty(value = "id")
    String id;
    public DeleteCardRequestModel(@JsonProperty(value = "id", required = true) String id){
        this.id = id;
    }
    public String getId() {
        return id;
    }
}
