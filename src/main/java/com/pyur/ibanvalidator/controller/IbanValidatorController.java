package com.pyur.ibanvalidator.controller;

import com.pyur.ibanvalidator.dto.IbanResponse;
import com.pyur.ibanvalidator.service.IbanValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "validate", produces = APPLICATION_JSON_VALUE)
public class IbanValidatorController {

  private final IbanValidationService ibanValidationService;

  @GetMapping
  public ResponseEntity<IbanResponse> validate(@RequestParam("iban") String iban) {
    return ResponseEntity.ok().body(ibanValidationService.validate(iban));
  }

}
