package model;

public class Administrator {
    private int id;
    private String firstName;
    private String lastName;
    private String pesel;
    private int addressId;
    private String bankNumber;
    private String email;
    private String password;

    public Administrator(int id, String firstName, String lastName, String pesel, int addressId, String bankNumber, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.addressId = addressId;
        this.bankNumber = bankNumber;
        this.email = email;
        this.password = password;
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPesel() { return pesel; }
    public int getAddressId() { return addressId; }
    public String getBankNumber() { return bankNumber; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}
