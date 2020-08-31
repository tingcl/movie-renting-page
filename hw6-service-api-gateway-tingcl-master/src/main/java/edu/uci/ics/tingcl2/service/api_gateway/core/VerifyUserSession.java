package edu.uci.ics.tingcl2.service.api_gateway.core;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.tingcl2.service.api_gateway.GatewayService;
import edu.uci.ics.tingcl2.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.api_gateway.models.idm.VerifySessionRequestModel;
import edu.uci.ics.tingcl2.service.api_gateway.models.idm.VerifySessionResponseModel;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class VerifyUserSession {
    public static VerifySessionResponseModel hasValidSession(String email, String sessionID) {
        ServiceLogger.LOGGER.info("Verifying valid session ID...");
        // Create a new Client
        ServiceLogger.LOGGER.info("Building client...");
        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);
        // Get the URI for the IDM
        ServiceLogger.LOGGER.info("Building URI...");
        String IDM_URI = GatewayService.getIdmConfigs().getIdmUri();
        ServiceLogger.LOGGER.info("Setting path to endpoint...");
        String IDM_ENDPOINT_PATH = GatewayService.getIdmConfigs().getEPSessionVerify();
        // Create a WebTarget to send a request at
        ServiceLogger.LOGGER.info("Building WebTarget...");
        WebTarget webTarget = client.target(IDM_URI).path(IDM_ENDPOINT_PATH);
        // Create an InvocationBuilder to create the HTTP request
        ServiceLogger.LOGGER.info("Starting invocation builder...");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        // Set the payload
        ServiceLogger.LOGGER.info("Setting payload of the request");
        VerifySessionRequestModel verifySessionRequestModel = new VerifySessionRequestModel(email, sessionID);
        // Send the request and save it to a Response
        ServiceLogger.LOGGER.info("Sending request...");
        Response response = invocationBuilder.post(Entity.entity(verifySessionRequestModel, MediaType.APPLICATION_JSON));
        ServiceLogger.LOGGER.info("Sent!");
        String jsonText = response.readEntity(String.class);
        ServiceLogger.LOGGER.info("jsonText: " + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        try {
            VerifySessionResponseModel verifySessionResponseModel = mapper.readValue(jsonText, VerifySessionResponseModel.class);
            return verifySessionResponseModel;
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
            }
        }
        return null;
    }
}
