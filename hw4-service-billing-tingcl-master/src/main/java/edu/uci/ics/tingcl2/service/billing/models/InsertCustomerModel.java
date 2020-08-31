package edu.uci.ics.tingcl2.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InsertCustomerModel {
    @JsonProperty(value = "resultCode")
    int resultCode;
    @JsonProperty(value = "message")
    String message;

    @JsonCreator
    public InsertCustomerModel(@JsonProperty(value = "resultCode", required = true) int resultCode) {
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
            case 333:
                message = "Duplicate insertion.";
                break;
            case 3300:
                message = "Case 3300: Customer inserted successfully.";
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
