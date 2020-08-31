package edu.uci.ics.tingcl2.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RetrieveCustomerRequestModel {
    @JsonProperty(value = "email")
    String email;

    @JsonCreator
    public RetrieveCustomerRequestModel(@JsonProperty(value = "email", required = true) String email){
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}
