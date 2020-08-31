package edu.uci.ics.tingcl2.service.basic.models;

import edu.uci.ics.tingcl2.service.basic.core.Record;

public class GetIdResponseModel {
    int resultCode;
    String message;
    Record record;
    public GetIdResponseModel(Record r, int code){
        resultCode = code;
        record = r;
        switch (resultCode){
            case 0:
                message = "Record retrieval successful";
                break;
            case 1:
                message = "Record not found";
                break;
        }
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record r) {
        record = r;
    }
}
