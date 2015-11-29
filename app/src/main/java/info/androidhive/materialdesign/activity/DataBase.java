package info.androidhive.materialdesign.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {

    private static SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "todo_app.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE = "todos";
    private static final String DATABASE_DATA_TABLE = "data";

    /**
     * Table #1 - User information
     */
    // Table column name
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_HEIHT = "heiht";
    public static final String COLUMN_DIETE = "diete";

    // Create table User information
    private static final String DATABASE_CREATE = "create table "
            + DATABASE_TABLE + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null, " + COLUMN_AGE + " text not null,"
            + COLUMN_WEIGHT  + " text not null,"
            + COLUMN_HEIHT + " text not null,"
            + COLUMN_DIETE + " text not null" + ");";

    /**
     * Table #2 - Users statistics
     */
    public static final String COLUMN_ID2 = "_id";
    public static final String COLUMN_DATE = "day";
    public static final String COLUMN_STEPS = "steps";
    public static final String COLUMN_DISTANCE = "distance";
    public static final String COLUMN_CALORIES = "calories";

    // Create table User information
    private static final String DATABASE_DATA_CREATE = "create table "
            + DATABASE_DATA_TABLE + "(" + COLUMN_ID2
            + " integer primary key autoincrement, " + COLUMN_DATE
            + " text not null, " + COLUMN_STEPS + " text not null,"
            + COLUMN_DISTANCE  + " text not null,"
            + COLUMN_CALORIES + " text not null,"+ ");";

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        Log.w(DataBase.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS todos");
        onCreate(db);
    }
    /**
     * Создаёт новый элемент списка дел. Если создан успешно - возвращается
     * номер строки rowId, иначе -1
     */
    public long createNewTodo(String name, String age, String weight, String heiht, String diete)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = createContentValues(name, age, weight, heiht, diete);

        long row = db.insert(DATABASE_TABLE, null, initialValues);
        db.close();
        return row;
    }

    /**
     * Создаёт новый элемент списка дел. Если создан успешно - возвращается
     * номер строки rowId, иначе -1
     */
    public long createNewDataRecord(String date, String steps, String distance, String calories)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = createContentValues(date, steps, distance, calories);

        long row = db.insert(DATABASE_TABLE, null, initialValues);
        db.close();
        return row;
    }

    /**
     * Update user inforamtion records
     */
    public boolean updateTodo(long rowId, String name, String age, String weight, String heiht, String diete) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updateValues = createContentValues(name, age, weight, heiht, diete);
        return db.update(DATABASE_TABLE, updateValues, COLUMN_ID + "=" + rowId, null) > 0;
    }

    /**
     * Update user inforamtion records
     */
    public boolean updateDataRecords(long rowId, String date, String steps, String distance, String calories) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updateValues = createContentValues(date, steps, distance, calories);
        return db.update(DATABASE_DATA_TABLE, updateValues, COLUMN_ID + "=" + rowId, null) > 0;
    }

    /**
     * Delete user information record from database
     */
    public void deleteTodo(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE, COLUMN_ID + "=" + rowId, null);
        db.close();
    }

    /**
     * Delete user data record from database
     */
    public void deleteDataRecord(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_DATA_TABLE, COLUMN_ID2 + "=" + rowId, null);
        db.close();
    }

    /**
     * Возвращает курсор со всеми элементами списка дел
     * @return курсор с результатами всех записей
     */
    public Cursor getAllTodos() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(DATABASE_TABLE, new String[] { COLUMN_ID,
                        COLUMN_NAME, COLUMN_AGE, COLUMN_WEIGHT,COLUMN_HEIHT,COLUMN_DIETE }, null,
                null, null, null, null);
    }

    /**
     * Return cursor with requested record for user information
     */
    public Cursor getTodo(long rowId) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(true, DATABASE_TABLE,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_AGE,
                        COLUMN_WEIGHT, COLUMN_HEIHT, COLUMN_DIETE}, COLUMN_ID + "=" + rowId, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Return cursor with requested record for user information
     * @param rowId number of record in database
     * @return Cursor with requested record for user data
     * @throws SQLException
     */
    public Cursor getDataRecord(long rowId) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(true, DATABASE_DATA_TABLE,
                new String[]{COLUMN_ID2, COLUMN_DATE, COLUMN_STEPS,
                        COLUMN_DISTANCE, COLUMN_CALORIES}, COLUMN_ID2 + "=" + rowId, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /*
     * Create key-value pair and write it to database
     */
    private ContentValues createContentValues(String name, String age,
                                              String weight, String heiht, String diete) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_AGE, age);
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_HEIHT, heiht);
        values.put(COLUMN_DIETE, diete);

        return values;
    }

    /*
     * Create and write new key-value for data record
     */
    private ContentValues createContentValues(String date, String steps,
                                              String distance, String calories) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_STEPS, steps);
        values.put(COLUMN_DISTANCE, distance);
        values.put(COLUMN_CALORIES, calories);

        return values;
    }
}