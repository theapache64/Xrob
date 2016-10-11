package com.theah64.xrob.models;

import android.provider.ContactsContract;
import android.util.Log;

import java.util.List;

/**
 * Created by theapache64 on 4/9/16.
 */
public class Contact {

    private String id;
    private final String androidContactId;
    private final String name;
    private List<PhoneNumber> phoneNumbers;
    private final boolean isSynced;

    public Contact(String id, String androidContactId, String name, List<PhoneNumber> phoneNumbers, boolean isSynced) {
        this.id = id;
        this.androidContactId = androidContactId;
        this.name = name;
        this.phoneNumbers = phoneNumbers;
        this.isSynced = isSynced;
    }

    public String getId() {
        return id;
    }

    public String getAndroidContactId() {
        return androidContactId;
    }

    public String getName() {
        return name;
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumbers=" + phoneNumbers +
                ", isSynced=" + isSynced +
                '}';
    }

    public static class PhoneNumber {

        private static final String TYPE_HOME = "HOME";
        private static final String TYPE_WORK = "WORK";
        private static final String TYPE_MOBILE = "MOBILE";
        private static final String TYPE_FAX_WORK = "FAX_WORK";
        private static final String TYPE_FAX_HOME = "FAX_HOME";
        private static final String TYPE_PAGER = "PAGER";
        private static final String TYPE_OTHER = "OTHER";
        private static final String TYPE_CALLBACK = "CALLBACK";
        private static final String TYPE_CAR = "CAR";
        private static final String TYPE_COMPANY_MAIN = "COMPANY_MAIN";
        private static final String TYPE_ISDN = "ISDN";
        private static final String TYPE_MAIN = "MAIN";
        private static final String TYPE_OTHER_FAX = "OTHER_FAX";
        private static final String TYPE_RADIO = "RADIO";
        private static final String TYPE_TELEX = "TELEX";
        private static final String TYPE_TTY_TDD = "TTY_TDD";
        private static final String TYPE_WORK_MOBILE = "WORK_MOBILE";
        private static final String TYPE_WORK_PAGER = "WORK_PAGER";
        private static final String TYPE_ASSISTANT = "ASSISTANT";
        private static final String TYPE_MMS = "MMS";
        private static final String TYPE_OTHER_2 = "OTHER_2";
        private static final String X = Contact.class.getSimpleName();
        private final String contactId, phone, phoneType;

        public PhoneNumber(String contactId, String phone, String phoneType) {
            this.contactId = contactId;
            this.phone = phone;
            this.phoneType = phoneType;
        }

        public String getPhoneType() {
            return phoneType;
        }

        public String getPhone() {
            return phone;
        }

        public String getContactId() {
            return contactId;
        }

        /**
         * To return string type from ContactsContract.CommonDataKinds.Phone.TYPE
         */
        public static String getTypeString(int commonDataKindPhoneType) {

            switch (commonDataKindPhoneType) {
                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                    return TYPE_HOME;
                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                    return TYPE_MOBILE;
                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                    return TYPE_WORK;
                case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                    return TYPE_FAX_WORK;
                case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                    return TYPE_FAX_HOME;
                case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER:
                    return TYPE_PAGER;
                case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                    return TYPE_OTHER;
                case ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK:
                    return TYPE_CALLBACK;
                case ContactsContract.CommonDataKinds.Phone.TYPE_CAR:
                    return TYPE_CAR;
                case ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN:
                    return TYPE_COMPANY_MAIN;
                case ContactsContract.CommonDataKinds.Phone.TYPE_ISDN:
                    return TYPE_ISDN;
                case ContactsContract.CommonDataKinds.Phone.TYPE_MAIN:
                    return TYPE_MAIN;
                case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER_FAX:
                    return TYPE_OTHER_FAX;
                case ContactsContract.CommonDataKinds.Phone.TYPE_RADIO:
                    return TYPE_RADIO;
                case ContactsContract.CommonDataKinds.Phone.TYPE_TELEX:
                    return TYPE_TELEX;
                case ContactsContract.CommonDataKinds.Phone.TYPE_TTY_TDD:
                    return TYPE_TTY_TDD;
                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:
                    return TYPE_WORK_MOBILE;
                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_PAGER:
                    return TYPE_WORK_PAGER;
                case ContactsContract.CommonDataKinds.Phone.TYPE_ASSISTANT:
                    return TYPE_ASSISTANT;
                case ContactsContract.CommonDataKinds.Phone.TYPE_MMS:
                    return TYPE_MMS;
                default:
                    return TYPE_OTHER_2;
            }
        }

        @Override
        public String toString() {
            return "PhoneNumber{" +
                    "contactId='" + contactId + '\'' +
                    ", phone='" + phone + '\'' +
                    ", phoneType='" + phoneType + '\'' +
                    '}';
        }
    }
}
