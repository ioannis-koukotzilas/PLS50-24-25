package services;

import java.util.Arrays;

import helpers.WaterInvoiceHelper;
import models.entities.GainsSorted;
import models.entities.WaterInvoice;
import views.WaterInvoiceView;

public class WaterInvoiceService {
  private static final int FIXED_FEE = 8;
  private double[] gain;

  private WaterInvoiceView waterInvoiceView;

  public WaterInvoiceService() {
    this.waterInvoiceView = new WaterInvoiceView();
    this.gain = new double[5];
  }

  public void initInvoiceCalculator() {
    int userConsumerNumber = waterInvoiceView.readConsumerNumber();
    int userWaterConsumption = waterInvoiceView.readWater();

    WaterInvoice tiredWaterInvoice = calculateInvoice(userWaterConsumption);
    WaterInvoice fixedWaterInvoice = calculateInvoiceFixed(userWaterConsumption);

    displayInvoice(userConsumerNumber, userWaterConsumption, tiredWaterInvoice, fixedWaterInvoice);
  }

  public void initMultipleInvoiceCalculator() {
    for (int i = 0; i < gain.length; i++) {
      int userWaterConsumption = waterInvoiceView.readWater();

      WaterInvoice tiredWaterInvoice = calculateInvoice(userWaterConsumption);
      WaterInvoice fixedWaterInvoice = calculateInvoiceFixed(userWaterConsumption);

      gain[i] = tiredWaterInvoice.total - fixedWaterInvoice.total;
    }

    GainsSorted gainsSorted = calculateLargest(gain);

    displayGainResult(gainsSorted);

    askForRepeatCalculations();
  }

  private void askForRepeatCalculations() {
    String userInput = waterInvoiceView.readUserRepeatDecision();
    userInput = userInput.trim().toLowerCase();

    if ("y".equals(userInput)) {
      initMultipleInvoiceCalculator();
    } else if ("n".equals(userInput)) {
      return;
    } else {
      waterInvoiceView.displayMessage("Mh egkyrh eisagwgh");
      askForRepeatCalculations();
    }
  }

  private WaterInvoice calculateInvoice(int userWaterConsumption) {
    WaterInvoice waterInvoice = new WaterInvoice();
    waterInvoice.waterCharge = calculateWaterCharge(userWaterConsumption);
    waterInvoice.sewerageFee = calculateSewerageFee(waterInvoice.waterCharge);
    waterInvoice.specialWaterFee = calculateSpecialWaterFee(userWaterConsumption);
    waterInvoice.waterVAT = calculateWaterVAT(waterInvoice.waterCharge);
    waterInvoice.feesVAT = calculateFeesVAT(FIXED_FEE, waterInvoice.sewerageFee, waterInvoice.specialWaterFee);
    waterInvoice.total = calculateTotal(FIXED_FEE, waterInvoice.waterCharge, waterInvoice.sewerageFee, waterInvoice.specialWaterFee, waterInvoice.waterVAT, waterInvoice.feesVAT);

    return waterInvoice;
  }

  private WaterInvoice calculateInvoiceFixed(int userWaterConsumption) {
    WaterInvoice waterInvoice = new WaterInvoice();
    waterInvoice.waterCharge = calculateWaterChargeFixed(userWaterConsumption);
    waterInvoice.sewerageFee = calculateSewerageFee(waterInvoice.waterCharge);
    waterInvoice.specialWaterFee = calculateSpecialWaterFee(userWaterConsumption);
    waterInvoice.waterVAT = calculateWaterVAT(waterInvoice.waterCharge);
    waterInvoice.feesVAT = calculateFeesVAT(null, waterInvoice.sewerageFee, waterInvoice.specialWaterFee);
    waterInvoice.total = calculateTotal(null, waterInvoice.waterCharge, waterInvoice.sewerageFee, waterInvoice.specialWaterFee, waterInvoice.waterVAT, waterInvoice.feesVAT);

    return waterInvoice;
  }

  private double calculateWaterCharge(int userWaterConsumption) {
    double waterCharge = 0;

    if (userWaterConsumption > 160) {
      waterCharge += (userWaterConsumption - 160) * 4.670;
      userWaterConsumption = 160;
    }

    if (userWaterConsumption > 120) {
      waterCharge += (userWaterConsumption - 120) * 3.820;
      userWaterConsumption = 120;
    }

    if (userWaterConsumption > 60) {
      waterCharge += (userWaterConsumption - 60) * 1.273;
      userWaterConsumption = 60;
    }

    if (userWaterConsumption > 40) {
      waterCharge += (userWaterConsumption - 40) * 0.743;
      userWaterConsumption = 40;
    }

    if (userWaterConsumption > 10) {
      waterCharge += (userWaterConsumption - 10) * 0.636;
      userWaterConsumption = 10;
    }

    if (userWaterConsumption > 0) {
      waterCharge += userWaterConsumption * 0.420;
    }

    return waterCharge;
  }

