package com.axelor.apps.data.mining.service;

import com.axelor.apps.tool.file.CsvTool;
import com.axelor.meta.MetaFiles;
import com.axelor.meta.db.MetaFile;
import com.axelor.meta.db.repo.MetaFileRepository;
import com.google.inject.Inject;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class InvoiceAnalysisServiceImpl implements InvoiceAnalysisService {

  public static final String PATH_TO_APRIORI_PYTHON_FILE =
      "/home/axelor/Projects/aos-dmbi-master/axelor-erp/modules/axelor-data-mining/src/main/resources/python/apriori.py";

  public static final String INPUT_DATASET_NAME = "invoice_product_data.csv";

  protected MetaFileRepository metaFileRepo;

  @Inject
  public InvoiceAnalysisServiceImpl(MetaFileRepository metaFileRepo) {
    this.metaFileRepo = metaFileRepo;
  }

  @Override
  public String runAprioriAnalysis(
      Map<String, Object> inputFileMap, BigDecimal minSupport, BigDecimal minConfidence)
      throws IOException {
    String analysisResult = "";

    if (inputFileMap != null) {
      MetaFile dataFile = metaFileRepo.find(Long.parseLong(inputFileMap.get("id").toString()));
      List<String[]> csvContent =
          CsvTool.cSVFileReader(MetaFiles.getPath(dataFile).toString(), ',');
      csvContent.remove(0); // Removing header

      csvContent.sort(
          (s1, s2) -> s2.length - s1.length); // Sorting in descending order based on size

      File tempDir = java.nio.file.Files.createTempDirectory(null).toFile();
      File csvFile = new File(tempDir, INPUT_DATASET_NAME);
      CsvTool.csvWriter(tempDir.getAbsolutePath(), INPUT_DATASET_NAME, ',', null, csvContent);

      String pathToInputFile = csvFile.getAbsolutePath();

      String[] cmd = {
        "python",
        PATH_TO_APRIORI_PYTHON_FILE,
        pathToInputFile,
        minSupport.toString(),
        minConfidence.toString()
      };

      Process p = Runtime.getRuntime().exec(cmd);
      BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String line;
      while ((line = in.readLine()) != null) {
        analysisResult += line + "\n";
      }
    }
    return analysisResult;
  }
}
