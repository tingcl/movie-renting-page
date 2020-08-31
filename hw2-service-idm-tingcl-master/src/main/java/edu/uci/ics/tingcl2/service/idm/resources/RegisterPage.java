package edu.uci.ics.tingcl2.service.idm.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.tingcl2.service.idm.core.ValidateRegister;
import edu.uci.ics.tingcl2.service.idm.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.idm.models.RegisterRequestModel;
import edu.uci.ics.tingcl2.service.idm.models.RegisterResponseModel;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import javax.ws.rs.core.Response.Status;

@Path("register")
public class RegisterPage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(String jsonText){
        ServiceLogger.LOGGER.info("Received request to register.");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        RegisterResponseModel responseModel = null;
        RegisterRequestModel requestModel;
        try {
            requestModel = mapper.readValue(jsonText, RegisterRequestModel.class);

            // Plausibility
            ServiceLogger.LOGGER.info("Checking register input for plausible correctness.");
            int generalCase = ValidateRegister.surface(requestModel);
            if(generalCase != 0){
                ServiceLogger.LOGGER.info("Surface checks failed.");
                responseModel = new RegisterResponseModel(generalCase);
                if(generalCase == 12){
                    return Response.status(Status.OK).entity(responseModel).build();
                }
                return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
            }
            ServiceLogger.LOGGER.info("Surface checks successful.");

            // Logical handler
            ServiceLogger.LOGGER.info("Checking register input with logical handlers.");
            responseModel = new RegisterResponseModel(ValidateRegister.handler(requestModel));
            if(responseModel.getResultCode() == -11){
                return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
            }
            return Response.status(Status.OK).entity(responseModel).build();
        }
        catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                responseModel = new RegisterResponseModel(-2);
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                responseModel = new RegisterResponseModel(-3);
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
            }
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }
    }
}
