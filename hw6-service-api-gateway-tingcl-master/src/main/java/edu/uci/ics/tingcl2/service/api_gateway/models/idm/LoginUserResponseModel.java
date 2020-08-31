package edu.uci.ics.tingcl2.service.api_gateway.models.idm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.tingcl2.service.api_gateway.models.Model;
import edu.uci.ics.tingcl2.service.api_gateway.utilities.ResultCodes;


@JsonIgnoreProperties(value = { "dataValid" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginUserResponseModel extends Model {
    private int resultCode;
    private String message;
    private String sessionID;

    @JsonCreator
    public LoginUserResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                                  @JsonProperty(value = "sessionID") String sessionID) {
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(resultCode);
        this.sessionID = sessionID;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[resultCode: " + resultCode + ", message: " + message + "]";
    }

    @JsonProperty(value = "resultCode", required = true)
    public int getResultCode() {
        return resultCode;
    }

    @JsonProperty(value = "message", required = true)
    public String getMessage() {
        return message;
    }
}