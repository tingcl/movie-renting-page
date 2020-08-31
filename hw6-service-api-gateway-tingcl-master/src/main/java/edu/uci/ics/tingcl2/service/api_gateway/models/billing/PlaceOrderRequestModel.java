package edu.uci.ics.tingcl2.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.tingcl2.service.api_gateway.models.RequestModel;

public class PlaceOrderRequestModel extends RequestModel {
    private String email;
    @JsonCreator
    public PlaceOrderRequestModel(@JsonProperty(value = "email", required = true) String email){
        this.email = email;
    }
    public String getEmail() { return email; }
}