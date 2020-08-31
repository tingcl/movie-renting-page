package edu.uci.ics.tingcl2.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
Used for both retriee and clear

 */


@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RetrieveRequestModel {
    @JsonProperty(value = "email")
    String email;

    @JsonCreator
    public RetrieveRequestModel(@JsonProperty(value = "email", required = true) String email){
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
