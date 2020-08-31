package edu.uci.ics.tingcl2.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.tingcl2.service.api_gateway.models.Model;
import edu.uci.ics.tingcl2.service.api_gateway.utilities.ResultCodes;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertStarInResponseModel extends Model {
    @JsonProperty(value = "resultCode")
    private int resultCode;
    @JsonProperty(value = "message")
    private String message;

    public InsertStarInResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode){
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(resultCode);

    }
    @JsonProperty(value = "resultCode")
    public int getResultCode() { return resultCode; }

    @JsonProperty(value = "message")
    public String getMessage() { return message; }
}
