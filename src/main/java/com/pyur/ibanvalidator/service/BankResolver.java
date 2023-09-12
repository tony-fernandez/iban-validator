package com.pyur.ibanvalidator.service;

import com.pyur.ibanvalidator.model.BankEntity;
import com.pyur.ibanvalidator.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class BankResolver {

  private final BankRepository bankRepository;

  public BankEntity resolve(String countryCode, String bankCode) {
    Optional<BankEntity> defaultBank =
            bankRepository.findByBankCodeAndCountryCodeAndBicEndsWith(bankCode, countryCode, "XXX");

    if (defaultBank.isPresent()) {
      return defaultBank.get();
    }

    List<BankEntity> banks = bankRepository.findByBankCodeAndCountryCode(bankCode, countryCode);

    if (isEmpty(banks)) {
      return null;
    }

    return banks.stream().findFirst().orElseThrow();
  }

}
