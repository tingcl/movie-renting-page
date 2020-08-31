package edu.uci.ics.tingcl2.service.idm.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.tingcl2.service.idm.core.ValidateSession;
import edu.uci.ics.tingcl2.service.idm.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.idm.models.SessionModel;
import edu.uci.ics.tingcl2.service.idm.models.SessionRequestModel;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;


@Path("session")
public class SessionPage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifySession(String jsonText) {
        ServiceLogger.LOGGER.info("Received request for session verification.");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        SessionRequestModel requestModel;
        SessionModel responseModel = null;
        try{
            requestModel = mapper.readValue(jsonText, SessionRequestModel.class);
            // Plausibility
            ServiceLogger.LOGGER.info("Checking register for plausible correctness.");
            int prePass = ValidateSession.surface(requestModel);
            if(prePass != 0){
                responseModel = new SessionModel(prePass, null);
                return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
            }

            // Logical handler
            ServiceLogger.LOGGER.info("Checking session with logical handlers.");
            int caseNum = ValidateSession.sessionHandler(requestModel);
            responseModel = new SessionModel(caseNum, null);
            if(caseNum == 130){
                responseModel = new SessionModel(caseNum, requestModel.getSessionID());
            }
            return Response.status(Status.OK).entity(responseModel).build();
        }
        catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                responseModel = new SessionModel(-2, null);
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                responseModel = new SessionModel(-3, null);
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
            }
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }
    }
}
