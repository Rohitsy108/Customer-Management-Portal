package com.customer.discount.demo.serviceimpl;

import com.customer.discount.demo.api.CustomerDB;
import com.customer.discount.demo.api.CustomerDBRepository;
import com.customer.discount.demo.api.DiscountDBRepository;
import com.customer.discount.demo.api.PurchaseDBRepository;
import com.customer.discount.demo.entity.Mongo.CustomerDatabase;
import com.customer.discount.demo.entity.Mongo.Discount;
import com.customer.discount.demo.entity.Mongo.Purchase;
import com.customer.discount.demo.restweb.model.request.CustomerResponse;
import com.customer.discount.demo.restweb.model.request.DiscountRequest;
import com.customer.discount.demo.restweb.model.request.PurchaseHistoryRequest;
import com.customer.discount.demo.restweb.model.request.ReportRequest;
import com.customer.discount.demo.serviceapi.getPointsHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class getPointsHandlerImpl implements getPointsHandler {
  @Autowired
  private CustomerDBRepository customerDBRepository;

  @Autowired
  private PurchaseDBRepository purchaseDBRepository;

  @Autowired
  private DiscountDBRepository discountDBRepository;
  @Override
  public CustomerDatabase addcustomer(CustomerDatabase getPointsResponse) {
    getPointsResponse.setUid(getPointsResponse.getName().toLowerCase().substring(0,4).concat(getPointsResponse.getMobileNumber().substring(0,4)));
    getPointsResponse.setCashbackValue(0);
    getPointsResponse.setReferrals(new ArrayList<>());
    customerDBRepository.save(getPointsResponse);
    String res= (getPointsResponse.getReferredBy().length()>0)?addReferral(getPointsResponse.getReferredBy(), getPointsResponse.getMobileNumber()):null;
    return customerDBRepository.findByMobileNumber(getPointsResponse.getMobileNumber());
  }

  private String addReferral(String referrals, String mobile) {
    CustomerDatabase customerDatabase;
    try {
       customerDatabase = customerDBRepository.findByMobileNumber(referrals);
    }
    catch(Exception e){
      return "REFERRAL NOT EXIST";
    }
    if(customerDatabase==null){
      return null;
    }
    if(customerDatabase.getReferrals()!=null && customerDatabase.getReferrals().size()>0) {
      List<String> referral=customerDatabase.getReferrals();
      referral.add(mobile);
      customerDatabase.setReferrals(referral);
    }
    else {
      customerDatabase.setReferrals(List.of(mobile));
    }
    purchaseDBRepository.save(Purchase.builder().amount(BigDecimal.valueOf(0))
      .mobile(referrals)
      .purchaseType(Purchase.PurchaseType.REFERRAL)
      .date(LocalDate.now())
      .points(50)
      .cashback(BigDecimal.valueOf(25))
      .build());
    customerDatabase.setPoints(customerDatabase.getPoints()+50);
    customerDatabase.setCashbackValue(customerDatabase.getCashbackValue()+25);
    CustomerDB.updateById(customerDatabase.getId(),customerDatabase);
    return referrals;
  }

  @Override
  public Purchase addPurchase(PurchaseHistoryRequest purchaseHistoryRequest) {
    return Objects.nonNull(customerDBRepository.findByMobileNumber(purchaseHistoryRequest.getMobile()))?
      PurchaseDB(purchaseHistoryRequest,customerDBRepository.findByMobileNumber(purchaseHistoryRequest.getMobile())):
      Objects.nonNull(addcustomer(CustomerDatabase.builder()
        .points((purchaseHistoryRequest.getAmount().multiply(BigDecimal.valueOf(findDiscount()))).setScale(0, RoundingMode.UP).intValue())
        .cashbackValue((purchaseHistoryRequest.getAmount().multiply(BigDecimal.valueOf(findDiscount()))).setScale(0, RoundingMode.UP).intValue()/2)
        .uid((Objects.nonNull(purchaseHistoryRequest.getName()) && purchaseHistoryRequest.getName().length()>0)?
          purchaseHistoryRequest.getName().toLowerCase().substring(0,4).concat(purchaseHistoryRequest.getMobile().substring(0,4))
          :"hg"+purchaseHistoryRequest.getMobile())
        .mobileNumber(purchaseHistoryRequest.getMobile())
        .name(purchaseHistoryRequest.getName().length()>0?purchaseHistoryRequest.getName():"H&G")
        .build()))?purchaseDBRepository.save(Purchase.builder().amount(purchaseHistoryRequest.getAmount())
        .date(LocalDate.now()).mobile(purchaseHistoryRequest.getMobile())
          .purchaseType(Purchase.PurchaseType.PURCHASE)
        .points((purchaseHistoryRequest.getAmount().multiply(BigDecimal.valueOf(findDiscount()))).setScale(0, RoundingMode.UP).intValue())
        .cashback((purchaseHistoryRequest.getAmount().multiply(BigDecimal.valueOf(findDiscount()))).setScale(0, RoundingMode.UP).divide(BigDecimal.valueOf(2))).build()):null;
  }

  @Override
  public List<CustomerDatabase> getAll() {
    return customerDBRepository.findAll();
  }

  @Override
  public List<Purchase> findPurchasebyMobile(String mobile) {
    return purchaseDBRepository.findByMobile(mobile);
  }

  @Override
  public CustomerDatabase getPoints(String keyword) {
    return customerDBRepository.findByMobileNumber(keyword)!=null?customerDBRepository.findByMobileNumber(keyword)
      :customerDBRepository.findByUid(keyword);
  }

  @Override
  public Discount addDiscount(DiscountRequest discount) {
   return discountDBRepository.save(Discount.builder().discount(discount.getDiscount())
      .date(LocalDateTime.now()).build());
  }

  private Purchase PurchaseDB(PurchaseHistoryRequest purchaseHistoryRequest, CustomerDatabase customerDatabase) {
    CustomerDB.updateById(customerDatabase.getId(), CustomerDatabase.builder()
      .points(customerDatabase.getPoints() + (purchaseHistoryRequest.getAmount().multiply(BigDecimal.valueOf(findDiscount()))).setScale(0, RoundingMode.UP).intValue())
        .cashbackValue(customerDatabase.getCashbackValue()+ (purchaseHistoryRequest.getAmount().multiply(BigDecimal.valueOf(findDiscount()))).setScale(0, RoundingMode.UP).intValue()/2)
      .build());
    log.info("updating purchaseDB " + purchaseHistoryRequest.toString() + "  " + customerDatabase.toString());
    return purchaseDBRepository.save(Purchase.builder()
    .amount(purchaseHistoryRequest.getAmount())
      .date(LocalDate.now())
      .mobile(purchaseHistoryRequest.getMobile())
      .points(purchaseHistoryRequest.getAmount().multiply(BigDecimal.valueOf(findDiscount())).setScale(0, RoundingMode.UP).intValue())
      .cashback(purchaseHistoryRequest.getAmount().multiply(BigDecimal.valueOf(findDiscount()/2)))
        .purchaseType(Purchase.PurchaseType.PURCHASE)
      .build());
  }
  @Override
  public Double findDiscount(){
    List<Discount> discounts=discountDBRepository.findAll(Sort.by(Sort.Direction.DESC,"date"));
    discounts=discounts.size()>0?discounts:List.of(Discount.builder().discount(10.0).build());
    return discounts.get(0).getDiscount()/100.0;
  }

  @Override
  public CustomerDatabase redeemDiscount(PurchaseHistoryRequest discount) {
    CustomerDatabase customerDB=customerDBRepository.findByMobileNumber(discount.getMobile());
    purchaseDBRepository.save(Purchase.builder().cashback(discount.getAmount().multiply(BigDecimal.valueOf(-1)))
      .points(discount.getAmount().multiply(BigDecimal.valueOf(-2)).intValue())
      .mobile(discount.getMobile())
      .amount(discount.getAmount().multiply(BigDecimal.valueOf(-1)))
      .date(LocalDate.now())
      .purchaseType(Purchase.PurchaseType.DISCOUNT_REDEEMED).build());
    return CustomerDB.updateById(customerDB.getId(),
     CustomerDatabase.builder()
     .cashbackValue(customerDB.getCashbackValue()-(discount.getAmount().intValue()))
     .points(customerDB.getPoints()-(discount.getAmount().multiply(BigDecimal.valueOf(2))).intValue())
     .build());
  }

  @Override
  public List<CustomerResponse> report(ReportRequest reportRequest) {
    log.info(reportRequest.toString());
    LocalDate  d1 = LocalDate.parse(reportRequest.getStartDate());
    LocalDate  d2 = LocalDate.parse(reportRequest.getEndDate());
    List<Purchase> purchase = purchaseDBRepository.findByDateBetween(d1.minusDays(1),d2.plusDays(1));
    Map<String, Purchase> set = new HashMap<>();
    for (Purchase p : purchase) {
      if (set.containsKey(p.getMobile())) {
        Purchase pp = set.get(p.getMobile());
        if(p.getPurchaseType().equals(Purchase.PurchaseType.PURCHASE))
          pp.setAmount(pp.getAmount().add(p.getAmount()));
        else
          pp.setAmount(pp.getAmount());
        pp.setPoints(pp.getPoints() + (p.getPoints()));
        pp.setCashback(pp.getCashback().add(p.getCashback()));
        set.put(p.getMobile(), pp);
      }
      else {
        set.put(p.getMobile(), p);
      }
    }
    List<CustomerResponse> customer=new ArrayList<>();
    for(String p:set.keySet()){
      CustomerDatabase c=customerDBRepository.findByMobileNumber(p);
      customer.add(CustomerResponse.builder().mobile(p).name(c.getName()).amount(set.get(p).getAmount())
        .points(set.get(p).getPoints()).cashbackValue(set.get(p).getCashback()).build());
    }
    return customer;
  }
}
