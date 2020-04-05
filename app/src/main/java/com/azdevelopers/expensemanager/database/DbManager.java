package com.azdevelopers.expensemanager.database;

import android.provider.BaseColumns;

public final class DbManager {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DbManager() {}

    /* Inner class that defines the table contents */
    public static class TableInfo implements BaseColumns {
        public static final String TABLE_NAME = "expenses";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_LOCATION = "location";
    }
}