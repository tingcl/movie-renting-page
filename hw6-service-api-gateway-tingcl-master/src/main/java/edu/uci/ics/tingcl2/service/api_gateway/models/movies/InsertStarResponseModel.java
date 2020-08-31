package edu.uci.ics.tingcl2.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.tingcl2.service.api_gateway.models.Model;
import edu.uci.ics.tingcl2.service.api_gateway.utilities.ResultCodes;

@JsonIgnoreProperties(value = { "dataValid" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertStarResponseModel extends Model {
    private int resultCode;
    private String message;

    public InsertStarResponseModel (@JsonProperty(value = "resultCode", required = true) int resultCode){
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(resultCode);

    }
    public int getResultCode() { return resultCode; }
    public String getMessage() { return message; }
}
