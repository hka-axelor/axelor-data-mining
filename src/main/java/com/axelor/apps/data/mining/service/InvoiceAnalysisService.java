package com.axelor.apps.data.mining.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public interface InvoiceAnalysisService {

  public String runAprioriAnalysis(
      Map<String, Object> inputFileMap, BigDecimal minSupport, BigDecimal minConfidence)
      throws IOException;
}
