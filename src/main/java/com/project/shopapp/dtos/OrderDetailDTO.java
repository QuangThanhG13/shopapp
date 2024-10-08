package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1, message = "Order's ID must be > 0 ")
    private Long orderId;

    @JsonProperty("product_id")
    @Min(value = 1, message = "Product's ID must be > 0 ")
    private Long productId;

    @Min(value = 0, message = "Price must be >= 0 ")
    private Float price;

    @JsonProperty("number_of_products")
    private int numberOfProducts;

    @Min(value = 0, message = "total_money must be > 0 ")
    @JsonProperty("total_money")
    private int totalMoney;

    private String color;
}
