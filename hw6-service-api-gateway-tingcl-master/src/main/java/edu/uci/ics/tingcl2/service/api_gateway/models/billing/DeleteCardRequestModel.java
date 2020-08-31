package edu.uci.ics.tingcl2.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.uci.ics.tingcl2.service.api_gateway.models.RequestModel;

public class DeleteCardRequestModel extends RequestModel {
    @JsonProperty(value = "id")
    private String id;

    public DeleteCardRequestModel(@JsonProperty(value = "id", required = true) String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
