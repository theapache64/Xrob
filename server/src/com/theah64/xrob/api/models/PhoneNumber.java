package com.theah64.xrob.api.models;

/**
 * Created by theapache64 on 11/9/16,7:40 AM.
 */
public class PhoneNumber {

    private String contactId;
    private final String phoneNumber;
    private final String type;

    public PhoneNumber(String contactId, String phoneNumber, String type) {
        this.contactId = contactId;
        this.phoneNumber = phoneNumber;
        this.type = type;
    }

    public String getContactId() {
        return contactId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "PhoneNumber{" +
                "contactId='" + contactId + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }
}
