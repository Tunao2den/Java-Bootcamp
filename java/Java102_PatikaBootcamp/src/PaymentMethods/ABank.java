package PaymentMethods;

public class ABank implements IBank{
    private String bankName;
    private String terminalID;
    private String password;

    public boolean connect(String ipAddress){
        System.out.println("Connected to the " + this.bankName);
        return true;
    }
    public boolean payment(double price, String cardNumber, String expireDate, int cvc){
        System.out.println("Waiting for response...");
        System.out.println("Payment succesfully");
        return true;
    }

    public ABank(String bankName, String terminalID, String password) {
        this.bankName = bankName;
        this.terminalID = terminalID;
        this.password = password;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getTerminalID() {
        return terminalID;
    }

    public void setTerminalID(String terminalID) {
        this.terminalID = terminalID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
