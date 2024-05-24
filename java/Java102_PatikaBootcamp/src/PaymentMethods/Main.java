package PaymentMethods;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter the price: ");
        double price = input.nextDouble();

        System.out.println("Enter the card number: ");
        String cardNumber = input.next();

        System.out.println("Enter the expire date: ");
        String expireDate = input.next();

        System.out.println("Enter the cvc: ");
        int cvc = input.nextInt();

        System.out.println("1. aBank");
        System.out.println("2. bBank");
        System.out.println("3. cBank");
        System.out.println("Select the bank: ");
        int selectBank = input.nextInt();

        switch (selectBank){
            case 1:
                ABank aPos = new ABank("A Bank", "43513153453", "5343543535");
                aPos.connect("127.1.1.1");
                aPos.payment(price, cardNumber, expireDate, cvc);
                break;
            case 2:
                BBank bPos = new BBank("B Bank", "123123123123", "123123321");
                bPos.connect("127.0.1.1");
                bPos.payment(price, cardNumber, expireDate, cvc);
                break;
            default:
                System.out.println("Enter a valid bank!");
                break;
        }
    }
}
