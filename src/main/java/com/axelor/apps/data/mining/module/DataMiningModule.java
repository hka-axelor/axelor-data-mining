package com.axelor.apps.data.mining.module;

import com.axelor.app.AxelorModule;
import com.axelor.apps.data.mining.service.InvoiceAnalysisService;
import com.axelor.apps.data.mining.service.InvoiceAnalysisServiceImpl;

public class DataMiningModule extends AxelorModule {

  @Override
  protected void configure() {
    bind(InvoiceAnalysisService.class).to(InvoiceAnalysisServiceImpl.class);
  }
}
