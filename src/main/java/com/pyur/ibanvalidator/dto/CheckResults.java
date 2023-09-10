package com.pyur.ibanvalidator.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckResults {
  private boolean bankCode;
}
