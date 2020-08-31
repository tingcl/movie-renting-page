package edu.uci.ics.tingcl2.service.api_gateway.models.idm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.tingcl2.service.api_gateway.models.RequestModel;

public class VerifyPrivilegeRequestModel extends RequestModel {
    private String email;
    private int plevel;

    @JsonCreator
    public VerifyPrivilegeRequestModel(
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "plevel", required = true) int plevel) {
        this.email = email;
        this.plevel = plevel;
    }
    @JsonProperty("email")
    public String getEmail() { return email; }
    @JsonProperty("plevel")
    public int getPlevel() { return plevel; }

}
