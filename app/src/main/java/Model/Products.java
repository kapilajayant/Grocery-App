package Model;

public class Products
{

    private String Name, Price, Quantity;


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public Products() {
    }

    public Products(String name, String price, String quantity) {
        Name = name;
        Price = price;
        Quantity = quantity;
    }
}