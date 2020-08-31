package edu.uci.ics.tingcl2.service.movies.core;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.tingcl2.service.movies.MovieService;
import edu.uci.ics.tingcl2.service.movies.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.movies.models.VerifyPrivilegeRequest;
import edu.uci.ics.tingcl2.service.movies.models.VerifyPrivilegeResponse;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class VerifyUserPrivilege {
    public static VerifyPrivilegeResponse hasAuthorization(String email, int plevel) {
        ServiceLogger.LOGGER.info("Verifying privilege level with IDM...");
        // Create a new Client
        ServiceLogger.LOGGER.info("Building client...");
        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);
        // Get the URI for the IDM
        ServiceLogger.LOGGER.info("Building URI...");
        String IDM_URI = MovieService.getMovieConfigs().getIdmConfigs().getIdmUri();
        ServiceLogger.LOGGER.info("Setting path to endpoint...");
        String IDM_ENDPOINT_PATH = MovieService.getMovieConfigs().getIdmConfigs().getPrivilegePath();
        // Create a WebTarget to send a request at
        ServiceLogger.LOGGER.info("Building WebTarget...");
        WebTarget webTarget = client.target(IDM_URI).path(IDM_ENDPOINT_PATH);
        // Create an InvocationBuilder to create the HTTP request
        ServiceLogger.LOGGER.info("Starting invocation builder...");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        // Set the payload
        ServiceLogger.LOGGER.info("Setting payload of the request");
        VerifyPrivilegeRequest verifyPrivilegeRequest = new VerifyPrivilegeRequest(email, plevel);
        // Send the request and save it to a Response
        ServiceLogger.LOGGER.info("Sending request...");
        Response response = invocationBuilder.post(Entity.entity(verifyPrivilegeRequest, MediaType.APPLICATION_JSON));
        ServiceLogger.LOGGER.info("Sent!");
        String jsonText = response.readEntity(String.class);
        ServiceLogger.LOGGER.info("jsonText: " + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        VerifyPrivilegeResponse verifyPrivilegeResponse = null;
        try {
            verifyPrivilegeResponse = mapper.readValue(jsonText, VerifyPrivilegeResponse.class);
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
        return verifyPrivilegeResponse;
    }
}
