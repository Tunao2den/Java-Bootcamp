public class SmartPhone extends Phone{
    public int storage;
    public int ram;
    public int camera;

    public SmartPhone(String id, String name, double price, String phoneNumber, int storage, int ram, int camera) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.phoneNumber = phoneNumber;
        this.storage = storage;
        this.ram = ram;
        this.camera = camera;
    }
}
