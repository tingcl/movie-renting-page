package edu.uci.ics.tingcl2.service.api_gateway.resources;

import edu.uci.ics.tingcl2.service.api_gateway.GatewayService;
import edu.uci.ics.tingcl2.service.api_gateway.exceptions.ModelValidationException;
import edu.uci.ics.tingcl2.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.api_gateway.models.billing.InsertCustomerRequestModel;
import edu.uci.ics.tingcl2.service.api_gateway.models.idm.*;
import edu.uci.ics.tingcl2.service.api_gateway.threadpool.ClientRequest;
import edu.uci.ics.tingcl2.service.api_gateway.utilities.ModelValidator;
import edu.uci.ics.tingcl2.service.api_gateway.utilities.TransactionIDGenerator;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("idm")
public class IDMEndpoints {
    /* registerUserRequest annotated fully. All succeeding functions follow the same format. */
    @Path("register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUserRequest(String jsonText) {
        ServiceLogger.LOGGER.info("Received request to register user.");
        RegisterUserRequestModel requestModel;
        // Map jsonText to RequestModel
        try {
            requestModel = (RegisterUserRequestModel) ModelValidator.verifyModel(jsonText, RegisterUserRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, RegisterUserResponseModel.class);
        }
        // Generate transaction id.
        String transactionID = TransactionIDGenerator.generateTransactionID();
        /*
            Create ClientRequest wrapper for HTTP request. This will be potentially unique for each endpoint, because
            not every endpoint requires the same information. The register user request in particular does not contain
            any information in the HTTP Header (email, sessionID, transactionID) because the client making this request
            doesn't have any of that information yet, whereas for most other endpoints, this will be the case. So, for
            this endpoint, all we can set is the RequestModel, the URI of the microservice we're sending the request to,
            the endpoint we're sending this request to, and the transactionID for this request.
         */

        ClientRequest cr = new ClientRequest();
        cr.setTransactionID(transactionID);
        // set the request model
        cr.setRequest(requestModel);
        // get the IDM URI from IDM configs
        cr.setURI(GatewayService.getIdmConfigs().getIdmUri());
        // get the register endpoint path from IDM configs
        cr.setEndpoint(GatewayService.getIdmConfigs().getEPUserRegister());
        // set request method type to post
        cr.setHttpMethodType("POST");


        String t = TransactionIDGenerator.generateTransactionID();
        InsertCustomerRequestModel r = new InsertCustomerRequestModel(requestModel.getEmail(), null,null, null,null);

        ClientRequest c = new ClientRequest();
        c.setTransactionID(t);
        c.setRequest(r);
        c.setURI(GatewayService.getBillingConfigs().getBillingUri());
        c.setEndpoint(GatewayService.getBillingConfigs().getEPCustomerInsert());
        c.setHttpMethodType("POST");

        // Now that the ClientRequest has been built, we can add it to our queue of requests.
        GatewayService.getThreadPool().getQueue().enqueue(c);
        GatewayService.getThreadPool().getQueue().enqueue(cr);

        // Generate a NoContentResponseModel to send to the client containing the time to wait before asking for the
        // requested information, and the transactionID.
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("transactionid", transactionID).build();
    }

    @Path("login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUserRequest(String jsonText) {
        ServiceLogger.LOGGER.info("Received request to login user.");
        LoginUserRequestModel requestModel;
        // Map jsonText to RequestModel
        try {
            requestModel = (LoginUserRequestModel) ModelValidator.verifyModel(jsonText, LoginUserRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, LoginUserRequestModel.class);
        }
        // Generate transaction id.
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setTransactionID(transactionID);
        cr.setRequest(requestModel);
        cr.setURI(GatewayService.getIdmConfigs().getIdmUri());
        cr.setEndpoint(GatewayService.getIdmConfigs().getEPUserLogin());
        cr.setHttpMethodType("POST");
        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("transactionid", transactionID).build();
    }

    @Path("session")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifySessionRequest(String jsonText) {
        ServiceLogger.LOGGER.info("Received request to verify session.");
        VerifySessionRequestModel requestModel;
        // Map jsonText to RequestModel
        try {
            requestModel = (VerifySessionRequestModel) ModelValidator.verifyModel(jsonText, VerifySessionRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, VerifySessionRequestModel.class);
        }
        // Generate transaction id.
        String transactionID = TransactionIDGenerator.generateTransactionID();
        ClientRequest cr = new ClientRequest();
        cr.setTransactionID(transactionID);
        cr.setRequest(requestModel);
        cr.setURI(GatewayService.getIdmConfigs().getIdmUri());
        cr.setHttpMethodType("POST");
        cr.setEndpoint(GatewayService.getIdmConfigs().getEPSessionVerify());

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("transactionid", transactionID).build();
    }

    @Path("privilege")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyUserPrivilegeRequest(String jsonText) {
        ServiceLogger.LOGGER.info("Received request to verify privilege.");
        VerifyPrivilegeRequestModel requestModel;
        // Map jsonText to RequestModel
        try {
            requestModel = (VerifyPrivilegeRequestModel) ModelValidator.verifyModel(jsonText, VerifyPrivilegeRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, VerifyPrivilegeResponseModel.class);
        }
        // Generate transaction id.
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setRequest(requestModel);
        cr.setTransactionID(transactionID);
        cr.setHttpMethodType("POST");
        cr.setEndpoint(GatewayService.getIdmConfigs().getEPUserPrivilegeVerify());
        cr.setURI(GatewayService.getIdmConfigs().getIdmUri());

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("transactionid", transactionID).build();
    }
}
