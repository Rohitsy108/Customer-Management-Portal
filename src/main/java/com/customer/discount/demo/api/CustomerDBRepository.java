package com.customer.discount.demo.api;

import com.customer.discount.demo.entity.Mongo.CustomerDatabase;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerDBRepository extends MongoRepository<CustomerDatabase,String> {
  CustomerDatabase findByUid(String uid);
  CustomerDatabase findByMobileNumber(String phone);

  List<CustomerDatabase> findAll();

}
