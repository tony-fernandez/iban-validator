package com.pyur.ibanvalidator.util;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

public final class ProcessorUtil {

  public static File file(String fileName) {
    try {
      return ResourceUtils.getFile(String.format("classpath:%s", fileName));
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  private ProcessorUtil() {

  }
}
