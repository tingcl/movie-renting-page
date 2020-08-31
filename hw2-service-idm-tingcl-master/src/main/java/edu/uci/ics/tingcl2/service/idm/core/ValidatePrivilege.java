package edu.uci.ics.tingcl2.service.idm.core;

import edu.uci.ics.tingcl2.service.idm.models.PrivilegeRequestModel;


public class ValidatePrivilege {
    public static int surface(PrivilegeRequestModel requestModel){
        if(requestModel.getPlevel() > 5 || requestModel.getPlevel() < 1){
            return -14;
        }
        if(requestModel.getEmail().length() > 50){
            return -10;
        }
        if(!requestModel.getEmail().matches("^(.+)@(.+)$")){
            return -11;
        }
        return 0;
    }
    public static int handler(PrivilegeRequestModel requestModel) {
        if (!UserRecords.emailExists(requestModel.getEmail())) {
            return 14;
        }
        if (UserRecords.getPlevel(requestModel.getEmail()) <= requestModel.getPlevel()) {
            return 140;
        }
        return 141;
    }
}
