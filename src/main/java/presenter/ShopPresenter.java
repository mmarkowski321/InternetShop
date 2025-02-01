package presenter;

public interface ShopPresenter {
    void loginUser(String email, String password, String role);
    void loginAdmin(String email, String password, String role);
    void register(String firstName, String lastName, String phoneNumber, String email, String password, int addressId);
    void browseProducts();
    void addToCart(int userId, int productId, int quantity);
    void viewCart(int userId);
    void editCart(int userId, int productId, int newQuantity);
    void placeOrder(int userId);
    void trackOrder(int userId);
    void viewSalesReports(int adminId);
    void clearCart(int userId);
    void manageProducts();
    void addNewProduct(String name, String description, float price, int categoryId, Integer photoId);
    void browseOrders();
    void updateOrderStatus(int orderId, int newStatus);
    void generateSalesReport(String startDate, String endDate);

}
