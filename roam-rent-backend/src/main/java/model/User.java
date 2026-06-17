package model;
import java.sql.Timestamp;

public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String role; // "CUSTOMER" or "DRIVER"
    private String address;
    private String city;
    private String state;
    private Timestamp createdAt;

    // Default constructor
    public User() {}

    // Constructor for registering a new user (without id and createdAt)
    public User(String name, String email, String password, String phone, String role, String address, String city, String state) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.address = address;
        this.city = city;
        this.state = state;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "User Info: " + this.name + " " + this.email + " " + this.phone;

    }


}