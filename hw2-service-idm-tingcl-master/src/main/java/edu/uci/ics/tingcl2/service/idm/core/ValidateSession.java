package edu.uci.ics.tingcl2.service.idm.core;


import edu.uci.ics.tingcl2.service.idm.models.SessionRequestModel;


/*
 * IDM session
 *
 * Checks sessions instance for correctness using general and logical cases
 */

public class ValidateSession {

    public static int surface(SessionRequestModel requestModel){
        if(requestModel.getSessionID().length() > 128){
            return -13;
        }
        if(requestModel.getEmail().length() > 50){
            return -10;
        }
        if(!requestModel.getEmail().matches("^(.+)@(.+)$")){
            return -11;
        }
        return 0;
    }
    public static int sessionHandler(SessionRequestModel requestModel){
        // User not found
        if(!UserRecords.emailExists(requestModel.getEmail())){
            return 14;
        }
        // closed
        if(UserRecords.returnSessionStatus(requestModel.getSessionID()) == 2){
            return 132;
        }
        // expired
        if(UserRecords.returnSessionStatus(requestModel.getSessionID()) == 3){
            return 131;
        }
        // revoked
        if(UserRecords.returnSessionStatus(requestModel.getSessionID()) == 4){
            return 133;
        }
        if(UserRecords.returnSessionStatus(requestModel.getSessionID()) == 1){
            return UserRecords.activeSessionUpdate(requestModel.getSessionID());
        }

        return 134;
    }
}
