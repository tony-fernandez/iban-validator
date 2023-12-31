package com.pyur.ibanvalidator.processor;

import com.pyur.ibanvalidator.model.BankEntity;
import com.pyur.ibanvalidator.repository.BankRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.FileReader;

import static com.pyur.ibanvalidator.util.ProcessorUtil.file;

@Slf4j
@Component
@RequiredArgsConstructor
public class SwissDataProcessor implements DataProcessor {

  private final BankRepository bankRepository;

  public static void main(String[] args) {
    log.info("Beginning Swiss processing...");
    long startTime = System.currentTimeMillis();
    try (FileReader reader = new FileReader(file("swiss.csv"))) {
      CSVFormat csvFormat = CSVFormat.DEFAULT
              .withHeader("group","bank_code","branch_id","bank_code_new","sic_number","head_office_bank_code","bank_code_type","valid_from","sic","euro_sic","language","short_name","name","domicile_address","postal_address","post_code","city","phone","fax","phone_code","country_code","postal_account","swift")
              .withSkipHeaderRecord(true);

      Iterable<CSVRecord> records = csvFormat.parse(reader);

      for (CSVRecord record : records) {

        String bic = record.get(SWIFT);

        if (org.apache.commons.lang3.StringUtils.isNotBlank(bic)) {

        }

      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      long endTime = System.currentTimeMillis();
      log.info("Total processing time: {}ms", endTime - startTime);
    }
  }


  @Override
  public void process(String fileName) {
    log.info("Beginning Swiss processing...");
    long startTime = System.currentTimeMillis();
    try (FileReader reader = new FileReader(file(fileName))) {
      CSVFormat csvFormat = CSVFormat.DEFAULT
              .withHeader("group","bank_code","branch_id","bank_code_new","sic_number","head_office_bank_code","bank_code_type","valid_from","sic","euro_sic","language","short_name","name","domicile_address","postal_address","post_code","city","phone","fax","phone_code","country_code","postal_account","swift")
          .withSkipHeaderRecord(true);

      Iterable<CSVRecord> records = csvFormat.parse(reader);

      for (CSVRecord record : records) {
        bank(record);
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      long endTime = System.currentTimeMillis();
      log.info("Total processing time: {}ms", endTime - startTime);
    }
  }

  private void bank(CSVRecord record) {
    BankEntity bank = new BankEntity();

    String newBankCode = record.get(BANK_CODE_NEW);

    if (StringUtils.isNotBlank(newBankCode)) {
      bank.setBankCode(newBankCode);
    } else {
      bank.setBankCode(record.get(BANK_CODE));
    }

    if (bank.getBankCode().length() == 4) {
      bank.setBankCode("0" + bank.getBankCode());
    }

    bank.setBic(record.get(SWIFT));
    bank.setCity(record.get(CITY));
    bank.setCountryName("Switzerland");
    bank.setCountryCode("CH");
    bank.setPostalCode(record.get(POST_CODE));
    bank.setName(record.get(NAME));
    bank.setShortName(record.get(SHORT_NAME));
    bankRepository.save(bank);
  }


}
