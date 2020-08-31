package edu.uci.ics.tingcl2.service.billing.models;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteModel {
    @JsonProperty(value = "resultCode")
    int resultCode;
    @JsonProperty(value = "message")
    String message;
    @JsonCreator
    public DeleteModel(@JsonProperty(value = "resultCode", required = true) int resultCode){
        this.resultCode = resultCode;
        switch(this.resultCode) {
            case -11:
                message = "Email address has invalid format.";
                break;
            case -10:
                message = "Email address has invalid length.";
                break;
            case -3:
                message = "JSON Parse Exception.";
                break;
            case -2:
                message = "JSON Mapping Exception.";
                break;
            case -1:
                message = "Internal Server Error.";
                break;
            case 312:
                message = "Shopping item does not exist.";
                break;
            case 3120:
                message = "Shopping cart item deleted successfully.";
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
