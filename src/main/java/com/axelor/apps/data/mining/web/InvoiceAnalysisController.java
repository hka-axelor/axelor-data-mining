package com.axelor.apps.data.mining.web;

import com.axelor.apps.data.mining.db.InvoiceAnalysis;
import com.axelor.apps.data.mining.db.repo.InvoiceAnalysisRepository;
import com.axelor.exception.service.TraceBackService;
import com.axelor.inject.Beans;
import com.axelor.meta.MetaFiles;
import com.axelor.meta.db.MetaFile;
import com.axelor.meta.db.repo.MetaFileRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.common.io.Files;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class InvoiceAnalysisController {

  @SuppressWarnings("unchecked")
  public void runAnalysis(ActionRequest request, ActionResponse response) {
    try {
      InvoiceAnalysis invoiceAnalysis = request.getContext().asType(InvoiceAnalysis.class);
      invoiceAnalysis = Beans.get(InvoiceAnalysisRepository.class).find(invoiceAnalysis.getId());

      Map<String, Object> map =
          (LinkedHashMap<String, Object>) request.getContext().get("invoiceMetaFile");

      if (map != null) {
        MetaFile dataFile =
            Beans.get(MetaFileRepository.class).find(Long.parseLong(map.get("id").toString()));
        File tempDir = java.nio.file.Files.createTempDirectory(null).toFile();
        File csvFile = new File(tempDir, "invoice_product_data.csv");
        Files.copy(MetaFiles.getPath(dataFile).toFile(), csvFile);

        BigDecimal minSupport = invoiceAnalysis.getMinSupport();
        BigDecimal minConfidence = invoiceAnalysis.getMinConfidence();

        String pathToFile = csvFile.getAbsolutePath();

        String[] cmd = {
          "python",
          "/home/axelor/Projects/aos-dmbi-master/axelor-erp/modules/axelor-data-mining/src/main/resources/python/apriori.py",
          pathToFile,
          minSupport.toString(),
          minConfidence.toString()
        };

        Process p = Runtime.getRuntime().exec(cmd);
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        String result = "";
        while ((line = in.readLine()) != null) {
          result += line + "\n";
        }
        response.setValue("analysisResult", result);
      }

    } catch (Exception e) {
      TraceBackService.trace(response, e);
    }
  }
}
