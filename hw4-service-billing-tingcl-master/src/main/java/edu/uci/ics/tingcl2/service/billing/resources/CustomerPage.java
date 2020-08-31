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

@Path("customer")
public class CustomerPage {
    @Path("insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCustomer(String jsonText) {
        ServiceLogger.LOGGER.info("Received request to insert customer information.");
        ObjectMapper mapper = new ObjectMapper();
        InsertCustomerRequestModel requestModel;
        InsertCustomerModel model;
        try {
            requestModel = mapper.readValue(jsonText, InsertCustomerRequestModel.class);
            model = ValidateInsertCustomer.logicalHandler(requestModel);
            return Response.status(Response.Status.OK).entity(model).build();

        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                model = new InsertCustomerModel(-2);
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                model = new InsertCustomerModel(-3);
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
                model = new InsertCustomerModel(-1);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
    }
    @Path("update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCart(String jsonText) {
        ServiceLogger.LOGGER.info("Received request to update cart.");
        ObjectMapper mapper = new ObjectMapper();
        InsertCustomerRequestModel requestModel;
        UpdateCustomerModel model;
        try {
            requestModel = mapper.readValue(jsonText, InsertCustomerRequestModel.class);
            model = ValidateUpdateCustomer.logicalHandler(requestModel);
            return Response.status(Response.Status.OK).entity(model).build();

        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                model = new UpdateCustomerModel(-2);
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                model = new UpdateCustomerModel(-3);
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
                model = new UpdateCustomerModel(-1);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
    }
    @Path("retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCustomer(String jsonText){
        ServiceLogger.LOGGER.info("Received request to retrieve customer information.");
        ObjectMapper mapper = new ObjectMapper();
        RetrieveCustomerRequestModel requestModel;
        RetreiveCustomerModel model;
        try{
            requestModel = mapper.readValue(jsonText, RetrieveCustomerRequestModel.class);
            model = ValidateRetrieveCustomer.logicalHandler(requestModel);
            return Response.status(Response.Status.OK).entity(model).build();
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                model = new RetreiveCustomerModel(-2, null);
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                model = new RetreiveCustomerModel(-3, null);
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
                model = new RetreiveCustomerModel(-1, null);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
    }
}
