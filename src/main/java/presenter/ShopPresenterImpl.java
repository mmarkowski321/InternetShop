package presenter;

import model.*;
import view.ShopView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ShopPresenterImpl implements ShopPresenter {
    private final ShopView view;
    private final Cart cart = new Cart();
    private Scanner scanner = new Scanner(System.in);
    public ShopPresenterImpl(ShopView view) {
        this.view = view;
    }

    @Override
    public void loginUser(String email, String password, String role) {
        try (Connection conn = DatabaseConnection.getConnection(role);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM Uzytkownik WHERE Email = ? AND Haslo = ?")) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getInt("Id_uzytkownika"),
                        rs.getString("Imie"),
                        rs.getString("Nazwisko"),
                        rs.getString("Numer_telefonu"),
                        rs.getString("Email"),
                        rs.getString("Haslo"),
                        rs.getInt("Id_adresu")
                );
                view.showMessage("Zalogowano jako: " + user.getFirstName());
                view.showUserMenu(user);
            } else {
                view.showMessage("Niepoprawne dane logowania");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            view.showMessage("Błąd połączenia z bazą danych.");
        }
    }

    @Override
    public void loginAdmin(String email, String password, String role) {
        try (Connection conn = DatabaseConnection.getConnection(role);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM Administrator WHERE Email = ? AND Haslo = ?")) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Administrator admin = new Administrator(
                        rs.getInt("Id_administrator"),
                        rs.getString("Imie"),
                        rs.getString("Nazwisko"),
                        rs.getString("PESEL"),
                        rs.getInt("Id_adresu"),
                        rs.getString("Numer_bankowy"),
                        rs.getString("Email"),
                        rs.getString("Haslo")
                );
                view.showMessage("Zalogowano jako administrator: " + admin.getFirstName());
                view.showAdminMenu(admin);
            } else {
                view.showMessage("Niepoprawne dane logowania");
                System.out.println("Zapytanie nie zwróciło żadnych wyników.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            view.showMessage("Błąd połączenia z bazą danych.");
        }
    }


    @Override
    public void register(String firstName, String lastName, String phoneNumber, String email, String password, String city, String street, String buildingNumber, String apartmentNumber, String postalCode, String country) {
        try (Connection conn = DatabaseConnection.getConnection("administrator")) {
            conn.setAutoCommit(false); // Rozpocznij transakcję

            // Wstawienie nowego adresu do tabeli Adresy
            String insertAddressQuery = "INSERT INTO Adresy (Miasto, Ulica, Numer_budynku, Numer_mieszkania, Kod_pocztowy, Kraj) VALUES (?, ?, ?, ?, ?, ?) RETURNING Id_adresu";
            int addressId;
            try (PreparedStatement addressStmt = conn.prepareStatement(insertAddressQuery)) {
                addressStmt.setString(1, city);
                addressStmt.setString(2, street);
                addressStmt.setString(3, buildingNumber);
                addressStmt.setString(4, apartmentNumber);
                addressStmt.setString(5, postalCode);
                addressStmt.setString(6, country);

                ResultSet addressResult = addressStmt.executeQuery();
                if (addressResult.next()) {
                    addressId = addressResult.getInt("Id_adresu");
                } else {
                    throw new SQLException("Nie udało się dodać adresu.");
                }
            }

            // Wstawienie użytkownika do tabeli Uzytkownik
            String insertUserQuery = "INSERT INTO Uzytkownik (Imie, Nazwisko, Numer_telefonu, Email, Haslo, Id_adresu) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement userStmt = conn.prepareStatement(insertUserQuery)) {
                userStmt.setString(1, firstName);
                userStmt.setString(2, lastName);
                userStmt.setString(3, phoneNumber);
                userStmt.setString(4, email);
                userStmt.setString(5, password);
                userStmt.setInt(6, addressId);
                userStmt.executeUpdate();
            }

            conn.commit(); // Zatwierdź transakcję
            view.showMessage("Rejestracja zakończona sukcesem");
        } catch (SQLException e) {
            e.printStackTrace();
            view.showMessage("Błąd rejestracji.");
        }
    }


    @Override
    public void browseProducts() {
        try (Connection conn = DatabaseConnection.getConnection("klient");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Produkty");
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Lista produktów:");
            while (rs.next()) {
                int id = rs.getInt("Id_produktu");
                String name = rs.getString("Nazwa_produktu");
                String description = rs.getString("Opis");
                float price = rs.getFloat("Cena");

                System.out.printf("ID: %d, Nazwa: %s, Opis: %s, Cena: %.2f PLN%n", id, name, description, price);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            view.showMessage("Błąd podczas wczytywania produktów z bazy danych.");
        }
    }



    @Override
    public void addToCart(int userId, int productId, int quantity) {
        try (Connection conn = DatabaseConnection.getConnection("klient");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM Produkty WHERE Id_produktu = ?")) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String productName = rs.getString("Nazwa_produktu");
                float price = rs.getFloat("Cena");
                CartItem item = new CartItem(productId, productName, quantity, price);
                cart.addItem(item);
                view.showMessage("Dodano produkt do koszyka: " + productName);
            } else {
                view.showMessage("Nie znaleziono produktu o podanym ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            view.showMessage("Błąd podczas dodawania do koszyka.");
        }
    }

    @Override
    public void viewCart(int userId) {
        if (cart.isEmpty()) {
            view.showMessage("Koszyk jest pusty.");
        } else {
            view.showMessage("Zawartość koszyka:");
            for (CartItem item : cart.getItems()) {
                System.out.println(item);
            }
            System.out.printf("Łączna cena: %.2f PLN%n", cart.getTotalPrice());
        }
    }

    @Override
    public void editCart(int userId, int productId, int newQuantity) {
        cart.updateItemQuantity(productId, newQuantity);
        view.showMessage("Zaktualizowano ilość produktu w koszyku.");
    }

    @Override
    public void placeOrder(int userId) {
        if (cart.isEmpty()) {
            view.showMessage("Koszyk jest pusty. Dodaj produkty przed złożeniem zamówienia.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection("administrator")) {
            conn.setAutoCommit(false); // Rozpocznij transakcję

            // Wstawienie zamówienia do tabeli Zamowienia
            String insertOrderQuery = "INSERT INTO Zamowienia (Wartosc, Data_zakupu, Id_status_zamowienia, Id_uzytkownika) VALUES (?, CURRENT_DATE, 1, ?) RETURNING Id_zamowienia";
            try (PreparedStatement insertOrderStmt = conn.prepareStatement(insertOrderQuery)) {
                insertOrderStmt.setFloat(1, cart.getTotalPrice());
                insertOrderStmt.setInt(2, userId);

                ResultSet rs = insertOrderStmt.executeQuery();
                if (!rs.next()) {
                    throw new SQLException("Nie udało się utworzyć zamówienia.");
                }
                int orderId = rs.getInt("Id_zamowienia");

                // Wstawienie produktów z koszyka do Zamowione_Egzemplarze
                String insertOrderedItemsQuery = "INSERT INTO Zamowione_Egzemplarze (Id_egzemplarza, Id_zamowienia) VALUES (?, ?)";
                try (PreparedStatement insertOrderedItemsStmt = conn.prepareStatement(insertOrderedItemsQuery)) {
                    for (CartItem item : cart.getItems()) {
                        insertOrderedItemsStmt.setInt(1, item.getProductId());
                        insertOrderedItemsStmt.setInt(2, orderId);
                        insertOrderedItemsStmt.executeUpdate();
                    }
                }

                // Zatwierdzenie transakcji
                conn.commit();
                view.showMessage("Zamówienie zostało złożone pomyślnie!");
                cart.clear(); // Opróżnij koszyk po dokonaniu zakupu
            } catch (SQLException e) {
                conn.rollback(); // Wycofanie transakcji w przypadku błędu
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            view.showMessage("Błąd podczas składania zamówienia.");
        }
    }

    @Override
    public void generateSalesReport(String startDate, String endDate) {
        try (Connection conn = DatabaseConnection.getConnection("administrator")) {
            String query = "SELECT * FROM GenerujRaportSprzedazy(?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setDate(1, java.sql.Date.valueOf(startDate));
                stmt.setDate(2, java.sql.Date.valueOf(endDate));

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        view.showMessage("ID Zamówienia: " + rs.getInt("Id_zamowienia") +
                                ", Wartość: " + rs.getFloat("Wartosc") +
                                ", Data zakupu: " + rs.getDate("Data_zakupu"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            view.showMessage("Błąd podczas generowania raportu sprzedaży.");
        }
    }



    @Override
    public void trackOrder(int userId) {
        view.showMessage("Status zamówienia: W trakcie realizacji.");
    }

    @Override
    public void manageProducts() {
        while (true) {
            view.showMessage("\nZarządzanie produktami:");
            view.showMessage("1. Dodaj nowy produkt");
            view.showMessage("2. Dodaj nową kategorię");
            view.showMessage("3. Edytuj istniejący produkt");
            view.showMessage("4. Powrót");
            view.showMessage("Wybierz opcję: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    // Wyświetlenie dostępnych kategorii
                    try (Connection conn = DatabaseConnection.getConnection("administrator");
                         PreparedStatement stmt = conn.prepareStatement("SELECT Id_kategorii, Nazwa_kategorii FROM Kategorie")) {
                        ResultSet rs = stmt.executeQuery();
                        view.showMessage("Dostępne kategorie:");
                        while (rs.next()) {
                            int id = rs.getInt("Id_kategorii");
                            String name = rs.getString("Nazwa_kategorii");
                            view.showMessage("ID: " + id + ", Nazwa: " + name);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        view.showMessage("Błąd podczas pobierania kategorii: " + e.getMessage());
                        return;
                    }

                    // Kontynuowanie dodawania nowego produktu
                    System.out.print("Podaj nazwę produktu: ");
                    String productName = scanner.nextLine();
                    System.out.print("Podaj opis produktu: ");
                    String productDescription = scanner.nextLine();
                    System.out.print("Podaj cenę produktu: ");
                    float price = scanner.nextFloat();
                    System.out.print("Podaj ID kategorii produktu: ");
                    int categoryId = scanner.nextInt();
                    System.out.print("Podaj ID zdjęcia produktu (lub 0, jeśli brak): ");
                    int imageId = scanner.nextInt();
                    scanner.nextLine();

                    addNewProduct(productName, productDescription, price, categoryId, imageId);
                }
                case 2 -> {
                    // Wyświetlenie dostępnych kategorii przed dodaniem nowej
                    try (Connection conn = DatabaseConnection.getConnection("administrator");
                         PreparedStatement stmt = conn.prepareStatement("SELECT Id_kategorii, Nazwa_kategorii FROM Kategorie")) {
                        ResultSet rs = stmt.executeQuery();
                        view.showMessage("Aktualne kategorie:");
                        while (rs.next()) {
                            int id = rs.getInt("Id_kategorii");
                            String name = rs.getString("Nazwa_kategorii");
                            view.showMessage("ID: " + id + ", Nazwa: " + name);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        view.showMessage("Błąd podczas pobierania kategorii: " + e.getMessage());
                        return;
                    }

                    // Dodawanie nowej kategorii
                    System.out.print("Podaj nazwę nowej kategorii: ");
                    String categoryName = scanner.nextLine();
                    addCategory(categoryName);
                }
                case 3 -> {
                    // Wyświetlenie wszystkich produktów
                    try (Connection conn = DatabaseConnection.getConnection("administrator");
                         PreparedStatement stmt = conn.prepareStatement("SELECT Id_produktu, Nazwa_produktu, Opis, Cena FROM Produkty")) {
                        ResultSet rs = stmt.executeQuery();
                        view.showMessage("Dostępne produkty:");
                        while (rs.next()) {
                            int id = rs.getInt("Id_produktu");
                            String name = rs.getString("Nazwa_produktu");
                            String description = rs.getString("Opis");
                            float price = rs.getFloat("Cena");
                            view.showMessage("ID: " + id + ", Nazwa: " + name + ", Opis: " + description + ", Cena: " + price);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        view.showMessage("Błąd podczas pobierania produktów: " + e.getMessage());
                        return;
                    }

                    // Edytowanie istniejącego produktu
                    System.out.print("Podaj ID produktu, który chcesz edytować: ");
                    int productId = scanner.nextInt();
                    scanner.nextLine();
                    editProduct(productId);
                }
                case 4 -> {
                    view.showMessage("Powrót do menu administratora.");
                    return;
                }
                default -> view.showMessage("Niepoprawna opcja.");
            }
        }
    }

    @Override
    public void editProduct(int productId) {
        try (Connection conn = DatabaseConnection.getConnection("administrator")) {
            // Wyświetlenie aktualnych danych produktu
            String selectQuery = "SELECT Nazwa_produktu, Opis, Cena FROM Produkty WHERE Id_produktu = ?";
            try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
                selectStmt.setInt(1, productId);
                ResultSet rs = selectStmt.executeQuery();
                if (rs.next()) {
                    String currentName = rs.getString("Nazwa_produktu");
                    String currentDescription = rs.getString("Opis");
                    float currentPrice = rs.getFloat("Cena");

                    view.showMessage("Aktualne dane produktu:");
                    view.showMessage("Nazwa: " + currentName);
                    view.showMessage("Opis: " + currentDescription);
                    view.showMessage("Cena: " + currentPrice);

                    // Pobranie nowych wartości od użytkownika
                    System.out.print("Podaj nową nazwę produktu (lub zostaw puste, aby nie zmieniać): ");
                    String newName = scanner.nextLine();
                    if (newName.isEmpty()) newName = currentName;

                    System.out.print("Podaj nowy opis produktu (lub zostaw puste, aby nie zmieniać): ");
                    String newDescription = scanner.nextLine();
                    if (newDescription.isEmpty()) newDescription = currentDescription;

                    System.out.print("Podaj nową cenę produktu (lub wpisz 0, aby nie zmieniać): ");
                    float newPrice = scanner.nextFloat();
                    scanner.nextLine();
                    if (newPrice == 0) newPrice = currentPrice;

                    // Aktualizacja danych produktu
                    String updateQuery = "UPDATE Produkty SET Nazwa_produktu = ?, Opis = ?, Cena = ? WHERE Id_produktu = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setString(1, newName);
                        updateStmt.setString(2, newDescription);
                        updateStmt.setFloat(3, newPrice);
                        updateStmt.setInt(4, productId);

                        int rowsUpdated = updateStmt.executeUpdate();
                        if (rowsUpdated > 0) {
                            view.showMessage("Produkt został zaktualizowany pomyślnie!");
                        } else {
                            throw new RuntimeException("Nie udało się zaktualizować produktu.");
                        }
                    }
                } else {
                    view.showMessage("Nie znaleziono produktu o podanym ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Błąd podczas edytowania produktu: " + e.getMessage());
        }
    }




    @Override
    public void addNewProduct(String productName, String productDescription, float price, int categoryId, Integer imageId) {
        String insertQuery = "INSERT INTO Produkty (Nazwa_produktu, Opis, Cena, Id_kategorii, Id_zdjecia) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection("administrator");
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            // Ustawianie parametrów zapytania
            stmt.setString(1, productName);
            stmt.setString(2, productDescription);
            stmt.setFloat(3, price);
            stmt.setInt(4, categoryId);

            // Obsługa opcjonalnego ID zdjęcia
            if (imageId == 0) {
                stmt.setNull(5, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(5, imageId);
            }

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                view.showMessage("Produkt został dodany pomyślnie!");
            } else {
                throw new RuntimeException("Nie udało się dodać produktu.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Błąd podczas dodawania nowego produktu: " + e.getMessage());
        }
    }



    @Override
    public void addCategory(String categoryName) {
        String insertQuery = "INSERT INTO Kategorie (Nazwa_kategorii) VALUES (?)";

        try (Connection conn = DatabaseConnection.getConnection("administrator");
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            // Ustawianie parametrów zapytania
            stmt.setString(1, categoryName);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                view.showMessage("Kategoria została dodana pomyślnie!");
            } else {
                throw new RuntimeException("Nie udało się dodać kategorii.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Błąd podczas dodawania kategorii: " + e.getMessage());
        }
    }




    @Override
    public void browseOrders() {
        try (Connection conn = DatabaseConnection.getConnection("administrator")) {
            String query = "SELECT * FROM Zamowienia";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    view.showMessage("ID Zamówienia: " + rs.getInt("Id_zamowienia") +
                            ", Wartość: " + rs.getFloat("Wartosc") +
                            ", Data zakupu: " + rs.getDate("Data_zakupu") +
                            ", Status: " + rs.getInt("Id_status_zamowienia"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            view.showMessage("Błąd podczas pobierania zamówień.");
        }
    }
    @Override
    public void updateOrderStatus(int orderId, int newStatus) {
        try (Connection conn = DatabaseConnection.getConnection("administrator")) {
            conn.setAutoCommit(false); // Rozpocznij transakcję

            // Wyłączenie wyzwalaczy dla tabeli Zamowienia
            String disableTriggerQuery = "ALTER TABLE Zamowienia DISABLE TRIGGER ALL";
            try (PreparedStatement disableTriggerStmt = conn.prepareStatement(disableTriggerQuery)) {
                disableTriggerStmt.executeUpdate();
            }

            // Proste zapytanie SQL do aktualizacji statusu zamówienia
            String updateQuery = "UPDATE Zamowienia SET Id_status_zamowienia = ? WHERE Id_zamowienia = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setInt(1, newStatus);
                stmt.setInt(2, orderId);

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    view.showMessage("Status zamówienia został zaktualizowany.");
                } else {
                    view.showMessage("Nie znaleziono zamówienia o podanym ID.");
                }
            }

            // Włączenie wyzwalaczy dla tabeli Zamowienia
            String enableTriggerQuery = "ALTER TABLE Zamowienia ENABLE TRIGGER ALL";
            try (PreparedStatement enableTriggerStmt = conn.prepareStatement(enableTriggerQuery)) {
                enableTriggerStmt.executeUpdate();
            }

            conn.commit(); // Zatwierdź transakcję
        } catch (SQLException e) {
            e.printStackTrace();
            view.showMessage("Błąd podczas aktualizacji statusu zamówienia.");
        }
    }



    @Override
    public void viewSalesReports(int adminId) {
        view.showMessage("Raport sprzedaży.");
    }
    @Override
    public void clearCart(int userId) {
        cart.clear();
        view.showMessage("Koszyk został opróżniony.");
    }
}
