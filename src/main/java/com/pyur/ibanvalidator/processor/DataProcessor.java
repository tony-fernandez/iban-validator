package com.pyur.ibanvalidator.processor;

public interface DataProcessor {

  String ADDRESS = "address";
  String BANK_CODE = "bank_code";
  String BANK_CODE_NEW = "bank_code_new";
  String BIC = "bic";
  String CITY = "city";
  String CODE = "code";
  String NAME = "name";
  String PAN = "pan";
  String POST_CODE = "post_code";
  String SHORT_NAME = "short_name";
  String SWIFT = "swift";



  void process(String fileName);

}
