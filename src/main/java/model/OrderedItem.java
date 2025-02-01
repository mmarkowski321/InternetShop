package model;

class OrderedItem {
    private int id;
    private int itemId;
    private int orderId;

    public OrderedItem(int id, int itemId, int orderId) {
        this.id = id;
        this.itemId = itemId;
        this.orderId = orderId;
    }

    public int getId() { return id; }
    public int getItemId() { return itemId; }
    public int getOrderId() { return orderId; }
}