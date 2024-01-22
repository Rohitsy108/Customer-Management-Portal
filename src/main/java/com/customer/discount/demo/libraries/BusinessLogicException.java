package com.customer.discount.demo.libraries;

import com.customer.discount.demo.entity.constant.ResponseCode;

public class BusinessLogicException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private String code;
  private String message;

  public BusinessLogicException(ResponseCode responseCode) {
    super();
    this.setCode(responseCode.getCode());
    this.setMessage(responseCode.getMessage());
  }

  public BusinessLogicException(String code, String message) {
    super();
    this.setCode(code);
    this.setMessage(message);
  }

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "BusinessLogicException{" +
      "code='" + code + '\'' +
      ", message='" + message + '\'' +
      '}';
  }
}
