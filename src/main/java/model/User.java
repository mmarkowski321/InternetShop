package model;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;
    private int addressId;

    public User(int id, String firstName, String lastName, String phoneNumber, String email, String password, int addressId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.addressId = addressId;
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public int getAddressId() { return addressId; }
}