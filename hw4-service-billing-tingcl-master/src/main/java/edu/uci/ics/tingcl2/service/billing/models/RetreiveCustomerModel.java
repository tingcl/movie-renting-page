package edu.uci.ics.tingcl2.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RetreiveCustomerModel {
    @JsonProperty(value = "resultCode", required = true)
    int resultCode;
    @JsonProperty(value = "message", required = true)
    String message;
    @JsonProperty(value = "customer")
    CustomerModel customer;

    @JsonCreator
    public RetreiveCustomerModel(int resultCode, CustomerModel customer) {
        this.resultCode = resultCode;
        this.customer = customer;
        switch (this.resultCode) {
            case -3:
                message = "JSON Parse Exception.";
                break;
            case -2:
                message = "JSON Mapping Exception.";
                break;
            case -1:
                message = "Internal Server Error.";
                break;
            case 332:
                message = "Customer does not exist.";
                break;
            case 3320:
                message = "Customer retrieved successfully.";
                break;
        }
    }

}
