public class PhoneApp {
    SmartPhone smartPhone1 = new SmartPhone("1", "SmartPhone1", 1000.0, "0555 111 11 11", 32, 8, 12);
    SmartPhone smartPhone2 = new SmartPhone("2", "SmartPhone2", 1500.0, "0555 222 11 11", 64, 8, 36);
    SmartPhone smartPhone3 = new SmartPhone("3", "SmartPhone3", 500.0, "0555 333 11 11", 256, 8, 12);
    SmartPhone smartPhone4 = new SmartPhone("4", "SmartPhone4", 3500.0, "0555 444 11 11", 128, 8, 36);
    SmartPhone smartPhone5 = new SmartPhone("5", "SmartPhone5", 1750.0, "0555 555 11 11", 128, 8, 12);

    SmartPhone[] smartPhoneArray = {smartPhone1, smartPhone2, smartPhone3, smartPhone4, smartPhone5};

    public Double calculateTotalPrice(SmartPhone[] PhoneArray) {
        double total = 0.0;
        for (SmartPhone smartPhone : PhoneArray) {
            total += smartPhone.price;
        }
        return total;
    }
    public double totalSmartPhonePrice = calculateTotalPrice(smartPhoneArray);
}
