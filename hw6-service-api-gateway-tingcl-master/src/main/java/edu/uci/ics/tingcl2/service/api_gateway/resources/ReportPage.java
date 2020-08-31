package edu.uci.ics.tingcl2.service.api_gateway.resources;

import edu.uci.ics.tingcl2.service.api_gateway.GatewayService;
import edu.uci.ics.tingcl2.service.api_gateway.core.Database;
import edu.uci.ics.tingcl2.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.api_gateway.models.gateway.GatewayReportResponseModel;

import javax.ws.rs.Path;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("report")
public class ReportPage {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReportRequest(@Context HttpHeaders headers){

        ServiceLogger.LOGGER.info("Received request for gateway report.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        ServiceLogger.LOGGER.info("email: " +  email);
        ServiceLogger.LOGGER.info("sessionID: " + sessionID);
        ServiceLogger.LOGGER.info("transactionID: " + transactionID);
        GatewayReportResponseModel response = Database.requestGatewayReport(transactionID);

        if(response == null){
            return Response.status(Status.NO_CONTENT).header("message", "No response to return").header("delay",
                    GatewayService.getGatewayConfigs().getRequestDelay()).header("transactionid", transactionID)
                    .header("email", email).header("sessionID", sessionID).build();
        }
        else{
            Database.removeCompletedRequest(transactionID);
            return Response.status(response.getHttpstatus()).entity(response.getJsonText()).build();
        }
    }
}
