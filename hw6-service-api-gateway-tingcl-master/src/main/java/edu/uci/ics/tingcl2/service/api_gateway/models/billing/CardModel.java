package edu.uci.ics.tingcl2.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardModel {
    @JsonProperty(value = "id")
    String id;
    @JsonProperty(value = "firstName")
    String firstName;
    @JsonProperty(value = "lastName")
    String lastName;
    @JsonProperty(value = "expiration")
    String expiration;

    @JsonCreator
    public CardModel(@JsonProperty(value = "id", required = true) String id,
                     @JsonProperty(value = "firstName", required = true) String firstName,
                     @JsonProperty(value = "lastName", required = true) String lastName,
                     @JsonProperty(value = "expiration", required = true) String expiration){
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

    public String getExpiration() {
        return expiration;
    }
}