  private double calculateWaterChargeFixed(int userWaterConsumption) {
    double waterCharge = 0;

    if (userWaterConsumption > 0) {
      waterCharge = userWaterConsumption * 0.5;
    }

    return waterCharge;
  }

  private double calculateSewerageFee(double waterCharge) {
    return waterCharge * 0.8;
  }

  private double calculateSpecialWaterFee(int userWaterConsumption) {
    return userWaterConsumption * 0.07;
  }

  private double calculateWaterVAT(double waterCharge) {
    return waterCharge * 0.13;
  }

  private double calculateFeesVAT(Integer fixedFee, double sewerageFee, double specialWaterFee) {
    if (fixedFee != null) {
      return (fixedFee + sewerageFee + specialWaterFee) * 0.24;
    } else {
      return (sewerageFee + specialWaterFee) * 0.24;
    }
  }

  private double calculateTotal(Integer fixedFee, double waterCharge, double sewerageFee, double specialWaterFee, double waterVAT, double feesVAT) {
    if (fixedFee != null) {
      return fixedFee + waterCharge + sewerageFee + specialWaterFee + waterVAT + feesVAT;
    } else {
      return waterCharge + sewerageFee + specialWaterFee + waterVAT + feesVAT;
    }
  }

  private GainsSorted calculateLargest(double[] gain) {
    GainsSorted gainsSorted = new GainsSorted();

    double[] sortedGain = gain.clone();
    Arrays.sort(sortedGain);

    gainsSorted.largest = sortedGain[sortedGain.length - 1];
    gainsSorted.secondLargest = sortedGain[sortedGain.length - 2];

    gainsSorted.largestIndex = -1;
    gainsSorted.secondLargestIndex = -1;

    for (int i = 0; i < gain.length; i++) {
      if (gain[i] == gainsSorted.largest && gainsSorted.largestIndex == -1) {
        gainsSorted.largestIndex = i;
      } else if (gain[i] == gainsSorted.secondLargest && gainsSorted.secondLargestIndex == -1) {
        gainsSorted.secondLargestIndex = i;
      }
    }

    return gainsSorted;
  }

  private void displayInvoice(int consumerNumber, int waterConsumption, WaterInvoice tiredWaterInvoice, WaterInvoice fixedWaterInvoice) {
    waterInvoiceView.displayMessage("Arithmos katanaloti: " + consumerNumber);
    waterInvoiceView.displayMessage("Ogkos nerou: " + waterConsumption);
    waterInvoiceView.displayMessage("Xrewsi nerou: " + WaterInvoiceHelper.formatDouble2Digits(tiredWaterInvoice.waterCharge));
    waterInvoiceView.displayMessage("Xrewsi apoxeteusis: " + WaterInvoiceHelper.formatDouble2Digits(tiredWaterInvoice.sewerageFee));
    waterInvoiceView.displayMessage("Eidiko telos kyklou nerou: " + WaterInvoiceHelper.formatDouble2Digits(tiredWaterInvoice.specialWaterFee));
    waterInvoiceView.displayMessage("FPA klimakwtis xrewsis: " + WaterInvoiceHelper.formatDouble2Digits(tiredWaterInvoice.waterVAT + tiredWaterInvoice.feesVAT));
    waterInvoiceView.displayMessage("SYNOLIKO POSO KLIMAKWTIS XREWSIS: " + WaterInvoiceHelper.formatDouble2Digits(tiredWaterInvoice.total));
    waterInvoiceView.displayMessage("FPA statheris xrewsis: " + WaterInvoiceHelper.formatDouble2Digits(fixedWaterInvoice.waterVAT + fixedWaterInvoice.feesVAT));
    waterInvoiceView.displayMessage("SYNOLIKO POSO STATHERIS XREWSIS: " + WaterInvoiceHelper.formatDouble2Digits(fixedWaterInvoice.total));
  }

  private void displayGainResult(GainsSorted gainsSorted) {
    waterInvoiceView.displayMessage("I megalyteri (se apolyti timi) diafora xreosis einai: " + WaterInvoiceHelper.formatDouble2Digits(gainsSorted.largest) + " sti thesi " + gainsSorted.largestIndex);
    waterInvoiceView.displayMessage("I defteri megalyteri (se apolyti timi) diafora xreosis einai: " + WaterInvoiceHelper.formatDouble2Digits(gainsSorted.secondLargest) + " sti thesi " + gainsSorted.secondLargestIndex);
  }
}