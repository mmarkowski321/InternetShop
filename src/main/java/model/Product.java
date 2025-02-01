package model;

class Product {
    private int id;
    private int imageId;
    private int categoryId;
    private String productName;
    private String description;
    private float price;

    public Product(int id, int imageId, int categoryId, String productName, String description, float price) {
        this.id = id;
        this.imageId = imageId;
        this.categoryId = categoryId;
        this.productName = productName;
        this.description = description;
        this.price = price;
    }

    public int getId() { return id; }
    public int getImageId() { return imageId; }
    public int getCategoryId() { return categoryId; }
    public String getProductName() { return productName; }
    public String getDescription() { return description; }
    public float getPrice() { return price; }
}
