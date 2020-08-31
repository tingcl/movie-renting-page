package edu.uci.ics.tingcl2.service.basic.core;

import edu.uci.ics.tingcl2.service.basic.models.ValidateStringRequestModel;
import edu.uci.ics.tingcl2.service.basic.models.ValidateStringResponseModel;

public class ValidateString {
    public static ValidateStringResponseModel validate(ValidateStringRequestModel requestModel) {
        if(requestModel.getInput().trim().isEmpty()) {
            return new ValidateStringResponseModel(3);
        }
        if(requestModel.getInput().length() > 512) {
            return new ValidateStringResponseModel(4);
        }
        if(requestModel.getLen() < 0){
            return new ValidateStringResponseModel(5);
        }
        if(requestModel.getInput().split("[\\s-]+").length == requestModel.getLen()) {
            return new ValidateStringResponseModel(0);
        }
        return new ValidateStringResponseModel(1);
    }
}


