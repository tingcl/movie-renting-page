package edu.uci.ics.tingcl2.service.api_gateway.models.gateway;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.tingcl2.service.api_gateway.models.Model;


@JsonIgnoreProperties(value = { "dataValid" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GatewayReportResponseModel extends Model {
    private String jsonText;
    private int httpstatus;

    @JsonCreator
    public GatewayReportResponseModel(@JsonProperty(value = "jsonText", required = true) String jsonText,
                                      @JsonProperty(value = "httpstatus", required = true) int httpstatus) {
        this.jsonText = jsonText;
        this.httpstatus = httpstatus;
    }

    public String getJsonText() {
        return jsonText;
    }

    public int getHttpstatus() {
        return httpstatus;
    }
}