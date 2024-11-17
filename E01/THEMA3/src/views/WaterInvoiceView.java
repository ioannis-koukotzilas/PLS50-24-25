package views;

import java.util.Scanner;

public class WaterInvoiceView {
  private Scanner scanner = new Scanner(System.in);

  public int readConsumerNumber() {
    System.out.printf("Eisagete arithmo katanaloti: ");
    int consumerNumber = scanner.nextInt();
    scanner.nextLine();
    return consumerNumber;
  }

  public int readWater() {
    System.out.printf("Eisagete katanalosi nerou se kybika metra: ");
    int water = scanner.nextInt();
    scanner.nextLine();
    return water;
  }

  public String readUserRepeatDecision() {
    System.out.printf("Thelete na epanalavete ti diadikasia? (Y/N): ");
    return scanner.nextLine();
  }

  public void displayMessage(String message) {
    System.out.println(message);
  }
}