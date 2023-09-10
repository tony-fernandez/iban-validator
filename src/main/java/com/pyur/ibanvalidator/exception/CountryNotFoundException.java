package com.pyur.ibanvalidator.exception;

public class CountryNotFoundException extends RuntimeException {

  private String message;

  public CountryNotFoundException(String message) {
    super(message);
  }
}
