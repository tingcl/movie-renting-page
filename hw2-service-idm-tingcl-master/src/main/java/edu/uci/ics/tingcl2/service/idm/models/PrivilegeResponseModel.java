package edu.uci.ics.tingcl2.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PrivilegeResponseModel {
    private int resultCode;
    private String message;

    @JsonCreator
    public PrivilegeResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode){
        this.resultCode = resultCode;
        switch(this.resultCode) {
            case -14:
                message = "Privilege level out of valid range.";
                break;
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
                message = "JSON Mapping Exception";
                break;
            case -1:
                message = "Internal Server Error.";
                break;
            case 14:
                message = "User not found.";
                break;
            case 140:
                message = "User has sufficient privilege level.";
                break;
            case 141:
                message = "User has insufficient privilege level.";
                break;
        }
    }
    @JsonProperty("resultCode")
    public int getResultCode() {
        return resultCode;
    }
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }
}
