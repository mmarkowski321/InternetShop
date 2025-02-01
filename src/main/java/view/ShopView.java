package view;

import model.Administrator;
import model.User;
import presenter.ShopPresenter;

public interface ShopView {
    void showMessage(String message);
    void showUserMenu(User user);
    void showMainMenu(ShopPresenter presenter);
    void showAdminMenu(Administrator admin);
}
