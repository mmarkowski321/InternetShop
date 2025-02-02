package view;

import model.Administrator;
import model.User;
import presenter.ShopPresenter;
import presenter.ShopPresenterImpl;

import java.util.Scanner;

public class ConsoleShopView implements ShopView {
    private final Scanner scanner = new Scanner(System.in);
    ShopPresenter presenter = new ShopPresenterImpl(this);

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }
    @Override
    public void showAdminMenu(Administrator admin) {
        while (true) {
            System.out.println("\nWitaj, " + admin.getFirstName() + " (Administrator)!");
            System.out.println("1. Zarządzaj produktami");
            System.out.println("2. Przeglądaj zamówienia");
            System.out.println("3. Zmień status zamówienia");
            System.out.println("4. Generuj raport sprzedaży");
            System.out.println("5. Wyloguj się");
            System.out.print("Wybierz opcję: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Usuwanie znaku nowej linii

            switch (choice) {
                case 1 -> presenter.manageProducts();
                case 2 -> presenter.browseOrders();
                case 3 -> {
                    // Pobranie danych od użytkownika dla zmiany statusu zamówienia
                    System.out.print("Podaj ID zamówienia: ");
                    int orderId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Podaj nowy status zamówienia (1 - Złożone, 2 - W trakcie realizacji, 3 - Zakończone, 4 - Anulowane): ");
                    int newStatus = scanner.nextInt();
                    scanner.nextLine();

                    presenter.updateOrderStatus(orderId, newStatus);
                }
                case 4 -> {
                    // Pobranie danych od użytkownika dla generowania raportu sprzedaży
                    System.out.print("Podaj datę początkową (YYYY-MM-DD): ");
                    String startDate = scanner.nextLine();
                    System.out.print("Podaj datę końcową (YYYY-MM-DD): ");
                    String endDate = scanner.nextLine();

                    presenter.generateSalesReport(startDate, endDate);
                }
                case 5 -> {
                    System.out.println("Wylogowano.");
                    return; // Powrót do głównego menu
                }
                default -> System.out.println("Niepoprawna opcja. Spróbuj ponownie.");
            }
        }
    }



    @Override
    public void showUserMenu(User user) {
        while (true) {
            System.out.println("\nWitaj, " + user.getFirstName() + "!");
            System.out.println("1. Przeglądaj produkty");
            System.out.println("2. Zobacz koszyk");
            System.out.println("3. Dodaj produkt do koszyka");
            System.out.println("4. Edytuj ilość produktu w koszyku");
            System.out.println("5. Opróżnij koszyk");
            System.out.println("6. Złóż zamówienie");
            System.out.println("7. Wyloguj się");
            System.out.print("Wybierz opcję: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Usuwanie znaku nowej linii

            switch (choice) {
                case 1 -> presenter.browseProducts();
                case 2 -> presenter.viewCart(user.getId());
                case 3 -> {
                    System.out.print("Podaj ID produktu: ");
                    int productId = scanner.nextInt();
                    System.out.print("Podaj ilość: ");
                    int quantity = scanner.nextInt();
                    presenter.addToCart(user.getId(), productId, quantity);
                }
                case 4 -> {
                    System.out.print("Podaj ID produktu: ");
                    int productId = scanner.nextInt();
                    System.out.print("Podaj nową ilość: ");
                    int quantity = scanner.nextInt();
                    presenter.editCart(user.getId(), productId, quantity);
                }
                case 5 -> presenter.clearCart(user.getId());
                case 6 -> presenter.placeOrder(user.getId());
                case 7 -> {
                    System.out.println("Wylogowano.");
                    return;
                }
                default -> System.out.println("Niepoprawna opcja. Spróbuj ponownie.");
            }
        }
    }



    @Override
    public void showMainMenu(ShopPresenter presenter) {
        while (true) {
            System.out.println("\n1. Zaloguj się");
            System.out.println("2. Zarejestruj się");
            System.out.println("3. Przeglądaj produkty");
            System.out.println("4. Wyjdź");
            System.out.print("Wybierz opcję: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Usuwanie znaku nowej linii

            switch (choice) {
                case 1 -> {
                    System.out.println("\n1.Zaloguj się jako klient");
                    System.out.println("2.Zaloguj się jako administrator");
                    int ch = scanner.nextInt();
                    scanner.nextLine();
                    switch (ch){
                        case 1 -> {
                            System.out.print("Podaj email: ");
                            String email = scanner.nextLine();
                            System.out.print("Podaj hasło: ");
                            String password = scanner.nextLine();
                            presenter.loginUser(email, password, "administrator");
                        }
                        case 2 ->{
                            System.out.print("Podaj email: ");
                            String email = scanner.nextLine();
                            System.out.print("Podaj hasło: ");
                            String password = scanner.nextLine();
                            presenter.loginAdmin(email, password, "administrator");
                        }
                    }
                    System.out.print("Podaj email: ");
                    String email = scanner.nextLine();
                    System.out.print("Podaj hasło: ");
                    String password = scanner.nextLine();
                    presenter.loginUser(email, password, "administrator");
                }
                case 2 -> {
                    System.out.print("Podaj imię: ");
                    String firstName = scanner.nextLine();
                    System.out.print("Podaj nazwisko: ");
                    String lastName = scanner.nextLine();
                    System.out.print("Podaj numer telefonu: ");
                    String phoneNumber = scanner.nextLine();
                    System.out.print("Podaj email: ");
                    String email = scanner.nextLine();
                    System.out.print("Podaj hasło: ");
                    String password = scanner.nextLine();
                    System.out.print("Podaj miasto: ");
                    String city = scanner.nextLine();
                    System.out.print("Podaj ulicę: ");
                    String street = scanner.nextLine();
                    System.out.print("Podaj numer budynku: ");
                    String buildingNumber = scanner.nextLine();
                    System.out.print("Podaj numer mieszkania (opcjonalnie): ");
                    String apartmentNumber = scanner.nextLine();
                    System.out.print("Podaj kod pocztowy (XX-XXX): ");
                    String postalCode = scanner.nextLine();
                    System.out.print("Podaj kraj: ");
                    String country = scanner.nextLine();
                    presenter.register(firstName, lastName, phoneNumber, email, password, city, street, buildingNumber, apartmentNumber, postalCode, country);

                }
                case 3-> {
                    presenter.browseProducts();
                }
                case 4 -> {
                    System.out.println("Zamykam aplikację.");
                    System.exit(0); // Kończy działanie aplikacji
                }
                default -> System.out.println("Niepoprawna opcja. Spróbuj ponownie.");
            }
        }
    }

}
