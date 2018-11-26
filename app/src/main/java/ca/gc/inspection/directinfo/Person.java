package ca.gc.inspection.directinfo;

import java.io.Serializable;

public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;                    // a + b
    private String email;                   // g
    private String phone;                   // e
    private String title;                   // d
    private String mobile;                  // f
    private String postalStreetNumber;      // h
    private String postalStreetName;        // i
    private String postalUnitType;          // j
    private String postalUnitID;            // k
    private String postOfficeBox;           // l
    private String postalCity;              // m
    private String postalProvince;          // n
    private String postalCode;              // p
    private String buildingName;            // q
    private String floor;                   // r
    private String roomNumber;              // s
    private String physicalStreetNumber;    // t
    private String physicalStreetName;      // u
    private String physicalUnitType;        // v
    private String physicalUnitID;          // w
    private String physicalCity;            // x
    private String physicalProvince;        // y
    private String country = "Canada";

    public Person(String name, String email, String phone, String title, String mobile, String postalStreetNumber,
                  String postalStreetName, String postalUnitType, String postalUnitID, String postOfficeBox,
                  String postalCity, String postalProvince, String postalCode, String buildingName, String floor,
                  String roomNumber, String physicalStreetNumber, String physicalStreetName, String physicalUnitType,
                  String physicalUnitID, String physicalCity, String physicalProvince) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.title = title;
        this.mobile = mobile;
        this.postalStreetNumber = postalStreetNumber;
        this.postalStreetName = postalStreetName;
        this.postalUnitType = postalUnitType;
        this.postalUnitID = postalUnitID;
        this.postOfficeBox = postOfficeBox;
        this.postalCity = postalCity;
        this.postalProvince = postalProvince;
        this.postalCode = postalCode;
        this.buildingName = buildingName;
        this.floor = floor;
        this.roomNumber = roomNumber;
        this.physicalStreetNumber = physicalStreetNumber;
        this.physicalStreetName = physicalStreetName;
        this.physicalUnitType = physicalUnitType;
        this.physicalUnitID = physicalUnitID;
        this.physicalCity = physicalCity;
        this.physicalProvince = physicalProvince;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPhysicalAddress () {
        StringBuilder physicalAddress = new StringBuilder();

        if (buildingName != null && !buildingName.isEmpty())
            physicalAddress.append(buildingName).append("\n");
        if (floor != null && !floor.isEmpty())
            physicalAddress.append(floor).append(", ");
        if (roomNumber != null && !roomNumber.isEmpty())
            physicalAddress.append("Room ").append(roomNumber).append("\n");
        if (physicalStreetNumber != null && !physicalStreetNumber.isEmpty())
            physicalAddress.append(physicalStreetNumber).append(" ").append(physicalStreetName);
        if (physicalUnitType != null && !physicalUnitType.isEmpty())
            physicalAddress.append(", ").append(physicalUnitType).append(" ").append(physicalUnitID);
        if (physicalCity != null && !physicalCity.isEmpty())
            physicalAddress.append("\n").append(physicalCity).append(", ").append(physicalProvince).append("\n");
        if (postalCode != null && !postalCode.isEmpty())
            physicalAddress.append(postalCode).append("\n");
        physicalAddress.append(country);


        return physicalAddress.toString();
    }

    public String getPostalAddress(){
        StringBuilder postalAddress = new StringBuilder();

        if (buildingName != null && !buildingName.isEmpty())
            postalAddress.append(buildingName).append("\n");
        if (floor != null && !floor.isEmpty())
            postalAddress.append(floor).append(", ");
        if (roomNumber != null && !roomNumber.isEmpty())
            postalAddress.append("Room ").append(roomNumber).append("\n");
        if (postalStreetNumber != null && !postalStreetNumber.isEmpty())
            postalAddress.append(postalStreetNumber).append(" ").append(postalStreetName);
        if (postalUnitType != null && !postalUnitType.isEmpty())
            postalAddress.append(", ").append(postalUnitType).append(" ").append(postalUnitID);
        if (postOfficeBox != null && !postOfficeBox.isEmpty())
            postalAddress.append("\n").append(postOfficeBox);
        if (postalCity != null && !postalCity.isEmpty())
            postalAddress.append("\n").append(postalCity).append(", ").append(postalProvince).append("\n");
        if (postalCode != null && !postalCode.isEmpty())
            postalAddress.append(postalCode).append("\n");
        postalAddress.append(country);

        return postalAddress.toString();
    }
}
