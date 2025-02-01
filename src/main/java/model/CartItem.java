package model;

public class CartItem {
    private int productId;
    private String productName;
    private int quantity;
    private float price;

    public CartItem(int productId, String productName, int quantity, float price) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public float getPrice() { return price; }

    public float getTotalPrice() {
        return quantity * price;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Nazwa: %s, Ilość: %d, Cena jednostkowa: %.2f PLN, Łączna cena: %.2f PLN",
                productId, productName, quantity, price, getTotalPrice());
    }
}
