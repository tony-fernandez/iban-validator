package com.pyur.ibanvalidator.service;

import com.pyur.ibanvalidator.exception.CountryNotFoundException;
import com.pyur.ibanvalidator.model.BankEntity;
import com.pyur.ibanvalidator.model.CountryEntity;
import com.pyur.ibanvalidator.repository.BankRepository;
import com.pyur.ibanvalidator.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class BankResolver {

  private final BankRepository bankRepository;
  private final CountryRepository countryRepository;


  public BankEntity resolve(String countryCode, String bankCode) {

    CountryEntity country = countryRepository
        .findByCode(countryCode)
        .orElseThrow(() -> new CountryNotFoundException(format("Unable to find country with code: %s", countryCode)));

    List<BankEntity> banks = bankRepository.findByBankCodeAndCountry(bankCode, country.getName());

    return banks.stream().findFirst().orElseThrow();
  }

}
