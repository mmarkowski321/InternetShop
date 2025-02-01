package model;

class Address {
    private int id;
    private String city;
    private String street;
    private String buildingNumber;
    private String apartmentNumber;
    private String postalCode;
    private String country;

    public Address(int id, String city, String street, String buildingNumber, String apartmentNumber, String postalCode, String country) {
        this.id = id;
        this.city = city;
        this.street = street;
        this.buildingNumber = buildingNumber;
        this.apartmentNumber = apartmentNumber;
        this.postalCode = postalCode;
        this.country = country;
    }

    public int getId() { return id; }
    public String getCity() { return city; }
    public String getStreet() { return street; }
    public String getBuildingNumber() { return buildingNumber; }
    public String getApartmentNumber() { return apartmentNumber; }
    public String getPostalCode() { return postalCode; }
    public String getCountry() { return country; }
}