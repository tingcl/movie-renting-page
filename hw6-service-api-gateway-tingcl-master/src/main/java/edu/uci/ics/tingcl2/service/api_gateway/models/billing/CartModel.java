package edu.uci.ics.tingcl2.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartModel {
    String email;
    String movieId;
    int quantity;

    @JsonCreator
    public CartModel(@JsonProperty(value = "email", required = true) String email,
                     @JsonProperty(value = "movieId", required = true) String movieId,
                     @JsonProperty(value = "quantity", required = true) int quantity){
        this.email = email;
        this.movieId = movieId;
        this.quantity = quantity;
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
}