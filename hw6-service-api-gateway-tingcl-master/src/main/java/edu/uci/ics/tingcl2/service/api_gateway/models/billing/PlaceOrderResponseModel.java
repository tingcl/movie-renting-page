package edu.uci.ics.tingcl2.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.tingcl2.service.api_gateway.models.Model;
import edu.uci.ics.tingcl2.service.api_gateway.utilities.ResultCodes;

@JsonIgnoreProperties(value = { "dataValid" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceOrderResponseModel extends Model {
    @JsonProperty(value = "resultCode")
    private int resultCode;
    @JsonProperty(value = "message")
    private String message;
    @JsonProperty(value = "redirectURL")
    private String redirectURL;
    @JsonProperty(value = "token")
    private String token;
    @JsonCreator
    public PlaceOrderResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                                   @JsonProperty(value = "redirectURL") String redirectURL,
                                   @JsonProperty(value = "token") String token){
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(resultCode);
        this.redirectURL = redirectURL;
        this.token = token;

    }
    public int getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public String getToken() {
        return token;
    }
}