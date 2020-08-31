package edu.uci.ics.tingcl2.service.api_gateway.models.idm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.tingcl2.service.api_gateway.models.Model;
import edu.uci.ics.tingcl2.service.api_gateway.utilities.ResultCodes;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = { "dataValid" })
public class VerifySessionResponseModel extends Model {
    private int resultCode;
    private String message;
    private String sessionID;

    @JsonCreator
    public VerifySessionResponseModel(
            @JsonProperty(value = "resultCode", required = true) int resultCode,
            @JsonProperty(value = "sessionID", required = true) String sessionID) {
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(resultCode);
        this.sessionID = sessionID;
    }

    public int getResultCode() { return resultCode; }

    public String getMessage() { return message; }

    public String getSessionID() { return sessionID; }
}
