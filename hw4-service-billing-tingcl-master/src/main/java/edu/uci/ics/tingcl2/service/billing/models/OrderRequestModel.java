package edu.uci.ics.tingcl2.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequestModel {
    @JsonProperty(value = "email")
    private String email;
    @JsonCreator
    public OrderRequestModel(@JsonProperty(value = "email", required = true) String email){
        this.email = email;
    }
    public String getEmail() { return email; }
}
