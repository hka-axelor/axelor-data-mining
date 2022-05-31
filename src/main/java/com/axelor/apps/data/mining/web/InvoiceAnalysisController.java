package com.axelor.apps.data.mining.web;

import com.axelor.apps.data.mining.db.InvoiceAnalysis;
import com.axelor.apps.data.mining.db.repo.InvoiceAnalysisRepository;
import com.axelor.apps.data.mining.service.InvoiceAnalysisService;
import com.axelor.exception.service.TraceBackService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class InvoiceAnalysisController {

  @SuppressWarnings("unchecked")
  public void runAnalysis(ActionRequest request, ActionResponse response) {
    try {
      InvoiceAnalysis invoiceAnalysis = request.getContext().asType(InvoiceAnalysis.class);
      invoiceAnalysis = Beans.get(InvoiceAnalysisRepository.class).find(invoiceAnalysis.getId());

      BigDecimal minSupport = invoiceAnalysis.getMinSupport();
      BigDecimal minConfidence = invoiceAnalysis.getMinConfidence();

      Map<String, Object> map =
          (LinkedHashMap<String, Object>) request.getContext().get("invoiceMetaFile");

      String result =
          Beans.get(InvoiceAnalysisService.class)
              .runAprioriAnalysis(map, minSupport, minConfidence);
      response.setValue("analysisResult", result);
    } catch (Exception e) {
      TraceBackService.trace(response, e);
    }
  }
}
