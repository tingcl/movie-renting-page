package edu.uci.ics.tingcl2.service.basic.models;


public class GetRecordsResponseModel {
    int resultCode;
    int numRecords;
    String message;

    public GetRecordsResponseModel(int num) {
        resultCode = 0;
        message = "Number of records successfully retrieved.";
        numRecords = num;
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

    public int getNumRecords() {
        return numRecords;
    }

    public void setNumRecords(int numRecords) {
        this.numRecords = numRecords;
    }
}
