package com.pyur.ibanvalidator.processor;

import com.pyur.ibanvalidator.model.BankEntity;
import com.pyur.ibanvalidator.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.FileReader;

import static com.pyur.ibanvalidator.util.ProcessorUtil.file;

@Slf4j
@Component
@RequiredArgsConstructor
public class GermanDataProcessor implements DataProcessor {

  private final BankRepository bankRepository;

  @Async
  @Override
  public void process(String fileName) {
    log.info("Beginning German processing...");
    long startTime = System.currentTimeMillis();
    try (FileReader reader = new FileReader(file(fileName))) {
      CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(NAME, POST_CODE, CITY, SHORT_NAME, PAN, BIC, BANK_CODE).withSkipHeaderRecord(true);

      Iterable<CSVRecord> records = csvFormat.parse(reader);

      for (CSVRecord record : records) {

        String bic = record.get(BIC);

        if (StringUtils.isNotBlank(bic)) {
          bank(record);
        }

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
    bank.setBic(record.get(BIC));
    bank.setCity(record.get(CITY));
    bank.setCountry("Germany");
    bank.setPostalCode(record.get(POST_CODE));
    bankRepository.save(bank);
  }

}
