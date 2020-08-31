package edu.uci.ics.tingcl2.service.idm.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.tingcl2.service.idm.core.UserRecords;
import edu.uci.ics.tingcl2.service.idm.core.ValidateLogin;
import edu.uci.ics.tingcl2.service.idm.core.ValidateRegister;
import edu.uci.ics.tingcl2.service.idm.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.idm.models.LoginResponseModel;
import edu.uci.ics.tingcl2.service.idm.models.RegisterRequestModel;
import edu.uci.ics.tingcl2.service.idm.security.Session;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("login")
public class LoginPage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(String jsonText) {
        ServiceLogger.LOGGER.info("Received request for login.");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        // Same fields as RegisterRequestModel
        RegisterRequestModel requestModel;
        LoginResponseModel responseModel = null;
        try{
            requestModel = mapper.readValue(jsonText, RegisterRequestModel.class);

            // Plausibility
            ServiceLogger.LOGGER.info("Checking login input for plausible correctness.");
            int generalCase = ValidateRegister.surface(requestModel);
            if(generalCase != 0){
                responseModel = new LoginResponseModel(generalCase, null);
                return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
            }
            // Logical handler
            ServiceLogger.LOGGER.info("Checking login input with logical handlers.");

            responseModel = new LoginResponseModel(ValidateLogin.handler(requestModel), null);

            if(responseModel.getResultCode() == -11){
                return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
            }
            if(responseModel.getResultCode() == 120){
                // Create a new session and revoke old sessions
                if(UserRecords.existSession(requestModel.getEmail())){
                    UserRecords.revokeSessions(requestModel.getEmail());
                }
                ServiceLogger.LOGGER.info("Adding session into database.");
                Session session = Session.createSession(requestModel.getEmail());
                UserRecords.addSession(session);
                // Successful login include session ID
                responseModel = new LoginResponseModel(120, session.getSessionID().toString());
            }
            return Response.status(Status.OK).entity(responseModel).build();
        }
        catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                responseModel = new LoginResponseModel(-2, null);
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                responseModel = new LoginResponseModel(-3, null);
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
            }
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }
    }
}
