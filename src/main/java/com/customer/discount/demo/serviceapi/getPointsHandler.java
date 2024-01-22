package com.customer.discount.demo.serviceapi;

import com.customer.discount.demo.entity.Mongo.CustomerDatabase;
import com.customer.discount.demo.entity.Mongo.Discount;
import com.customer.discount.demo.entity.Mongo.Purchase;
import com.customer.discount.demo.restweb.model.request.CustomerResponse;
import com.customer.discount.demo.restweb.model.request.DiscountRequest;
import com.customer.discount.demo.restweb.model.request.PurchaseHistoryRequest;
import com.customer.discount.demo.restweb.model.request.ReportRequest;

import java.util.List;
//@Service

public interface getPointsHandler {
  CustomerDatabase addcustomer(CustomerDatabase customerDatabase);

  Purchase addPurchase(PurchaseHistoryRequest purchaseHistoryRequest);

  List<CustomerDatabase> getAll();

  List<Purchase> findPurchasebyMobile(String mobile);

  CustomerDatabase getPoints(String keyword);

  Discount addDiscount(DiscountRequest discount);

  Double findDiscount();

  CustomerDatabase redeemDiscount(PurchaseHistoryRequest discount);
  List<CustomerResponse> report(ReportRequest reportRequest);
}
