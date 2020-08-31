package edu.uci.ics.tingcl2.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.uci.ics.tingcl2.service.api_gateway.utilities.ResultCodes;

@JsonIgnoreProperties(value = { "dataValid" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteCardResponseModel {
    @JsonProperty(value = "resultCode")
    int resultCode;
    @JsonProperty(value = "message")
    String message;
    @JsonCreator
    public DeleteCardResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode) {
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(resultCode);
    }
}