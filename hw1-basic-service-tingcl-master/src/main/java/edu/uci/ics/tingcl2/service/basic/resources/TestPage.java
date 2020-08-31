package edu.uci.ics.tingcl2.service.basic.resources;

import edu.uci.ics.tingcl2.service.basic.logger.ServiceLogger;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("test")
public class TestPage {
    @Path("hello")
    @GET
    public Response hello(){
        ServiceLogger.LOGGER.info("Hello!");
        return Response.status(Status.OK).build();
    }
}
