package edu.uci.ics.tingcl2.service.api_gateway.threadpool;

import edu.uci.ics.tingcl2.service.api_gateway.core.Database;
import edu.uci.ics.tingcl2.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.api_gateway.models.RequestModel;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class Worker extends Thread {
    int id;
    ThreadPool threadPool;

    private Worker(int id, ThreadPool threadPool) {
        this.id = id;
        this.threadPool = threadPool;
    }
    public static Worker CreateWorker(int id, ThreadPool threadPool) {
        return new Worker(id, threadPool);
    }
    public void process() {
        ClientRequest cr = threadPool.remove();
        ServiceLogger.LOGGER.info("Running request with worker # " + id + " at " + (System.currentTimeMillis() / 1000) % 60 + " seconds.");
        /* --------------------- Information below for testing purposes only -------------------- */
        ServiceLogger.LOGGER.info("Information about client request below...");
        ServiceLogger.LOGGER.info(" Email: " + cr.getEmail());
        ServiceLogger.LOGGER.info(" SessionID: " + cr.getSessionID());
        ServiceLogger.LOGGER.info(" TransactionID: " + cr.getTransactionID());
        ServiceLogger.LOGGER.info(" Request: " + cr.getRequest());
        /* -------------------------------------- End -------------------------------------------- */
        // Create a new Client
        ServiceLogger.LOGGER.info("Building client...");
        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);
        // Get the URI for the IDM
        String URI = cr.getURI();
        ServiceLogger.LOGGER.info("URI: " + URI);
        // Default end point path
        String IDM_ENDPOINT_PATH;
        if (cr.getPathparams() != null) {
            IDM_ENDPOINT_PATH = cr.getPathparams();
        }
        else{
            IDM_ENDPOINT_PATH = cr.getEndpoint();
        }
        ServiceLogger.LOGGER.info("default IDM_ENDPOINT_PATH: " + IDM_ENDPOINT_PATH);
        if(cr.getQueryparams() != null){
            ServiceLogger.LOGGER.info("Adding query parameters: "+ cr.getQueryparams());
            IDM_ENDPOINT_PATH += "?" + cr.getQueryparams();
            ServiceLogger.LOGGER.info("editing default, IDM_ENDPOINT_PATH with query params: "
                    + IDM_ENDPOINT_PATH);
        }
        // Create a WebTarget to send a request at
        ServiceLogger.LOGGER.info("Building WebTarget...");
        ServiceLogger.LOGGER.info("webTarget final path: " + (URI + IDM_ENDPOINT_PATH).replace(" ", "+"));
        WebTarget webTarget = client.target((URI + IDM_ENDPOINT_PATH).replace(" ", "+"));
        // Create an InvocationBuilder to create the HTTP request
        ServiceLogger.LOGGER.info("Starting invocation builder...");
        ServiceLogger.LOGGER.info("Sending following in headers: ");
        ServiceLogger.LOGGER.info(" email: " + cr.getEmail());
        ServiceLogger.LOGGER.info( " sessionid: " + cr.getSessionID());
        ServiceLogger.LOGGER.info(" transactionid: " + cr.getTransactionID());
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON)
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID());
        // Set the payload
        ServiceLogger.LOGGER.info("Sending payload of the request");
        // Creating request to send
        RequestModel requestModel = cr.getRequest();
        // Send the request and save it to a Response
        ServiceLogger.LOGGER.info("Sending request...");
        Response response;
        if(requestModel != null){
            ServiceLogger.LOGGER.info("Request type: " + cr.getHttpMethodType());
            response = invocationBuilder.method(cr.getHttpMethodType(), Entity.entity(requestModel, MediaType.APPLICATION_JSON));
        }
        else {
            ServiceLogger.LOGGER.info("Request type: " + cr.getHttpMethodType());
            response = invocationBuilder.method(cr.getHttpMethodType());
        }
        ServiceLogger.LOGGER.info("Sent!");

        // insertion parameters
        String transactionid = cr.getTransactionID();
        String email = cr.getEmail();
        String sessionid = cr.getSessionID();
        String jsonText = response.readEntity(String.class);
        int httpstatus = response.getStatus();

        Database.insertResponse(transactionid, email, sessionid, jsonText, httpstatus);
        ServiceLogger.LOGGER.info("Done!");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            ServiceLogger.LOGGER.warning("Error.");
        }
    }
    @Override
    public void run() {
        while (true) {
            try{
                process();
            }
            catch (Exception e)
            {
                ServiceLogger.LOGGER.info("Exception is caught");
            }
        }
    }
}
