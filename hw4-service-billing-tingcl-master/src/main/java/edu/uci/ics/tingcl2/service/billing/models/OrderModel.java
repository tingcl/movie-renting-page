package edu.uci.ics.tingcl2.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderModel {
    @JsonProperty(value = "resultCode")
    private int resultCode;
    @JsonProperty(value = "message")
    private String message;
    @JsonProperty(value = "redirectURL")
    private String redirectURL;
    @JsonProperty(value = "token")
    private String token;
    @JsonCreator
    public OrderModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                      @JsonProperty(value = "redirectURL") String redirectURL,
                      @JsonProperty(value = "token") String token){
        this.resultCode = resultCode;
        this.redirectURL = redirectURL;
        this.token = token;
        switch(this.resultCode){
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
            case 341:
                message = "Shopping cart for this customer not found.";
                break;
            case 342:
                message = "Create payment failed.";
                break;
            case 3400:
                message = "Order placed successfully";
                break;
        }
    }
    public int getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public String getToken() {
        return token;
    }
}
