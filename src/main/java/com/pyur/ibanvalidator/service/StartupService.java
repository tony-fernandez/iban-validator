package com.pyur.ibanvalidator.service;

import com.pyur.ibanvalidator.processor.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class StartupService {

  private final AustriaDataProcessor austriaDataProcessor;
  private final ExecutorService executorService;
  private final CountryDataProcessor countryDataProcessor;
  private final GermanDataProcessor germanDataProcessor;
  private final SwissDataProcessor swissDataProcessor;

  @PostConstruct
  public void init() {
    Map<String, DataProcessor> processors = new HashMap<>();
    processors.put("austria.csv", austriaDataProcessor);
    processors.put("countries.csv", countryDataProcessor);
    processors.put("bundersbank.csv", germanDataProcessor);
    processors.put("swiss.csv", swissDataProcessor);

    List<CompletableFuture<Void>> allPurges = new ArrayList<>();

    for (Entry<String, DataProcessor> entry: processors.entrySet()) {
      allPurges.add(CompletableFuture.runAsync(() -> entry.getValue().process(entry.getKey()), executorService));
    }

    CompletableFuture<Void> allFutures = CompletableFuture.allOf(allPurges.toArray(new CompletableFuture[0]));

    CompletableFuture<List<Void>> completed = allFutures.thenApply(
        f -> allPurges.stream()
            .map(CompletableFuture::join)
            .collect(toList()));

  }

}
