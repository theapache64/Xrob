package com.theah64.xrob.api.models;

/**
 * Created by theapache64 on 12/9/16.
 */
public class ClientVictimRelation {
    private final String id, clientId, victimId;

    public ClientVictimRelation(String id, String clientId, String victimId) {
        this.id = id;
        this.clientId = clientId;
        this.victimId = victimId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getVictimId() {
        return victimId;
    }
}
