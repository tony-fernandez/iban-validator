package com.pyur.ibanvalidator.service;

import com.pyur.ibanvalidator.dto.BankData;
import com.pyur.ibanvalidator.dto.IbanResponse;
import com.pyur.ibanvalidator.model.BankEntity;
import com.pyur.ibanvalidator.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import org.iban4j.IbanFormatException;
import org.iban4j.IbanUtil;
import org.iban4j.InvalidCheckDigitException;
import org.iban4j.UnsupportedCountryException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class IbanValidationService {

  private final BankResolver bankResolver;
  private final BankRepository bankRepository;

  public IbanResponse validate(String iban) {

    try {
      IbanUtil.validate(iban);

      BankEntity bank = bankResolver.resolve(IbanUtil.getCountryCode(iban), IbanUtil.getBankCode(iban));

      return IbanResponse.builder()
          .valid(true)
          .bankData(BankData.builder()
              .bic(bank.getBic())
              .bankCode(bank.getBankCode())
              .name(bank.getName())
              .city(bank.getCity())
              .zip(bank.getPostalCode())
              .build())
          .build();
    } catch (IbanFormatException ife) {
      return IbanResponse.builder()
          .valid(false)
          .build();
    } catch (UnsupportedCountryException uce) {
      return IbanResponse.builder()
          .valid(false)
          .build();
    } catch (InvalidCheckDigitException icde) {
      return IbanResponse.builder()
          .valid(false)
          .build();
    }

  }

  public static void main(String[] args) {
    //CH4204835217612990002
    //DE89370400440532013000
    String iban = "DE89370400440532013000";

    IbanUtil.validate(iban);


    System.out.println(IbanUtil.calculateCheckDigit(iban));
    System.out.println(IbanUtil.getCheckDigit(iban));
    System.out.println(IbanUtil.getCountryCode(iban));
    System.out.println(IbanUtil.getCountryCodeAndCheckDigit(iban));
    System.out.println(IbanUtil.getBban(iban));
    System.out.println(IbanUtil.getAccountNumber(iban));
    System.out.println(IbanUtil.getBankCode(iban));



  }


}
