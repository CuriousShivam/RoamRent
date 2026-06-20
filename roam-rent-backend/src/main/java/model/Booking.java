package model;

import java.sql.Timestamp;

public class Booking {
    private Long id;
    private Long requestId;
    private Long offerId;
    private String paymentStatus; // 'PENDING', 'PAID', 'FAILED'
    private String tripStatus;    // 'SCHEDULED', 'ONGOING', 'COMPLETED', 'CANCELLED'
    private Timestamp confirmedAt;

    // Default constructor
    public Booking() {}

    // Constructor for creating a new confirmation entry
    public Booking(Long requestId, Long offerId) {
        this.requestId = requestId;
        this.offerId = offerId;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }

    public Long getOfferId() { return offerId; }
    public void setOfferId(Long offerId) { this.offerId = offerId; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getTripStatus() { return tripStatus; }
    public void setTripStatus(String tripStatus) { this.tripStatus = tripStatus; }

    public Timestamp getConfirmedAt() { return confirmedAt; }
    public void setConfirmedAt(Timestamp confirmedAt) { this.confirmedAt = confirmedAt; }
}
