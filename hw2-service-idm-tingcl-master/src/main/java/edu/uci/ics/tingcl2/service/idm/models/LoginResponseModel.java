package edu.uci.ics.tingcl2.service.idm.models;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * IDM Login
 *
 * Response model for login which is sent back after each request.
 */

public class LoginResponseModel {
    private int resultCode;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sessionID;

    @JsonCreator
    public LoginResponseModel(
            @JsonProperty(value = "code", required = true) int resultCode,
            @JsonProperty(value = "id", required = true) String sessionID){
        this.resultCode = resultCode;
        this.sessionID = sessionID;

        switch(this.resultCode){
            case -12:
                message = "Password has invalid length.";
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
                message = "JSON Mapping Exception.";
                break;
            case -1:
                message = "Internal server error.";
                break;
            case 11:
                message = "Passwords do not match.";
                break;
            case 14:
                message = "User not found.";
                break;
            case 120:
                message = "User logged in successfully.";
                break;
        }

    }

    @JsonProperty("resultCode")
    public int getResultCode() { return resultCode; }

    @JsonProperty("sessionID")
    public String getSessionID() { return sessionID; }

    @JsonProperty("message")
    public String getMessage() { return message; }
}
