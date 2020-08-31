package edu.uci.ics.tingcl2.service.basic.resources;

import edu.uci.ics.tingcl2.service.basic.core.Record;
import edu.uci.ics.tingcl2.service.basic.core.StringRecords;
import edu.uci.ics.tingcl2.service.basic.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.basic.models.GetIdResponseModel;
import edu.uci.ics.tingcl2.service.basic.models.GetRecordsResponseModel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("get")
public class GetPage {
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecordCount(){
        try{
            ServiceLogger.LOGGER.info("Received request for a count of all records.");
            GetRecordsResponseModel responseModel = new GetRecordsResponseModel(StringRecords.retrieveRecordCount());
            ServiceLogger.LOGGER.info("Successful response model");
            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    @Path("{id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response IdSearch(@PathParam("id") int id){
        try{
            ServiceLogger.LOGGER.info("Received request for id record withdraw.");
            GetIdResponseModel responseModel;
            Record record = StringRecords.retrieveRecord(id);
            if(record == null){
                responseModel = new GetIdResponseModel(record, 1);
            } else {
                responseModel = new GetIdResponseModel(record, 0);
            }
            ServiceLogger.LOGGER.info("ResponseModel data is valid.");
            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
