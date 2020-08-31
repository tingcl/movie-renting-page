package edu.uci.ics.tingcl2.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InsertCustomerRequestModel {
    @JsonProperty(value = "email")
    String email;
    @JsonProperty(value = "firstName")
    String firstName;
    @JsonProperty(value = "lastName")
    String lastName;
    @JsonProperty(value = "ccId")
    String ccId;
    @JsonProperty(value = "address")
    String address;

    @JsonCreator
    public InsertCustomerRequestModel(@JsonProperty(value = "email", required = true) String email,
                                  @JsonProperty(value = "firstName", required = true) String firstName,
                                  @JsonProperty(value = "lastName", required = true) String lastName,
                                  @JsonProperty(value = "ccId", required = true) String ccId,
                                      @JsonProperty(value = "address", required = true) String address){
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ccId = ccId;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCcId() {
        return ccId;
    }

    public String getEmail() { return email; }

    public String getAddress() { return address; }
}
