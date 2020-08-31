package edu.uci.ics.tingcl2.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillingModel {
    @JsonProperty(value = "email")
    String email;
    @JsonProperty(value = "movieId")
    String movieId;
    @JsonProperty(value = "quantity")
    int quantity;
    @JsonProperty(value = "unit_price")
    float unit_price;
    @JsonProperty(value = "discount")
    float discount;
    @JsonProperty(value = "saleDate")
    String saleDate;

    @JsonCreator
    public BillingModel(@JsonProperty(value = "email", required = true) String email,
                        @JsonProperty(value = "movieId", required = true) String movieId,
                        @JsonProperty(value = "quantity", required = true) int quantity,
                        @JsonProperty(value = "unit_price", required = true) float unit_price,
                        @JsonProperty(value = "discount", required = true) float discount,
                        @JsonProperty(value = "saleDate", required = true) String saleDate) {

        this.email = email;
        this.movieId = movieId;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.discount = discount;
        this.saleDate = saleDate;
    }

    public String getEmail() {
        return email;
    }

    public String getMovieId() {
        return movieId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public float getUnit_price() {
        return unit_price;
    }

    public float getDiscount() {
        return discount;
    }
}