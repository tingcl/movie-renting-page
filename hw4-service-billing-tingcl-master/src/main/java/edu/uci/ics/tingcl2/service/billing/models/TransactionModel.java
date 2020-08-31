package edu.uci.ics.tingcl2.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionModel {
    @JsonProperty(value = "transactionId")
    private String transactionId;
    @JsonProperty(value = "state")
    private String state;
    @JsonProperty(value = "amount")
    private AmountModel amount;
    @JsonProperty(value = "transaction_fee")
    private TransactionFeeModel transaction_fee;
    @JsonProperty(value = "create_time")
    private String create_time;
    @JsonProperty(value = "update_time")
    private String update_time;
    @JsonProperty(value = "items")
    private BillingModel[] items;
    @JsonCreator
    public TransactionModel(
                            @JsonProperty(value = "transactionId", required = true) String transactionId,
                            @JsonProperty(value = "state", required = true) String state,
                            @JsonProperty(value = "amount", required = true) AmountModel amount,
                            @JsonProperty(value = "transaction_fee", required = true) TransactionFeeModel transaction_fee,
                            @JsonProperty(value = "create_time", required = true) String create_time,
                            @JsonProperty(value = "update_time", required = true) String update_time,
                            @JsonProperty(value = "items", required = true) BillingModel[] items) {
        this.transactionId = transactionId;
        this.state = state;
        this.amount = amount;
        this.transaction_fee = transaction_fee;
        this.create_time = create_time;
        this.update_time = update_time;
        this.items = items;
    }
    public String getTransactionId() {
        return transactionId;
    }
    public String getState() {
        return state;
    }
    public AmountModel getAmount() {
        return amount;
    }
    public TransactionFeeModel getTransaction_fee() {
        return transaction_fee;
    }
    public String getCreate_time() {
        return create_time;
    }
    public String getUpdate_time() {
        return update_time;
    }
    public BillingModel[] getItems() {
        return items;
    }
}
