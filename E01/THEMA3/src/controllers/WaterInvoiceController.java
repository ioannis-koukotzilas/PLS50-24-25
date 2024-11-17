package controllers;

import services.WaterInvoiceService;

public class WaterInvoiceController {
  public WaterInvoiceController() {}

  public void initInvoiceCalculator() {
    WaterInvoiceService waterInvoiceService = new WaterInvoiceService();
    waterInvoiceService.initInvoiceCalculator();
  }

  public void initMultipleInvoiceCalculator() {
    WaterInvoiceService waterInvoiceService = new WaterInvoiceService();
    waterInvoiceService.initMultipleInvoiceCalculator();
  }
}