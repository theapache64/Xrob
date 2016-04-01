package com.theah64.xrob.api.database.tables;

import com.theah64.xrob.api.database.utils.InsertQuery;
import com.theah64.xrob.api.models.Delivery;

/**
 * Created by Shifar Shifz on 11/29/2015.
 */
public class Deliveries extends BaseTable<Delivery> {


    public static final String COLUMN_ERROR = "error";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_DATA_TYPE = "data_type";
    private static final String TABLE_DELIVERIES = "deliveries";

    private static Deliveries instance = new Deliveries();

    private Deliveries() {
    }

    public static Deliveries getInstance() {
        return instance;
    }

    @Override
    public Delivery get(String column, String value) {
        return null;
    }

    @Override
    public boolean add(Delivery delivery) {

        final String query = new InsertQuery()
                .insertInto(TABLE_DELIVERIES)
                .set(COLUMN_USER_ID, delivery.getUserId())
                .set(COLUMN_ERROR, delivery.hasError())
                .set(COLUMN_MESSAGE, delivery.getMessage())
                .set(COLUMN_DATA_TYPE, delivery.getDataType())
                .build();


        return false;
    }

    @Override
    public String get(String byColumn, String byValues, String columnToReturn) {
        return null;
    }

    @Override
    public boolean update(String whereColumn, String whereColumnValue, String updateColumn, String newUpdateColumnValue) {
        return false;
    }
}
