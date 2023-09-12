package com.pyur.ibanvalidator.service;

import com.pyur.ibanvalidator.dto.BankData;
import com.pyur.ibanvalidator.dto.CheckResults;
import com.pyur.ibanvalidator.dto.IbanResponse;
import com.pyur.ibanvalidator.model.BankEntity;
import lombok.RequiredArgsConstructor;
import org.iban4j.IbanFormatException;
import org.iban4j.IbanUtil;
import org.iban4j.InvalidCheckDigitException;
import org.iban4j.UnsupportedCountryException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static java.lang.String.format;


@Service
@RequiredArgsConstructor
public class IbanValidationService {

  private final BankResolver bankResolver;

  public IbanResponse validate(String iban) {

    try {
      IbanUtil.validate(iban);

      BankEntity bank = bankResolver.resolve(IbanUtil.getCountryCode(iban), IbanUtil.getBankCode(iban));

      if (Objects.isNull(bank)) {
        return IbanResponse.builder()
                .iban(iban)
                .valid(true)
                .build();
      }

      return IbanResponse.builder()
          .valid(true)
          .iban(iban)
          .bankData(BankData.builder()
              .bic(bic(bank))
              .bankCode(bank.getBankCode())
              .name(bank.getName())
              .city(bank.getCity())
              .zip(bank.getPostalCode())
              .build())
          .checkResults(CheckResults.builder()
              .bankCode(true)
              .build())
           .messages(List.of(format("Bank code valid: %s", bank.getBankCode())))
          .build();
    } catch (IbanFormatException ife) {
      return IbanResponse.builder()
          .valid(false)
          .messages(List.of(format("Invalid iban format. %s", ife.getMessage())))
          .build();
    } catch (UnsupportedCountryException uce) {
      return IbanResponse.builder()
              .valid(false)
              .messages(List.of(format("Unsupported country. %s", uce.getMessage())))
              .build();
    } catch (InvalidCheckDigitException icde) {
      return IbanResponse.builder()
              .valid(false)
              .messages(List.of(format("Invalid check digit. %s", icde.getMessage())))
              .build();
    }

  }

  private static String bic(BankEntity bank) {
    if ("DE".equals(bank.getCountryCode()) && bank.getBankCode().length() > 6 && bank.getBankCode().substring(3, 6).equals("400")) {
      return "COBADEFFXXX";
    }
    return bank.getBic();
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
