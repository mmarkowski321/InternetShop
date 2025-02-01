package model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final List<CartItem> items = new ArrayList<>();

    public void addItem(CartItem item) {
        for (CartItem cartItem : items) {
            if (cartItem.getProductId() == item.getProductId()) {
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                return;
            }
        }
        items.add(item);
    }

    public void removeItem(int productId) {
        items.removeIf(item -> item.getProductId() == productId);
    }

    public void updateItemQuantity(int productId, int newQuantity) {
        for (CartItem cartItem : items) {
            if (cartItem.getProductId() == productId) {
                cartItem.setQuantity(newQuantity);
                return;
            }
        }
    }

    public List<CartItem> getItems() {
        return items;
    }

    public float getTotalPrice() {
        float total = 0;
        for (CartItem item : items) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
    }
}
