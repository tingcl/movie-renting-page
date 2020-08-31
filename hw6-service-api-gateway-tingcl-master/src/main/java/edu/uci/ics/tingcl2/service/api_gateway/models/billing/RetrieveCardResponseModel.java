package edu.uci.ics.tingcl2.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.tingcl2.service.api_gateway.utilities.ResultCodes;

@JsonIgnoreProperties(value = { "dataValid" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RetrieveCardResponseModel {
    @JsonProperty(value = "resultCode", required = true)
    int resultCode;
    @JsonProperty(value = "message", required = true)
    String message;
    @JsonProperty(value = "creditcard")
    CardModel creditcard;

    @JsonCreator
    public RetrieveCardResponseModel(int resultCode, CardModel creditcard) {
        this.resultCode = resultCode;
        this.creditcard = creditcard;
        this.message = ResultCodes.setMessage(resultCode);
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    public CardModel getCreditcard() {
        return creditcard;
    }
}