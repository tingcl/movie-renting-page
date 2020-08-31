package edu.uci.ics.tingcl2.service.basic.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.tingcl2.service.basic.core.StringRecords;
import edu.uci.ics.tingcl2.service.basic.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.basic.models.ValidateStringRequestModel;
import edu.uci.ics.tingcl2.service.basic.core.ValidateString;
import edu.uci.ics.tingcl2.service.basic.models.ValidateStringResponseModel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;

@Path("validateString")
public class ValidateStringPage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateString(String jsonText){

        ObjectMapper mapper = new ObjectMapper();
        ValidateStringRequestModel requestModel;
        ValidateStringResponseModel responseModel;

        try {
            // Map the JSON to an appropriate request model
            requestModel = mapper.readValue(jsonText, ValidateStringRequestModel.class);
            // Verify the data in the request is valid
            responseModel = ValidateString.validate(requestModel);

            // Return Response Codes and Response Models
            if (responseModel.getResultCode() == 0) {
                StringRecords.insertSentenceToDB(requestModel);
                return Response.status(Status.OK).entity(responseModel).build();
            }
            if (responseModel.getResultCode() == 1) {
                return Response.status(Status.OK).entity(responseModel).build();
            }
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();

        }
        catch (IOException e) {
            //If mapping error, return response model case 2
            responseModel = new ValidateStringResponseModel(2);
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
            }
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }
    }
}