package analytics;

// Represents access count info for a sale and postcode
public class Analytics {
    private String saleId;
    private String postCode;
    private int saleAccessCount;
    private int postCodeAccessCount;

    // Full constructor
    public Analytics(String saleId, String postCode, int saleAccessCount, int postCodeAccessCount) {
        this.saleId = saleId;
        this.postCode = postCode;
        this.saleAccessCount = saleAccessCount;
        this.postCodeAccessCount = postCodeAccessCount;
    }

    // Constructor with only saleId and postcode, counts default to zero
    public Analytics(String saleId, String postCode) {
        this(saleId, postCode, 0, 0);
    }

    // No-arg constructor needed for JSON serialization/deserialization
    public Analytics() {
    }

    // Getters and setters
    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public int getSaleAccessCount() {
        return saleAccessCount;
    }

    public void setSaleAccessCount(int saleAccessCount) {
        this.saleAccessCount = saleAccessCount;
    }

    public int getPostCodeAccessCount() {
        return postCodeAccessCount;
    }

    public void setPostCodeAccessCount(int postCodeAccessCount) {
        this.postCodeAccessCount = postCodeAccessCount;
    }

    // Helper methods to increment counts
    public void incrementSaleAccessCount() {
        this.saleAccessCount++;
    }

    public void incrementPostCodeAccessCount() {
        this.postCodeAccessCount++;
    }
}
