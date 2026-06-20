package model;

import java.sql.Timestamp;

public class TripRequest {
    private Long id;
    private Long customerId;
    private String pickupAddress;
    private String pickupCity;
    private String pickupState;
    private String destinationAddress;
    private String destinationCity;
    private String destinationState;
    private boolean isRoundTrip;
    private int passengerCount;
    private Timestamp travelDatetime;
    private String status; // 'PENDING', 'ACCEPTED', 'EXPIRED', 'CANCELLED'
    private Timestamp createdAt;

    // Default constructor
    public TripRequest() {}

    // Constructor for creating a new request
    public TripRequest(Long customerId, String pickupAddress, String pickupCity, String pickupState,
                       String destinationAddress, String destinationCity, String destinationState,
                       boolean isRoundTrip, int passengerCount, Timestamp travelDatetime) {
        this.customerId = customerId;
        this.pickupAddress = pickupAddress;
        this.pickupCity = pickupCity;
        this.pickupState = pickupState;
        this.destinationAddress = destinationAddress;
        this.destinationCity = destinationCity;
        this.destinationState = destinationState;
        this.isRoundTrip = isRoundTrip;
        this.passengerCount = passengerCount;
        this.travelDatetime = travelDatetime;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getPickupAddress() { return pickupAddress; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }

    public String getPickupCity() { return pickupCity; }
    public void setPickupCity(String pickupCity) { this.pickupCity = pickupCity; }

    public String getPickupState() { return pickupState; }
    public void setPickupState(String pickupState) { this.pickupState = pickupState; }

    public String getDestinationAddress() { return destinationAddress; }
    public void setDestinationAddress(String destinationAddress) { this.destinationAddress = destinationAddress; }

    public String getDestinationCity() { return destinationCity; }
    public void setDestinationCity(String destinationCity) { this.destinationCity = destinationCity; }

    public String getDestinationState() { return destinationState; }
    public void setDestinationState(String destinationState) { this.destinationState = destinationState; }

    public boolean isRoundTrip() { return isRoundTrip; }
    public void setRoundTrip(boolean roundTrip) { isRoundTrip = roundTrip; }

    public int getPassengerCount() { return passengerCount; }
    public void setPassengerCount(int passengerCount) { this.passengerCount = passengerCount; }

    public Timestamp getTravelDatetime() { return travelDatetime; }
    public void setTravelDatetime(Timestamp travelDatetime) { this.travelDatetime = travelDatetime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
