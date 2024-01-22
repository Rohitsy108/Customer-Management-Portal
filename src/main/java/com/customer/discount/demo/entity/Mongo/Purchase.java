package com.customer.discount.demo.entity.Mongo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "purchase")
public class Purchase implements Serializable {
    @Field(value="mobile")
    private String mobile;

    @Field(value="date")
    private LocalDate date;

    @Field(value="amount")
    private BigDecimal amount;

    @Field(value="points")
    private int points;

    @Field(value="cashback")
    private BigDecimal cashback;

    @Field(value="purchaseType")
    private PurchaseType purchaseType;

    public enum PurchaseType{
        PURCHASE,
        REFERRAL,
        DISCOUNT_REDEEMED
    }
}
