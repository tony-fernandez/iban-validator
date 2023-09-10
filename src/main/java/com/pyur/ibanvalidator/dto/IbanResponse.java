package com.pyur.ibanvalidator.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class IbanResponse {
  private String iban;
  private boolean valid;
  private BankData bankData;
  private CheckResults checkResults;
  private List<String> messages;
}
