package com.customer.discount.demo.entity.Mongo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.io.Serializable;
import java.util.List;

@Data
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "customer_data")
public class CustomerDatabase implements Serializable {
  @Id
  private ObjectId id;

  @Indexed(unique = true,background = true)
  @Field(value = "uid")
  private String uid;

  @Indexed
  @Field(value="mobile_number")
  private String mobileNumber;

  @Field(value = "name")
  private String name;

  @Field(value ="points")
  private int points;

  @Field(value="cashbackValue")
  private int cashbackValue;

  @Field(value="referredBy")
  private String referredBy;

  @Field(value="referrals")
  private List<String> referrals;
}
