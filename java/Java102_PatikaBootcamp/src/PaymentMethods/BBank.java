package PaymentMethods;

public class BBank implements IBank{
    private String bankName;
    private String terminalID;
    private String password;
    @Override
    public boolean connect(String ipAddress){
        return true;
    }
    @Override
    public boolean payment(double price, String cardNumber, String expireDate, int cvc) {
        return false;
    }
    public BBank(String bankName, String terminalID, String password) {
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
