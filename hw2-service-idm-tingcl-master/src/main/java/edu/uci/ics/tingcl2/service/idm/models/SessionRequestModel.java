package edu.uci.ics.tingcl2.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SessionRequestModel {
    private String email;
    private String sessionID;

    @JsonCreator
    public SessionRequestModel(
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "sessionID", required = true) String sessionID) {
        this.email = email;
        this.sessionID = sessionID;
    }
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }
    @JsonProperty("sessionID")
    public String getSessionID() { return sessionID; }
}
