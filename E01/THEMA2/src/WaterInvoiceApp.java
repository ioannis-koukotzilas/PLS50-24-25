import controllers.WaterInvoiceController;

public class WaterInvoiceApp {
  public static void main(String[] args) {
    WaterInvoiceController waterInvoiceController = new WaterInvoiceController();
    waterInvoiceController.initMultipleInvoiceCalculator();
  }
}