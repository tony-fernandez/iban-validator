package com.pyur.ibanvalidator.processor;

import com.pyur.ibanvalidator.model.CountryEntity;
import com.pyur.ibanvalidator.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.FileReader;

import static com.pyur.ibanvalidator.util.ProcessorUtil.file;

@Slf4j
@Component
@RequiredArgsConstructor
public class CountryDataProcessor implements DataProcessor {

  private final CountryRepository countryRepository;

  @Async
  @Override
  public void process(String fileName) {
    log.info("Beginning Country processing...");
    long startTime = System.currentTimeMillis();
    try (FileReader reader = new FileReader(file(fileName))) {
      CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(NAME, CODE).withSkipHeaderRecord(true);

      Iterable<CSVRecord> records = csvFormat.parse(reader);

      for (CSVRecord record : records) {
        country(record);
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      long endTime = System.currentTimeMillis();
      log.info("Total processing time: {}ms", endTime - startTime);
    }

  }

  private void country(CSVRecord record) {
    CountryEntity country = new CountryEntity();
    country.setCode(record.get(CODE));
    country.setName(record.get(NAME));
    countryRepository.save(country);
  }

}
