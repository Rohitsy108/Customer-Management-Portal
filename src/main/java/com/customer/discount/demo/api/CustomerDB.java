package com.customer.discount.demo.api;

import com.customer.discount.demo.entity.Mongo.CustomerDatabase;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Query;


@Repository
public class CustomerDB {

  private static MongoTemplate mongoTemplate;

  @Autowired
  public CustomerDB(MongoTemplate mongoTemplate) {
    CustomerDB.mongoTemplate = mongoTemplate;
  }

  public static CustomerDatabase updateById(ObjectId id, CustomerDatabase customerDatabase){
    Query query= new Query();
    query.addCriteria(Criteria.where("_id").is(id));
    Update update=new Update();
    update.set("points",customerDatabase.getPoints());
    update.set("cashbackValue", customerDatabase.getCashbackValue());
    update.set("referrals",customerDatabase.getReferrals());
   return mongoTemplate.findAndModify(query,update,CustomerDatabase.class);
  }

}
