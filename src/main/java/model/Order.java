package model;

class Order {
    private int id;
    private float value;
    private String purchaseDate;
    private int statusId;
    private int invoiceNumber;
    private int userId;

    public Order(int id, float value, String purchaseDate, int statusId, int invoiceNumber, int userId) {
        this.id = id;
        this.value = value;
        this.purchaseDate = purchaseDate;
        this.statusId = statusId;
        this.invoiceNumber = invoiceNumber;
        this.userId = userId;
    }

    public int getId() { return id; }
    public float getValue() { return value; }
    public String getPurchaseDate() { return purchaseDate; }
    public int getStatusId() { return statusId; }
    public int getInvoiceNumber() { return invoiceNumber; }
    public int getUserId() { return userId; }
}