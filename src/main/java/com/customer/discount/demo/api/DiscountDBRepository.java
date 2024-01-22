package com.customer.discount.demo.api;

import com.customer.discount.demo.entity.Mongo.Discount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountDBRepository extends MongoRepository<Discount,String> {
}
