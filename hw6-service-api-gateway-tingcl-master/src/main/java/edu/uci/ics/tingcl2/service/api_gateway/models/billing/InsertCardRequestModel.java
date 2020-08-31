package edu.uci.ics.tingcl2.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.tingcl2.service.api_gateway.models.RequestModel;

import java.util.Date;

@JsonIgnoreProperties(value = { "dataValid" })
public class InsertCardRequestModel extends RequestModel {
    String id;
    String firstName;
    String lastName;
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