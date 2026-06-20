package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class OfferResponse {
    private Long offerId;
    private Long requestId;
    private BigDecimal offeredPrice;
    private String offerStatus;
    private Timestamp createdAt;

    // Driver Details
    private Long driverId;
    private String driverName;
    private String driverPhone;

    // Vehicle Details
    private String carCompany;
    private String carModel;

    // Getters and Setters
    public Long getOfferId() { return offerId; }
    public void setOfferId(Long offerId) { this.offerId = offerId; }

    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }

    public BigDecimal getOfferedPrice() { return offeredPrice; }
    public void setOfferedPrice(BigDecimal offeredPrice) { this.offeredPrice = offeredPrice; }

    public String getOfferStatus() { return offerStatus; }
    public void setOfferStatus(String offerStatus) { this.offerStatus = offerStatus; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public String getDriverPhone() { return driverPhone; }
    public void setDriverPhone(String driverPhone) { this.driverPhone = driverPhone; }

    public String getCarCompany() { return carCompany; }
    public void setCarCompany(String carCompany) { this.carCompany = carCompany; }

    public String getCarModel() { return carModel; }
    public void setCarModel(String carModel) { this.carModel = carModel; }
}