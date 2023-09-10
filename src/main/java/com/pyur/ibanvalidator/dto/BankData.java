package com.pyur.ibanvalidator.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankData {
  private String bankCode;
  private String bic;
  private String name;
  private String city;
  private String zip;
}
