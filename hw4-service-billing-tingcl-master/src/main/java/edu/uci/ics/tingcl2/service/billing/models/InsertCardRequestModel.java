package edu.uci.ics.tingcl2.service.billing.models;

import com.fasterxml.jackson.annotation.*;

import java.util.Date;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertCardRequestModel {
    @JsonProperty(value = "id")
    String id;
    @JsonProperty(value = "firstName")
    String firstName;
    @JsonProperty(value = "lastName")
    String lastName;
    @JsonProperty(value = "expiration")
    Date expiration;

    @JsonCreator
    public InsertCardRequestModel(@JsonProperty(value = "id", required = true) String id,
                                  @JsonProperty(value = "firstName", required = true) String firstName,
                                  @JsonProperty(value = "lastName", required = true) String lastName,
                                  @JsonProperty(value = "expiration", required = true) Date expiration){
        this.id =id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.expiration = expiration;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getExpiration() {
        return expiration;
    }
}
