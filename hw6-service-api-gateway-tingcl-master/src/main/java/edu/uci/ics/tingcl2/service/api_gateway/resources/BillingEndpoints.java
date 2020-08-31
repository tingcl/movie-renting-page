package edu.uci.ics.tingcl2.service.api_gateway.resources;

import edu.uci.ics.tingcl2.service.api_gateway.GatewayService;
import edu.uci.ics.tingcl2.service.api_gateway.core.VerifyHeader;
import edu.uci.ics.tingcl2.service.api_gateway.core.VerifyUserSession;
import edu.uci.ics.tingcl2.service.api_gateway.exceptions.ModelValidationException;
import edu.uci.ics.tingcl2.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.api_gateway.models.billing.*;
import edu.uci.ics.tingcl2.service.api_gateway.models.idm.VerifySessionResponseModel;
import edu.uci.ics.tingcl2.service.api_gateway.threadpool.ClientRequest;
import edu.uci.ics.tingcl2.service.api_gateway.utilities.ModelValidator;
import edu.uci.ics.tingcl2.service.api_gateway.utilities.TransactionIDGenerator;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("billing")
public class BillingEndpoints {
    @Path("cart/insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertToCartRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to insert into cart.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        if(transactionID == null)
            transactionID = TransactionIDGenerator.generateTransactionID();

        InsertCartRequestModel requestModel;
        try {
            requestModel = (InsertCartRequestModel) ModelValidator.verifyModel(jsonText, InsertCartRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, InsertCartResponseModel.class);
        }

        ClientRequest cr = new ClientRequest();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setRequest(requestModel);
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCartInsert());
        cr.setHttpMethodType("POST");
        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID()).build();
    }

    @Path("cart/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCartRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to update cart.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        if(transactionID == null)
            transactionID = TransactionIDGenerator.generateTransactionID();

        UpdateCartRequestModel requestModel;
        try {
            requestModel = (UpdateCartRequestModel) ModelValidator.verifyModel(jsonText, UpdateCartRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, UpdateCartResponseModel.class);
        }
        ClientRequest cr = new ClientRequest();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setRequest(requestModel);
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setHttpMethodType("POST");
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCartUpdate());
        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID()).build();
    }

    @Path("cart/delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCartRequest(@Context HttpHeaders headers, String jsonText) {

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        if(transactionID == null)
            transactionID = TransactionIDGenerator.generateTransactionID();

        ServiceLogger.LOGGER.info("Received request to delete item from cart.");
        DeleteCartRequestModel requestModel;
        try {
            requestModel = (DeleteCartRequestModel) ModelValidator.verifyModel(jsonText, DeleteCartRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, DeleteCartResponseModel.class);
        }
        // Generate transaction id.
        ClientRequest cr = new ClientRequest();
        cr.setTransactionID(transactionID);
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttpMethodType("POST");
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCartDelete());

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID()).build();
    }

    @Path("cart/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCartRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to retrieve item from cart.");

        // Retrieving header values to be sent back to API Gateway
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        // Checking for valid header values
        VerifySessionResponseModel verifySessionResponseModel = VerifyHeader.header(email, sessionID);
        ServiceLogger.LOGGER.info("............................................................");
        ServiceLogger.LOGGER.info("............................................................");
        ServiceLogger.LOGGER.info("............................................................");
        ServiceLogger.LOGGER.info("............................................................");
        if(transactionID == null) {
            transactionID = TransactionIDGenerator.generateTransactionID();
        }
        if(verifySessionResponseModel.getResultCode() == -17 || verifySessionResponseModel.getResultCode() == -16){
            ServiceLogger.LOGGER.info("Invalid email or sessionID detected please request again");
            return Response.status(Status.BAD_REQUEST).entity(verifySessionResponseModel)
                    .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                    .header("transactionID", transactionID).build();
        }
        if(verifySessionResponseModel.getResultCode() != 130){
            return Response.status(Status.OK).entity(verifySessionResponseModel)
                    .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                    .header("transactionID", transactionID).build();
        }
        else{
            ServiceLogger.LOGGER.info("Active sessionID received.");
        }
        // Creating request for end point
        RetrieveCartRequestModel requestModel;
        try {
            requestModel = (RetrieveCartRequestModel) ModelValidator.verifyModel(jsonText, RetrieveCartRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, RetrieveCartResponseModel.class);
        }

        // Creating client request for thread pool
        ClientRequest cr = new ClientRequest();
        cr.setHttpMethodType("POST");
        cr.setTransactionID(transactionID);
        cr.setRequest(requestModel);
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCartRetrieve());
        cr.setEmail(email);
        cr.setSessionID(sessionID);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID()).build();
    }

    @Path("cart/clear")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearCartRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to clear cart.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        if(transactionID == null)
            transactionID = TransactionIDGenerator.generateTransactionID();

        ClearCartRequestModel requestModel;
        try {
            requestModel = (ClearCartRequestModel) ModelValidator.verifyModel(jsonText, ClearCartRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, ClearCartResponseModel.class);
        }
        ClientRequest cr = new ClientRequest();
        cr.setTransactionID(transactionID);
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setHttpMethodType("POST");
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCartClear());


        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID()).build();
    }

    @Path("creditcard/insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCreditCardRequest(@Context HttpHeaders headers,String jsonText) {
        ServiceLogger.LOGGER.info("Received request to add credit card.");

        // Retrieving header values to be sent back to API Gateway
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        // Checking for valid header values
        VerifySessionResponseModel verifySessionResponseModel = VerifyHeader.header(email, sessionID);

        if(transactionID == null) {
            transactionID = TransactionIDGenerator.generateTransactionID();
        }
        if(verifySessionResponseModel.getResultCode() == -17 || verifySessionResponseModel.getResultCode() == -16){
            ServiceLogger.LOGGER.info("Invalid email or sessionID detected please request again");
            return Response.status(Status.BAD_REQUEST).entity(verifySessionResponseModel)
                    .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                    .header("transactionID", transactionID).build();
        }
        if(verifySessionResponseModel.getResultCode() != 130){
            return Response.status(Status.OK).entity(verifySessionResponseModel)
                    .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                    .header("transactionID", transactionID).build();
        }
        else{
            ServiceLogger.LOGGER.info("Active sessionID received.");
        }

        // Creating endpoint request model
        InsertCardRequestModel requestModel;
        try {
            requestModel = (InsertCardRequestModel) ModelValidator.verifyModel(jsonText, InsertCardRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, InsertCardResponseModel.class);
        }

        // Setting client request fields
        ClientRequest cr = new ClientRequest();
        cr.setHttpMethodType("POST");
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCcInsert());

        // Adding client request into thread pool queue
        GatewayService.getThreadPool().getQueue().enqueue(cr);

        // Returning response with no content
        // Returning delay, email, sessionid, transactionid
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID()).build();
    }

    @Path("creditcard/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCreditCardRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to update credit card.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        if(transactionID == null)
            transactionID = TransactionIDGenerator.generateTransactionID();

        UpdateCardRequestModel requestModel;
        try {
            requestModel = (UpdateCardRequestModel) ModelValidator.verifyModel(jsonText, UpdateCardRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, UpdateCardResponseModel.class);
        }
        // Generate transaction id.
        ClientRequest cr = new ClientRequest();
        cr.setHttpMethodType("POST");
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setRequest(requestModel);
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCcUpdate());

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID()).build();
    }

    @Path("creditcard/delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCreditCardRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to delete credit card.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        if(transactionID == null)
            transactionID = TransactionIDGenerator.generateTransactionID();

        DeleteCardRequestModel requestModel;
        try {
            requestModel = (DeleteCardRequestModel) ModelValidator.verifyModel(jsonText, DeleteCardRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, DeleteCardResponseModel.class);
        }
        // Generate transaction id.
        ClientRequest cr = new ClientRequest();
        cr.setTransactionID(transactionID);
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setHttpMethodType("POST");
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCcDelete());

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID()).build();
    }

    @Path("creditcard/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCreditCardRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to retrieve credit card.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        if(transactionID == null)
            transactionID = TransactionIDGenerator.generateTransactionID();

        RetrieveCardRequestModel requestModel;
        try {
            requestModel = (RetrieveCardRequestModel) ModelValidator.verifyModel(jsonText, RetrieveCardRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, RetrieveCardResponseModel.class);
        }
        // Generate transaction id.
        ClientRequest cr = new ClientRequest();
        cr.setHttpMethodType("POST");
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setRequest(requestModel);
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCcRetrieve());

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID()).build();
    }

    @Path("customer/insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCustomerRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to insert customer.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        if(transactionID == null)
            transactionID = TransactionIDGenerator.generateTransactionID();

        InsertCustomerRequestModel requestModel;
        try {
            requestModel = (InsertCustomerRequestModel) ModelValidator.verifyModel(jsonText, InsertCustomerRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, InsertCustomerResponseModel.class);
        }
        // Generate transaction id.

        ClientRequest cr = new ClientRequest();
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setRequest(requestModel);
        cr.setHttpMethodType("POST");
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCustomerInsert());

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID()).build();
    }

    @Path("customer/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomerRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to update customer.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        if(transactionID == null)
            transactionID = TransactionIDGenerator.generateTransactionID();

        UpdateCustomerRequestModel requestModel;
        try {
            requestModel = (UpdateCustomerRequestModel) ModelValidator.verifyModel(jsonText, UpdateCustomerRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, UpdateCustomerResponseModel.class);
        }
        // Generate transaction id.
        ClientRequest cr = new ClientRequest();
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setRequest(requestModel);
        cr.setHttpMethodType("POST");
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCustomerUpdate());

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID()).build();
    }

    @Path("customer/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCustomerRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to retrieve customer.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        if(transactionID == null)
            transactionID = TransactionIDGenerator.generateTransactionID();

        RetrieveCustomerRequestModel requestModel;
        try {
            requestModel = (RetrieveCustomerRequestModel) ModelValidator.verifyModel(jsonText, RetrieveCustomerRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, RetrieveCustomerResponseModel.class);
        }
        // Generate transaction id.
        ClientRequest cr = new ClientRequest();
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setRequest(requestModel);
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setHttpMethodType("POST");
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCustomerRetrieve());

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID()).build();
    }

    @Path("order/place")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response placeOrderRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to place order.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        if(transactionID == null)
            transactionID = TransactionIDGenerator.generateTransactionID();

        // Check valid sessionID
        VerifySessionResponseModel verifySessionResponseModel = VerifyUserSession.hasValidSession(email, sessionID);
        if(verifySessionResponseModel.getResultCode() == 130){
            ServiceLogger.LOGGER.info("Valid active session ID continue as normal.");
        }
        else{
            return Response.status(Status.OK).entity(verifySessionResponseModel)
                    .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                    .header("transactionID", transactionID).build();
        }
        PlaceOrderRequestModel requestModel;
        try {
            requestModel = (PlaceOrderRequestModel) ModelValidator.verifyModel(jsonText, PlaceOrderRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, PlaceOrderResponseModel.class);
        }
        // Generate transaction id.
        ClientRequest cr = new ClientRequest();
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setRequest(requestModel);
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setHttpMethodType("POST");
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPOrderPlace());

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID()).build();
    }

    @Path("order/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveOrderRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to retrieve order.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        if(transactionID == null)
            transactionID = TransactionIDGenerator.generateTransactionID();

        RetrieveOrderRequestModel requestModel;
        try {
            requestModel = (RetrieveOrderRequestModel) ModelValidator.verifyModel(jsonText, RetrieveOrderRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, RetrieveOrderResponseModel.class);
        }
        ClientRequest cr = new ClientRequest();
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setRequest(requestModel);
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setHttpMethodType("POST");
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPOrderRetrieve());

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID()).build();
    }
}
