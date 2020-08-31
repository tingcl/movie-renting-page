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
import javax.ws.rs.core.Response.Status;

@Path("cart")
public class CartPage {
    @Path("insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCart(String jsonText) {
        ServiceLogger.LOGGER.info("Received request to insert movie into cart.");
        ObjectMapper mapper = new ObjectMapper();
        InsertRequestModel requestModel;
        InsertModel model;
        try {
            requestModel = mapper.readValue(jsonText, InsertRequestModel.class);
            model = ValidateInsert.logicalHandler(requestModel);
            if (model.getResultCode() == 33 || model.getResultCode() == 311 || model.getResultCode() == 3100)
                return Response.status(Status.OK).entity(model).build();

        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                model = new InsertModel(-2);
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                model = new InsertModel(-3);
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
                model = new InsertModel(-1);
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(model).build();
            }
        }
        return Response.status(Status.BAD_REQUEST).entity(model).build();
    }
    @Path("update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCart(String jsonText){
        ServiceLogger.LOGGER.info("Received request to update cart.");
        ObjectMapper mapper = new ObjectMapper();
        InsertRequestModel requestModel;
        UpdateModel model;
        try{
            requestModel = mapper.readValue(jsonText, InsertRequestModel.class);
            model = ValidateUpdate.logicalHandler(requestModel);
            if (model.getResultCode() == 33 || model.getResultCode() == 312 || model.getResultCode() == 3110)
                return Response.status(Status.OK).entity(model).build();

        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                model = new UpdateModel(-2);
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                model = new UpdateModel(-3);
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
                model = new UpdateModel(-1);
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(model).build();
            }
        }
        return Response.status(Status.BAD_REQUEST).entity(model).build();
    }
    @Path("delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCart(String jsonText){
        ServiceLogger.LOGGER.info("Received request to delete cart item.");
        ObjectMapper mapper = new ObjectMapper();
        DeleteRequestModel requestModel;
        DeleteModel model;
        try{
            requestModel = mapper.readValue(jsonText, DeleteRequestModel.class);
            model = ValidateDelete.logicalHandler(requestModel);
            if (model.getResultCode() == 312 || model.getResultCode() == 3120)
                return Response.status(Status.OK).entity(model).build();
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                model = new DeleteModel(-2);
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                model = new DeleteModel(-3);
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
                model = new DeleteModel(-1);
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(model).build();
            }
        }
        return Response.status(Status.BAD_REQUEST).entity(model).build();
    }
    @Path("retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCart(String jsonText){
        ServiceLogger.LOGGER.info("Received request to retrieve cart item.");
        ObjectMapper mapper = new ObjectMapper();
        RetrieveRequestModel requestModel;
        RetrieveModel model;
        try{
            requestModel = mapper.readValue(jsonText, RetrieveRequestModel.class);
            model = ValidateRetrieve.logicalHandler(requestModel);
            if (model.getResultCode() == 312 || model.getResultCode() == 3130)
                return Response.status(Status.OK).entity(model).build();
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                model = new RetrieveModel(-2, null);
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                model = new RetrieveModel(-3, null);
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
                model = new RetrieveModel(-1, null);
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(model).build();
            }
        }
        return Response.status(Status.BAD_REQUEST).entity(model).build();
    }
    @Path("clear")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearCart(String jsonText){
        ServiceLogger.LOGGER.info("Received request to clear cart item.");
        ObjectMapper mapper = new ObjectMapper();
        RetrieveRequestModel requestModel;
        ClearModel model;
        try{
            requestModel = mapper.readValue(jsonText, RetrieveRequestModel.class);
            model = ValidateClear.logicalHandler(requestModel);
            if (model.getResultCode() == 3140)
                return Response.status(Status.OK).entity(model).build();
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                model = new ClearModel(-2);
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                model = new ClearModel(-3);
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
                model = new ClearModel(-1);
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(model).build();
            }
        }
        return Response.status(Status.BAD_REQUEST).entity(model).build();
    }
}
