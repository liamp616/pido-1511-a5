package ucf.assignments.exercise56;

public class Item {
    //  name: String
    private String name;
    //  serialNumber: String
    private String serialNumber;
    //  value: double
    private double value;

    //  create constructor
    public Item(String name, String serialNumber, double value) {
        this.name = name;
        this.serialNumber = serialNumber;
        this.value = value;
    }

    //  create getter and setter methods for variables
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
