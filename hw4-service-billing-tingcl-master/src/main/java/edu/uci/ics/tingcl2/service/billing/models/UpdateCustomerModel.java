package edu.uci.ics.tingcl2.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateCustomerModel {
    @JsonProperty(value = "resultCode")
    int resultCode;
    @JsonProperty(value = "message")
    String message;

    @JsonCreator
    public UpdateCustomerModel(@JsonProperty(value = "resultCode", required = true) int resultCode) {
        this.resultCode = resultCode;
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
            case 321:
                message = "Credit card ID has invalid length.";
                break;
            case 322:
                message = "Credit card ID has invalid value.";
                break;
            case 331:
                message = "Credit card ID not found.";
                break;
            case 332:
                message = "Customer does not exist.";
                break;
            case 3310:
                message = "Customer updated successfully.";
                break;
        }
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }
}