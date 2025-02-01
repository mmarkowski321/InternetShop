package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // URL bazy danych PostgreSQL
    private static final String URL = "jdbc:postgresql://localhost:5432/SklepOdziezowy";

    // Dane logowania dla administratora
    private static final String ADMIN_USER = "administrator";
    private static final String ADMIN_PASSWORD = "SklepOdziezowy321!";

    // Dane logowania dla klienta
    private static final String CLIENT_USER = "klient";
    private static final String CLIENT_PASSWORD = "klient_password";

    // Dane logowania dla klienta-obserwatora
    private static final String OBSERVER_USER = "klient_obserwator";
    private static final String OBSERVER_PASSWORD = "observer_password";

    /**
     * Tworzy połączenie z bazą danych w zależności od roli użytkownika.
     *
     * @param role Rola użytkownika (administrator, klient, klient_obserwator)
     * @return Obiekt Connection do bazy danych
     * @throws SQLException Jeśli połączenie z bazą danych nie powiodło się
     * @throws IllegalArgumentException Jeśli podano nieznaną rolę użytkownika
     */
    public static Connection getConnection(String role) throws SQLException {
        String user;
        String password;

        // Wybór użytkownika i hasła na podstawie roli
        switch (role) {
            case "administrator" -> {
                user = ADMIN_USER;
                password = ADMIN_PASSWORD;
            }
            case "klient" -> {
                user = CLIENT_USER;
                password = CLIENT_PASSWORD;
            }
            case "klient_obserwator" -> {
                user = OBSERVER_USER;
                password = OBSERVER_PASSWORD;
            }
            default -> throw new IllegalArgumentException("Nieznana rola użytkownika: " + role);
        }

        // Nawiązanie połączenia z bazą danych
        return DriverManager.getConnection(URL, user, password);
    }

    /**
     * Testuje połączenie z bazą danych dla każdej roli.
     */
    public static void testConnections() {
        try {
            System.out.println("Test połączenia jako administrator...");
            try (Connection conn = getConnection("administrator")) {
                System.out.println("Połączenie jako administrator: SUKCES");
            }

            System.out.println("Test połączenia jako klient...");
            try (Connection conn = getConnection("klient")) {
                System.out.println("Połączenie jako klient: SUKCES");
            }

            System.out.println("Test połączenia jako klient-obserwator...");
            try (Connection conn = getConnection("klient_obserwator")) {
                System.out.println("Połączenie jako klient-obserwator: SUKCES");
            }
        } catch (SQLException e) {
            System.err.println("Błąd połączenia: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Nieprawidłowa rola: " + e.getMessage());
        }
    }
}
