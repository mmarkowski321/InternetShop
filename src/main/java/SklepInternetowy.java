import presenter.ShopPresenter;
import presenter.ShopPresenterImpl;
import view.ConsoleShopView;
import view.ShopView;

public class SklepInternetowy {
    public static void main(String[] args) {
        ShopView view = new ConsoleShopView();
        ShopPresenter presenter = new ShopPresenterImpl(view);

        view.showMainMenu(presenter);
    }
}
