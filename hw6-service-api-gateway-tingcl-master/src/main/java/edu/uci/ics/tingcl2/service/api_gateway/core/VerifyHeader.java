package edu.uci.ics.tingcl2.service.api_gateway.core;

import edu.uci.ics.tingcl2.service.api_gateway.models.idm.VerifySessionResponseModel;

public class VerifyHeader {
    public static VerifySessionResponseModel header(String email, String sessionID){
        if(email == null){
            return new VerifySessionResponseModel(-16,null);
        }
        else if(sessionID == null){
            return new VerifySessionResponseModel(-17,null);
        }
        else{
            return VerifyUserSession.hasValidSession(email, sessionID);
        }
    }
}
