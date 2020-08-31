package edu.uci.ics.tingcl2.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRetrieveModel {
    @JsonProperty(value = "resultCode")
    int resultCode;
    @JsonProperty(value = "message")
    String message;
    @JsonProperty(value = "transactions")
    TransactionModel[] transactions;

    @JsonCreator
    public OrderRetrieveModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                              @JsonProperty(value = "transactions") TransactionModel[] transactions) {
        this.resultCode = resultCode;
        this.transactions = transactions;
        switch (this.resultCode) {
            case -3:
                message = "JSON Parse Exception.";
                break;
            case -2:
                message = "JSON Mapping Exception.";
                break;
            case -1:
                message = "Internal Server Error.";
                break;
            case 332:
                message = "Customer does not exist.";
                break;
            case 3410:
                message = "Orders retrieved successfully.";
                break;

        }
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    public TransactionModel[] getTransactions() {
        return transactions;
    }
}