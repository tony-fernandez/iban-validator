package com.pyur.ibanvalidator.processor;

import com.pyur.ibanvalidator.model.BankEntity;
import com.pyur.ibanvalidator.repository.BankRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;

import static com.pyur.ibanvalidator.util.ProcessorUtil.file;

@Slf4j
@Component
@RequiredArgsConstructor
public class GermanDataProcessor implements DataProcessor {

  private final BankRepository bankRepository;

  public static void main(String[] args) {
    try (BufferedReader reader = new BufferedReader( new FileReader(file("bundesbank.txt")))) {

      String line = reader.readLine();

      while(line != null) {
        String bankCode = line.substring(0, 8);
        String name = line.substring(9, 67).trim();
        String postCode = line.substring(68, 72).trim();
        String city = line.substring(72, 107).trim();
        String shortName = line.substring(107, 134).trim();
        String pan = line.substring(134, 139).trim();
        String bic = line.substring(139, 150).trim();

        System.out.println(String.format("bank_code: %s name: %s post_code: %s city: %s short_name: %s pan: %s bic: %s", bankCode, name, postCode, city, shortName, pan, bic));



        line = reader.readLine();
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Async
  @Override
  public void process(String fileName) {

    //100000001Bundesbank                                                10591Berlin                             BBk Berlin                 20100MARKDEF110009011380U000000000
    //100100101Postbank                                                  10916Berlin                             Postbank Berlin            10010PBNKDEFF10024000538U000000000
    //100101111SEB                                                       10789Berlin                             SEB Berlin                 25805ESSEDE5F10013005361U000000000
    //100102221The Royal Bank of Scotland Niederlassung Frankfurt        10105Berlin                             RBS NDL Frankfurt          29190ABNADEFFBER10011142U000000000
    //100104241Aareal Bank                                               10666Berlin                             Aareal Bank                26910AARBDE5W10009004795U000000000
    //100196101Dexia Kommunalbank Deutschland                            10969Berlin                             Dexia Berlin                    DXIADEBBXXX09055273U000000000
    //100202001BHF-BANK                                                  10117Berlin                             BHF-BANK Berlin            25155BHFBDEFF10060004794U000000000





    log.info("Beginning German processing...");

    long startTime = System.currentTimeMillis();
    try (BufferedReader reader = new BufferedReader( new FileReader(file("bundesbank.txt")))) {

      String line = reader.readLine();

      while(line != null) {
        String bankCode = line.substring(0, 8);
        String name = line.substring(9, 67).trim();
        String postCode = line.substring(67, 72).trim();
        String city = line.substring(72, 107).trim();
        String shortName = line.substring(107, 134).trim();
        String bic = line.substring(139, 150).trim();
        bank(Bank.builder()
                .bankCode(bankCode)
                .bic(bic)
                .city(city)
                .postCode(postCode)
                .name(name)
                .shortName(shortName)
                .build());
        line = reader.readLine();
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      long endTime = System.currentTimeMillis();
      log.info("Total processing time: {}ms", endTime - startTime);
    }
  }

  private void bank(Bank input) {
    BankEntity bank = new BankEntity();
    bank.setBankCode(input.bankCode());
    bank.setBic(input.bic());
    bank.setCity(input.city());
    bank.setCountryName("Germany");
    bank.setCountryCode("DE");
    bank.setPostalCode(input.postCode());
    bank.setName(input.name());
    bank.setShortName(input.shortName());
    bankRepository.save(bank);
  }

  @Builder
  private record Bank(String bankCode, String name, String postCode, String city, String shortName, String bic) {}

}
