package edu.uci.ics.tingcl2.service.idm.resources;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.tingcl2.service.idm.core.ValidatePrivilege;
import edu.uci.ics.tingcl2.service.idm.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.idm.models.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import javax.ws.rs.core.Response.Status;

@SuppressWarnings("Duplicates")

@Path("privilege")
public class PrivilegePage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyPrivilege(String jsonText) {
        ServiceLogger.LOGGER.info("Received request for privilege verification.");
        PrivilegeRequestModel requestModel;
        PrivilegeResponseModel responseModel = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            requestModel = mapper.readValue(jsonText, PrivilegeRequestModel.class);
            ServiceLogger.LOGGER.info("Checking privilege for plausible correctness.");
            int generalCase = ValidatePrivilege.surface(requestModel);
            if (generalCase != 0) {
                responseModel = new PrivilegeResponseModel(generalCase);
                return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
            }
            responseModel = new PrivilegeResponseModel(ValidatePrivilege.handler(requestModel));
            return Response.status(Status.OK).entity(responseModel).build();
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                responseModel = new PrivilegeResponseModel(-2);
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                responseModel = new PrivilegeResponseModel(-3);
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
            }
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }
    }
}
