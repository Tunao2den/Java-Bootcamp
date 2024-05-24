package PaymentMethods;

public interface IBank {
     boolean connect(String ipAddress);
     boolean payment(double price, String cardNumber, String expireDate, int cvc);
}
