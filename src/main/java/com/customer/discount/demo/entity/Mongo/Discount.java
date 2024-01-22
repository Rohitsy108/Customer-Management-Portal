package com.customer.discount.demo.entity.Mongo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "discount")
public class Discount {
  @Field(value="discount")
  private Double discount;

  @Field(value="date")
  private LocalDateTime date;

}
