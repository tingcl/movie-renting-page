package edu.uci.ics.tingcl2.service.billing.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.api.payments.Payment;
import edu.uci.ics.tingcl2.service.billing.core.ValidateCompleteOrder;
import edu.uci.ics.tingcl2.service.billing.core.ValidateOrder;
import edu.uci.ics.tingcl2.service.billing.core.ValidateOrderRetrieve;
import edu.uci.ics.tingcl2.service.billing.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.billing.models.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("order")
public class OrderPage {
    @Path("place")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response placeOrder(String jsonText){
        ServiceLogger.LOGGER.info("Received request to place order.");
        ObjectMapper mapper = new ObjectMapper();
        OrderRequestModel requestModel;
        OrderModel model;
        try {
            requestModel = mapper.readValue(jsonText, OrderRequestModel.class);
            model = ValidateOrder.logicalHandler(requestModel);
            return Response.status(Response.Status.OK).entity(model).build();

        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                model = new OrderModel(-2, null, null);
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                model = new OrderModel(-3, null, null);
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
                model = new OrderModel(-1, null, null);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
    }

    @Path("complete")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response completeOrder(@QueryParam("paymentId") String paymentId,
                                  @QueryParam("token") String token,
                                  @QueryParam("PayerID") String PayerID){
        ServiceLogger.LOGGER.info("paymentId: " + paymentId);
        ServiceLogger.LOGGER.info("token: " + token);
        ServiceLogger.LOGGER.info("PayerID: " + PayerID);
        CompleteOrderModel model = ValidateCompleteOrder.logicalHandler(paymentId, token, PayerID);
        return Response.status(Response.Status.OK).entity(model).build();
    }

    @Path("retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveOrder(String jsonText) {
        ServiceLogger.LOGGER.info("Received request to place order.");
        ObjectMapper mapper = new ObjectMapper();
        OrderRequestModel requestModel;
        OrderRetrieveModel model;
        try {
            requestModel = mapper.readValue(jsonText, OrderRequestModel.class);
            model = ValidateOrderRetrieve.logicalHandler(requestModel);
            return Response.status(Response.Status.OK).entity(model).build();
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                model = new OrderRetrieveModel(-2, null);
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                model = new OrderRetrieveModel(-3, null);
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
                model = new OrderRetrieveModel(-1, null);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
    }

}
