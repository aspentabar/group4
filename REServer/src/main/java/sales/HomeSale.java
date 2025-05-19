package sales;

// Simple class to provide test data in SalesDAO

public class HomeSale {
    public String propertyId;
    public String downloadDate;
    public String councilName;
    public String purchasePrice;
    public String address;
    public String postCode;
    public String propertyType;
    public String strataLotNumber;
    public String propertyName;
    public String areaType;
    public String contractDate;
    public String settlementDate;
    public String zoning;
    public String natureOfProperty;
    public String primaryPurpose;
    public String legalDescription;
    public int count;
    public int postCodeCount;


    public HomeSale(String propertyId, String downloadDate, String councilName, String purchasePrice, String address, 
    String postCode, String propertyType, String strataLotNumber, String propertyName, String areaType, String contractDate, 
    String settlementDate, String zoning, String natureOfProperty, String primaryPurpose, String legalDescription, int count, int postCodeCount) {
        this.propertyId = propertyId;
        this.downloadDate = downloadDate;
        this.councilName = councilName;
        this.purchasePrice = purchasePrice;
        this.address = address;
        this.postCode = postCode;
        this.propertyType = propertyType;
        this.strataLotNumber = strataLotNumber;
        this.propertyName = propertyName;
        this.areaType = areaType;
        this.contractDate = contractDate;
        this.settlementDate = settlementDate;
        this.zoning = zoning;
        this.natureOfProperty = natureOfProperty;
        this.primaryPurpose = primaryPurpose;
        this.legalDescription = legalDescription;
        this.count = count;
        this.postCodeCount = postCodeCount;
    }

    public HomeSale(String propertyId, String postCode, String purchasePrice, int count) {
        this.propertyId = propertyId;
        this.postCode = postCode;
        this.purchasePrice = purchasePrice;
        this.count = count;
    }
    
    public int getCount(){
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPostCodeCount(){
        return postCodeCount;
    }

    public void setPostCodeCount(int postCodeCount) {
        this.postCodeCount = postCodeCount;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(String downloadDate) {
        this.downloadDate = downloadDate;
    }

    public String getCouncilName() {
        return councilName;
    }

    public void setCouncilName(String councilName) {
        this.councilName = councilName;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getStrataLotNumber() {
        return strataLotNumber;
    }

    public void setStrataLotNumber(String strataLotNumber) {
        this.strataLotNumber = strataLotNumber;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public String getContractDate() {
        return contractDate;
    }

    public void setContractDate(String contractDate) {
        this.contractDate = contractDate;
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getZoning() {
        return zoning;
    }

    public void setZoning(String zoning) {
        this.zoning = zoning;
    }

    public String getNatureOfProperty() {
        return natureOfProperty;
    }

    public void setNatureOfProperty(String natureOfProperty) {
        this.natureOfProperty = natureOfProperty;
    }

    public String getPrimaryPurpose() {
        return primaryPurpose;
    }

    public void setPrimaryPurpose(String primaryPurpose) {
        this.primaryPurpose = primaryPurpose;
    }

    public String getLegalDescription() {
        return legalDescription;
    }

    public void setLegalDescription(String legalDescription) {
        this.legalDescription = legalDescription;
    }




    // needed for JSON conversion
    public HomeSale() {}


}
