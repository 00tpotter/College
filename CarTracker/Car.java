public class Car
{
    private String vin;
    private String make;
    private String model;
    private double price;
    private double mileage;
    private String color;

    public Car(String v, String ma, String mo, double p, double mi, String c)
    {
        vin = v;
        make = ma;
        model = mo;
        price = p;
        mileage = mi;
        color = c;
    }

    public String getVIN()
    {
        return vin;
    }

    public String getMake()
    {
        return make;
    }

    public String getModel()
    {
        return model;
    }

    public double getPrice()
    {
        return price;
    }

    public double getMileage()
    {
        return mileage;
    }
    
    public String getColor()
    {
        return color;
    }

    public String toString()
    {
        String output = "VIN: " + vin + "\tMake: " + make + "\tModel: " + model + "\tPrice: " + price + "\tMileage: " + mileage + "\tColor: " + color;
        return output;
    }
}