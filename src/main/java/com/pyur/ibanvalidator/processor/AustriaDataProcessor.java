package com.pyur.ibanvalidator.processor;

import com.pyur.ibanvalidator.model.BankEntity;
import com.pyur.ibanvalidator.repository.BankRepository;
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
public class AustriaDataProcessor implements DataProcessor {

  private final BankRepository bankRepository;

  public static void main(String[] args) {
    log.info("Beginning Austria processing...");
    long startTime = System.currentTimeMillis();
    try (FileReader reader = new FileReader(file("austria.csv"))) {
      CSVFormat csvFormat = CSVFormat.DEFAULT
              .withHeader("main_institution_flag","identity_number","bank_code","institute_type","sector","company_register_number","name","street","post_code","city","postal_address_street","postal_address_post_code","postal_address_city","po_box","federal_state","phone_number","fax","e_mail","swift","home_page","foundation_date")
              .withSkipHeaderRecord(true);

      Iterable<CSVRecord> records = csvFormat.parse(reader);

      for (CSVRecord record : records) {
        String bic = record.get(SWIFT);
        log.info(bic);
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
    log.info("Beginning Austrian processing...");
    long startTime = System.currentTimeMillis();
    try (FileReader reader = new FileReader(file(fileName))) {
      CSVFormat csvFormat = CSVFormat.DEFAULT
              .withHeader("main_institution_flag","identity_number","bank_code","institute_type","sector","company_register_number","name","street","post_code","city","postal_address_street","postal_address_post_code","postal_address_city","po_box","federal_state","phone_number","fax","e_mail","swift","home_page","foundation_date")
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

    bank.setBankCode(record.get(BANK_CODE));
    bank.setBic(record.get(SWIFT));
    bank.setCity(record.get(CITY));
    bank.setCountryName("Austria");
    bank.setCountryCode("AT");
    bank.setPostalCode(record.get(POST_CODE));
    bank.setName(record.get(NAME));
    bankRepository.save(bank);
  }


}
