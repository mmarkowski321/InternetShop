package model;

class OrderStatus {
    private int id;
    private String status;

    public OrderStatus(int id, String status) {
        this.id = id;
        this.status = status;
    }

    public int getId() { return id; }
    public String getStatus() { return status; }
}