package com.customer.discount.demo.entity.constant;

public interface ApiPath {
  String BASE_PATH = "/rohit-shop";
  String V1 = "/v1";
  String DASHBOARD = "/dashboard";
  String GET_POINTS="/getPoints";
  String ADD_CUSTOMER="/addCustomer";
  String BASE_PATH_V1 = BASE_PATH + V1;
  String ID = "/id/{id}";
  String PhoneNumber = "/phone/{phoneNumber}";
  String ADD_PURCHASE="/addPurchase";
  String VIEW_PURCHASE="/viewPurchase";
  String DISCOUNT="/discount";
  String REDEEM_DISCOUNT="/redeemDiscount";
  String REPORT="/report";

}
