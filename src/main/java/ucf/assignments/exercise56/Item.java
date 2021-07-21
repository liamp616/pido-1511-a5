package ucf.assignments.exercise56;

public class Item {
    //  serialNumber: String
    private String serialNumber;
    //  name: String
    private String name;
    //  value: double
    private double value;

    //  create constructor
    public Item(String serialNumber, String name, double value) {
        this.serialNumber = serialNumber;
        this.name = name;
        this.value = value;
    }

    //  create getter and setter methods for variables
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
