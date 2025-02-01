package model;

class Item {
    private int id;
    private int productId;
    private String size;
    private String color;

    public Item(int id, int productId, String size, String color) {
        this.id = id;
        this.productId = productId;
        this.size = size;
        this.color = color;
    }

    public int getId() { return id; }
    public int getProductId() { return productId; }
    public String getSize() { return size; }
    public String getColor() { return color; }
}