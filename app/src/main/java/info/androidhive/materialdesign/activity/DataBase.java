package info.androidhive.materialdesign.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DataBase extends SQLiteOpenHelper {


    private static SQLiteDatabase mDb;

    private static final String TAG = "Database";
    private static final String DATABASE_NAME = "todo_app.db";
    private static final int DATABASE_VERSION = 1;
    private static final String USER_TABLE = "todos";
    private static final String DATA_TABLE = "data";
    private static final String GOAL_TABLE = "goal";

    /**
     * Table #1 - User information
     */
    // Table column name
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_HEIGHT = "heiht";
    public static final String COLUMN_DIET = "diete";
    public static final String COLUMN_LOOK = "look";

    // Create table User information
    private static final String DATABASE_CREATE = "create table "
            + USER_TABLE + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null, " + COLUMN_AGE + " text not null,"
            + COLUMN_WEIGHT  + " text not null,"
            + COLUMN_HEIGHT + " text not null,"
            + COLUMN_DIET + " text not null,"
            + COLUMN_LOOK + " text not null" + ");";

    /**
     * Table #2 - Users statistics
     */
    public static final String KEY_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_STEPS = "steps";
    public static final String COLUMN_DISTANCE = "distance";
    public static final String COLUMN_CALORIES = "calories";

    // Create table User information
    private static final String TABLE_DATA_CREATE = "create table "
            + DATA_TABLE + "(" + KEY_ID
            + " integer primary key autoincrement, "
            + COLUMN_DATE + " text not null,"
            + COLUMN_STEPS + " text not null,"
            + COLUMN_DISTANCE  + " text not null,"
            + COLUMN_CALORIES + " text not null);";

    /**
     * Table #3 - User goals
     */
    public static final String GOAL_DATE = "date";
    public static final String GOAL_DISTANCE = "distance";

    // Create table User information
    private static final String GOAL_TABLE_CREATE = "create table "
            + GOAL_TABLE + "(" + KEY_ID
            + " integer primary key autoincrement, "
            + GOAL_DATE + " int not null,"
            + GOAL_DISTANCE + " text not null);";

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(DATABASE_CREATE);
        db.execSQL(TABLE_DATA_CREATE);
        db.execSQL(GOAL_TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        Log.w(DataBase.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS todos");
        db.execSQL("DROP TABLE IF EXISTS" + DATA_TABLE);
        onCreate(db);
    }

    /**
     * Создаёт новый элемент списка дел. Если создан успешно - возвращается
     * номер строки rowId, иначе -1
     */
    public long createNewTodo(String name, String age, String weight, String heiht, String diete, String look)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = createContentValues(name, age, weight, heiht, diete, look);

        long row = db.insert(USER_TABLE, null, initialValues);
        db.close();
        return row;
    }

    /**
     * Update user information records
     */
    public boolean updateTodo(long rowId, String name, String age, String weight, String heiht, String diete, String look) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updateValues = createContentValues(name, age, weight, heiht, diete, look);
        return db.update(USER_TABLE, updateValues, COLUMN_ID + "=" + rowId, null) > 0;
    }

    /**
     * Delete user information record from database
     */
    public void deleteTodo(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USER_TABLE, COLUMN_ID + "=" + rowId, null);
        db.close();
    }

    /**
     * Возвращает курсор со всеми элементами списка дел
     * @return курсор с результатами всех записей
     */
    public Cursor getAllTodos() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(USER_TABLE, new String[] { COLUMN_ID,
                        COLUMN_NAME, COLUMN_AGE, COLUMN_WEIGHT,COLUMN_HEIGHT,COLUMN_DIET    , COLUMN_LOOK }, null,
                null, null, null, null);
    }

    /**
     * Return cursor with requested record for user information
     */
    public Cursor getTodo(long rowId) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(true, USER_TABLE,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_AGE,
                        COLUMN_WEIGHT, COLUMN_HEIGHT, COLUMN_DIET, COLUMN_LOOK}, COLUMN_ID + "=" + rowId, null,
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
                                              String weight, String heiht, String diete, String look) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_AGE, age);
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_HEIGHT, heiht);
        values.put(COLUMN_DIET, diete);
        values.put(COLUMN_LOOK, look);

        return values;
    }

    // Table 2 - Data records operations

    /**
     * Create new Data record with current time
     * Row number is equal rowId, else -1
     */
    public long createNewDataRecord(String steps, String distance, String calories)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        String time = getDateTime();

        initialValues.put(COLUMN_DATE, time);
        initialValues.put(COLUMN_STEPS, steps);
        initialValues.put(COLUMN_DISTANCE, distance);
        initialValues.put(COLUMN_CALORIES, calories);

        long row = db.insert(DATA_TABLE, null, initialValues);
        db.close();

        return row;
    }

    /**
     * Create new Data record
     * Row number is equal rowId, else -1
     */
    public long createNewDataRecord(String time, String steps, String distance, String calories)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();

        initialValues.put(COLUMN_DATE, time);
        initialValues.put(COLUMN_STEPS, steps);
        initialValues.put(COLUMN_DISTANCE, distance);
        initialValues.put(COLUMN_CALORIES, calories);

        long row = db.insert(DATA_TABLE, null, initialValues);
        db.close();

        return row;
    }

    /**
     * Update user data records
     */
    public boolean updateDataRecord(long rowId, String steps, String distance, String calories) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updateValues = new ContentValues();
        String time = getDateTime();
        Log.d(TAG, "Updating " + time + " RowID:" + String.valueOf(rowId));
        updateValues.put(COLUMN_DATE, time);
        updateValues.put(COLUMN_STEPS, steps);
        updateValues.put(COLUMN_DISTANCE, distance);
        updateValues.put(COLUMN_CALORIES, calories);

        return db.update(DATA_TABLE, updateValues, KEY_ID + "=" + rowId, null) > 0;
    }

    /**
     * Delete user data record from database
     */
    public void deleteDataRecord(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATA_TABLE, KEY_ID + "=" + rowId, null);
        db.close();
    }

    /**
     * Return cursor with requested record for user information
     * @return Cursor with requested record for user data
     * @throws SQLException
     */
    public Cursor getLastDataRecord() throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(true, DATA_TABLE,
                new String[]{KEY_ID, COLUMN_DATE, COLUMN_STEPS,
                        COLUMN_DISTANCE, COLUMN_CALORIES}, null, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToPosition(mCursor.getCount() - 1);
        }
        return mCursor;
    }

    /**
     * Return cursor with requested record for user information
     * @return Cursor with requested record for user data
     * @throws SQLException
     */
    public Cursor getLastNDataRecord(int n) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(true, DATA_TABLE,
                new String[]{KEY_ID, COLUMN_DATE, COLUMN_STEPS,
                        COLUMN_DISTANCE, COLUMN_CALORIES}, null, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToPosition(mCursor.getCount() - (n + 1));
        }
        return mCursor;
    }

    /**
     * Return all data records
     * @return cursor with all data records
     */
    public Cursor getAllDataRecords() {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.query(DATA_TABLE, new String[] { KEY_ID,
                        COLUMN_DATE, COLUMN_STEPS, COLUMN_DISTANCE, COLUMN_CALORIES }, null,
                null, null, null, null);
    }
    /*
     * Create and write new key-value for data record
     */
    private ContentValues createContentValuesData(String date, String steps,
                                              String distance, String calories) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_STEPS, steps);
        values.put(COLUMN_DISTANCE, distance);
        values.put(COLUMN_CALORIES, calories);

        return values;
    }

     /**
     * Date and time formatting function
     * @return - formatted date and time in String value
     */
    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    /// Table #3 - Goal table CRUD operations
    public void printTables() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> dirArray = new ArrayList<String>();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        while(c.moveToNext()){
            String s = c.getString(0);
            if(s.equals("android_metadata"))
            {
                System.out.println("Get Metadata");
                continue;
            }
            else
            {
                dirArray.add(s);
            }
        }
        Log.d("DB", dirArray.toString());
    }

    public long insertGoal(long timestamp, String distance)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GOAL_DATE, timestamp);
        values.put(GOAL_DISTANCE, distance);
        long row = db.insert(GOAL_TABLE, null, values);
        db.close();
        return row;
    }

    public Cursor getAllGoals() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(GOAL_TABLE, new String[] { KEY_ID, GOAL_DATE, GOAL_DISTANCE }, null,
                null, null, null, null);
    }

    /**
     * Return cursor with requested record for user information
     * @return Cursor with requested record for user data
     * @throws SQLException
     */
    public Cursor getEarliestGoalRecord() throws SQLException {
        String query_to_fetch_earliest="select *  from "+GOAL_TABLE+" order  by " + GOAL_DATE + " ASC ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query_to_fetch_earliest, null);
        return cursor;
    }

   /* public void deleteEarliestGoalRecord() throws SQLException {
        String query_to_fetch_earliest="select *  from "+GOAL_TABLE+" order  by " + GOAL_DATE + " ASC ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query_to_fetch_earliest, null);
        db.delete(cursor);*/

    public void deleteFirstRow()
   {

       String query_to_fetch_earliest="select *  from "+GOAL_TABLE+" order  by " + GOAL_DATE + " ASC ";
       SQLiteDatabase db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(query_to_fetch_earliest, null);

       if(cursor.moveToFirst()) {
           String rowId = cursor.getString(cursor.getColumnIndex(KEY_ID));

           db.delete(GOAL_TABLE, KEY_ID + "=?", new String[]{rowId});
       }

   }

}

    /*public void deleteDataRecord(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATA_TABLE, KEY_ID + "=" + rowId, null);
        db.close();
    }*/
