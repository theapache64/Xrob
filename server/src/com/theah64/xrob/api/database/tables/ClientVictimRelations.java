package com.theah64.xrob.api.database.tables;

import com.theah64.xrob.api.models.ClientVictimRelation;

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

    }
}
