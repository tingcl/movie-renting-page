package edu.uci.ics.tingcl2.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompleteOrderModel {
    @JsonProperty(value = "resultCode")
    private int resultCode;
    @JsonProperty(value = "message")
    private String message;
    @JsonCreator
    public CompleteOrderModel(@JsonProperty(value = "resultCode", required = true) int resultCode){
        this.resultCode = resultCode;
        switch(this.resultCode){
            case -1:
                message = "Internal Server Error.";
                break;
            case 3421:
                message = "Token not found.";
                break;
            case 3422:
                message = "Payment can not be completed.";
                break;
            case 3420:
                message = "Payment is completed successfully.";
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
