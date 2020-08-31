package edu.uci.ics.tingcl2.service.billing.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.tingcl2.service.billing.core.*;
import edu.uci.ics.tingcl2.service.billing.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.billing.models.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Path("creditcard")
public class CardPage {
    @Path("insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCard(String jsonText){
        ServiceLogger.LOGGER.info("Received request to insert card into creditcards.");
        ObjectMapper mapper = new ObjectMapper();
        InsertCardRequestModel requestModel;
        InsertCardModel model;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
            mapper.setDateFormat(dateFormat);
            requestModel = mapper.readValue(jsonText, InsertCardRequestModel.class);
            model = ValidateInsertCard.logicalHandler(requestModel);
            return Response.status(Response.Status.OK).entity(model).build();
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                model = new InsertCardModel(-2);
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                model = new InsertCardModel(-3);
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
                model = new InsertCardModel(-1);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
    }
    @Path("update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCard(String jsonText){
        ServiceLogger.LOGGER.info("Received request to update card information.");
        ObjectMapper mapper = new ObjectMapper();
        InsertCardRequestModel requestModel;
        UpdateCardModel model;
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
            mapper.setDateFormat(dateFormat);
            requestModel = mapper.readValue(jsonText, InsertCardRequestModel.class);
            model = ValidateUpdateCard.logicalHandler(requestModel);
            return Response.status(Response.Status.OK).entity(model).build();

        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                model = new UpdateCardModel(-2);
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                model = new UpdateCardModel(-3);
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
                model = new UpdateCardModel(-1);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
    }
    @Path("delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCard(String jsonText) {
        ServiceLogger.LOGGER.info("Received request to delete card id.");
        ObjectMapper mapper = new ObjectMapper();
        DeleteCardRequestModel requestModel;
        DeleteCardModel model;
        try {
            requestModel = mapper.readValue(jsonText, DeleteCardRequestModel.class);
            model = ValidateDeleteCard.logicalHandler(requestModel);
            return Response.status(Response.Status.OK).entity(model).build();
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                model = new DeleteCardModel(-2);
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                model = new DeleteCardModel(-3);
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
                model = new DeleteCardModel(-1);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
    }
    @Path("retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCard(String jsonText){
        ServiceLogger.LOGGER.info("Received request to retrieve card information.");
        ObjectMapper mapper = new ObjectMapper();
        DeleteCardRequestModel requestModel;
        RetrieveCardModel model;
        try{
            requestModel = mapper.readValue(jsonText, DeleteCardRequestModel.class);
            model = ValidateRetrieveCard.logicalHandler(requestModel);
            return Response.status(Response.Status.OK).entity(model).build();
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                model = new RetrieveCardModel(-2, null);
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                model = new RetrieveCardModel(-3, null);
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
                model = new RetrieveCardModel(-1, null);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(model).build();

    }

}
