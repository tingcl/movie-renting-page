package edu.uci.ics.tingcl2.service.api_gateway.threadpool;

import edu.uci.ics.tingcl2.service.api_gateway.models.RequestModel;

public class ClientRequest {
    // Request variables
    private String email;
    private String sessionID;
    private String transactionID;
    private RequestModel request;
    private String URI;
    private String endpoint;
    private String httpMethodType;
    private String queryparams;
    private String pathparams;

    public ClientRequest(){ }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public RequestModel getRequest() {
        return request;
    }

    public void setRequest(RequestModel request) {
        this.request = request;
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getHttpMethodType() {
        return httpMethodType;
    }

    public void setHttpMethodType(String httpMethodType) {
        this.httpMethodType = httpMethodType;
    }

    public String getQueryparams() {
        return queryparams;
    }

    public void setQueryparams(String queryparams) {
        this.queryparams = queryparams;
    }

    public String getPathparams() {
        return pathparams;
    }

    public void setPathparams(String pathparams) {
        this.pathparams = pathparams;
    }
}
