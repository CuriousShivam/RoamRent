package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class TripOffer {
    private Long id;
    private Long requestId;
    private Long driverId;
    private Long carId;
    private BigDecimal offeredPrice;
    private String status; // 'SENT', 'ACCEPTED', 'REJECTED'
    private Timestamp createdAt;

    // Default constructor
    public TripOffer() {}

    // Constructor for creating a new offer
    public TripOffer(Long requestId, Long driverId, Long carId, BigDecimal offeredPrice) {
        this.requestId = requestId;
        this.driverId = driverId;
        this.carId = carId;
        this.offeredPrice = offeredPrice;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }

    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    public Long getCarId() { return carId; }
    public void setCarId(Long carId) { this.carId = carId; }

    public BigDecimal getOfferedPrice() { return offeredPrice; }
    public void setOfferedPrice(BigDecimal offeredPrice) { this.offeredPrice = offeredPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}