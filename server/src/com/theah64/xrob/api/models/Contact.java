package com.theah64.xrob.api.models;

import java.util.List;

/**
 * Created by theapache64 on 4/9/16,11:39 AM.
 */
public class Contact {

    private final String userId, id, name;
    private List<PhoneNumber> phoneNumbers;

    public Contact(String userId, String id, String name, List<PhoneNumber> phone) {
        this.userId = userId;
        this.id = id;
        this.name = name;
        this.phoneNumbers = phone;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public List<PhoneNumber> getPhone() {
        return phoneNumbers;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumbers='" + phoneNumbers + '\'' +
                '}';
    }


    public static class PhoneNumber {

        private String contactId;
        private final String phone;
        private final String type;

        public PhoneNumber(String contactId, String phone, String type) {
            this.contactId = contactId;
            this.phone = phone;
            this.type = type;
        }

        public String getContactId() {
            return contactId;
        }

        public String getPhone() {
            return phone;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return "PhoneNumber{" +
                    "contactId='" + contactId + '\'' +
                    ", phone='" + phone + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }

        public void setContactId(String contactId) {
            this.contactId = contactId;
        }

    }
}
