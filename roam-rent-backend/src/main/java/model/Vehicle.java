package model;

import java.sql.Timestamp;

public class Vehicle {
    private Long id;
    private Long ownerId;
    private String company;
    private String model;
    private String licensePlate;
    private int seatingCapacity;
    private boolean isActive;
    private Timestamp createdAt;

    // Default constructor
    public Vehicle() {}

    // Constructor for registering a new vehicle
    public Vehicle(Long ownerId, String company, String model, String licensePlate, int seatingCapacity) {
        this.ownerId = ownerId;
        this.company = company;
        this.model = model;
        this.licensePlate = licensePlate;
        this.seatingCapacity = seatingCapacity;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public int getSeatingCapacity() { return seatingCapacity; }
    public void setSeatingCapacity(int seatingCapacity) { this.seatingCapacity = seatingCapacity; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
