package ca.gc.inspection.directinfo;

import android.content.Intent;
import android.provider.ContactsContract;

import java.io.Serializable;

public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;                    // (d|e) + b + a + (f|g) (prefix + first + last + suffix)
    private String title;                   // (h|i)
    private String phone;                   // j
    private String email;                   // m
    private String address;                 // (n|o)
    private String country;                 // (p|q)
    private String province;                // (r|s)
    private String city;                    // (t|u)
    private String postalCode;              // v
    private String deptAcronym;             // w
    private String deptName;                //(x|y)
    private String orgAcronym;              // z
    private String orgName;                 // (aa|ab)


    Person(String abdefg, String hi, String j, String m, String no,
           String pq, String rs, String tu, String v, String w, String xy, String z,
           String aaab) {
        this.name = abdefg;
        this.title = hi;
        this.phone = j;
        this.email = m;
        this.address = no;
        this.country = pq;
        this.province = rs;
        this.city = tu;
        this.postalCode = v;
        this.deptAcronym = w;
        this.deptName = xy;
        this.orgAcronym = z;
        this.orgName = aaab;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    String getTitle() { return title; }

    String getDept() { return deptName; }
    String getDeptAndOrg() {return  deptAcronym.split("-")[0] + " - " + orgAcronym.split("-")[0]; }

    String getOrg() { return orgName; }


    String getPhone() { return (formatNum(phone)) ;}

    String getEmail() {return email;}

    String getAddress() {
        StringBuilder physicalAddress = new StringBuilder();

        if (!address.equals("null"))
            physicalAddress.append(address).append("\n");
        if (!city.equals("null"))
            physicalAddress.append(city).append(", ");
        if (!province.equals("null"))
            physicalAddress.append(province).append(" ");
        if (!postalCode.equals("null"))
            physicalAddress.append(postalCode).append("\n");
        if (!country.equals("null"))
            physicalAddress.append(country);
        return physicalAddress.toString();
    }


    Intent addToContacts() {
        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

        intent.putExtra(ContactsContract.Intents.Insert.NAME, this.name);
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, this.email)
                .putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, this.phone)
                .putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, "CFIA");
        intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, this.title);

        return intent;
    }

    //for google map searching
    String getPhysicalMapInfo() {
       return this.address + this.city + this.province;
    }

    /** *** UPDATE March 19, 2019 ***
    Name: formatNum
    Author: Edison Mendoza
    Parameters: num - Phone number from database
    Return: Formatted number to display to app
    Description: Helper function to format and display numbers properly to work with the inconsistent format of the database
    */
    private String formatNum (String num){
        char[] arr = num.toCharArray();
        StringBuilder numOnly = new StringBuilder();
        for (char c : arr){
            if (Character.isDigit(c)) {
                numOnly.append(c);
            }
        }
        String ret = (numOnly.length() < 10) ? "null" : ("(" + numOnly.substring(0,3) + ") " +  numOnly.substring(3,6) + " " + numOnly.substring(6,10));
        if (numOnly.length() > 10) // means there is an extension number
            ret += " Ext. " + numOnly.substring(10);
        return ret;
    }
}